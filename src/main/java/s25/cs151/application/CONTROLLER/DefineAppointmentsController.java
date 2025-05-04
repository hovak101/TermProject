package s25.cs151.application.CONTROLLER;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import s25.cs151.application.MODEL.AppointmentDao;
import s25.cs151.application.MODEL.AppointmentDaoInt;
import s25.cs151.application.MODEL.CourseBean;
import s25.cs151.application.MODEL.CourseDao;
import s25.cs151.application.MODEL.SemesterTimeSlotsBean;
import s25.cs151.application.MODEL.SemesterTimeSlotsDao;

/**
 * Controller for the Define Appointments screen.
 * Handles the form for faculty to enter office hours schedule/appointments.
 */
public class DefineAppointmentsController {
    private SceneController sceneController;
    private AppointmentDaoInt appointmentDao;
    private CourseDao courseDao;
    private SemesterTimeSlotsDao timeSlotDao;

    // FXML injected UI components
    @FXML private TextField studentNameField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TextField reasonField;
    @FXML private TextArea commentArea;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorMessageLabel;

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML is loaded.
     * Sets up the form with time slots and courses, and configures button actions.
     */
    @FXML
    public void initialize() {
        appointmentDao = new AppointmentDao();
        courseDao = new CourseDao();
        timeSlotDao = new SemesterTimeSlotsDao();

        // Load time slots from DAO for dropdown
        List<SemesterTimeSlotsBean> timeSlots = timeSlotDao.getTimeSlots();
        List<String> timeSlotStrings = timeSlots.stream()
                .map(slot -> slot.getFromHour() + " - " + slot.getToHour())
                .collect(Collectors.toList());
        timeSlotComboBox.setItems(FXCollections.observableArrayList(timeSlotStrings));
        
        // Set default value for time slot dropdown
        if (!timeSlotStrings.isEmpty()) {
            timeSlotComboBox.setValue(timeSlotStrings.get(0));
        }

        // Load courses from DAO for dropdown
        List<CourseBean> courses = courseDao.getCourses();
        List<String> courseStrings = courses.stream()
                .map(course -> course.getCourseCode() + "-" + course.getSectionNumber())
                .collect(Collectors.toList());
        courseComboBox.setItems(FXCollections.observableArrayList(courseStrings));
        
        // Set default value for course dropdown
        if (!courseStrings.isEmpty()) {
            courseComboBox.setValue(courseStrings.get(0));
        }

        // Set today's date as default
        datePicker.setValue(LocalDate.now());

        // Setup Save Button
        saveButton.setOnAction(event -> saveAppointment());

        // Setup Cancel Button to return to Home
        cancelButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("VIEW/Home.fxml");
            }
        });

        // Clear error message
        errorMessageLabel.setText("");
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
     * Handles the save button action.
     * Validates the form input and stores the appointment.
     */
    private void saveAppointment() {
        // Reset error message
        errorMessageLabel.setText("");

        // Validate required fields
        if (studentNameField.getText().trim().isEmpty()) {
            errorMessageLabel.setText("Error: Student Name is required");
            return;
        }

        if (datePicker.getValue() == null) {
            errorMessageLabel.setText("Error: Date is required");
            return;
        }

        if (timeSlotComboBox.getValue() == null) {
            errorMessageLabel.setText("Error: Time Slot is required");
            return;
        }

        if (courseComboBox.getValue() == null) {
            errorMessageLabel.setText("Error: Course is required");
            return;
        }

        // Get values from form
        String studentName = studentNameField.getText().trim();
        LocalDate date = datePicker.getValue();
        String timeSlot = timeSlotComboBox.getValue();
        String course = courseComboBox.getValue();
        String reason = reasonField.getText() == null ? "" : reasonField.getText().trim();
        String comment = commentArea.getText() == null ? "" : commentArea.getText().trim();

        try {
            // Store the appointment
            appointmentDao.storeAppointment(studentName, date, timeSlot, course, reason, comment);
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Appointment successfully saved!");
            alert.showAndWait();
            
            // Navigate to view appointments
            if (sceneController != null) {
                sceneController.switchScene("VIEW/ViewAppointments.fxml");
            }
        } catch (Exception e) {
            errorMessageLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 