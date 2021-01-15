package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws ExceptionConstructObject {
		
		if(sim != null && eventsFactory != null) {
			this.sim = sim;
			this.eventsFactory = eventsFactory;
		}else {
			throw new ExceptionConstructObject("null values in constructor: Controller");
		}
	}
	
	public void loadEvents(InputStream in) throws ExceptionJSONConstructor, JSONException, ExceptionConstructObject {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		Event e = null;
		for(int i = 0; i < jo.getJSONArray("events").length(); i++) {
			e = this.eventsFactory.createInstance(jo.getJSONArray("events").getJSONObject(i));
			if(e != null) {
				sim.addEvent(e);
			}else {
				throw new ExceptionJSONConstructor("JSON does not match");
			}
			
		}
	}
	
	public void run(int n, OutputStream out) {
		JSONArray ja = new JSONArray();
		PrintStream p = new PrintStream(out);
		try {
			while(n != 0) {
				sim.advance();
				ja.put(sim.report());
				n--;
			}
		}catch(Exception e) {
			
		}
		
		p.println("{");
		p.println("  " + '"' + "states" + '"'+ ": [");
		for(int i = 0; i < ja.length()-1; i++) {
			p.println(ja.getJSONObject(i).toString() + ",");
		}
		p.println(ja.getJSONObject(ja.length()-1).toString());
		p.println("]");
		p.println("}");
		
	}
	
	public void run(int n) {
		try {
			while(n != 0) {
				sim.advance();
				n--;
			}
		}catch(Exception e) {
			
		}
	}
	

	
	public void reset() {
		this.sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		this.sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		this.sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		this.sim.addEvent(e);
	}

}


