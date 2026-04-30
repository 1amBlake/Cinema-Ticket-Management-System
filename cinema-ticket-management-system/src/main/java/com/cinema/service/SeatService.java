package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.SeatDao;
import com.cinema.entity.Seat;
import com.cinema.enums.SeatStatus;


public class SeatService {

    private final SeatDao seatDao;

    public SeatService() {
        this.seatDao = new SeatDao();
    }


    public List<Seat> getAllSeats() throws SQLException {
        return seatDao.getAllSeats();
    }


    public Seat findSeatById(int id) throws SQLException {
        return seatDao.findById(id);
    }


    public boolean addSeat(Seat seat) throws SQLException {
        validate(seat);
        return seatDao.addSeat(seat);
    }

 
    public boolean updateSeat(Seat seat) throws SQLException {
        validate(seat);
        return seatDao.updateSeat(seat);
    }

    public boolean deleteSeat(int id) throws SQLException {
        return seatDao.deleteSeatById(id);
    }


    public List<Seat> getSeatsByScreenId(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("Invalid screenId");
        }

        return seatDao.searchByScreenId(screenId);
    }


    public List<Seat> getSeatsByMovieSessionId(int sessionId) throws SQLException {
        if (sessionId <= 0) {
            throw new IllegalArgumentException("Invalid sessionId");
        }


        throw new UnsupportedOperationException("Chưa implement (cần MovieSessionDao)");
    }

    public boolean updateSeatStatus(int seatId, SeatStatus status) throws SQLException {

        if (seatId <= 0) {
            throw new IllegalArgumentException("Invalid seatId");
        }

        if (status == null) {
            throw new IllegalArgumentException("Seat status is required");
        }

        Seat seat = seatDao.findById(seatId);

        if (seat == null) {
            throw new IllegalArgumentException("Seat not found");
        }

        seat.setSeatStatus(status);

        return seatDao.updateSeat(seat);
    }


    private void validate(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat is null");
        }

        if (seat.getScreen() == null) {
            throw new IllegalArgumentException("Screen is required");
        }

        if (seat.getSeatType() == null) {
            throw new IllegalArgumentException("Seat type is required");
        }

        if (seat.getSeatRow() == null || seat.getSeatRow().trim().isEmpty()) {
            throw new IllegalArgumentException("Seat row is required");
        }

        if (seat.getSeatCol() == null || seat.getSeatCol().trim().isEmpty()) {
            throw new IllegalArgumentException("Seat column is required");
        }
    }
}