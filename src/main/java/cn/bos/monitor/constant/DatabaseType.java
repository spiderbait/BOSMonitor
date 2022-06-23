package cn.bos.monitor.constant;

public enum DatabaseType {
    MYSQL("com.mysql.cj.jdbc.Driver"),ORACLE("oracle.jdbc.driver.OracleDriver");

    private String driverClass;

    private DatabaseType(String driver) {
        this.driverClass = driver;
    }

    public static DatabaseType getInstance(String type) {
        try {
            return DatabaseType.valueOf(type.toUpperCase());
        }catch (Exception e) {
            return null;
        }
    }

    public String getDriverClass() {
        return driverClass;
    }
}
