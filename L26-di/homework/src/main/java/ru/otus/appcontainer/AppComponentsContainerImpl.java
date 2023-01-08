package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        // You code here...
        Object configClassInstance = configClass.getConstructor().newInstance();

        List<Method> methods = Arrays.stream(configClass.getMethods()).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order())).toList();

        String componentName;
        Object component;
        Parameter[] parameters;
        for (Method method : methods) {
            parameters = method.getParameters();
            componentName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(componentName)) {
                throw new Exception("duplicate component name: " + componentName);
            }

            if (parameters.length == 0) {
                component = method.invoke(configClassInstance);
            } else {
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < args.length; i++) {
                    args[i] = getAppComponent(parameters[i].getType());
                }
                component = method.invoke(configClassInstance, args);
            }

            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        }
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream().filter(component -> componentClass.isAssignableFrom(component.getClass())).findAny().orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
