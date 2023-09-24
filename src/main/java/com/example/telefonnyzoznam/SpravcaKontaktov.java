package com.example.telefonnyzoznam;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpravcaKontaktov {
    String email;


    Connection connection;

    private ObservableList<Kontakt> osoby = FXCollections.observableArrayList();

    public ObservableList<Kontakt> getOsoby() {
        return osoby;
    }

    public void pridaj(Kontakt osoba){
        osoby.add(osoba);
    }
    public void odober(Kontakt osoba){
        osoby.remove(osoba);
    }

    public void obnovOsoby(String email) {
        List<Kontakt> zoznam = getContactsByEmail(email);
        for (Kontakt k : zoznam) {
            osoby.add(k);
        }
    }
    public List<Kontakt> getContactsByEmail(String email) {
        connection = DatabaseConnector.getInstance().getConnection();
        List<Kontakt> contacts = new ArrayList<>();


        String sql = "SELECT name, number FROM \"contacts\" WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String number = resultSet.getString("number");
                    contacts.add(new Kontakt(name, number));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }
}
