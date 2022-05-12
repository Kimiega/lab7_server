package connection;

import collection.City;
import collection.UserToken;
import utils.DateAdapter;
import ioManager.RequestElement;
import utils.Encryptor;

import java.sql.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class SQLManagerDB {
    private static final String URL = "jdbc:postgresql://pg:5432/studs";
    private static final String addWithIDTemplate = "INSERT INTO world (id, name, coordinateX, coordinateY," +
            " creationDate, area, population, metersabovesealevel, timezone, agglomeration" +
            ", climate, governorname, " +
            "governorage, governorbirthday, owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String addWithoutIDTemplate = "INSERT INTO world (id, name, coordinateX, coordinateY," +
            " creationDate, area, population, metersabovesealevel, timezone, agglomeration" +
            ", climate, governorname, " +
            "governorage, governorbirthday, owner) VALUES (nextval('ids'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";

    private final Connection connection;
    public SQLManagerDB(String adminLogin, String password) throws SQLException {
        this.connection = DriverManager.getConnection(URL, adminLogin, password);
    }

    public ConcurrentSkipListSet<City> getCollection() throws SQLException {
        Statement statement = connection.createStatement();
        ConcurrentSkipListSet<City> world = new ConcurrentSkipListSet<>();
        ResultSet result = statement.executeQuery("SELECT * FROM world;");
        while (result.next()) {
            String[] values = new String[15];

            for (int i = 0; i < 15; i++) {
                values[i] = result.getString(i+1);
            }
            world.add(RequestElement.readElement(values));
        }
        statement.close();
        return world;
    }
    public void addWithID(City city, UserToken user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(addWithIDTemplate);
        statement.setInt(1,city.getId());
        statement.setString(2, city.getName());
        statement.setLong(3, city.getCoordinates().getX());
        statement.setFloat(4, city.getCoordinates().getY());
        statement.setString(5, DateAdapter.dateToString(city.getCreationDate()));
        statement.setInt(6, city.getArea());
        statement.setLong(7, city.getPopulation());
        statement.setFloat(8, city.getMetersAboveSeaLevel());
        statement.setInt(9, city.getTimezone());
        statement.setLong(10, city.getAgglomeration());
        if (city.getClimate()==null)
            statement.setString(11,null);
        else
            statement.setString(11, city.getClimate().toString());
        if (city.getGovernor()==null)
        {
            statement.setString(12, null);
            statement.setLong(13, 0);
            statement.setString(14, null);
        }
        else {
            statement.setString(12, city.getGovernor().getName());
            statement.setLong(13, city.getGovernor().getAge());
            statement.setString(14, DateAdapter.dateToString(city.getGovernor().getBirthday()));
        }
        statement.setString(15, user.getLogin());
        statement.executeUpdate();
        statement.close();
    }
    public void addWithoutID(City city, UserToken user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(addWithoutIDTemplate);
        statement.setString(1, city.getName());
        statement.setLong(2, city.getCoordinates().getX());
        statement.setFloat(3, city.getCoordinates().getY());
        statement.setString(4, DateAdapter.dateToString(city.getCreationDate()));
        statement.setInt(5, city.getArea());
        statement.setLong(6, city.getPopulation());
        statement.setFloat(7, city.getMetersAboveSeaLevel());
        statement.setInt(8, city.getTimezone());
        statement.setLong(9, city.getAgglomeration());
        if (city.getClimate()==null)
            statement.setString(10,null);
        else
            statement.setString(10, city.getClimate().toString());
        if (city.getGovernor()==null)
        {
            statement.setString(11, null);
            statement.setLong(12, 0);
            statement.setString(13, null);
        }
        else {
            statement.setString(11, city.getGovernor().getName());
            statement.setLong(12, city.getGovernor().getAge());
            statement.setString(13, DateAdapter.dateToString(city.getGovernor().getBirthday()));
        }
        statement.setString(14, user.getLogin());
        statement.executeUpdate();
        statement.close();
    }
    public int remove(City city, UserToken user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM world WHERE id= ? AND owner= ?;");
        statement.setInt(1, city.getId());
        statement.setString(2, user.getLogin());
        int res = statement.executeUpdate();
        statement.close();
        return res;
    }
    public int clear(UserToken user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM world WHERE owner= ?;");
        statement.setString(1, user.getLogin());
        int res = statement.executeUpdate();
        statement.close();
        return res;
    }
    public void registerUser(UserToken user) throws SQLException {
        String login = user.getLogin();
        String password = Encryptor.encryptSHA384(user.getPassword());
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (login, password) VALUES (?, ?);");
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeUpdate();
        statement.close();
    }
    public boolean checkUser(UserToken user) throws SQLException {
        Statement statement = connection.createStatement();
        String login = user.getLogin();
        String password = Encryptor.encryptSHA384(user.getPassword());
        ResultSet result = statement.executeQuery("SELECT * FROM users;");
        boolean f = false;
        while (result.next()) {
            if (login.equals(result.getString(1))) {
                if (password.equals(result.getString(2))) {
                    f = true;
                } else {
                    return false;
                }
            }
        }
        statement.close();
        return f;
    }
    public int getLastID() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM world");
        ResultSet result = statement.executeQuery();
        int id = 0;
        while (result.next()) {
            id = result.getInt(1);
        }
        statement.close();
        return id;
    }
    public void restartSeq(int id) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("ALTER SEQUENCE ids RESTART WITH "+id+";");
        //statement.setInt(1,id);
        statement.executeUpdate();
        statement.close();
    }
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
