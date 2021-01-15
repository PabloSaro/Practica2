package simulator.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exception.ExceptionJSONConstructor;

public class RoadMap {
	
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private HashMap<String, Junction> crossMap;
	private HashMap<String, Road> roadMap;
	private HashMap<String, Vehicle> vehicleMap;
	
	
	protected RoadMap() {
		this.junctionList = new ArrayList<Junction>();
		this.roadList = new ArrayList<Road>();
		this.vehicleList = new ArrayList<Vehicle>();
		this.crossMap = new HashMap<String, Junction>();
		this.roadMap = new HashMap<String, Road>();
		this.vehicleMap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(this.junctionList.size() > 0) {
			if(this.crossMap.get(j.getId()) == null ) {
				this.junctionList.add(j);
				this.crossMap.put(j.getId(), j);
			}
		}else {
			this.junctionList.add(j);
			this.crossMap.put(j.getId(), j);
		}
	}
	
	void addRoad(Road r) throws ExceptionJSONConstructor {
		if(this.roadList.size() > 0) {				
			if(this.roadMap.get(r.getId()) == null && this.crossMap.get(r.getSource().getId()) != null &&
					this.crossMap.get(r.getDestination().getId()) != null) {
				this.roadList.add(r);
				this.roadMap.put(r.getId(), r);
			}else {
				throw new ExceptionJSONConstructor("duplicate road (id or junction connection)");
			}
		}else {
			this.roadList.add(r);
			this.roadMap.put(r.getId(), r);
		}
	}

	
	private boolean itinerary(Vehicle v) {
		boolean ok = true;
		List<Junction> itinerary = v.getItinerary();
		for(int i = 0; i < itinerary.size()-1; i++) {
			if(itinerary.get(i).roadTo(itinerary.get(i)) == null) {
				ok = false;
			}
		}
		return ok;
	}

	void addVehicle(Vehicle v) throws ExceptionJSONConstructor {
		if(this.vehicleList.size() >= 0) {
			if(this.vehicleMap.get(v.getId()) == null && itinerary(v)) {
				this.vehicleList.add(v);
				this.vehicleMap.put(v.getId(), v);
			}else {
				throw new ExceptionJSONConstructor("duplicate vehicle or invalid itinerary");
			}
			
		}
	}
	
	public Junction getJuntion(String id) {
		Junction j = null;
		for(int i =0 ; i < this.junctionList.size(); i++) {
			if(this.junctionList.get(i).getId().equals(id)) {
				j = this.junctionList.get(i);
			}
		}
		return j;
	}
	
	public Road getRoad(String id) {
		Road r = null;
		for(int i = 0; i < this.roadList.size(); i++) {
			if(this.roadList.get(i).getId().equals(id)) {
				r = this.roadList.get(i);
			}
		}
		return r;
	}
	
	public Vehicle getVehicle(String id) {
		Vehicle v = null;
		for(int i = 0; i < this.vehicleList.size(); i++) {
			if(this.vehicleList.get(i).getId().equals(id)) {
				v = this.vehicleList.get(i);
			}
		}
		return v;
	}
	
	public List<Junction> getJunctions(){
		List<Junction> copy = new ArrayList<Junction>();
		for(int i = 0; i < this.junctionList.size(); i++) {
			copy.add(this.junctionList.get(i));
		}
		return copy;
	}
	
	public List<Road> getRoads(){
		List<Road> copy = new ArrayList<Road>();
		for(int i = 0; i < this.roadList.size(); i++) {
			copy.add(this.roadList.get(i));
		}
		return copy;
	}
	
	public List<Vehicle> getVehicles(){
		List<Vehicle> copy = new ArrayList<Vehicle>();
		for(int i = 0; i < this.vehicleList.size(); i++) {
			copy.add(this.vehicleList.get(i));
		}
		return copy;
	}
	public String[] getVehiclesId() {
		String[] Vehicles = new String[vehicleList.size()];
		for(int i = 0; i < vehicleList.size(); i++) {
			Vehicles[i] = this.vehicleList.get(i).toString();
		}
		return Vehicles;	
	}
	
	public String[] getRoadsId() {
		String[] Roads = new String[roadList.size()];
		for(int i = 0; i < roadList.size(); i++) {
			Roads[i] = this.roadList.get(i).toString();
		}
		return Roads;	
	}
	
	void reset() {
		this.junctionList = new ArrayList<Junction>();
		this.roadList = new ArrayList<Road>();
		this.vehicleList = new ArrayList<Vehicle>();
		this.crossMap = new HashMap<String, Junction>();
		this.roadMap = new HashMap<String, Road>();
		this.vehicleMap = new HashMap<String, Vehicle>();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for(int i = 0; i < this.junctionList.size(); i++) {
			ja.put(this.junctionList.get(i).report());
		}
		jo.put("junctions", ja);
		ja = new JSONArray();
		for(int i = 0; i < this.roadList.size(); i++) {
			ja.put(this.roadList.get(i).report());
		}
		jo.put("roads", ja);
		ja = new JSONArray();
		for(int i = 0; i < this.junctionList.size(); i++) {
			ja.put(this.vehicleList.get(i).report());
		}
		jo.put("vehicles", ja);
		return jo;
	}
}
