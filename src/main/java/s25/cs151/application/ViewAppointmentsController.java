package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

public class ViewAppointmentsController {
    private SceneController sceneController;
    private AppointmentDaoInt appointmentDao;

    @FXML private TableView<AppointmentBean> appointmentsTable;
    @FXML private TableColumn<AppointmentBean, String> studentNameColumn;
    @FXML private TableColumn<AppointmentBean, LocalDate> dateColumn;
    @FXML private TableColumn<AppointmentBean, String> timeSlotColumn;
    @FXML private TableColumn<AppointmentBean, String> courseColumn;
    @FXML private TableColumn<AppointmentBean, String> reasonColumn;
    @FXML private TableColumn<AppointmentBean, String> commentColumn;

    @FXML private Button refreshButton;
    @FXML private Button defineAppointmentButton;
    @FXML private Button homeButton;

    @FXML
    public void initialize() {
        appointmentDao = new AppointmentDao();

        // Configure the table columns
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Format the date column
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

        // Load appointments data
        loadAppointments();

        // Configure button actions
        refreshButton.setOnAction(event -> loadAppointments());

        defineAppointmentButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("DefineAppointments.fxml");
            }
        });

        homeButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("Home.fxml");
            }
        });
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    private void loadAppointments() {
        List<AppointmentBean> appointments = appointmentDao.getAppointments();
        
        // Sort appointments by date and time slot
        appointments.sort(new AppointmentComparator());
        
        ObservableList<AppointmentBean> observableAppointments = FXCollections.observableArrayList(appointments);
        appointmentsTable.setItems(observableAppointments);
    }
    
    /**
     * Comparator to sort appointments by date and time slot in ascending order
     */
    private static class AppointmentComparator implements Comparator<AppointmentBean> {
        @Override
        public int compare(AppointmentBean a1, AppointmentBean a2) {
            // First compare by date
            int dateComparison = a1.getDate().compareTo(a2.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }
            
            // If dates are the same, compare by time slot
            // Extract start time from time slot string (e.g. "9:30 AM - 9:45 AM")
            return compareTimeSlots(a1.getTimeSlot(), a2.getTimeSlot());
        }
        
        private int compareTimeSlots(String slot1, String slot2) {
            try {
                // Extract start time from first time slot (format: "HH:MM AM/PM - HH:MM AM/PM")
                String startTime1 = slot1.split(" - ")[0].trim();
                String startTime2 = slot2.split(" - ")[0].trim();
                
                // Parse time for comparison
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                LocalTime time1 = LocalTime.parse(startTime1, formatter);
                LocalTime time2 = LocalTime.parse(startTime2, formatter);
                
                return time1.compareTo(time2);
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                // If parsing fails, fall back to string comparison
                return slot1.compareTo(slot2);
            }
        }
    }
} 