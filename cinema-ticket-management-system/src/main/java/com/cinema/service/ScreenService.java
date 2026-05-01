package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.ScreenDao;
import com.cinema.entity.Screen;
import com.cinema.validator.ScreenValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Screen.
 * 
 * Lớp này là tầng trung gian giữa Controller và ScreenDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * liên quan đến phòng chiếu và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Lưu ý:
 * - ScreenStatus chỉ thể hiện trạng thái vận hành của phòng chiếu:
 *   AVAILABLE, MAINTENANCE hoặc UNAVAILABLE.
 * - Việc kiểm tra rạp có tồn tại hay không được xử lý ở ScreenDao.
 * - Việc kiểm tra loại phòng chiếu có tồn tại hay không được xử lý ở ScreenDao.
 * - Việc kiểm tra trùng tên phòng trong cùng rạp được xử lý ở ScreenDao.
 * - Việc kiểm tra phòng chiếu đã được sử dụng trước khi xóa được xử lý ở ScreenDao.
 * 
 * @author Trọng
 * @author Minh Huy
 */
public class ScreenService {

    private final ScreenDao screenDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo ScreenService với ScreenDao mặc định.
     */
    public ScreenService() {
        this(new ScreenDao());
    }

    /**
     * Constructor cho phép truyền ScreenDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế ScreenDao bằng đối tượng khác.
     *
     * @param screenDao - DAO dùng để thao tác dữ liệu Screen
     * @throws IllegalArgumentException nếu screenDao là null
     */
    public ScreenService(ScreenDao screenDao) {
        if (screenDao == null) {
            throw new IllegalArgumentException("screenDao không được null!");
        }

        this.screenDao = screenDao;
    }

    /**
     * Lấy danh sách tất cả phòng chiếu.
     *
     * @return danh sách tất cả phòng chiếu
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Screen> getAllScreens() throws SQLException {
        return screenDao.getAllScreens();
    }

    /**
     * Tìm phòng chiếu theo mã phòng.
     *
     * @param screenId - Mã phòng chiếu cần tìm
     * @return đối tượng Screen nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu screenId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Screen findScreenById(int screenId) throws SQLException {
        validateRequiredId(screenId, "screenId");

        return screenDao.findById(screenId);
    }

    /**
     * Thêm phòng chiếu mới.
     *
     * @param screen - Phòng chiếu cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu phòng chiếu không hợp lệ,
     *                                  rạp không tồn tại,
     *                                  loại phòng chiếu không tồn tại
     *                                  hoặc tên phòng đã tồn tại trong cùng rạp
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addScreen(Screen screen) throws SQLException {
        ScreenValidator.validateForCreate(screen);

        return screenDao.addScreen(screen);
    }

    /**
     * Cập nhật thông tin phòng chiếu.
     *
     * @param screen - Phòng chiếu cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu dữ liệu phòng chiếu không hợp lệ,
     *                                  rạp không tồn tại,
     *                                  loại phòng chiếu không tồn tại
     *                                  hoặc tên phòng bị trùng trong cùng rạp
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateScreen(Screen screen) throws SQLException {
        ScreenValidator.validateForUpdate(screen);

        return screenDao.updateScreen(screen);
    }

    /**
     * Xóa phòng chiếu theo mã phòng.
     * 
     * Không cho phép xóa nếu phòng chiếu đã có ghế hoặc đã phát sinh suất chiếu.
     *
     * @param screenId - Mã phòng chiếu cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu screenId không hợp lệ hoặc phòng chiếu đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteScreenById(int screenId) throws SQLException {
        validateRequiredId(screenId, "screenId");

        return screenDao.deleteScreenById(screenId);
    }

    /**
     * Lấy danh sách phòng chiếu theo mã rạp.
     *
     * @param theaterId - Mã rạp
     * @return danh sách phòng chiếu thuộc rạp
     * @throws IllegalArgumentException nếu theaterId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Screen> getScreensByTheaterId(int theaterId) throws SQLException {
        validateRequiredId(theaterId, "theaterId");

        List<Screen> result = new ArrayList<>();

        for (Screen screen : screenDao.getAllScreens()) {
            if (screen.getTheater() != null
                    && screen.getTheater().getTheaterId() == theaterId) {
                result.add(screen);
            }
        }

        return result;
    }

    /**
     * Tìm kiếm phòng chiếu theo tên gần đúng.
     * 
     * Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách phòng chiếu.
     *
     * @param keyword - Từ khóa tìm kiếm
     * @return danh sách phòng chiếu phù hợp
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Screen> searchScreens(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllScreens();
        }

        return screenDao.searchByName(keyword.trim());
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
 * Các điểm đã bổ sung và chỉnh sửa so với ScreenService ban đầu:
 * 
 * 1. Bổ sung import ArrayList:
 *    - Thêm:
 *      import java.util.ArrayList;
 * 
 *    Lý do:
 *    Method getScreensByTheaterId() version mới dùng ArrayList để lưu danh sách
 *    phòng chiếu sau khi lọc theo mã rạp.
 * 
 * 2. Bổ sung import ScreenValidator:
 *    - Thêm:
 *      import com.cinema.validator.ScreenValidator;
 * 
 *    Lý do:
 *    Service không nên tự kiểm tra dữ liệu thủ công bằng method validate()
 *    riêng. Việc kiểm tra dữ liệu cơ bản và ràng buộc nghiệp vụ nội tại
 *    của Screen nên được giao cho ScreenValidator để đồng bộ với cấu trúc
 *    Validator của dự án.
 * 
 * 3. Bổ sung Javadoc cho class ScreenService:
 * 
 *    Lý do:
 *    Giúp mô tả rõ vai trò của ScreenService là tầng trung gian giữa
 *    Controller và ScreenDao. Service chịu trách nhiệm kiểm tra dữ liệu
 *    đầu vào, xử lý nghiệp vụ cơ bản và gọi DAO để thao tác database.
 * 
 * 4. Bổ sung ghi chú nghiệp vụ trong Javadoc của class:
 *    - ScreenStatus chỉ thể hiện trạng thái vận hành của phòng chiếu:
 *      AVAILABLE, MAINTENANCE hoặc UNAVAILABLE.
 *    - Việc kiểm tra rạp tồn tại được xử lý ở ScreenDao.
 *    - Việc kiểm tra loại phòng chiếu tồn tại được xử lý ở ScreenDao.
 *    - Việc kiểm tra trùng tên phòng trong cùng rạp được xử lý ở ScreenDao.
 *    - Việc kiểm tra phòng chiếu đã được sử dụng trước khi xóa được xử lý ở ScreenDao.
 * 
 *    Lý do:
 *    Làm rõ tầng trách nhiệm:
 *    - Validator kiểm tra dữ liệu nội tại của object.
 *    - Service kiểm tra input và điều phối nghiệp vụ.
 *    - DAO xử lý các rule cần truy vấn database.
 * 
 * 5. Sửa constructor mặc định:
 *    - Từ:
 *      public ScreenService() {
 *          this.screenDao = new ScreenDao();
 *      }
 * 
 *    - Thành:
 *      public ScreenService() {
 *          this(new ScreenDao());
 *      }
 * 
 *    Lý do:
 *    Constructor mặc định tái sử dụng constructor có tham số,
 *    giúp tránh lặp code và đồng bộ với style các Service khác.
 * 
 * 6. Bổ sung constructor có tham số ScreenDao:
 *    - Thêm:
 *      public ScreenService(ScreenDao screenDao)
 * 
 *    Lý do:
 *    Cho phép truyền ScreenDao từ bên ngoài vào Service.
 *    Cách này hữu ích khi test, mock DAO hoặc thay thế DAO khác.
 * 
 * 7. Bổ sung kiểm tra null cho ScreenDao trong constructor:
 *    - Nếu screenDao == null thì ném IllegalArgumentException.
 * 
 *    Lý do:
 *    Tránh lỗi NullPointerException về sau khi gọi các method trong Service.
 * 
 * 8. Bổ sung Javadoc cho các method public:
 * 
 *    Lý do:
 *    Giúp người đọc hiểu rõ chức năng từng method, tham số truyền vào,
 *    giá trị trả về và các lỗi có thể phát sinh.
 * 
 * 9. Sửa findScreenById():
 *    - Từ:
 *      public Screen findScreenById(int id) throws SQLException {
 *          return screenDao.findById(id);
 *      }
 * 
 *    - Thành:
 *      public Screen findScreenById(int screenId) throws SQLException {
 *          validateRequiredId(screenId, "screenId");
 *          return screenDao.findById(screenId);
 *      }
 * 
 *    Lý do:
 *    ID phòng chiếu bắt buộc phải lớn hơn 0.
 *    Service nên kiểm tra dữ liệu đầu vào trước khi chuyển xuống DAO.
 * 
 * 10. Sửa addScreen():
 *     - Từ:
 *       validate(screen);
 * 
 *     - Thành:
 *       ScreenValidator.validateForCreate(screen);
 * 
 *     Lý do:
 *     Method validate() cũ chỉ kiểm tra screen null, screenName rỗng,
 *     theater null và screenType null.
 * 
 *     Trong khi ScreenValidator kiểm tra đầy đủ hơn:
 *     - screen không null.
 *     - screenName không rỗng.
 *     - screenName không vượt quá 255 ký tự.
 *     - theater không null.
 *     - theaterId phải lớn hơn 0.
 *     - screenType không null.
 *     - screenTypeId phải lớn hơn 0.
 *     - screenStatus không null.
 *     - business rule cho định dạng screenName.
 * 
 * 11. Sửa updateScreen():
 *     - Từ:
 *       validate(screen);
 * 
 *     - Thành:
 *       ScreenValidator.validateForUpdate(screen);
 * 
 *     Lý do:
 *     Khi cập nhật phòng chiếu, ngoài các kiểm tra chung, còn bắt buộc
 *     screenId > 0. Rule này nằm trong ScreenValidator.validateForUpdate().
 * 
 * 12. Đổi tên method xóa:
 *     - Từ:
 *       deleteScreen(int id)
 * 
 *     - Thành:
 *       deleteScreenById(int screenId)
 * 
 *     Lý do:
 *     Tên method rõ nghĩa hơn, đồng bộ với ScreenDao.deleteScreenById()
 *     và các Service khác trong dự án.
 * 
 * 13. Bổ sung kiểm tra ID trong deleteScreenById():
 *     - Gọi validateRequiredId(screenId, "screenId") trước khi gọi DAO.
 * 
 *     Lý do:
 *     Không cho phép xóa với screenId <= 0.
 *     Việc không cho xóa phòng chiếu nếu đã có ghế hoặc suất chiếu
 *     được xử lý ở ScreenDao.
 * 
 * 14. Sửa getScreensByTheaterId():
 *     - Version cũ:
 *       + Kiểm tra theaterId bằng if trực tiếp.
 *       + Dùng stream().filter().toList() để lọc danh sách.
 *       + Không kiểm tra screen.getTheater() null trước khi gọi getTheaterId().
 * 
 *     - Version mới:
 *       + Dùng validateRequiredId(theaterId, "theaterId") để kiểm tra ID.
 *       + Dùng vòng for và ArrayList để code dễ đọc, dễ giải thích hơn.
 *       + Kiểm tra screen.getTheater() != null trước khi lấy theaterId.
 * 
 *     Lý do:
 *     Cách viết mới dễ hiểu hơn với đồ án và tránh lỗi NullPointerException
 *     nếu có Screen nào bị thiếu Theater khi ánh xạ dữ liệu.
 * 
 * 15. Bổ sung searchScreens(String keyword):
 * 
 *     Lý do:
 *     Hỗ trợ tìm kiếm phòng chiếu theo tên gần đúng.
 *     Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách phòng chiếu.
 *     Cách này thuận tiện cho Controller/GUI khi ô tìm kiếm bị để trống.
 * 
 * 16. Xóa method validate(Screen screen) tự viết trong Service.
 * 
 *     Lý do:
 *     Method validate() cũ bị thiếu nhiều rule quan trọng:
 *     - Không kiểm tra screenName quá 255 ký tự.
 *     - Không kiểm tra theaterId > 0.
 *     - Không kiểm tra screenTypeId > 0.
 *     - Không kiểm tra screenStatus null.
 *     - Không kiểm tra screenId > 0 khi update.
 *     - Không kiểm tra business rule của screenName.
 * 
 *     Các kiểm tra này đã được chuyển về ScreenValidator.
 * 
 * 17. Bổ sung method validateRequiredId(int id, String fieldName):
 * 
 *     Lý do:
 *     Dùng chung cho các method cần ID bắt buộc như find, delete
 *     và getScreensByTheaterId. Cách này giúp tránh lặp code và thống nhất
 *     thông báo lỗi.
 * 
 * 18. Chuẩn hóa thông báo lỗi:
 *     - Version cũ dùng tiếng Anh như:
 *       "Invalid theaterId", "Screen is null", "Screen name is required".
 * 
 *     - Version mới dùng tiếng Việt đồng bộ hơn:
 *       "screenId phải lớn hơn 0!",
 *       "theaterId phải lớn hơn 0!",
 *       và các lỗi validate nằm trong ScreenValidator.
 * 
 *     Lý do:
 *     Dự án đang dùng thông báo lỗi tiếng Việt ở Entity, Validator, DAO
 *     và các Service khác.
 * 
 * Kết luận:
 * - Version mới rõ tầng trách nhiệm hơn:
 *   Service không tự validate thủ công nữa mà gọi ScreenValidator.
 *   DAO xử lý các ràng buộc cần truy vấn database như rạp tồn tại,
 *   loại phòng tồn tại, trùng tên phòng trong cùng rạp và phòng đã được sử dụng.
 * 
 * - Version mới an toàn hơn:
 *   Có kiểm tra ID trước khi gọi DAO.
 *   Tránh lỗi NullPointerException trong getScreensByTheaterId().
 *   Không còn validate thủ công thiếu rule.
 * 
 * - Version mới dễ bảo trì hơn:
 *   Có constructor truyền DAO để hỗ trợ test.
 *   Tên method rõ nghĩa hơn.
 *   Có Javadoc mô tả đầy đủ chức năng và nghiệp vụ.
 */