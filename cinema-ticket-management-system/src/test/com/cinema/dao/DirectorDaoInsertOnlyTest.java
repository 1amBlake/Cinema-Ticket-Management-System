package com.cinema.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.cinema.entity.Director;

public class DirectorDaoInsertOnlyTest {

    private DirectorDao directorDao;

    @Before
    public void setUp() {
        directorDao = new DirectorDao();
    }

    @Test
    public void addManyDirectors_ShouldInsertDataToDatabase_WithoutCleaning() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());

        assertTrue(directorDao.addDirector(new Director("Christopher Nolan Test " + suffix)));
        assertTrue(directorDao.addDirector(new Director("Denis Villeneuve Test " + suffix)));
        assertTrue(directorDao.addDirector(new Director("James Cameron Test " + suffix)));
        assertTrue(directorDao.addDirector(new Director("Bong Joon Ho Test " + suffix)));
    }
}