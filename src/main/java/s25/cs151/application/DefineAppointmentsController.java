package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DefineAppointmentsController {
    private SceneController sceneController;
    private AppointmentDaoInt appointmentDao;
    private CourseDao courseDao;
    private SemesterTimeSlotsDao timeSlotDao;

    @FXML private TextField studentNameField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TextField reasonField;
    @FXML private TextArea commentArea;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorMessageLabel;

    @FXML
    public void initialize() {
        appointmentDao = new AppointmentDao();
        courseDao = new CourseDao();
        timeSlotDao = new SemesterTimeSlotsDao();

        // Load time slots from DAO
        List<SemesterTimeSlotsBean> timeSlots = timeSlotDao.getTimeSlots();
        List<String> timeSlotStrings = timeSlots.stream()
                .map(slot -> slot.getFromHour() + " - " + slot.getToHour())
                .collect(Collectors.toList());
        timeSlotComboBox.setItems(FXCollections.observableArrayList(timeSlotStrings));
        
        if (!timeSlotStrings.isEmpty()) {
            timeSlotComboBox.setValue(timeSlotStrings.get(0));
        }

        // Load courses from DAO
        List<CourseBean> courses = courseDao.getCourses();
        List<String> courseStrings = courses.stream()
                .map(course -> course.getCourseCode() + "-" + course.getSectionNumber())
                .collect(Collectors.toList());
        courseComboBox.setItems(FXCollections.observableArrayList(courseStrings));
        
        if (!courseStrings.isEmpty()) {
            courseComboBox.setValue(courseStrings.get(0));
        }

        // Set today's date as default
        datePicker.setValue(LocalDate.now());

        // Setup Save Button
        saveButton.setOnAction(event -> saveAppointment());

        // Setup Cancel Button
        cancelButton.setOnAction(event -> {
            if (sceneController != null) {
                sceneController.switchScene("Home.fxml");
            }
        });

        // Clear error message
        errorMessageLabel.setText("");
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

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
            
            // Navigate back to Home or ViewAppointments
            if (sceneController != null) {
                sceneController.switchScene("ViewAppointments.fxml");
            }
        } catch (Exception e) {
            errorMessageLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 