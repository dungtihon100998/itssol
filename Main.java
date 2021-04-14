package car;
import model.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Main {
    private static String[] brands = {"TOYOTA", "BMW", "HYUNDAI"};
    private List<Car> carList;

    public static Connection getJDBCConnection() {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/db_car1";
        final String DB_USER = "root";
        final String DB_PASS = "root";
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        Main main = new Main();
        main.init();
    }

    public List<Car> getAll() throws SQLException {
        List<Car> carList = new ArrayList<>();
        Connection connection = getJDBCConnection();
        String sql = "select * from car";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            Car car = new Car();
            car.setId(Long.parseLong(result.getString("id")));
            car.setName(result.getString("name"));
            car.setBrand(result.getString("brand"));
            car.setNumberPlate(result.getString("numberPlate"));
            car.setYearOfManufacture(Integer.parseInt(result.getString("yearOfManufacture")));
            if (result.getString("actionDuration") != null) {
                car.setActionDuration(Long.parseLong(result.getString("actionDuration")));
            }
            car.setHaveInsurance(Long.parseLong(result.getString("haveInsurance")) == 0 ? false : true);
            if (result.getString("havePositioningDevice") != null) {
                car.setHavePositioningDevice(Long.parseLong(result.getString("havePositioningDevice")) == 0 ? false : true);
            }

            carList.add(car);
        }

        return carList;
    }

    public void init() throws SQLException {
        Connection connection = getJDBCConnection();
        Statement statement = connection.createStatement();
        Random random = new Random();
        carList = getAll();
        if (carList == null || carList.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                Car car = new Car();
                car.setName("Car #(" + i + ")");
                car.setBrand(brands[random.nextInt(3)]);
                car.setNumberPlate(String.format("%05d", random.nextInt(100000)));
                car.setYearOfManufacture(random.nextInt(33) + 1980);
                car.setHaveInsurance(random.nextInt(2) == 0 ? false : true);
                String sql = "insert into car(name, brand, yearOfManufacture, numberPlate, haveInsurance) value " +
                        "('" + car.getName() + "'," +
                        "'" + car.getBrand() + "'," +
                        car.getYearOfManufacture() + "," +
                        car.getNumberPlate() + "," +
                        car.getHaveInsurance() +
                        ")";
                statement.executeUpdate(sql);
            }
            carList = getAll();
        }

    }
}
