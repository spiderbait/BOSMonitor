package cn.bos.monitor.dao;

import cn.bos.monitor.constant.DatabaseType;
import cn.bos.monitor.entity.JdbcConfig;
import cn.bos.monitor.entity.Rule;
import cn.bos.monitor.util.EncryptUtil;
import cn.bos.monitor.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityFetcher {

    Logger logger = LoggerFactory.getLogger(EntityFetcher.class);

    private JdbcConfig jdbcConfig = new JdbcConfig();

    public EntityFetcher() {
        String dbUrl = PropertiesUtil.getProperty("sys.config.mysql.url");
        String user = PropertiesUtil.getProperty("sys.config.mysql.user");
        String pwd = PropertiesUtil.getProperty("sys.config.mysql.password");
        jdbcConfig.setJdbcId("default");
        jdbcConfig.setJdbcType(DatabaseType.MYSQL.name());
        jdbcConfig.setJdbcUrl(dbUrl);
        jdbcConfig.setJdbcUser(user);
        jdbcConfig.setJdbcPwd(pwd);
    }

    public List<cn.bos.monitor.entity.List> getLists() throws SQLException, ClassNotFoundException {
        DataSourceConnector connector = DataSourceConnector.createDataSource(jdbcConfig);
        ResultSet rs = connector.execQuery("select * from list");
        logger.debug("Get list query executed.");
        List<cn.bosc.monitorcontrol.entity.List> result = new ArrayList<cn.bosc.monitorcontrol.entity.List>();
        while(rs.next()){
            cn.bosc.monitorcontrol.entity.List row = new cn.bos.monitor.entity.List();
            row.setMid(rs.getInt("mid"));
            row.setName(rs.getString("name"));
            row.setDescription(rs.getString("description"));
            logger.debug("Get list: mid = " + rs.getInt("mid")
                    + ", name = " + rs.getString("name")
                    + ", description = " + rs.getString("description"));
            result.add(row);
        }
        connector.closeConnection();
        return result;
    }

    public List<Rule> getRules() throws SQLException{
        DataSourceConnector connector = DataSourceConnector.createDataSource(jdbcConfig);
        ResultSet rs = connector.execQuery("select * from rule where enable=1");
        logger.debug("Get rule query executed.");
        List<Rule> result = new ArrayList<Rule>();
        while(rs.next()){
            Rule row = new Rule();
            row.setId(rs.getInt("id"));
            row.setMid(rs.getInt("mid"));
            row.setType(rs.getString("type"));
            row.setName(rs.getString("name"));
            row.setSpan(rs.getString("span"));
            row.setPath(rs.getString("path"));
            row.setJobList(rs.getString("job_list"));
            row.setWhereClause(rs.getString("where_clause"));
            row.setEndKeyword(rs.getString("end_keyword"));
            row.setReceivers(rs.getString("receivers"));
            row.setQuerySql(rs.getString("query_sql"));
            logger.debug("Rule query: id = "
                    + rs.getInt("id") + ", mid = "
                    + rs.getInt("mid") + ", name = "
                    + rs.getString("name"));
            result.add(row);
        }
        connector.closeConnection();
        logger.debug("Get rule query connection detached.");
        return result;
    }

    public List<Rule> getRulesByMid(int mid) throws SQLException{
        DataSourceConnector connector = DataSourceConnector.createDataSource(jdbcConfig);
        ResultSet rs = connector.execQuery("select * from rule where mid=" + mid + " and enable=1");
        List<Rule> result = new ArrayList<>();
        while(rs.next()){
            //
            Rule row = new Rule();
            row.setId(rs.getInt("id"));
            row.setMid(rs.getInt("mid"));
            row.setType(rs.getString("type"));
            row.setSpan(rs.getString("span"));
            row.setPath(rs.getString("path"));
            row.setJobList(rs.getString("job_list"));
            row.setQuerySql(rs.getString("query_sql"));
            result.add(row);
        }
        connector.closeConnection();
        return result;
    }

    public JdbcConfig getJdbcConfig(int mid) {
        DataSourceConnector oc = DataSourceConnector.createDataSource(jdbcConfig);
        JdbcConfig jdbcConfig = null;
        try {
            ResultSet rs = oc.execQuery("SELECT mid, jdbc_name, jdbc_type, jdbc_url, jdbc_database, jdbc_user," +
                    " jdbc_password, pin_code, test_sql FROM jdbc_config where mid=" + mid);
            if (rs.next()) {
                jdbcConfig = new JdbcConfig();
                jdbcConfig.setJdbcId(String.valueOf(mid));
                jdbcConfig.setJdbcName(rs.getString(2));
                jdbcConfig.setJdbcType(rs.getString(3));
                jdbcConfig.setJdbcUrl(rs.getString(4));
                jdbcConfig.setJdbcDatabase(rs.getString(5));
                jdbcConfig.setJdbcUser(rs.getString(6));
                jdbcConfig.setJdbcPwd(EncryptUtil.decrypt(rs.getString(7), rs.getString(8)));
                jdbcConfig.setTestSql(rs.getString(9));
            }
        }catch (Exception e) {
            jdbcConfig = null;
            logger.error("fetch jdbcconfig["+mid+"]",e);
        } finally {
            oc.closeConnection();
        }
        return jdbcConfig;
    }
}
