package com.cinema.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cinema.entity.Director;
import com.cinema.testutil.ClearDb;

public class DirectorDaoTest {

    private DirectorDao directorDao;

    @Before
    public void setUp() {
        ClearDb.cleanDatabase();
        directorDao = new DirectorDao();
    }

    @Test
    public void addDirector_ShouldReturnTrue_WhenDirectorIsValid() throws Exception {
        Director director = new Director("Christopher Nolan");

        boolean result = directorDao.addDirector(director);

        assertTrue(result);
    }

    @Test
    public void findById_ShouldReturnDirector_WhenIdExists() throws Exception {
        Director director = new Director("Christopher Nolan");

        assertTrue(directorDao.addDirector(director));

        List<Director> directors = directorDao.getAllDirectors();
        assertFalse(directors.isEmpty());

        int id = directors.get(0).getDirectorId();

        Director foundDirector = directorDao.findById(id);

        assertNotNull(foundDirector);
        assertEquals("Christopher Nolan", foundDirector.getDirectorName());
    }

    @Test
    public void updateDirector_ShouldReturnTrue_WhenDirectorExists() throws Exception {
        Director director = new Director("Christopher Nolan");

        assertTrue(directorDao.addDirector(director));

        Director savedDirector = directorDao.getAllDirectors().get(0);
        Director updatedData = new Director(savedDirector.getDirectorId(), "Nolan Updated");

        boolean result = directorDao.updateDirector(updatedData);

        assertTrue(result);

        Director updatedDirector = directorDao.findById(savedDirector.getDirectorId());
        assertEquals("Nolan Updated", updatedDirector.getDirectorName());
    }

    @Test
    public void deleteDirectorById_ShouldReturnTrue_WhenDirectorExists() throws Exception {
        Director director = new Director("Christopher Nolan");

        assertTrue(directorDao.addDirector(director));

        Director savedDirector = directorDao.getAllDirectors().get(0);

        boolean result = directorDao.deleteDirectorById(savedDirector.getDirectorId());

        assertTrue(result);
        assertNull(directorDao.findById(savedDirector.getDirectorId()));
    }

    @Test
    public void getAllDirectors_ShouldReturnDirectorList() throws Exception {
        Director director1 = new Director("Christopher Nolan");
        Director director2 = new Director("Denis Villeneuve");

        assertTrue(directorDao.addDirector(director1));
        assertTrue(directorDao.addDirector(director2));

        List<Director> directors = directorDao.getAllDirectors();

        assertEquals(2, directors.size());
    }

    @Test
    public void searchByName_ShouldReturnMatchedDirectors() throws Exception {
        Director director1 = new Director("Christopher Nolan");
        Director director2 = new Director("Denis Villeneuve");

        assertTrue(directorDao.addDirector(director1));
        assertTrue(directorDao.addDirector(director2));

        List<Director> directors = directorDao.searchByName("Nolan");

        assertEquals(1, directors.size());
        assertEquals("Christopher Nolan", directors.get(0).getDirectorName());
    }
}