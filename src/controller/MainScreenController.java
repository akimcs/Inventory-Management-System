/**
 * @author Austin Kim
 */

package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** The MainScreenController Class allows users to interact with the UI and inventory. */
public class MainScreenController implements Initializable {

    /** The search field to input text for a part ID or name. */
    @FXML
    private TextField partsTextInputField;

    /** The tableview which displays parts. */
    @FXML
    private TableView<Part> partsTableView;

    /** The tableview column which displays the part id. */
    @FXML
    private TableColumn<Part, Integer> partID;

    /** The tableview column which displays the part name. */
    @FXML
    private TableColumn<Part, String> partName;

    /** The tableview column which displays the part stock. */
    @FXML
    private TableColumn<Part, Integer> partInvCount;

    /** The tableview column which displays the part price. */
    @FXML
    private TableColumn<Part, Double> partCostPerUnit;

    /** The search field to input text for a product ID or name. */
    @FXML
    private TextField productsTextInputField;

    /** The tableView which displays products. */
    @FXML
    private TableView<Product> productsTableView;

    /** The tableview column which displays the product id. */
    @FXML
    private TableColumn<Product, Integer> productID;

    /** The tableview column which displays the product name. */
    @FXML
    private TableColumn<Product, String> productName;

    /** The tableview column which displays the product stock. */
    @FXML
    private TableColumn<Product, Integer> productInvCount;

    /** The tableview column which displays the product price. */
    @FXML
    private TableColumn<Product, Double> productCostPerUnit;

    /** The button that changes the stage to AddPart.fxml */
    @FXML
    private Button partsAddButton;

    /** The button that changes the stage to ModifyPart.fxml */
    @FXML
    private Button partsModifyButton;

    /** The button that changes the stage to AddProduct.fxml */
    @FXML
    private Button productsAddButton;

    /** The button that changes the stage to ModifyProduct.fxml */
    @FXML
    private Button productsModifyButton;

    /**
     * This method initializes the controller to populate the parts tableview and products tableview.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populatePartsTableView(Inventory.getAllParts());
        populateProductsTableView(Inventory.getAllProducts());
    }

    /**
     * This method fills the parts tableview (left-side) with a list of parts.
     * @param listOfParts The list of parts to fill the tableview with.
     */
    public void populatePartsTableView(ObservableList<Part> listOfParts) {
        partsTableView.setItems(listOfParts);
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCount. setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method fills the products tableview (right-side) with a list of products.
     * @param listOfProducts The list of products to fill the tableview with.
     */
    public void populateProductsTableView(ObservableList<Product> listOfProducts) {
        productsTableView.setItems(listOfProducts);
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCount. setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method searches the list of all parts for the String or Integer in the partsTextInputField.
     */
    @FXML
    void clickPartsSearchButton() {

        String searchText = partsTextInputField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            populatePartsTableView(Inventory.getAllParts());
        }

        else {

            ObservableList<Part> searchResultsList = FXCollections.observableArrayList();
            ObservableList<Part> allParts = Inventory.getAllParts();

            try {

                int searchId = Integer.parseInt(searchText);
                if (Inventory.lookupPart(searchId) != null) {
                    searchResultsList.add(Inventory.lookupPart(searchId));
                }

                for (int i = 0; i < allParts.size(); i++) {
                    if (Integer.toString(allParts.get(i).getId()).contains(searchText)) {
                        if (allParts.get(i).getId() != searchId) {
                            searchResultsList.add(allParts.get(i));
                        }
                    }
                }
                searchResultsList.addAll(Inventory.lookupPart(searchText));
            }

            catch (NumberFormatException exception) {
                ObservableList<Part> allPartialStringMatches = Inventory.lookupPart(searchText);

                for (int i = 0; i < allParts.size(); i++) {
                    if (allParts.get(i).getName().toLowerCase().equals(searchText)) {
                        searchResultsList.add(allParts.get(i));
                        allPartialStringMatches.remove(allParts.get(i));
                    }
                }

                searchResultsList.addAll(allPartialStringMatches);
            }

            finally {
                populatePartsTableView(searchResultsList);
                if (searchResultsList.isEmpty()) {
                    dialogBox(Alert.AlertType.ERROR, "Part Not Found", "Unable to find part, try again.");
                }
            }
        }
    }

    /**
     * This method adds a part to the list of all parts using the AddPart.fxml stage.
     * @throws IOException Exception
     */
    @FXML
    void clickPartsAddButton() throws IOException {
        changeScene(partsAddButton, "/view/AddPart.fxml");
    }

    /**
     * This method modifies a selected part using the ModifyPart.fxml stage.
     * @throws IOException Exception
     */
    @FXML
    void clickPartsModifyButton() throws IOException {

        Part selectingPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectingPart == null) {
            dialogBox(Alert.AlertType.ERROR, "No Part Selected", "Select a part and try again.");
        }
        else {
            FXMLLoader loader = changeScene(partsModifyButton, "/view/ModifyPart.fxml");
            ModifyPartController controller = loader.getController();
            controller.displayPart(selectingPart);
        }
    }

    /**
     *  This method deletes a selected part from the list of all parts.
     */
    @FXML
    void clickPartsDeleteButton() {

        Part selectingPart = partsTableView.getSelectionModel().getSelectedItem();

        if (selectingPart == null) {
            dialogBox(Alert.AlertType.ERROR, "No Part Selected", "Select a part and try again.");
        }
        else {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Delete Confirmation", "This will delete the selected part. Continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                Inventory.deletePart(selectingPart);
            }
        }
        partsTableView.getSelectionModel().select(null);
    }

    /**
     * This method searches the list of all products for the String or Integer in the productsTextInputField.
     */
    @FXML
    void clickProductsSearchButton() {

        String searchText = productsTextInputField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            populateProductsTableView(Inventory.getAllProducts());
        }

        else {

            ObservableList<Product> searchResultsList = FXCollections.observableArrayList();
            ObservableList<Product> allProducts = Inventory.getAllProducts();

            try {

                int searchId = Integer.parseInt(searchText);
                if (Inventory.lookupProduct(searchId) != null) {
                    searchResultsList.add(Inventory.lookupProduct(searchId));
                }

                for (int i = 0; i < allProducts.size(); i++) {
                    if (Integer.toString(allProducts.get(i).getId()).contains(searchText)) {
                        if (allProducts.get(i).getId() != searchId) {
                            searchResultsList.add(allProducts.get(i));
                        }
                    }
                }
                searchResultsList.addAll(Inventory.lookupProduct(searchText));
            }

            catch (NumberFormatException exception) {
                ObservableList<Product> allPartialStringMatches = Inventory.lookupProduct(searchText);

                for (int i = 0; i < allProducts.size(); i++) {
                    if (allProducts.get(i).getName().toLowerCase().equals(searchText)) {
                        searchResultsList.add(allProducts.get(i));
                        allPartialStringMatches.remove(allProducts.get(i));
                    }
                }

                searchResultsList.addAll(allPartialStringMatches);
            }

            finally {
                populateProductsTableView(searchResultsList);
                if (searchResultsList.isEmpty()) {
                    dialogBox(Alert.AlertType.ERROR, "Product Not Found", "Unable to find product, try again.");
                }
            }
        }
    }

    /**
     * This method adds a product to the list of all products using the AddProduct.fxml stage.
     * @throws IOException Exception
     */
    @FXML
    void clickProductsAddButton() throws IOException {
        changeScene(productsAddButton, "/view/AddProduct.fxml");
    }

    /**
     * This method modifies a selected part using the ModifyProduct.fxml stage.
     * @throws IOException Exception
     */
    @FXML
    void clickProductsModifyButton() throws IOException {

        Product selectingProduct = productsTableView.getSelectionModel().getSelectedItem();

        if (selectingProduct == null) {
            dialogBox(Alert.AlertType.ERROR, "No Product Selected", "Select a product and try again.");
        }
        else {
            FXMLLoader loader = changeScene(productsModifyButton, "/view/ModifyProduct.fxml");
            ModifyProductController controller = loader.getController();
            controller.displayProduct(selectingProduct);
        }
    }

    /**
     * This method deletes a selected product from the list of all products.
     */
    @FXML
    void clickProductsDeleteButton() {

        Product selectingProduct = productsTableView.getSelectionModel().getSelectedItem();

        if (selectingProduct == null) {
            dialogBox(Alert.AlertType.ERROR, "No Product Selected", "Select a product and try again.");
        }
        else if (selectingProduct.getAllAssociatedParts() == null || selectingProduct.getAllAssociatedParts().isEmpty()) {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Delete Confirmation", "This will delete the selected product. This product has no associated parts with it. Continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectingProduct);
            }
        }
        else {
            dialogBox(Alert.AlertType.ERROR, "Cannot Delete Product With Associated Parts", "This product contains associated parts. You cannot delete it.");
        }
        productsTableView.getSelectionModel().select(null);
    }

    /**
     * This method closes the JavaFX application.
     */
    @FXML
    void clickExitButton() {
        Platform.exit();
    }

    /**
     * This method changes the stage to a different fxml file.
     * @param buttonName the name of the button clicked.
     * @param fileName the name of the fxml file.
     * @return the FXMLLoader object used to pass parts/products onto the next stage.
     * @throws IOException Exception
     */
    public FXMLLoader changeScene(Button buttonName, String fileName) throws IOException {
        Stage stage = (Stage) buttonName.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        return loader;
    }

    /**
     * This method displays a dialog box for error or confirmation purposes.
     * @param messageType The alert type (error or confirmation).
     * @param title The text to display as the title.
     * @param content The text to display as the content.
     * @return The dialog box.
     */
    public Optional<ButtonType> dialogBox(Alert.AlertType messageType, String title, String content) {
        Alert alert = new Alert(messageType);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
