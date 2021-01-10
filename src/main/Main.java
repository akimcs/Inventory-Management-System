/**
 * @author Austin Kim
 */

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/** The Main Class opens the MainScreen stage and populates the application with sample data.  */
public class Main extends Application {

    /**
     * This method loads and shows the MainScreen stage.
     * @param primaryStage The stage to load the application.
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This method fills the inventory with sample data and launches the JavaFX application.
     * @param args args
     */
    public static void main(String[] args) {
        exampleTestData();
        launch(args);
    }

    /** This method provides the sample data. */
    private static void exampleTestData() {
        InHouse part1 = new InHouse(47, "button", 15.50, 5, 1, 10, 97584);
        InHouse part2 = new InHouse(4, "sleeve", 29.78, 7, 3, 14, 34243);
        Outsourced part3 = new Outsourced(3, "B", 20.99, 42, 10, 100, "Intel");
        Outsourced part4 = new Outsourced(7, "keyboard", 40.50, 75, 20, 100, "Dell");
        InHouse part5 = new InHouse(59, "Windows7", 149.99, 45, 5, 100, 1000);
        Outsourced part6 = new Outsourced(86, "b", 100.00, 55, 1, 100, "Adobe");

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Inventory.addPart(part5);
        Inventory.addPart(part6);

        Product product1 = new Product(50, "shirt", 50.00, 12, 5, 50);
        Product product2 = new Product(57, "computer", 400.00, 15, 10, 45);
        Product product3 = new Product(140, "Software Package7", 500.00, 10, 5, 15);

        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);
        product2.addAssociatedPart(part3);
        product2.addAssociatedPart(part4);
        product3.addAssociatedPart(part5);
        product3.addAssociatedPart(part6);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
    }
}
