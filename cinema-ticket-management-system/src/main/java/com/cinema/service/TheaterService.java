package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.TheaterDao;
import com.cinema.entity.Theater;
import com.cinema.validator.TheaterValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Theater.
 * 
 * Lớp này là tầng trung gian giữa Controller và TheaterDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa, thêm một số method)
 */
public class TheaterService {

    private final TheaterDao theaterDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo TheaterService với TheaterDao mặc định.
     */
    public TheaterService() {
        this(new TheaterDao());
    }

    /**
     * Constructor cho phép truyền TheaterDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế TheaterDao bằng đối tượng khác.
     *
     * @param theaterDao - Đối tượng DAO dùng để thao tác dữ liệu Theater
     * @throws IllegalArgumentException nếu theaterDao là null
     */
    public TheaterService(TheaterDao theaterDao) {
        if (theaterDao == null) {
            throw new IllegalArgumentException("theaterDao không được null!");
        }

        this.theaterDao = theaterDao;
    }

    /**
     * Lấy danh sách tất cả rạp.
     *
     * @return danh sách tất cả rạp
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Theater> getAllTheaters() throws SQLException {
        return theaterDao.getAllTheaters();
    }

    /**
     * Tìm rạp theo mã rạp.
     *
     * @param theaterId - Mã rạp cần tìm
     * @return đối tượng Theater nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu theaterId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Theater findTheaterById(int theaterId) throws SQLException {
        validateRequiredId(theaterId, "theaterId");

        return theaterDao.findById(theaterId);
    }

    /**
     * Thêm rạp mới.
     *
     * @param theater - Đối tượng rạp cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu rạp không hợp lệ hoặc tên rạp đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addTheater(Theater theater) throws SQLException {
        TheaterValidator.validateForCreate(theater);

        return theaterDao.addTheater(theater);
    }

    /**
     * Cập nhật thông tin rạp.
     *
     * @param theater - Đối tượng rạp cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu dữ liệu rạp không hợp lệ hoặc tên rạp đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateTheater(Theater theater) throws SQLException {
        TheaterValidator.validateForUpdate(theater);

        return theaterDao.updateTheater(theater);
    }

    /**
     * Xóa rạp theo mã rạp.
     *
     * @param theaterId - Mã rạp cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu theaterId không hợp lệ hoặc rạp đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteTheaterById(int theaterId) throws SQLException {
        validateRequiredId(theaterId, "theaterId");

        return theaterDao.deleteTheaterById(theaterId);
    }

    /**
     * Tìm kiếm rạp theo tên gần đúng.
     * 
     * Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách rạp.
     *
     * @param keyword - Từ khóa tìm kiếm
     * @return danh sách rạp phù hợp
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Theater> searchTheaters(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTheaters();
        }

        return theaterDao.searchByName(keyword.trim());
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
 * Các điểm đã bổ sung và chỉnh sửa so với TheaterService ban đầu:
 * 
 * 1. Sửa lại package cho đúng cấu trúc dự án:
 *    - Từ:
 *      package main.java.com.cinema.service;
 *    - Thành:
 *      package com.cinema.service;
 * 
 *    Lý do:
 *    Trong Java, package không ghi theo đường dẫn vật lý đầy đủ src/main/java.
 *    Vì class nằm trong src/main/java/com/cinema/service nên package đúng là
 *    com.cinema.service.
 * 
 * 2. Sửa import Theater cho đúng package:
 *    - Từ:
 *      import main.java.com.cinema.entity.Theater;
 *    - Thành:
 *      import com.cinema.entity.Theater;
 * 
 *    Lý do:
 *    Entity Theater nằm trong package com.cinema.entity, không nằm trong
 *    main.java.com.cinema.entity.
 * 
 * 3. Bổ sung import TheaterValidator:
 *    - Thêm:
 *      import com.cinema.validator.TheaterValidator;
 * 
 *    Lý do:
 *    Service không nên tự validate thủ công quá nhiều.
 *    Việc kiểm tra dữ liệu cơ bản của Theater nên được giao cho TheaterValidator
 *    để đồng bộ với cấu trúc Validator của dự án.
 * 
 * 4. Sửa constructor mặc định:
 *    - Từ:
 *      public TheaterService() {
 *          this.theaterDao = new TheaterDao();
 *      }
 * 
 *    - Thành:
 *      public TheaterService() {
 *          this(new TheaterDao());
 *      }
 * 
 *    Lý do:
 *    Giúp constructor mặc định tái sử dụng constructor có tham số,
 *    tránh lặp code và đồng bộ với style các Service khác.
 * 
 * 5. Bổ sung constructor có tham số TheaterDao:
 *    - Thêm:
 *      public TheaterService(TheaterDao theaterDao)
 * 
 *    Lý do:
 *    Cho phép truyền TheaterDao từ bên ngoài vào Service.
 *    Cách này hữu ích khi test, mock DAO hoặc thay thế DAO khác.
 * 
 * 6. Bổ sung kiểm tra null cho TheaterDao trong constructor:
 *    - Nếu theaterDao == null thì ném IllegalArgumentException.
 * 
 *    Lý do:
 *    Tránh lỗi NullPointerException về sau khi gọi các method trong Service.
 * 
 * 7. Sửa findTheaterById():
 *    - Bổ sung validateRequiredId(theaterId, "theaterId") trước khi gọi DAO.
 * 
 *    Lý do:
 *    ID truyền vào phải lớn hơn 0.
 *    Service nên kiểm tra dữ liệu đầu vào trước khi chuyển xuống DAO.
 * 
 * 8. Sửa addTheater():
 *    - Bỏ method validate(theater) tự viết trong Service.
 *    - Thay bằng TheaterValidator.validateForCreate(theater).
 *    - Bỏ đoạn gọi theaterDao.existsByName(theater.getTheaterName()).
 * 
 *    Lý do:
 *    TheaterValidator đã kiểm tra đầy đủ hơn validate cũ.
 *    Ngoài ra, existsByName() trong TheaterDao đang là private nên Service
 *    không được phép gọi trực tiếp. Việc kiểm tra trùng tên đã được xử lý
 *    bên trong TheaterDao.addTheater().
 * 
 * 9. Sửa updateTheater():
 *    - Bỏ validate(theater) tự viết.
 *    - Thay bằng TheaterValidator.validateForUpdate(theater).
 * 
 *    Lý do:
 *    Khi cập nhật, ngoài kiểm tra tên rạp, còn cần kiểm tra theaterId > 0.
 *    Rule này đã được viết trong TheaterValidator.validateForUpdate().
 * 
 * 10. Đổi tên method xóa:
 *     - Từ:
 *       deleteTheater(int id)
 * 
 *     - Thành:
 *       deleteTheaterById(int theaterId)
 * 
 *     Lý do:
 *     Tên method rõ nghĩa hơn, đồng bộ với TheaterDao.deleteTheaterById()
 *     và các Service khác trong dự án.
 * 
 * 11. Bổ sung kiểm tra ID trong deleteTheaterById():
 *     - Gọi validateRequiredId(theaterId, "theaterId") trước khi gọi DAO.
 * 
 *     Lý do:
 *     Không cho phép xóa với ID <= 0.
 *     Còn việc kiểm tra rạp đang được dùng trong phòng chiếu đã được xử lý
 *     ở TheaterDao.
 * 
 * 12. Giữ lại searchTheaters(String keyword):
 *     - Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách rạp.
 *     - Nếu có keyword thì trim và gọi theaterDao.searchByName(keyword.trim()).
 * 
 *     Lý do:
 *     Cách xử lý này hợp lý cho Controller.
 *     Khi ô tìm kiếm rỗng, giao diện có thể hiển thị lại toàn bộ dữ liệu.
 * 
 * 13. Xóa method validate(Theater theater) tự viết trong Service.
 * 
 *     Lý do:
 *     Method này chỉ kiểm tra null và theaterName rỗng, chưa kiểm tra đầy đủ
 *     như TheaterValidator. Nếu giữ lại sẽ bị trùng trách nhiệm và dễ lệch
 *     rule validate giữa các tầng.
 * 
 * 14. Bổ sung method validateRequiredId(int id, String fieldName):
 * 
 *     Lý do:
 *     Dùng chung cho các method cần ID bắt buộc như find và delete.
 *     Giúp code gọn hơn, tránh lặp điều kiện id <= 0 nhiều lần.
 * 
 * 15. Bổ sung Javadoc cho class, constructor và các method public.
 * 
 *     Lý do:
 *     Giúp người đọc hiểu rõ vai trò của TheaterService,
 *     tham số truyền vào, giá trị trả về và các lỗi có thể phát sinh.
 * 
 * Kết luận:
 * - TheaterService sau khi sửa đã đúng package.
 * - Không còn gọi method private của TheaterDao.
 * - Đã dùng TheaterValidator thay cho validate thủ công.
 * - Có constructor hỗ trợ test.
 * - Có kiểm tra ID ở tầng Service.
 * - Tên method rõ nghĩa và đồng bộ hơn với cấu trúc dự án.
 */