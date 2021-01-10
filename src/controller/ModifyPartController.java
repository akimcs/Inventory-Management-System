/**
 * @author Austin Kim
 */

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.util.Optional;

/** The ModifyPartController Class allows users to modify an existing part. */
public class ModifyPartController  {

    /** The toggle group for the radio buttons. */
    public ToggleGroup InOutTG;

    /** The part type label. */
    @FXML
    private Label machineIdLabel;

    /** The InHouse radio button. */
    @FXML
    private RadioButton inHouseRadioButton;

    /** The outsourced radio button. */
    @FXML
    private RadioButton outSourcedRadioButton;

    /** The not editable text field which displays an automatically generated and unique ID. */
    @FXML
    private TextField partIdTextInputField;

    /** The text field to input a part name. */
    @FXML
    private TextField nameTextInputField;

    /** The text field to input a part stock. */
    @FXML
    private TextField invTextInputField;

    /** The text field to input a part price. */
    @FXML
    private TextField costTextInputField;

    /** The text field to input a part min. */
    @FXML
    private TextField minTextInputField;

    /** The text field to input a part max. */
    @FXML
    private TextField maxTextInputField;

    /** The text field to input a part machine ID or company name. */
    @FXML
    private TextField machineIdTextInputField;

    /** The button that adds the part to the list of all parts. */
    @FXML
    private Button saveButton;

    /** The button that goes back to the MainScreen without adding the part. */
    @FXML
    private Button cancelButton;

    /** This part holds a copy of the original part being modified. */
    private Part originalPart;

    /** This String holds a copy of the original part name. */
    private String originalName;

    /** This String holds a copy of the original part price. */
    private String originalPrice;

    /** This String holds a copy of the original part stock. */
    private String originalStock;

    /** This String holds a copy of the original part min. */
    private String originalMin;

    /** This String holds a copy of the original part max. */
    private String originalMax;

    /** This String holds a copy of the original part machine ID or company name. */
    private String originalMachineId;

    /**
     * This method changes the part type label to "Machine ID".
     */
    @FXML
    void clickInHouseRadioButton() {
        machineIdLabel.setText("Machine ID");
    }

    /**
     * This method changes the part type label to "Company Name".
     */
    @FXML
    void clickOutSourcedRadioButton() {
        machineIdLabel.setText("Company Name");
    }

    /**
     * This method updates the existing part only if it passes criteria and error checks.
     * @throws IOException Exception
     */
    @FXML
    void clickSaveButton() throws IOException {
        if (noChanges()) {
            goToMainScreen(saveButton);
        }
        else {

            String nameText = nameTextInputField.getText().trim();
            String priceText = costTextInputField.getText().trim();
            String stockText = invTextInputField.getText().trim();
            String minText = minTextInputField.getText().trim();
            String maxText = maxTextInputField.getText().trim();
            String machineIdText = machineIdTextInputField.getText().trim();

            if (nameText.isEmpty()) {
                dialogBox(Alert.AlertType.ERROR, "Missing Name", "Enter a name.");
            }
            else if (priceText.isEmpty()) {
                dialogBox(Alert.AlertType.ERROR, "Missing Price", "Enter a price.");
            }
            else if (stockText.isEmpty()) {
                dialogBox(Alert.AlertType.ERROR, "Missing Inv", "Enter an inventory count.");
            }
            else if (minText.isEmpty()) {
                dialogBox(Alert.AlertType.ERROR, "Missing Min", "Enter a minimum inventory count.");
            }
            else if (maxText.isEmpty()) {
                dialogBox(Alert.AlertType.ERROR, "Missing Max", "Enter a maximum inventory count.");
            }
            else if (!(inHouseRadioButton.isSelected() || outSourcedRadioButton.isSelected())) {
                dialogBox(Alert.AlertType.ERROR, "No Radio Button Selected", "Select InHouse or OutSourced.");
            }
            else if (machineIdText.isEmpty()) {
                if (inHouseRadioButton.isSelected()) {
                    dialogBox(Alert.AlertType.ERROR, "Missing Machine ID", "Enter a machine ID.");
                }
                else {
                    dialogBox(Alert.AlertType.ERROR, "Missing Company Name", "Enter a company name.");
                }
            }
            else {
                try {
                    double price = Double.parseDouble(costTextInputField.getText().trim());
                    int stock = Integer.parseInt(invTextInputField.getText().trim());
                    int min = Integer.parseInt(minTextInputField.getText().trim());
                    int max = Integer.parseInt(maxTextInputField.getText().trim());

                    if (price <= 0.0) {
                        dialogBox(Alert.AlertType.ERROR, "Invalid Price", "Price must be greater than zero.");
                    }
                    else if (!(min <= stock) || !(stock <= max) || !(min < max)) {
                        dialogBox(Alert.AlertType.ERROR, "Incorrect Min, Max, or Stock Values", "Min should be less than Max. Stock should be inclusively between those two values.");
                    }
                    else {
                        if (inHouseRadioButton.isSelected()) {
                            int machineId = Integer.parseInt(machineIdText);
                            Inventory.updatePart(Inventory.getAllParts().indexOf(originalPart), new InHouse(originalPart.getId(), nameText, price, stock, min, max, machineId));
                        }
                        else {
                            Inventory.updatePart(Inventory.getAllParts().indexOf(originalPart), new Outsourced(originalPart.getId(), nameText, price, stock, min, max, machineIdText));
                        }
                        goToMainScreen(saveButton);
                    }
                }
                catch (NumberFormatException exception) {
                    dialogBox(Alert.AlertType.ERROR, "Improper Values in Text Fields", "Enter valid values in text fields.");
                }
            }
        }
    }

    /**
     * This method changes the stage to MainScreen.fxml without saving; a confirmation screen appears if changes are detected in the form.
     * @throws IOException Exception
     */
    @FXML
    void clickCancelButton() throws IOException {
        if (noChanges()) {
            goToMainScreen(cancelButton);
        }
        else {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Changes Detected in Modify Part Form", "Changes will not be saved. Continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                goToMainScreen(cancelButton);
            }
        }
    }

    /**
     * This method saves the original part's data and displays it in the text fields.
     * @param selectedPart the part from the MainScreen to be modified.
     */
    public void displayPart(Part selectedPart) {

        originalPart = selectedPart;
        originalName = selectedPart.getName();
        originalPrice = String.valueOf(selectedPart.getPrice());
        originalStock = String.valueOf(selectedPart.getStock());
        originalMin = String.valueOf(selectedPart.getMin());
        originalMax = String.valueOf(selectedPart.getMax());

        if (selectedPart instanceof InHouse) {
            originalMachineId = String.valueOf(((InHouse) selectedPart).getMachineId());
            inHouseRadioButton.setSelected(true);
            inHouseRadioButton.requestFocus();
            machineIdLabel.setText("Machine ID");
            machineIdTextInputField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }
        else {
            originalMachineId = ((Outsourced) selectedPart).getCompanyName();
            outSourcedRadioButton.setSelected(true);
            outSourcedRadioButton.requestFocus();
            machineIdLabel.setText("Company Name");
            machineIdTextInputField.setText(((Outsourced) selectedPart).getCompanyName());
        }

        partIdTextInputField.setText(String.valueOf(selectedPart.getId()));
        nameTextInputField.setText(selectedPart.getName());
        invTextInputField.setText(String.valueOf(selectedPart.getStock()));
        costTextInputField.setText(String.valueOf(selectedPart.getPrice()));
        minTextInputField.setText(String.valueOf(selectedPart.getMin()));
        maxTextInputField.setText(String.valueOf(selectedPart.getMax()));
    }

    /**
     * This method detects if there is any changes in the form.
     * @return true if form is empty, false otherwise.
     */
    public boolean noChanges() {

        String nameText = nameTextInputField.getText().trim();
        String priceText = costTextInputField.getText().trim();
        String stockText = invTextInputField.getText().trim();
        String minText = minTextInputField.getText().trim();
        String maxText = maxTextInputField.getText().trim();
        String machineIdText = machineIdTextInputField.getText().trim();

        if (originalName.equals(nameText) && originalPrice.equals(priceText) && originalStock.equals(stockText) && originalMin.equals(minText) && originalMax.equals(maxText) && originalMachineId.equals(machineIdText)) {
            return ((originalPart instanceof InHouse) && (inHouseRadioButton.isSelected())) || ((originalPart instanceof Outsourced) && (outSourcedRadioButton.isSelected()));
        }
        return false;
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
