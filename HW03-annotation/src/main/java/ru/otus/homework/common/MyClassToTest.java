package ru.otus.homework.common;

import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MyClassToTest {

    @Before
    private void init(){
        System.out.println("do init");
    }

    @After
    private void finish(){
        System.out.println("do finish");
    }



    @Test
    void test1(){
        System.out.println("тест 1");
    }


    @Test
    void test2(){
        System.out.println("тест 2");
        throw new RuntimeException();
    }

    @Test
    void test3(){
        System.out.println("тест 3");
        assertThat(1).isEqualTo(2);
    }

    @Test
    private void test4(){
        System.out.println("тест 4");
    }


}
