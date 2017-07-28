/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package winnerstech;


import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;


/**
 * SpinBot - a sample robot by Mathew Nelson.
 * <p/>
 * Moves in a circle, firing hard when an enemy is detected.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class AleDurTech extends AdvancedRobot {

	/**
	 * SpinBot's run method - Circle
	 */
	public void run() {
		// Set colors
		setBodyColor(new Color(140,0,0));
		setGunColor(Color.gray);
		setRadarColor(Color.yellow);
		setScanColor(Color.yellow);
		setBulletColor(Color.white);

		// Loop forever
		while (true) {
			// Tell the game that when we take move,
			// we'll also want to turn right... a lot.
			setTurnRight(10000);
			// Limit our speed to 5
			setMaxVelocity(5);
			// Start moving (and turning)
			ahead(10000);
			// Repeat.
		}
	}

	/**
	 * onScannedRobot: Fire hard!
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
		stop();
		ahead(50);
		resume();
		scan();
	}

	/**
	 * onHitByBullet:  Turn perpendicular to the bullet, and move a bit.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnRight(90);
		back(50);
	}

	/**
	 * onHitRobot:  If it's our fault, we'll stop turning and moving,
	 * so we need to turn again to keep spinning.
	 */
	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() > -10 && e.getBearing() < 10) {
			fire(3);
		}
		if (e.isMyFault()) {
			turnRight(10);
		}
	}
}
