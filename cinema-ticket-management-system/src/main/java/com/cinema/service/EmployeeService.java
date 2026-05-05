package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.EmployeeDao;
import com.cinema.entity.Employee;
import com.cinema.validator.EmployeeValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Employee (Nhân viên).
 * * Lớp này là tầng trung gian giữa Controller và EmployeeDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * * Các ràng buộc chính:
 * - Tên nhân viên không được để trống và không vượt quá giới hạn ký tự.
 * - Ngày sinh không được ở tương lai, ngày vào làm hợp lệ.
 * - Không cho phép thêm/cập nhật nhân viên thiếu chức vụ (JobTitle) hoặc trạng thái.
 * - Id phải lớn hơn 0 khi thực hiện tìm kiếm, cập nhật, xóa.
 * * @author Trọng
 * @author Huy (sửa)
 */
public class EmployeeService {

    private static final int MAX_KEYWORD_LENGTH = 255;

    private final EmployeeDao employeeDao;

    public EmployeeService() {
        this.employeeDao = new EmployeeDao();
    }

    /**
     * Lấy danh sách tất cả nhân viên.
     */
    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDao.getAllEmployees();
    }

    /**
     * Tìm kiếm nhân viên theo ID.
     */
    public Employee findEmployeeById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Mã nhân viên (id) phải lớn hơn 0!");
        }
        return employeeDao.findById(id);
    }

    /**
     * Thêm mới một nhân viên.
     */
    public boolean addEmployee(Employee employee) throws SQLException {
        // Sử dụng Validator chuyên dụng của hệ thống
        EmployeeValidator.validateForCreate(employee);
        
        return employeeDao.addEmployee(employee);
    }

    /**
     * Cập nhật thông tin nhân viên.
     */
    public boolean updateEmployee(Employee employee) throws SQLException {
        // Sử dụng Validator chuyên dụng của hệ thống
        EmployeeValidator.validateForUpdate(employee);
        
        return employeeDao.updateEmployee(employee);
    }

    /**
     * Xóa nhân viên theo ID.
     */
    public boolean deleteEmployeeById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Mã nhân viên (id) phải lớn hơn 0!");
        }
        
        // Có thể bổ sung business rule: Không cho phép xóa nếu nhân viên này 
        // đang tồn tại hóa đơn (Invoices) chưa thanh toán, hoặc thay vì xóa cứng thì set trạng thái RESIGNED.
        
        return employeeDao.deleteEmployeeById(id);
    }

    /**
     * Tìm kiếm nhân viên theo tên gần đúng.
     */
    public List<Employee> searchEmployeesByName(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(); // Trả về list rỗng nếu từ khóa trống
        }
        
        if (keyword.trim().length() > MAX_KEYWORD_LENGTH) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được vượt quá " + MAX_KEYWORD_LENGTH + " ký tự!");
        }
        
        return employeeDao.searchByName(keyword);
    }
}