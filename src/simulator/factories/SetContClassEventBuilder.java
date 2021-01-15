package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;


public class SetContClassEventBuilder extends Builder<Event> {
	private int time;
	private List<Pair<String,Integer>> cs;

	public SetContClassEventBuilder(String type) {
		super(type);
		this.cs = new ArrayList<Pair<String,Integer>>();
	}

	@Override
	protected Event createTheInstance(JSONObject data)throws ExceptionConstructObject, ExceptionJSONConstructor {
		// TODO Auto-generated method stub
		NewSetContClassEvent sw=null;
		 String first;
		 Integer second;
		 Pair<String, Integer>pair;
		
		if (data.has("time")){
			this.time = data.getInt("time");
		}
		if (data.has("info")){
			for(int i = 0 ; i < data.getJSONArray("info").length();i++ ) {
				first = data.getJSONArray("info").getJSONObject(i).getString("vehicle");
				second = data.getJSONArray("info").getJSONObject(i).getInt("class");
				pair = new Pair<String, Integer>(first,second);
				this.cs.add(pair);
			}
			
		}
		sw= new NewSetContClassEvent(this.time,this.cs);
		return sw;
	}

}
