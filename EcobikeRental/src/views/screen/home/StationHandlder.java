package views.screen.home;

import entity.station.Station;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import views.screen.FXMLScreenHandler;

import java.io.IOException;
import java.sql.SQLException;

public class StationHandlder  extends FXMLScreenHandler{

    @FXML
    private Label name;

    @FXML
    private Label addr;

    @FXML
    private Label bike;

    @FXML
    private Label dock;
    
    @FXML
    private Button infoBtn;

    private  Station station;
    
    private HomeScreenHandler homeScreenHandler;

    public StationHandlder(String screenPath, Station station, HomeScreenHandler homeScreenHandler) throws IOException {
        super(screenPath);
        this.station= station;
        this.homeScreenHandler = homeScreenHandler;
        name.setText(station.getName());
        addr.setText(station.getAddress());
        bike.setText(String.valueOf(station.getBikeQuantity()));
        dock.setText(String.valueOf(station.getEmptyDocks()));
        infoBtn.setOnMouseClicked(e->{
        	try {
				homeScreenHandler.getInfo(this.station);
			} catch (IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
    }
}
