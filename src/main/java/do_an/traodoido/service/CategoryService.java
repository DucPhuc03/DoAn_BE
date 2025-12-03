package do_an.traodoido.service;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    RestResponse<List<Category>> getAllCategories();

    RestResponse<List<Category>> getAllCategoriesAdmin();

    RestResponse<String> createCategory(Category category, MultipartFile image) throws IOException;

    RestResponse<String> updateCategory(Long id, Category category, MultipartFile image) throws IOException;

    RestResponse<String> deleteCategory(Long id);
}
