package org.example.repositories;

import org.example.models.*;
import org.example.init.DummyData;

import java.util.ArrayList;
import java.util.List;

public class SettingsRepository extends BaseRepository {

    public SettingsRepository() {
        super();
    }

    public void dropAllTables() {
        dropAll();
    }

    public void createDummyData() {
        new DummyData();
    }

    public List<Object> getEverything() {
        List<Object> list = new ArrayList<>();
        list.addAll(findAll(Customer.class));
        list.addAll(findAll(Employee.class));
        list.addAll(findAll(Product.class));
        list.addAll(findAll(Order.class));
        list.addAll(findAll(OrderImage.class));
        list.addAll(findAll(Invoice.class));
        return list;
    }

}
