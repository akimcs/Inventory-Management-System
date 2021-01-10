/**
 * @author Austin Kim
 */

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The Inventory Class holds all Parts/Products, modifies the Part/Product list, provides Part/Product search, and generates IDs. */
public class Inventory {

    /** This list holds all the part objects. */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** This list holds all the product objects. */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method adds a part to the list of all parts.
     * @param newPart The part to add to the list.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * This method adds a product to the list of all products.
     * @param newProduct The product to add to the list.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This method searches the list of all parts for a specific part.
     * @param partId The ID of a part being searched for.
     * @return The part object with matching ID, null if not found.
     */
    public static Part lookupPart(int partId) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partId) {
                return allParts.get(i);
            }
        }
        return null;
    }

    /**
     * This method searches the list of all products for a specific product.
     * @param productId The ID of a product being searched for.
     * @return The product object with matching ID, null if not found.
     */
    public static Product lookupProduct(int productId) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productId) {
                return allProducts.get(i);
            }
        }
        return null;
    }

    /**
     * This method searches the list of all parts for similar parts.
     * @param partName A string to search the part names for.
     * @return A list of part objects with names that contain String partName.
     */
    public static ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> partialStringMatches = FXCollections.observableArrayList();

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getName().toLowerCase().contains(partName.toLowerCase())) {
                partialStringMatches.add(allParts.get(i));
            }
        }
        return partialStringMatches;
    }

    /**
     * This method searches the list of all products for similar products.
     * @param productName A string to search the products names for.
     * @return A list of product objects with names that contain String productName.
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> partialStringMatches = FXCollections.observableArrayList();

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName().toLowerCase().contains(productName.toLowerCase())) {
                partialStringMatches.add(allProducts.get(i));
            }
        }
        return partialStringMatches;
    }

    /**
     * This method updates a part object in the list of all parts.
     * @param index The index of the old part in the parts list.
     * @param selectedPart The new part to replace the old part.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method updates a product object in the list of all products.
     * @param index The index of the old product in the products list.
     * @param newProduct The new product to replace the old product.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * This method deletes a specific part from the list of all parts.
     * @param selectedPart The part to delete.
     * @return true if deleted, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * This method deletes a specific product from the list of all products.
     * @param selectedProduct The product to delete.
     * @return true if deleted, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * This method returns the list of all parts.
     * @return The list of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method returns the list of all products.
     * @return This list of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * This method provides the next unique ID for a new part.
     * @return A new unique part ID.
     */
    public static int autoPartId() {
        int highestPartId = 0;
        for (int i = 0; i < allParts.size(); i++) {
            if (highestPartId <= allParts.get(i).getId()) {
                highestPartId = allParts.get(i).getId() + 1;
            }

        }
        return highestPartId;
    }

    /**
     * This method provides the next unique ID for a new product.
     * @return A new unique product ID.
     */
    public static int autoProductId() {
        int highestProductId = 0;
        for (int i = 0; i < allProducts.size(); i++) {
            if (highestProductId <= allProducts.get(i).getId()) {
                highestProductId = allParts.get(i).getId() + 1;
            }
        }
        return highestProductId;
    }
}
