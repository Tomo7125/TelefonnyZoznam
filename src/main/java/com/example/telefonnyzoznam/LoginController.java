package com.example.telefonnyzoznam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Label labelPrihlasenie;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldHeslo;
    @FXML
    private Button buttonPrihlas;
    @FXML
    private Button buttonRegister;
    @FXML
    private Label labelSprava;
    Connection connection;

    @FXML
    public void prihlasenie() throws IOException {

        if (!textFieldEmail.getText().isEmpty() | !textFieldHeslo.getText().isEmpty()){
            if (kontrolaHesla(textFieldEmail.getText() , textFieldHeslo.getText())){
                prepniNaDashboard();
            }else labelSprava.setText("Email alebo heslo nieje správne");

        }else labelSprava.setText("Zadaj email a heslo");

    }
    public void prepniNaDashboard() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();

        UserData userData = new UserData(textFieldEmail.getText()); // Vytvorenie objektu s údajmi
        DashBoardController dashboardController = loader.getController();
        dashboardController.setUserData(userData); // Nastavenie údajov v DashBoardController

        // Získanie hlavného Stage z triedy Start
        Stage stage = Start.getPrimaryStage();

        // Nastavenie nového layoutu ako scény
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Zobrazenie nového layoutu
        stage.setTitle("DashBoard");
        stage.show();


    }
    @FXML
    public void registracia() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = loader.load();

        // Získanie hlavného Stage z triedy Start
        Stage stage = Start.getPrimaryStage();

        // Nastavenie nového layoutu ako scény
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Zobrazenie nového layoutu
        stage.setTitle("Registracia");
        stage.show();
    }
    public boolean kontrolaHesla(String email , String password){
        boolean exists = false;
        connection = DatabaseConnector.getInstance().getConnection();


            String sql = "SELECT COUNT(*) FROM \"user\" WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    exists = (count > 0);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
    }

        return exists;
    }
}
