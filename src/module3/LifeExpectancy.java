package module3;

//Java utilities libraries
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.Map;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

public class LifeExpectancy extends PApplet {
	// The map
		private UnfoldingMap map;
		private Map<String, Float> lifeExpByCountry;
		private List<Feature> countries = new ArrayList<Feature>();
		private List<Marker> countryMarkers = new ArrayList<Marker>();
		
		// My Map Provider
		AbstractMapProvider provider = new Microsoft.HybridProvider();
	
	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, provider );
		MapUtils.createDefaultEventDispatcher(this,  map);
		
		//Read in the CSV File
		lifeExpByCountry = loadLifeExpectancyFromCSV("C:/DEV/gitRepo/UnfoldingMaps/data/LifeExpectancyWorldBank.csv");
		
		countries = GeoJSONReader.loadData(this, "C:/DEV/gitRepo/UnfoldingMaps/data/countries.geo.json");
		
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		map.addMarkers(countryMarkers);
		shadeCountries();
	}
	
	public void draw() {
		map.draw();
	}
	
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName){
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		String[] rows = loadStrings(fileName);
		
		
		
		for (String row : rows) {
			String[] columns = row.split(",");
			String country = columns[4];
			String lifeExp = columns[5];
						
			if (country.length() == 3) {
				if(!lifeExp.equals("..")){    
					float value = Float.parseFloat(lifeExp); 
					lifeExpMap.put(country, value);   
				}
			}
		}
		return lifeExpMap;
	}
	
	private void shadeCountries() {
		for (Marker marker : countryMarkers) {
			String countryId = marker.getId();
			
			//check to make sure the country being shown on the map has a corresponding data 
			//in the lifeExpectancy data 
			if (lifeExpByCountry.containsKey(countryId)) {
				float lifeExp = lifeExpByCountry.get(countryId);
				//map is a built-in method of unfolding maps.  It translates the life expectancy
				//minimum (40) and maximum (90) into color values (10) and (255) respectively.  
				//The method returns float and we cast to integer
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255); 
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else {
				marker.setColor(color(150,150,150));
			}
		}
	}
}
