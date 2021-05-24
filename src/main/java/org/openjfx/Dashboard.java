package org.openjfx;

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
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

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

    private ObservableList<Car> cars;

    @FXML
    private AnchorPane changeMe;

    @FXML
    private Text rentalNumber;


    @FXML
    private Text grade;

    @FXML
    private TextField gradeField;

    private double n;

    private DecimalFormat decimalFormat = new DecimalFormat("#.00");



    @FXML
    public void logOffOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/primary.fxml"));
        Parent content = loader.load();
        changeMe.getChildren().setAll(content);
    }

    public ObservableList<Car> getData() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        ObservableList<Car> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connectDB.prepareStatement("SELECT  * FROM cars where city = '" + PrimaryController.cityStatic + "'" );
//            PreparedStatement ps = connectDB.prepareStatement("SELECT  * FROM cars where city = 'Deva'" );
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        brand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        model.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        year.setCellValueFactory(new PropertyValueFactory<Car, String>("year"));
        seats.setCellValueFactory(new PropertyValueFactory<Car, String>("seats"));
        city.setCellValueFactory(new PropertyValueFactory<Car, String>("city"));

        cars = getData();
        tableView.setItems(cars);
        n = Math.random()*4 + 1;
        grade.setText(decimalFormat.format(n));


    }


    public void selectedRow(MouseEvent mouseEvent) {
        rentalNumber.setVisible(true);
    }

    public void calculate(ActionEvent event) {
        double aux = (n + Double.parseDouble(gradeField.getText()))/2;
        grade.setText(decimalFormat.format(aux));
    }
}
