package com.example.financemanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Incomes {
    private final LocalDate date;
    private final float total;
    private final float salary;
    private final float helpers;
    private final float autobusiness;
    private final float positiveincome;
    private final float other;

    private final static String PRICE_FORMAT = "%.2f €";

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    public Incomes(LocalDate date, float salary, float helpers, float autobusiness, float positiveincome, float other) {
        this.date = date;
        this.total = salary + helpers + autobusiness + positiveincome + other;
        this.salary = salary;
        this.helpers = helpers;
        this.autobusiness = autobusiness;
        this.positiveincome = positiveincome;
        this.other = other;
    }

    public StringProperty dateProperty() {
        return new SimpleStringProperty(date.format(DATE_FORMAT));
    }

    public StringProperty totalProperty() {
        return formatAmount(total);
    }

    private SimpleStringProperty formatAmount(float amount) {
        return new SimpleStringProperty(String.format(PRICE_FORMAT, amount));
    }

    public StringProperty salaryProperty() {
        return formatAmount(salary);
    }

    public StringProperty helpersProperty() {
        return formatAmount(helpers);
    }

    public StringProperty autobusinessProperty() {
        return formatAmount(autobusiness);
    }

    public StringProperty positiveincomeProperty() {
        return formatAmount(positiveincome);
    }

    public StringProperty otherProperty() {
        return formatAmount(other);
    }

    public LocalDate getDate() {
        return date;
    }

    public float getTotal() {
        return total;
    }

    public float getSalary() {return salary;}

    public float getHelpers() {
        return helpers;
    }

    public float getAutoBusiness() {
        return autobusiness;
    }

    public float getPositiveIncome() {
        return positiveincome;
    }

    public float getOther() {
        return other;
    }

    @Override
    public String toString() {
        return "incomes{" +
            "date=" + date +
            ", total=" + total +
            ", salary=" + salary +
            ", helpers=" + helpers +
            ", autobusiness=" + autobusiness +
            ", positiveincome=" + positiveincome +
            ", other=" + other +
            '}';
    }

    public int compareTo(Incomes Incomes) {
        return -this.date.compareTo(Incomes.date);
    }
}