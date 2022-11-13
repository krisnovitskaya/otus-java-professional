package ru.otus.homework.common;


import ru.otus.homework.annotation.Log;

public class TestLoggingImpl implements ITestLogging {

    @Log
    @Override
    public void calculation() {

    }

    @Log
    @Override
    public void calculation(int param1){
    }

    @Override
    public void calculation(int param1, int param2){

    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3){

    }


}
