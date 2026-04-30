package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.EmployeeAccountDao;
import com.cinema.entity.EmployeeAccount;
import com.cinema.enums.AccountStatus;


public class EmployeeAccountService {

    private final EmployeeAccountDao accountDao;

    public EmployeeAccountService() {
        this.accountDao = new EmployeeAccountDao();
    }


    public List<EmployeeAccount> getAllAccounts() throws SQLException {
        return accountDao.getAllEmployeeAccounts();
    }


    public EmployeeAccount findAccountById(int id) throws SQLException {
        return accountDao.findById(id);
    }

 
    public EmployeeAccount findAccountByUsername(String username) throws SQLException {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }


        return accountDao.searchByAccountName(username.trim())
                .stream()
                .filter(acc -> acc.getAccountName().equalsIgnoreCase(username.trim()))
                .findFirst()
                .orElse(null);
    }


    public boolean addAccount(EmployeeAccount account) throws SQLException {
        validate(account);
        return accountDao.addEmployeeAccount(account);
    }

    public boolean updateAccount(EmployeeAccount account) throws SQLException {
        validate(account);
        return accountDao.updateEmployeeAccount(account);
    }


    public boolean deleteAccount(int id) throws SQLException {
        return accountDao.deleteEmployeeAccountById(id);
    }


    public boolean lockAccount(int accountId) throws SQLException {

        EmployeeAccount account = accountDao.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        account.setAccountStatus(AccountStatus.INACTIVE);

        return accountDao.updateEmployeeAccount(account);
    }


    public boolean unlockAccount(int accountId) throws SQLException {

        EmployeeAccount account = accountDao.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        account.setAccountStatus(AccountStatus.ACTIVE);

        return accountDao.updateEmployeeAccount(account);
    }


    public EmployeeAccount login(String username, String password) throws SQLException {
        return accountDao.login(username, password);
    }


    private void validate(EmployeeAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null");
        }

        if (account.getAccountName() == null || account.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (account.getAccountPassword() == null || account.getAccountPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (account.getEmployee() == null) {
            throw new IllegalArgumentException("Employee is required");
        }

        if (account.getAccountStatus() == null) {
            throw new IllegalArgumentException("Account status is required");
        }
    }
}