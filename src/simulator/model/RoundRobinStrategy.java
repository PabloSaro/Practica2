package simulator.model;


import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{
	private int timeSlot;
	
	public RoundRobinStrategy(int timeSlot) {
		this.timeSlot= timeSlot;
	}
	
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs,
			int currGreen, int lastSwitchingTime, int currTime) {
		int index;

		if(roads.size() != 0) {
			if(currGreen == -1) {
				index = 0;	
			}else if(currTime - lastSwitchingTime < timeSlot){
				index = currGreen;
			}else {
				index = (currGreen+1) % roads.size();
			}
		}else {
			index = -1;
		}
		return index;
	}
	 

}
