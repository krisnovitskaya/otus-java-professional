package ru.otus.homework.tester;


import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MySimpleTestRunner {

    public void run(Class classToTest) throws ReflectiveOperationException {
        Set<MethodGroup> methodGroupSet = analise(classToTest);
        int countDone = 0;
        int countFailed = 0;
        for (MethodGroup methodGroup : methodGroupSet) {
            if (startTest(methodGroup)) countDone++;
            else countFailed++;
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
        Object instance = methodGroup.getConstructor().newInstance();
        if (Objects.nonNull(methodGroup.getBefore())) {
            try {
                methodGroup.getBefore().setAccessible(true);
                methodGroup.getBefore().invoke(instance);
            } catch (Throwable throwable) {
                flag = false;
                throwable.printStackTrace();
            }
        }

        if (flag) {
            try {
                methodGroup.getTest().setAccessible(true);
                methodGroup.getTest().invoke(instance);
            } catch (Throwable throwable) {
                flag = false;
                throwable.printStackTrace();
            }
        }

        if (Objects.nonNull(methodGroup.getAfter())) {
            try {
                methodGroup.getAfter().setAccessible(true);
                methodGroup.getAfter().invoke(instance);
            } catch (Throwable throwable) {
                flag = false;
                throwable.printStackTrace();
            }
        }

        return flag;
    }

    private Set<MethodGroup> analise(Class classToTest) throws ReflectiveOperationException {
        Constructor constructor = classToTest.getConstructor();
        List<Method> declaredMethods = Arrays.stream(classToTest.getDeclaredMethods()).toList();
        Method before = declaredMethods.stream().filter(m -> m.isAnnotationPresent(Before.class)).findFirst().orElse(null);
        Method after = declaredMethods.stream().filter(m -> m.isAnnotationPresent(After.class)).findFirst().orElse(null);
        return declaredMethods.stream().filter(m -> m.isAnnotationPresent(Test.class)).map(m -> new MethodGroup(constructor, before, m, after)).collect(Collectors.toSet());
    }


    private static class MethodGroup {
        private Constructor constructor;
        private Method before;
        private Method test;
        private Method after;

        public MethodGroup(Constructor constructor, Method before, Method test, Method after) {
            this.constructor = constructor;
            this.before = before;
            this.test = test;
            this.after = after;
        }

        public Constructor getConstructor() {
            return constructor;
        }

        public Method getBefore() {
            return before;
        }

        public Method getTest() {
            return test;
        }

        public Method getAfter() {
            return after;
        }
    }
}
