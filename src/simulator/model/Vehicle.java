package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;

public class Vehicle extends SimulatedObject {
	
	
	private List<Junction> itinerary;
	private int maxSpeed;
	private int currentSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contamination;
	private int totalContamination;
	private int totalTravDistance;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws ExceptionConstructObject {
		super(id);
		if(maxSpeed >= 0 && contClass >= 0 && 
				contClass <= 10 && itinerary.size() >= 2) {
			this.maxSpeed = maxSpeed;
			this.contamination = contClass;
			this.itinerary = itinerary;
			this.status = VehicleStatus.PENDING;
			this.road = null;
		}else {
			throw new ExceptionConstructObject("null values in constructor: Vehicle");
		}
	}
	
	public List<Junction> getItinerary(){
		List<Junction> copy = new ArrayList<Junction>();
		for(int i = 0; i < this.itinerary.size(); i++) {
			copy.add(this.itinerary.get(i));
		}
		return copy;
	}
	
	public Road getRoad() {
		return this.road;
	}
	
	public int getLocation() {
		return this.location;
	}
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	public int getCurrentSpeed() {
		return this.currentSpeed;
	}
	
	public int getContamination() {
		return this.contamination;
	}
	public int getTotalContamination() {
		return this.totalContamination;
	}
	public int getDistance() {
		return this.totalTravDistance;
	}
	
	public VehicleStatus getStatus() {
		return this.status;
	}
	
	public void setSpeed(int s) throws ExceptionIncorrectValue {
		if(this.status.equals(VehicleStatus.TRAVELING)) {	
			if(s < 0) {
				throw new ExceptionIncorrectValue("Negative value for speed");
			}else if(this.maxSpeed > s) {
				this.currentSpeed = s;
			}else {
				this.currentSpeed = this.maxSpeed;
			}
		}else {
			this.currentSpeed = 0; 
		}
		
			
	}
	
	public void setContamination(int c) throws ExceptionIncorrectValue {
		if(c < 0 || c >10 ) {
			throw new ExceptionIncorrectValue("Invalid value for contamination");
		}else {
			this.contamination = c;
		}
	}

	@Override
	void advance(int time) throws ExceptionIncorrectValue {
		int pos = this.location;
		int tmp = 0;
		if(status.equals(VehicleStatus.TRAVELING)) {
			if(this.location + this.currentSpeed < this.road.getLenght()) {
				this.location = this.location + this.currentSpeed;
				this.totalTravDistance += this.currentSpeed;
			}
			else {
				this.totalTravDistance += ( this.road.getLenght() - this.location);
				this.location  = this.road.getLenght(); 
			}
			
			tmp += this.contamination * (this.location - pos);
			this.road.addContamination(tmp);
			this.totalContamination += tmp;
			
			if(this.location == this.road.getLenght()) {
					this.status = VehicleStatus.WAITING;
					this.road.getDestination().enter(this);
				this.currentSpeed=0;
			}
		}
	}
	
	public void moveToNextRoad() throws ExceptionIncorrectValue {
		if(this.status.equals(VehicleStatus.PENDING) || this.status.equals(VehicleStatus.WAITING)){
			this.location = 0;
			this.currentSpeed = 0;
			if(this.status.equals(VehicleStatus.WAITING)) {
				if(this.road.getDestination().equals(this.itinerary.get(this.itinerary.size()-1))) {
					this.status = VehicleStatus.ARRIVED;
					this.road.exit(this);
					this.road=null;
				}else{
					this.road.exit(this);
					this.road = this.road.getDestination().roadTo(this.road.getDestination());
					this.road.enter(this);
					this.status = VehicleStatus.TRAVELING;
				}
			}else{
				this.road = this.itinerary.get(0).roadTo(this.itinerary.get(0));
				this.road.enter(this);
				this.status = VehicleStatus.TRAVELING;
			}
		}else {
			throw new ExceptionIncorrectValue("Incorrect Status for vwhicle");
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", this._id);
		jo.put("speed", this.currentSpeed);
		jo.put("distance", this.totalTravDistance);
		jo.put("co2", this.totalContamination);
		jo.put("class", this.contamination);
		jo.put("status", this.status);
		if(this.road != null) {
			jo.put("road", this.road.getId());
			jo.put("location", this.location);
		}
		return jo;
	}

}
