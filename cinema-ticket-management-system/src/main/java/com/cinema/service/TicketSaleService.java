package com.cinema.service;

import com.cinema.dao.MovieDao;
import com.cinema.dao.MovieSessionDao;
import com.cinema.dao.SeatDao;
import com.cinema.dao.TicketDao;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieSession;
import com.cinema.entity.Screen;
import com.cinema.entity.Seat;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketPricing;
import com.cinema.enums.MovieStatus;
import com.cinema.enums.TicketStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho bán vé.
 * Chứa các chức vụ liên quan đến bán vé.
 * 
 * @author Hải Anh (chính)
 */
public class TicketSaleService {

    private final MovieDao movieDao;
    private final MovieSessionDao movieSessionDao;
    private final SeatDao seatDao;
    private final TicketDao ticketDao;
    private final TicketPricingService ticketPricingService;

    public TicketSaleService() {
        this.movieDao = new MovieDao();
        this.movieSessionDao = new MovieSessionDao();
        this.seatDao = new SeatDao();
        this.ticketDao = new TicketDao();
        this.ticketPricingService = new TicketPricingService();
    }

	/**
	 * Lấy danh sách phim đang chiếu
	 * 
	 * @return danh sách phim phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
    public List<Movie> getShowingMovies() throws SQLException {
        List<Movie> movies = movieDao.getAllMovies();
        List<Movie> showing = new ArrayList<>();
        for(Movie movie : movies) {
            if(movie.getMovieStatus() == MovieStatus.SHOWING) {
                showing.add(movie);
            }
        }

        return showing;
    }

	/**
	 * Lấy danh sách suất chiếu theo phim
	 * 
	 * @param movieId - Mã phim
	 * @return danh sách suất chiếu phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
    public List<MovieSession> getSessionsByMovie(int movieId) throws SQLException {
        if(movieId <= 0) {
            throw new IllegalArgumentException("Mã phim phải lớn hơn 0!");
        }

        return movieSessionDao.searchByMovieId(movieId);
    }

	/**
	 * Lấy danh sách ghế theo phòng chiếu (Seat không có Session)
	 * 
	 * @param screenId - Mã phòng chiếu
	 * @return danh sách ghế phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
    public List<Seat> getSeatsByScreenId(int screenId) throws SQLException {
        if(screenId <= 0) {
            throw new IllegalArgumentException("Mã phòng chiếu phải lớn hơn 0!");
        }

        return seatDao.searchByScreenId(screenId);
    }

	/**
	 * kiểm tra ghế đã bán chưa
	 * 
	 * Ông giúp tôi thay đổi existsByMovieSessionIdAndSeatId thành public hoặc tạo cái tương tự mà để public vào DAO của ticket
	 * 
	 * @param sessionId - Mã suất chiếu
	 * @param seatId - Mã ghế
	 * @return true nếu ghế đã được bán
	 * @throws SQLException nếu có lỗi SQL
	 */
    /*public boolean isSeatSold(int sessionId, int seatId) throws SQLException {
        if(sessionId <= 0 || seatId <= 0) {
            throw new IllegalArgumentException("Mã suất chiếu và mã ghế phải lớn hơn 0!");
        }

        //return ticketDao.existsByMovieSessionIdAndSeatId(sessionId, seatId);
    } */

	/**
	 * Chọn nhiều ghế
	 * 
	 * @param sessionId - Mã suất chiếu
	 * @param seatIds - Danh sách mã ghế
	 * @return danh sách ghế phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
    /*public List<Seat> selectSeats(int sessionId, List<Integer> seatIds) throws SQLException {
        List<Seat> selectedSeats = new ArrayList<>();
        for(int seatId : seatIds) {
            if(isSeatSold(sessionId, seatId)) {
                throw new IllegalArgumentException("Ghế " + seatId + " đã được bán!");
            }

            Seat seat = seatDao.findById(seatId);
            if(seat == null) {
                throw new IllegalArgumentException("Không tìm thấy ghế: " + seatId);
            }

            selectedSeats.add(seat);
        }

        return selectedSeats;
    } */

	/**
	 * Tính tổng tiền vé
	 * 
	 * @param seats - Danh sách ghế
	 * @return tổng tiền ghế
	 * @throws SQLException nếu có lỗi SQL
	 */
    public double calculateTotal(List<Seat> seats) throws SQLException {
        double total = 0;
        for (Seat seat : seats) {
            total += ticketPricingService.calculateTicketPrice(seat.getSeatType().getSeatTypeId()
            		,seat.getScreen().getScreenType().getScreenTypeId());
        }

        return total;
    }

	/**
	 * Bán vé / tạo vé
	 * 
	 * @param movieSession - suất chiếu
	 * @param seats - Danh sách ghế
	 * @return danh sách vé đươc bán / tạo
	 * @throws SQLException nếu có lỗi SQL
	 */
   /* public List<Ticket> sellTickets(MovieSession movieSession, List<Seat> seats) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        for (Seat seat : seats) {
            if (isSeatSold(movieSession.getMovieSessionId(), seat.getSeatId())) {
                throw new IllegalArgumentException("Ghế " + seat.getSeatId() + " đã bán!");
            }

            TicketPricing ticketPricing = ticketPricingService.getPriceBySeatTypeAndScreenType(
                            seat.getSeatType().getSeatTypeId(),
                            seat.getScreen().getScreenType().getScreenTypeId());
            
            Ticket ticket = new Ticket();
            ticket.setMovieSession(movieSession);
            ticket.setSeat(seat);
            ticket.setTicketPricing(ticketPricing);
            ticket.setTicketStatus(TicketStatus.SOLD);

            ticketDao.addTicket(ticket);

            tickets.add(ticket);
        }

        return tickets;
    } */
}