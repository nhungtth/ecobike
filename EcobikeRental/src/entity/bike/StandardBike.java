package entity.bike;

public class StandardBike extends Bike{
	private String id;
	private int timeLimit;
	private int battery;
	
	public String getId() {
		return id;
	}
	public Bike setId(String id) {
		this.id = id;
		return this;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	
	
}
