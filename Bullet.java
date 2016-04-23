import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import net.java.games.jogl.*;
import java.util.*;
import java.io.*;

class Bullet extends CollisionDetect
{
	private int bulletCounter = 0;

	public int getBulletCounter()	{ return bulletCounter;	}

	public Bullet(Point3d cord, float dir)
	{

		this.cord = cord;
		this.dir = dir;
		dead = false;
		hide = true; //Prevents bullet from displaying when it is too close to player
	}

	// ignoring stepSize for BULLET and ROBOT
	public void moveOneStep(float stepSize)
	{
		// The bullet disapears after 200 steps.
		bulletCounter++;
		if (bulletCounter == 200)
			dead = true;
		// The first step, must be larger, or the bullet will collide with the player.
		if (hide)
		{
			stepSize = 3;
			hide = false;
		}
		else
			stepSize = .3f;

		Point3d cordTemp = new Point3d(cord.x, cord.y, cord.z);

		cord.x = cord.x + stepSize*(float)Math.cos(Math.toRadians(dir));
        cord.z = cord.z - stepSize*(float)Math.sin(Math.toRadians(dir));

		float wallNormal = dangerousWallNormal(cord.x, cord.z, dir);
			if (wallNormal != -1)
				dir = calcReflect(wallNormal);
		return;
	}

	public float isCollide(CollisionDetect p)
	{
		if(p instanceof Robot)
		{
			if (distance(cord.x, cord.z, p.cord.x, p.cord.z) < 2)
			{
					p.dead = true;
					dead = true;
					return 0;
			}
		}
		return 0;
	}

	public void draw(GL gl, GLU glu, float t)
	{
		quadric = glu.gluNewQuadric();
		gl.glPushMatrix();
			gl.glColor3f(1f,0f,0f);
			gl.glTranslatef(cord.x, cord.y, cord.z);
			glu.gluSphere(quadric, .5, 10, 10);
		gl.glPopMatrix();
	}
}