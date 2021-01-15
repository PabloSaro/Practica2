package simulator.model;

import simulator.exception.ExceptionConstructObject;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
	int contLimit, int length, Weather weather) throws ExceptionConstructObject {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	private int calculateX() {
		int x = 2;
		if(weather.equals(Weather.WINDY) || weather.equals(Weather.STORM)) {
		  x=10;	
		}
		return x;
	}
	
	@Override
	public void reduceTotalContamination() {
		int x = calculateX();
		this.totalContamination = this.totalContamination - x;
		if(this.totalContamination < 0) {
			this.totalContamination = 0;
		}
	}

	@Override
	public void updateSpeedLimit() {
		this.currentSpeedLimit = this.maxSpeed;
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int s =this.currentSpeedLimit;
		int f = v.getContamination();
		return (int) (((11.0 - f)/ 11.0)*s);
	}
	
	
}
