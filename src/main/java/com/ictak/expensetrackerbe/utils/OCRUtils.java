package com.ictak.expensetrackerbe.utils;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.repository.CategoryRepository;
import com.ictak.expensetrackerbe.repository.PaymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OCRUtils {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private PaymentTypeRepository paymentTypeRepo;

    public ExpenseEntity extractInvoiceDetails(String text, int userId) {
        ExpenseEntity expense = null;

        // Extract and print the title (first line)
        String[] lines = text.split("\n");
        String title = lines[0];
        System.out.println("Title: " + title);

        // Extract payee
        Pattern payeePattern = Pattern.compile("Payee: (.+)");
        Matcher payeeMatcher = payeePattern.matcher(text);
        String payee = "";
        if (payeeMatcher.find()) {
            payee = payeeMatcher.group(1);
        }

        // Extract category
        Pattern categoryPattern = Pattern.compile("Category: (.+)");
        Matcher categoryMatcher = categoryPattern.matcher(text);
        int category = 1;
        if (categoryMatcher.find()) {
            category = categoryRepo.getCategoryId(categoryMatcher.group(1).trim());
        }

        // Extract date
        Pattern datePattern = Pattern.compile("Date: (.+)");
        Matcher dateMatcher = datePattern.matcher(text);
        LocalDate date = null;
        if (dateMatcher.find()) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(dateMatcher.group(1), df);
        }

        // Extract payment type
        Pattern paymentTypePattern = Pattern.compile("Payment Type: (.+)");
        Matcher paymentTypeMatcher = paymentTypePattern.matcher(text);
        int paymentType = 1;
        if (paymentTypeMatcher.find()) {
            paymentType = paymentTypeRepo.getPaymentTypeId(paymentTypeMatcher.group(1).trim());
        }

        // Extract tax
        Pattern taxPattern = Pattern.compile("Tax (.+)");
        Matcher taxMatcher = taxPattern.matcher(text);
        double taxRate = 0.0;
        if (taxMatcher.find()) {
            taxRate = Double.parseDouble(taxMatcher.group(1));
        }

        // Extract total
        Pattern totalPattern = Pattern.compile("Total (.+)");
        Matcher totalMatcher = totalPattern.matcher(text);
        double totalAmount = 0.0;
        if (totalMatcher.find()) {
            totalAmount = Double.parseDouble(totalMatcher.group(1).replaceAll(",", ""));
        }

        if (!title.isEmpty() && !payee.isEmpty() && date != null) {
            expense = new ExpenseEntity();
            expense.setUserId(userId);
            expense.setTitle(title);
            expense.setPayee(payee);
            expense.setCategory(category);
            expense.setPaymentType(paymentType);
            expense.setDescription("");
            expense.setAmount(totalAmount);
            expense.setGst(taxRate);
            expense.setDate(date.atStartOfDay());
        }
        return expense;
    }
}