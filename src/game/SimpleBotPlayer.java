package game;

import java.util.HashMap;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class SimpleBotPlayer extends Player
{
	Enemy e1 = null, e2 = null;
	Objective obj = null;
	double TURN_THRESH = 8d;
	float[] SIDESTEP_RANGE = {15, 35};
	float OBJ_SCORE = 94.383644f, ENEMY_SCORE = -63.77149f, SIDE_STEP = 43.69832f, SAME_DIR = 7f; //79.89938 -71.86815 2.3317995
	Vector2f lastMove = new Vector2f();

	public SimpleBotPlayer(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		super(g, name, image, col, pos);
	}

	public void update(GameContainer gc, int delta)
	{
		if(e1 == null || e2 == null)
		{
			e1 = (Enemy) this.game.getEntity("enemy1");
			e2 = (Enemy) this.game.getEntity("enemy2");
			//System.out.println("HERE");
		}
		else
		{
			if(obj == null)
			{
				obj = (Objective) this.game.getEntity("obj");
				//System.out.println("THERE");
			}

			if(obj == null)
			{

			}
			else
			{
				speed+=ACCEL;
				if(speed>MAX_SPEED)
					speed=MAX_SPEED;
				
				Vector2f objDir = obj.getPos().copy().sub(pos).normalise().scale(speed),
						e1Dir = e1.getPos().copy().sub(pos).normalise().scale(speed),
						e2Dir = e2.getPos().copy().sub(pos).normalise().scale(speed);

				//Give weight to each direction
				HashMap<Vector2f, Integer> dirList = new HashMap<Vector2f, Integer>();
				for(float i=-speed; i<=speed; i++)
				{
					for(float j=-(speed - Math.abs(i)); j<= speed - Math.abs(i); j++)
					{
						Vector2f newDir = new Vector2f(i, j);
						if(i==0 && j==0)
							continue;
						int score = 0; //(int) Math.rint(5) - 2;
						/*
						 * In direction of enemy, decrease score
						 * In direction of obj, increase score
						 * Give score some random value
						 */
						//System.out.println(newDir.getTheta() + " " + objDir.getTheta());
						//Keep moving in the same direction, to avoid jerkiness
						if(Math.abs((newDir.getTheta() - lastMove.getTheta() + 180) % 360 - 180) <= TURN_THRESH)
						{
							score += SAME_DIR;
						}
						
						//Toward obj
						if(Math.abs((newDir.getTheta() - objDir.getTheta() + 180) % 360 - 180) <= TURN_THRESH)
						{
							score += OBJ_SCORE;
						}
						
						//Toward Enemy
						if(Math.abs((newDir.getTheta() - e1Dir.getTheta() + 180) % 360 - 180) <= TURN_THRESH)
						{
							score += ENEMY_SCORE;
						}
						if(Math.abs((newDir.getTheta() - e2Dir.getTheta() + 180) % 360 - 180) <= TURN_THRESH)
						{
							score += ENEMY_SCORE;
						}
						
						//Side step
						if(Math.abs((newDir.getTheta() - e1Dir.getTheta() + 180) % 360 - 180) >= 90 - SIDESTEP_RANGE[0] && Math.abs((newDir.getTheta() - e1Dir.getTheta() + 180) % 360 - 180) <= 90 + SIDESTEP_RANGE[1])
						{
							score += SIDE_STEP;
						}
						if(Math.abs((newDir.getTheta() - e2Dir.getTheta() + 180) % 360 - 180) >= 90 - SIDESTEP_RANGE[0] && Math.abs((newDir.getTheta() - e2Dir.getTheta() + 180) % 360 - 180) <= 90 + SIDESTEP_RANGE[1])
						{
							score += SIDE_STEP;
						}
						dirList.put(newDir, score);
					}
				}

				Iterator<Vector2f> it = dirList.keySet().iterator();
				float defMoveX = (float)Math.rint(2*speed) - speed;
				float defMoveY = (float)Math.rint(2*(speed - Math.abs(defMoveX))) - (speed - Math.abs(defMoveX));
				Vector2f ret = new Vector2f(defMoveX, defMoveY);
				int maxScore = 0;
				while(it.hasNext())
				{
					Vector2f next = it.next();
					int nextScore = dirList.get(next);
					if(nextScore > maxScore || ret == null)
					{
						ret = next;
						maxScore = nextScore;
					}
				}
				lastMove = ret;
				setDir(lastMove.copy().normalise());
				//System.out.println(ret);
				pos.add(ret);
				
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
			obj = null;
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
}
