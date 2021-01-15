package simulator.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	
	private RoadMap map;
	
	//Un atributo por boton todos los botones en una Jtoolbar
	//un roadmap y el nde ticks
	
	private JButton selectB;
	
	private JButton setCO2B;
	
	private JButton setWeatherB;
	
	private JButton exitB;
	
	private JButton runB;
	
	private JButton stopB;
	
	private JToolBar bar;
	
	private JFileChooser chooser;
	
	private int time;
	
	private int ticks;
	
	private JSpinner nTicks;
	
	private boolean _stopped;
	
	private MainWindow mw;
	
	public ControlPanel(Controller _ctrl, MainWindow mw) {
		initPanel();
		this.mw = mw;
		this.time = 0;
		this.map = null;
		this._ctrl = _ctrl;
		this._ctrl.addObserver(this);
		this._stopped = false;
	}
	
	public void initPanel(){
		this.bar = new JToolBar();
		
		this.nTicks = new JSpinner(new SpinnerNumberModel(1, 1, 500, 5));
		this.nTicks.setVisible(true);
		this.nTicks.setPreferredSize(new Dimension(80,40));
		
		selectFile();
		this.bar.add(this.selectB);
		
		this.bar.addSeparator(new Dimension(15, 10));
		
		
		setContamination();
		setWeather();
		this.bar.add(this.setCO2B);
		this.bar.add(this.setWeatherB);
		this.bar.addSeparator(new Dimension(15, 10));

		
		runButton();
		stopButton();
		this.bar.add(this.runB);
		this.bar.add(this.stopB);
		this.bar.add(this.nTicks);
		
		this.bar.add(Box.createGlue());
		
		this.bar.addSeparator(new Dimension(700, 40));
		
		exitButton();
		this.bar.add(this.exitB);
		
		this.bar.setVisible(true);
		this.add(bar);
		this.bar.setFloatable(false);
		this.setVisible(true);
	}
	
	
	public void selectFile() {
		this.selectB = new JButton();
		this.selectB.setIcon(new ImageIcon("resources/icons/open.png"));
		this.selectB.setToolTipText("select a JSON file");
		this.selectB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				try {
					int val = chooser.showOpenDialog(mw);
					_ctrl.reset();
					if(JFileChooser.APPROVE_OPTION == val) {
						_ctrl.loadEvents(new FileInputStream(chooser.getSelectedFile()));
					}
				}catch(FileNotFoundException | ExceptionJSONConstructor | JSONException | ExceptionConstructObject ex) {
					
					JOptionPane.showMessageDialog(null,"An Error has occurred, you must choose a file .json", "Error",
							JOptionPane.ERROR_MESSAGE, null);
				} 
			}

		});
		this.selectB.setVisible(true);
	}
	
	
	public void setContamination() {
		this.setCO2B = new JButton();
		this.setCO2B.setIcon(new ImageIcon("resources/icons/co2class.png"));
		this.setCO2B.setToolTipText("change Contamination for a vehicle");
		this.setCO2B.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog CDialog = new ChangeCO2ClassDialog(_ctrl, time, map);
				CDialog.setSize(new Dimension(700, 200));
				CDialog.setLocationRelativeTo(mw);
				CDialog.setVisible(true);
			}
		});
		this.setCO2B.setVisible(true);
	}
	
	public void setWeather() {
		this.setWeatherB = new JButton();
		this.setWeatherB.setIcon(new ImageIcon("resources/icons/weather.png"));
		this.setWeatherB.setToolTipText("change Weather for a road");
		this.setWeatherB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				ChangeWeatherDialog WDialog = new ChangeWeatherDialog(_ctrl, time,map);
				WDialog.setSize(new Dimension(700, 200));
				WDialog.setLocationRelativeTo(mw);
				WDialog.setVisible(true);
			} 

		});
		this.setWeatherB.setVisible(true);
	}
	
	
	public void exitButton() {
		this.exitB = new JButton();
		this.exitB.setIcon(new ImageIcon("resources/icons/exit.png"));
		this.exitB.setToolTipText("exit from the simulator");
		this.exitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExitDialog EDialog = new ExitDialog();
				EDialog.setSize(new Dimension(200, 100));
				EDialog.setLocationRelativeTo(mw);
				EDialog.setVisible(true);
			}
		});
		this.exitB.setVisible(true);
	}
	
	public void runButton() {
		this.runB = new JButton();
		this.runB.setIcon(new ImageIcon("resources/icons/run.png"));
		this.runB.setToolTipText("run the simulator");
		this.runB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ticks = Integer.parseInt(nTicks.getValue().toString());
				enableToolBar(false);
				run_sim(ticks);
			}
		});
		this.runB.setVisible(true);
	}
	public void stopButton() {
		this.stopB = new JButton();
		this.stopB.setIcon(new ImageIcon("resources/icons/stop.png"));
		this.stopB.setToolTipText("stop the simulator");
		this.stopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		this.stopB.setVisible(true);
	}
	
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
		_ctrl.run(1);
		} catch (Exception e) {
		// TODO show error message
			_stopped = true;
			JOptionPane.showMessageDialog(null,"An Error has occurred", "Error",
					JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		SwingUtilities.invokeLater(new Runnable() { 
			@Override
			public void run() {
			run_sim(n - 1);
			}
		});
		} else {
			enableToolBar(true);
			_stopped = true;
			}
		}
		
	
		private void stop() {
		_stopped = true;
		}
		
	public void enableToolBar(boolean enable) {
		this.selectB.setEnabled(enable);
		this.setCO2B.setEnabled(enable);
		this.setWeatherB.setEnabled(enable);
		this.nTicks.setEnabled(enable);
		this.exitB.setEnabled(enable);
		this._stopped = enable;
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


	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.map = map;
		this.time = time;
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.map = map;
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.map = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this.map = map;
		this.time = time;
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
	

}
