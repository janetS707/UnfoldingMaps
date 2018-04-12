package module1;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Janet S.
  * Date: July 17, 2015
  * */
public class HelloWorld2 extends PApplet
{
	// You can ignore this.  It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;
	
	UnfoldingMap map1;
	
	AbstractMapProvider provider = new Microsoft.HybridProvider();

	public void setup() {
		size(900, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3

		// This sets the background color for the Applet.  
		this.background(214, 192, 246);
		
		// Set a zoom level
		int zoomLevel = 10;
				
		// Create the Maps  
		// parameters: PApplet, x, y, width, height, provider
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);
		
		
		// Zoom in and centers the map at 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(-33.9f, 18.4f));
	    // 33.2 (latitude) and -111.7 (longitude)
	   
		
	    // Make the maps interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
	}

	/** Draw the Applet window.  */
	public void draw() {
		map1.draw();
		
	}
}
