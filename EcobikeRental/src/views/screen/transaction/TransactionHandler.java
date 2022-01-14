package views.screen.transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import controller.BaseController;
import controller.ReturnBikeController;
import controller.TransactionController;
import entity.dock.Dock;
import entity.rentbike.RentBike;
import entity.transaction.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class TransactionHandler extends BaseScreenHandler{
	@FXML
	private TextField cardNumber;
	
	@FXML
	private TextField holderName;

	@FXML
	private TextField expirationDate;

	@FXML
	private TextField securityCode;
	
	@FXML
	private Text username;
	
	@FXML
	private Text type;
	
	@FXML
	private Text bikeId;
	
	@FXML
	private Text dockId;
	
	@FXML
	private Text station;
	
	@FXML
	private Text deposit;
	
	@FXML
	private Text fees;
	
	@FXML
	private Text time;
	
	@FXML
	private TextField note;
	
	@FXML
	private Button btnConfirm;
	
	@FXML
	private Label bikeLabel;
	
	private Dock dock;
	private RentBike bike;

	// for return transaction
	public TransactionHandler(Stage stage, String screenPath, Dock dock) throws SQLException, IOException {
		super(stage, screenPath);
		this.dock = dock;
		this.bike = BaseController.getRentBike();
		setTransactionInfo();
		setCardInfo();
	}
	
	// for rent transaction
	public TransactionHandler(Stage stage, String screenPath, RentBike bike) throws SQLException, IOException {
		super(stage, screenPath);
		this.bike = bike;
		setTransactionInfo();
		setCardInfo();
	}
	
	public TransactionController getBController() {
		return (TransactionController) super.getBController();
	}

	public void setCardInfo() {
		cardNumber.setText(Configs.CARD_NAME);
		holderName.setText(Configs.CARD_HOLDER);
		expirationDate.setText(Configs.EXPIRATION_DATE);
		securityCode.setText(Configs.CVV);
	}
	
	public void setTransactionInfo() {
		username.setText(BaseController.getUser().getName());
		bikeId.setText(bike.getId());
		bikeLabel.setText("Bike: " + this.bike.getId());
		if(dock != null) {
			dockId.setText(dock.getId());
			station.setText(dock.getStationName());
			deposit.setText(String.valueOf(bike.getDeposit()));
			
			bike.setReturnDock(dock.getId());
			bike.setReturnDate(Utils.getToday());
			
			long t = (bike.getReturnDate().getTime() - bike.getRentDate().getTime())/6000000;
			time.setText(String.valueOf(t));
			
			int f = new ReturnBikeController().calculateFees(t);
			fees.setText(String.valueOf(f));
			type.setText(Configs.RETURN);
  		} else {
  			String dockIdString = bike.getRentDock();
  			Dock d = new Dock().getDockById(dockIdString);
			dockId.setText(dockIdString);
			station.setText(d.getStationName());
			deposit.setText("0");
			fees.setText(String.valueOf(bike.getDeposit()));
			type.setText(Configs.RENT);
		}
		
		btnConfirm.setOnMouseClicked(e -> {
			try {
				confirmToPay();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		});
	}
	
	private void confirmToPay() throws IOException {
		String contents = note.getText();
		TransactionController ctrl = getBController();
		// validate info
		HashMap info = new HashMap<>();
		info.put("note", contents);
		info.put("cardNumber", cardNumber.getText());
		info.put("holderName", holderName.getText());
		info.put("expirationDate", expirationDate.getText());
		info.put("cvv", securityCode.getText());
		if(!ctrl.validateTransactionInfo(info)) {
			PopupScreen.error("Please enter all fields.");
			return;
		}
		
		Map<String, String> response;
		
		response = ctrl.processTransaction(bike.getDeposit(), Integer.valueOf(fees.getText()), type.getText(), contents, cardNumber.getText(), holderName.getText(),
				expirationDate.getText(), securityCode.getText(), Configs.PAY);	
		createTransaction(Configs.PAY, Integer.valueOf(fees.getText()), contents, response);
		displayResult(response.get("RESULT"), response.get("MESSAGE"));
	}

	public void displayResult(String result, String message) throws IOException {
		if(result == "TRANSACTION FAILED!") {
			PopupScreen.error(message);
		} else {
			PopupScreen.success(message);
		}
	}
	
	private void createTransaction(String type, int amount, String contents, Map<String, String> response) {
		if(response.get("RESULT") == "TRANSACTION FAILED!")
			return;
		Transaction transaction = new Transaction(type, bike, amount, username.getText(), contents);
		//Transaction.saveTransaction(transaction);
	}

	@FXML
	public void goHome(MouseEvent event) {
		homeScreenHandler.show();
	}
}
