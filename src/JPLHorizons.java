import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jfree.chart.util.ArrayUtils;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
//
public class JPLHorizons {
	private double lat;
	private double lon;
	private String SC;
	private double freq;
	private double az;
	private double el;
	private double dist;
	
	private String coord;
	private Calendar cal;
	private DateTimeFormatter dtf;
	private LocalDateTime now;
	private String formattedNow;
	JSONObject requestedData;
	private BufferedReader input;
	private InputStreamReader inputStream;
	private StringBuffer response;
	private String inputLine;
	private JSONObject myResponse;
	//http vars
	//private String url;
	private URL obj;
	private  HttpURLConnection jpl;
	//get data out of response vars
	private int startOfEph;
	private int endOfEph;
	private String ephString;
	private String[] ephArr;
	private int ephArrSize;
	private double delta;
	//data arrays
	private ArrayList<Double> azArrList;
	private ArrayList<Double> elArrList;
	private ArrayList<Double> distArrList;
	private ArrayList<String> timeArrList;
	private ArrayList<Double> deltaArrList;
	private StringBuilder url;
	//private double[] azArr;
	//private double[] elArr;
	//private double[] distArr;
	private int responseCode;
	
	public JPLHorizons() throws Exception{
		

		//set time stuff
		dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		cal = Calendar.getInstance();
		//other vars
		url = new StringBuilder("https://ssd.jpl.nasa.gov/api/horizons.api?");
		
		
		
	}
	//TODO if response code != 200 say that website down or equivilent for response code and dont just say that internet not work
	public void requestCurrentData(double lat, double lon, double freq, String SC) throws Exception{
		this.lat = lat;
		this.lon = lon;
		this.SC = SC;
		this.freq = freq;

		coord = Double.toString(lon) + "," + Double.toString(lat) +",0";
		//time stuff
		now = LocalDateTime.now(ZoneOffset.UTC);
		//now = java.time.LocalTime.now();
		formattedNow = now.format(dtf);
		
		//set connection params
		url.delete(0,url.length());
		url.append("https://ssd.jpl.nasa.gov/api/horizons.api?");
		url.append("format=text");
		url.append("&COMMAND=\'"+SC+"\'");
		url.append("&CENTER=\'coord\'&ANG_FORMAT=\'DEG\'&TIME_DIGITS=\'SECONDS\'&RANGE_UNITS=\'KM\'&OBJ_DATA=\'NO\'&VEC_LABELS=\'NO\'&CSV_FORMAT='YES'");
		url.append("&QUANTITIES='4,20'");
		url.append("&SITE_COORD=\'"+coord+"\'");
		url.append("&START_TIME=\'"+now.format(dtf)+"\'");
		url.append("&STOP_TIME=\'"+now.plusMinutes(1).format(dtf)+"\'");
		url.append("&STEP_SIZE=\'1 min\'");
		
		obj = new URL(url.toString());
		
		//request data from jpl horizons
		
		jpl=(HttpURLConnection)obj.openConnection();
		jpl.setRequestMethod("GET");
		jpl.setRequestProperty("User-Agent", "Mozilla/5.0");
		responseCode = jpl.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		response = new StringBuffer();
		inputStream = new InputStreamReader(jpl.getInputStream());
		input = new BufferedReader(inputStream);
		
	    while ((inputLine = input.readLine()) != null) {
	      	response.append(inputLine);
	      	System.out.println(inputLine);
	     }
	     input.close();
	     //print full text response string for debugging
	     System.out.println(response.toString());   
	     System.out.println(url+"\n");
	     ephString = response.toString();
	     
	     // look for $$SOE, take everything in string until $$EOE
	     startOfEph = ephString.indexOf("$$SOE");
	     endOfEph = ephString.indexOf("$$EOE");
	     
	     System.out.println("$$SOE at: "+startOfEph+"\n$$EOE at: "+endOfEph);
  	     ephString = ephString.substring(startOfEph+5, endOfEph);
	     System.out.println(ephString);
	     //if JPL gives an error, show user the error
	     if(startOfEph==-1 || endOfEph==-1) {
	    	 JOptionPane.showMessageDialog(null, response.toString(), "JPL error", JOptionPane.ERROR_MESSAGE);
	     }
	     //test
	     ephArr = ephString.split(",",0);
	     for (String a : ephArr)
	            System.out.println(a);
	     
	     //set az el and dist from arr into their own vars
	     az = Double.parseDouble(ephArr[3]);
	     el = Double.parseDouble(ephArr[4]);
	     dist = Double.parseDouble(ephArr[5]);
	     delta = Double.parseDouble(ephArr[6]);
	      
	     
	}

	public double getAz() {
		return az;
	}

	public double getEl() {
		return el;
	}

	public double getDist() {
		return dist;
	}
	public double getDelta() {
		return delta;
	}
	//request a range of data
	public void requestTimeRangeData(double lat, double lon, double freq, String SC,String startTime,String endTime,String step) throws Exception{
		this.lat = lat;
		this.lon = lon;
		this.SC = SC;
		this.freq = freq;

		coord = Double.toString(lon) + "," + Double.toString(lat) +",0";

		//set connection params
		//use a string buffer instead of concatenate onto string because its more efficient
		url.delete(0,url.length());
		url.append("https://ssd.jpl.nasa.gov/api/horizons.api?");
		url.append("format=text");
		url.append("&COMMAND=\'"+SC+"\'");
		url.append("&CENTER=\'coord\'&ANG_FORMAT=\'DEG\'&TIME_DIGITS=\'SECONDS\'&RANGE_UNITS=\'KM\'&OBJ_DATA=\'NO\'&VEC_LABELS=\'NO\'&CSV_FORMAT='YES'");
		url.append("&QUANTITIES='4,20'");
		url.append("&SITE_COORD=\'"+coord+"\'");
		//add start end and time step in url request
		url.append("&START_TIME=\'"+startTime+"\'");
		url.append("&STOP_TIME=\'"+endTime+"\'");
		url.append("&STEP_SIZE=\'"+step+"\'");
		
		obj = new URL(url.toString());
		
		//request data from jpl horizons
		//TODO clear arrays after running
		jpl=(HttpURLConnection)obj.openConnection();
		jpl.setRequestMethod("GET");
		jpl.setRequestProperty("User-Agent", "Mozilla/5.0");
		responseCode = jpl.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		response = new StringBuffer();
		inputStream = new InputStreamReader(jpl.getInputStream());
		input = new BufferedReader(inputStream);
		
	    while ((inputLine = input.readLine()) != null) {
	      	response.append(inputLine);
	     }
	     input.close();
	     //print full text response string for debugging
	     System.out.println(response.toString());
	     System.out.println(url+"\n");
	     ephString = response.toString();
	     
	     // look for $$SOE, take everything in string until $$EOE
	     startOfEph = ephString.indexOf("$$SOE");
	     endOfEph = ephString.indexOf("$$EOE");
	     if(startOfEph==-1 || endOfEph==-1) {
	    	 JOptionPane.showMessageDialog(null, response.toString(), "JPL error", JOptionPane.ERROR_MESSAGE);
	     }
	     System.out.println("$$SOE at: "+startOfEph+"\n$$EOE at: "+endOfEph);
  	     ephString = ephString.substring(startOfEph+5, endOfEph);
	     System.out.println(ephString);
	     
	     
	     ephArr = ephString.split(",",0);
	     ephArrSize = ephArr.length;
	     System.out.println(ephArrSize);
	     //set az el and dist from ephArr into their own arrayLists
	     azArrList=  new ArrayList<Double>();
	     elArrList= new ArrayList<Double>();
	     distArrList = new ArrayList<Double>();
	     deltaArrList = new ArrayList<Double>();
	     timeArrList = new ArrayList<String>();
	     for(int i=0;i<ephArrSize;i++) {
	    	 if(i%7==0) {
	    		 timeArrList.add(ephArr[i]);
	    	 }
	    	 if(i%7==3) {
	    		 azArrList.add(Double.parseDouble(ephArr[i]));
	    	 }
	    	 else if(i%7==4) {
	    		 elArrList.add(Double.parseDouble(ephArr[i]));
	    	 }
	    	 else if(i%7==5) {
	    		 distArrList.add(Double.parseDouble(ephArr[i])/**149597870.7*/);
	    	 }
	    	 else if(i%7==6) {
	    		 deltaArrList.add(Double.parseDouble(ephArr[i]));
	    	 }
	     }
	     System.out.println(azArrList);
	     System.out.println(elArrList);
	}
	
	     
	public ArrayList<Double> getAzArrList() {
	 	return azArrList;
	}
	public ArrayList<Double> getElArrList() {
	 	return elArrList;
	}
	public ArrayList<Double> getDistArrList() {
	 	return distArrList;
	}
	public ArrayList<String> getTimeArrList(){
		return timeArrList;
	}
	public ArrayList<Double> getDeltaArrList(){
		return deltaArrList;
	}
	public int getResponseCode() {
		return responseCode;
	}
	
	
	
	
}
