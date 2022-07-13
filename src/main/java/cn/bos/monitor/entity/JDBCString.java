package cn.bos.monitor.entity;

public class JDBCString {

    String driver;
    String user;
    String password;
    String url;

    public JDBCString(String driver, String user, String password, String url) {
        this.driver = driver;
        this.user = user;
        this.password = password;
        this.url = url;
    }

    public JDBCString() {

    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
