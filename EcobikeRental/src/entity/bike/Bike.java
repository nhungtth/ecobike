package entity.bike;

import java.sql.*;

import entity.db.EcobikeDB;
import entity.dock.Dock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import entity.db.EcobikeDB;
import entity.station.Station;

public class Bike {
	private String id;
	private String type;
	private int price;
	private boolean status; // status of bike
	private String dockId;

	public String getId() {
		return id;
	}

	public Bike setId(String id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public Bike setType(String type) {
		this.type = type;
		return this;
	}

	public int getPrice() {
		return price;
	}

	public Bike setPrice(int price) {
		this.price = price;
		return this;
	}

	public boolean isStatus() {
		return status;
	}

	public Bike setStatus(boolean status) {
		this.status = status;
		return this;
	}

	public String getDockId() {
		return dockId;
	}

	public Bike setDockId(String dockId) {
		this.dockId = dockId;
		return this;
	}

	// lay thong tin ve xe trong csdl
	public Bike getBikeById(String id) {
		try {
			Bike bike = new Bike();
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM bike WHERE bike_id = '" + id  + "'";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
			rs.next();

			bike.setId(rs.getString("bike_id"));
			bike.setStatus(rs.getBoolean("status"));
			bike.setType(rs.getString("type"));
			bike.setPrice(rs.getInt("price"));
			bike.setDockId(rs.getString("dock_id"));

			statement.close();
			con.close();

			return bike;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List getAllBike() {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM bike";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet res = statement.executeQuery(sql);
			ArrayList bikes = new ArrayList<>();
			while (res.next()) {
				Bike bike = new Bike().setId(res.getString("bike_id")).setType(res.getString("type"))
						.setDockId(res.getString("dock_id")).setPrice(res.getInt("price"))
						.setStatus(res.getBoolean("status"));
				bikes.add(bike);
			}
			statement.close();
			con.close();
			return bikes;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// lay thong tin xe trong station qua id
	public static List getBikesByStationId(String stationId) throws SQLException {
		Connection con = EcobikeDB.getConnection();
		String sql = "SELECT * FROM bike b INNER JOIN dock d ON b.dock_id = d.dock_id WHERE d.station_id = '" + stationId + "'";
		PreparedStatement stm = con.prepareStatement(sql);
		ResultSet res = stm.executeQuery(sql);
		ArrayList bikes = new ArrayList<>();
		while (res.next()) {
			Bike bike = new Bike().setId(res.getString("bike_id")).setType(res.getString("type"))
					.setDockId(res.getString("dock_id"))

					.setStatus(res.getBoolean("status"));
			bikes.add(bike);
		}
		stm.close();
		con.close();
		return bikes;
	}

	// lay thong tin xe trong station qua id
	public static List<Bike> getBikesAvailableByStationName(String name) {
		List<Bike> listBikes = new ArrayList<>();
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM bike b INNER JOIN dock d ON b.dock_id = d.dock_id JOIN station s ON s.station_id = d.dock_id WHERE b.status = " + true + " AND s.name = '" + name + "'";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				Bike bike = new Bike();
				bike.setId(rs.getString("bike_id"));
				bike.setStatus(true);
				bike.setType(rs.getString("type"));
				bike.setPrice(rs.getInt("price"));
				bike.setDockId(rs.getString("dock_id"));

				listBikes.add(bike);
			}
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listBikes;
	}

	// update bike status
	public void updateBikeStatus(Bike bike) {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "";
			if(bike.getDockId() != null)
				sql = "UPDATE bike SET status = " + bike.isStatus() + ", dock_id = '" + bike.getDockId() +"' WHERE bike_id = '" + bike.getId() + "'";
			else {
				sql = "UPDATE bike SET status = " + bike.isStatus() + ", dock_id = null WHERE bike_id = '" + bike.getId() + "'";
			}

			PreparedStatement statement = con.prepareStatement(sql);
			statement.executeUpdate(sql);
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//get all bike available in all station
	public static List<Bike> getAllBikeAvailable() {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM bike WHERE status = " + true;
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet res = statement.executeQuery(sql);
			ArrayList bikes = new ArrayList<>();
			while (res.next()) {
				Bike bike = new Bike()
						.setId(res.getString("bike_id"))
						.setType(res.getString("type"))
						.setDockId(res.getString("dock_id"))
						.setPrice(res.getInt("price"))
						.setStatus(res.getBoolean("status"));
				bikes.add(bike);
			}
			statement.close();
			con.close();
			return bikes;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
