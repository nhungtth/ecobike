package views.screen.returnbike;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import common.exception.StationNotAvailableException;
import controller.ReturnBikeController;
import controller.TransactionController;
import entity.dock.Dock;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.popup.PopupScreen;

public class DockHandler extends FXMLScreenHandler {
	@FXML
	private ImageView image;

	@FXML
	private Label name;

	@FXML
	private Text station;

	@FXML
	private Text address;

	@FXML
	private Button returnButton;

	private static Logger LOGGER = Utils.getLogger(DockHandler.class.getName());
	private Dock dock;
	private ReturnBikeHandler returnBike;

	public DockHandler(String screenPath, Dock dock, ReturnBikeHandler returnBikeHandler)
			throws SQLException, IOException {
		super(screenPath);
		this.dock = dock;
		this.returnBike = returnBikeHandler;
		returnButton.setOnMouseClicked(event -> {
			try {
				ReturnBikeController controller = returnBike.getBController();
				if (controller.checkAvailability(dock.getId())) {
					returnBike.createTransactionHandler(this.dock);
				} else {
					PopupScreen.error("This dock is not available.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		setDockInfo();
	}

	private void setDockInfo() {
		name.setText(dock.getId());
		address.setText(dock.getAddress());
		station.setText(dock.getStationName());
	}

	public Dock getDock() {
		return dock;
	}

	public void setDock(Dock dock) {
		this.dock = dock;
	}
	
}
