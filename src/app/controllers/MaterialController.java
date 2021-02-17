package app.controllers;

import app.classes.Document;
import app.classes.Subject;
import app.classes.User;
import app.models.DocumentDAO;
import app.models.SubjectDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.print.Doc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.sql.SQLException;

public class MaterialController {
    private final User author;
    private final Subject selectedSubject;
    private final DocumentDAO documentDAO;

    public TableColumn colName;
    public TableColumn colLocation;
    public TableColumn colDate;
    public TableColumn colAuthor;
    public TableView<Document> materialTable;

    public Button btnAddMaterial;

    public MaterialController(User author, Subject selectedSubject, DocumentDAO documentDAO) {
        this.author = author;
        this.selectedSubject = selectedSubject;
        this.documentDAO = documentDAO;
    }

    @FXML
    public void initialize() {
        tableInit();
        materialTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1) {
                if (materialTable.getSelectionModel().getSelectedItem() != null) {
                    documentPicker();
                }
            }
        });
        btnAddMaterial.setOnMouseClicked(mouseEvent -> addMaterial());
        setupControls();
    }

    private void tableInit() {
        colName.setCellValueFactory(new PropertyValueFactory<Document, String>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<Document, String>("path"));
        colDate.setCellValueFactory(new PropertyValueFactory<Document, String>("uploadDate"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Document, User>("author"));
        materialTable.setItems(FXCollections.observableArrayList(documentDAO.fetchDocumentList(selectedSubject)));
    }

    private void addMaterial() {
        try {
            AddMaterialController addMaterialController = new AddMaterialController(author.getId(), selectedSubject.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addMaterial.fxml"));
            loader.setController(addMaterialController);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setOnHiding(windowEvent -> tableInit());
            stage.setTitle("Dodaj novi materijal");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void documentPicker() {
        Document document = (Document) materialTable.getSelectionModel().getSelectedItem();
        if (document.isDownloadable()) {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Izaberite download folder");
            File selectedDirectory = directoryChooser.showDialog(new Stage());

            if (selectedDirectory != null) {
                Path path = Path.of(selectedDirectory.getAbsolutePath() + File.separator + document.getName() + "." + document.getType());
                Thread downloadThread = new Thread(() -> {
                    downloadFile(document, path);
                });
                downloadThread.start();
                openDownloadProgressScreen(downloadThread, path, document.getSize());
            }
        }
    }

    private void downloadFile(Document document, Path path) {
        ReadableByteChannel readChannel = null;
        FileOutputStream fileOS = null;
        FileChannel writeChannel = null;
        try {
            readChannel = Channels.newChannel(new URL(document.getPath()).openStream());
            fileOS = new FileOutputStream(path.toString());
            writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeUsedResources(readChannel, fileOS, writeChannel);
        }
    }

    private void closeUsedResources(ReadableByteChannel readableByteChannel, FileOutputStream fileOutputStream, FileChannel fileChannel) {
        try {
            if (readableByteChannel != null) {
                readableByteChannel.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (fileChannel != null) {
                fileChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDownloadProgressScreen(Thread downloadThread, Path path, int maxSize) {
        try {
            DownloadProgressController downloadProgressController = new DownloadProgressController(downloadThread::interrupt, path, maxSize);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/downloadProgress.fxml"));
            loader.setController(downloadProgressController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Preuzimanje");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
            stage.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupControls() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem details = new MenuItem("Details");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(details, delete);
        details.setOnAction(actionEvent -> {
            if (materialTable.getSelectionModel().getSelectedItem() != null) {
                openDetails(materialTable.getSelectionModel().getSelectedItem());
            }
        });
        delete.setOnAction(actionEvent -> {
            if (materialTable.getSelectionModel().getSelectedItem() != null) {
                deleteDocument(materialTable.getSelectionModel().getSelectedItem());
            }
        });
        materialTable.setContextMenu(contextMenu);
    }

    private void openDetails(Document document) {
        try {
            DetailsMaterialController detailsMaterialController = new DetailsMaterialController(document, documentDAO, SubjectDAO.getInstance());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailsMaterial.fxml"));
            loader.setController(detailsMaterialController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setOnHiding(windowEvent -> tableInit());
            stage.setTitle("Detalji o dokumentu");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDocument(Document document) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Upozorenje o brisanju");
        alert.setContentText("Da li zelite obrisati");
        alert.showAndWait();
        if(alert.getResult().getButtonData().isDefaultButton()){
            documentDAO.delete(document);
            tableInit();
        }
    }
}
