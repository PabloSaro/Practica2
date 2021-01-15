package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>  {

	private int time;
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	public NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		NewVehicleEvent nv = null;
		if (data.has("time")){
			this.time = data.getInt("time");
		}
		if (data.has("id")){
			this.id = data.getString("id");
		}
		if (data.has("maxspeed")){
			this.maxSpeed = data.getInt("maxspeed");
		}
		if (data.has("class")){
			this.contClass = data.getInt("class");
		}
		if (data.has("itinerary")){
			this.itinerary = new ArrayList<String>();
			for(int i=0;i < data.getJSONArray("itinerary").length();i++) {
				this.itinerary.add(data.getJSONArray("itinerary").getString(i));
			}
			nv = new NewVehicleEvent(this.time,this.id,this.maxSpeed,this.contClass,this.itinerary);
		}	
		return nv;
	}
	

}
