package views.screen.stationinfo;

import entity.bike.Bike;
import entity.dock.Dock;
import entity.rentbike.RentBike;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.rentbike.RentBikeHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class BikeHandler extends FXMLScreenHandler {
	@FXML
	private Label name;

	@FXML
	private Label dock;

	@FXML
	private Label type;

	@FXML
	private Label station;

	@FXML
	private Button infoBtn;

	private static Logger LOGGER = Utils.getLogger(BikeHandler.class.getName());
	private Bike bike;
	private StationScreenHandler stationScreenHandler;
	private RentBikeHandler rentBikeHandler;

	public BikeHandler(String screenPath, Bike bike, StationScreenHandler stationScreenHandler) throws IOException {
		super(screenPath);
		this.bike = bike;
		this.stationScreenHandler = stationScreenHandler;
		setInfo();

		infoBtn.setOnMouseClicked(e -> {
			try {
				stationScreenHandler.createBikeInfoHandler(this.bike);
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	public BikeHandler(String screenPath, Bike bike, RentBikeHandler handler) throws IOException {
		super(screenPath);
		this.bike = bike;
		this.rentBikeHandler = handler;

		setInfo();

		infoBtn.setText("RENT");
		infoBtn.setOnMouseClicked(e -> {
			try {
				rentBikeHandler.createTransactionHandler(this.bike);
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	private void setInfo() {
		Dock d = new Dock().getDockById(this.bike.getDockId());
		station.setText(d.getStationName());
		name.setText(bike.getId());
		type.setText(bike.getType());
		dock.setText(bike.getDockId());
	}

	public Bike getBike() {
		return bike;
	}

	public void setBike(Bike bike) {
		this.bike = bike;
	}

	public Label getStation() {
		return station;
	}

	public void setStation(Label station) {
		this.station = station;
	}

}
