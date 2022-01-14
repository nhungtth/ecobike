package entity.rentbike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import entity.bike.Bike;
import entity.db.EcobikeDB;
import entity.dock.Dock;
import entity.station.Station;
import utils.Utils;

public class RentBike {
	private static Logger LOGGER = Utils.getLogger(RentBike.class.getName());
	private String id;
	private Timestamp rentDate;
	private Timestamp returnDate;
	private String rentDock;
	private String returnDock;
	private int deposit;
	private String type;

	public RentBike(String id, Timestamp rentDate, String rentDock, int deposit) {
		this.id = id;
		this.rentDate = rentDate;
		this.rentDock = rentDock;
		this.deposit = deposit;
	}

	public RentBike() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getRentDate() {
		return rentDate;
	}

	public void setRentDate(Timestamp rentDate) {
		this.rentDate = rentDate;
	}

	public Timestamp getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}

	public String getRentDock() {
		return rentDock;
	}

	public void setRentDock(String rentDock) {
		this.rentDock = rentDock;
	}

	public String getReturnDock() {
		return returnDock;
	}

	public void setReturnDock(String returnDock) {
		this.returnDock = returnDock;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void saveRentBike(RentBike bike) {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "INSERT INTO bike_rent (bike_id, rent_time, rent_dock, deposit) VALUES ('" + bike.getId() + "', '" + Utils.getToday() + "', '" + bike.getRentDock() + "', " + bike.getDeposit() + ")";
			LOGGER.info(sql);
			PreparedStatement statement = con.prepareStatement(sql);
			int rs = statement.executeUpdate(sql);
			if (rs == 1) {
				LOGGER.info("rent bike " + bike.getId() + " in " + bike.getRentDock());
			}
			statement.close();
			con.close();	
			update(bike, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(RentBike bike, boolean s) {
		try {
			Bike b = new Bike().getBikeById(bike.getId());
			Dock d;
			if(s) {
				d = new Dock().getDockById(bike.getRentDock());
				b.setStatus(false);
				b.setDockId(null);
				d.setStatus(true);
			} else {
				d = new Dock().getDockById(bike.getReturnDock());
				b.setStatus(true);
				b.setDockId(d.getId());
				d.setStatus(false);		
			}
			
			b.updateBikeStatus(b);
			d.updateDockStatus(d);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	// update rent bike in db
	public void updateRentBike(RentBike bike) {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "UPDATE bike_rent SET return_dock = '" + bike.getReturnDock() + "' , return_time = '" + Utils.getToday() + "' WHERE bike_id = '" + bike.getId() + "'";
			PreparedStatement statement = con.prepareStatement(sql);
			int rs = statement.executeUpdate(sql);
			if (rs == 1) {
				LOGGER.info("Return bike " + bike.getId() + "to " + bike.getReturnDock());
			}

			statement.close();
			con.close();
			update(bike, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static RentBike getCurrentBike() {
		RentBike rentBike = new RentBike();
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM bike_rent r INNER JOIN bike b ON r.bike_id = b.bike_id WHERE return_dock IS NULL";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);

			if (rs.next()) {
				rentBike = new RentBike(rs.getString("bike_id"), rs.getTimestamp("rent_time"), rs.getString("rent_dock"),
						rs.getInt("deposit"));
				rentBike.setType(rs.getString("type"));
				LOGGER.info("Current Bike rented: " + rentBike.getId());
			} else {
				LOGGER.info("There is no bike being rented.");
			}
			statement.close();
			con.close();
			return rentBike;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	

}
