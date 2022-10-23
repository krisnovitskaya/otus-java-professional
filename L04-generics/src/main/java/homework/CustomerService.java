package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> storage = new TreeMap<>(new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return Long.compare(o1.getScores(), o2.getScores());
        }
    });
    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = storage.firstEntry();
        return entry == null ? null: new AbstractMap.SimpleImmutableEntry<>(new Customer(entry.getKey().getId(), entry.getKey().getName(),entry.getKey().getScores()), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = storage.higherEntry(customer);
        return entry == null ? null: new AbstractMap.SimpleImmutableEntry<>(new Customer(entry.getKey().getId(), entry.getKey().getName(),entry.getKey().getScores()), entry.getValue());
    }

    public void add(Customer customer, String data) {
        storage.put(customer, data);
    }
}
