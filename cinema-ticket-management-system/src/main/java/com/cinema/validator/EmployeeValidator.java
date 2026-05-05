package com.cinema.validator;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.cinema.entity.Employee;

/**
 * Validator cho thực thể Employee.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Employee
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class EmployeeValidator {
	private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}]+(\\s[\\p{L}]+)*$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[35789]\\d{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$");
    /**
     * Constructor private để ngăn tạo đối tượng từ lớp EmployeeValidator.
     */
    private EmployeeValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Employee trong trường hợp thêm mới.
     *
     * @param employee - Đối tượng Employee cần kiểm tra
     */
    public static void validateForCreate(Employee employee) {
        validateCommon(employee);
    }

    /**
     * Kiểm tra dữ liệu của Employee trong trường hợp cập nhật.
     *
     * @param employee - Đối tượng Employee cần kiểm tra
     */
    public static void validateForUpdate(Employee employee) {
        validateCommon(employee);

        if (employee.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Employee.
     *
     * @param employee - Đối tượng Employee cần kiểm tra
     */
    private static void validateCommon(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("employee không được null!");
        }

        if (employee.getEmployeeName() == null || employee.getEmployeeName().trim().isEmpty()) {
            throw new IllegalArgumentException("employeeName không được để trống!");
        }

        if (employee.getEmployeeName().trim().length() > 255) {
            throw new IllegalArgumentException("employeeName không được vượt quá 255 ký tự!");
        }

        if (employee.getEmployeePhoneNumber() != null
                && employee.getEmployeePhoneNumber().trim().length() > 20) {
            throw new IllegalArgumentException("employeePhoneNumber không được vượt quá 20 ký tự!");
        }

        if (employee.getBirthDate() != null && employee.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthDate không được ở tương lai!");
        }

        if (employee.getEmployeeEmail() != null
                && employee.getEmployeeEmail().trim().length() > 255) {
            throw new IllegalArgumentException("employeeEmail không được vượt quá 255 ký tự!");
        }

        if (employee.getHireDate() != null
                && employee.getHireDate().isAfter(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("hireDate không hợp lệ!");
        }

        if (employee.getJobTitle() == null) {
            throw new IllegalArgumentException("jobTitle không được null!");
        }

        if (employee.getJobTitle().getJobTitleId() <= 0) {
            throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
        }

        if (employee.getEmployeeStatus() == null) {
            throw new IllegalArgumentException("employeeStatus không được null!");
        }

        validateBusinessRule(employee);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Employee.
     *
     * @param employee - Đối tượng Employee cần kiểm tra
     */
    private static void validateBusinessRule(Employee employee) {
    	String name = employee.getEmployeeName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("employeeName không được để trống!");
        }
        if (name.trim().length() > 255) {
            throw new IllegalArgumentException("employeeName không được vượt quá 255 ký tự!");
        }
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new IllegalArgumentException("Tên nhân viên không hợp lệ (không được chứa số hoặc ký tự đặc biệt)!");
        }

        // 2. Kiểm tra Số điện thoại (Regex VN 10 số)
        String phone = employee.getEmployeePhoneNumber();
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ (Phải gồm 10 số và bắt đầu bằng 03, 05, 07, 08 hoặc 09)!");
            }
        }

        // 3. Kiểm tra Email (Regex định dạng chuẩn)
        String email = employee.getEmployeeEmail();
        if (email != null && !email.trim().isEmpty()) {
            if (email.trim().length() > 255) {
                throw new IllegalArgumentException("employeeEmail không được vượt quá 255 ký tự!");
            }
            if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new IllegalArgumentException("Email không đúng định dạng (VD: example@domain.com)!");
            }
        }
        
        if (employee.getBirthDate() != null) {
            int age = LocalDate.now().getYear() - employee.getBirthDate().getYear();
            if (age < 18) {
                throw new IllegalArgumentException("Nhân viên phải từ đủ 18 tuổi trở lên!");
            }
        }
    }
}