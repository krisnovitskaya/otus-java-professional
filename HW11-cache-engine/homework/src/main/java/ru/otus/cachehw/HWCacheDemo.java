package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }
}
// Homework task
//    Закончите реализацию MyCache из вебинара.
//    используйте WeakHashMap для хранения значений.
//    Добавьте кэширование в DBService из задания про Hibernate ORM
//    или "Самодельный ORM".
//    Для простоты скопируйте нужные классы в это ДЗ.
//    Убедитесь, что ваш кэш действительно работает быстрее СУБД и
//    сбрасывается при недостатке памяти.
//    Код предыдущего задания менять не надо.
//    Просто скопируйте все нужные классы.
