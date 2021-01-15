package simulator.model;

import java.util.List;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws ExceptionConstructObject {
		super(time);
		if(cs != null) {	
			this.cs =cs;
		}else {
			throw new ExceptionConstructObject("cs shouldn't be null");
		}
	}

	@Override
	void execute(RoadMap map) throws ExceptionIncorrectValue {
		List<Vehicle> v = map.getVehicles();
		for(int i=0; i < this.cs.size();i++) {
			for(int j=0;j < v.size();j++) {
				if(this.cs.get(i).getFirst().equals(v.get(j).getId())) {
					if(map.getVehicle(cs.get(i).getFirst()) != null) {
						v.get(j).setContamination(cs.get(i).getSecond());
					}else {
						throw new ExceptionIncorrectValue("vehicle for contamination doesn't exist");
					}
				}
			}	
		}
	}
	
	@Override
	public String toString() {
		String line = "Change CO2 Class: [";
		for(int i = 0; i < this.cs.size(); i++) {
			if( i != this.cs.size() -1)
				line += "("+ this.cs.get(i).getFirst() + "," + this.cs.get(i).getSecond()+"), ";
			else
				line += "("+ this.cs.get(i).getFirst() + "," + this.cs.get(i).getSecond()+")]";
		}
		return line;
	}

}
