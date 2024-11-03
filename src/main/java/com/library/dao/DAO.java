package com.library.dao;//package com.library.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    <U> T get(U id) throws SQLException;
    List<T> getAll();
    //void save(T t);
    <U> void add(U t) throws SQLException;
    void delete(T t) throws SQLException;

}
