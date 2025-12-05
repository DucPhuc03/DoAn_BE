package do_an.traodoido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "API quản lý danh mục sản phẩm")
public class CategoryController {
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "Tạo danh mục mới (Admin)", description = "Tạo một danh mục sản phẩm mới với hình ảnh. Chỉ dành cho Admin.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> createCategory(
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Category category = objectMapper.readValue(categoryJson, Category.class);
        return ResponseEntity.ok(categoryService.createCategory(category, image));
    }

    @Operation(summary = "Cập nhật danh mục (Admin)", description = "Cập nhật thông tin danh mục. Hình ảnh là tùy chọn. Chỉ dành cho Admin.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Category category = objectMapper.readValue(categoryJson, Category.class);
        return ResponseEntity.ok(categoryService.updateCategory(id, category, image));
    }

    @Operation(summary = "Xóa danh mục (Admin)", description = "Xóa một danh mục khỏi hệ thống. Chỉ dành cho Admin.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<RestResponse<String>> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @Operation(summary = "Lấy danh sách danh mục", description = "Lấy danh sách tất cả danh mục đang hoạt động. Không yêu cầu xác thực.")
    @GetMapping
    public ResponseEntity<RestResponse<List<Category>>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @Operation(summary = "Lấy danh sách tất cả danh mục (Admin)", description = "Lấy danh sách tất cả danh mục bao gồm cả các danh mục không hoạt động. Chỉ dành cho Admin.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/admin")
    public ResponseEntity<RestResponse<List<Category>>> getAllCategoriesAdmin() {

        return ResponseEntity.ok(categoryService.getAllCategoriesAdmin());
    }
}
