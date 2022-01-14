package views.screen.home;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import common.exception.InvalidDeliveryInfoException;
import controller.HomeController;
import controller.RentBikeController;
import controller.ReturnBikeController;
import entity.bike.Bike;
import entity.dock.Dock;
import entity.rentbike.RentBike;
import entity.station.Station;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.rentbike.RentBikeHandler;
import views.screen.returnbike.DockHandler;
import views.screen.returnbike.ReturnBikeHandler;
import views.screen.stationinfo.StationScreenHandler;

public class HomeScreenHandler extends BaseScreenHandler implements Initializable {

	public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

	@FXML
	private TextField search;

	@FXML
	private Button rentBtn;

	@FXML
	private Button returnBtn;

	@FXML
	private GridPane grid;

	@FXML
	private Label bikeLabel;

	private List homeItems;
	private RentBike bike;

	public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		this.homeScreenHandler = this;
		if (this.bike != null && this.bike.getId() != null)
			bikeLabel.setText("Bike: " + bike.getId());
		addListener();
	}

	public HomeController getBController() {
		return (HomeController) super.getBController();
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setBController(new HomeController());
		this.bike = getBController().getRentBike();
		try {
			List stations = getBController().getAllStation();
			this.homeItems = new ArrayList<>();
			for (Object o : stations) {
				Station station = (Station) o;
				StationHandlder handler = new StationHandlder(Configs.STATION_HOME, station, this);
				this.homeItems.add(handler);
			}
		} catch (SQLException | IOException e) {
			LOGGER.info("Errors occured: " + e.getMessage());
			e.printStackTrace();
		}

		addStationToGrid(this.homeItems);

	}

	private void addStationToGrid(List items) {
		ArrayList list = (ArrayList) ((ArrayList) items).clone();
		int c = 0;
		for (Object item : list) {
			StationHandlder sh = (StationHandlder) item;
			grid.addRow(c, sh.getContent());
			c++;
		}
	}

	// set event listener for button
	public void addListener() {
		this.bike = getBController().getRentBike();

		rentBtn.setOnMouseClicked(e -> {
			if (this.bike != null && this.bike.getId() != null) {
				try {
					PopupScreen.error("You must return curent bike before renting one.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				RentBikeHandler rentBikeHandler;
				try {
					rentBikeHandler = new RentBikeHandler(this.stage, Configs.RENT_BIKE_PATH);
					rentBikeHandler.setHomeScreenHandler(this);
					rentBikeHandler.setBController(new RentBikeController());
					rentBikeHandler.setScreenTitle("Rent Bike");
					rentBikeHandler.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		returnBtn.setOnMouseClicked(e -> {
			if (this.bike != null && this.bike.getId() != null) {
				
				try {
					ReturnBikeHandler returnBikeHandler = new ReturnBikeHandler(this.stage, Configs.RETURN_BIKE_PATH);
					returnBikeHandler.setHomeScreenHandler(this);
					returnBikeHandler.setBController(new ReturnBikeController());
					returnBikeHandler.setPreviousScreen(this);
					returnBikeHandler.setScreenTitle("Return Bike");
					returnBikeHandler.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				try {
					PopupScreen.error("No bike to return.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

	}

	public void getInfo(Station station) throws IOException, SQLException {
		StationScreenHandler stationScreen;
		stationScreen = new StationScreenHandler(this.stage, Configs.STATION_INFO_PATH, station);
		stationScreen.setHomeScreenHandler(this);
		stationScreen.setBController(new HomeController());
		stationScreen.setPreviousScreen(this);
		stationScreen.setScreenTitle("Station Screen");
		stationScreen.show();
	}

	@FXML
	void requestToSearch(MouseEvent event) throws IOException, InterruptedException, SQLException {
		String key = search.getText();
		Station station_rs = getBController().getSByStationName(key);
		if (station_rs.getId() == null) {
			PopupScreen.error("No results.");
			return;
		}

		SearchStationScreenHandler searchStationScreen;
		searchStationScreen = new SearchStationScreenHandler(this.stage, Configs.SEARCH_STATION, station_rs, this);
		searchStationScreen.setHomeScreenHandler(this);
		searchStationScreen.setBController(new HomeController());
		searchStationScreen.setPreviousScreen(this);
		searchStationScreen.setScreenTitle("Search Station Screen");
		searchStationScreen.show();

	}
}
