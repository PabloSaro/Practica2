package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.exception.ExceptionJSONConstructor;

public class NewVehicleEvent extends Event{

	
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass,
			List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) throws ExceptionIncorrectValue, ExceptionConstructObject, ExceptionJSONConstructor {
		List<Junction> path = new ArrayList<Junction>();
		for(int i=0;i < this.itinerary.size();i++) {
			path.add(map.getJuntion(this.itinerary.get(i))); 
		}
		Vehicle v = new Vehicle(this.id, this.maxSpeed, this.contClass, path);
		map.addVehicle(v);
		v.moveToNextRoad();		
	}

	@Override
	public String toString() {
		return "New Vehicle '"+this.id+"'";
	}
}
