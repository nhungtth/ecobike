package controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.dock.Dock;
import utils.Configs;
import utils.Utils;

/**
 * this class controls the flow of events in return bike usecase
 * @author NhungTTH
 *
 */
public class ReturnBikeController extends BaseController implements FeesCalculator{

	/**
	 * this method checks if a dock is available now
	 * @param id: id of dock
	 * @return true if dock is available, else false
	 */
	public boolean checkAvailability(String id) {
		Dock dock = Dock.getDockById(id);
		if(dock != null)
			return dock.isStatus();
		else {
			return false;
		}
	}

	/**
	 * this method gets all docks which are available
	 * @return List[Dock]
	 */
	public List getAvailableDocks() {
		List dock =  Dock.getDocksAvailable();
		if(dock == null || dock.size() <= 0 )
			return null;
		return dock;
	}

	/**
	 * this method calculates fees for renting a bike
	 * @param t: time renting bike (minutes)
	 * @return total: amount to pay
	 */
	@Override
	public int calculateFees(long t, String type) {
		int total = 0;
		if (t >= 10) {
			total += 10000;
			if (t > 30) {
				t = t - 30;
				total += (t / 15) * 3000;
			}
		}
		if(type != Configs.STANDARD)
			total = (int) (total*1.5);
		return total;
	}

}
