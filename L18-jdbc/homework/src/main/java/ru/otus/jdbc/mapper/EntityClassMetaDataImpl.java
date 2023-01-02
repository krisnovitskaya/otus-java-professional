package ru.otus.jdbc.mapper;

import ru.otus.crm.model.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{

    private final String name;
    private final Constructor<T> constructor;

    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityType){
        this.name = entityType.getSimpleName().toLowerCase();

        this.allFields = Arrays.stream(entityType.getDeclaredFields()).toList();

        this.idField = allFields.stream().peek(field -> System.out.println(field.getAnnotation(Id.class))).filter(field -> field.getAnnotation(Id.class) != null).findFirst().orElseThrow();

        this.fieldsWithoutId = allFields.stream().filter(field -> !field.equals(idField)).toList();

        Class<?>[] paramType = new Class[allFields.size()];
        for (int i = 0; i < paramType.length; i++) {
            paramType[i] = allFields.get(i).getType();
        }
        try {
            this.constructor = entityType.getDeclaredConstructor(paramType);
        }catch (NoSuchMethodException e){
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
