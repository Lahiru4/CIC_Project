package controller;

import com.jfoenix.controls.*;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import module.AreaManagement;
import module.ManageBlockSummary;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class BlokFormController extends Object implements Initializable, Serializable {

    public AnchorPane blokFormContext;

    public JFXComboBox comboBox1;
    public JFXComboBox comboBox2;
    public JFXComboBox comboBox3;

    public Label blocklable5;
    public Label blocklable6;
    public Label blocklable7;
    public Label blocklable8;

    public JFXButton areamanagebtn1;
    public JFXDatePicker datetxt;
    public JFXTextField cultivatedQtytxt;
    public Label plantArea;
    public Label plantAreaLabel;
    public Tab plantMaintainswitch;
    public JFXTextArea planttextarea;
    public JFXTextField type;
    public JFXTextField fertilizerAmount;
    public JFXTextField plants_amount;
    public JFXDatePicker datepicker1;

    @FXML
    private TableView<AreaManagement> areaManagementTable;

    @FXML
    private TableColumn<AreaManagement,String> date;

    @FXML
    private TableColumn <AreaManagement,String> block_name;

    @FXML
    private TableColumn <AreaManagement,String> plant_qtys;

    @FXML
    private TableColumn <AreaManagement,String> plant_type;


    private ObservableList<AreaManagement> list=null;


    private Connection connection = DbConnection.getInstance().getConnection();

    private PreparedStatement preparedStatement = null;

    private ResultSet resultSet = null;

    private Statement statement = null;

    @FXML
    private TableView<ManageBlockSummary> manageBlockSummaryTable;

    @FXML
    private TableColumn<ManageBlockSummary,String > areaName;

    @FXML
    private TableColumn<ManageBlockSummary,String> Date;

    @FXML
    private TableColumn<ManageBlockSummary, String> fertilizer_type;

    @FXML
    private TableColumn<ManageBlockSummary, Double> fertilizer_siize;

    @FXML
    private TableColumn<ManageBlockSummary, Integer> plant_quantaty;

    private ObservableList<ManageBlockSummary> list1=null;

    private String text;

    private String block_id = null;


    public BlokFormController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setAreaManagementTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setCombobox();
    }

    public void btnPlant(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) blokFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/PlantForm.fxml"))));

    }


    public void setCombobox() {

        String[] selectType1 = {"Block A", "Block B", "Block C", "Block D", "Block E"};
        comboBox1.setItems(FXCollections.observableArrayList(selectType1));

        String[] selectType2 = {"Cavendish Banana", "Anamalu Banana", "Kolikuttu Banana"};
        comboBox2.setItems(FXCollections.observableArrayList(selectType2));

        String[] selectType3 = {"Ammonium", "Chlorine", "Pottasium", "Uria"};
        comboBox3.setItems(FXCollections.observableArrayList(selectType3));

    }

    public void sumOfPlantQty() throws SQLException {

        preparedStatement = connection.prepareStatement("SELECT  block.block_name,sum(block_plant.amount) FROM block_plant JOIN block ON block.block_id = block_plant.block_id WHERE block.block_name=?");


        preparedStatement.setString(1, comboBox1.getValue().toString());
        resultSet = preparedStatement.executeQuery();

        int sum = 0;

        while (resultSet.next()) {
            sum = resultSet.getInt("sum(block_plant.amount)");
        }

        blocklable7.setText(sum + "");
    }

    //nimnakaveesha123456@gmail.comKavee2001


    public void setLabelValues() throws SQLException, ClassNotFoundException {

        sumOfPlantQty();

        Statement statement2 = connection.createStatement();

        ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM block WHERE block_name=" + "'" + comboBox1.getValue().toString() + "'");

        while (resultSet2.next()) {

            blocklable5.setText(resultSet2.getDouble("area") + "");
            blocklable8.setText(resultSet2.getDouble("ph_value") + "");
            blocklable6.setText(resultSet2.getDouble("humidity") + "");

        }

    }

    public void onAction1(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setLabelValues();
    }

    public void onActionSave(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        //adding Items to table

        areaManagementTable.getItems().add(new AreaManagement(datetxt.getValue().toString(),
                                            comboBox1.getValue().toString(),
                                            cultivatedQtytxt.getText(),comboBox2.getValue().toString()));

        //adding Items to DataBase

        statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT block_id FROM block WHERE block_name=" + "'" +
                                                    comboBox1.getValue().toString() + "'");

        while (resultSet.next()) {
            block_id = resultSet.getString("block_id");
        }

        statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT plant_id FROM plant WHERE plant_type=" + "'" +
                                                    comboBox2.getValue().toString() + "'");

        String plant_id = null;

        while (resultSet.next()) {
            plant_id = resultSet.getString("plant_id");
        }

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO block_plant VALUES(?,?,?,?)");

        preparedStatement.setObject(1, block_id);
        preparedStatement.setObject(2, plant_id);

        try {
            comboBox2.setItems(null);
            String[] selectType2 = {"Cavendish Banana", "Anamalu Banana", "Kolikuttu Banana"};
            comboBox2.setItems(FXCollections.observableArrayList(selectType2));
        } catch (Exception e) {
            System.out.println("Exception");
        }


        preparedStatement.setObject(3, datetxt.getValue().toString());
        datetxt.setValue(null);

        preparedStatement.setObject(4, cultivatedQtytxt.getText());
        cultivatedQtytxt.setText(null);

        preparedStatement.execute();

        sumOfPlantQty();


    }

    public void setAreaManagementTable() throws SQLException {
        date.setCellValueFactory(new PropertyValueFactory<AreaManagement, String>("date"));
        block_name.setCellValueFactory(new PropertyValueFactory<AreaManagement, String>("block_name"));
        plant_qtys.setCellValueFactory(new PropertyValueFactory<AreaManagement, String>("plant_qty"));
        plant_type.setCellValueFactory(new PropertyValueFactory<AreaManagement, String>("plant_type"));


        list = FXCollections.observableArrayList();
        areaManagementTable.setItems(list);


        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT block_plant.plant_date,block.block_name,block_plant.amount,plant.plant_type FROM block_plant INNER JOIN block ON  block.block_id=block_plant.block_id INNER JOIN plant ON plant.plant_id=block_plant.plant_id");
        while (resultSet.next()) {
            areaManagementTable.getItems().add(new AreaManagement(resultSet.getString("plant_date"),
                    resultSet.getString("block_name"),
                    resultSet.getInt("amount") + "",
                    resultSet.getString("plant_type")));

            }

    }

    public void onSelectionplantMaintain(Event event) throws SQLException, ClassNotFoundException {

        try {
            plantAreaLabel.setText( comboBox1.getValue().toString());
        }catch(Exception e){
            System.out.println("Exception");
        }

        setPlantMaintainTable();

    }

    public void setPlantMaintainTable() throws SQLException, ClassNotFoundException {

        areaName.setCellValueFactory(new PropertyValueFactory<ManageBlockSummary,String>("areaName"));
        Date.setCellValueFactory(new PropertyValueFactory<ManageBlockSummary,String>("Date"));
        fertilizer_type.setCellValueFactory(new PropertyValueFactory<ManageBlockSummary,String>("fertilizer_type"));
        fertilizer_siize.setCellValueFactory(new PropertyValueFactory<ManageBlockSummary,Double>("fertilizer_siize"));
        plant_quantaty.setCellValueFactory(new PropertyValueFactory<ManageBlockSummary,Integer>("plant_quantaty"));


        list1=FXCollections.observableArrayList();
        manageBlockSummaryTable.setItems(list1);


            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT block.block_name,block_fertilizer.fertilizer_date,fertilizer.fertilizer_type, block_fertilizer.fertilizer_distributed_amount,block_fertilizer.plant_qty FROM block_fertilizer JOIN block ON block.block_id=block_fertilizer.block_id JOIN fertilizer ON fertilizer.fertilizer_id=block_fertilizer.fertilizer_id");

            String note ="";
            while (resultSet.next()) {
                manageBlockSummaryTable.getItems().add(new ManageBlockSummary(resultSet.getString("block_name"), resultSet.getString("fertilizer_date"), resultSet.getString("fertilizer_type"), resultSet.getDouble("fertilizer_distributed_amount"),resultSet.getInt("plant_qty")));
                System.out.println(resultSet.getInt("plant_qty"));
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT note FROM block WHERE block_name='"+ comboBox1.getValue().toString()+"'");

            while (resultSet.next()){
                text=resultSet.getString("note");
                planttextarea.setText(text);
            }

    }

    public void onActionaddNote(ActionEvent actionEvent) throws SQLException {

        text+='\n'+type.getText();

        preparedStatement = connection.prepareStatement("UPDATE block SET note=? WHERE block_name=?");
        preparedStatement.setString(1, text);
        preparedStatement.setString(2,comboBox1.getValue().toString() );
        preparedStatement.executeUpdate();

        planttextarea.setText(text);

    }

    public void onActionaddreport(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT block_id FROM block WHERE block_name=" + "'" +
                comboBox1.getValue().toString() + "'");

        while (resultSet.next()) {
            block_id = resultSet.getString("block_id");
        }

        statement = connection.createStatement();

        String fertilizer_id=null;

        resultSet = statement.executeQuery("SELECT fertilizer_id FROM fertilizer WHERE fertilizer_type="+"'" +
                comboBox3.getValue().toString() + "'");

        while (resultSet.next()) {
            fertilizer_id = resultSet.getString("fertilizer_id");
        }

        preparedStatement = connection.prepareStatement("INSERT INTO block_fertilizer VALUES(?,?,?,?,?)");
        preparedStatement.setString(1, block_id);
        preparedStatement.setString(2,fertilizer_id);
        preparedStatement.setDouble(3,Double.parseDouble(fertilizerAmount.getText()));
        preparedStatement.setString(4,datepicker1.getValue().toString());
        preparedStatement.setInt(5,Integer.parseInt(plants_amount.getText()));

        preparedStatement.executeUpdate();

        setPlantMaintainTable();

    }
}
