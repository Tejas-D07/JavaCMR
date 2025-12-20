package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Dashboard extends Application {

    private BorderPane root = new BorderPane();

    @Override
    public void start(Stage stage) {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color:#1e1e2f;");
        sidebar.setPrefWidth(200);

        Button addBtn = createButton("➕ Add Customer");
        Button viewBtn = createButton("👁 View Customers");

        sidebar.getChildren().addAll(addBtn, viewBtn);

        root.setLeft(sidebar);
        root.setCenter(new AddCustomerView());

        addBtn.setOnAction(e -> root.setCenter(new AddCustomerView()));
        viewBtn.setOnAction(e -> root.setCenter(new ViewCustomerView()));

        Scene scene = new Scene(root, 900, 550);
        stage.setTitle("Customer Management System");
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(160);
        btn.setStyle("""
            -fx-background-color:#2d2d44;
            -fx-text-fill:white;
            -fx-background-radius:10;
            -fx-font-size:14;
        """);
        return btn;
    }
}
