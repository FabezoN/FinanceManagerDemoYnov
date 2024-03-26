package com.example.financemanager.controller;



import com.example.financemanager.db.IncomesDAO;
import com.example.financemanager.model.Incomes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import org.slf4j.Logger;

import java.util.Optional;

public class IncomesController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(IncomesController.class);


    @FXML
    private TableView<Incomes> incomeTable;

    public void initialize() {incomeTable.setItems(IncomesDAO.getIncomes());}

    public void addIncomes(ActionEvent event) {
        Dialog<Incomes> addPersonDialog = new IncomesDialog();
        Optional<Incomes> result = addPersonDialog.showAndWait();
        result.ifPresent(IncomesDAO::insertIncome);

        log.debug(result.toString());
        event.consume();
    }
}
