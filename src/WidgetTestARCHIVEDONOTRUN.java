
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.renderer.PolarItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.*;
import org.jfree.data.category.*;
import org.jfree.data.xy.*;


import java.awt.GridBagLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.FlowLayout;
import java.awt.Window.Type;
import java.awt.*;
import javax.swing.*;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
//TODO make an EME mode for moon? Maybe also have a way to try and calc best time to reach certain area?
//TODO add documentation to github
class WidgetTestARCHIVEDONOTRUN extends JFrame implements ActionListener{
	private JComboBox scBox;
	private Color bgColor;
	private Color textColor;
	private JDialog addSC;
	
	private JMenuBar menuBar;
	private JMenu menu;
	private JPLHorizons jpl;
	//Array of options for selectSCBox
	private ArrayList<String> scBoxOptions;
	private ArrayList<Double> freqList;
	private ArrayList<String> scNickList;
	
	//data arrays
	private ArrayList<String> timeList;
	private ArrayList<Double> azList;
	private ArrayList<Double> elList;
	private ArrayList<Double> distList;
	private ArrayList<Double> dopplerList;
	
	//swing stuff
	private JTextField latBox;
	private JTextField longBox;
	private final Action action = new SwingAction();
	private JTextField timeIntervalField;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JLabel distLabel;
	private JLabel azLabel;
	private JLabel elLabel;
	private LocalDateTime now;
	private DateTimeFormatter dtf;
	private String scBoxOptionItem;
	private XYSeries series1;
	private PolarPlot plot;
	private JSpinner selectTimeSpinner;
	private SpinnerListModel spinnerModel;
	private JLabel dopplerLabel;
	//some constants for calculations
	private double speedOfLight;
	//JSON stuff
	private JSONArray scArr;
	private JSONObject tempJson;
	private JSONObject spaceCraftJson;
	private JSONTokener jsonTokener;
	private File scJsonFile;
	private FileWriter fileWriter;
	private FileReader fileReader;
	public WidgetTestARCHIVEDONOTRUN(){
		//initalize json stuff
		spaceCraftJson = new JSONObject();
		tempJson = new JSONObject();
		scArr = new JSONArray();
		
		/*  if there is no sc.JSON file, fill out scArr with the moon and LRO
		 * if there is an sc.json file, load everything in it into scArr
		 * then parse scArr into the 3 arraylists*/
		try {
			//make the sc.json file path, will be in same directory as this file is, or the .exe or .jar file, depends on OS
			scJsonFile = new File(System.getProperty("user.dir")+"\\sc.json");
			
			
			//if sc.json doesnt exist or is empty, make it
			if(scJsonFile.length()==0) {
				fileWriter = new FileWriter(scJsonFile);
				//create empty sc.json file
				scJsonFile.createNewFile();
				//make spaceCraftJson with moon and lro
				spaceCraftJson.put("spaceCraft",scArr);
				tempJson.put("scId","301");
				tempJson.put("scNick","Moon");
				tempJson.put("freq",0.0);
				scArr.put(tempJson); 
				tempJson = new JSONObject();
			    tempJson.put("scId","LRO");
			    tempJson.put("scNick","LRO");
			    tempJson.put("freq",2270.5);
			    scArr.put(tempJson); 
			    fileWriter.write(spaceCraftJson.toString());
			    fileWriter.close();
			}else {
				//if the file does exist, read it
				fileReader = new FileReader(scJsonFile);
				jsonTokener = new JSONTokener(fileReader);
				spaceCraftJson = new JSONObject(jsonTokener);
			}
			//put stuff from spaceCraftJson into the 3 array lists for id nickname and freq
			//initalize array lists with null and Create new S/C options
			scBoxOptions= new ArrayList<String>(Arrays.asList("","Create new S/C"));
			scNickList = new ArrayList<String>(Arrays.asList("","Create new S/C"));
			freqList = new ArrayList<Double>(Arrays.asList(0.0,0.0));
			//go through spaceCraftJson in a loop and fill out each arraylist
			scArr = spaceCraftJson.getJSONArray("spaceCraft");
			System.out.println(scArr);
			for(int i=0;i<scArr.length();i++) {
				tempJson = scArr.getJSONObject(i);
				scBoxOptions.add((String) tempJson.opt("scId"));
				scNickList.add((String) tempJson.opt("scNick"));
				freqList.add(Double.parseDouble(tempJson.opt("freq").toString()));
			}
		
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
			//if the previous code somehow doesnt work, must mean something is wrong with java
			JOptionPane.showMessageDialog(null, "Code broke while trying to process sc.json, let dev know", "Code broke", JOptionPane.ERROR_MESSAGE);
			System.out.println("code broke during json initialization, no real way for user to break this part so let the dev know");
			System.exit(1);
		}
		System.out.println(spaceCraftJson.toString());
		//set constants
		speedOfLight = 299792.458;
		//init dopplerList
		dopplerList = new ArrayList<Double>();
		
		getContentPane().setForeground(new Color(0, 255, 0));
		getContentPane().setBackground(new Color(0, 0, 0));
		setForeground(new Color(0, 255, 0));
		setBackground(new Color(0, 0, 0));
		
		
		//make new JPLHorizons, if that doesnt work for whatever reason send stuff to cmd line
		try {
			jpl = new JPLHorizons();
		}
		catch(Exception e){
			System.out.println("No internet");
		}
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    		try {
		    			spaceCraftJson.put("spaceCraft",scArr);
						fileWriter = new FileWriter(scJsonFile);
						fileWriter.write(spaceCraftJson.toString());
					    fileWriter.close();
					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
		        }
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//menu bar setup
		menuBar = new JMenuBar();
		menu = new JMenu();
		this.setJMenuBar(menuBar);
		
		//TODO add a clock on the menu bar
		
		//gui stuff
		JButton settingsButton = new JButton("Settings");
		settingsButton.setToolTipText("Go to the settings menu");
		menuBar.add(settingsButton);
		
		bgColor = new Color(0,0,0);
		textColor = new Color(96,220,0);
		
		this.setTitle("Spacecraft Tracker V0.7");
		getContentPane().setLayout(new GridLayout(1, 2));
		
		JPanel westPanel = new JPanel();
		westPanel.setBackground(bgColor);
		getContentPane().add(westPanel);
		westPanel.setLayout(new GridLayout(2, 1));
		JPanel scPanel = new JPanel();
		scPanel.setBackground(bgColor);
		
		XYDataset dataset = getXYDataset();
		
		westPanel.add(scPanel);
		scPanel.setLayout(null);
		
		latBox = new JTextField();
		latBox.setText("38.917863");
		latBox.setToolTipText("Enter your latitude");
		latBox.setHorizontalAlignment(SwingConstants.CENTER);
		latBox.setForeground(textColor);
		latBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		latBox.setBackground(bgColor);
		latBox.setBounds(10, 11, 123, 20);
		scPanel.add(latBox);
		latBox.setColumns(10);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setBounds(133, 11, 50, 11);
		scPanel.add(horizontalStrut);
		
		longBox = new JTextField();
		longBox.setText("-105.062490");
		longBox.setToolTipText("Enter your longitude");
		longBox.setHorizontalAlignment(SwingConstants.CENTER);
		longBox.setForeground(textColor);
		longBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		longBox.setBackground(bgColor);
		longBox.setBounds(181, 11, 136, 20);
		scPanel.add(longBox);
		longBox.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Latitude");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setForeground(textColor);
		lblNewLabel.setBackground(bgColor);
		lblNewLabel.setBounds(10, 42, 123, 25);
		scPanel.add(lblNewLabel);
		
		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setHorizontalAlignment(SwingConstants.CENTER);
		lblLongitude.setForeground(textColor);
		lblLongitude.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLongitude.setBackground(bgColor);
		lblLongitude.setBounds(181, 42, 136, 25);
		scPanel.add(lblLongitude);

		JLabel lblSc = new JLabel("Spacecraft");
		lblSc.setHorizontalAlignment(SwingConstants.CENTER);
		lblSc.setForeground(new Color(96, 220, 0));
		lblSc.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSc.setBackground(Color.BLACK);
		lblSc.setBounds(10, 114, 123, 25);
		scPanel.add(lblSc);
		
	      XYSeriesCollection dataset1 = new XYSeriesCollection();

	      series1 = new XYSeries("Series1");
	      series1.add(0, 0);
	      series1.add(90, 45);
	      series1.add(180, 90);
	      dataset1.addSeries(series1);
		
		JFreeChart chart = ChartFactory.createPolarChart(
	           null, // Chart title
	            dataset1,
	            false,
	            true,
	            false
	            );
		//TODO make graph only plot points, or find a way to get rid of connecting line
		
		
		plot = (PolarPlot) chart.getPlot();
		//plot.setRenderer(0, PolarItemRenderer);
		ValueAxis axis = plot.getAxis(); 
		axis.setRange(0,90);
		axis.setInverted(true);
		axis.setTickLabelsVisible(true);

		ChartPanel polarGraphPanel = new ChartPanel(chart);
		polarGraphPanel.setBackground(bgColor);
		westPanel.add(polarGraphPanel);
		polarGraphPanel.setMouseZoomable(false);
		
		JPanel dataPanel = new JPanel();
		dataPanel.setBackground(bgColor);
		getContentPane().add(dataPanel);
		dataPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.GREEN);
		panel.setBackground(Color.BLACK);
		dataPanel.add(panel);
		panel.setLayout(null);
		
		elLabel = new JLabel("0");
		elLabel.setHorizontalAlignment(SwingConstants.CENTER);
		elLabel.setForeground(textColor);
		elLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		elLabel.setBackground(Color.BLACK);
		elLabel.setBounds(10, 11, 136, 25);
		panel.add(elLabel);
		
		JLabel elLabelDescribe = new JLabel("Elevation");
		elLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		elLabelDescribe.setForeground(textColor);
		elLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		elLabelDescribe.setBackground(bgColor);
		elLabelDescribe.setBounds(10, 40, 136, 25);
		panel.add(elLabelDescribe);
		
		azLabel = new JLabel("0");
		azLabel.setHorizontalAlignment(SwingConstants.CENTER);
		azLabel.setForeground(textColor);
		azLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		azLabel.setBackground(bgColor);
		azLabel.setBounds(181, 11, 136, 25);
		panel.add(azLabel);
		
		JLabel azLabelDescribe = new JLabel("Azimuth");
		azLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		azLabelDescribe.setForeground(textColor);
		azLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		azLabelDescribe.setBackground(bgColor);
		azLabelDescribe.setBounds(181, 40, 136, 25);
		panel.add(azLabelDescribe);
		
		distLabel = new JLabel("0");
		distLabel.setHorizontalAlignment(SwingConstants.CENTER);
		distLabel.setForeground(textColor);
		distLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distLabel.setBackground(bgColor);
		distLabel.setBounds(10, 76, 136, 25);
		panel.add(distLabel);
		
		JLabel distLabelDescribe = new JLabel("Distance");
		distLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		distLabelDescribe.setForeground(textColor);
		distLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distLabelDescribe.setBackground(bgColor);
		distLabelDescribe.setBounds(10, 105, 136, 25);
		panel.add(distLabelDescribe);
		
		dopplerLabel = new JLabel("0");
		dopplerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dopplerLabel.setForeground(new Color(96, 220, 0));
		dopplerLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dopplerLabel.setBackground(bgColor);
		dopplerLabel.setBounds(181, 76, 136, 25);
		panel.add(dopplerLabel);
		
		JLabel dopplerLabelDescribe = new JLabel("Doppler Shift");
		dopplerLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		dopplerLabelDescribe.setForeground(new Color(96, 220, 0));
		dopplerLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dopplerLabelDescribe.setBackground(bgColor);
		dopplerLabelDescribe.setBounds(181, 105, 136, 25);
		panel.add(dopplerLabelDescribe);
		
		JLabel timeSpinnerLbl = new JLabel("Time");
		timeSpinnerLbl.setHorizontalAlignment(SwingConstants.CENTER);
		timeSpinnerLbl.setForeground(new Color(96, 220, 0));
		timeSpinnerLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		timeSpinnerLbl.setBackground(bgColor);
		timeSpinnerLbl.setBounds(10, 175, 136, 25);
		panel.add(timeSpinnerLbl);
		
		//stuff for spinner that selects what time you want to view data for 
		timeList = new ArrayList<String>();
		timeList.add(" ");
		spinnerModel =  new SpinnerListModel(timeList);
		selectTimeSpinner = new JSpinner();
		selectTimeSpinner.setModel(spinnerModel);
		selectTimeSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		selectTimeSpinner.setToolTipText("Choose what time you want to know the values for, only works when you asked for a range of points");
		selectTimeSpinner.setForeground(textColor);
		selectTimeSpinner.setBackground(bgColor);
		selectTimeSpinner.setBounds(10, 144, 147, 20);
		panel.add(selectTimeSpinner);
		//listener checking if selected index has changed
		//TODO make time interval become the update rate, and if its null then default to 1 min. also make display update smoothly, make it track in betwen updates for motors
		selectTimeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//check and see if first index is selected, if it is graph all points
				if(getSelectedIndex(selectTimeSpinner,timeList)==0) {
					//get size of lists before for loop, more efficent
		    		int listSize = azList.size();
		    		//clear graph
		    		series1.clear();
					// go through each index of data lists and add to polar graph
					
		    		series1.add(Double.NaN, Double.NaN);
		    		
		    		for(int i=0;i<listSize;i++) {
		    			if(elList.get(i)>0) {
		    				series1.add(azList.get(i).doubleValue(), elList.get(i).doubleValue());
		    			}
		    		}
		    		series1.add(Double.NaN, Double.NaN);
				}
				//if 1st index isnt selected then only plot one point at chosen time
				else {
					//clear graph
					series1.clear();
					//add point of interest onto graph
					series1.add(azList.get(getSelectedIndex(selectTimeSpinner,timeList)),elList.get(getSelectedIndex(selectTimeSpinner,timeList)));		
										
				}
				//TODO clear all the arrays 
				
				//do this regardless of what index is chosen
				//set azLabel
				azLabel.setText(azList.get(getSelectedIndex(selectTimeSpinner,timeList)).toString());
				//set elLabel
				elLabel.setText(elList.get(getSelectedIndex(selectTimeSpinner,timeList)).toString());
				//set distLabel
				distLabel.setText(distList.get(getSelectedIndex(selectTimeSpinner,timeList)).toString());
				//set dopplerLabel
				dopplerLabel.setText(String.valueOf(dopplerList.get(getSelectedIndex(selectTimeSpinner,timeList))));
				
				
			}
		});
		//set up dropdown box for selecting SC
		scBox = new JComboBox(scNickList.toArray());
		scBox.setToolTipText("Select a S/C to track");
		scBox.setForeground(textColor);
		scBox.setBackground(bgColor);
		scBox.setOpaque(true);
		scBox.setBounds(10, 78, 123, 25);
		
		
		scPanel.add(scBox);
		
		String[] timeIntervals = {"","s","m","h","d"};
		JComboBox timeComboBox = new JComboBox(timeIntervals);
		timeComboBox.setToolTipText("Select time interval, leave blank for current time");
		timeComboBox.setForeground(textColor);
		timeComboBox.setBackground(bgColor);
		timeComboBox.setBounds(83, 148, 50, 22);
		scPanel.add(timeComboBox);
		
		JLabel lblTimeInterval = new JLabel("Interval");
		lblTimeInterval.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeInterval.setForeground(textColor);
		lblTimeInterval.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTimeInterval.setBackground(Color.BLACK);
		lblTimeInterval.setBounds(10, 181, 123, 25);
		scPanel.add(lblTimeInterval);
		
		timeIntervalField = new JTextField();
		timeIntervalField.setToolTipText("Enter the time interval, leave blank for current time");
		timeIntervalField.setHorizontalAlignment(SwingConstants.CENTER);
		timeIntervalField.setForeground(textColor);
		timeIntervalField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		timeIntervalField.setColumns(10);
		timeIntervalField.setBackground(Color.BLACK);
		timeIntervalField.setBounds(10, 150, 63, 20);
		scPanel.add(timeIntervalField);
		
		JLabel lblStartTime = new JLabel("Start Time");
		lblStartTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblStartTime.setForeground(textColor);
		lblStartTime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblStartTime.setBackground(Color.BLACK);
		lblStartTime.setBounds(181, 114, 136, 25);
		scPanel.add(lblStartTime);
		
		startTimeField = new JTextField();
		startTimeField.setText("2022-06-20 22:20");
		startTimeField.setToolTipText("Enter start time, must be in yyyy-MM-dd HH:mm format, leave blank for current time");
		startTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		startTimeField.setForeground(textColor);
		startTimeField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		startTimeField.setColumns(10);
		startTimeField.setBackground(Color.BLACK);
		startTimeField.setBounds(181, 78, 136, 20);
		scPanel.add(startTimeField);
		
		JLabel lblEndtime = new JLabel("End Time");
		lblEndtime.setHorizontalAlignment(SwingConstants.CENTER);
		lblEndtime.setForeground(textColor);
		lblEndtime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEndtime.setBackground(Color.BLACK);
		lblEndtime.setBounds(181, 181, 136, 25);
		scPanel.add(lblEndtime);
		
		endTimeField = new JTextField();
		endTimeField.setToolTipText("Enter stop time, must be in yyyy-MM-dd HH:mm format, leave blank for current time");
		endTimeField.setText("2022-06-21 22:20");
		endTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		endTimeField.setForeground(textColor);
		endTimeField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		endTimeField.setColumns(10);
		endTimeField.setBackground(Color.BLACK);
		endTimeField.setBounds(181, 147, 136, 20);
		scPanel.add(endTimeField);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(325, 0, 2, 437);
		scPanel.add(separator);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBackground(Color.WHITE);
		separator.setForeground(Color.WHITE);
		
		/*TODO make it so spacecraft can be deleted from the comboBox. Make it so spacecraft are stored in a .JSON file
		 make it that the json file is checked when the program is opened, and saved to an array. When program is closed save everything to .JSON file
		 if an SC is deleted update the JSON file. or not
		 */
		
		scBox.addItemListener(new ItemListener() {
            @SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent ie) {
            	if(ie.getStateChange() == ItemEvent.SELECTED) {
            		
            		//do nothing if null is selected
            		if(scBox.getSelectedIndex()==0) {
            			series1.clear();
            			distLabel.setText("");
            			azLabel.setText("");
            			elLabel.setText("");
            		}
            		else {
            			//make sure latbox and longbox are filed out
            			if(latBox.getText().equals("")||longBox.getText().equals("")) {
            				JOptionPane.showMessageDialog(null, "Make sure latitute and longitude are filled out correctly!", "Lat Long Config Error", JOptionPane.ERROR_MESSAGE);
                			scBox.setSelectedIndex(0);
                			
                			
            			}
            			//if create new S/C has been selected, make a new SC in the scBoxOptions array
            			else if(scBox.getSelectedIndex()==1) {
            				JTextField idField = new JTextField();
            				JTextField nickField = new JTextField();
            				JTextField freqField = new JTextField();
            				Object[] options = {"Enter SC ID",idField,
            						"Enter Nickname",nickField,
            						"Enter frequency"};
            				
            				freqList.add(Double.parseDouble(JOptionPane.showInputDialog(null, options,"Enter SC data",JOptionPane.CANCEL_OPTION)));
            				scBoxOptions.add(idField.getText());
            				scNickList.add(nickField.getText());
            				//add data to scArr
            				try {
            					tempJson = new JSONObject();
								tempJson.put("scId",scBoxOptions.get(scBoxOptions.size()-1));
								tempJson.put("scNick",scNickList.get(scNickList.size()-1));
	            				tempJson.put("freq",freqList.get(freqList.size()-1));
	            				scArr.put(tempJson); 
							} catch (JSONException e) {
								e.printStackTrace();
							}
            				
            				
            				scBox.addItem(scNickList.get(scNickList.size()-1));
            				//set selected index to 0, which is null
            				scBox.setSelectedIndex(0);
            			}
            			//if something other than create new S/C has been selected, request data from jpl horizons
            			else {
            				//if none of the time fields are setup, then get current S/C data
            				//check if time is not set, doing it outside of the if statement runs faster
            				boolean checkTimeEmpty = ( (timeIntervalField.getText().equals("")|| timeIntervalField.getText().equals("0")) &&
            					endTimeField.getText().equals("")&& 
            					startTimeField.getText().equals("") && 
            					timeComboBox.getSelectedIndex()==0);
            				//check if time is set correctly, doing it outside of the if statement runs faster
            				boolean checkTimeCorrect  = ( (!timeIntervalField.getText().equals("")|| !timeIntervalField.getText().equals("0")) &&
            					!endTimeField.getText().equals("")&& 
            					!startTimeField.getText().equals("") && 
            					timeComboBox.getSelectedIndex()!=0);
            				if(checkTimeEmpty) {
            					doRequestCurrentData();
            				}
            				//if all the time fields are set correctly, then get range of data from jpl horizons
            				else if(checkTimeCorrect) {
            					doRequestTimeRangeData(timeIntervalField.getText()+" "+timeIntervals[timeComboBox.getSelectedIndex()]);
            				
            				}
            				//if some of time fields are set up but not all give user a warning
            				else {
            					JOptionPane.showMessageDialog(null, "Make sure all time inputs are empty or are fully filled out!", "Time Configuration error", JOptionPane.ERROR_MESSAGE);
            					scBox.setSelectedIndex(0);
            				}
            			}
            		}
            	}
            }
		});
		

	    this.setSize(670,500);
		this.setVisible(true);
		
	}
	
	private XYDataset getXYDataset() {
	     
	      XYSeriesCollection dataset = new XYSeriesCollection();

	      XYSeries series1 = new XYSeries("Series1",false);
	      System.out.println(series1.getAutoSort());
	      series1.add(0, 0);
	      series1.add(90, 45);
	      series1.add(180, 90);
	      dataset.addSeries(series1);
	      
	      return dataset;
	   }
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
			
			
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	 public static void main(String args[]) {
		 new WidgetTestARCHIVEDONOTRUN();
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	//TODO make error message show up for current data request, doesnt currently work
	//TODO make error message have text wrapping
	//TODO make the lat long freq and S/C be input when calling function, should be faster than getting those values every single time the position updates
	//swing worker for requesting current data, needs optimizing
	public  void doRequestCurrentData() {
		 SwingWorker sw1 = new SwingWorker() {
	    @Override
	    protected Void doInBackground() {
	
	    	try {
	    		scBoxOptionItem = (String) scBoxOptions.toArray()[scBox.getSelectedIndex()];
	    		int checkIndex = scBox.getSelectedIndex();
	    		//init time stuff
	    		dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    		LocalDateTime now1 = LocalDateTime.now(ZoneOffset.UTC);
	    		LocalDateTime now2 = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(5);
	    		//keep updating data after an amount of time has passed until a difference S/C is selected
	    		while(checkIndex == scBox.getSelectedIndex()) {
	    			now1 = LocalDateTime.now(ZoneOffset.UTC);
	    			
	    			if(now1.isAfter(now2)) {
	    				jpl.requestCurrentData(Double.parseDouble(latBox.getText()),Double.parseDouble(longBox.getText()),1.0,scBoxOptionItem);
	    				distLabel.setText(jpl.getDist()+"");
	    				azLabel.setText(jpl.getAz()+"");
	    				elLabel.setText(jpl.getEl()+"");
	    				dopplerLabel.setText(String.valueOf(freqList.get(checkIndex)+getDopplerShift(jpl.getDelta(),freqList.get(checkIndex))));
	    				//update graph
	    				series1.clear();
	    				
	    				series1.add(jpl.getAz(), jpl.getEl());
	    				//update now2 to current time
	    				
	    				now2 = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(30);
	    			}
	    		}
	    		return null;
			
	    	} catch (Exception e) {
	    		int responseCode = jpl.getResponseCode();
	    		if(responseCode != 200) {
	    			
	    		}
	    		e.printStackTrace();
	    		System.out.println("Could not connect to JPL horizons");
	    		return null;
	    	}
	    }
		 };
		sw1.execute(); 
	}
	
	//swing worker for requesting range of data
	public  void doRequestTimeRangeData(String step) {
		SwingWorker sw1 = new SwingWorker() {
	    @Override
	    protected Void doInBackground() {
	    	
	    	try {
	    		//get selected SC
	    		scBoxOptionItem = (String) scBoxOptions.toArray()[scBox.getSelectedIndex()];
	    		//request data from jpl horizons
	    		jpl.requestTimeRangeData(Double.parseDouble(latBox.getText()),Double.parseDouble(longBox.getText()),1.0,scBoxOptionItem,startTimeField.getText(),endTimeField.getText(),step);
	    		
	    		
	    		//get arraylists from jpl
	    		azList = jpl.getAzArrList();
	    		elList = jpl.getElArrList();
	    		distList = jpl.getDistArrList();
	    		timeList = jpl.getTimeArrList();
	    		dopplerList = jpl.getDeltaArrList();
	    		//add buffer in data, so all points can be displayed at once
	    		azList.add(0,0.0);
	    		elList.add(0,0.0);
	    		distList.add(0,0.0);
	    		timeList.add(0,"");
	    		dopplerList.add(0,0.0);
	    		
	    		
	    		spinnerModel.setList(timeList);
	    		
	    		//get size of lists before for loop, more efficent
	    		int listSize = azList.size();
	    		//clear graph
	    		series1.clear();
	    		// go through each index of data lists and add to polar graph
	    		series1.add(Double.NaN, Double.NaN);
	    		
	    		for(int i=0;i<listSize;i++) {
	    			if(elList.get(i)>0) {
	    				series1.add(azList.get(i).doubleValue(), elList.get(i).doubleValue());
	    			}
	    		}
	    		series1.add(Double.NaN, Double.NaN);
	    		
	    		//set dopplerList to doppler shifts
	    		for(int i=1;i<listSize;i++) {
	    			dopplerList.set(i,freqList.get(scBox.getSelectedIndex())+getDopplerShift(freqList.get(scBox.getSelectedIndex()),dopplerList.get(i)));
	    		}
	    		return null;
			
	    		} catch (Exception e) {
	    		int responseCode = jpl.getResponseCode();
	    		e.printStackTrace();
	    		System.out.println("Could not connect to JPL horizons");
	    		return null;
	    		}
	    	}
		 };
		sw1.execute(); 
	}
	//function to get index of a spinner
	public int getSelectedIndex(JSpinner spinner, ArrayList<?> values) {
	    int index=0;
	    for(Object o :values) {
	        if(o.equals(spinner.getValue()))
	            return index;
	        index++;
	    }
	    return -1;
	}
	//doppler shift calculations
	public double getDopplerShift(double freq,double deldot) {
		return (deldot/speedOfLight)*freq;
	}
}
//go through an array of SC objects and find where a certain SC is



