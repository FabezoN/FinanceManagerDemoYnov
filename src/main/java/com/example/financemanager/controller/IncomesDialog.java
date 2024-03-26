package com.example.financemanager.controller;


import com.example.financemanager.FinanceTrackerApplication;
import com.example.financemanager.model.Incomes;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class IncomesDialog extends Dialog<Incomes> {

    @FXML
    private TextField dateField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField helpersField;

    @FXML
    private TextField autobusinessField;

    @FXML
    private TextField positiveincomeField;

    @FXML
    private TextField otherField;

    @FXML
    private ButtonType createButtonIncome;

    public IncomesDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(FinanceTrackerApplication.class.getResource("income-dialog.fxml"));
            loader.setController(this);

            DialogPane dialogPane = loader.load();

            setTitle("Ajouter des revenues");
            setDialogPane(dialogPane);
            initResultConverter();

            // Disable button when all field are not filled
            computeIfButtonIsDisabled();

            // Ensure only numeric input are set in the fields
            forceDoubleFormat();

            forceDateFormat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void forceDateFormat() {
        UnaryOperator<TextFormatter.Change> dateValidationFormatter = t -> {
            if (t.isAdded()) {
                if (t.getControlText().length() > 8) {
                    t.setText("");
                } else if (t.getControlText().matches(".*[0-9]{2}")) {
                    if (t.getText().matches("[^/]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            }
            return t;
        };
        dateField.setTextFormatter(new TextFormatter<>(dateValidationFormatter));
    }

    private void forceDoubleFormat() {
        UnaryOperator<TextFormatter.Change> numberValidationFormatter = t -> {
            if (t.isReplaced())
                if(t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.]")) {
                    t.setText("");
                }
            }
            return t;
        };
        salaryField.setTextFormatter(new TextFormatter<>(numberValidationFormatter));
        helpersField.setTextFormatter(new TextFormatter<>(numberValidationFormatter));
        autobusinessField.setTextFormatter(new TextFormatter<>(numberValidationFormatter));
        positiveincomeField.setTextFormatter(new TextFormatter<>(numberValidationFormatter));
        otherField.setTextFormatter(new TextFormatter<>(numberValidationFormatter));
    }

    private void computeIfButtonIsDisabled() {
        getDialogPane().lookupButton(createButtonIncome).disableProperty().bind(
            Bindings.createBooleanBinding(() -> dateField.getText().trim().isEmpty(), dateField.textProperty())
                .or(Bindings.createBooleanBinding(() -> salaryField.getText().trim().isEmpty(), salaryField.textProperty())
                    .or(Bindings.createBooleanBinding(() -> helpersField.getText().trim().isEmpty(), helpersField.textProperty())
                        .or(Bindings.createBooleanBinding(() -> autobusinessField.getText().trim().isEmpty(), autobusinessField.textProperty())
                            .or(Bindings.createBooleanBinding(() -> positiveincomeField.getText().trim().isEmpty(), positiveincomeField.textProperty())
                                .or(Bindings.createBooleanBinding(() -> otherField.getText().trim().isEmpty(), otherField.textProperty())
                                ))))
                ));
    }

    private void initResultConverter() {
        setResultConverter(buttonType -> {
            if (!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                return null;
            }

            return new Incomes(
                LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yy")),
                Float.parseFloat(salaryField.getText()),
                Float.parseFloat(helpersField.getText()),
                Float.parseFloat(autobusinessField.getText()),
                Float.parseFloat(positiveincomeField.getText()),
                Float.parseFloat(otherField.getText())
            );
        });
    }

    @FXML
    private void initialize() {

    }
}
