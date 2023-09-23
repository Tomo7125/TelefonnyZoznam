package com.example.telefonnyzoznam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private Label nadpis;
    @FXML
    private TextField textFieldMeno;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldHeslo;
    @FXML
    private Button buttonRegistracia;
    @FXML
    private Button buttonPrihlasenie;
    @FXML
    private Label labelSpravaNadEmailom;

    Connection connection;
    @FXML
    public void Registracia() throws IOException, SQLException {
        connection = DatabaseConnector.getInstance().getConnection();
        if (!textFieldEmail.getText().isEmpty() | !textFieldHeslo.getText().isEmpty() | !textFieldMeno.getText().isEmpty()){
            if (!emailExists(textFieldEmail.getText())){
                zaregistruj(textFieldEmail.getText() , textFieldHeslo.getText() , textFieldMeno.getText());
                uspesnaRegistracia();
                prihlasenie();
                connection.close();
            }else labelSpravaNadEmailom.setText("Email už existuje");
        }else labelSpravaNadEmailom.setText("Všetky polia musia byť vyplnené");

    }
    @FXML
    public void prihlasenie() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Získanie hlavného Stage z triedy Start
        Stage stage = Start.getPrimaryStage();

        // Nastavenie nového layoutu ako scény
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Zobrazenie nového layoutu
        stage.setTitle("Login");
        stage.show();
    }
    // Pre túto aplikaciu nepoužívam šifrovanie hesla v reálnej aplikáci by prebehlo šifrovanie hesla
    public void zaregistruj(String email , String password , String name){

        String sql = "INSERT INTO \"user\" (name, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            preparedStatement.executeUpdate(); // Vykonanie dotazu
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean emailExists(String email) {
        boolean exists = false;


        String sql = "SELECT COUNT(*) FROM \"user\" WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    exists = (count > 0);
                }
            }
        }
        catch (SQLException e) {
        e.printStackTrace();
        }

        return exists;
    }
    public void uspesnaRegistracia() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrácia úspešná");
        alert.setHeaderText(null);
        alert.setContentText("Registrácia bola úspešná. Môžete sa prihlásiť.");

        alert.showAndWait(); // Toto zobrazi oznamovacie okno a caka, kym ho uzivatel zatvori
    }

    public void initialize() {
    }
}