package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.enums.CategoryStatus;
import do_an.traodoido.repository.CategoryRepository;
import do_an.traodoido.service.CategoryService;
import do_an.traodoido.util.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service;
    @Override
    public RestResponse<List<Category>> getAllCategories() {
        return RestResponse.<List<Category>>builder()
                .code(1000)
                .message("Lấy danh sách danh mục thành công")
                .data(categoryRepository.findAllByStatus(CategoryStatus.ACTIVE))
                .build();
    }

    @Override
    public RestResponse<List<Category>> getAllCategoriesAdmin() {
        return RestResponse.<List<Category>>builder()
                .code(1000)
                .message("Lấy danh sách danh mục thành công")
                .data(categoryRepository.findAll())
                .build();
    }


    @Override
    public RestResponse<String> createCategory(Category category, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadFile(image, "category");
            category.setImage(imageUrl);
        }
        category.setStatus(CategoryStatus.ACTIVE);

        categoryRepository.save(category);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Tạo danh mục thành công")
                .data("Tạo danh mục thành công")
                .build();
    }

    @Override
    public RestResponse<String> updateCategory(Long id, Category category, MultipartFile image) throws IOException {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));

        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }
        if (category.getStatus() != null) {
            existingCategory.setStatus(category.getStatus());
        }
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadFile(image, "category");
            existingCategory.setImage(imageUrl);
        }

        categoryRepository.save(existingCategory);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Cập nhật danh mục thành công")
                .data("Cập nhật danh mục thành công")
                .build();
    }

    @Override
    public RestResponse<String> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));
        category.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(category);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Xóa danh mục thành công")
                .data("Xóa danh mục thành công")
                .build();
    }
}
