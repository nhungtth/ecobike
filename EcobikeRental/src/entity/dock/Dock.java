package entity.dock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import entity.db.EcobikeDB;
import entity.station.Station;
import utils.Utils;

public class Dock {
	private static Logger LOGGER = Utils.getLogger(Dock.class.getName());
	private String id;
	private boolean status; // status of dock
	private String stationId; // station of dock point
	private String stationName;
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// get available docks
	public static List<Dock> getDocksAvailable() {
		List<Dock> listDock = new ArrayList<>();
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM dock d INNER JOIN station s ON d.station_id = s.station_id WHERE d.status = " + true;
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery(sql);

			while (rs.next()) {
				Dock dock = new Dock();
				dock.setId(rs.getString("dock_id"));
				dock.setStatus(true);
				dock.setStationId(rs.getString("station_id"));
				dock.setAddress(rs.getString("address"));
				dock.setStationName(rs.getString("name"));

				listDock.add(dock);
			}
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listDock;
	}

	public void updateDockStatus(Dock dock) {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "UPDATE dock SET status = " + dock.isStatus() + " WHERE dock_id = '" + dock.getId() + "'";
			PreparedStatement statement = con.prepareStatement(sql);
			int rs = statement.executeUpdate(sql);
			if(rs == 1) {
				LOGGER.info("Update dock " + dock.getId() + "to " + dock.isStatus());
			}
			statement.close();
			
			// update station of dock
			Station st = new Station().getStationById(dock.getStationId());
			if(dock.isStatus()) {
				st.setBikeQuantity(st.getBikeQuantity() - 1);
				st.setEmptyDocks(st.getEmptyDocks() + 1);		
			} else {
				st.setBikeQuantity(st.getBikeQuantity() + 1);
				st.setEmptyDocks(st.getEmptyDocks() - 1);	
			}
			
			st.updateStation(st);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Dock getDockById(String id) {
		Dock dock = new Dock();
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM dock d INNER JOIN station s ON d.station_id = s.station_id WHERE d.dock_id = '" + id + "'";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				dock.setId(id);
				dock.setStatus(rs.getBoolean("status"));
				dock.setStationId(rs.getString("station_id"));
				dock.setAddress(rs.getString("address"));
				dock.setStationName(rs.getString("name"));
			}
			
			statement.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dock;
	}
}
