package main.java.com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.TheaterDao;
import main.java.com.cinema.entity.Theater;


public class TheaterService {

    private final TheaterDao theaterDao;

    public TheaterService() {
        this.theaterDao = new TheaterDao();
    }

    public List<Theater> getAllTheaters() throws SQLException {
        return theaterDao.getAllTheaters();
    }


    public Theater findTheaterById(int id) throws SQLException {
        return theaterDao.findById(id);
    }


    public boolean addTheater(Theater theater) throws SQLException {
        validate(theater);


        if (theaterDao.existsByName(theater.getTheaterName())) {
            throw new IllegalArgumentException("Theater name already exists");
        }

        return theaterDao.addTheater(theater);
    }

 
    public boolean updateTheater(Theater theater) throws SQLException {
        validate(theater);

        return theaterDao.updateTheater(theater);
    }


    public boolean deleteTheater(int id) throws SQLException {
        return theaterDao.deleteTheaterById(id);
    }


    public List<Theater> searchTheaters(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTheaters();
        }
        return theaterDao.searchByName(keyword.trim());
    }



    private void validate(Theater theater) {
        if (theater == null) {
            throw new IllegalArgumentException("Theater is null");
        }

        if (theater.getTheaterName() == null || theater.getTheaterName().trim().isEmpty()) {
            throw new IllegalArgumentException("Theater name is required");
        }
    }
}