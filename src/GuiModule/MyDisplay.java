package GuiModule;

import processing.core.PApplet;

public class MyDisplay extends PApplet{
	
	public void setup() 
	{
		size(400, 400);
		background(200,200,200);
	}
	
	public void draw()
	{
		fill(255,255,0);
		ellipse(200, 200, 350, 350);
		fill(0,0,0);
		ellipse(150, 130, 50, 70);
		ellipse(250, 130, 50, 70);
		arc(200,280,75, 75, 0, PI);
		noFill();
		
	}
}
