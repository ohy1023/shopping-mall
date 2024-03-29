//package store.myproject.onlineshop;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import store.myproject.onlineshop.domain.customer.Level;
//import store.myproject.onlineshop.domain.membership.dto.MemberShipCreateRequest;
//import store.myproject.onlineshop.service.MemberShipService;
//
//import java.math.BigDecimal;
//
//@Component
//@RequiredArgsConstructor
//public class InitDb {
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit();
//    }
//
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//        private final MemberShipService memberShipService;
//
//        public void dbInit() {
//            MemberShipCreateRequest bronze = MemberShipCreateRequest.builder()
//                    .baseline(new BigDecimal(0))
//                    .level(Level.BRONZE)
//                    .discountRate(new BigDecimal(0))
//                    .build();
//
//            memberShipService.saveMemberShip(bronze);
//
//            MemberShipCreateRequest silver = MemberShipCreateRequest.builder()
//                    .baseline(new BigDecimal(100000))
//                    .level(Level.SILVER)
//                    .discountRate(new BigDecimal("0.1"))
//                    .build();
//
//            memberShipService.saveMemberShip(silver);
//
//            MemberShipCreateRequest gold = MemberShipCreateRequest.builder()
//                    .baseline(new BigDecimal(1000000))
//                    .level(Level.GOLD)
//                    .discountRate(new BigDecimal("0.15"))
//                    .build();
//
//            memberShipService.saveMemberShip(gold);
//
//            MemberShipCreateRequest diamond = MemberShipCreateRequest.builder()
//                    .baseline(new BigDecimal(10000000))
//                    .level(Level.DIAMOND)
//                    .discountRate(new BigDecimal("0.3"))
//                    .build();
//
//            memberShipService.saveMemberShip(diamond);
//        }
//    }
//
//
//}