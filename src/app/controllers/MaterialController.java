package app.controllers;

import app.classes.*;
import app.models.AddMaterialModel;
import app.models.DocumentDAO;
import app.models.MaterialModel;
import app.models.SubjectDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.sql.SQLException;

public class MaterialController implements MenuPopUp.ContextAction {

    public TextField searchField;
    public ListView<Document> documentList;
    public Button btnAddMaterial;

    private final MaterialModel materialModel;

    public MaterialController(MaterialModel materialModel) {
        this.materialModel = materialModel;
    }

    @FXML
    public void initialize() {
        tableInit();
        if(materialModel.getUserType().equals(UserType.PROFESSOR)){
            setupControls();
            btnAddMaterial.setOnMouseClicked(mouseEvent -> addMaterial());
        }
        documentList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1) {
                if (documentList.getSelectionModel().getSelectedItem() != null) {
                    documentPicker();
                }
            }
        });
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                documentList.setItems(materialModel.search(t1));
            }
        });
    }

    private void tableInit() {
        documentList.setItems(materialModel.fetchDocuments());
    }

    private void addMaterial() {
        try {
            AddMaterialModel addMaterialModel = new AddMaterialModel(materialModel.getAuthorID(), materialModel.getSelectedSubjectID());
            AddMaterialController addMaterialController = new AddMaterialController(addMaterialModel);
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
        Document document = documentList.getSelectionModel().getSelectedItem();
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
        MenuPopUp menuPopUp = new MenuPopUp(this);
        ContextMenu contextMenu = menuPopUp.createContextMenu();
        documentList.setContextMenu(contextMenu);
    }

    @Override
    public void detailsAction() {
        if (documentList.getSelectionModel().getSelectedItem() != null) {
            openDetails(documentList.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void deleteAction() {
        if (documentList.getSelectionModel().getSelectedItem() != null) {
            deleteDocument(documentList.getSelectionModel().getSelectedItem());
        }
    }
    private void openDetails(Document document) {
        try {
            DetailsMaterialController detailsMaterialController = new DetailsMaterialController(document, DocumentDAO.getInstance(), SubjectDAO.getInstance());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailsMaterial.fxml"));
            loader.setController(detailsMaterialController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setOnHiding(windowEvent -> {
                tableInit();
            });
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
        if (alert.getResult().getButtonData().isDefaultButton()) {
            materialModel.delete(document);
            tableInit();
        }
    }
}
