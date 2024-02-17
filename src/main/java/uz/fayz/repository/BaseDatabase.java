package uz.fayz.repository;


import uz.fayz.util.BotConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDatabase {



    protected Connection connection() {
        try {
            return DriverManager.getConnection(BotConstants.URL, BotConstants.DATABASE_USERNAME, BotConstants.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("database auth is failed for username %s and database %s", BotConstants.DATABASE_USERNAME, BotConstants.PASSWORD));
        }
    }
}
