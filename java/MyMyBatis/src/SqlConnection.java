package ch.asynk;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlConnection
{
    private Logger logger;
    private SqlSessionManager manager;

    public void info(String msg) { logger.info(msg); }
    public void error(Exception e) { logger.error("SQL ERROR : " + e.getMessage()); }

    public SqlConnection()
    {
        this.logger = logger = LoggerFactory.getLogger(Main.class);
        PooledDataSource ds = new PooledDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:test;sql.syntax_pgs=true;check_props=true", "username", "password");
        ds.setDefaultAutoCommit(false);
        // ds.setPoolMaximumActiveConnections();
        // ds.setPoolMaximumIdleConnections();
        // ds.setPoolMaximumCheckoutTime();
        // ds.setPoolTimeToWait();
        // ds.setPoolPingQuery();
        // ds.setPoolPingEnabled();
        // ds.setPoolPingConnectionsNotUsedFor();

        Environment e = new Environment("default", new JdbcTransactionFactory(), ds);
        // e.();

        Configuration c = new Configuration(e);
        // c.();

        SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(c);
        // ssf.();

        SqlSessionManager sm = SqlSessionManager.newInstance(ssf);
        // sm.();

        this.manager = sm;
    }

    protected void registerMapper(String alias, Class<?> beanClass, Class<?> mapperClass)
    {
        logger.info("register "+alias+ " -> "+beanClass.getName()+" "+mapperClass.getName());
        Configuration config = manager.getConfiguration();
        config.addMapper(mapperClass);
        config.getTypeAliasRegistry().registerAlias(alias, beanClass);
    }

    public void runScript(String filename) throws java.io.IOException
    {
        info("Run script : " + filename);
        SqlSession session = manager.openSession();
        Reader reader = Resources.getResourceAsReader(filename);
        ScriptRunner runner = new ScriptRunner(session.getConnection());
        runner.setLogWriter(null);
        runner.runScript(reader);
        reader.close();
        session.close();
    }

    public SqlSession openSqlSession()
    {
        // no autocommit
        return manager.openSession(false);
    }
}
