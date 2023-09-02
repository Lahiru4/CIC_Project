package controller;

import db.DbConnection;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageprofitController implements Initializable {


    public StackedBarChart stackedbarchatmanageProfit;
    public void profitManageOnAction(Event event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String query1 = "SELECT block_name,(SUM(stock_order.qty)*harvest_manage.price_per_kg)-((SUM(additionalCost))+(SUM(employee_salary))+(SUM(plant_Expenses))+(SUM(fertilizer_expenses))) FROM block JOIN harvest on block.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN stock on stock.stock_id = harvest.stock_id JOIN stock_order on stock.stock_id = stock_order.stock_id GROUP BY block.block_id;";

            XYChart.Series<String,Double> series = new XYChart.Series<>();
            series.setName("Profit");
            try {
                ResultSet rs = connection.createStatement().executeQuery(query1);
                while (rs.next()){
                    series.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
                }
            }catch (Exception e){

            }
            stackedbarchatmanageProfit.getData().add(series);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
