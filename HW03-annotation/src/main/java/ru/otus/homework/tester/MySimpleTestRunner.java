package ru.otus.homework.tester;


import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MySimpleTestRunner {

    public void run(Class<?> classToTest) throws ReflectiveOperationException {
        Set<MethodGroup> methodGroupSet = analise(classToTest);
        int countDone = 0;
        int countFailed = 0;
        for (MethodGroup methodGroup : methodGroupSet) {
            if (startTest(methodGroup)) {
                countDone++;
            } else {
                countFailed++;
            }
        }

        printResult(methodGroupSet.size(), classToTest.getName(), countDone, countFailed);
    }

    private void printResult(int size, String name, int countDone, int countFailed) {
        StringBuilder result = new StringBuilder("Тест класса ");
        result.append(name).append("\n");
        result.append("Успешно выполнено ").append(countDone).append(" тестов").append("\n");
        result.append("Упало ").append(countFailed).append(" тестов").append("\n");
        result.append("Всего запущено тестов ").append(size);
        System.out.println(result);
    }

    private boolean startTest(MethodGroup methodGroup) throws ReflectiveOperationException {
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
                methodGroup.test().invoke(instance);
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

    private Set<MethodGroup> analise(Class<?> classToTest) throws ReflectiveOperationException {
        Constructor<?> constructor = classToTest.getConstructor();
        List<Method> declaredMethods = Arrays.stream(classToTest.getDeclaredMethods()).toList();

        Set<Method> before = declaredMethods.stream().filter(m -> m.isAnnotationPresent(Before.class)).collect(Collectors.toSet());
        Set<Method> after = declaredMethods.stream().filter(m -> m.isAnnotationPresent(After.class)).collect(Collectors.toSet());
        return declaredMethods.stream().filter(m -> m.isAnnotationPresent(Test.class)).map(m -> new MethodGroup(constructor, before, m, after)).collect(Collectors.toSet());
    }


    private record MethodGroup(Constructor<?> constructor, Set<Method> before, Method test, Set<Method> after) {
    }
}
