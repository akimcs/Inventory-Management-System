/**
 * @author Austin Kim
 */

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** The AddPartController Class allows users to input a new part. */
public class AddPartController implements Initializable {

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

    /**
     * This method initializes the controller to show the ID and auto-select a part type.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIdTextInputField.setText(String.valueOf(Inventory.autoPartId()));
        inHouseRadioButton.setSelected(true);
    }

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
     * This method saves the part to the list of all parts only if it passes criteria and error checks.
     * @throws IOException Exception
     */
    @FXML
    void clickSaveButton() throws IOException {

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
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
                int min = Integer.parseInt(minText);
                int max = Integer.parseInt(maxText);

                if (price <= 0.0) {
                    dialogBox(Alert.AlertType.ERROR, "Invalid Price", "Price must be greater than zero.");
                }
                else if (!(min <= stock) || !(stock <= max) || !(min < max)) {
                    dialogBox(Alert.AlertType.ERROR, "Incorrect Min, Max, or Stock Values", "Min should be less than Max. Stock should be inclusively between those two values.");
                }
                else {
                    if (inHouseRadioButton.isSelected()) {
                        int machineId = Integer.parseInt(machineIdTextInputField.getText().trim());
                        Inventory.addPart(new InHouse(Inventory.autoPartId(), nameText, price, stock, min, max, machineId));
                    }
                    else {
                        String companyName = machineIdTextInputField.getText().trim();
                        Inventory.addPart(new Outsourced(Inventory.autoPartId(), nameText, price, stock, min, max, companyName));
                    }
                    goToMainScreen(saveButton);
                }
            }
            catch(NumberFormatException exception) {
                dialogBox(Alert.AlertType.ERROR, "Improper Values in Text Fields", "Enter valid values in text fields.");
            }
        }
    }

    /**
     * This method changes the stage to MainScreen.fxml without saving; a confirmation screen appears if text is detected in the form.
     * @throws IOException Exception
     */
    @FXML
    void clickCancelButton() throws IOException {
        if (noChanges()) {
            goToMainScreen(cancelButton);
        }
        else {
            Optional<ButtonType> confirmationScreen = dialogBox(Alert.AlertType.CONFIRMATION, "Text Detected in Add Part Form", "This will clear all text and not save. Continue?");
            if (confirmationScreen.isPresent() && confirmationScreen.get() == ButtonType.OK) {
                goToMainScreen(cancelButton);
            }
        }
    }

    /**
     * This method detects if there is any text in the form.
     * @return true if form is empty, false otherwise.
     */
    public boolean noChanges() {
        String nameText = nameTextInputField.getText().trim();
        String priceText = costTextInputField.getText().trim();
        String stockText = invTextInputField.getText().trim();
        String minText = minTextInputField.getText().trim();
        String maxText = maxTextInputField.getText().trim();
        String machineIdText = machineIdTextInputField.getText().trim();

        return nameText.isEmpty() && priceText.isEmpty() && stockText.isEmpty() && minText.isEmpty() && maxText.isEmpty() && machineIdText.isEmpty();
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
