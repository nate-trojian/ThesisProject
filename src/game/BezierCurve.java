package game;

import org.newdawn.slick.geom.Vector2f;

public class BezierCurve
{
	Vector2f start, control, end;
	float currStep, totDist;
	final float STEPS = 15f;
	
	public BezierCurve(Vector2f s, Vector2f c, Vector2f e)
	{
		start = s.copy();
		control = c.copy();
		end = e.copy();
		currStep = 0;
		totDist = getLength();
		//System.out.println("DIST of BEZIER " + totDist);
	}
	
	public float getLength()
	{
		Vector2f prevP = new Vector2f(), p = new Vector2f();
		float ret=0, t=0;
		for(float i=0; i<STEPS; i++)
		{
			t = i/STEPS;
			p = evaluate(t);
			if(i>0)
			{
				ret+= p.distanceSquared(prevP);
				prevP = p;
			}
		}
		return (float) Math.sqrt(ret);
	}
	
	public Vector2f evaluate(float t)
	{
		return start.copy().scale((1-t) * (1-t)).add(control.copy().scale(2 * (1-t) * t)).add(end.copy().scale(t*t));
	}
	
	public Vector2f increment(float s)
	{
		//System.out.println("INC BY: " + s/totDist);
		currStep += s/totDist;
		if(currStep > 1f)
			currStep = 1f;
		return evaluate(currStep);
	}
	
	public boolean isDone()
	{
		return currStep >= 1f;
	}
}
