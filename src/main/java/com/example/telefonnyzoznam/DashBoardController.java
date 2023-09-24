package com.example.telefonnyzoznam;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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
    @FXML
    public void pridajKontaktDoDB(){
        Dialog<Kontakt> dialog = new Dialog<>();
        dialog.setTitle("Nový kontakt");
        dialog.setWidth(350);
        dialog.setHeight(250);

        vytvorObsahDialogu(dialog);
        final Optional<Kontakt> vysledok = dialog.showAndWait();
        if (vysledok.isPresent()) {
            Kontakt kontakt = vysledok.get();
            spravcaKontaktov.pridaj(user.getEmail(), kontakt.meno , kontakt.cislo);

            // Aktualizujte ListView po pridaní kontaktu
            listViewZoznamKontaktov.getItems().setAll(zoznamKontaktov());
        }
    }
    public ObservableList<String> zoznamKontaktov(){
        ObservableList<String> zoznam = FXCollections.observableArrayList();
        for (Kontakt k : spravcaKontaktov.getOsoby()){
            zoznam.add(k.meno);
        }return zoznam;
    }
    public void vytvorObsahDialogu(Dialog<Kontakt> dialog){

        ButtonType createButtonType = new ButtonType("OK" , ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().setAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField menoTextField = new TextField();
        TextField cisloTextField = new TextField();
        Label menoLabel = new Label("Meno : ");
        Label cisloLabel = new Label("Cislo : ");

        grid.add(menoLabel,0,0);
        grid.add(menoTextField,1,0);
        grid.add(cisloLabel,0,1);
        grid.add(cisloTextField,1,1);

        dialog.setResultConverter(new Callback<ButtonType, Kontakt>() {
            @Override
            public Kontakt call(ButtonType param) {
                try {
                    return new Kontakt(menoTextField.getText(), cisloTextField.getText());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Chyba: " + ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Kontakt sa nepodarilo nastaviť!");
                    alert.showAndWait();
                    return null;
                }
            }
        });
        dialog.getDialogPane().setContent(grid);

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
