package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;


public class ProcessorEvenSecondCheck implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondCheck(DateTimeProvider dateTimeProvider){
        this.dateTimeProvider = dateTimeProvider;
    }
    @Override
    public Message process(Message message) {
        if(dateTimeProvider.getDate().getSecond() % 2 == 0) throw new EvenSecondException("Выполнение в четную секунду");
        return message.toBuilder().build();
    }
}
