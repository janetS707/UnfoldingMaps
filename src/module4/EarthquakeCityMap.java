package module4;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// My Map Provider
	AbstractMapProvider mapProvider = new Microsoft.HybridProvider();
	
	// Color stuff
	int magenta = color(255, 51, 204);
	int red = color(255, 0, 0);
	int backgroundColor = color(255, 204, 153);
	int yellow = color(255, 255, 0);
    int blue = color(0, 153, 255);
    int white = color(255, 255, 255);
	
	// Markers for each city
	private List<Marker> cityMarkers;
	
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// A List of quakes for each country
	private List<Marker> countryQuakes;
	
	public void setup() {		
		size(1400, 1000, OPENGL);
		//map = new UnfoldingMap(this, 200, 50, 650, 600, mapProvider);
		map = new UnfoldingMap(this, 350, 50, 950, 850, mapProvider);
		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
	    //earthquakesURL = "2.5_week.atom";
		
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// WHEN TAKING THIS QUIZ: Uncomment the next line
		earthquakesURL = "quiz1.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(backgroundColor);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {	
		//***************************** A D D   K E Y **************************************
		// helper method to draw key in GUI
		// TODO: Implement this method to draw the key
			
		int x = 50;
		int y = 250;
		int w = 275;
		int h = 450;
		int s = 15;
		
		fill(255, 255, 255);
		// x, y, w, h
		//rect(50, 250, 275, 350);
		rect(x, y, w, h);
		
		textSize(18);
		fill(0, 0, 150, 150);
		text("Earthquake Key", x+50, y+55);
		
		fill(255, 0, 0); //red
		triangle(x+15, y+105, x+25, y+90, x+35, y+105);
		textSize(18);
		fill(0, 0, 150, 150);
		text("City Marker", x+50, y+105);
		
		fill(255, 255, 255); //white
		ellipse(x+25, y+150, s, s);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Land Marker", x+50, y+155);
		
		fill(255, 255, 255); //white
		rect(x+20, y+190, s, s);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Ocean Quake", x+50, y+205);
		
		fill(0, 0, 150, 150);
		text("Size ~ Magnitude", x+20, y+255);		
		
		fill(255, 255, 0); //yellow
		ellipse(x+25, y+295, s, s);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Shallow", x+50, y+300);
		
		fill(0, 153, 255); //blue
		ellipse(x+25, y+345, s, s);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Intermediate", x+50, y+350);
		
		fill(255, 0, 0); //red
		ellipse(x+25, y+395, s, s);
		textSize(18);
		fill(0, 0, 150, 150);
		text("Deep", x+50, y+400);
		
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		// Loop over all the country markers.  
		// For each, check if the earthquake PointFeature is in the 
		// If isInCountry ever returns true, isLand should return true.
		for (Marker m : countryMarkers) {
			if (isInCountry(earthquake, m)) {
				return true;
			}
		}
		// not inside any country
		return false;
	}
	
	/* prints countries with number of earthquakes as
	 * Country1: numQuakes1
	 * Country2: numQuakes2
	 * ...
	 * OCEAN QUAKES: numOceanQuakes
	 * */
	private void printQuakes() 
	{
		System.out.println("*********** Total # of quakes ************");
		
		for (Marker cm : countryMarkers) {
			int landQuakeCounter = 0;
			String countryName = (String)cm.getProperty("name");
			
			for (Marker lqm : quakeMarkers) {
				EarthquakeMarker em = (EarthquakeMarker)lqm;
				if (em.isOnLand) {
					String eqCountryName = ((LandQuakeMarker)lqm).getCountry();
					if (eqCountryName.contentEquals(countryName)) {
						landQuakeCounter += 1;
						}
					}
				} //end quakeMarkers
			
			if (landQuakeCounter > 0) {
				if (landQuakeCounter > 0) {
					System.out.println(countryName + " : " + landQuakeCounter);
					}
				}
		} //end countryMarkers
		
		int oceanQuakeCounter = 0;
		for (Marker lqm : quakeMarkers) {
			EarthquakeMarker em = (EarthquakeMarker)lqm;
			
			if (!em.isOnLand) {
				oceanQuakeCounter += 1;
					}
			} //end quakeMarkers
		
		if (oceanQuakeCounter > 0) {
			System.out.println("Total Quakes in Ocean: " + oceanQuakeCounter);
			}
	} //end printQuakes
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	// You should not have to modify this code
	
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
