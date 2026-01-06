package com.bank.BankSimulation.service;

import com.bank.BankSimulation.dto.CreateAccountRequest;
import com.bank.BankSimulation.model.Account;
import com.bank.BankSimulation.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    // CRUD operations
    @Autowired
    AccountRepository accountRepository;
    public Account createAccount(CreateAccountRequest request){
        Account account=new Account();
        account.setName(request.getName());
        account.setAge(request.getAge());
        account.setBalance(BigDecimal.ZERO);
        Account saved=accountRepository.save(account);
        String accountNumber=String.format("%016d",saved.getId());
        saved.setAccountNumber(accountNumber);
        return accountRepository.save(saved);
    }
    public BigDecimal deposit(String accountNumber, BigDecimal amount){
        Account account=accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));
        account.deposit(amount);
        accountRepository.save(account);
        return account.getBalance();
    }
    public BigDecimal withdraw(String accountNumber,BigDecimal amount){
        Account account=accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));
        account.withdraw(amount);
        accountRepository.save(account);
        return account.getBalance();
    }
    public void delete(String accountNumber){
        Account account=accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }
    @Transactional
    public void transfer(String accountNumber1,String accountNumber2,BigDecimal amount){
        Account account1=accountRepository.findByAccountNumber(accountNumber1).orElseThrow(()-> new RuntimeException("Your Account not found"));
        Account account2=accountRepository.findByAccountNumber(accountNumber2).orElseThrow(()-> new RuntimeException("Recievers Account not found"));
        if(amount==null || amount.compareTo(BigDecimal.ZERO)<=0 || amount.compareTo(account1.getBalance())>0){
            throw new IllegalArgumentException("Insufficient Balance");
        }
        account1.withdraw(amount);
        account2.deposit(amount);
        accountRepository.save(account1);
        accountRepository.save(account2);
    }
    public BigDecimal checkBalance(String accountNumber){
        Account account=accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));
        return account.getBalance();
    }
}
