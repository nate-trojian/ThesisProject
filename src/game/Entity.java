package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity
{
	protected Game game;
	protected String name;
	protected float speed;
	protected Vector2f pos, dir;
	protected Shape col;
	protected Image image;

	public Entity(Game g, String name, Image image, Shape col)
	{
		game = g;
		this.name = name;
		this.image = image;
		this.col = col;
		speed = 0f;
		pos = new Vector2f();
		dir = new Vector2f();
	}

	public Entity(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		game = g;
		this.name = name;
		this.image = image;
		this.col = col;
		speed = 0f;
		dir = new Vector2f();
		this.pos = pos;
	}

	public String getName()
	{
		return name;
	}

	public void setSpeed(float s)
	{
		speed = s;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setDir(Vector2f dir)
	{
		this.dir = dir;
	}

	public Vector2f getDir()
	{
		return new Vector2f(dir);
	}

	public void setPos(Vector2f pos)
	{
		this.pos = pos;
	}

	public Vector2f getPos()
	{
		return new Vector2f(pos);
	}

	public void render(Graphics g)
	{
		image.draw(pos.x, pos.y);
		g.draw(getCollisionShape());
	}

	public abstract void update(GameContainer gc, int delta);

	public Shape getNormalCollisionShape()
	{
		return col;
	}

	public Shape getCollisionShape()
	{
		//return col.transform(Transform.createRotateTransform((float) Math.toRadians(dir.getTheta()), col.getCenterX(), col.getCenterY())).transform(Transform.createTranslateTransform(pos.x, pos.y));
		return col.transform(Transform.createTranslateTransform(pos.x, pos.y));
	}

	public boolean isCollidingWith(Entity collidable)
	{
		return collidable.getCollisionShape().intersects(getCollisionShape());
	}

	public abstract void handleCollision(Entity collidable);

	public boolean equals(Object o)
	{
		if(o instanceof Entity)
		{
			Entity e = (Entity) o;
			return e.getName().equals(name);
		}
		return false;
	}
	
	public abstract void exit();
}
