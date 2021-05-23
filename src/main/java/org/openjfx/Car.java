package org.openjfx;

public class Car {

    private String brand;
    private String model;
    private String year;
    private String seats;
    private String city;
    private int id;

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }


    public Car(String brand, String model, String year, String seats, String city, int id) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.seats = seats;
        this.city = city;
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Car(){

    }

    public Car(String brand, String model, String year, String seats, String city) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.seats = seats;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return brand.equals(car.brand) && model.equals(car.model) && year.equals(car.year) && seats.equals(car.seats) && city.equals(car.city);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
