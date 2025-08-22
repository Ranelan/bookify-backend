package com.booklify.service;

public interface IService <T, Id> {

    T save(T entity);

    T findById(Id id);

    T update(T entity);

    boolean deleteById(Id id);
}
