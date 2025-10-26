package do_an.traodoido.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreatePostDTO {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Item condition is required")
    private String itemCondition;
    
    private LocalDate postDate;
    
    @NotBlank(message = "Trade location is required")
    private String tradeLocation;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
