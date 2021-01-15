package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


import simulator.control.Controller;
import simulator.exception.ExceptionConstructObject;
import simulator.misc.Pair;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog {

private static final long serialVersionUID = 1L;
	
	
	private Controller _ctrl;
	
	private String idRoad;
	
	private Weather weatherR;
	
	private int ticks;
	
	private int time;
	
	private JComboBox roads;
	
	private JComboBox weatherComboBox;
	
	private JSpinner nTicks;
	
	private JLabel des;
	
	private JLabel labelR;
	
	private JLabel labelW;
	
	private JLabel labelT;
	
	private JButton ok;
	
	private JButton cancel;
	
	private RoadMap map;
	
	
	public ChangeWeatherDialog(Controller _ctrl, int time,RoadMap map) {
		super();
		this.map = map;
		this.time = time;
		this.setTitle("Change Road Weather");
		this._ctrl = _ctrl;
		initDialog();
	}
	
	public void initDialog() {
		JPanel mainDialog = new JPanel(new BorderLayout());
		this.roads = new JComboBox<String>(this.map.getRoadsId());
		this.weatherComboBox = new JComboBox<Weather>(Weather.values());
		this.nTicks = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		this.des = new JLabel("Schedule an event to change the Weather of a road"
				+ " after given number of simulation ticks from now:");
		this.labelR = new JLabel("Road: ");
		this.labelW = new JLabel("Weather: ");
		this.labelT = new JLabel("Ticks: ");
		this.ok = new JButton("OK");
		this.cancel = new JButton("Cancel");
		
		JPanel north = new JPanel();
		JPanel west = new JPanel();
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		JPanel south = new JPanel();

		north.add(des);
		west.add(labelR);
		west.add(roads);
		center.add(labelW);
		center.add(weatherComboBox);
		east.add(labelT);
		east.add(nTicks);
		south.add(cancel, BorderLayout.CENTER);
		south.add(ok, BorderLayout.CENTER);
		
		mainDialog.add(north, BorderLayout.NORTH);
		mainDialog.add(west, BorderLayout.WEST);
		mainDialog.add(center, BorderLayout.CENTER);
		mainDialog.add(east, BorderLayout.EAST);
		mainDialog.add(south, BorderLayout.SOUTH);
		
		//listener de los botones y de los Jspinner
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(roads.getSelectedItem() != null) {
					idRoad = roads.getSelectedItem().toString();
					weatherR = (Weather) weatherComboBox.getSelectedItem();
					ticks = Integer.parseInt(nTicks.getValue().toString());
					List<Pair<String,Weather>> pairList = new ArrayList<Pair<String,Weather>>();
					
				
					pairList.add(new Pair<String,Weather>(idRoad, weatherR));
					
					
					try {
						_ctrl.addEvent( new SetWeatherEvent(ticks+time, pairList));
						ChangeWeatherDialog.this.dispose();
					} catch (ExceptionConstructObject e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog.this.dispose();
			}

		});
		
		this.add(mainDialog);
		this.setVisible(true);
		
	}
	

}
