package store.myproject.onlineshop.global.token;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import store.myproject.onlineshop.domain.customer.Customer;
import store.myproject.onlineshop.exception.AppException;
import store.myproject.onlineshop.global.utils.CookieUtils;
import store.myproject.onlineshop.global.utils.JwtUtils;
import store.myproject.onlineshop.domain.customer.repository.CustomerRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static store.myproject.onlineshop.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final CustomerRepository customerRepository;

    private final JwtUtils jwtUtils;

    private final String LOGIN = "/api/v1/customers/login";
    private final String LOGIN_URI = "/api/v1/customers/login";

    private final String JOIN_URI = "/api/v1/customers/join";


    @Value("${access-token-maxage}")
    public int accessTokenMaxAge;
    @Value("${refresh-token-maxage}")
    public int refreshTokenMaxAge;

    /**
     * Access Token 은 Header 에 담아서 보내고, Refresh Token 은 Cookie 에 담아서 보낸다.
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("jwtFilter 실행");
        log.info("request.getRequestURI() : {}", request.getRequestURI());
        log.info("request.getRequestURL() : {}", request.getRequestURL());

        // 메인페이지 요청일 때 토큰 검증을 하지 않는다.
        if (request.getRequestURI().equals("/")) {
            log.info("메인페이지 요청입니다.");
            filterChain.doFilter(request, response);
            return; // 필터가 더 이상 진행되지 않도록 리턴
        }

        // Login 요청일 때 토큰 검증을 하지 않는다.
        if (request.getRequestURI().equals(LOGIN_URI)
                || request.getRequestURI().equals(LOGIN)) {
            log.info("로그인 요청입니다.");
            filterChain.doFilter(request, response);
            return; // 필터가 더 이상 진행되지 않도록 리턴
        }


        Cookie[] cookie = request.getCookies();
        log.info("request.getCookies() : {}", (Object[]) request.getCookies());

        Optional<String> accessTokenAtCookie = CookieUtils.extractAccessToken(request);
        Optional<String> refreshTokenAtCookie = CookieUtils.extractRefreshToken(request);

        if (accessTokenAtCookie.isEmpty()) {
            throw new JwtException("AccessToken 없습니다.");
        }

        String accessToken = accessTokenAtCookie.get();

        if (jwtUtils.isValid(accessToken)) {
            throw new JwtException("잘못된 AccessToken 입니다.");
        }

        if (jwtUtils.isExpired(accessToken)) {
            throw new JwtException("만료된 AccessToken 입니다.");
        }

        String info = jwtUtils.getEmail(accessToken);

        // refresh Token 존재 여부 확인
        if (refreshTokenAtCookie.isEmpty()) {
            log.error("Refresh Token 없습니다.");
            log.info("request.getRequestURI() : {}", request.getRequestURI());
            log.info("request.getRequestURL() : {}", request.getRequestURL());
            throw new JwtException("Refresh Token 없습니다.");
        }

        String refreshToken = refreshTokenAtCookie.get();
        log.info("refreshToken : {}", refreshToken);

        // access Token 만료된 경우 -> refresh Token 검증
        if (jwtUtils.isExpired(refreshToken)) {
            // refresh Token 만료된 경우
            log.error("Refresh Token 만료");
            log.info("refreshToken : {}", refreshToken);
            throw new JwtException("만료된 RefreshToken 입니다. 다시 로그인 해주세요.");
        }

        // refresh Token 유효한 경우 -> access Token / refresh Token 재발급
        String newAccessToken = jwtUtils.createAccessToken(info);
        log.info("newAccessToken : {}", newAccessToken);
        String newRefreshToken = jwtUtils.createRefreshToken(info);
        log.info("newRefreshToken : {}", newRefreshToken);


        // 발급된 accessToken을 response cookie 에 저장
        CookieUtils.addAccessTokenAtCookie(response, newAccessToken);
        // 발급된 refreshToken을 response cookie 에 저장
        CookieUtils.addRefreshTokenAtCookie(response, newRefreshToken);

        Customer customer = customerRepository.findByEmail(info)
                .orElseThrow(() -> new AppException(CUSTOMER_NOT_FOUND, CUSTOMER_NOT_FOUND.getMessage()));

        // 유효성 검증 통과한 경우
        log.info("유효성 검증 통과! \n SecurityContextHolder 에 Authentication 객체를 저장합니다!");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority(customer.getCustomerRole().name())));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
