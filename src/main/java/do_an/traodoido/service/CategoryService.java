package do_an.traodoido.service;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;

import java.util.List;

public interface CategoryService {
    RestResponse<List<Category>> getAllCategories();

    RestResponse<String> createCategory(Category category);
}
