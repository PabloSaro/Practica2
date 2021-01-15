package simulator.factories;

import org.json.JSONObject;



import simulator.model.Event;

import simulator.model.NewCityRoadEvent;

import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event> {
	
	private int time;
	private String id;
	private String srcJunc;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;
	
	public NewCityRoadEventBuilder(String type) {
		super(type);

	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		NewCityRoadEvent cr = null;
		Class<Weather> e = Weather.class;
		
		if (data.has("time")){
			this.time = data.getInt("time");
		}
		if (data.has("id")){
			this.id = data.getString("id");
		}
		if (data.has("src")){
			this.srcJunc = data.getString("src");
		}
		if (data.has("dest")){
			this.destJunc = data.getString("dest");
		}
		if (data.has("length")){
			this.length = data.getInt("length");
		}
		if (data.has("co2limit")){
			this.co2Limit = data.getInt("co2limit");
		}
		if (data.has("maxspeed")){
			this.maxSpeed = data.getInt("maxspeed");
		}
		if (data.has("weather")){
			this.weather = data.getEnum(e,"weather");
		}
		
		cr = new NewCityRoadEvent(this.time,this.id,this.srcJunc,this.destJunc,this.length,this.co2Limit,this.maxSpeed,this.weather);
		return cr;
	}

}
