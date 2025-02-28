package simulator.model;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.exception.ExceptionJSONConstructor;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		// TODO complete
		return 0;
	}

	public abstract String toString();
	
	abstract void execute(RoadMap map) throws ExceptionIncorrectValue, ExceptionConstructObject, ExceptionJSONConstructor ;
}
