package org.store.services;

import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.CreateData;
import org.store.services.fileHandling.ReadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoryService {
    private final List<Category> categories;
    private final ReadData readData = new ReadData();

    public CategoryService() throws FileNotFoundException {
        File categoryFile = CreateData.getCategoriesPath().toFile();
        if (!categoryFile.exists()) {
            new CreateData().createCategories();
        }
        categories = new ArrayList<>(readData.readCategoriesFromFile());
    }

    public void showCategory() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj nazwę kategorii: ");
        String name = scanner.nextLine();

        List<Product> productList = readData.readProductsFromFile();
        // here I must update lastID from RAM, because every time I call
        // readProductsFromFile(), I invoke Category constructor, which
        // increments lastID pole
        Category.setLastID(categories.get(categories.size() - 1).getID() + 1);
        Category foundCategory = categories.stream()
                .filter(category -> category.getName().equals(name))
                .findAny()
                .orElse(null);

        if (foundCategory != null) {
            System.out.println("-------------- ID [" + foundCategory.getID() + "] " + foundCategory.getName() + "--------------");
            productList.stream()
                    .filter(product -> product.getCategory().getName().equals(name))
                    .map(product -> "[" + product.getID() + "] " + product.getName())
                    .forEach(System.out::println);
        } else {
            System.out.println("Niepoprawna kategoria");
        }
    }

    public void showAllCategories() {
        categories.stream()
                .map(category -> "[" + category.getID() + "] " + category.getName())
                .forEach(name -> System.out.println("\t" + name));
    }

    public void addCategory() throws FileNotFoundException {
        //Category.setLastID(categories.get(categories.size()-1).getID() + 1);
        System.out.println(" ++++++++++++ Dodanie kategorii ++++++++++++");
        Scanner scanner = new Scanner(System.in);
        String newCategoryName;
        List<String> categoriesNames = categories.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę dodawanej kategorii: ");
            newCategoryName = scanner.nextLine();
            if (Category.isNameCorrect(newCategoryName)) {
                System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
            } else if (categoriesNames.contains(newCategoryName)) {
                System.out.println("\tKategoria o takiej nazwie już istnieje! Proszę podać inną nazwę");
            } else {
                categories.add(new Category(newCategoryName));
                break;
            }
        } while (true);

        CreateData update = new CreateData();
        update.writeCategories(categories);
        System.out.println("\tNowa kategoria została dodana");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void removeCategory() throws FileNotFoundException {
        System.out.println("----------- Usunięcie kategorii -----------");
        Scanner scanner = new Scanner(System.in);
        String categoryToRemove;
        List<String> categoriesNames = categories.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę kategorii do usunięcia( -1 żeby cofnąć): ");
            categoryToRemove = scanner.nextLine();
            if (categoryToRemove.equals("-1")) {
                break;
            }
            if (Category.isNameCorrect(categoryToRemove)) {
                System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
            } else if (!categoriesNames.contains(categoryToRemove)) {
                System.out.println("\tKategoria o takiej nazwie nie istnieje! Proszę podać inną nazwę");
            } else {
                categories.remove(findByName(categoryToRemove));
                System.out.println("\tKategoria została usunięta");
                break;
            }
        } while (true);

        CreateData update = new CreateData();
        update.writeCategories(categories);
        System.out.println("--------------------------------------------");
    }

    private int findByName(final String name) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
