import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import net.java.games.jogl.*;
import java.util.*;
import java.io.*;

abstract class CollisionDetect
{
	protected Point3d cord;
	protected float dir;
	protected boolean dead;
	protected float scale = 1;
	protected float step;
	protected boolean hide = false;

	protected GLUquadric quadric;

	//Accessors
	public float getScale()		{ return scale;	}
	public Point3d getCord()	{ return cord;	}
	public float getDir()		{ return dir;	}
	public boolean getDead()	{ return dead;	}
	public boolean getHide()	{ return hide;	}

	public void setDir(float newDir)
	{
		dir = newDir;
	}

    // Moves the object one step in its current direction.
	// Ignores stepSize for BULLET and ROBOT
	abstract public void moveOneStep(float stepSize);

	// Checks if the objects is within a certain distance of the
	// passed-in object.  If so, returns appropriate health damage.
	// Otherwise, returns zero.
	abstract public float isCollide(CollisionDetect p);

	// Returns distance between two objects.
	public float distance(float x1, float z1, float x2, float z2)
	{
		return ((float)Math.sqrt((x1 - x2) * (x1 - x2) + (z1 - z2) * (z1 - z2)));
	}

	// Draws object.
	abstract public void draw(GL gl, GLU glu, float t);

	// Returns new direction if an object is to bounce off another object
	// with passed-in wall normal.
	public float calcReflect(float wallNormal)
	{
		return ( (dir + 180 - ( ( dir - wallNormal) * 2) ) % 360 );
	}

	// Returns -1 if object is in bounds, or the wall normal if it is not.
	//
	// These if statements correspond to areas near walls in the mansion.
	// If an object enters an area, and it is moving toward a wall, this
	// method returns the normal of the wall the object is about to hit.
	// Otherwise, it returns -1.

	public float dangerousWallNormal(float x, float z, float dir)
	{
		float wallNormal = -1f;
		//1-10
		if (x > -30 && x < -28 	&& z > -30 	&& z < 130) wallNormal = 180f;
		else if (x > -28 	&& x < 118 	&& z > -30 	&& z < -28)	wallNormal = 90f;
		else if (x > 118 	&& x < 120 	&& z > -30 	&& z < 130)	wallNormal = 0f;
		else if (x > -28 	&& x < 118 	&& z > 128 	&& z < 130)	wallNormal = 270f;
		else if (x > 90 	&& x < 92 	&& z > -2 	&& z < 2)	wallNormal = 180f;
		else if (x > 90 	&& x < 92 	&& z > 5  	&& z < 35)	wallNormal = 180f;
		else if (x > 90 	&& x < 92 	&& z > 38 	&& z < 42)	wallNormal = 180f;
		else if (x > 90 	&& x < 92 	&& z > 45 	&& z < 75)	wallNormal = 180f;
		else if (x > 90 	&& x < 92 	&& z > 78 	&& z < 82)	wallNormal = 180f;
		else if (x > 90 	&& x < 92 	&& z > 85 	&& z < 100)	wallNormal = 180f;
		//11-20
		else if (x > 100 	&& x < 102 	&& z > 85 	&& z < 92) 	wallNormal = 90f;
		else if (x > 38  	&& x < 82	&& z > 100 	&& z < 102) wallNormal = 90f;
		else if (x > 25  	&& x < 35 	&& z > 100 	&& z < 102) wallNormal = 90f;
		else if (x > -2  	&& x < 22 	&& z > 100 	&& z < 102) wallNormal = 90f;
		else if (x > -2  	&& x < 0 	&& z > 98 	&& z < 100) wallNormal = 0f;
		else if (x > -2  	&& x < 0 	&& z > 65 	&& z < 95) 	wallNormal = 0f;
		else if (x > -2  	&& x < 0 	&& z > 58 	&& z < 62) 	wallNormal = 0f;
		else if (x > -2  	&& x < 0 	&& z > 25 	&& z < 55) 	wallNormal = 0f;
		else if (x > -2  	&& x < 0 	&& z > 18 	&& z < 22) 	wallNormal = 0f;
		else if (x > -2  	&& x < 0 	&& z > 0 	&& z < 15) 	wallNormal = 0f;
		//21-30
		else if (x > -2  	&& x < 5 	&& z > -2 	&& z < 0) 	wallNormal = 270f;
		else if (x > 8   	&& x < 52 	&& z > -2 	&& z < 0) 	wallNormal = 270f;
		else if (x > 55  	&& x < 65 	&& z > -2 	&& z < 0) 	wallNormal = 270f;
		else if (x > 68		&& x < 90 	&& z > -2 	&& z < 0) 	wallNormal = 270f;
		else if (x > 68		&& x < 90 	&& z > 0 	&& z < 2) 	wallNormal = 90f;
		else if (x > 88 	&& x < 90 	&& z > 5 	&& z < 35)	wallNormal = 0f;
		else if (x > 85 	&& x < 88 	&& z > 18 	&& z < 20) 	wallNormal = 270f;
		else if (x > 38		&& x < 82 	&& z > 18 	&& z < 20) 	wallNormal = 270f;
		else if (x > 60 	&& x < 62 	&& z > 0 	&& z < 15) 	wallNormal = 180f;
		else if (x > 62 	&& x < 65 	&& z > 0 	&& z < 2) 	wallNormal = 90f;
		//31-40
		else if (x > 38 	&& x < 82 	&& z > 20 	&& z < 22) 	wallNormal = 90f;
		else if (x > 85 	&& x < 88 	&& z > 20 	&& z < 22) 	wallNormal = 90f;
		// no 33
		else if (x > 68 	&& x < 90 	&& z > 38 	&& z < 40) 	wallNormal = 270f;
		else if (x > 62 	&& x < 65 	&& z > 38 	&& z < 40) 	wallNormal = 270f;
		else if (x > 60 	&& x < 62 	&& z > 25 	&& z < 55) 	wallNormal = 180f;
		else if (x > 62 	&& x < 65 	&& z > 40 	&& z < 42) 	wallNormal = 90f;
		else if (x > 68 	&& x < 90 	&& z > 40 	&& z < 40) 	wallNormal = 90f;
		else if (x > 88 	&& x < 90 	&& z > 45 	&& z < 75) 	wallNormal = 0f;
		else if (x > 85 	&& x < 88 	&& z > 58 	&& z < 60) 	wallNormal = 270f;
		//41-50
		else if (x > 38 	&& x < 82 	&& z > 58 	&& z < 60) 	wallNormal = 270f;
		else if (x > 38 	&& x < 82 	&& z > 60 	&& z < 62) 	wallNormal = 90f;
		else if (x > 85 	&& x < 88 	&& z > 60 	&& z < 62) 	wallNormal = 90f;
		else if (x > 68 	&& x < 90 	&& z > 78 	&& z < 80) 	wallNormal = 270f;
		else if (x > 62 	&& x < 65 	&& z > 78 	&& z < 80) 	wallNormal = 270f;
		else if (x > 60 	&& x < 62 	&& z > 65 	&& z < 95) 	wallNormal = 180f;
		else if (x > 62 	&& x < 65 	&& z > 80 	&& z < 82) 	wallNormal = 90f;
		else if (x > 68 	&& x < 90 	&& z > 80 	&& z < 82) 	wallNormal = 90f;
		else if (x > 88 	&& x < 90 	&& z > 85 	&& z < 100) wallNormal = 0f;
		else if (x > 85 	&& x < 88 	&& z > 98 	&& z < 100) wallNormal = 270f;
		//51-60
		else if (x > 38 	&& x < 82 	&& z > 98 	&& z < 100) wallNormal = 270f;
		else if (x > 8 		&& x < 52 	&& z > 0 	&& z < 2) 	wallNormal = 90f;
		else if (x > 55 	&& x < 58 	&& z > 0 	&& z < 2) 	wallNormal = 90f;
		else if (x > 58 	&& x < 60 	&& z > 0 	&& z < 15)	wallNormal = 0f;
		else if (x > 32 	&& x < 35 	&& z > 18 	&& z < 20) 	wallNormal = 270f;
		else if (x > 30 	&& x < 32 	&& z > 5 	&& z < 35) 	wallNormal = 180f;
		else if (x > 32 	&& x < 35 	&& z > 20 	&& z < 22) 	wallNormal = 90f;
		else if (x > 58 	&& x < 60 	&& z > 25 	&& z < 55) 	wallNormal = 0f;
		else if (x > 55 	&& x < 58 	&& z > 38 	&& z < 40) 	wallNormal = 270f;
		else if (x > 8 		&& x < 52 	&& z > 38 	&& z < 40) 	wallNormal = 270f;
		//61-70
		else if (x > 8 		&& x < 52 	&& z > 40 	&& z < 42) 	wallNormal = 90f;
		else if (x > 55 	&& x < 58 	&& z > 40 	&& z < 42) 	wallNormal = 90f;
		else if (x > 32 	&& x < 35 	&& z > 58 	&& z < 60) 	wallNormal = 270f;
		else if (x > 30 	&& x < 32 	&& z > 45 	&& z < 75) 	wallNormal = 180f;
		else if (x > 32 	&& x < 35 	&& z > 60 	&& z < 62) 	wallNormal = 90f;
		else if (x > 58 	&& x < 60 	&& z > 65 	&& z < 95) 	wallNormal = 0f;
		else if (x > 55 	&& x < 58 	&& z > 78 	&& z < 80) 	wallNormal = 270f;
		else if (x > 8 		&& x < 52 	&& z > 78 	&& z < 80) 	wallNormal = 270f;
		else if (x > 8 		&& x < 52 	&& z > 80 	&& z < 82) 	wallNormal = 90f;
		else if (x > 55 	&& x < 58 	&& z > 80 	&& z < 82) 	wallNormal = 90f;
		//71-80
		else if (x > 32 	&& x < 35 	&& z > 98 	&& z < 100) wallNormal = 270f;
		else if (x > 30 	&& x < 32 	&& z > 85 	&& z < 100) wallNormal = 180f;
		else if (x > 0 		&& x < 2 	&& z > 0 	&& z < 15) 	wallNormal = 180f;
		else if (x > 2 		&& x < 5 	&& z > 0 	&& z < 2) 	wallNormal = 90f;
		else if (x > 28 	&& x < 30 	&& z > 5 	&& z < 35) 	wallNormal = 0f;
		else if (x > 25 	&& x < 28 	&& z > 18 	&& z < 20) 	wallNormal = 270f;
		else if (x > 0 		&& x < 22 	&& z > 18 	&& z < 20) 	wallNormal = 270f;
		else if (x > 0 		&& x < 22 	&& z > 20 	&& z < 22) 	wallNormal = 90f;
		else if (x > 25 	&& x < 28 	&& z > 20 	&& z < 22) 	wallNormal = 90f;
		else if (x > 2		&& x < 5 	&& z > 38 	&& z < 40) 	wallNormal = 270f;
		//81-90
		else if (x > 0 		&& x < 2 	&& z > 25	&& z < 55) 	wallNormal = 180f;
		else if (x > 2 		&& x < 5 	&& z > 40 	&& z < 42) 	wallNormal = 90f;
		else if (x > 28 	&& x < 30 	&& z > 45 	&& z < 75) 	wallNormal = 0f;
		else if (x > 25 	&& x < 28 	&& z > 58 	&& z < 60) 	wallNormal = 270f;
		else if (x > 0 		&& x < 22 	&& z > 58 	&& z < 60) 	wallNormal = 270f;
		else if (x > 0 		&& x < 22 	&& z > 60 	&& z < 62) 	wallNormal = 90f;
		else if (x > 25 	&& x < 28 	&& z > 60 	&& z < 62) 	wallNormal = 90f;
		else if (x > 2		&& x < 5 	&& z > 78 	&& z < 80) 	wallNormal = 270f;
		else if (x > 0 		&& x < 2 	&& z > 65 	&& z < 95) 	wallNormal = 180f;
		else if (x > 2 		&& x < 5 	&& z > 80 	&& z < 82) 	wallNormal = 90f;
		//91-93
	    else if (x > 28 	&& x < 30 	&& z > 85 	&& z < 100) wallNormal = 0f;
		else if (x > 25 	&& x < 28 	&& z > 98 	&& z < 100) wallNormal = 270f;
		else if (x > 0 		&& x < 22 	&& z > 98 	&& z < 100) wallNormal = 270f;
		else if (x > 120 	&& x < 122 	&& z > -30 	&& z < 130) wallNormal = 180f;
		else if (x > -32	&& x < -30 	&& z > -30 	&& z < 130) wallNormal = 0f;
		else if (x > -30	&& x < 120 	&& z > -32 	&& z < -30) wallNormal = 270f;
		else if (x > -30	&& x < 120 	&& z > 130 	&& z < 132) wallNormal = 90f;

		if ((wallNormal == 0   && dir > 90  && dir < 270) ||
			(wallNormal == 90  && dir > 180 && dir < 360) ||
			(wallNormal == 180 && ( (dir > 270 && dir < 360) || (dir >=0 && dir <90) ) ) ||
        	(wallNormal == 270 && dir > 0  && dir < 180))
        	wallNormal = -1f;

		return wallNormal;
	}
}