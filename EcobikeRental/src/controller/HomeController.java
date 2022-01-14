package controller;

import entity.bike.Bike;
import entity.station.Station;

import java.sql.SQLException;
import java.util.List;


/**
 * This class controls the flow of events in homescreen
 * @author LinhDH
 */
public class HomeController extends BaseController{


    /**
     * this method gets all Station in DB and return back to home to display
     * @return List[Station]
     * @throws SQLException
     */
    public List getAllStation() throws SQLException{
        return new Station().getAllStation();
    }

    /**
     * this method gets all bike in station with stationId
     * @param stationId
     * @return List[Bike]
     * @throws SQLException
     */
    public List getBikesByStationId(String stationId) throws SQLException{
        return Bike.getBikesByStationId(stationId);
    }

    /**
     * this method gets a Station with name
     * @param name
     * @return Station
     * @throws SQLException
     */
    public Station getSByStationName(String name) throws SQLException{
        return Station.getStationByName(name);
    }

}
