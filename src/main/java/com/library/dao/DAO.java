package com.library.dao;//package com.library.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    <U> T get(U id);
    List<T> getAll();
    //void save(T t);
    <U> void add(U t);
    void delete(T t);

}
