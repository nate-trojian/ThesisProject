package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Game extends BasicGame
{
    public ArrayList<Entity> entityList;
    public HashSet<Entity> toRemove, toAdd;
    public HashBucket<Entity, String, Entity> collisions;
    private int gameTime;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private final int BLOCK_LENGTH = 32;
    private final int GRID_WIDTH = 18; //Subtract 1 since it spawns based on upper left corner
    private final int GRID_HEIGHT = 15; //Subtract 1 since it spawns based on upper left corner
    private final int OBJ_OFFSET_X = 96;
    private final int OBJ_OFFSET_Y = 36;
    private boolean debug = false, begun, log, replay;
    private Random rand;
    private Writer out = null;
    private Scanner objScan = null;
    private ArrayList<Vector2f> objList;
    private String sep = System.getProperty("file.separator");
    private char nl = '\n';
    private int trialNum;
    //private int[] answers = {4,2,3,2,3,4,3,2,4};
    //private int[] answers = {1};
    private int[] answers = {0,4,3};
    //private int[] answers = {0,4};

    public Game()
    {
        super("THESIS PROJECT");
    }

    public static void main(String args[]) throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new Game());

        // Application properties
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.setTargetFrameRate(60);
        //app.setSmoothDeltas(true);

        app.start();
    }

    public void init(GameContainer gc) throws SlickException
    {
        trialNum=0;
        initGame();
        gc.setShowFPS(debug);
    }

    public void initGame() throws SlickException
    {
        entityList = new ArrayList<Entity>();
        collisions = new HashBucket<Entity, String, Entity>();
        toRemove = new HashSet<Entity>();
        toAdd = new HashSet<Entity>();
        switch(answers[trialNum])
        {
            case 0:
                entityList.add(new Player(this, "player", new Image("data/img/player.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                        new Vector2f(Math.round((GRID_WIDTH+1) * (.65)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (.5)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
                break;
            case 1:
                entityList.add(new KeyLogPlayer(this, "player", new Image("data/img/player.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                        new Vector2f(Math.round((GRID_WIDTH+1) * (.65)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (.5)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
                break;
            case 2:
                //Should give 0,1,2
                entityList.add(new LogReaderPlayer(this, "player", new Image("data/img/player.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                        new Vector2f(Math.round((GRID_WIDTH+1) * (.65)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (.5)) * BLOCK_LENGTH + OBJ_OFFSET_Y), (int)Math.floor(((double)trialNum)/3D)));
                System.out.println((int)Math.floor(((double)trialNum)/3D));
                break;
            case 3:
                entityList.add(new SimpleBotPlayer(this, "player", new Image("data/img/player.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                        new Vector2f(Math.round((GRID_WIDTH+1) * (.65)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (.5)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
                break;
            case 4:
                entityList.add(new AdaptiveBotPlayer(this, "player", new Image("data/img/player.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                        new Vector2f(Math.round((GRID_WIDTH+1) * (.65)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (.5)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
                break;
        }
        entityList.add(new Enemy(this, "enemy1", new Image("data/img/enemy.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                new Vector2f(Math.round((GRID_WIDTH+1) * (1f/6f)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (1f/3f)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
        entityList.add(new Enemy(this, "enemy2", new Image("data/img/enemy.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH),
                new Vector2f(Math.round((GRID_WIDTH+1) * (1f/6f)) * BLOCK_LENGTH + OBJ_OFFSET_X, Math.round((GRID_HEIGHT+1) * (2f/3f)) * BLOCK_LENGTH + OBJ_OFFSET_Y)));
        rand = new Random(System.currentTimeMillis());

        gameTime = 0;
        log = entityList.get(0) instanceof KeyLogPlayer;
        replay = entityList.get(0) instanceof LogReaderPlayer;
        if(log)
        {
            try
            {
                File temp = new File("." + sep + "data" + sep + "log" + sep + "obj" + ((KeyLogPlayer)entityList.get(0)).fileNum + ".in");
                out = new BufferedWriter(new FileWriter(temp));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(replay)
        {
            try
            {
                File temp = new File("." + sep + "data" + sep + "log" + sep + "obj" + ((LogReaderPlayer)entityList.get(0)).logNum + ".in");
                objScan = new Scanner(temp);
                objList = new ArrayList<Vector2f>();
                while(objScan.hasNextLine())
                {
                    String line = objScan.nextLine();
                    String[] parts = line.split(" ");
                    objList.add(new Vector2f(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
                }
                objScan.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        genNewObj();

        for(Entity e : entityList)
            collisions.addBucket(e);
    }

    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        if(debug)
            renderDebug(gc, g);
        for(Entity e: entityList)
        {
            e.render(g);
        }
    }

    public void renderDebug(GameContainer gc, Graphics g) throws SlickException
    {
        Color temp = g.getColor();

        //draw area where obj can spawn
        g.setColor(Color.green);
        g.fillRect(OBJ_OFFSET_X, OBJ_OFFSET_Y, (GRID_WIDTH+1)*BLOCK_LENGTH, (GRID_HEIGHT+1)*BLOCK_LENGTH);

        g.setColor(Color.white);
        //draw vertical lines
        int numWidth = (int)Math.floor((double)gc.getScreenWidth()/(double)BLOCK_LENGTH);
        int offsetW = gc.getScreenWidth()%BLOCK_LENGTH;
        for(int i=0; i<numWidth; i++)
        {
            g.drawLine(offsetW + i*BLOCK_LENGTH, 0, offsetW + i*BLOCK_LENGTH, gc.getScreenHeight());
        }
        //draw horizontal lines
        int numHeight = (int)Math.floor((double)gc.getScreenHeight()/(double)BLOCK_LENGTH);
        int offsetH = gc.getScreenHeight()%BLOCK_LENGTH;
        for(int i=0; i<numHeight; i++)
        {
            g.drawLine(0, offsetH + i*BLOCK_LENGTH, gc.getScreenWidth(), offsetH + i*BLOCK_LENGTH);
        }

        //Draw enemies targets
        if(entityList.get(1) != null)
        {
            Enemy e = (Enemy) entityList.get(1);
            if(e.target != null)
                g.drawOval(e.target.x, e.target.y, 3, 3);
        }
        if(entityList.get(2) != null)
        {
            Enemy e = (Enemy) entityList.get(2);
            if(e.target != null)
                g.drawOval(e.target.x, e.target.y, 3, 3);
        }

        g.setColor(temp);
    }

    public void update(GameContainer gc, int delta) throws SlickException
    {
        //System.out.println("UPDATE BEGIN");

        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
        {
            exit();
            gc.exit();
        }

        if(!begun)
        {
            if(gc.getInput().isKeyDown(Input.KEY_SPACE))
            {
                begun = true;
            }
        }
        else
        {
            gameTime+=delta;

            for(Entity e: entityList)
            {
                e.update(gc, delta);
                for(int i=0; i<entityList.size(); i++)
                {
                    Entity e2 = entityList.get(i);

                    if(e.equals(e2))
                        continue;

                    if(e.isCollidingWith(e2) && !collisions.bucketContains(e2, e))
                    {
                        if(debug) System.out.println(e.getName() + " " + e2.getName());
                        collisions.addToBucket(e, e2.getName(), e2);
                        e.handleCollision(e2);
                    }
                }
            }

            //handleCollisions();

            entityList.removeAll(toRemove);
            collisions.dumpAllBuckets(toRemove);
            entityList.addAll(toAdd);
            collisions.addAllBuckets(toAdd);
            toRemove.clear();
            toAdd.clear();

            //System.out.println("UPDATE END");
        }
    }

    public void removeEntity(Entity e) throws SlickException
    {
        toRemove.add(e);
        if(e instanceof Objective)
        {
            genNewObj();
        }
    }

    public void genNewObj() throws SlickException
    {
        boolean valid = false;
        Vector2f newPos = new Vector2f();
        while(!valid)
        {
            valid = true;
            newPos = new Vector2f((float) Math.round(rand.nextFloat() * GRID_WIDTH) * BLOCK_LENGTH + OBJ_OFFSET_X, (float) Math.round(rand.nextFloat() * GRID_HEIGHT) * BLOCK_LENGTH + OBJ_OFFSET_Y);
            for(int i=0; i<3; i++)
            {
                Entity e = entityList.get(i);
                if(e.getCollisionShape().contains(newPos.x, newPos.y) || e.getCollisionShape().hasVertex(newPos.x, newPos.y))
                {
                    valid=false;
                    break;
                }
            }
        }
        if(debug) System.out.println("GEN @ " + newPos);
        if(log)
        {
            if(out != null)
            {
                try
                {
                    out.write(newPos.x + " " + newPos.y + nl);
                    out.flush();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        if(replay)
        {
            newPos = objList.remove(0);
        }
        toAdd.add(new Objective(this, "obj", new Image("data/img/obj.png"), new Rectangle(0f, 0f, BLOCK_LENGTH, BLOCK_LENGTH), newPos));
    }

    public void handleCollisions()
    {
        //Allows for more things to be done if necessary
        Iterator<Entity> keyIt = collisions.keyIterator();
        while(keyIt.hasNext())
        {
            Entity e1 = keyIt.next();
            Iterator<Entity> valIt = collisions.valuesIterator(e1);
            while(valIt.hasNext())
            {
                Entity e2 = valIt.next();
                e2.handleCollision(e1);
            }
        }
    }

    public void reset()
    {
        try
        {
            begun = false;
            trialNum = (trialNum+1)%answers.length;
            System.err.println("GAME OVER: " + ((Player) entityList.get(0)).getScore() + " TIME OF RUN: " + gameTime/1000 + "s");
            initGame();
        }
        catch(SlickException se)
        {
            se.printStackTrace();
        }
    }

    public Entity getEntity(String name)
    {
        for(int i=0; i<entityList.size(); i++)
        {
            if(entityList.get(i).getName().equals(name))
                return entityList.get(i);
        }
        return null;
    }

    public void exit()
    {
        for(Entity e : entityList)
        {
            e.exit();
        }
        if(log)
        {
            try
            {
                out.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public int getID()
    {
        return 0;
    }
}
