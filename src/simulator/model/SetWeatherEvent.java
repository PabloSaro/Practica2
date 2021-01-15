package simulator.model;

import java.util.List;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{

	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws ExceptionConstructObject {
		super(time);
		if(ws != null) {
			this.ws = ws;
		}else {
			throw new ExceptionConstructObject("ws shouldn't be null");
		}
	}

	@Override
	void execute(RoadMap map) throws ExceptionIncorrectValue {
		List <Road> r = map.getRoads();
		for(int i = 0; i < this.ws.size(); i++) {
			for(int j=0; j < r.size();j++) {
				if(this.ws.get(i).getFirst().equals(r.get(j).getId())) {
					if(map.getRoad(ws.get(i).getFirst()) != null) {
						r.get(j).setWeather(ws.get(i).getSecond());
					}else {
						throw new ExceptionIncorrectValue("road for weather doesn't exist");
					}
				}
			}

		}
	}
	
	@Override
	public String toString() {
		String line = "Set Weather Class: [";
		for(int i = 0; i < this.ws.size(); i++) {
			if( i != this.ws.size() -1)
				line += "("+ this.ws.get(i).getFirst() + "," + this.ws.get(i).getSecond()+"), ";
			else
				line += "("+ this.ws.get(i).getFirst() + "," + this.ws.get(i).getSecond()+")]";
		}
		return line;
	}

}
