package com.example.telefonnyzoznam;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    private Label labelPocetKontaktovPrihlaseneho;
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
    SpravcaKontaktov spravcaKontaktov = new SpravcaKontaktov();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userEmail = UserData.getInstance().getEmail(); // Získať e-mail z Singletonu
        user.setEmail(userEmail);
        spravcaKontaktov.obnovOsoby(user.getEmail());
        listViewZoznamKontaktov.setItems(zoznamKontaktov());
        user.setName(zistiMenoPodlaEmailu(user.getEmail()));
        labelMenoPrihlaseneho.setText(user.getName());
        labelPocetKontaktovPrihlaseneho.setText(String.valueOf(spravcaKontaktov.getOsoby().size()));
        listViewZoznamKontaktov.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                obnov();
            }
        });
        if (!listViewZoznamKontaktov.getItems().isEmpty()){
            listViewZoznamKontaktov.getSelectionModel().select(0);
        }
    }
    public void obnov(){
         String vybranaOsoba = listViewZoznamKontaktov.getSelectionModel().getSelectedItem().toString();
        if (vybranaOsoba != null){
            for (Kontakt k : spravcaKontaktov.getOsoby()){
                if (k.meno == vybranaOsoba){
                    labelMenoKontaktu.setText(k.meno);
                    labelCisloKontaktu.setText(k.cislo);
                }
            }

        }
    }
    public ObservableList<String> zoznamKontaktov(){
        ObservableList<String> zoznam = FXCollections.observableArrayList();
        for (Kontakt k : spravcaKontaktov.getOsoby()){
            zoznam.add(k.meno);
        }return zoznam;
    }
    public String zistiMenoPodlaEmailu(String email) {
        String meno = null;
        try {
            connection = DatabaseConnector.getInstance().getConnection();
            String sql = "SELECT name FROM \"user\" WHERE email = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        meno = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meno;
    }
}
