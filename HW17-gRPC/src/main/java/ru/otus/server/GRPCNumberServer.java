package ru.otus.server;

import io.grpc.ServerBuilder;

import java.io.IOException;

import static ru.otus.common.GRPCAppConst.SERVER_PORT;


//Разработать клиент-серверное приложение с применением технологии gRPC.
//
//        Серверная часть.
//        сервер по запросу клиента генерирует последовательность чисел.
//        запрос от клиента содержит начальное значение (firstValue) и конечное(lastValue).
//        Раз в две секунды сервер генерирует новое значение и "стримит" его клиенту:
//        firstValue + 1
//        firstValue + 2
//        ...
//        lastValue
//        Клиентская часть.
//        клиент отправдяет запрос серверу для получения последовательности чисел от 0 до 30.
//        клиент запускает цикл от 0 до 50.
//        раз в секунду выводит в консоль число (currentValue) по такой формуле:
//        currentValue = [currentValue] + [ПОСЛЕДНЕЕ число от сервера] + 1
//        начальное значение: currentValue = 0
//        Число, полученное от сервера должно учитываться только один раз.
//        Обратите внимание, сервер может вернуть несколько чисел, надо взять именно ПОСЛЕДНЕЕ.
//        Должно получиться примерно так:
//        currentValue:1
//        число от сервера:2
//        currentValue:4 <--- число от сервера учитываем только один раз
//        currentValue:5 <--- тут число от сервера уже не учитывается.
//        число от сервера:3
//        currentValue:9
//        currentValue:10
//        new value:4
//        currentValue:15
//        currentValue:16
//        Для коммуникации используйте gRPC.
//        Клиент и сервер не обязательно разделять по модулям.
//        Можно сделать один модуль с двумя main-классами для клиента и сервера.
public class GRPCNumberServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting gRPCNumberServer...");

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new RemoteNumberServiceImpl()).build();

        server.start();
        System.out.println("Server started and waiting for client connections...");
        server.awaitTermination();
    }
}
