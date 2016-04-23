import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import net.java.games.jogl.*;
import java.util.*;
import java.io.*;

class Robot extends CollisionDetect
{
	public Robot(Point3d cord, float dir)
	{
		this.cord = cord;
		this.dir = dir;
		dead = false;
	}

	// ignoring stepSize for BULLET and ROBOT
	public void moveOneStep(float stepSize)
	{
		if(dead)
			stepSize =0f;
		else
			stepSize = .05f;

		cord.x = cord.x + stepSize*(float)Math.cos(Math.toRadians(dir));
        cord.z = cord.z - stepSize*(float)Math.sin(Math.toRadians(dir));

		float wallNormal = dangerousWallNormal(cord.x, cord.z, dir);
			if (wallNormal != -1)
				dir = calcReflect(wallNormal);
		return;
	}

	public float isCollide(CollisionDetect p)
	{
		return 0;
	}

	public void draw(GL gl, GLU glu, float t)
	{
		quadric = glu.gluNewQuadric();
		if(dead && scale > 0)
		{
			System.out.println(dead);
			scale -=.01f;
			drawRobot(gl, glu, 0);
		}
		else
			drawRobot(gl, glu, t);
	}

    public void drawRobot(GL gl, GLU glu, float t)
    {
		gl.glPushMatrix();

			gl.glTranslatef(cord.x, cord.y,cord.z);
			gl.glScalef(scale, scale, scale);
			gl.glTranslatef(-cord.x, -cord.y,-cord.z);

			gl.glTranslatef(cord.x, 0, cord.z);
			gl.glRotatef(dir, 0f, 1f, 0f);

			drawRobotAtOrigin(gl, glu, t);
		gl.glPopMatrix();
	}

    private void drawRobotAtOrigin(GL gl, GLU glu, float t)
    {
		gl.glPushMatrix();
			gl.glTranslatef(-.5f, 0, 0);

				//Eyes
			gl.glPushMatrix();
				gl.glTranslatef(1, 5.5f,-.2f);
				gl.glColor3f(1,.5f,.5f);
				glu.gluSphere(quadric,  .15f, 10, 10);
				gl.glTranslatef(0, 0,.4f);
				glu.gluSphere(quadric,  .15f, 10, 10);
			gl.glPopMatrix();

				//Head
			gl.glPushMatrix();
				gl.glTranslatef(.5f, 5f, 0);
				gl.glRotatef(-90f, 1f, 0f, 0f);
				gl.glColor3f(.55f,.55f,.55f);
				glu.gluCylinder(quadric, .5f, .5f, 1, 10, 10);
			gl.glPopMatrix();

				//Torso
			gl.glBegin(GL.GL_QUADS);
				gl.glColor3f(.6f,.6f,.6f);
				gl.glVertex3f(0, 5, -1);
				gl.glVertex3f(0, 5, 1);
				gl.glVertex3f(1, 5, 1);
				gl.glVertex3f(1, 5, -1);

				gl.glVertex3f(0, 3, -1);
				gl.glVertex3f(0, 3, 1);
				gl.glVertex3f(1, 3, 1);
				gl.glVertex3f(1, 3, -1);

				gl.glColor3f(.5f,.5f,.5f);
				gl.glVertex3f(0, 3, -1);
				gl.glVertex3f(0, 5, -1);
				gl.glVertex3f(0, 5, 1);
				gl.glVertex3f(0, 3, 1);

				gl.glColor3f(.2f,.2f,.2f);
				gl.glVertex3f(0, 3, -1);
				gl.glVertex3f(0, 5, -1);
				gl.glVertex3f(1, 5, -1);
				gl.glVertex3f(1, 3, -1);

				gl.glVertex3f(0, 3, 1);
				gl.glVertex3f(0, 5, 1);
				gl.glVertex3f(1, 5, 1);
				gl.glVertex3f(1, 3, 1);
			gl.glEnd();

		  //  gl.glEnable(gl.GL_TEXTURE_2D);
		  //      gl.glBindTexture(GL.GL_TEXTURE_2D, texture6);
				gl.glBegin(GL.GL_QUADS);
					gl.glTexCoord2f(1f,0f); gl.glVertex3f(1,  3, -1);
					gl.glTexCoord2f(1f,1f); gl.glVertex3f(1, 5, -1);
					gl.glTexCoord2f(0f,1f); gl.glVertex3f(1, 5, 1);
					gl.glTexCoord2f(0f,0f); gl.glVertex3f(1, 3, 1);
				gl.glEnd();
		  //  gl.glDisable(GL.GL_TEXTURE_2D);

			gl.glPushMatrix();
				gl.glTranslatef(.5f, 3, 0.5f);
				drawUpperLeg(gl, glu, t);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(.5f, 3, -.5f);
				drawUpperLeg(gl, glu, t + 0.5f);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(.25f, 5, 1);
				drawUpperArm(gl, glu, t);
			gl.glPopMatrix();

			gl.glPushMatrix();
				gl.glTranslatef(.25f, 5, -1.2f);
				drawUpperArm(gl, glu, t+ 0.5f);
			gl.glPopMatrix();

		gl.glPopMatrix();
	}

	private void drawUpperLeg(GL gl, GLU glu, float t)
	{
		gl.glPushMatrix();
			gl.glRotatef(30 * (float)Math.sin(2 * Math.PI * t), 0f, 0f, 1f);

			gl.glPushMatrix();
				gl.glTranslatef(0, -1.5f, 0);
				drawLowerLeg(gl, glu, t);
			gl.glPopMatrix();

			drawPrismAtOrigin(gl, glu);

		gl.glPopMatrix();
	}

	private void drawLowerLeg(GL gl, GLU glu, float t)
	{
		gl.glPushMatrix();
			gl.glRotatef(22.5f * (float)Math.sin(4 * Math.PI * (t - (1 / 8) ) ) - 22.5f, 0f, 0f, 1f);
			drawPrismAtOrigin(gl, glu);
		gl.glPopMatrix();
	}

	private void drawUpperArm(GL gl, GLU glu, float t)
	{
		gl.glPushMatrix();
			gl.glRotatef(-40 * (float)Math.sin(2 * Math.PI * t), 0f, 0f, 1f);

			gl.glPushMatrix();
				gl.glTranslatef(0, -1.5f, 0);
				drawLowerArm(gl, glu, t);
			gl.glPopMatrix();

			drawPrismAtOrigin(gl, glu);

		gl.glPopMatrix();
	}

	private void drawLowerArm(GL gl, GLU glu, float t)
	{
		gl.glPushMatrix();
			gl.glRotatef(-45 * (float)Math.sin(2 * Math.PI * t) + 45, 0f, 0f, 1f);
			drawPrismAtOrigin(gl, glu);
		gl.glPopMatrix();
	}

	private void drawPrismAtOrigin(GL gl, GLU glu)
	{
		gl.glBegin(GL.GL_QUADS);
			gl.glColor3f(.6f,.6f,.6f);
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(0, 0, .2f);
			gl.glVertex3f(.2f, 0, .2f);
			gl.glVertex3f(.2f, 0, 0);

			gl.glVertex3f(0, -1.5f, 0);
			gl.glVertex3f(0, -1.5f, .2f);
			gl.glVertex3f(.2f, -1.5f, .2f);
			gl.glVertex3f(.2f, -1.5f, 0);

			gl.glColor3f(.5f,.5f,.5f);
			gl.glVertex3f(0, -1.5f, 0);
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(.2f, 0, 0);
			gl.glVertex3f(.2f, -1.5f, 0);

			gl.glVertex3f(0, -1.5f, .2f);
			gl.glVertex3f(0, 0, .2f);
			gl.glVertex3f(.2f, 0, .2f);
			gl.glVertex3f(.2f, -1.5f, .2f);

			gl.glColor3f(.2f,.2f,.2f);
			gl.glVertex3f(.2f, -1.5f, 0);
			gl.glVertex3f(.2f, 0, 0);
			gl.glVertex3f(.2f, 0, .2f);
			gl.glVertex3f(.2f, -1.5f, .2f);

			gl.glVertex3f(0, -1.5f, 0);
			gl.glVertex3f(0, 0, 0);
			gl.glVertex3f(0, 0, .2f);
			gl.glVertex3f(0, -1.5f, .2f);
		gl.glEnd();
	}

}