package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.SeatDao;
import com.cinema.entity.Seat;
import com.cinema.enums.SeatStatus;
import com.cinema.validator.SeatValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Seat.
 * 
 * Lớp này là tầng trung gian giữa Controller và SeatDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý các nghiệp vụ cơ bản
 * liên quan đến ghế và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Lưu ý:
 * - SeatStatus chỉ thể hiện trạng thái vật lý của ghế:
 *   AVAILABLE hoặc MAINTENANCE.
 * - Trạng thái đã bán/đã đặt ghế không nên lưu trong SeatStatus,
 *   vì trạng thái bán vé phụ thuộc vào từng suất chiếu.
 * - Việc kiểm tra trùng vị trí ghế trong cùng phòng được xử lý ở SeatDao.
 * - Việc kiểm tra ghế đã phát sinh vé trước khi cập nhật/xóa được xử lý ở SeatDao.
 * 
 * @author Trọng
 * @author Minh Huy
 */
public class SeatService {

    private final SeatDao seatDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo SeatService với SeatDao mặc định.
     */
    public SeatService() {
        this(new SeatDao());
    }

    /**
     * Constructor cho phép truyền SeatDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế SeatDao bằng đối tượng khác.
     *
     * @param seatDao - DAO dùng để thao tác dữ liệu Seat
     * @throws IllegalArgumentException nếu seatDao là null
     */
    public SeatService(SeatDao seatDao) {
        if (seatDao == null) {
            throw new IllegalArgumentException("seatDao không được null!");
        }

        this.seatDao = seatDao;
    }

    /**
     * Lấy danh sách tất cả ghế.
     *
     * @return danh sách tất cả ghế
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Seat> getAllSeats() throws SQLException {
        return seatDao.getAllSeats();
    }

    /**
     * Tìm ghế theo mã ghế.
     *
     * @param seatId - Mã ghế cần tìm
     * @return đối tượng Seat nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu seatId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Seat findSeatById(int seatId) throws SQLException {
        validateRequiredId(seatId, "seatId");

        return seatDao.findById(seatId);
    }

    /**
     * Thêm ghế mới.
     *
     * @param seat - Ghế cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu ghế không hợp lệ,
     *                                  phòng chiếu không tồn tại,
     *                                  loại ghế không tồn tại
     *                                  hoặc vị trí ghế đã tồn tại trong phòng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addSeat(Seat seat) throws SQLException {
        SeatValidator.validateForCreate(seat);

        return seatDao.addSeat(seat);
    }

    /**
     * Cập nhật thông tin cấu trúc của ghế.
     * 
     * Method này dùng cho cập nhật các thông tin như:
     * phòng chiếu, loại ghế, hàng và cột.
     * 
     * Nếu chỉ muốn đổi trạng thái vận hành của ghế,
     * hãy dùng updateSeatStatus().
     */
    public boolean updateSeat(Seat seat) throws SQLException {
        SeatValidator.validateForUpdate(seat);

        return seatDao.updateSeat(seat);
    }

    /**
     * Xóa ghế theo mã ghế.
     * 
     * Không cho phép xóa nếu ghế đã phát sinh vé.
     *
     * @param seatId - Mã ghế cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu seatId không hợp lệ hoặc ghế đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteSeatById(int seatId) throws SQLException {
        validateRequiredId(seatId, "seatId");

        return seatDao.deleteSeatById(seatId);
    }

    /**
     * Lấy danh sách ghế theo mã phòng chiếu.
     *
     * @param screenId - Mã phòng chiếu
     * @return danh sách ghế thuộc phòng chiếu
     * @throws IllegalArgumentException nếu screenId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Seat> getSeatsByScreenId(int screenId) throws SQLException {
        validateRequiredId(screenId, "screenId");

        return seatDao.searchByScreenId(screenId);
    }

    /**
     * Cập nhật trạng thái vận hành của ghế.
     * 
     * Method này chỉ cập nhật trạng thái vật lý của ghế như:
     * AVAILABLE hoặc MAINTENANCE.
     * 
     * Không dùng method này để biểu diễn ghế đã bán hoặc đã đặt,
     * vì trạng thái bán vé phụ thuộc vào từng suất chiếu.
     *
     * @param seatId - Mã ghế cần cập nhật trạng thái
     * @param status - Trạng thái mới của ghế
     * @return true nếu cập nhật thành công, false nếu không tìm thấy ghế
     * @throws IllegalArgumentException nếu seatId không hợp lệ,
     *                                  status null hoặc không tìm thấy ghế
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateSeatStatus(int seatId, SeatStatus status) throws SQLException {
        validateRequiredId(seatId, "seatId");

        if (status == null) {
            throw new IllegalArgumentException("seatStatus không được null!");
        }

        Seat seat = seatDao.findById(seatId);

        if (seat == null) {
            throw new IllegalArgumentException("Ghế không tồn tại!");
        }

        return seatDao.updateSeatStatusById(seatId, status);
    }

    /**
     * Kiểm tra ID bắt buộc.
     *
     * @param id - Giá trị ID cần kiểm tra
     * @param fieldName - Tên trường dùng trong thông báo lỗi
     */
    private void validateRequiredId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " phải lớn hơn 0!");
        }
    }
}

/*
 * Các điểm đã bổ sung và chỉnh sửa so với SeatService ban đầu:
 * 
 * 1. Bổ sung import SeatValidator:
 *    - Thêm:
 *      import com.cinema.validator.SeatValidator;
 * 
 *    Lý do:
 *    Service không nên tự kiểm tra dữ liệu thủ công bằng method validate()
 *    riêng. Việc kiểm tra dữ liệu cơ bản và ràng buộc nghiệp vụ nội tại
 *    của Seat nên được giao cho SeatValidator để đồng bộ với cấu trúc
 *    Validator của dự án.
 * 
 * 2. Sửa constructor mặc định:
 *    - Từ:
 *      public SeatService() {
 *          this.seatDao = new SeatDao();
 *      }
 * 
 *    - Thành:
 *      public SeatService() {
 *          this(new SeatDao());
 *      }
 * 
 *    Lý do:
 *    Constructor mặc định tái sử dụng constructor có tham số,
 *    giúp tránh lặp code và đồng bộ với style các Service khác.
 * 
 * 3. Bổ sung constructor có tham số SeatDao:
 *    - Thêm:
 *      public SeatService(SeatDao seatDao)
 * 
 *    Lý do:
 *    Cho phép truyền SeatDao từ bên ngoài vào Service.
 *    Cách này hữu ích khi test, mock DAO hoặc thay thế DAO khác.
 * 
 * 4. Bổ sung kiểm tra null cho SeatDao trong constructor:
 *    - Nếu seatDao == null thì ném IllegalArgumentException.
 * 
 *    Lý do:
 *    Tránh lỗi NullPointerException về sau khi gọi các method trong Service.
 * 
 * 5. Bổ sung Javadoc cho class, constructor và các method public.
 * 
 *    Lý do:
 *    Seat là thực thể quan trọng vì liên quan đến phòng chiếu, sơ đồ ghế,
 *    loại ghế, trạng thái ghế và nghiệp vụ bán vé. Javadoc giúp người đọc
 *    hiểu rõ method nào dùng cho nghiệp vụ nào.
 * 
 * 6. Sửa findSeatById():
 *    - Từ:
 *      public Seat findSeatById(int id) throws SQLException {
 *          return seatDao.findById(id);
 *      }
 * 
 *    - Thành:
 *      public Seat findSeatById(int seatId) throws SQLException {
 *          validateRequiredId(seatId, "seatId");
 *          return seatDao.findById(seatId);
 *      }
 * 
 *    Lý do:
 *    ID ghế bắt buộc phải lớn hơn 0. Service nên kiểm tra dữ liệu đầu vào
 *    trước khi chuyển xuống DAO.
 * 
 * 7. Sửa addSeat():
 *    - Từ:
 *      validate(seat);
 * 
 *    - Thành:
 *      SeatValidator.validateForCreate(seat);
 * 
 *    Lý do:
 *    Method validate() cũ chỉ kiểm tra seat null, screen null, seatType null,
 *    seatRow rỗng và seatCol rỗng. Trong khi SeatValidator kiểm tra đầy đủ hơn:
 *    screenId > 0, seatTypeId > 0, độ dài seatRow, độ dài seatCol,
 *    seatStatus khác null và các business rule của Seat.
 * 
 * 8. Sửa updateSeat():
 *    - Từ:
 *      validate(seat);
 * 
 *    - Thành:
 *      SeatValidator.validateForUpdate(seat);
 * 
 *    Lý do:
 *    Khi cập nhật ghế, ngoài các kiểm tra chung, còn bắt buộc seatId > 0.
 *    Rule này nằm trong SeatValidator.validateForUpdate().
 * 
 * 9. Làm rõ ý nghĩa của updateSeat():
 *    - Version cũ dùng updateSeat() chung chung, dễ hiểu nhầm là cập nhật
 *      cả trạng thái vận hành của ghế.
 *    - Version mới mô tả updateSeat() là cập nhật thông tin cấu trúc ghế:
 *      phòng chiếu, loại ghế, hàng và cột.
 * 
 *    Lý do:
 *    Trạng thái vận hành của ghế nên được cập nhật bằng method riêng
 *    updateSeatStatus(), tránh trộn giữa cập nhật cấu trúc ghế và cập nhật
 *    trạng thái vật lý của ghế.
 * 
 * 10. Đổi tên method xóa:
 *     - Từ:
 *       deleteSeat(int id)
 * 
 *     - Thành:
 *       deleteSeatById(int seatId)
 * 
 *     Lý do:
 *     Tên method rõ nghĩa hơn, đồng bộ với SeatDao.deleteSeatById()
 *     và các Service khác trong dự án.
 * 
 * 11. Bổ sung kiểm tra ID trong deleteSeatById():
 *     - Gọi validateRequiredId(seatId, "seatId") trước khi gọi DAO.
 * 
 *     Lý do:
 *     Không cho phép xóa với seatId <= 0. Việc không cho xóa ghế đã phát sinh vé
 *     được xử lý ở SeatDao.
 * 
 * 12. Sửa getSeatsByScreenId():
 *     - Từ:
 *       if (screenId <= 0) {
 *           throw new IllegalArgumentException("Invalid screenId");
 *       }
 * 
 *     - Thành:
 *       validateRequiredId(screenId, "screenId");
 * 
 *     Lý do:
 *     Tái sử dụng method kiểm tra ID bắt buộc, giúp code gọn hơn và thông báo lỗi
 *     đồng bộ tiếng Việt với các Service khác.
 * 
 * 13. Xóa getSeatsByMovieSessionId(int sessionId):
 * 
 *     Lý do:
 *     Version cũ có method này nhưng bên trong chỉ ném:
 *       UnsupportedOperationException("Chưa implement...")
 * 
 *     Nếu Controller hoặc GUI gọi nhầm method này, chương trình sẽ lỗi runtime.
 *     Ngoài ra, nghiệp vụ lấy ghế theo suất chiếu không chỉ thuộc SeatService,
 *     vì cần biết suất chiếu thuộc phòng nào và ghế nào đã bán theo bảng vé.
 *     Phần này nên để TicketSaleService hoặc service nghiệp vụ bán vé xử lý sau.
 * 
 * 14. Sửa updateSeatStatus():
 *     - Version cũ:
 *       + Tìm Seat theo seatId.
 *       + setSeatStatus(status) trên object.
 *       + Gọi seatDao.updateSeat(seat).
 * 
 *     - Version mới:
 *       + Kiểm tra seatId hợp lệ.
 *       + Kiểm tra status khác null.
 *       + Tìm Seat để đảm bảo ghế tồn tại.
 *       + Gọi seatDao.updateSeatStatusById(seatId, status).
 * 
 *     Lý do:
 *     updateSeat() là cập nhật cấu trúc ghế và có thể bị chặn nếu ghế đã phát sinh vé.
 *     Trong khi đổi trạng thái ghế sang AVAILABLE hoặc MAINTENANCE là nghiệp vụ
 *     vận hành vật lý của ghế, nên cần method DAO riêng chỉ cập nhật trang_thai.
 * 
 * 15. Làm rõ ý nghĩa của SeatStatus:
 *     - SeatStatus chỉ biểu diễn trạng thái vật lý của ghế:
 *       AVAILABLE hoặc MAINTENANCE.
 * 
 *     - SeatStatus không biểu diễn ghế đã bán hoặc đã đặt.
 * 
 *     Lý do:
 *     Trạng thái bán vé phụ thuộc vào từng suất chiếu. Một ghế có thể đã bán
 *     ở suất chiếu này nhưng vẫn còn trống ở suất chiếu khác.
 * 
 * 16. Xóa method validate(Seat seat) tự viết trong Service.
 * 
 *     Lý do:
 *     Method validate() cũ bị thiếu nhiều rule quan trọng:
 *     - Không kiểm tra screenId > 0.
 *     - Không kiểm tra seatTypeId > 0.
 *     - Không kiểm tra seatRow quá 10 ký tự.
 *     - Không kiểm tra seatCol quá 10 ký tự.
 *     - Không kiểm tra seatStatus null.
 *     - Không kiểm tra seatId > 0 khi update.
 * 
 *     Các kiểm tra này đã được chuyển về SeatValidator.
 * 
 * 17. Bổ sung method validateRequiredId(int id, String fieldName):
 * 
 *     Lý do:
 *     Dùng chung cho các method cần ID bắt buộc như find, delete,
 *     getSeatsByScreenId và updateSeatStatus. Cách này giúp tránh lặp code
 *     và thống nhất thông báo lỗi.
 * 
 * 18. Chuẩn hóa thông báo lỗi:
 *     - Version cũ dùng tiếng Anh như:
 *       "Invalid seatId", "Seat status is required", "Seat not found".
 * 
 *     - Version mới dùng tiếng Việt đồng bộ hơn:
 *       "seatId phải lớn hơn 0!",
 *       "seatStatus không được null!",
 *       "Ghế không tồn tại!".
 * 
 *    Lý do:
 *    Dự án đang dùng thông báo lỗi tiếng Việt ở Entity, Validator, DAO
 *    và các Service khác.
 * 
 * Kết luận:
 * - Version mới rõ tầng trách nhiệm hơn:
 *   Service gọi Validator để kiểm tra dữ liệu đầu vào.
 *   DAO xử lý kiểm tra liên quan database như phòng chiếu tồn tại,
 *   loại ghế tồn tại, trùng vị trí ghế, ghế đã phát sinh vé.
 * 
 * - Version mới an toàn hơn:
 *   Không còn method chưa implement gây crash runtime.
 *   Không còn validate thủ công thiếu rule.
 *   Không còn dùng updateSeat() để cập nhật trạng thái ghế.
 * 
 * - Version mới đúng nghiệp vụ hơn:
 *   Tách cập nhật cấu trúc ghế và cập nhật trạng thái vận hành ghế.
 *   Không dùng SeatStatus để biểu diễn trạng thái đã bán hoặc đã đặt.
 */