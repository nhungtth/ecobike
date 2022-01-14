package views.screen.stationinfo;

import controller.HomeController;
import controller.TransactionController;
import entity.bike.Bike;
import entity.dock.Dock;
import entity.rentbike.RentBike;
import entity.station.Station;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.returnbike.DockHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class StationScreenHandler extends BaseScreenHandler{
    private static Logger LOGGER = Utils.getLogger(StationScreenHandler.class.getName());

    @FXML
    private Label id;

    @FXML
    private Label name;

    @FXML
    private Label addr;

    @FXML
    private Label bike;

    @FXML
    private Label dock;

    @FXML
    private Label area;

    @FXML
    private AnchorPane pane;
    
    @FXML
    private Label bikeLabel;

    private  Station station;
    private List stationItems;

    public StationScreenHandler(Stage stage, String screenPath, Station station) throws IOException, SQLException {
        super(stage, screenPath);
        this.station = station.getStationById(station.getId());
        try{
            List bikes = Bike.getBikesByStationId(this.station.getId());
            this.stationItems = new ArrayList<>();
            for(Object o: bikes) {
                Bike bike = (Bike) o;
                BikeHandler handler = new BikeHandler(Configs.BIKE_STATION, bike, this);
                this.stationItems.add(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addBikesToGrid(this.stationItems);
        setStationInfo();
    }
    
    private void addBikesToGrid(List items) {
        ArrayList list = (ArrayList)((ArrayList) items).clone();
        GridPane grid = new GridPane();
        grid.setMaxWidth(pane.getMaxWidth());
        int c = 0;
        for(Object item: list) {
            BikeHandler dh = (BikeHandler) item;
            grid.addRow(c, dh.getContent());
            c++;
        }
        pane.getChildren().add(grid);
    }

    private void setStationInfo() {
        id.setText(String.valueOf(station.getId()));
        name.setText(String.valueOf(station.getName()));
        addr.setText(String.valueOf(station.getAddress()));
        area.setText(String.valueOf(station.getArea()));
        bike.setText(String.valueOf(station.getBikeQuantity()));
        dock.setText(String.valueOf(station.getEmptyDocks()));
        RentBike rb = getBController().getRentBike();
		if ( rb != null && rb.getId() != null) {
			bikeLabel.setText(rb.getId());
		}
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }
    
    public void createBikeInfoHandler(Bike bike) throws SQLException, IOException {
		BikeInfoHandler bikeInfoHandler = new BikeInfoHandler(this.stage, Configs.BIKE_INFO_PATH, bike, this.station);
		bikeInfoHandler.setBController(new HomeController());
		bikeInfoHandler.setHomeScreenHandler(homeScreenHandler);
		bikeInfoHandler.show();
	}

    @FXML
	public void goHome(MouseEvent event) {
		homeScreenHandler.show();
	}
}
