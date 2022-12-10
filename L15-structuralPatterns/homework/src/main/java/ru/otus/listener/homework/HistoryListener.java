package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> map = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if(Objects.isNull(msg.getField13())){
            map.put(msg.getId(), msg.toBuilder().build());
        } else {
            var ofm = new ObjectForMessage();
            Message historyMsg = msg.toBuilder().field13(ofm).build();
            if(Objects.nonNull(msg.getField13().getData())){
                ofm.setData(List.copyOf(msg.getField13().getData()));
            }
            map.put(msg.getId(), historyMsg);
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(map.get(id));
    }
}
