package s25.cs151.application.CONTROLLER;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import s25.cs151.application.MODEL.AppointmentBean;
import s25.cs151.application.MODEL.AppointmentDao;
import s25.cs151.application.MODEL.AppointmentDaoInt;

/**
 * Controller for the View Appointments screen.
 * Displays all appointments/office hours in a sorted table format.
 */
public class ViewAppointmentsController {
    private SceneController sceneController;
    private AppointmentDaoInt appointmentDao;
    private ObservableList<AppointmentBean> appointmentsList;
    private FilteredList<AppointmentBean> filteredAppointments;

    // FXML injected table and columns
    @FXML private TableView<AppointmentBean> appointmentsTable;
    @FXML private TableColumn<AppointmentBean, String> studentNameColumn;
    @FXML private TableColumn<AppointmentBean, LocalDate> dateColumn;
    @FXML private TableColumn<AppointmentBean, String> timeSlotColumn;
    @FXML private TableColumn<AppointmentBean, String> courseColumn;
    @FXML private TableColumn<AppointmentBean, String> reasonColumn;
    @FXML private TableColumn<AppointmentBean, String> commentColumn;
    @FXML private TableColumn<AppointmentBean, Void> deleteColumn;
    @FXML private TextField searchField;

    // FXML injected buttons
    @FXML private Button refreshButton;
    @FXML private Button defineAppointmentButton;
    @FXML private Button homeButton;

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML is loaded.
     * Sets up the table columns, loads appointment data, and configures button actions.
     */
    @FXML
    public void initialize() {
        appointmentDao = new AppointmentDao();

        // Configure the table columns - bind to bean properties
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Set up the delete button column
        setupDeleteColumn();

        // Format the date column to display dates in MM/dd/yyyy format
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<AppointmentBean, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Load appointments data into the table
        loadAppointments();

        // Set up search functionality
        setupSearch();

        // Configure button actions
        refreshButton.setOnAction(event -> loadAppointments());

        defineAppointmentButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("VIEW/DefineAppointments.fxml");
            }
        });

        homeButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("VIEW/Home.fxml");
            }
        });
    }

    /**
     * Sets up the delete button column with a custom cell factory.
     */
    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(col -> new TableCell<AppointmentBean, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(deleteButton);

            {
                pane.setAlignment(Pos.CENTER);
                deleteButton.getStyleClass().add("delete-button");
                
                deleteButton.setOnAction(event -> {
                    AppointmentBean appointment = getTableView().getItems().get(getIndex());
                    handleDeleteAppointment(appointment);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /**
     * Handles the deletion of an appointment.
     * Shows a confirmation dialog and deletes the appointment if confirmed.
     * 
     * @param appointment The appointment to delete
     */
    private void handleDeleteAppointment(AppointmentBean appointment) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Appointment");
        confirmDialog.setContentText("Are you sure you want to delete the appointment for " + 
            appointment.getStudentName() + " on " + 
            appointment.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + 
            " at " + appointment.getTimeSlot() + "?");

        // Style the dialog
        try {
            DialogPane dialogPane = confirmDialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/s25/cs151/application/style/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");
            
            // Style the buttons
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.getStyleClass().add("secondary-button");
            
            Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
            cancelButton.getStyleClass().add("main-button");
        } catch (Exception e) {
            // Continue even if styling fails
            e.printStackTrace();
        }

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Remove from the observable list
                appointmentsList.remove(appointment);
                
                // Save the updated list to the database
                appointmentDao.storeAppointments(appointmentsList);
                
                // Show success message
                showAlert("Success", "Appointment deleted successfully.");
            }
        });
    }

    /**
     * Shows an alert dialog with the specified header and message.
     */
    private void showAlert(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        // Style the alert
        try {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/s25/cs151/application/style/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");

            // Style the OK button
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.getStyleClass().add("main-button");
        } catch (Exception e) {
            // Continue even if styling fails
            e.printStackTrace();
        }

        alert.showAndWait();
    }

    /**
     * Sets up the search functionality for filtering appointments by student name.
     */
    private void setupSearch() {
        // Create a filtered list that will be used to filter the appointments
        filteredAppointments = new FilteredList<>(appointmentsList, p -> true);

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointment -> {
                // If search field is empty, show all appointments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert both the search text and student name to lowercase for case-insensitive search
                String lowerCaseFilter = newValue.toLowerCase();
                String studentName = appointment.getStudentName().toLowerCase();

                // Return true if the student name contains the search text
                return studentName.contains(lowerCaseFilter);
            });
        });

        // Create a sorted list that will be used to sort the filtered appointments
        SortedList<AppointmentBean> sortedAppointments = new SortedList<>(filteredAppointments);
        
        // Set the comparator for sorting (descending by date and time slot)
        sortedAppointments.setComparator(new AppointmentComparator());
        
        // Set the sorted and filtered list to the table
        appointmentsTable.setItems(sortedAppointments);
    }

    /**
     * Sets the scene controller for navigation between views.
     * 
     * @param sceneController The SceneController instance
     */
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Loads appointments from the DAO and displays them in the table.
     */
    private void loadAppointments() {
        List<AppointmentBean> appointments = appointmentDao.getAppointments();
        appointmentsList = FXCollections.observableArrayList(appointments);
        
        // Update the filtered list with the new data
        if (filteredAppointments != null) {
            filteredAppointments.setPredicate(p -> true);
        }
    }
    
    /**
     * Custom comparator for sorting appointments first by date, then by time slot.
     * Implements the requirement to sort appointments in descending order by schedule date
     * and time slot.
     */
    private static class AppointmentComparator implements Comparator<AppointmentBean> {
        @Override
        public int compare(AppointmentBean a1, AppointmentBean a2) {
            // First compare by date (descending)
            int dateComparison = a2.getDate().compareTo(a1.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }
            
            // If dates are the same, compare by time slot (descending)
            return compareTimeSlots(a2.getTimeSlot(), a1.getTimeSlot());
        }
        
        /**
         * Helper method to compare time slots by extracting and parsing the start times.
         * 
         * @param slot1 First time slot string (e.g. "9:30 AM - 9:45 AM")
         * @param slot2 Second time slot string
         * @return negative if slot1 is earlier, positive if slot1 is later, 0 if equal
         */
        private int compareTimeSlots(String slot1, String slot2) {
            try {
                // Extract start time from time slot strings (format: "HH:MM AM/PM - HH:MM AM/PM")
                String startTime1 = slot1.split(" - ")[0].trim();
                String startTime2 = slot2.split(" - ")[0].trim();
                
                // Parse times using appropriate formatter for comparison
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                LocalTime time1 = LocalTime.parse(startTime1, formatter);
                LocalTime time2 = LocalTime.parse(startTime2, formatter);
                
                return time2.compareTo(time1); // Note: reversed for descending order
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                // If parsing fails, fall back to string comparison
                return slot2.compareTo(slot1); // Note: reversed for descending order
            }
        }
    }
} 