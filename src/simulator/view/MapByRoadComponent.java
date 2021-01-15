package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _BLACK_LINE_COLOR = Color.black;

	private RoadMap _map;

	private List<Image> _images ;
	
	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		this.setPreferredSize(new Dimension(300, 200));
	}

	private void initGUI() {
		this._images = new ArrayList<Image>();
		this._images.add(loadImage("cont_0.png"));
		this._images.add(loadImage("cont_1.png"));
		this._images.add(loadImage("cont_2.png"));
		this._images.add(loadImage("cont_3.png"));
		this._images.add(loadImage("cont_4.png"));
		this._images.add(loadImage("cont_5.png"));
		this._car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		int y = 50;
		int aux = 0;
		int c;
		
		int idx;
		Color colorJunction = _RED_LIGHT_COLOR;
		//for (Road r : _map.getRoads()) {
		for(int i=0;i < _map.getRoads().size();i++) {	
			
			int x1 = 50;
			int x2 = getWidth()-100;
			y=(i + 1)*50;
			aux += 50;
			
			g.setColor(_BLACK_LINE_COLOR);
			g.drawString(_map.getRoads().get(i).getId(), x1 - 30 , y);
			g.drawLine(x1, y, x2, y);
			
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 , y -5 , _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(_map.getRoads().get(i).getSource().getId(), x1, y -15);
			
			
			colorJunction = _RED_LIGHT_COLOR;
			idx = _map.getRoads().get(i).getDestination().getCurrentGreen();
			if (idx != -1 && _map.getRoads().get(i).equals(_map.getRoads().get(i).getDestination().getRoadIn().get(idx))) {
				colorJunction = _GREEN_LIGHT_COLOR;
			}
			
			g.setColor(colorJunction);
			g.fillOval(x2 , y -5  , _JRADIUS, _JRADIUS);
			g.setColor(_BLACK_LINE_COLOR);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(_map.getRoads().get(i).getDestination().getId(), x2, y - 15);
			for(int j=0; j < _map.getRoads().get(i).getVehiclesList().size();j++) {
				drawVehicles(g,x1,x2,y,_map.getRoads().get(i).getVehiclesList().get(j));
			}
			g.drawImage(loadImage(_map.getRoads().get(i).getWeather().imagenWeather(_map.getRoads().get(i).getWeather())), (int) x2 + 15, y -15, 32, 32, this);
			
			c =(int) Math.floor(Math.min((double)_map.getRoads().get(i).getTotalContamination()/
					(1.0+(double)_map.getRoads().get(i).getContaminationLimit()),1.0)/0.19);
			g.drawImage(_images.get(c), (int) x2 + 62, y -15, 32, 32, this);
		}
		
		
		//drawJunctions(g);
	}


	private void drawVehicles(Graphics g,int x1, int x2, int y, Vehicle v) {

		if (v.getStatus() != VehicleStatus.ARRIVED) {

			// The calculation below compute the coordinate (vX,vY) of the vehicle on the
			// corresponding road. It is calculated relativly to the length of the road, and
			// the location on the vehicle.
			Road r = v.getRoad();
			double x =  x1 + (int) ((x2 - x1) * ((double)  v.getLocation() / (double) r.getLenght()));
			// Choose a color for the vehcile's label and background, depending on its
			// contamination class
			int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContamination()));
			g.setColor(new Color(0, vLabelColor, 0));
			// draw an image of a car (with circle as background) and it identifier
			g.fillOval((int) (x - 1), y - 6, 14, 14);
			g.drawImage(_car, (int) x, y - 6, 12, 12, this);
			g.drawString(v.getId(), (int) x, y - 6);
			
		}
	}

	private void drawJunctions(Graphics g) {
		for (Junction j : _map.getJunctions()) {

			// (x,y) are the coordinates of the junction
			int x = j.getX();
			int y = j.getY();
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x , y, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(j.getId(), x, y);


		}
	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
