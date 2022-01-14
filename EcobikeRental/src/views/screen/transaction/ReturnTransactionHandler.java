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
import entity.bike.Bike;
import entity.dock.Dock;
import entity.rentbike.RentBike;
import entity.transaction.CreditCard;
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

public class ReturnTransactionHandler extends BaseScreenHandler {
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
	public ReturnTransactionHandler(Stage stage, String screenPath, RentBike bike, int fees, long time) throws SQLException, IOException {
		super(stage, screenPath);
		this.bike = bike;
		this.dock = new Dock().getDockById(this.bike.getReturnDock());
		setTransactionInfo(fees, time);
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

	public void setTransactionInfo(int fee, long t) {
		username.setText(BaseController.getUser().getName());
		bikeId.setText(bike.getId());
		bikeLabel.setText("Bike: " + this.bike.getId());
		dockId.setText(dock.getId());
		station.setText(dock.getStationName());
		deposit.setText(String.valueOf(bike.getDeposit()));

		time.setText(String.valueOf(t));
		fees.setText(String.valueOf(fee));
		type.setText(Configs.RETURN);

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
		if (!ctrl.validateTransactionInfo(info)) {
			PopupScreen.error("Please enter all fields.");
			return;
		}

		Map<String, String> response;
		CreditCard card = new CreditCard(cardNumber.getText(), holderName.getText(), Integer.parseInt(securityCode.getText()), expirationDate.getText());
		response = ctrl.returnTransaction(bike.getDeposit(), Integer.valueOf(fees.getText()), contents, card);

		//createTransaction(Configs.PAY, Integer.valueOf(fees.getText()), contents, response);
		displayResult(response.get("RESULT"), response.get("MESSAGE"));
	}

	public void displayResult(String result, String message) throws IOException {
		if (result == "TRANSACTION FAILED!") {
			PopupScreen.error(message, this);
			bike.setReturnDate(null);
			bike.setReturnDock(null);
		} else {
			PopupScreen.success(message, this);
			new RentBike().updateRentBike(bike);
		}
	}

	private void createTransaction(String type, int amount, String contents, Map<String, String> response) {
		if (response.get("RESULT") == "TRANSACTION FAILED!")
			return;
		Transaction transaction = new Transaction(type, bike, amount, username.getText(), contents);
		transaction.setPurpose(Configs.RETURN);
		Transaction.saveTransaction(transaction);
	}

	@FXML
	public void goHome(MouseEvent event) {
		homeScreenHandler.show();
	}
}
