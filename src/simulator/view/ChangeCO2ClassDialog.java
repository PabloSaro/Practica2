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
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;


public class ChangeCO2ClassDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Integer[] CO2List = {0 , 1, 2 , 3, 4, 5, 6, 7, 8, 9, 10 };
	
	private Controller _ctrl;
	
	private String idVehicle;
	
	private int CO2;
	
	private int ticks;
	
	private int time;
	
	private JComboBox vehicles;
	
	private JComboBox CO2combobox;
	
	private JSpinner nTicks;
	
	private JLabel des;
	
	private JLabel labelV;
	
	private JLabel labelCO2;
	
	private JLabel labelT;
	
	private JButton ok;
	
	private JButton cancel;
	
	private RoadMap map;
	
	
	public ChangeCO2ClassDialog(Controller _ctrl, int time,RoadMap map) {
		super();
		this.map = map;
		this.time = time;
		this.setTitle("Change CO2 Class");
		this._ctrl = _ctrl;
		initDialog();
	}
	
	public void initDialog() {
		JPanel mainDialog = new JPanel(new BorderLayout());
		this.vehicles = new JComboBox<String>(this.map.getVehiclesId());
		this.CO2combobox = new JComboBox<Integer>(this.CO2List);
		this.nTicks = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		this.des = new JLabel("Schedule an event to change the CO2 class of"
				+ " a vehicle after a given number of simulation ticks from now:");
		this.labelV = new JLabel("Vehicles: ");
		this.labelCO2 = new JLabel("CO2 Class: ");
		this.labelT = new JLabel("Ticks: ");
		this.ok = new JButton("OK");
		this.cancel = new JButton("Cancel");
		
		JPanel north = new JPanel();
		JPanel west = new JPanel();
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		JPanel south = new JPanel();

		north.add(des);
		west.add(labelV);
		west.add(vehicles);
		center.add(labelCO2);
		center.add(CO2combobox);
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
				if(vehicles.getSelectedItem() != null) {
					idVehicle = vehicles.getSelectedItem().toString();
					CO2 = Integer.parseInt(CO2combobox.getSelectedItem().toString());
					ticks = Integer.parseInt(nTicks.getValue().toString());
					List<Pair<String,Integer>> pairList = new ArrayList<Pair<String,Integer>>();
					pairList.add(new Pair<String, Integer>(idVehicle, CO2));

					try {
						_ctrl.addEvent( new NewSetContClassEvent(ticks+time, pairList));
						ChangeCO2ClassDialog.this.dispose();
					} catch (ExceptionConstructObject e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.dispose();
			}

		});
		
		this.add(mainDialog);
		this.setVisible(true);
		
	}
	

}
