package uz.fayz.repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static uz.fayz.util.BotConstants.*;

public abstract class BaseDatabase {



    protected Connection connection() {
        try {
            return DriverManager.getConnection(URL, DATABASE_USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("database auth is failed for username %s and database %s", DATABASE_USERNAME, PASSWORD));
        }
    }
}
