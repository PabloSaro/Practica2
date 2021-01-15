package simulator.model;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.exception.ExceptionJSONConstructor;

public class NewCityRoadEvent extends Event{

	private String id;
	private String srcJunc;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;
	
	
	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, 
			int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) throws  ExceptionIncorrectValue, ExceptionConstructObject, ExceptionJSONConstructor {
		Junction src = map.getJuntion(this.srcJunc);
		Junction dest = map.getJuntion(this.destJunc);
		CityRoad cr = new CityRoad(this.id, src, dest, this.maxSpeed, this.co2Limit, this.length, this.weather);
		map.getJuntion(this.srcJunc).addOutGoingRoad(cr);
		map.getJuntion(this.destJunc).addIncommingRoad(cr);
		map.addRoad(cr);
	}
	
	@Override
	public String toString() {
		return "New City Road '"+this.id+"'";
	}
}
