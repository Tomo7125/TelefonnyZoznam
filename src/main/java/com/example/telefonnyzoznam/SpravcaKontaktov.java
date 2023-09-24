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

    public void pridaj(String email, String meno , String cislo){
        connection = DatabaseConnector.getInstance().getConnection();
        String sql = "INSERT INTO \"contacts\" (email, name, number) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, meno);
            statement.setString(3, cislo);

            int riadky = statement.executeUpdate();
            if (riadky > 0) {
                System.out.println("Kontakt bol úspešne pridaný.");
            } else {
                System.out.println("Pridanie kontaktu zlyhalo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Chyba pri pridávaní kontaktu do databázy: " + e.getMessage());
        }
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
