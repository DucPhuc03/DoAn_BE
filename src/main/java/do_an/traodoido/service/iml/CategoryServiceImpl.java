package do_an.traodoido.service.iml;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.repository.CategoryRepository;
import do_an.traodoido.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public RestResponse<List<Category>> getAllCategories() {
        return RestResponse.<List<Category>>builder()
                .code(1000)
                .message("Lấy danh sách danh mục thành công")
                .data(categoryRepository.findAll())
                .build();
    }

    @Override
    public RestResponse<String> createCategory(Category category) {
        categoryRepository.save(category);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Tạo danh mục thành công")
                .data("Tạo danh mục thành công")
                .build();
    }
}
