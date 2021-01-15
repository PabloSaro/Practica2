package simulator.model;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.exception.ExceptionJSONConstructor;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	private RoadMap roadMap;
	private List<Event> event;
	private int time;
	private List<TrafficSimObserver> observers;
	
	public TrafficSimulator() {
		this.roadMap = new RoadMap();
		this.event = new SortedArrayList<Event>();
		this.time=0;
		this.observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		this.event.add(e);
		notifyOnEventAdded(e);
	}
	public void advance() throws ExceptionIncorrectValue, ExceptionConstructObject, ExceptionJSONConstructor{
		time++;	
		
		notifyOnAdvanceStart(time);
		
		for(int i=0; i < event.size();i++) {
			if(event.get(i).getTime() == this.time) {
				event.get(i).execute(roadMap);
				event.remove(i);
				i--;
			}
		}
		List<Junction> jun = roadMap.getJunctions();
		List<Road> roads = roadMap.getRoads();
		try {
			for(int i=0;i < jun.size();i++) {
				jun.get(i).advance(time);
			}
			for(int i=0;i < roads.size();i++) {
				roads.get(i).advance(time);
			}
			
			notifyOnAdvanceEnd(time);
		}catch(ExceptionIncorrectValue e) {
			notifyOnError(e);
			throw new ExceptionIncorrectValue("Incorrect value for roads or junction");
		}
	}
	
	public void reset() {
		this.roadMap = new RoadMap();
		this.event = new SortedArrayList<Event>();
		this.time=0;
		notifyOnReset();
	}
	
	public JSONObject report() {
        JSONObject jo = new JSONObject();
        JSONObject q = new JSONObject();
        JSONArray ja = new JSONArray();
        jo.put("time", this.time);
        for(int i = 0; i < this.roadMap.getJunctions().size(); i++) {
            ja.put(this.roadMap.getJunctions().get(i).report());
        }
        q.put("junctions", ja);
        ja = new JSONArray();

        for(int i = 0; i < this.roadMap.getRoads().size(); i++) {
            ja.put(this.roadMap.getRoads().get(i).report());
        }
        q.put("roads", ja);
        ja = new JSONArray();

        for(int i = 0; i < this.roadMap.getVehicles().size(); i++) {
            ja.put(this.roadMap.getVehicles().get(i).report());
        }
        q.put("vehicles", ja);
        ja = new JSONArray();

        jo.put("state", q);
        return jo;
    }
	
	public void notifyOnAdvanceStart(int time) {
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onAdvanceStart(this.roadMap, this.event, time);
		}
	}
	
	public void notifyOnEventAdded(Event e) {
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onEventAdded(this.roadMap, this.event, e, time);
		}
	}
	
	public void notifyOnAdvanceEnd(int time) {
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onAdvanceEnd(this.roadMap, this.event, time);
		}
	}
	
	public void notifyOnReset() {
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onReset(this.roadMap, this.event, time);
		}
	}
	
	public void notifyOnError(Exception e) {
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onError(e.getMessage());
		}
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		this.observers.add(o);
		for(int i = 0; i < this.observers.size(); i++) {
			this.observers.get(i).onRegister(this.roadMap, this.event, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		this.observers.remove(o);
	}
	

}
