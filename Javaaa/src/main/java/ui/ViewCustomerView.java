package ui;

import javafx.collections.FXCollections;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Customer;
import service.CustomerService;

public class ViewCustomerView extends VBox {

    public ViewCustomerView() {
        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        table.getColumns().addAll(List.of(idCol, nameCol, emailCol, phoneCol));
        table.setItems(FXCollections.observableArrayList(CustomerService.getAllCustomers()));

        getChildren().add(table);
    }
}
