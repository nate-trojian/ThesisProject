package game;

import org.newdawn.slick.geom.Shape;

public class Util
{
	public static boolean pointInShape(float[] p, Shape s)
	{
		return s.contains(p[0], p[1]);
	}
}
