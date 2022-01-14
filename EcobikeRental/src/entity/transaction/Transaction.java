package entity.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import entity.db.EcobikeDB;
import entity.rentbike.RentBike;
import entity.user.User;
import utils.Utils;

public class Transaction {
	private static Logger LOGGER = Utils.getLogger(Transaction.class.getName());
	private String id;
	private String type;
	private RentBike bike;
	private int amount;
	private String username;
	private String content;
	private String purpose;

	public Transaction(String type, RentBike bike, int amount, String username, String content) {
		super();
		this.type = type;
		this.bike = bike;
		this.amount = amount;
		this.username = username;
		this.content = content;
	}

	public Transaction(int amount, String content) {
		super();
		this.amount = amount;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RentBike getBike() {
		return bike;
	}

	public void setBike(RentBike bike) {
		this.bike = bike;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// save transaction
	public static void saveTransaction(Transaction transaction) {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "INSERT INTO transaction(transaction_id, content, amount, create_at, purpose, type) VALUES ('"
			+ transaction.getId() + "', '" + transaction.getContent() + "', '" + transaction.getAmount() + "', '" 
					+ Utils.getToday() + "', '" + transaction.getPurpose() 
					+ "', '" + transaction.getType() + "')";
			//LOGGER.info(sql);
			PreparedStatement statement = con.prepareStatement(sql);
			
			int rs = statement.executeUpdate(sql);
 
			if (rs == 1) {
				LOGGER.info("Transaction: amount " + transaction.getAmount() + " type: " + transaction.getType() + " for " + transaction.getPurpose());
			} else {
				LOGGER.info("There is something wrong while save transaction.");
			}
			statement.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
