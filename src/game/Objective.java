package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Objective extends Entity
{

	public Objective(Game g, String name, Image image, Shape col)
	{
		super(g, name, image, col);
	}

	public Objective(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		super(g, name, image, col, pos);
	}

	public void update(GameContainer gc, int delta)
	{
	}

	public void handleCollision(Entity collidable)
	{
	}
	
	public void exit()
	{
	}
}
