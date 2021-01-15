package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	private int time;
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEventBuilder(String type) {
		super(type);
		this.ws = new ArrayList<Pair<String,Weather>>();
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws ExceptionConstructObject, ExceptionJSONConstructor {
		SetWeatherEvent sw = null;
		 Pair<String,Weather> pair;
		 String first;
		 Weather second;
		 Class<Weather> e = Weather.class;
		 
		if (data.has("time")){
			this.time = data.getInt("time");
		}
		if (data.has("info")){
			for(int i = 0 ; i < data.getJSONArray("info").length();i++ ) {
				first = data.getJSONArray("info").getJSONObject(i).getString("road");
				second = data.getJSONArray("info").getJSONObject(i).getEnum(e,"weather");
				pair = new Pair<String, Weather>(first,second);
				this.ws.add(pair);
			}
			
		}
		sw = new SetWeatherEvent(this.time,this.ws);
		return sw;	
	}

}
