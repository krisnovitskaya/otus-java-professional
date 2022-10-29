package ru.otus.homework.common;

import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MyClassToTest {

    @Before
    public void init(){
        System.out.println("do init");
    }

    @After
    public void finish(){
        System.out.println("do finish");
    }



    @Test
    public void test1(){
        System.out.println("тест 1");
    }


    @Test
    public void test2(){
        System.out.println("тест 2");
        throw new RuntimeException();
    }

    @Test
    public void test3(){
        System.out.println("тест 3");
        assertThat(1).isEqualTo(2);
    }

    @Test
    public void test4(){
        System.out.println("тест 4");
    }


}
