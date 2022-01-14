package controller;

import entity.rentbike.RentBike;
import entity.user.User;
import utils.Configs;

/**
 * This class is the base controller for our EcobikeRental project
 * 
 * @author NhungTTH
 */
public class BaseController {

	/**
	 * this method gets current bike rented
	 * @return RentBike
	 */
	public static RentBike getRentBike() {
		RentBike r = RentBike.getCurrentBike();
		if(r != null)
			return r;
		return null;
	}

	/**
	 * this method get current user logged in
	 * @return User
	 */
	public static User getUser() {
		return User.getCurrentUser();
	}

//	/**
//	 * this method calculate deposit when rent a bike
//	 * @param id: id of bike user want to rent
//	 * @return amount to deposit
//	 */
//	public int calculateDeposit(String id) {
//		switch (id) {
//		case Configs.TWIN:
//			return 550000;
//		case Configs.EBIKE:
//			return 700000;
//		case Configs.STANDARD:
//			return 400000;
//		default:
//			return 0;
//		}
//	}
}
