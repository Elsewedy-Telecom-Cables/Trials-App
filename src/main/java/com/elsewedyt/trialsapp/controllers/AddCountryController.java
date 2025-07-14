package com.elsewedyt.trialsapp.controllers;

import com.elsewedyt.trialsapp.dao.CountryDAO;
import com.elsewedyt.trialsapp.logging.Logging;
import com.elsewedyt.trialsapp.models.Country;
import com.elsewedyt.trialsapp.models.UserContext;
import com.elsewedyt.trialsapp.services.UserService;
import com.elsewedyt.trialsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

public class AddCountryController implements Initializable {
    @FXML
    private Button add_country_btn;

    @FXML
    private Button clear_country_btn;

    @FXML
    private TableColumn<Country,String> country_delete_colm;

    @FXML
    private TableColumn<Country,String> country_id_colm;

    @FXML
    private TableColumn<Country,String> country_name_colm;

    @FXML
    private TextField country_name_textF;

    @FXML
    private TableView<Country> country_table_view;

    @FXML
    private TextField filter_country_textF;

    @FXML
    private Button update_country_btn;

    @FXML
    private TextField update_country_name_textF;
    ObservableList<Country> countryList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryList = CountryDAO.getAllCountries();
        loadCountriesData();
        setupCountryTableListener();
        country_table_view.setItems(countryList);
        add_country_btn.setCursor(Cursor.HAND);
        update_country_btn.setCursor(Cursor.HAND);
        clear_country_btn.setCursor(Cursor.HAND);


    }
    // Load Countries Data
    private void loadCountriesData() {
        country_name_colm.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        country_id_colm.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        country_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        country_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Country, String>, TableCell<Country, String>> cellFactory = param -> {
            final TableCell<Country, String> cell = new TableCell<Country, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Country"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this country?");
                            alert.setContentText("Delete country confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Country country = country_table_view.getSelectionModel().getSelectedItem();
                                            CountryDAO.deleteCountry(country.getCountryId());
                                            countryList = CountryDAO.getAllCountries();
                                            country_table_view.setItems(countryList);
                                            WindowUtils.ALERT("Success", "Country deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteCountry", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        country_delete_colm.setCellFactory(cellFactory);
        country_table_view.setItems(countryList);

    }

    // Add Country
    @FXML
    void add_country(ActionEvent event) {
        String countryName = country_name_textF.getText().trim();
        if (countryName.isEmpty()) {
            WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Country country = new Country();
        country.setCountryName(countryName);

        boolean success = CountryDAO.insertCountry(country);

        if (success) {
            WindowUtils.ALERT("Success", "Country added successfully", WindowUtils.ALERT_INFORMATION);
            country_name_textF.clear();
            update_country_name_textF.clear();
            filter_country_textF.clear();
            countryList = CountryDAO.getAllCountries();
            country_table_view.setItems(countryList);

        } else {
            WindowUtils.ALERT("database_error", "country_add_failed", WindowUtils.ALERT_ERROR);
        }
    }
    // Update Country
    @FXML
    void update_country(ActionEvent event) {
        try {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry == null) {
                WindowUtils.ALERT("ERR", "No Country selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String countryName = update_country_name_textF.getText().trim();
            if (countryName.isEmpty()) {
                WindowUtils.ALERT("ERR", "country_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedCountry.setCountryName(countryName);
            boolean success = CountryDAO.updateCountry(selectedCountry);
            if (success) {
                WindowUtils.ALERT("Success", "Country updated successfully", WindowUtils.ALERT_INFORMATION);
                update_country_name_textF.clear();
                country_name_textF.clear();
                filter_country_textF.clear();
                countryList = CountryDAO.getAllCountries();
                country_table_view.setItems(countryList);
            } else {
                WindowUtils.ALERT("ERR", "country_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateCountry", ex);
        }
    }

    // Clear Country
    @FXML
    void clear_country(ActionEvent event) {
        filter_country_textF.clear();
        update_country_name_textF.clear();
        country_name_textF.clear();
    }

    // Filter Countries
    @FXML
    void filter_country(KeyEvent event) {
        FilteredList<Country> filteredData = new FilteredList<>(countryList, p -> true);
        filter_country_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(country -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (country.getCountryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = country.getCountryId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Country> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(country_table_view.comparatorProperty());
        country_table_view.setItems(sortedData);
    }

    // Setup Country Table Listener
    private void setupCountryTableListener() {
        country_table_view.setOnMouseClicked(event -> {
            Country selectedCountry = country_table_view.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                update_country_name_textF.setText(selectedCountry.getCountryName());
            }
        });
    }

}
