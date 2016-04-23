import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import net.java.games.jogl.*;
import java.util.*;
import java.io.*;

class Player extends CollisionDetect
{
	public Player(Point3d cord, float dir)
	{

		this.cord = cord;
		this.dir = dir;
		dead = false;
	}

	// ignoring stepSize for BULLET and ROBOT
	public void moveOneStep(float stepSize)
	{
		Point3d cordTemp = new Point3d(cord.x, cord.y, cord.z);

		cordTemp.x = cord.x + stepSize*(float)Math.cos(Math.toRadians(dir));
        cordTemp.z = cord.z - stepSize*(float)Math.sin(Math.toRadians(dir));

		float wallNormal = dangerousWallNormal(cord.x, cord.z, dir);
		if (wallNormal == -1)
		{
			cord.x = cordTemp.x;
			cord.z = cordTemp.z;
		}
		return;
	}


	public float isCollide(CollisionDetect p)
	{
		if(p instanceof Robot)
		{
			if (distance(cord.x, cord.z, p.cord.x, p.cord.z) < 2)
			{
				return 10f;
			}
		}

		if(p instanceof Bullet)
		{
			if (distance(cord.x, cord.z, p.cord.x, p.cord.z) < 2)
			{
				if (p.hide)
					return 0;
				else
				{
					p.dir = (calcReflect(dir) + 180) % 360;
					return 50;
				}
			}
		}
		return 0;
	}

	// Player is not displayed.
	public void draw(GL gl, GLU glu, float t)
	{

	}
}