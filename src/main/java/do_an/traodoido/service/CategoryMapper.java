package do_an.traodoido.service;

import do_an.traodoido.dto.response.LabelDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryMapper {

    // Danh sách label Vision → category hệ thống
    private static final Map<String, String> MAP = Map.ofEntries(
            // ELECTRONICS
            Map.entry("Mobile phone", "electronics"),
            Map.entry("Smartphone", "electronics"),
            Map.entry("Cellphone", "electronics"),
            Map.entry("Telephone", "electronics"),
            Map.entry("Laptop", "electronics"),
            Map.entry("Notebook computer", "electronics"),
            Map.entry("Computer", "electronics"),
            Map.entry("Gadget", "electronics"),
            Map.entry("Electronics", "electronics"),
            Map.entry("Electronic device", "electronics"),
            Map.entry("Tablet computer", "electronics"),
            Map.entry("Monitor", "electronics"),
            Map.entry("TV", "electronics"),
            Map.entry("Remote control", "electronics"),
            Map.entry("Wireless device", "electronics"),
            Map.entry("Headphones", "electronics"),
            Map.entry("Earphones", "electronics"),
            Map.entry("Smartwatch", "electronics"),
            Map.entry("Camera", "electronics"),
            Map.entry("Video camera", "electronics"),
            Map.entry("Speaker", "electronics"),
            Map.entry("Router", "electronics"),
            Map.entry("Modem", "electronics"),
            Map.entry("Keyboard", "electronics"),
            Map.entry("Mouse", "electronics"),

            // FASHION
            Map.entry("Clothing", "fashion"),
            Map.entry("Apparel", "fashion"),
            Map.entry("Outerwear", "fashion"),
            Map.entry("Shirt", "fashion"),
            Map.entry("T-shirt", "fashion"),
            Map.entry("Pants", "fashion"),
            Map.entry("Shorts", "fashion"),
            Map.entry("Skirt", "fashion"),
            Map.entry("Dress", "fashion"),
            Map.entry("Coat", "fashion"),
            Map.entry("Jacket", "fashion"),
            Map.entry("Hoodie", "fashion"),
            Map.entry("Sweater", "fashion"),
            Map.entry("Textile", "fashion"),
            Map.entry("Uniform", "fashion"),
            Map.entry("Fashion accessory", "fashion"),
            Map.entry("Hat", "fashion"),
            Map.entry("Cap", "fashion"),

            // SHOES
            Map.entry("Footwear", "shoes"),
            Map.entry("Shoe", "shoes"),
            Map.entry("Sneaker", "shoes"),
            Map.entry("Running shoe", "shoes"),
            Map.entry("Boot", "shoes"),
            Map.entry("Sandals", "shoes"),
            Map.entry("Slipper", "shoes"),
            Map.entry("High heels", "shoes"),
            Map.entry("Athletic shoe", "shoes"),

            // BOOK
            Map.entry("Book", "book"),
            Map.entry("Publication", "book"),
            Map.entry("Textbook", "book"),
            Map.entry("Novel", "book"),
            Map.entry("Magazine", "book"),
            Map.entry("Reading", "book"),
            Map.entry("Paper", "book"),
            Map.entry("Book cover", "book"),

            // BAG
            Map.entry("Bag", "bag"),
            Map.entry("Backpack", "bag"),
            Map.entry("Handbag", "bag"),
            Map.entry("Tote bag", "bag"),
            Map.entry("Shoulder bag", "bag"),
            Map.entry("Luggage", "bag"),
            Map.entry("Suitcase", "bag"),
            Map.entry("Wallet", "bag"),
            Map.entry("Purse", "bag"),

            // FURNITURE
            Map.entry("Furniture", "furniture"),
            Map.entry("Table", "furniture"),
            Map.entry("Desk", "furniture"),
            Map.entry("Chair", "furniture"),
            Map.entry("Stool", "furniture"),
            Map.entry("Cabinet", "furniture"),
            Map.entry("Shelf", "furniture"),
            Map.entry("Lamp", "furniture"),
            Map.entry("Sofa", "furniture"),
            Map.entry("Bed frame", "furniture"),
            Map.entry("Drawer", "furniture"),

            // HOUSEWARE
            Map.entry("Home appliance", "houseware"),
            Map.entry("Appliance", "houseware"),
            Map.entry("Kitchen appliance", "houseware"),
            Map.entry("Kettle", "houseware"),
            Map.entry("Toaster", "houseware"),
            Map.entry("Microwave oven", "houseware"),
            Map.entry("Rice cooker", "houseware"),
            Map.entry("Frying pan", "houseware"),
            Map.entry("Cooking pot", "houseware"),
            Map.entry("Water bottle", "houseware"),
            Map.entry("Tableware", "houseware"),
            Map.entry("Cup", "houseware"),
            Map.entry("Plate", "houseware"),

            // TOY
            Map.entry("Toy", "toy"),
            Map.entry("Doll", "toy"),
            Map.entry("Action figure", "toy"),
            Map.entry("Lego", "toy"),
            Map.entry("Stuffed toy", "toy"),
            Map.entry("Game", "toy"),
            Map.entry("Puzzle", "toy"),
            Map.entry("Board game", "toy"),

            // SPORT
            Map.entry("Sports equipment", "sport"),
            Map.entry("Ball", "sport"),
            Map.entry("Soccer ball", "sport"),
            Map.entry("Basketball", "sport"),
            Map.entry("Tennis racket", "sport"),
            Map.entry("Badminton racket", "sport"),
            Map.entry("Helmet", "sport"),
            Map.entry("Skateboard", "sport"),

            // COSMETIC
            Map.entry("Cosmetics", "cosmetic"),
            Map.entry("Makeup", "cosmetic"),
            Map.entry("Skin care", "cosmetic"),
            Map.entry("Perfume", "cosmetic"),
            Map.entry("Lipstick", "cosmetic"),
            Map.entry("Nail polish", "cosmetic"),
            Map.entry("Beauty product", "cosmetic")
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

