package do_an.traodoido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> createCategory(
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Category category = objectMapper.readValue(categoryJson, Category.class);
        return ResponseEntity.ok(categoryService.createCategory(category, image));
    }

    @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Category category = objectMapper.readValue(categoryJson, Category.class);
        return ResponseEntity.ok(categoryService.updateCategory(id, category, image));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<RestResponse<String>> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<Category>>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/admin")
    public ResponseEntity<RestResponse<List<Category>>> getAllCategoriesAdmin() {

        return ResponseEntity.ok(categoryService.getAllCategoriesAdmin());
    }
}
