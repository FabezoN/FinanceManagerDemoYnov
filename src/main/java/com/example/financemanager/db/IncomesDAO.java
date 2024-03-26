package com.example.financemanager.db;

import com.example.financemanager.model.Expense;
import com.example.financemanager.model.Incomes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class IncomesDAO {
    private static final Logger log = LoggerFactory.getLogger(IncomesDAO.class);

    private static final String tableName = "incomes";
    private static final String dateColumn = "date";
    private static final String salaryColumn = "salary";
    private static final String helpersColumn = "helpers";
    private static final String autobusinessColumn = "autobusiness";
    private static final String positiveincomeColumn= "positiveincome";

    private static final String otherColumn = "other";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final ObservableList<Incomes> incomes;

    static {
        incomes = FXCollections.observableArrayList();
        fetchAllDataFromDB();
    }

    public static ObservableList<Incomes> getIncomes() {
        return FXCollections.unmodifiableObservableList(incomes.sorted(Incomes::compareTo));
    }

    private static void fetchAllDataFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            incomes.clear();
            while (rs.next()) {
                incomes.add(new Incomes(
                    LocalDate.parse(rs.getString(dateColumn), DATE_FORMAT),
                    rs.getFloat(salaryColumn),
                    rs.getFloat(helpersColumn),
                    rs.getFloat(autobusinessColumn),
                    rs.getFloat(positiveincomeColumn),
                    rs.getFloat(otherColumn)));
            }
        } catch (SQLException e) {
            log.error("Could not load Incomes from database", e);
            incomes.clear();
        }
    }

    public static void insertIncome(Incomes income) {
        //update database
        String query = "INSERT INTO incomes(date, salary, helpers, autobusiness, positiveincome,other) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, income.getDate().format(DATE_FORMAT));
            statement.setFloat(2, income.getSalary());
            statement.setFloat(3, income.getHelpers());
            statement.setFloat(4, income.getAutoBusiness());
            statement.setFloat(5, income.getPositiveIncome());
            statement.setFloat(6, income.getOther());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not insert Incomes in database", e);
        }

        //update cache
        incomes.add(income);
    }

    public static List<Incomes> findLastExpensesEndingAtCurrentMonth(int numberOfLine, LocalDate currentMonth) {
        String query = "SELECT * FROM " + tableName
            + " WHERE " + dateColumn + " <= '" + currentMonth.format(DATE_FORMAT)
            + "' ORDER BY " + dateColumn + " DESC LIMIT " + numberOfLine;

        List<Incomes> lastInCOMES = new ArrayList<>();

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                lastInCOMES.add(new Incomes(
                    LocalDate.parse(rs.getString(dateColumn), DATE_FORMAT),
                    rs.getFloat(salaryColumn),
                    rs.getFloat(helpersColumn),
                    rs.getFloat(autobusinessColumn),
                    rs.getFloat(positiveincomeColumn),
                    rs.getFloat(otherColumn)));
            }
        } catch (SQLException e) {
            log.error("Could not load Incomes from database", e);
        }
        return lastInCOMES;

    }
}
