package com.bank.BankSimulation.controller;

import com.bank.BankSimulation.dto.CreateAccountRequest;
import com.bank.BankSimulation.dto.TransferRequest;
import com.bank.BankSimulation.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;
    @PostMapping
    public ResponseEntity<CreateAccountRequest> createAccount(@RequestBody CreateAccountRequest request){
        return new ResponseEntity(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<BigDecimal> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        BigDecimal updatedBalance=accountService.deposit(accountNumber,amount);
        return new ResponseEntity<>(updatedBalance,HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<BigDecimal> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount){
        BigDecimal updatedBalance=accountService.withdraw(accountNumber,amount);
        return new ResponseEntity<>(updatedBalance,HttpStatus.OK);
    }

    @DeleteMapping("/{accountNumber}/delete")
    public ResponseEntity<String> delete(@PathVariable String accountNumber){
        accountService.delete(accountNumber);
        return ResponseEntity.ok("Account deleted Successfully");
    }

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<String> transfer(@PathVariable String accountNumber,@RequestBody TransferRequest request){
        accountService.transfer(accountNumber,request.getAccountNumber(),request.getAmount());
        return ResponseEntity.ok("Transaction Successfull");
    }

    @GetMapping("/{accountNumber}/balance")
    public BigDecimal checkBalance(@PathVariable String accountNumber){
        return accountService.checkBalance(accountNumber);
    }
}
