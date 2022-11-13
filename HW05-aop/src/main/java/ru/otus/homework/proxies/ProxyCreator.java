package ru.otus.homework.proxies;

import ru.otus.homework.annotation.Log;
import ru.otus.homework.common.ITestLogging;

import java.lang.reflect.*;
import java.util.*;

public class ProxyCreator {

    private ProxyCreator() {
    }

    public static  ITestLogging createProxyClass(Class<? extends ITestLogging> clazz) throws Exception {
        return (ITestLogging) Proxy.newProxyInstance(ProxyCreator.class.getClassLoader(),
                new Class<?>[]{ITestLogging.class}, new LogInvocationHandler(clazz));
    }

    static class LogInvocationHandler implements InvocationHandler {
        private final ITestLogging proxiedClass;
        private final Set<MethodInfo> loggingMethods = new HashSet<>();

        LogInvocationHandler(Class<? extends ITestLogging> clazz) throws Exception {
            Constructor<? extends ITestLogging> constructor = clazz.getConstructor();
            this.proxiedClass = constructor.newInstance();

            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if(declaredMethod.isAnnotationPresent(Log.class)){
                    loggingMethods.add(new MethodInfo(declaredMethod.getName(), declaredMethod.getParameterTypes()));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

            MethodInfo methodInfo = new MethodInfo(method.getName(), method.getParameterTypes());
            if(loggingMethods.contains(methodInfo)){
                logging(methodInfo, args);
            }
            return method.invoke(proxiedClass, args);
        }

        @Override
        public String toString() {
            return "LogInvocationHandler{" +
                    "proxiedClass=" + proxiedClass +
                    '}';
        }

        private void logging(MethodInfo methodInfo, Object[] args){
            System.out.printf("executed method: %s, params: %s%n", methodInfo.name,
                    Optional.ofNullable(args).map(arg -> Arrays.stream(arg).toList().toString()).orElse("[]"));
        }
    }

    record MethodInfo(String name, Class<?>[] parameters){
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodInfo that = (MethodInfo) o;
            return Objects.equals(name, that.name) && Arrays.equals(parameters, that.parameters);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name);
            result = 31 * result + Arrays.hashCode(parameters);
            return result;
        }
    }
}
