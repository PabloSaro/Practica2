package simulator.launcher;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _viewMode = "gui";
	private static String _inFile = null;
	private static String _outFile = null;
	private static int _timeLimit;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseViewOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTimeLimitOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Choose a view Mode").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseViewOption(CommandLine line) throws ParseException {
		if(line.hasOption("m")) {
			_viewMode = line.getOptionValue("m");
			if (!_viewMode.equals("gui")) {
				if (!_viewMode.equals("console")) {
					throw new ParseException("Choose a view Mode");
				}
			}
		}else {
			throw new ParseException("Choose a view Mode");
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		if(line.hasOption("i") && !_viewMode.equals("gui")) {
			_inFile = line.getOptionValue("i");
			if (!_viewMode.equals("console")) {
				throw new ParseException("An events file is missing");
			}
		}else{
			_inFile = line.getOptionValue("i");
			if (!_viewMode.equals("gui")) {
				throw new ParseException("An events file is missing");
			}
			//throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(line.hasOption("o") && !_viewMode.equals("gui")) {
			_outFile = line.getOptionValue("o");
			if (_outFile == null && !_viewMode.equals("gui")) {
				throw new ParseException("You need a output file");
			}
		}else {
			 if(!_viewMode.equals("gui")){
					 throw new ParseException("You need a output file");
			 }
		}
	}
	
	private static void parseTimeLimitOption(CommandLine line) throws ParseException {
		if(!line.hasOption("t")) {
			if(_timeLimit <= 0) {
				_timeLimit = _timeLimitDefaultValue;
			}else {
				_timeLimit = Integer.parseInt(line.getOptionValue("t"));
			}
		}else {
			_timeLimit = Integer.parseInt(line.getOptionValue("t"));
		}
	}

	private static void initFactories() {
		
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder("round_robin_lss") );
		lsbs.add( new MostCrowdedStrategyBuilder("most_crowded_lss") );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		ArrayList<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder("move_first_dqs") );
		dqbs.add( new MoveAllStrategyBuilder("most_all_dqs") );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(
		dqbs);
		
		ArrayList<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder("new_junction", lssFactory, dqsFactory));
		ebs.add(new NewCityRoadEventBuilder("new_city_road"));
		ebs.add(new NewInterCityRoadEventBuilder("new_inter_city_road") );
		ebs.add(new NewVehicleEventBuilder("new_vehicle"));
		ebs.add(new SetContClassEventBuilder("set_cont_class"));
		ebs.add(new SetWeatherEventBuilder("set_weather"));

		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws IOException, ExceptionConstructObject {
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim, _eventsFactory);
		InputStream in = new FileInputStream(_inFile);
		OutputStream out = new FileOutputStream(_outFile);
		try {
			ctrl.loadEvents(in);
		}catch(ExceptionJSONConstructor e) {
			e.printStackTrace();
		}
		ctrl.run(_timeLimit, out);
	}
	
	private static void startGUIMode() throws IOException, ExceptionConstructObject {
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim, _eventsFactory);
		try {
			if(_inFile != null) {
				InputStream in = new FileInputStream(_inFile);
				ctrl.loadEvents(in);
			}
		}catch(ExceptionJSONConstructor | FileNotFoundException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
		ctrl.run(_timeLimit);
		
	}

	private static void start(String[] args) throws IOException, ExceptionConstructObject {
		initFactories();
		parseArgs(args);
		if(_viewMode.equals("console")) {
			startBatchMode();
		}else {
			startGUIMode();
		}
		
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
