package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;


/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */

//Example: {country="Virgin Islands", altitude=0, code="", city="Charlotte Amalie", name="Crown Bay"}

public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		String altitude = getAltitude();
		if(altitude.equalsIgnoreCase("0")) {
			
			// Save previous drawing style
			pg.pushStyle();
			
			// IMPLEMENT: drawing triangle for each Airport
			pg.fill(0, 255, 0);
			pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
			
			// Restore previous drawing style
			pg.popStyle();
		}
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		String name = getName();
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, pg.textWidth(name)+10, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
				
		pg.popStyle();
		
		
	}
	
	private String getName()
	{
		String name = (String)getProperty("name");
		return name + " Airport";
		
	}
	
	private String getAltitude()
	{
		return (String)getProperty("altitude");
		
	}
}
