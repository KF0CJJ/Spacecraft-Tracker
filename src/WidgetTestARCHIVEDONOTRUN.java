
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


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
import java.awt.Choice;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.*;


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
	
	//public static void main(String[] args) {
	//	WidgetTestARCHIVEDONOTRUN WidgetTest = new WidgetTestARCHIVEDONOTRUN();
	//}
	
	public WidgetTestARCHIVEDONOTRUN() {
		
		
		getContentPane().setForeground(new Color(0, 255, 0));
		getContentPane().setBackground(new Color(0, 0, 0));
		setForeground(new Color(0, 255, 0));
		setBackground(new Color(0, 0, 0));
		
		
		//data stuff
		try {
			jpl = new JPLHorizons();
		}
		catch(Exception e){
			System.out.println("No internet");
		}
		//gui stuff
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//menu bar setup
		menuBar = new JMenuBar();
		menu = new JMenu();
		this.setJMenuBar(menuBar);
		
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
		 // Create chart
		
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
		
		//set renderer so only points are plotted with no lines connecting them
		
		
		plot = (PolarPlot) chart.getPlot();
		//plot.setRenderer(0, PolarItemRenderer);
		ValueAxis axis = plot.getAxis(); 
		axis.setRange(0,90);
		axis.setInverted(true);
		axis.setTickLabelsVisible(true);

	    
	    //setContentPane(panel);
	    
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
		elLabelDescribe.setBackground(Color.BLACK);
		elLabelDescribe.setBounds(10, 40, 136, 25);
		panel.add(elLabelDescribe);
		
		azLabel = new JLabel("0");
		azLabel.setHorizontalAlignment(SwingConstants.CENTER);
		azLabel.setForeground(textColor);
		azLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		azLabel.setBackground(Color.BLACK);
		azLabel.setBounds(181, 11, 136, 25);
		panel.add(azLabel);
		
		JLabel azLabelDescribe = new JLabel("Azimuth");
		azLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		azLabelDescribe.setForeground(textColor);
		azLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		azLabelDescribe.setBackground(Color.BLACK);
		azLabelDescribe.setBounds(181, 40, 136, 25);
		panel.add(azLabelDescribe);
		
		distLabel = new JLabel("0");
		distLabel.setHorizontalAlignment(SwingConstants.CENTER);
		distLabel.setForeground(textColor);
		distLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distLabel.setBackground(Color.BLACK);
		distLabel.setBounds(10, 76, 136, 25);
		panel.add(distLabel);
		
		JLabel distLabelDescribe = new JLabel("Distance");
		distLabelDescribe.setHorizontalAlignment(SwingConstants.CENTER);
		distLabelDescribe.setForeground(textColor);
		distLabelDescribe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distLabelDescribe.setBackground(Color.BLACK);
		distLabelDescribe.setBounds(10, 105, 136, 25);
		panel.add(distLabelDescribe);
		
		JLabel dopplerLabel = new JLabel("0");
		dopplerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dopplerLabel.setForeground(new Color(96, 220, 0));
		dopplerLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dopplerLabel.setBackground(Color.BLACK);
		dopplerLabel.setBounds(181, 76, 136, 25);
		panel.add(dopplerLabel);
		
		JLabel distLabelDescribe_1 = new JLabel("Distance");
		distLabelDescribe_1.setHorizontalAlignment(SwingConstants.CENTER);
		distLabelDescribe_1.setForeground(new Color(96, 220, 0));
		distLabelDescribe_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distLabelDescribe_1.setBackground(Color.BLACK);
		distLabelDescribe_1.setBounds(181, 105, 136, 25);
		panel.add(distLabelDescribe_1);
		
		
		scBoxOptions= new ArrayList<String>(Arrays.asList("","Create new S/C","LRO","301"));
		
		scBox = new JComboBox(scBoxOptions.toArray());
		scBox.setToolTipText("Select a S/C to track");
		scBox.setForeground(textColor);
		scBox.setBackground(new Color(0, 0, 0));
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
		
		scBox.addItemListener(new ItemListener() {
            @SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent ie) {
            	
            	if(ie.getStateChange() == ItemEvent.SELECTED) {
            		//do nothing if null is selected
            		if(scBox.getSelectedIndex()==0) {
            			
            		}
            		//if create new S/C has been selected, make a new SC in the scBoxOptions array
            		else if(scBox.getSelectedIndex()==1) {
            			scBoxOptions.add(JOptionPane.showInputDialog(null, "Enter S/C name", ""));
            			scBox.addItem(scBoxOptions.get(scBoxOptions.size()-1));
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
		// TODO Auto-generated method stub
		
	}
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
	    				//update graph
	    				series1.clear();
	    				
	    				series1.add(jpl.getAz(), jpl.getEl());
	    				//update now2 to current time
	    				
	    				now2 = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(30);
	    			}
	    		}
	    		return null;
			
	    	} catch (Exception e) {
		
	    		e.printStackTrace();
	    		System.out.println("Could not connect to JPL horizons");
	    		return null;
	    	}
	    }
		 };
		sw1.execute(); 
	}
	private ArrayList<Double> azList;
	private ArrayList<Double> elList;
	private ArrayList<Double> distList;
	//swing worker for requesting range of data
	public  void doRequestTimeRangeData(String step) {
		SwingWorker sw1 = new SwingWorker() {
	    @Override
	    protected Void doInBackground() {
	
	    	try {
	    		scBoxOptionItem = (String) scBoxOptions.toArray()[scBox.getSelectedIndex()];
	    		jpl.requestTimeRangeData(Double.parseDouble(latBox.getText()),Double.parseDouble(longBox.getText()),1.0,scBoxOptionItem,startTimeField.getText(),endTimeField.getText(),step);
	    		distLabel.setText(jpl.getDist()+"");
	    		azLabel.setText(jpl.getAz()+"");
	    		elLabel.setText(jpl.getEl()+"");
	    		//update graph
	    		series1.clear();
	    		
	    		//get arraylists from jpl
	    		azList = jpl.getAzArrList();
	    		elList = jpl.getElArrList();
	    		distList = jpl.getDistArrList();
	    		//get size of lists before for loop, more efficent
	    		int listSize = azList.size();
	    		// go through each index of data lists and add to polar graph
	    		series1.add(0, Double.NaN);
	    		for(int i=0;i<listSize;i++) {
	    			if(elList.get(i)>0) {
	    				series1.add(azList.get(i).doubleValue(), elList.get(i).doubleValue());
	    			}
	    		}
	    		series1.add(0, Double.NaN);
	    		
	    		
	    		return null;
			
	    		} catch (Exception e) {
		
	    		e.printStackTrace();
	    		System.out.println("Could not connect to JPL horizons");
	    		return null;
	    		}
	    	}
		 };
		sw1.execute(); 
	}
}
	



