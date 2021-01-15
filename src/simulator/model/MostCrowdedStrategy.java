package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot= timeSlot;
	}
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs,
			int currGreen, int lastSwitchingTime, int currTime) {
		int index = currGreen;
		int tam = 0;
		if(roads.size() != 0) {
			if(currGreen == -1) {
				index = 0;
				for(int i =0; i < roads.size();i++) {
					if(tam < qs.get(i).size())
					{
						index = i;
						tam = qs.size();
					}
				}
				}else if(currTime - lastSwitchingTime < timeSlot){
					index = currGreen;
				}else {
					for(int i =(currGreen+1 % roads.size()); i != currGreen;i++) {
						if(i == roads.size()) {
							i=0;
						}
						if(tam < qs.get(i).size())
						{
							index = i;
							tam = qs.size();
						}
					}
				}
			}else {
				index = -1;
			}
		
		return index;
	}

}
