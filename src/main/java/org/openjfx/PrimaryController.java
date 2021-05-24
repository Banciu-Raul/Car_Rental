package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

public class PrimaryController {


    @FXML
    private AnchorPane changeMe;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextField city;

    @FXML
    private Text validText;

    private int okay = 0;
    private int okayC = 0;
    private String email;

    private String role;

    public static String cityStatic;

    @FXML
    public void loginOnAction(ActionEvent event) throws IOException {
        String nextFXML;
        if (!username.getText().isBlank() && !password.getText().isBlank() && !city.getText().isBlank()) {
            getUser();
            validateLogin();
            validateCity();

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
                "' AND replace(cast(aes_decrypt(password, 'key1234') as char(100)), '" + email + "', '') = '" + password.getText() + "'";

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
                email = queryResult.getString("email");
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
        ObservableList<Car> list = FXCollections.observableArrayList();

        // String verifyLogin = "SELECT count(1) FROM cars where city = '" + city.getText() +"'";

        try {
            PreparedStatement ps = connectDB.prepareStatement("SELECT  * FROM cars where city = '" + city.getText() + "'" );
//            PreparedStatement ps = connectDB.prepareStatement("SELECT  * FROM cars where city = 'Deva'" );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Car(rs.getString("brand"), rs.getString("model"), rs.getString("year"), rs.getString("seats"), rs.getString("city"), Integer.parseInt(rs.getString("id"))));

            }
            if(list.isEmpty() == false){
                okayC = 1;
                cityStatic = list.get(0).getCity();

            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
