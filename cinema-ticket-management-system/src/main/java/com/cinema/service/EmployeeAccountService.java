package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.EmployeeAccountDao;
import com.cinema.dao.EmployeeDao;
import com.cinema.entity.Employee;
import com.cinema.entity.EmployeeAccount;
import com.cinema.enums.AccountStatus;
import com.cinema.utils.AutoSetUserNameAccount;
import com.cinema.validator.EmployeeAccountValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể EmployeeAccount.
 * Tận dụng các bước kiểm tra trùng lặp đã có sẵn bên trong DAO.
 */
public class EmployeeAccountService {
	private final EmployeeDao employeeDao;
    private static final int MAX_KEYWORD_LENGTH = 255;
    private final EmployeeAccountDao accountDao;

    public EmployeeAccountService() {
        this.accountDao = new EmployeeAccountDao();
        this.employeeDao = new EmployeeDao();
    }

    public List<EmployeeAccount> getAllAccounts() throws SQLException {
        return accountDao.getAllEmployeeAccounts();
    }

    public EmployeeAccount findAccountById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Mã tài khoản (id) phải lớn hơn 0!");
        }
        return accountDao.findById(id);
    }

    public boolean addAccount(EmployeeAccount account) throws SQLException {

        // 1. Generate username từ employee bên trong account
        String username = AutoSetUserNameAccount.generateUsername(account.getEmployee());
        account.setAccountName(username);

        //  2. Validate
        EmployeeAccountValidator.validateForCreate(account);

        //  3. Insert DB
        return accountDao.addEmployeeAccount(account);
    }

    public boolean updateAccount(EmployeeAccount account) throws SQLException {
        // Validator đã kiểm tra ID > 0 và các định dạng dữ liệu khác
        EmployeeAccountValidator.validateForUpdate(account);
        
        // DAO sẽ xử lý việc kiểm tra trùng tên tài khoản ở bản ghi khác
        return accountDao.updateEmployeeAccount(account);
    }

    public boolean deleteAccount(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Mã tài khoản (id) phải lớn hơn 0!");
        }
        return accountDao.deleteEmployeeAccountById(id);
    }

    public List<EmployeeAccount> searchAccounts(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String trimmed = keyword.trim();
        if (trimmed.length() > MAX_KEYWORD_LENGTH) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được vượt quá " + MAX_KEYWORD_LENGTH + " ký tự!");
        }

        return accountDao.searchByAccountName(trimmed);
    }

    public boolean lockAccount(int accountId) throws SQLException {
        EmployeeAccount account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản để khóa!");
        }
        account.setAccountStatus(AccountStatus.LOCKED);
        return accountDao.updateEmployeeAccount(account);
    }

    public boolean unlockAccount(int accountId) throws SQLException {
        EmployeeAccount account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản để mở khóa!");
        }
        account.setAccountStatus(AccountStatus.ACTIVE);
        return accountDao.updateEmployeeAccount(account);
    }

    public EmployeeAccount login(String username, String password) throws SQLException {
        EmployeeAccount account = accountDao.login(username, password);

        if (account == null) return null;

        Employee employee = employeeDao.findById(account.getEmployee().getEmployeeId());

        account.setEmployee(employee);

        return account;
    }
}