package simulator.model;

public enum Weather {
	SUNNY, CLOUDY, RAINY, WINDY, STORM;
	
	public String imagenWeather(Weather weather) {
		String wea ="";
		switch(weather)
		{
		case SUNNY: wea = "sun.png";
			break;
		case CLOUDY: wea = "cloud.png";
			break;
		case RAINY: wea = "rain.png";
			break;
		case WINDY: wea = "wind.png";
			break;
		case STORM: wea = "storm.png";
			break;
		}
		return wea;
	}	
}


