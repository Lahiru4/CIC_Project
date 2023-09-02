package controller;

import animatefx.animation.AnimationFX;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInFormController {
    public Label lblError;

    public AnchorPane logInFormContext;
    public JFXPasswordField userPassword;
    public JFXTextField logIn_email;
    private static Pattern emailPattern;


    private Connection connection;

    public void btnSignIn(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        LoginFormManager();
    }

    public void btnSignUp(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/SignUpForm.fxml"))));
        window.setResizable(false);
    }

    private void LoginFormManager() throws SQLException, ClassNotFoundException, IOException {

        String userInputEmail = logIn_email.getText();
        String userInputPassword = userPassword.getText();
        String accountType;

        connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select gmail_id,password,account_type from admin ");
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean op = resultSet.next();
        int count=0;
        //Input Chek
        if (userInputEmail.equals("")){new animatefx.animation.Bounce(logIn_email).play();}
        if (userInputPassword.equals("")){new animatefx.animation.Bounce(userPassword).play();}
        if (userInputEmail.equals("")|| userPassword.getText().equals("")) {
            count++;

        } else {
            while (op) {
                //email chek
                if (resultSet.getString(1).equals(userInputEmail)) {
                    count++;
                    accountType=resultSet.getString(3);
                        //password chek ste......
                        if (resultSet.getString(2).equals(userInputPassword)) {
                            System.out.println(accountType);
                            switch (accountType){
                                case "Manager":managelodeStage();
                                    break;
                                case "crop Harvestor":harvestlodeStage();
                                    break;
                                case "Financial Officer":financiallodeStage();

                                    break;
                            }
                            op = false;

                        } else {
                            new Alert(Alert.AlertType.ERROR, "Invalid password").show();
                            op = false;
                        }

                } else {
                        op = resultSet.next();
                }
            }

        }
        if (count==0)new Alert(Alert.AlertType.ERROR, "Un Register User").show();
    }
    public static boolean emailValidator(String email){
        emailPattern = Pattern.compile("^([a-z|0-9]{3,})[@]([a-z]{2,})\\.(com|lk)$");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
    public void managelodeStage() throws IOException {
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ManagerDashboard/DashBoardForm.fxml"))));
        window.setResizable(true);
    }
    public void financiallodeStage() throws IOException {
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Financial_OfficerDashboard/Financial_OfficerDashboard.fxml"))));
    }public void harvestlodeStage() throws IOException {
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/crop_HarvestorDashboard/HarvestorDashboard.fxml"))));
    }
}