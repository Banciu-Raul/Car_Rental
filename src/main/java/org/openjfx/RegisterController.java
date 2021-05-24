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
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterController {

    @FXML
    private AnchorPane changeMe;

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private Text registerText;

    @FXML
    private TextField username;


    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void returnToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/primary.fxml"));
        Parent content = loader.load();
        changeMe.getChildren().setAll(content);

    }

    @FXML
    public void signUpOnAction(ActionEvent event) {
        if(!name.getText().isBlank() && !email.getText().isBlank() && !phone.getText().isBlank() && !username.getText().isBlank()
                && !passwordField.getText().isBlank() && !confirmPasswordField.getText().isBlank()){
            if(passwordField.getText().equals(confirmPasswordField.getText())){
                registerUser();
            } else {
                registerText.setText("Password does not match.");
            }
        } else{
            registerText.setText("All fields must be filled!");
        }
    }

    private void registerUser(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        System.out.println("DB Connected.");

        String Name = name.getText();
        String Email = email.getText();
        String Phone = phone.getText();
        String Password = passwordField.getText();
        String Username = username.getText();

        String insertFields = "INSERT INTO users (name, email, username, phone, password, role) VALUES ('";
        String insertValues =  Name + "','" + Email  + "','" + Username + "','" + Phone + "'," + "aes_encrypt(concat('" + Password + "', '" + Email + "'), 'key1234'), 'user')";
        String insertRegister = insertFields + insertValues;

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertRegister);
            System.out.println("Register success.");
        } catch (SQLException e){
            registerText.setText("Username or email already in use.");
            e.printStackTrace();
            e.getCause();
        }
    }

}
