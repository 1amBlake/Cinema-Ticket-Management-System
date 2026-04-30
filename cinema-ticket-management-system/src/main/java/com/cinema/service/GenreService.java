package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.GenreDao;
import com.cinema.entity.Genre;
import com.cinema.validator.GenreValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Genre.
 * 
 * Lớp này là tầng trung gian giữa Controller và GenreDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Các ràng buộc chính:
 * - Tên thể loại không được để trống.
 * - Tên thể loại không được vượt quá 100 ký tự.
 * - Tên thể loại không được trùng trong hệ thống.
 * - Không cho phép xóa thể loại đang được gán cho phim.
 * 
 * @author Quốc Anh
 */
public class GenreService {

    private static final int MAX_GENRE_NAME_LENGTH = 100;

    private final GenreDao genreDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo GenreService với GenreDao mặc định.
     */
    public GenreService() {
        this(new GenreDao());
    }

    /**
     * Constructor cho phép truyền GenreDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế GenreDao bằng đối tượng khác.
     *
     * @param genreDao - Đối tượng DAO dùng để thao tác dữ liệu Genre
     * @throws IllegalArgumentException nếu genreDao là null
     */
    public GenreService(GenreDao genreDao) {
        if (genreDao == null) {
            throw new IllegalArgumentException("genreDao không được null!");
        }

        this.genreDao = genreDao;
    }

    /**
     * Lấy danh sách tất cả thể loại phim trong hệ thống.
     *
     * @return danh sách tất cả thể loại phim
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Genre> getAllGenres() throws SQLException {
        return genreDao.getAllGenres();
    }

    /**
     * Tìm thể loại phim theo mã thể loại.
     *
     * @param genreId - Mã thể loại cần tìm
     * @return đối tượng Genre nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu genreId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Genre findGenreById(int genreId) throws SQLException {
        if (genreId <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }

        return genreDao.findById(genreId);
    }

    /**
     * Thêm một thể loại phim mới vào hệ thống.
     * 
     * Dữ liệu thể loại sẽ được kiểm tra trước khi thêm vào cơ sở dữ liệu.
     * Tên thể loại không được trùng với thể loại đã tồn tại.
     *
     * @param genre - Đối tượng Genre cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu genre không hợp lệ hoặc tên thể loại đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addGenre(Genre genre) throws SQLException {
        GenreValidator.validateForCreate(genre);
        return genreDao.addGenre(genre);
    }

    /**
     * Cập nhật thông tin của một thể loại phim.
     * 
     * Dữ liệu thể loại sẽ được kiểm tra trước khi cập nhật.
     * Tên thể loại sau khi cập nhật không được trùng với thể loại khác.
     *
     * @param genre - Đối tượng Genre chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi cần cập nhật
     * @throws IllegalArgumentException nếu dữ liệu genre không hợp lệ hoặc tên thể loại đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateGenre(Genre genre) throws SQLException {
        GenreValidator.validateForUpdate(genre);
        return genreDao.updateGenre(genre);
    }

    /**
     * Xóa thể loại phim theo mã thể loại.
     * 
     * Không cho phép xóa nếu mã thể loại không hợp lệ.
     * Việc kiểm tra thể loại có đang được gán cho phim hay không
     * sẽ được xử lý ở GenreDao.
     *
     * @param genreId - Mã thể loại cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi cần xóa
     * @throws IllegalArgumentException nếu genreId không hợp lệ hoặc thể loại đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteGenreById(int genreId) throws SQLException {
        if (genreId <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }

        return genreDao.deleteGenreById(genreId);
    }

    /**
     * Tìm kiếm thể loại phim theo tên gần đúng.
     * 
     * Nếu keyword là null hoặc chuỗi rỗng thì DAO sẽ xử lý như tìm tất cả
     * các thể loại có tên phù hợp với chuỗi rỗng.
     *
     * @param keyword - Từ khóa tên thể loại cần tìm
     * @return danh sách thể loại có tên phù hợp với từ khóa
     * @throws IllegalArgumentException nếu keyword vượt quá 100 ký tự
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Genre> searchGenresByName(String keyword) throws SQLException {
        if (keyword != null && keyword.trim().length() > MAX_GENRE_NAME_LENGTH) {
            throw new IllegalArgumentException("keyword không được vượt quá 100 ký tự!");
        }

        return genreDao.searchByName(keyword);
    }
}
/*
 * Các điểm đã bổ sung so với service mẫu ban đầu:
 * - Bổ sung Javadoc cho class, constructor và các method để mô tả rõ chức năng.
 * - Đổi tên method theo hướng rõ nghĩa hơn, ví dụ getAllGenres(), addGenre(),
 *   updateGenre(), deleteGenreById(), searchGenresByName().
 * - Bổ sung findGenreById() để tìm thể loại theo mã, phục vụ xem chi tiết
 *   hoặc cập nhật dữ liệu từ Controller.
 * - Gọi GenreValidator ở tầng Service trước khi thêm hoặc cập nhật dữ liệu.
 * - Kiểm tra genreId hợp lệ trước khi tìm kiếm theo mã hoặc xóa thể loại.
 * - Kiểm tra độ dài keyword trước khi tìm kiếm theo tên.
 * - Bổ sung hằng số MAX_GENRE_NAME_LENGTH để đồng bộ với giới hạn
 *   tên thể loại trong database.
 * 
 * Các ràng buộc nghiệp vụ/database liên quan:
 * - Tên thể loại không được để trống.
 * - Tên thể loại không được vượt quá 100 ký tự.
 * - Tên thể loại không được trùng trong hệ thống.
 * - Không cho phép xóa thể loại đang được gán cho phim.
 * 
 * Lưu ý:
 * - Việc kiểm tra trùng tên thể loại được xử lý ở GenreDao khi thêm hoặc cập nhật.
 * - Việc kiểm tra thể loại có đang được gán cho phim hay không
 *   được xử lý ở GenreDao khi xóa.
 * - DAO vẫn chịu trách nhiệm thao tác trực tiếp với cơ sở dữ liệu.
 * Minh Huy
 */