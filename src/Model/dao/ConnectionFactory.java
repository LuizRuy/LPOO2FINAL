package Model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Properties properties;

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException, IOException {
        readProperties();
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    private static void readProperties() throws IOException {
        if (properties == null) {
            Properties props = new Properties();
            FileInputStream file = new FileInputStream("./src/Model/dao/DataBase.properties");
            props.load(file);
            properties = props;
        }
    }
}
