package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{

    private final String selectAllSql;
    private final String selectByIdSql;

    private final String insertSql;
    private final String updateSql;
    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        var tableName = entityClassMetaData.getName();
        var allFieldsName = entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", "));
        var allFieldsNameWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", "));
        var signs = entityClassMetaData.getFieldsWithoutId().stream().map(field -> "?").collect(Collectors.joining(", "));
        var setFields = entityClassMetaData.getFieldsWithoutId().stream().map(field -> field.getName() + " = ?").collect(Collectors.joining(", "));


        this.selectAllSql = String.format("select %s from %s", allFieldsName, tableName);
        this.selectByIdSql = String.format("select %s from %s where %s  = ?", allFieldsName, tableName, entityClassMetaData.getIdField().getName());
        this.insertSql = String.format("insert into %s(%s) values (%s)", tableName, allFieldsNameWithoutId, signs);
        this.updateSql = String.format("update %s set %s where %s = ?", tableName, setFields, entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
