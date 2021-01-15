package simulator.model;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;

public class Junction extends SimulatedObject{
	
	private List<Road> roadsIn;
	private Map<Junction, Road> roadsOut;
	private List<List<Vehicle>> queueRoad;
	private int currGreen;
	private int lastSwitchingTime;
	private LightSwitchingStrategy switchLight;
	private DequeuingStrategy dequeue;
	@SuppressWarnings("unused")
	private int x;
	@SuppressWarnings("unused")
	private int y;
	

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) throws ExceptionConstructObject {
		super(id);
		if(lsStrategy != null && dqStrategy != null &&
				(xCoor > 0 && yCoor > 0)) {
			this.switchLight = lsStrategy; 
			this.dequeue = dqStrategy;
			this.x = xCoor;
			this.y = yCoor;
			this.roadsIn = new ArrayList<Road>();
			this.roadsOut = new HashMap<Junction, Road>();
			this.queueRoad = new ArrayList<List<Vehicle>>();
			this.currGreen = -1;
			this.lastSwitchingTime = 0;
		}else {
			throw new ExceptionConstructObject("null values in constructor: Junction");
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getCurrentGreen() {
		return this.currGreen;
	}
	
	public List<Road> getRoadIn(){
		List<Road> copy = new ArrayList<Road>();
		for(int i = 0; i < this.roadsIn.size(); i++) {
			copy.add(this.roadsIn.get(i));
		}
		return copy;
	}
	
	public String getQueueRoad(){
		String waitVehicles = "";
		for(int i = 0; i < this.queueRoad.size(); i++) {
			if(this.queueRoad.get(i).size() > 0) {
				waitVehicles = this.roadsIn.get(i).getId() + ":[";
				for(int j = 0; j < this.queueRoad.get(i).size(); j++) {
					waitVehicles += this.queueRoad.get(i).get(j).getId();
					if(j != this.queueRoad.get(i).size() - 1) {
						waitVehicles += ",";
					}
				}
				waitVehicles += "]";
			}
		}
		return waitVehicles;
	}
	
	
	void addIncommingRoad(Road r) throws ExceptionIncorrectValue {
		if(r.getDestination().equals(this)) {
			this.roadsIn.add(r);
			this.queueRoad.add(r.getVehiclesList());
		}else {
			throw new ExceptionIncorrectValue("Incorrect value for junction destination of road");
		}
	}
	
	void addOutGoingRoad(Road r) throws ExceptionIncorrectValue {
		if(!this.roadsOut.containsKey(this) && r.getSource().equals(this)) {
			this.roadsOut.put(this, r);	
		}else {
			throw new ExceptionIncorrectValue("Incorrect value for junction source of road");
		}
		
	}
	
	void enter(Vehicle v) {
		for(int i = 0; i < this.queueRoad.size(); i++) {
			if(this.roadsIn.get(i).getId().equals(v.getRoad().getId())) {
				this.queueRoad.get(i).add(v);
			}
		}
	
	}
	
	Road roadTo(Junction j){
		return this.roadsOut.get(j);
	}
	

	@Override
	void advance(int time) throws ExceptionIncorrectValue{
		List<Vehicle> lv = new ArrayList<Vehicle>();
		if(this.queueRoad.size() > 0) {
			for(int i = 0; i < this.queueRoad.size(); i++) {
				if(this.queueRoad.get(i).size() > 0) {
					lv = this.dequeue.dequeue(this.queueRoad.get(i));
					for(int j=0; j < lv.size();j++) {
						if(lv.get(j).getId().equals(this.queueRoad.get(i).get(j).getId()) && i == this.currGreen) {
							this.queueRoad.get(i).get(j).moveToNextRoad();
							this.queueRoad.get(i).remove(j);			
						}
					}
				}
			}
			
			int aux = this.switchLight.chooseNextGreen(this.roadsIn, this.queueRoad,
					this.currGreen, this.lastSwitchingTime, time);
			if(aux != this.currGreen) {
				this.currGreen = aux;
				this.lastSwitchingTime = time;
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONObject q = new JSONObject();
		JSONArray ja = new JSONArray();
		JSONArray jb = new JSONArray();
		jo.put("id", this._id);
		if(this.currGreen == -1) {
			jo.put("green", "none");
		}else {
			jo.put("green", this.roadsIn.get(this.currGreen).getId());
		}
		for(int i = 0; i < this.queueRoad.size(); i++) {
			q.put("road", this.roadsIn.get(i).getId());
			for(int j = 0; j < this.queueRoad.get(i).size(); j++) {			
				ja.put(this.queueRoad.get(i).get(j));
			}
			q.put("vehicles", ja);
			jb.put(q);
			ja = new JSONArray();
			q = new JSONObject();
		}
		
		jo.put("queues", jb);
		return jo;
	}

}
