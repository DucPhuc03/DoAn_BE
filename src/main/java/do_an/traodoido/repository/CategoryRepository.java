package do_an.traodoido.repository;

import do_an.traodoido.entity.Category;
import do_an.traodoido.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByStatus(CategoryStatus status);

}
