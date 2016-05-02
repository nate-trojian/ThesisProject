package game;

import java.io.File;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class LogReaderPlayer extends Player
{
	String sep = System.getProperty("file.separator");
	Scanner scan;
	int logNum;
	public LogReaderPlayer(Game g, String name, Image image, Shape col, int logNum)
	{
		super(g, name, image, col);
		try
		{
			scan = new Scanner(new File("." + sep + "data" + sep + "log" + sep + "log" + logNum + ".in"));
			scan.useDelimiter(",");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public LogReaderPlayer(Game g, String name, Image image, Shape col, Vector2f pos, int logNum)
	{
		super(g, name, image, col, pos);
		this.logNum = logNum;
		try
		{
			scan = new Scanner(new File("." + sep + "data" + sep + "log" + sep + "log" + logNum + ".in"));
			scan.useDelimiter(",");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void update(GameContainer gc, int delta)
	{
		if(!scan.hasNext())
		{
			System.err.println("Well this is awkward");
		}
		else
		{
			String tempNext = scan.next();
			int next = Integer.parseInt(tempNext);
			Vector2f newDir = new Vector2f();
			/*
			 * 0 - no movement
			 * 1 - north
			 * 2 - north-east
			 * 3 - east
			 * 4 - south-east
			 * 5 - south
			 * 6 - south-west
			 * 7 - west
			 * 8 - north-west
			 */
			switch(next)
			{
				case 1:
					newDir = new Vector2f(0,1);
					break;
				case 2:
					newDir = new Vector2f(1,1);
					break;
				case 3:
					newDir = new Vector2f(1,0);
					break;
				case 4:
					newDir = new Vector2f(1,-1);
					break;
				case 5:
					newDir = new Vector2f(0,-1);
					break;
				case 6:
					newDir = new Vector2f(-1,-1);
					break;
				case 7:
					newDir = new Vector2f(-1,0);
					break;
				case 8:
					newDir = new Vector2f(-1,1);
					break;
			}
			float currSpeed = speed;
			if(newDir.x != 0f || newDir.y != 0f)
			{
				speed += 1f;
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
	}
	
	public void exit()
	{
		scan.close();
	}
}
