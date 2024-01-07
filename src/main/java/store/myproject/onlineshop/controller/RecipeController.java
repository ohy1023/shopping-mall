package store.myproject.onlineshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import store.myproject.onlineshop.domain.MessageResponse;
import store.myproject.onlineshop.domain.Response;
import store.myproject.onlineshop.service.RecipeService;

@RequiredArgsConstructor
@RestController("/api/v1/recipes")
@Tag(name = "Recipe", description = "레시피 API")
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(summary = "좋아요 누르기")
    @PostMapping("/{id}/likes")
    public Response<MessageResponse> pushLike(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        return Response.success(recipeService.pushLike(id, email));
    }


    @Operation(summary = "좋아요 개수 조회")
    @GetMapping("/{id}/likes")
    public Response<Integer> countLike(@PathVariable Long id) {
        Integer likeCnt = recipeService.countLike(id);
        return Response.success(likeCnt);
    }

}
