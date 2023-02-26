package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//В чат из примера к вебинару добавьте специальную комнату: 1408.
//        В этой комнате нельзя отправлять сообщения.
//        Однако в нее приходят все сообщения из всех других комнат.
//        Обратите внимание:
//        при входе в комнату 1408 должны загрузиться все сообщения из всех комнат (по аналогии с типовыми комнатами).
@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
