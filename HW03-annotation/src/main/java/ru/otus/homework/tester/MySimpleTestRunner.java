package ru.otus.homework.tester;


import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySimpleTestRunner {

    public void run(Class<?> classToTest) throws ReflectiveOperationException {
        MethodGroup methodGroup = analise(classToTest);
        int countDone = 0;
        int countFailed = 0;
        for (Method method : methodGroup.test()) {
            if (startTest(methodGroup, method)) {
                countDone++;
            } else {
                countFailed++;
            }
        }

        printResult(methodGroup.test().size(), classToTest.getName(), countDone, countFailed);
    }

    private void printResult(int size, String name, int countDone, int countFailed) {
        StringBuilder result = new StringBuilder("Тест класса ");
        result.append(name).append("\n");
        result.append("Успешно выполнено ").append(countDone).append(" тестов").append("\n");
        result.append("Упало ").append(countFailed).append(" тестов").append("\n");
        result.append("Всего запущено тестов ").append(size);
        System.out.println(result);
    }

    private boolean startTest(MethodGroup methodGroup, Method test) throws ReflectiveOperationException {
        boolean flag = true;
        Object instance = methodGroup.constructor().newInstance();

        for (Method before : methodGroup.before()) {
            try {
                before.invoke(instance);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }

        if (flag) {
            try {
                test.invoke(instance);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }

        for (Method after : methodGroup.after()) {
            try {
                after.invoke(instance);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }

        return flag;
    }

    private MethodGroup analise(Class<?> classToTest) throws ReflectiveOperationException {
        Constructor<?> constructor = classToTest.getConstructor();
        List<Method> declaredMethods = Arrays.stream(classToTest.getDeclaredMethods()).toList();
        Set<Method> before = new HashSet<>();
        Set<Method> test = new HashSet<>();
        Set<Method> after = new HashSet<>();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Before.class)) {
                before.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                test.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                after.add(method);
            }
        }
        return new MethodGroup(constructor, before, test, after);
    }


    private record MethodGroup(Constructor<?> constructor, Set<Method> before, Set<Method> test, Set<Method> after) {
    }
}
