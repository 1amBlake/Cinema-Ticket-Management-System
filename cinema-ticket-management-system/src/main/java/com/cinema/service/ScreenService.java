package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.ScreenDao;
import com.cinema.entity.Screen;


public class ScreenService {

    private final ScreenDao screenDao;

    public ScreenService() {
        this.screenDao = new ScreenDao();
    }

    public List<Screen> getAllScreens() throws SQLException {
        return screenDao.getAllScreens();
    }


    public Screen findScreenById(int id) throws SQLException {
        return screenDao.findById(id);
    }


    public boolean addScreen(Screen screen) throws SQLException {
        validate(screen);
        return screenDao.addScreen(screen);
    }


    public boolean updateScreen(Screen screen) throws SQLException {
        validate(screen);
        return screenDao.updateScreen(screen);
    }

    public boolean deleteScreen(int id) throws SQLException {
        return screenDao.deleteScreenById(id);
    }

    public List<Screen> getScreensByTheaterId(int theaterId) throws SQLException {

        if (theaterId <= 0) {
            throw new IllegalArgumentException("Invalid theaterId");
        }


        return screenDao.getAllScreens()
                .stream()
                .filter(s -> s.getTheater().getTheaterId() == theaterId)
                .toList();
    }

    private void validate(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen is null");
        }

        if (screen.getScreenName() == null || screen.getScreenName().trim().isEmpty()) {
            throw new IllegalArgumentException("Screen name is required");
        }

        if (screen.getTheater() == null) {
            throw new IllegalArgumentException("Theater is required");
        }

        if (screen.getScreenType() == null) {
            throw new IllegalArgumentException("Screen type is required");
        }
    }
}