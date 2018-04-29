package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.awt.Color;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Janet Sandy
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	// My Map Provider
	AbstractMapProvider provider = new Microsoft.HybridProvider();

	
	// Color stuff
	int magenta = color(255, 51, 204);
	int red = color(255, 0, 0);
	int backgroundColor = color(255, 204, 153);
	int yellow = color(255, 255, 0);
    int blue = color(0, 153, 255);
    int white = color(255, 255, 255);
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	//private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/";

	//***************************** S E T U P **************************************
	public void setup() {
		size(1400, 1000, OPENGL);

		map = new UnfoldingMap(this, 350, 50, 950, 850, provider);
		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		earthquakesURL = "2.5_week.atom";
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
	    
	    List<Marker> markers = new ArrayList<Marker>();
	    
	    //----------------- Build 5 biggest quakes --------------------------
	    /*
	    List<PointFeature> bigQuakes = buildBiggestQuakes();
	    
	    for (PointFeature f: bigQuakes) {
	    	Marker marker = new SimplePointMarker(f.getLocation(), f.getProperties());
	    	Integer year = Integer.parseInt(marker.getProperty("year").toString());
	    	
	    	if (year > 2000) {
	    		marker.setColor(yellow);
	    	}
		    else {
		    	marker.setColor(blue);
		    }
		 markers.add(marker);
		 }
		 */
	   
	    //----------------- Populate other points from a file -------------------------  
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    for (PointFeature eq: earthquakes) {
	    	SimplePointMarker marker = createMarker(eq);
	    	markers.add(marker);
	    }
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
		
	/*
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	
	//***************************** C R E A T E    M A R K E R **************************
	private SimplePointMarker createMarker(PointFeature feature){
		
		//Data Structure of features
		//<entry>
	    //    <id>urn:earthquake-usgs-gov:us:1000309d</id>
	    //    <title>M 4.4 - 106km NNE of Tobelo, Indonesia</title>
	    //    <updated>2015-08-07T20:01:21.163Z</updated>
	    //    <link rel="alternate" type="text/html" href="http://earthquake.usgs.gov/earthquakes/eventpage/us1000309d"/>
	    //    <link rel="alternate" type="application/cap+xml" href="http://earthquake.usgs.gov/earthquakes/feed/v1.0/detail/us1000309d.cap"/>
	    //    <summary type="html">
	    //        <![CDATA[<p class="quicksummary"><a href="http://earthquake.usgs.gov/earthquakes/eventpage/us1000309d#dyfi" class="mmi-I" title="Did You Feel It? maximum reported intensity (0 reports)">DYFI? - <strong class="roman">I</strong></a></p><dl><dt>Time</dt><dd>2015-08-07 19:22:37 UTC</dd><dd>2015-08-08 04:22:37 +09:00 at epicenter</dd><dt>Location</dt><dd>2.540&deg;N 128.525&deg;E</dd><dt>Depth</dt><dd>226.11 km (140.50 mi)</dd></dl>]]>
	    //    </summary>
	    //    <georss:point>2.5395 128.5252</georss:point>
	    //    <georss:elev>-226110</georss:elev>
	    //    <category label="Age" term="Past Day"/>
	    //    <category label="Magnitude" term="Magnitude 4"/>
    	//</entry>
		 
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation(), feature.getProperties());
				
		String title = marker.getProperty("title").toString();
		Float  magnitude = new Float(title.substring(2,5));
		
	    if (magnitude < THRESHOLD_LIGHT) {
	    	marker.setColor(blue);
	    	marker.setRadius(magnitude*2);
	    }
	    else if (magnitude >= THRESHOLD_LIGHT && magnitude < THRESHOLD_MODERATE) {
	    	marker.setColor(yellow);
	    	marker.setRadius(magnitude*3);
	    }
	    else {
	    	marker.setColor(red);
	    	marker.setRadius(magnitude*4);
	    }
	    
	    // Finally return the marker
	    return marker;
	}
	
	//***************************** D R A W **************************************
	public void draw() {
	    background(backgroundColor);
	    map.draw();
	    addKey();
	}

	//***************************** A D D   K E Y **************************************
	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
		
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255, 255, 255);
		rect(50, 250, 275, 250);
		
		textSize(18);
		fill(0, 0, 150, 150);
		text("Earthquake Key", 100, 295);
		
		fill(0, 153, 255); //blue
		ellipse(75, 350, 10, 10);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Magnitude below 4.0", 100, 355);
		
		fill(255, 255, 0); //yellow
		ellipse(75, 400, 15, 15);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Magnitude 4.0+", 100, 405);
		
		fill(255, 0, 0); //red
		ellipse(75, 450, 20, 20);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Magnitude 5.0+", 100, 455);
		
		
	}
	
	private List<PointFeature>  buildBiggestQuakes() {
		
		List<PointFeature> bigQuakes = new ArrayList<PointFeature>();
		
	    //------ Chile Earthquake -----
	    Location chileLoc = new Location (-38.14f,-73.03f);   
	    PointFeature chileEqFtr = new PointFeature(chileLoc);
	    chileEqFtr.addProperty("title", "Chile");
	    chileEqFtr.addProperty("magnitude", "9.5");
	    chileEqFtr.addProperty("date", "May 22, 1960");
	    chileEqFtr.addProperty("year", "1960");
	    bigQuakes.add(chileEqFtr);
	    
	  //------ Alaska Earthquake -----
	    Location alaskaLoc = new Location (61.02f,-147.65f);  
	    PointFeature alaskaEqFtr = new PointFeature(alaskaLoc);
	    alaskaEqFtr.addProperty("title", "Alaska");
	    alaskaEqFtr.addProperty("magnitude", "9.2");
	    alaskaEqFtr.addProperty("date", "March 03, 1964");
	    alaskaEqFtr.addProperty("year", "1964");
	    bigQuakes.add(alaskaEqFtr);
	    
	    //------ Sumatra Earthquake -----
	    Location sumatraLoc = new Location (3.30f,95.78f);  
	    PointFeature sumatraEqFtr = new PointFeature(sumatraLoc);
	    sumatraEqFtr.addProperty("title", "Sumatra");
	    sumatraEqFtr.addProperty("magnitude", "9.1");
	    sumatraEqFtr.addProperty("date", "December 26, 2004");
	    sumatraEqFtr.addProperty("year", "2004");
	    bigQuakes.add(sumatraEqFtr);
	    
	  //------ Japan Earthquake -----
	    Location japanLoc = new Location (38.322f,142.369f);  
	    PointFeature japanEqFtr = new PointFeature(japanLoc);
	    japanEqFtr.addProperty("title", "Japan");
	    japanEqFtr.addProperty("magnitude", "9.0");
	    japanEqFtr.addProperty("date", "March 11, 2011");
	    japanEqFtr.addProperty("year", "2011");
	    bigQuakes.add(japanEqFtr);
	    
	  //------ Kanamori Earthquake -----
	    Location kanamoriLoc = new Location (52.76f,160.06f);  
	    PointFeature kanamoriEqFtr = new PointFeature(kanamoriLoc);
	    kanamoriEqFtr.addProperty("title", "Kanamori");
	    kanamoriEqFtr.addProperty("magnitude", "9.0");
	    kanamoriEqFtr.addProperty("date", "November 04, 1952");
	    kanamoriEqFtr.addProperty("year", "1952");
	    bigQuakes.add(kanamoriEqFtr);
	    
	    return bigQuakes;
	}
}
