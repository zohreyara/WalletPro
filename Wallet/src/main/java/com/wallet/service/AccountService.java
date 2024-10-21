package com.wallet.service;

import com.wallet.entity.Account;
import com.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // Create a new account with validation
    public Account createAccount(Account account) {
        validateAccount(account); // Validate before saving
        return accountRepository.save(account);
    }

    // واریز مبلغ به مانده حساب همراه با اعتبارسنجی
    public Account deposit(Long accountId, double amount) {
        validateAmount(amount); // Validate deposit amount

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setBalance(account.getBalance() + amount); // Update balance
            return accountRepository.save(account); // Save updated account
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    // برداشت مبلغ از مانده حساب با اعتبارسنجی
    public Account withdraw(Long accountId, double amount) {
        validateAmount(amount); // Validate withdrawal amount

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            if (account.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            account.setBalance(account.getBalance() - amount); // Update balance
            return accountRepository.save(account); // Save updated account
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    // Validate the Account entity
    private void validateAccount(Account account) {
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Account> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Validation errors: \n" + sb.toString());
        }

        if (account.getBalance() < 10000) { // Minimum balance check
            throw new IllegalArgumentException("Minimum balance must be at least 10,000 Rials.");
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (amount > 10000000) { // Daily withdrawal limit check
            throw new IllegalArgumentException("Withdrawal limit exceeded. Maximum allowed is 10,000,000 Rials.");
        }
    }

    public List<Account> getAllAccounts() {
        return null;
    }

    public Account getAccountById(Long id) {
        return null;
    }

    public boolean deleteAccount(Long id) {
        return false;
    }

    public Account updateAccount(Long id, Account account) {
        return account;
    }



}
