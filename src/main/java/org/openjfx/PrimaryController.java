package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrimaryController {


    @FXML
    private AnchorPane changeMe;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private static TextField city;

    @FXML
    private Text validText;

    private int okay = 0;
    private int okayC = 0;

    private String role;

    @FXML
    public void loginOnAction(ActionEvent event) throws IOException {
        String nextFXML;
        if (!username.getText().isBlank() && !password.getText().isBlank() && city.getText().isBlank()) {
            validateLogin();
            validateCity();
            getUser();

            if (role.equals("admin")) {
                nextFXML = "fxml/Dealer.fxml";
            } else nextFXML = "fxml/Dashboard.fxml";

            if (okay == 1 && okayC == 1) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(nextFXML));
                Parent content = loader.load();
                changeMe.getChildren().setAll(content);
            }

        } else {
            validText.setText("Please enter all fields.");
        }
    }

    @FXML
    public void registerOnAction(ActionEvent event)  throws  IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/register.fxml"));
        Parent content = loader.load();
        changeMe.getChildren().setAll(content);
    }

    private void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM users WHERE username = '" + username.getText() +
                "' AND replace(cast(aes_decrypt(password, 'key1234') as char(100)), '" + username.getText() + "', '') = '" + password.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    okay = 1;
                } else {
                    validText.setText("Invalid login. Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void getUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String getRow = "SELECT * FROM users WHERE username = '" + username.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getRow);
            while (queryResult.next()) {
                role = queryResult.getString("role");
                System.out.println(queryResult.getString("id"));
                System.out.println(role);
            }
        } catch (SQLException e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    public void validateCity(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM cars where city = '" + city.getText() +"'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) > 0) {
                    okayC = 1;
                } else {
                    validText.setText("Please select another city..");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }


    public static String getCity() {
        return city.getText();
    }

}
