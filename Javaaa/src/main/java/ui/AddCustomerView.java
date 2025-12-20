package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Customer;
import service.CustomerService;

public class AddCustomerView extends VBox {

    public AddCustomerView() {
        setPadding(new Insets(30));
        setSpacing(15);

        Label title = new Label("Add Customer");
        title.setStyle("-fx-font-size:22; -fx-font-weight:bold;");

        TextField idField = new TextField();
        idField.setPromptText("Customer ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button saveBtn = new Button("Save Customer");
        saveBtn.setStyle("-fx-background-color:#4a4aff; -fx-text-fill:white;");

        saveBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Customer c = new Customer(
                        id,
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText());
                CustomerService.addCustomer(c);
                clear(idField, nameField, emailField, phoneField);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "ID must be a number!");
                alert.show();
            }
        });

        getChildren().addAll(title, idField, nameField, emailField, phoneField, saveBtn);
    }

    private void clear(TextField... fields) {
        for (TextField f : fields)
            f.clear();
    }
}
