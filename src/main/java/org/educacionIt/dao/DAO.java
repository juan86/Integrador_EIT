package org.educacionIt.dao;

import java.util.List;

public interface DAO <E, K>{
    E searchById(K key);
    List<E> getByProperty(String propertyName, E entity);
    List<E> getAll();
    int insert(E entity);
    void update(E entity);
    void delete(E entity);
}
