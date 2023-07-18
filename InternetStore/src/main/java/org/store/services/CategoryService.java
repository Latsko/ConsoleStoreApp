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
        if(categoryFile.exists()) {
            categories = new ArrayList<>(readData.readCategoriesFromFile());
        } else {
            //tutaj może być wygenerowanie właśnie pliku z danymi, czyli
            new CreateData().createCategories(null);
            categories = new ArrayList<>();
        }
    }

    public void showCategory(final String name) throws FileNotFoundException {
        List<Product> productList = new ArrayList<>(readData.readProductsFromFile());
        Category foundCategory = categories.stream()
                .filter(category -> category.getName().equals(name))
                .findAny()
                .orElse(null);

        if(foundCategory != null) {
            System.out.println("[" + foundCategory.getID() + "] " + foundCategory.getName());
            productList.stream()
                    .filter(product -> product.getCategory().getName().equals(name))
                    .forEach(System.out::println);
        } else {
            System.out.println("Niepoprawna kategoria");
        }
    }

    public void showAllCategories() {
        categories.stream()
                .map(Category::getName)
                .forEach(name -> System.out.println("\t" + name));
    }

    public void addCategory() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String newCategoryName;
        List<String> categoriesNames = categories.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę dodawanej kategorii: ");
            newCategoryName = scanner.nextLine();
            if (!Category.nameIsCorrect(newCategoryName)) {
                System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
            } else if (categoriesNames.contains(newCategoryName)) {
                System.out.println("\tKategoria o takiej nazwie już istnieje! Proszę podać inną nazwę");
            } else {
                categories.add(new Category(newCategoryName));
                break;
            }
        } while (true);

        CreateData update = new CreateData();
        update.createCategories(categories);
    }

    public void removeCategory() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String categoryToRemove;
        List<String> categoriesNames = categories.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę kategorii do usunięcia: ");
            categoryToRemove = scanner.nextLine();
            if (!Category.nameIsCorrect(categoryToRemove)) {
                System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
            } else if (!categoriesNames.contains(categoryToRemove)) {
                System.out.println("\tKategoria o takiej nazwie nie istnieje! Proszę podać inną nazwę");
            } else {
                categories.remove(findByName(categoryToRemove));
                break;
            }
        } while (true);

        CreateData update = new CreateData();
        update.createCategories(categories);
    }

    private int findByName(final String name) {
        for (int i = 0; i < categories.size(); i++) {
            if(categories.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
