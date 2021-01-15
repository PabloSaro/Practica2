package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {
	public List<Vehicle> dequeue(List<Vehicle> q){
		List<Vehicle> remove = new ArrayList<Vehicle> ();
		for(int i=0; i < q.size();i++) {
			remove.add(q.get(i));
		}
		return remove;
	}
}

