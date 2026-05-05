package com.cinema.service;

import com.cinema.dao.TicketPricingDao;
import com.cinema.entity.TicketPricing;

import java.sql.SQLException;
import java.util.List;

/**
 * Service cho bảng giá vé.
 * Chứa các chức vụ liên quan đến bảng giá vé.
 * 
 * @author Hải Anh (chính)
 */
public class TicketPricingService {

    private final TicketPricingDao ticketPricingDao;

    public TicketPricingService() {
        this.ticketPricingDao = new TicketPricingDao();
    }

	/**
	 * Lấy tất cả bảng giá vé
	 * 
	 * @return danh sách bảng giá vé
	 * @throws SQLException nếu có lỗi SQL
	 */
    public List<TicketPricing> getAllTicketPricings() throws SQLException {
        return ticketPricingDao.getAllTicketPricings();
    }

	/**
	 * Tìm bảng giá vé theo mã
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return đối tượng TicketPricing
	 * @throws SQLException nếu có lỗi SQL
	 */
    public TicketPricing findById(int ticketPricingId) throws SQLException {
        if(ticketPricingId <= 0) {
            throw new IllegalArgumentException("Mã bảng giá vé phải lớn hơn 0!");
        }

        return ticketPricingDao.findById(ticketPricingId);
    }

	/**
	 * Tìm bảng giá vé theo loại ghế và loại phòng chiếu (TicketPricing không có SessionType nên tôi làm ScreenType)
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param screenTypeId - Mã loại ghế
	 * @return đối tượng TicketPricing
	 * @throws SQLException nếu có lỗi SQL
	 */
    public TicketPricing getPriceBySeatTypeAndScreenType(int seatTypeId, int screenTypeId) throws SQLException {
        if(seatTypeId <= 0) {
            throw new IllegalArgumentException("Mã loại ghế phải lớn hơn 0!");
        }

        if(screenTypeId <= 0) {
            throw new IllegalArgumentException("Mã loại phòng chiếu phải lớn hơn 0!");
        }

        List<TicketPricing> ticketPricings = ticketPricingDao.getAllTicketPricings();
        for(TicketPricing ticketPricing : ticketPricings) {
            if(ticketPricing.getSeatType().getSeatTypeId() == seatTypeId
            		&& ticketPricing.getScreenType().getScreenTypeId() == screenTypeId) {
                return ticketPricing;
            }
        }

        return null;
    }

	/**
	 * Tính giá vé theo loại ghế và loại phòng chiếu
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return giá vé
	 * @throws SQLException nếu có lỗi SQL
	 */
    public double calculateTicketPrice(int seatTypeId, int screenTypeId) throws SQLException {
        TicketPricing ticketPricing = getPriceBySeatTypeAndScreenType(seatTypeId, screenTypeId);
        if(ticketPricing == null) {
            throw new IllegalArgumentException("Không tìm thấy bảng giá phù hợp!");
        }

        return ticketPricing.getPrice();
    }
}