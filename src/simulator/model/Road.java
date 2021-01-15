package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;

public abstract class Road extends SimulatedObject {
	
	protected Junction source;
	protected Junction destination;
	protected int length;
	protected int maxSpeed;
	protected int currentSpeedLimit;
	protected int contaminationLimit;
	protected Weather weather;
	protected int totalContamination;
	protected List<Vehicle> vehicles;
	
	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) throws ExceptionConstructObject {
			super(id);
			this.vehicles = new ArrayList<Vehicle>();
			if(maxSpeed > 0) {
				this.maxSpeed = maxSpeed;
				this.currentSpeedLimit = this.maxSpeed;
			}
			else {
				throw new ExceptionConstructObject("null values in constructor: Road maxspeed");
			}
			if(contLimit > 0)
				this.contaminationLimit = contLimit;
			else {
				throw new ExceptionConstructObject("null values in constructor: Road contLimit");
			}
			if(length > 0)
				this.length = length;
			else {
				throw new ExceptionConstructObject("null values in constructor: Road length");
			}
			if(weather != null && srcJunc != null && destJunc != null) {
				this.weather = weather;
				this.source = srcJunc;
				this.destination = destJunc;
			}else {
				throw new ExceptionConstructObject("null values in constructor: Road weather, junction source"
						+ "or junction destination");
			}
	}
	
	public void enter(Vehicle v) throws ExceptionIncorrectValue {
		if(v.getLocation() == 0 && v.getCurrentSpeed() == 0) {
			vehicles.add(v);
		}
		else {
			throw new ExceptionIncorrectValue("Incorrect value for vehicle speed or vehicle location");
		}
		
	}
	
	public void exit(Vehicle v) {
		this.vehicles.remove(v);
	}
	
	public void setWeather(Weather w) throws ExceptionIncorrectValue {
		if(w != null)
			this.weather = w;
		else {
			throw new ExceptionIncorrectValue("Incorrect value for weather road");
		}	
	}
	public void addContamination(int c) throws ExceptionIncorrectValue {
		if(c >= 0) {
			this.totalContamination += c;
		}else {
			throw new ExceptionIncorrectValue("Incorrect value for contamination road");
		}
	}
	public void advance(int time) throws ExceptionIncorrectValue {
		int tmp;
		reduceTotalContamination();
		updateSpeedLimit();
		for(int i=0;i < vehicles.size();i++) {
			tmp = calculateVehicleSpeed(vehicles.get(i));
			vehicles.get(i).setSpeed(tmp);
			vehicles.get(i).advance(time);
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		jo.put("id", this._id);
		jo.put("speedlimit", this.currentSpeedLimit);
		jo.put("weather", this.weather);
		jo.put("co2", this.totalContamination);
		for(int i = 0 ; i < this.vehicles.size(); i++)
			ja.put(this.vehicles.get(i).getId());
		jo.put("vehicles", ja);
		return jo;
	}
	public Junction getSource() {
		return this.source;
	}
	
	public int getLenght() {
		return this.length;
	}
	
	public Junction getDestination() {
		return this.destination;
	}
	
	public int getVehiclesListSize() {
		return this.vehicles.size();
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getSpeedLimit() {
		return this.currentSpeedLimit;
	}
	
	public int getTotalContamination() {
		return this.totalContamination;
	}
	
	public int getContaminationLimit() {
		return this.contaminationLimit;
	}
	
	public Weather getWeather() {
		return this.weather;
	}
	
	public List<Vehicle> getVehiclesList(){
		List<Vehicle> copyList = new ArrayList<Vehicle>();
		
		for(int i = 0; i < this.vehicles.size(); i++) {
			copyList.add(this.vehicles.get(i));
		}
		
		return copyList;
	}
	
	public abstract void reduceTotalContamination();
	public abstract void updateSpeedLimit();
	public abstract int calculateVehicleSpeed(Vehicle v);
	

}
