package controller.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {
    public AnchorPane loadFormContext;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            home();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void home () throws IOException {

    }


    public void btnBlock(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/blokForm.fxml");

        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void btnDash(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/HomeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void btnExit(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage)  loadFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/LogInForm.fxml"))));
    }

    public void dashboardHarvestOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/HarvestFrom.fxml");
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void mainStockBtn(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/StockManagefrom.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void dashBoardProfitOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/Manageprofit.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }
}
