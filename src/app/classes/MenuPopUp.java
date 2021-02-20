package app.classes;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MenuPopUp {

    public interface ContextAction{
        void detailsAction();
        void deleteAction();
    }
    public final ContextAction contextAction;

    public MenuPopUp(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem details = new MenuItem("Details");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(details, delete);
        details.setOnAction(actionEvent -> contextAction.detailsAction());
        delete.setOnAction(actionEvent -> contextAction.deleteAction());

        return contextMenu;
    }

}
