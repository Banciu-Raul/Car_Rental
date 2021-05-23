package org.openjfx;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.SelectionModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Dealer implements Initializable {

    @FXML
    private AnchorPane changeMe;

    @FXML
    private TableView<Car> tableView;

    @FXML
    private TableColumn<Car, String> brand;

    @FXML
    private TableColumn<Car, String> model;

    @FXML
    private TableColumn<Car, String> year;

    @FXML
    private TableColumn<Car, String> seats;

    @FXML
    private TableColumn<Car, String> city;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField seatsField;

    @FXML
    private TextField cityField;


    private int id_selectedCar;
    private ObservableList<Car> cars;
    private int index = -1;
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pst = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       refreshTable();

    }


    public ObservableList<Car> getData() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        ObservableList<Car> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connectDB.prepareStatement("SELECT  * FROM cars ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Car(rs.getString("brand"), rs.getString("model"), rs.getString("year"), rs.getString("seats"), rs.getString("city"), Integer.parseInt(rs.getString("id"))));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }


        return list;

    }


    @FXML
    void addOnAction(ActionEvent event) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        System.out.println("DB Connected.");

        String brand = brandField.getText();
        String model = modelField.getText();
        String year = yearField.getText();
        String seats = seatsField.getText();
        String city = cityField.getText();

        String sql = "INSERT into cars(brand, model, year, seats, city) values(?, ?, ?, ?, ?)";
        try {
            pst = connectDB.prepareStatement(sql);
            pst.setString(1, brand);
            pst.setString(2, model);
            pst.setString(3, year);
            pst.setString(4, seats);
            pst.setString(5, city);
            pst.execute();
            refreshTable();
            System.out.println("Car added.");


        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    public void deleteOnAction(ActionEvent event) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sql = "delete from cars where id = " + id_selectedCar;
        try{
            pst = connectDB.prepareStatement(sql);
            pst.execute();
            refreshTable();
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    public void logOffOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/primary.fxml"));
        Parent content = loader.load();
        changeMe.getChildren().setAll(content);
    }

    @FXML
    public void updateOnAction(ActionEvent event) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        try{
            String value1 = brandField.getText();
            String value2 = modelField.getText();
            String value3 = yearField.getText();
            String value4 = seatsField.getText();
            String value5 = cityField.getText();

            String sql = "update cars set brand = '" + value1 + "', model ='" + value2 + "', year = '" + value3 + "', seats = '" + value4 + "', city = '" + value5 + "'";
            String where = "where id = " + id_selectedCar;
            pst = connectDB.prepareStatement(sql + where);
            pst.execute();
            refreshTable();



        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

    }

    /////////select cars///////////
    @FXML
    public void getSelect(MouseEvent event){
        index = tableView.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        Car test = new Car(brand.getCellData(index).toString(),model.getCellData(index).toString(), year.getCellData(index).toString(),seats.getCellData(index).toString(),city.getCellData(index).toString());
        brandField.setText(brand.getCellData(index).toString());
        modelField.setText(model.getCellData(index).toString());
        yearField.setText(year.getCellData(index).toString());
        seatsField.setText(seats.getCellData(index).toString());
        cityField.setText(city.getCellData(index).toString());

        for(Car car : cars){
            if(car.equals(test)){
                id_selectedCar = car.getid();
            }
        }
        System.out.println(id_selectedCar);



    }

    private void refreshTable(){
        brand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        model.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        year.setCellValueFactory(new PropertyValueFactory<Car, String>("year"));
        seats.setCellValueFactory(new PropertyValueFactory<Car, String>("seats"));
        city.setCellValueFactory(new PropertyValueFactory<Car, String>("city"));

        cars = getData();
        tableView.setItems(cars);
    }


}
