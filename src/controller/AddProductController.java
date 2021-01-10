/**
 * @author Austin Kim
 */

package controller;

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

/** The AddProductController allows users to input a new part. */
public class AddProductController implements Initializable {

    /** The not editable text field which displays an automatically generated and unique ID. */
    @FXML
    private TextField productIdTextInputField;

    /** The text field to input a product name. */
    @FXML
    private TextField nameTextInputField;

    /** The text field to input a product stock. */
    @FXML
    private TextField invTextInputField;

    /** The text field to input a product price. */
    @FXML
    private TextField costTextInputField;

    /** The text field to input a product min. */
    @FXML
    private TextField minTextInputField;

    /** The text field to input a product max. */
    @FXML
    private TextField maxTextInputField;

    /** The search field to input text for a part ID or name. */
    @FXML
    private TextField searchTextInputField;

    /** The tableview which displays the list of all parts */
    @FXML
    private TableView<Part> topTableView;

    /** The tableview column which displays the part id. */
    @FXML
    private TableColumn<Part, Integer> allPartID;

    /** The tableview column which displays the part name. */
    @FXML
    private TableColumn<Part, String> allPartName;

    /** The tableview column which displays the part stock. */
    @FXML
    private TableColumn<Part, Integer> allPartInvCount;

    /** The tableview column which displays the part price. */
    @FXML
    private TableColumn<Part, Double> allCostPerUnit;

    /** The tableview which displays the list of associated parts. */
    @FXML
    private TableView<Part> bottomTableView;

    /** The tableview column which displays the part id. */
    @FXML
    private TableColumn<Part, Integer> associatedPartID;

    /** The tableview column which displays the part name. */
    @FXML
    private TableColumn<Part, String> associatedPartName;

    /** The tableview column which displays the part stock. */
    @FXML
    private TableColumn<Part, Integer> associatedInvCount;

    /** The tableview column which displays the part price. */
    @FXML
    private TableColumn<Part, Double> associatedCostPerUnit;

    /** The button that adds the product to the list of all products. */
    @FXML
    private Button saveButton;

    /** The button that goes back to the MainScreen without adding the part. */
    @FXML
    private Button cancelButton;

    /** This list holds the parts to associate with the product. */
    private final ObservableList<Part> listOfAssociatedParts = FXCollections.observableArrayList();

    /**
     * This method initializes the controller to show the ID and populate the top tableview with all parts.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productIdTextInputField.setText(String.valueOf(Inventory.autoProductId()));
        populateAllPartsTableView(Inventory.getAllParts());
    }

    /**
     * This method fills the all-parts tableview (top) with the list of all parts.
     * @param listOfParts The list of parts to fill the tableview with.
     */
    public void populateAllPartsTableView(ObservableList<Part> listOfParts) {
        topTableView.setItems(listOfParts);
        allPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartInvCount. setCellValueFactory(new PropertyValueFactory<>("stock"));
        allCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method fills the associated-parts tableview (bottom) with a list of associated parts.
     * @param listOfParts The list of parts to fill the tableview with.
     */
    public void populateAssociatedPartsTableView(ObservableList<Part> listOfParts) {
        bottomTableView.setItems(listOfParts);
        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInvCount. setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method searches the list of all parts for the String or Integer in the searchTextInputField.
     */
    @FXML
    void clickSearchButton() {

        String searchText = searchTextInputField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            populateAllPartsTableView(Inventory.getAllParts());
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
                populateAllPartsTableView(searchResultsList);
                if (searchResultsList.isEmpty()) {
                    dialogBox(Alert.AlertType.ERROR, "Part Not Found", "Unable to find part, try again.");
                }
            }
        }
    }

    /**
     * This method adds the selected part to the list of associated parts and the bottom tableview.
     */
    @FXML
    void clickAddButton() {
        Part selectedPart = topTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            dialogBox(Alert.AlertType.ERROR, "No Part Selected", "Select a part to associate.");
        }
        else if (listOfAssociatedParts.contains(selectedPart)) {
            dialogBox(Alert.AlertType.ERROR, "Part Already Associated", "This part is already associated.");
        }
        else {
            listOfAssociatedParts.add(selectedPart);
            populateAssociatedPartsTableView(listOfAssociatedParts);
        }
        bottomTableView.getSelectionModel().select(null);
    }

    /**
     * This method removes the selected part from the list of associated parts and the bottom tableview.
     */
    @FXML
    void clickRemoveAssociatedPartButton() {
        Part selectedPart = bottomTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            dialogBox(Alert.AlertType.ERROR, "No Part Selected", "Select a part to disassociate.");
        }
        else {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Delete Associated Part?", "This action will delete the associated part from product. Continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                listOfAssociatedParts.remove(selectedPart);
                populateAssociatedPartsTableView(listOfAssociatedParts);
            }
        }
        bottomTableView.getSelectionModel().select(null);
    }

    /**
     * This method saves the product to the list of all products only if it passes criteria and error checks.
     * @throws IOException Exception
     */
    @FXML
    void clickSaveButton() throws IOException {

        String nameText = nameTextInputField.getText().trim();
        String priceText = costTextInputField.getText().trim();
        String stockText = invTextInputField.getText().trim();
        String minText = minTextInputField.getText().trim();
        String maxText = maxTextInputField.getText().trim();

        if (nameText.isEmpty()) {
            dialogBox(Alert.AlertType.ERROR, "Missing Name", "Enter a name.");
        }
        else if (priceText.isEmpty()) {
            dialogBox(Alert.AlertType.ERROR, "Missing Price", "Enter a price.");
        }
        else if (minText.isEmpty()) {
            dialogBox(Alert.AlertType.ERROR, "Missing Min", "Enter a minimum inventory count.");
        }
        else if (maxText.isEmpty()) {
            dialogBox(Alert.AlertType.ERROR, "Missing Max", "Enter a maximum inventory count.");
        }
        else {
            try {
                double price = Double.parseDouble(priceText);
                int stock = 0;
                int min = Integer.parseInt(minText);
                int max = Integer.parseInt(maxText);

                if (!(stockText.isEmpty())) {
                    stock = Integer.parseInt(stockText);
                }

                double priceOfParts = 0.0;
                for (int i = 0; i < listOfAssociatedParts.size(); i++) {
                    priceOfParts += listOfAssociatedParts.get(i).getPrice();
                }

                if (price <= 0.0) {
                    dialogBox(Alert.AlertType.ERROR, "Invalid Price", "Price must be greater than zero.");
                }
                else if (price < priceOfParts) {
                    dialogBox(Alert.AlertType.ERROR, "Invalid Price", "Product price cannot be less than total cost of associated parts.");
                }
                else if (!(min <= stock) || !(stock <= max) || !(min < max)) {
                    dialogBox(Alert.AlertType.ERROR, "Incorrect Min, Max, or Stock Values", "Min should be less than Max. Stock should be inclusively between those two values.");
                }
                else {
                    Product newProduct = new Product(Inventory.autoProductId(), nameText, price, stock, min, max);
                    for (int i = 0; i < listOfAssociatedParts.size(); i++) {
                        newProduct.addAssociatedPart(listOfAssociatedParts.get(i));
                    }
                    Inventory.addProduct(newProduct);
                    goToMainScreen(saveButton);
                }
            }
            catch(NumberFormatException exception) {
                dialogBox(Alert.AlertType.ERROR, "Improper Values in Text Fields", "Enter valid values in text fields.");
            }
        }
    }

    /**
     * This method changes the stage to MainScreen.fxml without saving; a confirmation screen appears if text detected in the form.
     * @throws IOException Exception
     */
    @FXML
    void clickCancelButton() throws IOException {
        if (noChanges()) {
            goToMainScreen(cancelButton);
        }
        else {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Changes Detected in Add Product Form", "Changes will not be saved. Do you want to continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                goToMainScreen(cancelButton);
            }
        }
    }

    /**
     * This method detects if there is any text or associated parts in the form.
     * @return true if form is empty, false otherwise.
     */
    public boolean noChanges() {
        String nameText = nameTextInputField.getText().trim();
        String invText = invTextInputField.getText().trim();
        String costText = costTextInputField.getText().trim();
        String minText = minTextInputField.getText().trim();
        String maxText = maxTextInputField.getText().trim();

        return (bottomTableView.getItems() == null || bottomTableView.getItems().isEmpty()) &&
                nameText.isEmpty() && invText.isEmpty() && costText.isEmpty() &&
                minText.isEmpty() && maxText.isEmpty();
    }

    /**
     * This method changes the stage to MainScreen.fxml.
     * @param buttonName the name of the button clicked.
     * @throws IOException Exception
     */
    private void goToMainScreen(Button buttonName) throws IOException {
        Stage stage = (Stage) buttonName.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainScreen.fxml")).load()));
        stage.show();
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
