package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class MainController {
    private TransactionDao transactionDao;

    MainController(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @GetMapping("/")
    public String main (){
        return "index";
    }

    @GetMapping("/dodaj")
    public String addTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "addTransactionForm";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(Transaction transaction) {
        transactionDao.addTransaction(transaction);
        return "redirect:/";
    }

    @GetMapping("/przychody")
    public String przychodyForm(Model model) {
        List<Transaction> transactions = transactionDao.readTransaction(true);
        model.addAttribute("transactions", transactions);
        return "transactionsForm";
    }

    @GetMapping("/wydatki")
    public String wydatkiForm(Model model) {
        List<Transaction> transactions = transactionDao.readTransaction(false);
        model.addAttribute("transactions", transactions);
        return "transactionsForm";
    }

    @GetMapping("/findExpensesAmount")
    public String findExpensesAmount(ExpenseFind find, Model model) {
        List<Transaction> transactions = transactionDao.readAboveAmount(find.getAmountAbove());
        model.addAttribute("transactions", transactions);
        return "transactionsForm";
    }
}