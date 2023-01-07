package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    private boolean cacheOn;


    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = new MyCache<>();
        this.cacheOn = false;
    }

    public boolean cacheOnStatus(){
        return cacheOn;
    }

    public void switchCacheStatus(){
        cacheOn = !cacheOn;
        log.info("switch cache status: {}", cacheOn);
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                if(cacheOn) putInCache(createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            if(cacheOn) putInCache(manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        if (cacheOn){
            Optional<Manager> fromCache = getFromCache(no);
            return fromCache.or(() -> getOneFromDBAndUpdateCache(no));
        }
        return getOneFromDB(no);
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            managerList.forEach(this::putInCache);
            return managerList;
        });
    }

    private void putInCache(Manager manager){
        cache.put(manager.getNo().toString(), manager);
    }

    private Optional<Manager> getFromCache(long id){
        return Optional.ofNullable(cache.get(String.valueOf(id)));
    }

    private Optional<Manager> getOneFromDB(long id){
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, id);
            log.debug("manager load from db: {}", managerOptional);
            return managerOptional;
        });
    }

    private Optional<Manager> getOneFromDBAndUpdateCache(long id) {
        Optional<Manager> oneFromDB = getOneFromDB(id);
        oneFromDB.ifPresent(this::putInCache);
        log.info("manager load from db and update cache: {}", oneFromDB);
        return oneFromDB;
    }
}
