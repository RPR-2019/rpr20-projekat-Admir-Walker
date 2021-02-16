package app.controllers;

import app.classes.Document;
import app.classes.Subject;
import app.classes.User;
import app.models.DocumentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public class MaterialScreenController {
    private final User author;
    private final Subject selectedSubject;
    private final DocumentDAO documentDAO;

    public TableColumn colName;
    public TableColumn colLocation;
    public TableColumn colDate;
    public TableColumn colAuthor;
    public TableView materialTable;

    public Button btnAddMaterial;

    public MaterialScreenController(User author, Subject selectedSubject, DocumentDAO documentDAO) {
        this.author = author;
        this.selectedSubject = selectedSubject;
        this.documentDAO = documentDAO;
    }

    @FXML
    public void initialize() {
        tableInit();
        materialTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1) {
                documentPicker();
            }
        });
        btnAddMaterial.setOnMouseClicked(mouseEvent -> addMaterial());
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
            DownloadProgressScreenController downloadProgressScreenController = new DownloadProgressScreenController(downloadThread::interrupt, path, maxSize);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/downloadProgressScreen.fxml"));
            loader.setController(downloadProgressScreenController);
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
}
