package do_an.traodoido.util;

import do_an.traodoido.dto.response.LabelDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryMapper {

    // Danh sách label Vision → category hệ thống
    private static final Map<String, String> MAP = Map.<String, String>ofEntries(

            // 🔵 ELECTRONICS – Điện tử
            Map.entry("Mobile phone", "Điện tử"),
            Map.entry("Smartphone", "Điện tử"),
            Map.entry("Cellphone", "Điện tử"),
            Map.entry("Telephone", "Điện tử"),
            Map.entry("Laptop", "Điện tử"),
            Map.entry("Notebook computer", "Điện tử"),
            Map.entry("Computer", "Điện tử"),
            Map.entry("Gadget", "Điện tử"),
            Map.entry("Electronics", "Điện tử"),
            Map.entry("Electronic device", "Điện tử"),
            Map.entry("Tablet computer", "Điện tử"),
            Map.entry("Monitor", "Điện tử"),
            Map.entry("TV", "Điện tử"),
            Map.entry("Remote control", "Điện tử"),
            Map.entry("Wireless device", "Điện tử"),
            Map.entry("Headphones", "Điện tử"),
            Map.entry("Earphones", "Điện tử"),
            Map.entry("Smartwatch", "Điện tử"),
            Map.entry("Camera", "Điện tử"),
            Map.entry("Video camera", "Điện tử"),
            Map.entry("Speaker", "Điện tử"),
            Map.entry("Router", "Điện tử"),
            Map.entry("Modem", "Điện tử"),
            Map.entry("Keyboard", "Điện tử"),
            Map.entry("Mouse", "Điện tử"),

            // 🟣 FASHION – Thời trang
            Map.entry("Clothing", "Thời trang"),
            Map.entry("Apparel", "Thời trang"),
            Map.entry("Outerwear", "Thời trang"),
            Map.entry("Shirt", "Thời trang"),
            Map.entry("T-shirt", "Thời trang"),
            Map.entry("Pants", "Thời trang"),
            Map.entry("Shorts", "Thời trang"),
            Map.entry("Skirt", "Thời trang"),
            Map.entry("Dress", "Thời trang"),
            Map.entry("Coat", "Thời trang"),
            Map.entry("Jacket", "Thời trang"),
            Map.entry("Hoodie", "Thời trang"),
            Map.entry("Sweater", "Thời trang"),
            Map.entry("Textile", "Thời trang"),
            Map.entry("Uniform", "Thời trang"),
            Map.entry("Fashion accessory", "Thời trang"),
            Map.entry("Hat", "Thời trang"),
            Map.entry("Cap", "Thời trang"),

            // 🟠 SHOES – Giày
            Map.entry("Footwear", "Giày"),
            Map.entry("Shoe", "Giày"),
            Map.entry("Sneaker", "Giày"),
            Map.entry("Running shoe", "Giày"),
            Map.entry("Boot", "Giày"),
            Map.entry("Sandals", "Giày"),
            Map.entry("Slipper", "Giày"),
            Map.entry("High heels", "Giày"),
            Map.entry("Athletic shoe", "Giày"),

            // 🟡 BOOK – Sách
            Map.entry("Book", "Sách"),
            Map.entry("Publication", "Sách"),
            Map.entry("Textbook", "Sách"),
            Map.entry("Novel", "Sách"),
            Map.entry("Magazine", "Sách"),
            Map.entry("Reading", "Sách"),
            Map.entry("Paper", "Sách"),
            Map.entry("Book cover", "Sách"),

            // 🟤 BAG – Túi
            Map.entry("Bag", "Túi"),
            Map.entry("Backpack", "Túi"),
            Map.entry("Handbag", "Túi"),
            Map.entry("Tote bag", "Túi"),
            Map.entry("Shoulder bag", "Túi"),
            Map.entry("Luggage", "Túi"),
            Map.entry("Suitcase", "Túi"),
            Map.entry("Wallet", "Túi"),
            Map.entry("Purse", "Túi"),

            // 🟢 FURNITURE – Nội thất
            Map.entry("Furniture", "Nội thất"),
            Map.entry("Table", "Nội thất"),
            Map.entry("Desk", "Nội thất"),
            Map.entry("Chair", "Nội thất"),
            Map.entry("Stool", "Nội thất"),
            Map.entry("Cabinet", "Nội thất"),
            Map.entry("Shelf", "Nội thất"),
            Map.entry("Lamp", "Nội thất"),
            Map.entry("Sofa", "Nội thất"),
            Map.entry("Bed frame", "Nội thất"),
            Map.entry("Drawer", "Nội thất"),

            // 🟩 HOUSEWARE – Gia dụng
            Map.entry("Home appliance", "Gia dụng"),
            Map.entry("Appliance", "Gia dụng"),
            Map.entry("Kitchen appliance", "Gia dụng"),
            Map.entry("Kettle", "Gia dụng"),
            Map.entry("Toaster", "Gia dụng"),
            Map.entry("Microwave oven", "Gia dụng"),
            Map.entry("Rice cooker", "Gia dụng"),
            Map.entry("Frying pan", "Gia dụng"),
            Map.entry("Cooking pot", "Gia dụng"),
            Map.entry("Water bottle", "Gia dụng"),
            Map.entry("Tableware", "Gia dụng"),
            Map.entry("Cup", "Gia dụng"),
            Map.entry("Plate", "Gia dụng"),

            // 🧸 TOY – Đồ chơi
            Map.entry("Toy", "Đồ chơi"),
            Map.entry("Doll", "Đồ chơi"),
            Map.entry("Action figure", "Đồ chơi"),
            Map.entry("Lego", "Đồ chơi"),
            Map.entry("Stuffed toy", "Đồ chơi"),
            Map.entry("Game", "Đồ chơi"),
            Map.entry("Puzzle", "Đồ chơi"),
            Map.entry("Board game", "Đồ chơi"),

            // 🏅 SPORT – Thể thao
            Map.entry("Sports equipment", "Thể thao"),
            Map.entry("Ball", "Thể thao"),
            Map.entry("Soccer ball", "Thể thao"),
            Map.entry("Basketball", "Thể thao"),
            Map.entry("Tennis racket", "Thể thao"),
            Map.entry("Badminton racket", "Thể thao"),
            Map.entry("Helmet", "Thể thao"),
            Map.entry("Skateboard", "Thể thao"),

            // 💄 COSMETIC – Mỹ phẩm
            Map.entry("Cosmetics", "Mỹ phẩm"),
            Map.entry("Makeup", "Mỹ phẩm"),
            Map.entry("Skin care", "Mỹ phẩm"),
            Map.entry("Perfume", "Mỹ phẩm"),
            Map.entry("Lipstick", "Mỹ phẩm"),
            Map.entry("Nail polish", "Mỹ phẩm"),
            Map.entry("Beauty product", "Mỹ phẩm")
    );



    // ⭐ HÀM GỘP NHÃN → CATEGORY (PHẢI GỌI HÀM NÀY)
    public String aggregateCategory(List<LabelDTO> labels) {

        Map<String, Double> categoryScores = new HashMap<>();

        for (LabelDTO label : labels) {
            String category = MAP.get(label.getName());
            if (category != null) {
                categoryScores.merge(category, (double) label.getConfidence(), Double::sum);
            }
        }

        if (categoryScores.isEmpty()) return "unknown";

        // Lấy category có điểm cao nhất
        return categoryScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
}

