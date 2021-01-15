package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	public void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl, this), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		JPanel eventsView =
		createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		
		TitledBorder eventB = new TitledBorder("Events");
		LineBorder lineB = new LineBorder(Color.BLACK, 3);
		
		eventB.setBorder(lineB);
		eventsView.setBorder(eventB);
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel vehiclesView =
		createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		
		TitledBorder vehicleB = new TitledBorder("Vehicles");
		
		vehicleB.setBorder(lineB);
		vehiclesView.setBorder(vehicleB);
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
				
		JPanel roadsView =
		createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		
		TitledBorder roadB = new TitledBorder("Roads");
		
		roadB.setBorder(lineB);
		roadsView.setBorder(roadB);
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		
		JPanel junctionsView =
		createViewPanel(new JTable(new JunctionTableModel(_ctrl)), "Junction");
		
		TitledBorder junctionB = new TitledBorder("Junctions");
		
		junctionB.setBorder(lineB);
		junctionsView.setBorder(junctionB);
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		// TODO add other tables
		// ...
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		
		TitledBorder mapB = new TitledBorder("Map");
		
		mapB.setBorder(lineB);
		mapView.setBorder(mapB);
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		JPanel roadView = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Road");
		
		TitledBorder rMapB = new TitledBorder("Road By Map");
		
		rMapB.setBorder(lineB);
		roadView.setBorder(rMapB);
		roadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(roadView);
		
		// TODO add a map for MapByRoadComponent
		// ...
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p.add(new JScrollPane(c));
		return p;
	}


}
