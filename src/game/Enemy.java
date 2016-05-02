package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Enemy extends Entity
{
	private boolean begin;
	private Player p;
	public Vector2f target;
	private BezierCurve bc;
	private final float MAX_SPEED = 10f, ACCEL = 0.5f; //3.75f;
	private final float MAX_DIST_THRESH = 100f, MIN_DIST_THRESH = 4f;

	public Enemy(Game g, String name, Image image, Shape col, Vector2f pos)
	{
		super(g, name, image, col, pos);
		begin = false;
		p = (Player) g.getEntity("player");
	}

	public void update(GameContainer gc, int delta)
	{
		if(!begin)
		{
			if(p.getSpeed() != 0f)
				begin = true;
		}
		else
		{
			if(target == null || bc.isDone() || pos.distanceSquared(pos) >= MAX_DIST_THRESH || pos.distanceSquared(target) <= (MIN_DIST_THRESH * MIN_DIST_THRESH))
			{
				target = p.getPos().add(p.getDir().scale(p.getNormalCollisionShape().getWidth()*2f));
				bc = new BezierCurve(pos,new Vector2f(pos.x,target.y),target);
				speed *= .85f;
				//System.out.println(pos + " " + target);
			}
			speed+=ACCEL;
			if(speed>MAX_SPEED)
				speed=MAX_SPEED;
			
			Vector2f newDir = bc.increment(bc.getLength() * (speed/MAX_SPEED) * 0.0101f).sub(pos);
			setDir(newDir.copy().normalise());
			pos.add(newDir);

			boolean hitWall=false;
			if(pos.x < 0)
			{
				pos.x = 1;
				dir.x = -dir.x;
				speed -= 0.1f;
				if(speed < 0)
					speed = 0;
				hitWall = true;
			}
			if(pos.x > (float)(gc.getWidth() - image.getWidth()))
			{
				pos.x = (float)(gc.getWidth() - image.getWidth() - 1);
				dir.x = -dir.x;
				speed -= 0.1f;
				if(speed < 0)
					speed = 0;
				hitWall = true;
			}
			if(pos.y < 0)
			{
				pos.y = 1;
				dir.y = -dir.y;
				speed -= 0.1f;
				if(speed < 0)
					speed = 0;
				hitWall = true;
			}
			if(pos.y > (float)(gc.getHeight() - image.getHeight()))
			{
				pos.y = (float)(gc.getHeight() - image.getHeight() - 1);
				dir.y = -dir.y;
				speed -= 0.1f;
				if(speed < 0)
					speed = 0;
				hitWall = true;
			}

			if(hitWall)
			{
				//recalculate if hit wall
				target = null;
			}
		}
	}

	public void handleCollision(Entity collidable)
	{
		if(collidable instanceof Enemy)
		{
			Enemy e = (Enemy) collidable;
			//add half widths then subtract dist
			boolean xNeg = pos.x < e.getPos().x;
			boolean yNeg = pos.y < e.getPos().y;
			float xHalf = (col.getWidth()/2 + e.getNormalCollisionShape().getWidth()/2) - Math.abs(pos.x - e.getPos().x);
			float yHalf = (col.getHeight()/2 + e.getNormalCollisionShape().getHeight()/2) - Math.abs(pos.y - e.getPos().y);
			if(xHalf <= yHalf)
			{
				pos.x += (xNeg?-1:1)*xHalf;
			}
			else
			{
				pos.y += (yNeg?-1:1)*yHalf;
			}
			target = null;
		}
		else if(collidable instanceof Player)
		{
			game.reset();
		}
	}
	
	public void exit()
	{
	}
}
