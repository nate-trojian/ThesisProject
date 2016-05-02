package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class AdaptiveBotPlayer extends Player
{
	Enemy e1 = null, e2 = null;
	Objective obj = null;
	float VIEW_ANGLE = 25f, E_PROX = 10f;
	Vector2f lastMove;
	
	public AdaptiveBotPlayer(Game g, String name, Image image, Shape col,Vector2f pos)
	{
		super(g, name, image, col, pos);
		lastMove = new Vector2f();
	}
	
	public void update(GameContainer gc, int delta)
	{
		/*
		 * If objective uncontested, go for it
		 * If enemy is inbetween you and obj, sidestep until they aren't - play with thresh value
		 * Move toward center value
		 * 
		 */
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
				speed += ACCEL;
				if(speed > MAX_SPEED)
					speed = MAX_SPEED;
				
				Vector2f objDir = obj.getPos().copy().sub(pos).normalise().scale(speed),
						e1Dir = e1.getPos().copy().sub(pos).normalise().scale(speed),
						e2Dir = e2.getPos().copy().sub(pos).normalise().scale(speed);
				
				//System.out.print(obj.getPos() + " ");
				
				Vector2f ret = new Vector2f();
				
				if(Math.abs((objDir.getTheta() - e1Dir.getTheta() + 180) % 360 - 180) <= VIEW_ANGLE ||
						(new Line(pos, obj.getPos())).distanceSquared(e1.getPos()) <= (E_PROX*E_PROX))
				{
					//Enemy is in way of obj - sidestep necessary - keep moving in sidestep if available
					Vector2f sideStep1 = new Vector2f(e1Dir.getTheta()+90).scale(speed);
					Vector2f sideStep2 = new Vector2f(e1Dir.getTheta()-90).scale(speed);
					if((sideStep1.getTheta() - e2Dir.getTheta() + 180) % 360 - 180 <= 0)
					{
						//Well darn
						ret = sideStep2;
						//System.out.println("2");
					}
					else
					{
						ret = sideStep1;
						//System.out.println("1");
					}
				}
				else if(Math.abs((objDir.getTheta() - e2Dir.getTheta() + 180) % 360 - 180) <= VIEW_ANGLE ||
						(new Line(pos, obj.getPos())).distanceSquared(e2.getPos()) <= (E_PROX*E_PROX))
				{
					//Enemy is in way of obj - sidestep necessary
					Vector2f sideStep1 = new Vector2f(e2Dir.getTheta()+90).normalise().scale(speed);
					Vector2f sideStep2 = new Vector2f(e2Dir.getTheta()-90).normalise().scale(speed);
					if((sideStep1.getTheta() - e1Dir.getTheta() + 180) % 360 - 180 <= 0)
					{
						//Well darn
						ret = sideStep2;
						//System.out.println("2");
					}
					else
					{
						ret = sideStep1;
						//System.out.println("1");
					}
				}
				else
				{
					//Nothing has been chosen
					ret = objDir;
					//System.out.println("0");
				}
				lastMove = ret;
				setDir(ret.copy().normalise());
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