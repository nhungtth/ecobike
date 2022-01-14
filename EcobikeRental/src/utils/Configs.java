package utils;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
/**
 * @author nguyenlm Contains the configs for AIMS Project
 */
public class Configs {

	// api constants
	public static final String GET_BALANCE_URL = "https://ecopark-system-api.herokuapp.com/api/card/balance/118609_group1_2020";
	public static final String GET_VEHICLECODE_URL = "https://ecopark-system-api.herokuapp.com/api/get-vehicle-code/1rjdfasdfas";
	public static final String PROCESS_TRANSACTION_URL = "https://ecopark-system-api.herokuapp.com/api/card/processTransaction";
	public static final String RESET_URL = "https://ecopark-system-api.herokuapp.com/api/card/reset";

	// demo data
	public static final String POST_DATA = "{"
			+ " \"secretKey\": \"BXutHoU0hvg=\" ,"
			+ " \"transaction\": {"
			+ " \"command\": \"pay\" ,"
			+ " \"cardCode\": \"kscq1_group17_2021\" ,"
			+ " \"owner\": \"Group 17\" ,"
			+ " \"cvvCode\": \"436\" ,"
			+ " \"dateExpried\": \"1125\" ,"
			+ " \"transactionContent\": \"Pay for return\" ,"
			+ " \"amount\": 50000 "
			+ "}"
		+ "}";
	public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiIxMTg2MDlfZ3JvdXAxXzIwMjAiLCJpYXQiOjE1OTkxMTk5NDl9.y81pBkM0pVn31YDPFwMGXXkQRKW5RaPIJ5WW5r9OW-Y";

	// database Configs
	public static final String DB_NAME = "ecobike_rental";
	public static final String DB_USERNAME = System.getenv("DB_USERNAME");
	public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

	public static String CURRENCY = "VND";
	public static float PERCENT_VAT = 10;

	// static resource
	public static final String IMAGE_PATH = "assets/images";
	public static final String RESULT_SCREEN_PATH = "/views/fxml/result.fxml";
	public static final String SPLASH_SCREEN_PATH = "/views/fxml/splash.fxml";
	public static final String HOME_PATH  = "/views/fxml/home.fxml";
	public static final String POPUP_PATH = "/views/fxml/popup.fxml";
	public static final String BIKE_INFO_PATH = "/views/fxml/bike_info.fxml";
	public static final String STATION_SEARCH_PATH = "/views/fxml/station_search.fxml";
	public static final String STATION_INFO_PATH = "/views/fxml/station_info.fxml";
	public static final String RENT_BIKE_PATH = "/views/fxml/rent_bike.fxml";
	public static final String RETURN_BIKE_PATH = "/views/fxml/return_bike.fxml";
	public static final String RENT_TRANSACTION_PATH = "/views/fxml/rent_transaction.fxml";
	public static final String RETURN_TRANSACTION_PATH = "/views/fxml/return_transaction.fxml";
	public static final String DOCK_PATH = "/views/fxml/dock.fxml";
	public static final String BIKE_STATION = "/views/fxml/bike_station.fxml";
	public static final String SEARCH_STATION = "/views/fxml/search_station.fxml";
	public static final String STATION_HOME = "/views/fxml/station_home.fxml";

	public static Font REGULAR_FONT = Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.REGULAR, 24);
	
	// type of bike
	public static final String STANDARD = "Standard Bike";
	public static final String EBIKE = "Standard E-bike";
	public static final String TWIN = "Twin Bike";
	
	// purpose of transaction
	public static final String RETURN = "Return";
	public static final String RENT = "Rent";
	
	// type of transaction
	public static final String PAY = "pay";
	public static final String REFUND = "refund";
	public static final String RESET = "reset";
	
	// card info 
	public static final String CARD_NAME = "kscq1_group17_2021";
	public static final String CARD_HOLDER = "Group 17";
	public static final String EXPIRATION_DATE = "11/25";
	public static final String CVV = "436";
}
