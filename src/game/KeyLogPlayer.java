package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class KeyLogPlayer extends Player
{
	String sep = System.getProperty("file.separator");
	File log;
	Writer out;
	int fileNum;
	public KeyLogPlayer(Game g, String name, Image image, Shape col)
	{
		super(g, name, image, col);
		int count = 0;
		log = new File("." + sep + "data" + sep + "log" + sep + "log" + count + ".in");
		try
		{
			while(!log.createNewFile())
			{
				count++;
				log = new File("." + sep + "data" + sep + "log" + sep + "log" + count + ".in");
			}
			out = new BufferedWriter(new FileWriter(log));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public KeyLogPlayer(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		super(g, name, image, col, pos);
		int count = 0;
		log = new File("." + sep + "data" + sep + "log" + sep + "log" + count + ".in"); //Change back for mac
		try
		{
			while(!log.createNewFile())
			{
				count++;
				log = new File("." + sep + "data" + sep + "log" + sep + "log" + count + ".in");
			}
			out = new BufferedWriter(new FileWriter(log));
			fileNum = count;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
		
		/*
		 * -1 - no movement
		 * 0 - north
		 * 1 - north-east
		 * 2 - east
		 * 3 - south-east
		 * 4 - south
		 * 5 - south-west
		 * 6 - west
		 * 7 - north-west
		 */
		
		boolean[] pressed = new boolean[8];
		//for(int i=0; i<8; i++)
		//	pressed[i] = false;
		
		switch((int)newDir.x)
		{
			case -1:
				pressed[0] = false;
				pressed[1] = false;
				pressed[2] = false;
				pressed[3] = false;
				pressed[4] = false;
				pressed[5] = true;
				pressed[6] = true;
				pressed[7] = true;
				break;
			case 0:
				pressed[0] = true;
				pressed[1] = false;
				pressed[2] = false;
				pressed[3] = false;
				pressed[4] = true;
				pressed[5] = false;
				pressed[6] = false;
				pressed[7] = false;
				break;
			case 1:
				pressed[0] = false;
				pressed[1] = true;
				pressed[2] = true;
				pressed[3] = true;
				pressed[4] = false;
				pressed[5] = false;
				pressed[6] = false;
				pressed[7] = false;
				break;
		}
		switch((int)newDir.y)
		{
			case -1:
				//pressed[0] = true;
				//pressed[1] = true;
				pressed[2] = false;
				pressed[3] = false;
				pressed[4] = false;
				pressed[5] = false;
				pressed[6] = false;
				//pressed[7] = true;
				break;
			case 0:
				pressed[0] = false;
				pressed[1] = false;
				//pressed[2] = true;
				pressed[3] = false;
				pressed[4] = false;
				pressed[5] = false;
				//pressed[6] = true;
				pressed[7] = false;
				break;
			case 1:
				pressed[0] = false;
				pressed[1] = false;
				pressed[2] = false;
				//pressed[3] = true;
				//pressed[4] = true;
				//pressed[5] = true;
				pressed[6] = false;
				pressed[7] = false;
				break;
		}
		
		int b = -1;
		for(int i=0; i<8; i++)
		{
			if(pressed[i])
			{
				b=i;
				break;
			}
		}
		
		try
		{
			//System.out.println(b+1);
			out.write((b+1) + ",");
			out.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

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
	
	public void exit()
	{
		try
		{
			if(log.length() == 0L)
				log.deleteOnExit();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
