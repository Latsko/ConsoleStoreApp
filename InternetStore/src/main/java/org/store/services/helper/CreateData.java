package org.store.services.helper;

import org.store.entities.Category;
import org.store.entities.Product;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateData {
    private static final Path productsPath = Paths.get("InternetStore\\src\\main\\java\\org\\store\\data", "products.json");
    private static final Path categoriesPath = Paths.get("InternetStore\\src\\main\\java\\org\\store\\data", "categories.json");
    private static final Path ordersPath = Paths.get("InternetStore\\src\\main\\java\\org\\store\\data", "orders.json");

    public static Path getProductsPath() {
        return productsPath;
    }

    public static Path getCategoriesPath() {
        return categoriesPath;
    }

    public static Path getOrdersPath() {
        return ordersPath;
    }

    public static Category[] getCategories() {
        return new Category[]{
                new Category("TV", 0),
                new Category("AGD", 1),
                new Category("Komputery", 2),
                new Category("Smartfony", 3),
                new Category("Sport", 4),
                new Category("Gaming", 5),
                new Category("Kamery", 6),
                new Category("Zabawki", 7)};
    }

    public static Product[][] getProducts() {
        final Category[] categories = getCategories();
        final Product[] tv = {
                new Product(0, 2000, "LG 32A5KQ", categories[0], 10),
                new Product(1, 3500, "LIN 32D1700", categories[0], 15),
                new Product(2, 600, "BLAUPUNKT", categories[0], 18),
                new Product(3, 490, "JVC LT-32VH2105", categories[0], 5),
                new Product(4, 900, "HISENSE 32A4BG", categories[0], 20),
                new Product(5, 1300, "PHILIPS 32PFS5905", categories[0], 10),
                new Product(6, 2300, "LG 32H57705", categories[0], 14)
        };

        final Product[] agd = {
                new Product(7, 3399, "Lodówka SAMSUNG RB38T774DB1", categories[1], 19),
                new Product(8, 2199, "Lodówka BEKO B5RCNA365HXB", categories[1], 8),
                new Product(9, 4000, "Lodówka HAIER A3FE737CMJ", categories[1], 14),
                new Product(10, 3200, "Pralka ELECTROLUX MEW7F349PXP", categories[1], 12),
                new Product(11, 3900, "Pralka SAMSUNG BESPOKE WW11BB744DGB", categories[1], 7),
                new Product(12, 1500, "Kuchnia GORENJE MEKS5121S ExtraSteam", categories[1], 15),
                new Product(13, 1850, "Kuchnia AMICA 6117GET3", categories[1], 17)
        };

        final Product[] computers = {
                new Product(14, 3670, "Komputer APPLE Mac Mini 2023", categories[2], 16),
                new Product(15, 4199, "Komputer MAD DOG MD5600X-A03V2", categories[2], 18),
                new Product(16, 3700, "Komputer ACER Nitro N50-640", categories[2], 10),
                new Product(17, 5500, "Laptop DELL G15 5525-8403", categories[2], 2),
                new Product(18, 2900, "Laptop LENOVO IdeaPad", categories[2], 9),
                new Product(19, 3500, "Laptop HP Victus", categories[2], 5),
                new Product(20, 1850, "Laptop HP 15-dw3123nw", categories[2], 17)
        };

        final Product[] smartphones = {
                new Product(21, 3670, "Smartfon SAMSUNG Galaxy S23", categories[3], 16),
                new Product(22, 4199, "Smartfon APPLE iPhone 14 128GB", categories[3], 28),
                new Product(23, 400, "Smartfon XIAOMI Redmi 12C", categories[3], 17),
                new Product(24, 5500, "Smartfon APPLE iPhone 13 256GB", categories[3], 21),
                new Product(25, 2900, "Smartfon XIAOMI Redmi Note 12", categories[3], 19),
                new Product(26, 3500, "Smartfon SAMSUNG Galaxy S20", categories[3], 15),
                new Product(27, 1850, "Smartfon SAMSUNG Galaxy A34", categories[3], 8)
        };

        final Product[] sports = {
                new Product(28, 6140, "Atlas EB FIT 9500", categories[4], 15),
                new Product(29, 120, "Sztanga HMS SGP15", categories[4], 18),
                new Product(30, 400, "Sztanga PURE 2 IMPROVE", categories[4], 17),
                new Product(31, 89, "Obciążniki na ręce i kostki EB FIT", categories[4], 21),
                new Product(32, 800, "Worek treningowy TIGUAR TI-FWB060", categories[4], 13),
                new Product(33, 350, "Ławka do ćwiczeń EB FIT", categories[4], 25),
                new Product(34, 850, "Ławka do ćwiczeń MARBO SPORT", categories[4], 4)
        };

        final Product[] gaming = {
                new Product(35, 2300, "Konsola SONY PlayStation 5 Digital", categories[5], 15),
                new Product(36, 2700, "Konsola SONY PlayStation 5", categories[5], 18),
                new Product(37, 2600, "Konsola MICROSOFT XBOX Series X", categories[5], 17),
                new Product(38, 1700, "Konsola MICROSOFT XBOX Series S", categories[5], 25),
                new Product(39, 1200, "Kierownica LOGITECH G920", categories[5], 3),
                new Product(40, 1550, "Konsola NINTENDO Switch Oled", categories[5], 27),
                new Product(41, 1850, "Gogle VR OCULUS Quest 2", categories[5], 4)
        };

        final Product[] cameras = {
                new Product(42, 1900, "Aparat CANON EOS 2000D", categories[6], 5),
                new Product(43, 8700, "Aparat NIKON D780 Body", categories[6], 15),
                new Product(44, 3600, "Aparat CANON EOS 250D", categories[6], 17),
                new Product(45, 3100, "Aparat SONY ZV-1 Czarny", categories[6], 8),
                new Product(46, 4200, "Aparat NIKON Coolpix P1000", categories[6], 13),
                new Product(47, 3300, "Aparat CANON EOS R50", categories[6], 7),
                new Product(48, 8450, "Aparat SONY Alpha A7 III", categories[6], 14)
        };

        final Product[] toys = {
                new Product(49, 300, "LEGO City Łódź podwodna badacza dna morskiego", categories[7], 25),
                new Product(50, 3700, "LEGO Star Wars AT-AT 75313", categories[7], 18),
                new Product(51, 2600, "LEGO Marvel Hulkbuster 76210", categories[7], 16),
                new Product(52, 310, "Zabawka drewniana UGEARS", categories[7], 4),
                new Product(53, 400, "Zabawka drewniana modele 3D Sejf", categories[7], 23),
                new Product(54, 100, "Wyrzutnia Nerf Elite 2.0", categories[7], 17),
                new Product(55, 85, "Wyrzutnia HASBRO", categories[7], 16)
        };
        return new Product[][]{tv, agd, computers, smartphones, sports, gaming, cameras, toys};
    }
}