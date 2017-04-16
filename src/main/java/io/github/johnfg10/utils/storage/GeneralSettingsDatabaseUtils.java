package io.github.johnfg10.utils.storage;

import java.sql.*;

/**
 * Created by johnfg10 on 01/04/2017.
 */
public class GeneralSettingsDatabaseUtils {
    private String hostname = null;
    private int port = 0;
    private String username = null;
    private String password = null;
    private String schema = null;

    public GeneralSettingsDatabaseUtils(String hostname, int port, String username, String password, String schema){
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.schema = schema;
        try {
            excuteSql(
                    "CREATE TABLE IF NOT EXISTS botdatabase.generalsettings (" +
                            "id INT NOT NULL AUTO_INCREMENT," +
                            "PRIMARY KEY(id)," +
                            "UNIQUE (ID)," +
                            "guildid varchar(255) NOT NULL," +
                            "UNIQUE (guildid)," +
                            "defaultprefix varchar(255) NOT NULL," +
                            "modrole varchar(255) NOT NULL" +
                            "musicchannel varchar(255)" +
                            ")"
            );
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean excuteSql(String sql) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        boolean successful = false;

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection
                    (String.format("jdbc:mysql://%1s:%2s/%3s?serverTimezone=GMT&useSSL=false", hostname, String.valueOf(port), schema), username, password);

            Statement stmt = conn.createStatement();
            successful = stmt.execute(sql);
            stmt.close();
            conn.close();
        return successful;
    }

    public String getSetting(String setting, String guildid) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        ResultSet resultSet = null;
        String returnValue = "";
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection
                (String.format("jdbc:mysql://%1s:%2s/%3s?serverTimezone=GMT&useSSL=false", hostname, String.valueOf(port), schema), username, password);

        Statement stmt = conn.createStatement();
        resultSet = stmt.executeQuery(String.format("SELECT %1s FROM botdatabase.generalsettings WHERE guildid = '%2s';", setting, guildid));
        while (resultSet.next()){
            returnValue = resultSet.getString(setting);
        }
        stmt.close();
        conn.close();
        resultSet.close();
        return returnValue;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }
}
