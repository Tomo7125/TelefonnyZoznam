package com.example.telefonnyzoznam;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    private User user = new User();
    @FXML
    private ImageView imageViewProfilovka;
    @FXML
    private ListView listViewZoznamKontaktov;
    @FXML
    private Label labelMeno;
    @FXML
    private Label labelMenoPrihlaseneho;
    @FXML
    private Label LabelPocetKontaktovPrihlaseneho;
    @FXML
    private Label labelPocetKontaktov;
    @FXML
    private Label labelMenoKon;
    @FXML
    private Label labelMenoKontaktu;
    @FXML
    private Label labelCislo;
    @FXML
    private Label labelCisloKontaktu;
    @FXML
    private Button buttonPridajKontakt;
    @FXML
    private Button buttonOdstranKontakt;
    Connection connection;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SpravcaKontaktov spravcaKontaktov = new SpravcaKontaktov(user.getEmail());
        listViewZoznamKontaktov.setItems(spravcaKontaktov.getOsoby());
    }

    public void setUserData(UserData userData) {
        this.user.setEmail(userData.getEmail());
    }
}
