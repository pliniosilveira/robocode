package psilveira;
import robocode.*;
import robocode.util.*;
import java.awt.Color;
import java.util.*;
import java.lang.Math;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Tico - a robot by (your name here)
 */
public class Tico extends TeamRobot
{

	private Random rnd = new Random(42);
	//private Hashtable<String, ScannedRobotEvent> enemies = new Hashtable<String, ScannedRobotEvent>();

	/**
	 * run: Tico's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		
		setColors(Color.blue,Color.white,Color.red); // body,gun,radar
		setAdjustRadarForGunTurn(false);

		// Robot main loop
		while(true) {
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
	        // Check for new targets.
	        // Only necessary for Narrow Lock because sometimes our radar is already
	        // pointed at the enemy and our onScannedRobot code doesn't end up telling
	        // it to turn, so the system doesn't automatically call scan() for us
	        // [see the javadocs for scan()].
	        scan();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

		String enemy_name = e.getName();
		if (isTeammate(enemy_name)) return;
		
		/*ScannedRobotEvent old_e = e;
		if ( !this.enemies.containsKey(enemy_name) ) {
			old_e = this.enemies.get(enemy_name);
		}
		this.enemies.put(enemy_name, e);*/
		
		double radarTurn = getHeadingRadians() + e.getBearingRadians()
	        - getRadarHeadingRadians();
		out.println("radar turn " + radarTurn);
		setTurnRadarRightRadians(2.0*Utils.normalRelativeAngle(radarTurn));

		double gunTurn = getHeadingRadians() + e.getBearingRadians()
	        - getGunHeadingRadians();
		out.println("gun turn " + gunTurn);
		setTurnGunRightRadians(1.1*Utils.normalRelativeAngle(gunTurn));

		if (e.getDistance() < 400){
			out.println("Fire! ");
			fire(Rules.MAX_BULLET_POWER - e.getDistance()/200);
		}else{
			setTurnRight(e.getBearing());
			ahead(e.getDistance()/2);
		}

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like

		double radarTurn = getHeadingRadians() + e.getBearingRadians()
	        - getRadarHeadingRadians();
		out.println(radarTurn);
		turnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));

		double gunTurn = getHeading() + e.getBearing()
	        - getGunHeading();
		out.println(gunTurn);
		turnGunRight(gunTurn);

		if (Math.abs(gunTurn) < Rules.MAX_BULLET_POWER){
			out.println("Fire! " + (Rules.MAX_BULLET_POWER - Math.abs(gunTurn)));
			fire(Rules.MAX_BULLET_POWER - Math.abs(gunTurn));
		}
		
		if(rnd.nextBoolean()){
			setTurnRight(e.getBearing()+90);
		}else{
			setTurnRight(e.getBearing()-90);
		}
		if(rnd.nextBoolean()){
			back(40);
		}else{
			ahead(40);
		}
		
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		double before = getRadarHeading();
		setTurnRight(e.getBearing()+180);
		setTurnRadarRight(e.getBearing()+180-before);		
		ahead(300);
	}	
}
