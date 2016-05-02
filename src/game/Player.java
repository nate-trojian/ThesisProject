package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Entity
{
	public final float MAX_SPEED = 4f, ACCEL = 1f;
	public int score;

	public Player(Game g, String name, Image image, Shape col)
	{
		super(g, name, image, col);
		score = 0;
	}

	public Player(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		super(g, name, image, col, pos);
		score = 0;
	}

	public void update(GameContainer gc, int delta)
	{
		Vector2f newDir = new Vector2f();
		float currSpeed = speed;
		Input in = gc.getInput();
		if(in.isKeyDown(Input.KEY_A) || in.isKeyDown(Input.KEY_LEFT))
		{
			newDir.add(new Vector2f(-1, 0));
		}
		if(in.isKeyDown(Input.KEY_D)  || in.isKeyDown(Input.KEY_RIGHT))
		{
			newDir.add(new Vector2f(1, 0));
		}
		if(in.isKeyDown(Input.KEY_W)  || in.isKeyDown(Input.KEY_UP))
		{
			newDir.add(new Vector2f(0, -1));
		}
		if(in.isKeyDown(Input.KEY_S)  || in.isKeyDown(Input.KEY_DOWN))
		{
			newDir.add(new Vector2f(0, 1));
		}

		if(newDir.x != 0f || newDir.y != 0f)
		{
			speed += ACCEL;
			speed = (dir.scale(currSpeed)).add(newDir.scale(speed)).length();
			if(speed > MAX_SPEED)
				speed = MAX_SPEED;
		}
		else if(newDir.x == 0 && newDir.y == 0)
		{
			speed -= 0.0875f;
			if(speed < 0)
				speed = 0;
		}

		dir = dir.scale(MAX_SPEED).add(newDir).normalise();
		if(speed == 0)
			dir = new Vector2f();
		pos = pos.add(dir.scale(speed));
		//image.setRotation((float)dir.getTheta());

		if(pos.x < 0)
		{
			pos.x = 1;
			dir.x = -dir.x;
			speed -= 0.1f;
			if(speed < 0)
				speed = 0;
		}
		if(pos.x > (float)(gc.getWidth() - image.getWidth()))
		{
			pos.x = (float)(gc.getWidth() - image.getWidth() - 1);
			dir.x = -dir.x;
			speed -= 0.1f;
			if(speed < 0)
				speed = 0;
		}
		if(pos.y < 0)
		{
			pos.y = 1;
			dir.y = -dir.y;
			speed -= 0.1f;
			if(speed < 0)
				speed = 0;
		}
		if(pos.y > (float)(gc.getHeight() - image.getHeight()))
		{
			pos.y = (float)(gc.getHeight() - image.getHeight() - 1);
			dir.y = -dir.y;
			speed -= 0.1f;
			if(speed < 0)
				speed = 0;
		}
	}

	public void handleCollision(Entity collidable)
	{
		if(collidable instanceof Enemy)
		{
			game.reset();
		}
		else
		{
			incrementScore();
			try
			{
				game.removeEntity(collidable);
			}
			catch(SlickException se)
			{
				se.printStackTrace();
			}
		}
	}

	public int getScore()
	{
		return score;
	}

	public void incrementScore()
	{
		score++;
	}

	public void exit()
	{
	}
}
