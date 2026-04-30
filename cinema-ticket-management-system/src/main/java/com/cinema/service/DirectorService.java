package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.DirectorDao;
import com.cinema.entity.Director;
import com.cinema.validator.DirectorValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Director.
 * 
 * Lớp này là tầng trung gian giữa Controller và DirectorDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * @author Quốc Anh
 */
public class DirectorService {

    private final DirectorDao directorDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo DirectorService với DirectorDao mặc định.
     */
    public DirectorService() {
        this(new DirectorDao());
    }

    /**
     * Constructor cho phép truyền DirectorDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế DirectorDao bằng đối tượng khác.
     *
     * @param directorDao - Đối tượng DAO dùng để thao tác dữ liệu Director
     * @throws IllegalArgumentException nếu directorDao là null
     */
    public DirectorService(DirectorDao directorDao) {
        if (directorDao == null) {
            throw new IllegalArgumentException("directorDao không được null!");
        }

        this.directorDao = directorDao;
    }

    /**
     * Lấy danh sách tất cả đạo diễn trong hệ thống.
     *
     * @return danh sách tất cả đạo diễn
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Director> getAllDirectors() throws SQLException {
        return directorDao.getAllDirectors();
    }

    /**
     * Tìm đạo diễn theo mã đạo diễn.
     *
     * @param directorId - Mã đạo diễn cần tìm
     * @return đối tượng Director nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu directorId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Director findDirectorById(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        return directorDao.findById(directorId);
    }

    /**
     * Thêm một đạo diễn mới vào hệ thống.
     * 
     * Dữ liệu đạo diễn sẽ được kiểm tra trước khi thêm vào cơ sở dữ liệu.
     *
     * @param director - Đối tượng Director cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu director không hợp lệ
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addDirector(Director director) throws SQLException {
        DirectorValidator.validateForCreate(director);
        return directorDao.addDirector(director);
    }

    /**
     * Cập nhật thông tin của một đạo diễn.
     * 
     * Dữ liệu đạo diễn sẽ được kiểm tra trước khi cập nhật.
     *
     * @param director - Đối tượng Director chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi cần cập nhật
     * @throws IllegalArgumentException nếu dữ liệu director không hợp lệ
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateDirector(Director director) throws SQLException {
        DirectorValidator.validateForUpdate(director);
        return directorDao.updateDirector(director);
    }

    /**
     * Xóa đạo diễn theo mã đạo diễn.
     * 
     * Không cho phép xóa nếu mã đạo diễn không hợp lệ.
     * Việc kiểm tra đạo diễn có đang được gán cho phim hay không
     * sẽ được xử lý ở DirectorDao.
     *
     * @param directorId - Mã đạo diễn cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi cần xóa
     * @throws IllegalArgumentException nếu directorId không hợp lệ hoặc đạo diễn đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteDirectorById(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        return directorDao.deleteDirectorById(directorId);
    }

    /**
     * Tìm kiếm đạo diễn theo tên gần đúng.
     * 
     * Nếu keyword là null hoặc chuỗi rỗng thì DAO sẽ xử lý như tìm tất cả
     * các đạo diễn có tên phù hợp với chuỗi rỗng.
     *
     * @param keyword - Từ khóa tên đạo diễn cần tìm
     * @return danh sách đạo diễn có tên phù hợp với từ khóa
     * @throws IllegalArgumentException nếu keyword vượt quá 255 ký tự
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Director> searchDirectorsByName(String keyword) throws SQLException {
        if (keyword != null && keyword.trim().length() > 255) {
            throw new IllegalArgumentException("keyword không được vượt quá 255 ký tự!");
        }

        return directorDao.searchByName(keyword);
    }
}

/*
 * Các điểm đã bổ sung so với service mẫu ban đầu:
 * - Bổ sung Javadoc cho class, constructor và các method để mô tả rõ chức năng.
 * - Đổi tên method theo hướng rõ nghĩa hơn, ví dụ addDirector(), updateDirector(),
 *   deleteDirectorById(), searchDirectorsByName().
 * - Bổ sung findDirectorById() để tìm đạo diễn theo mã, phục vụ xem chi tiết
 *   hoặc cập nhật dữ liệu từ Controller.
 * - Gọi DirectorValidator ở tầng Service trước khi thêm hoặc cập nhật dữ liệu.
 * - Kiểm tra directorId hợp lệ trước khi tìm kiếm theo mã hoặc xóa đạo diễn.
 * - Kiểm tra độ dài keyword trước khi tìm kiếm theo tên.
 * 
 * Lưu ý:
 * - Việc kiểm tra đạo diễn có đang được gán cho phim hay không
 *   được xử lý ở DirectorDao khi xóa.
 * - DAO vẫn chịu trách nhiệm thao tác trực tiếp với cơ sở dữ liệu.
 */