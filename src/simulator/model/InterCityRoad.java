package simulator.model;

import simulator.exception.ExceptionConstructObject;

public class InterCityRoad extends Road {

	
	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
	int contLimit, int length, Weather weather) throws ExceptionConstructObject {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	private int calculateX() {
		switch(this.weather.name()) {
		case "SUNNY": return 2;
		case "CLOUDY": return 3;
		case "RAINY": return 10;
		case "WINDY": return 15;
		case "STORM": return 20;
		}
		return 0;
	}
	
	@Override
	public void reduceTotalContamination() {
		int x = calculateX();
		this.totalContamination = (int) (((100.0-x)/100.0)*this.totalContamination);
	}

	@Override
	public void updateSpeedLimit() {
		if(this.totalContamination > this.contaminationLimit) {
			this.currentSpeedLimit = (int) (this.maxSpeed*0.5);		
		}else {
			this.currentSpeedLimit = this.maxSpeed;
		}	
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int speed = 0;
		if(this.weather.equals(Weather.STORM))
			speed = (int) (this.currentSpeedLimit * 0.8);
		else
			speed = this.currentSpeedLimit;
			
		//v.setSpeed(speed);
		return speed;
	}
	
	
	
	
	
	

}
