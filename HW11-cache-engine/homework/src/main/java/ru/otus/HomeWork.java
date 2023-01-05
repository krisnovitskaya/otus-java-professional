package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;


////-Xms8m -Xmx8m
//16:54:50.502 [main] INFO ru.otus.HomeWork - time without cache 215
//16:54:50.502 [main] INFO ru.otus.HomeWork - time with cache 91
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);


        log.info("cache status {}", dbServiceManager.cacheOnStatus());
        Manager firstManager = dbServiceManager.saveManager(new Manager("test for id"));
        long index = firstManager.getNo() + 1;
        long lastIndex = index + 200;

        for (long i = index; i < lastIndex; i++) {
            dbServiceManager.saveManager(new Manager("test" + i));
        }

        long test1Start = System.currentTimeMillis();
        for (long i = lastIndex - 1; i >= index; i--) {
            System.out.println(dbServiceManager.getManager(i));
        }
        long test1End = System.currentTimeMillis();


        dbServiceManager.switchCacheStatus();

        long index2 = lastIndex;
        long lastIndex2 = index2 + 200;

        for (long i = index2; i < lastIndex2; i++) {
            dbServiceManager.saveManager(new Manager("test2" + i));
        }

        long test2Start = System.currentTimeMillis();
        for (long i = lastIndex2 - 1; i >= index2; i--) {
            System.out.println(dbServiceManager.getManager(i));
        }
        long test2End = System.currentTimeMillis();

        log.info("time without cache {}", test1End - test1Start);
        log.info("time with cache {}", test2End - test2Start);

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
