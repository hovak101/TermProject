package s25.cs151.application.CONTROLLER;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import s25.cs151.application.MODEL.CourseBean;
import s25.cs151.application.MODEL.CourseDao;
import s25.cs151.application.MODEL.CourseDaoInt;

public class ViewCoursesController {
    private SceneController sceneController;
    private CourseDaoInt courseDao;
    private ObservableList<CourseBean> courseList;

    @FXML private TableView<CourseBean> coursesTable;
    @FXML private TableColumn<CourseBean, String> courseCodeColumn;
    @FXML private TableColumn<CourseBean, String> courseNameColumn;
    @FXML private TableColumn<CourseBean, Integer> sectionNumberColumn;
    @FXML private Button backButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        // Set up table columns
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        sectionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));

        // Initialize DAO
        courseDao = new CourseDao();

        // Load courses data
        loadCourses();

        // Set up button handler
        backButton.setOnAction(event -> handleBackButton());
        editButton.setOnAction(event -> handleEditButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    private void loadCourses() {
        List<CourseBean> courses = courseDao.getCourses();
        courseList = FXCollections.observableList(courses);
        coursesTable.setItems(courseList);
    }

    @FXML
    private void handleBackButton() {
        sceneController.switchScene("VIEW/Home.fxml");
    }

    @FXML
    private void handleEditButton() {
        CourseBean selected = coursesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        } else {
            showAlert("Action Required", "Please select a course to edit.");
        }
    }

    private void showEditDialog(CourseBean course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/VIEW/EditCourseDialog.fxml"));
            Parent root = loader.load();
            
            EditCourseDialogController dialogController = loader.getController();
            dialogController.setCourse(course);
            dialogController.setParentController(this);

            Scene scene = new Scene(root);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Course");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(coursesTable.getScene().getWindow());
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteButton() {
        CourseBean selected = coursesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Create and style the confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete Course");
            confirmDialog.setContentText("Are you sure you want to delete the following course?\n• " + 
                selected.getCourseCode() + " - " + selected.getCourseName() + " - Section " + selected.getSectionNumber());

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
                    courseList.remove(selected);
                    // Save changes
                    saveCourses();
                    // Show success message
                    showAlert("Success", "Course deleted successfully.");
                }
            });
        } else {
            showAlert("Action Required", "Please select a course to delete.");
        }
    }

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

    public void refreshTable() {
        saveCourses();
        loadCourses();
    }

    private void saveCourses() {
        try {
            // Clear the file and write all courses
            java.io.PrintWriter writer = new java.io.PrintWriter(CourseDao.FILE_NAME);
            for (CourseBean course : courseList) {
                writer.println(course.getCourseCode() + "," + 
                             course.getCourseName() + "," + 
                             course.getSectionNumber());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error saving courses: " + e.getMessage());
        }
    }
} 