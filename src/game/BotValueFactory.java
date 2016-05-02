package game;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class BotValueFactory
{
	Robot r;
	Game g;
	public static void main(String[] args)
	{
		BotValueFactory bvf = new BotValueFactory();
	}

	public BotValueFactory()
	{
		try
		{
			r = new Robot();
			AppGameContainer app = new AppGameContainer((g = new Game()));

			// Application properties
			app.setDisplayMode(800, 600, false);
			app.setTargetFrameRate(60);
			//app.setSmoothDeltas(true);
			System.out.println("HERE");
			ArrayList<Thread> threads = new ArrayList<Thread>();
			threads.add(new Thread(new GameTask(app)));
			threads.add(new Thread(new RobotTask(g)));
			for(Thread t : threads)
				t.start();
			int running=0;
			do
			{
				running=0;
				for(Thread t : threads)
				{
					if(t.isAlive())
						running++;
				}
			}while(running>0);
			System.out.println("FINISHED");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void keyPress(int key)
	{
		r.keyPress(key);
		r.delay(10);
		r.keyRelease(key);
		//System.out.println("pressed: " + key);
	}

	private class GameTask implements Runnable
	{
		AppGameContainer app;
		boolean doOnce = false;
		public GameTask(AppGameContainer app)
		{
			this.app = app;
		}

		public void run()
		{
			if(!doOnce)
			{
				doOnce = true;
				try
				{
					app.start();
					//System.out.println("BLAH");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private class RobotTask implements Runnable
	{
		Field begun, val1, val2, val3, score;
		Game g;
		SimpleBotPlayer s;
		float param1=0, param2=0, param3=0, highParam1=0, highParam2=0, highParam3=0, highScore=0, count;
		int trials;
		public RobotTask(Game g)
		{
			this.g = g;
			trials = 0;
			try
			{
				begun = Game.class.getDeclaredField("begun");
				begun.setAccessible(true);
				val1 = SimpleBotPlayer.class.getDeclaredField("OBJ_SCORE");
				val1.setAccessible(true);
				val2 = SimpleBotPlayer.class.getDeclaredField("ENEMY_SCORE");
				val2.setAccessible(true);
				val3 = SimpleBotPlayer.class.getDeclaredField("SIDE_STEP");
				val3.setAccessible(true);
				score = Player.class.getDeclaredField("score");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//System.out.println("STUFF");
		}

		public void run()
		{
			while(g.entityList == null)
			{
				//System.out.println("Running");
			}
			while(g.entityList != null)
			{
				try
				{
					if(!begun.getBoolean(g))
					{
						boolean init = false;
						if(s!=null)
						{
							//We've already done a trial
							count += score.getInt(s);
							trials++;
							if(trials%5==0)
							{
								count /= 5f;
								if(count > highScore)
								{
									highScore = count;
									highParam1 = param1;
									highParam2 = param2;
									highParam3 = param3;
									System.err.println(highParam1 + " " + highParam2 + " " + highParam3);
								}
								count=0;
								param1 += (float) Math.random() * 10 - 5;
								val1.setFloat(s, param1);
								param2 += (float) Math.random() * 10 - 5;
								val2.setFloat(s, param2);
								param3 += (float) Math.random() * 10 - 5;
								val3.setFloat(s, param3);
								
								/*param1 = (float) Math.random() * 100;
								val1.setFloat(s, param1);
								param2 = (float) Math.random() * -100;
								val2.setFloat(s, param2);
								param3 = (float) Math.random() * 100;
								val3.setFloat(s, param3);
								*/
							}
						}
						else
						{
							init = true;
						}
						while(g.entityList == null || (s = (SimpleBotPlayer) g.getEntity("player")) == null)
						{

						}
						if(init)
						{
							param1 = val1.getFloat(s);
							param2 = val2.getFloat(s);
							param3 = val3.getFloat(s);
						}

						r.delay(100);
						keyPress(KeyEvent.VK_SPACE);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
