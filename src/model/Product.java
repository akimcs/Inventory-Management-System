/**
 * @author Austin Kim
 */

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The Product class represents a product with its associated parts. */
public class Product {

    /** This list holds all the part objects associated with the Product class. */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** The product ID. */
    private int id;

    /** The product name. */
    private String name;

    /** The product price per unit. */
    private double price;

    /** The product inventory count.*/
    private int stock;

    /** The product minimum inventory count. */
    private int min;

    /** The product maximum inventory count. */
    private int max;

    /**
     * This method serves as the constructor for the Product class.
     * @param id the product id to set.
     * @param name the product to set.
     * @param price the product price to set.
     * @param stock the product stock to set.
     * @param min the product min to set.
     * @param max the product max to set.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the product id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the product id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the product name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the product price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the product stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the product stock to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the product min.
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the product min to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the product max.
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the product max to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * This method adds a part to the list of part objects associated with the instantiated Product Class.
     * @param part The part to add to the list of part objects associated with the instantiated Product Class.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * This method deletes a specific part from the list of part objects associated with the instantiated Product Class.
     * @param selectedAssociatedPart The part to delete.
     * @return true if deleted, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * This method returns the list of part objects associated with the instantiated Product Class.
     * @return The list of part objects associated with the instantiated Product Class.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
