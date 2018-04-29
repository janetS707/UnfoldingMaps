package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
		
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Drawing a centered square for Ocean earthquakes
		// DO NOT set the fill color.  That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered square.
		
		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor
		int m = (int) this.getMagnitude();
		
		 if (m < THRESHOLD_LIGHT) {
		    	m = m*2;
		    }
		    else if (m >= THRESHOLD_LIGHT && m < THRESHOLD_MODERATE) {
		    	m = m*3;
		    }
		    else {
		    	m = m*4;
		    }

		int calcX = (int) ((x + m)/2);
		int calcY = (int) ((y + m)/2);
		
		pg.rect(calcX, calcY, m, m);
		
	}
	


	

}
