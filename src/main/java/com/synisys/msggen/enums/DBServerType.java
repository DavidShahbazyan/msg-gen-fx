package com.synisys.msggen.enums;

import com.synisys.msggen.utils.ResourceManager;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 2:27 PM <br/>
 */
public enum DBServerType {
    MSSQLServer(1, ResourceManager.getMessage("label.server.sql.name"),
            ResourceManager.getParam("DEFAULT.SQL.DRIVER"),
            ResourceManager.getParam("DEFAULT.SQL.JDBC.URL"),
            Integer.valueOf(ResourceManager.getParam("DEFAULT.SQL.PORT"))),
    ORAServer(2, ResourceManager.getMessage("label.server.ora.name"),
            ResourceManager.getParam("DEFAULT.ORA.DRIVER"),
            ResourceManager.getParam("DEFAULT.ORA.JDBC.URL"),
            Integer.valueOf(ResourceManager.getParam("DEFAULT.ORA.PORT"))),
    MySQLServer(3, ResourceManager.getMessage("label.server.mySql.name"),
            ResourceManager.getParam("DEFAULT.MySQL.DRIVER"),
            ResourceManager.getParam("DEFAULT.MySQL.JDBC.URL"),
            Integer.valueOf(ResourceManager.getParam("DEFAULT.MySQL.PORT"))),
    ;


    private Integer id;
    private String name;
    private String driver;
    private String jdbcUrlPattern;
    private Integer defaultPortNumber;

    DBServerType(Integer id, String name, String driver, String jdbcUrlPattern, Integer defaultPortNumber) {
        this.id = id;
        this.name = name;
        this.driver = driver;
        this.jdbcUrlPattern = jdbcUrlPattern;
        this.defaultPortNumber = defaultPortNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getJdbcUrlPattern() {
        return jdbcUrlPattern;
    }

    public Integer getDefaultPortNumber() {
        return defaultPortNumber;
    }
}
