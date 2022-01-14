package controller;

import entity.db.EcobikeDB;
import entity.rentbike.RentBike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import entity.bike.Bike;
import entity.station.Station;
import utils.Configs;

/**
 * this class controls the flow of events in rent bike usecase
 * 
 * @author LanVTN
 *
 */
public class RentBikeController extends BaseController implements DepositCalculator{

	/**
	 * this method get all bikes available in station with name
	 * 
	 * @param name: station name
	 * @return List[Bike]
	 */
	public List<Bike> getBikesAvailableByStationName(String name) {
		List bikes =  Bike.getBikesAvailableByStationName(name);
		if(bikes == null || bikes.size() <= 0 )
			return null;
		return bikes;
	}

	/**
	 * this method check if bike is available now
	 * @param id: id of bike
	 * @return true if bike is available, else false
	 */
	public boolean checkBikeAvalaible(String id) {
		Bike bike = new Bike().getBikeById(id);
		if (bike != null) {
			return bike.isStatus();
		} else {
			return false;
		}
	}

	/**
	 * this method gets all bike available
	 * 
	 * @return List[Bike]
	 */
	public List<Bike> getAllBikeAvailable() {
		List bikes =  Bike.getAllBikeAvailable();
		if(bikes == null || bikes.size() <= 0 )
			return null;
		return bikes;
	}

	/**
	 * this method updates status of bike in DB
	 * 
	 * @param bike
	 */
	public void updateBikeStatus(Bike bike) {
		if(bike != null)
			bike.updateBikeStatus(bike);
	}

	/**
	 * this method save a new bike which be rented
	 * 
	 * @param bike
	 */
	public void saveRentBike(RentBike bike) {
		if(bike != null)
			new RentBike().saveRentBike(bike);
	}
	
	/**
	 * this method calculate deposit when rent a bike
	 * @param id: id of bike user want to rent
	 * @return amount to deposit
	 */
	@Override
	public int calculateDeposit(String id) {
		switch (id) {
		case Configs.TWIN:
			return 550000;
		case Configs.EBIKE:
			return 700000;
		case Configs.STANDARD:
			return 400000;
		default:
			return 0;
		}
	}

}
