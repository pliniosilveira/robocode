/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package winnerstech;


import robocode.HitRobotEvent;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.RobotDeathEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;


/**
 * Durvaletech - a sample robot by Mathew Nelson, and maintained by Flemming N. Larsen
 * <p/>
 * Moves around the outer edge with the gun facing in.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class DurvAleTech extends Robot {

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	int turnDirection = 1; // Clockwise or counterclockwise
	int count = 0; // Keeps track of how long we've
	// been searching for our target
	int dist = 50;
	double gunTurnAmt; // How much to turn our gun when searching
	String trackName = null; // Name of the robot we're currently tracking
	String trackNameOld = null; // Name of the robot we're currently tracking
	String friendName = "hpe.Aledurtech";
	
	
	private void moveToNearestWall() {
		double down = getY();
		double up = getBattleFieldHeight() - getY();
		out.println("up " + up);
		out.println("down " + down);
		
		double left = getX();
		double right = getBattleFieldWidth() - getX();
		out.println("left " + left);
		out.println("right " + right);
		
		out.println("getHeading() " + getHeading());
		
		if (Math.min(up,down) <= Math.min(left,right)) {
		
			if (up <= down) {
				out.println("up " + up);
				// go up
				if (getHeading() <= 180){
					turnLeft(getHeading());
				}else{
					turnRight(360 - getHeading());
				}
			}else{
				out.println("down " + down);
				//do down
				if (getHeading() <= 180){
					turnRight(180 - getHeading());
				}else{
					turnLeft(getHeading() - 180);
				}
			}
			
		}else{
		
			if (right <= left) {
				out.println("right " + right);
				// go right
				if (90 < getHeading() && getHeading() <= 270){
					turnLeft(getHeading() - 90);
				}else{
					if(getHeading() > 180) {
						turnRight(90 + (360 - getHeading()));
					}else{
						turnRight(90 - getHeading());
					}
				}
			}else{
				out.println("left " + left);
				//go left
				if (90 < getHeading() && getHeading() <= 270){
					turnRight(270 - getHeading());
				}else{
					if(getHeading() > 180) {
						turnLeft(getHeading() - 270);
					}else{
						turnLeft(getHeading() + 90);
					}
				}
			}
		
		}
		
	}
	
	/**
	 * run: Move around the walls
	 */
	public void run() {
		// Set colors
		setBodyColor(Color.red);
		setGunColor(Color.black);
		setRadarColor(new Color(100,0,100));
		setBulletColor(Color.white);
		setScanColor(new Color(100,0,100));

		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		//turnLeft(getHeading() % 90);
		moveToNearestWall();
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunRight(95);
		turnRight(90);

		while (true) {
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println("getHeading() " + getHeading());
			out.println(" ");
			if(getHeading() % 90 != 0) {
				//turnLeft(getHeading() % 90);
				moveToNearestWall();
				ahead(turnDirection*moveAmount);
				turnRight(turnDirection*90);
			}
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(turnDirection*moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnRight(turnDirection*90);
		}
	}

	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		// If he's in front of us, set back up a bit.
		//double bearingFromGun = e.getBearing() - 90;
		//turnDirection = (int)Math.signum(bearingFromGun);
		//out.println("turnDirection@ "+turnDirection);
		//if (e.getBearing() > -90 && e.getBearing() < 90) {
		//	back(100);
		//} // else he's in back of us, so set ahead a bit.
		//else {
			ahead(-turnDirection*20);
		//}
	}

	/**
	 * onHitByBullet:  Turn perpendicular to the bullet, and move a bit.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		//turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));

		ahead(dist);
		dist *= -1;
		scan();
	}

	/**
	 * onScannedRobot:  Here's the good stuff
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		// code to avoid shotting a friend.
		String packageName = e.getName().substring(0,e.getName().indexOf('.'));
		String MyPackageName = getName().substring(0,e.getName().indexOf('.'));
		out.println("Spotted a " + packageName);
		out.println("I'm " + MyPackageName);
		if( packageName.equals(MyPackageName) ){
			out.println("It's a friend don't shot");
			return;
		}
		
		fire(3);
		
		// Note that scan is called automatically when the robot is moving.
		// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
		// wall, so that we do not start moving up it until it's gone.
		if (peek) {
			scan();
		
			stop();
		
			double bearingFromGun = e.getBearing() - 90;
			//turnDirection = (int)Math.signum(bearingFromGun);
			//ahead(turnDirection*10);
			ahead(-10);
			fire(2);
			
			out.println("zcanned");
			out.println("e.getBearing() "+ e.getBearing());
			out.println("e.getBearing()-90 "+ bearingFromGun);
			out.println("Math.signum(e.getBearing()-90) " + Math.signum(bearingFromGun));
			out.println("turnDirection. "+turnDirection);
		
			resume();
		}
		
	}
	
}
