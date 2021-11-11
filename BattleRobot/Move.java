package BattleRobot;

import java.awt.geom.Point2D;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Move{
	private BattleRobot robot;
	//final long seed =100;
	private final double MAX_RANGE = 180;
	private final double MAX_DISTANCE = 500; //小刻みに動く方が予測されにくいため. 
	private final double PI = 180;
	private static final int NUMBER_OF_ENEMIES = 9;
	private final double FIELD_HEIGHT = 2000;
	private final double FIELD_WIDTH = 2000;
	
	//Anti-gravity
	private static  Point2D.Double[] enemyPoints = new Point2D.Double[NUMBER_OF_ENEMIES];
	private int count;
	
	
	public Move(BattleRobot r) {
		robot = r;
	}
	
	
	public void randomMove() {
		double randomAngle = this.generateRandomAngle();
		double randomDistance = this.generateRandomDistance();
		if(willHitWall(randomDistance,randomAngle)) {
			robot.setTurnRight(180-randomAngle);
			robot.setAhead(randomDistance);
			return;
		}
		robot.setTurnRight(randomAngle);
		robot.setAhead(500);
		
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		// Anti-gravity
		moveAGFAngle(e);
	}
	
	public void onHitByBullet(HitRobotEvent e) {
		
	}
	
	public void onHitWall(HitWallEvent e) {
		robot.setTurnRight(180-robot.getHeading());
		robot.setAhead(100);
	}

	public void onHitByBullet(HitByBulletEvent e) {
		
	}
	public void onHitRobot(HitRobotEvent e) {
	
	}
	public void onHitWall1(HitWallEvent e) {

	}
	//This method is called when one of your bullets hits another robot.
	public void	onBulletHit(BulletHitEvent event) {
		
	}
	
	//This method is called when one of your bullets hits another bullet.
	public void	onBulletHitBullet(BulletHitBulletEvent event) {
		
	}
	
	//This method is called when one of your bullets misses, i.e. hits a wall.
	public void	onBulletMissed(BulletMissedEvent event) {
		
	}
	/***
	 * 
	 * @param distance
	 * @param Angle: degree angle
	 * @return
	 */
	private boolean willHitWall(double distance, double Angle) {
		double nextRobotX = 0;
		double nextRobotY = 0;
		nextRobotX = robot.getX() + distance*Math.sin(Angle);
		nextRobotY = robot.getY() + distance*Math.cos(Angle);
		if(!(0<nextRobotX && nextRobotX<FIELD_WIDTH) || !(0<nextRobotY && nextRobotY<FIELD_HEIGHT)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/***
	 * calculate anti-gravity force. 
	 * How to calculatioon: sum force vectors to find the direction we're being pushd in.
	 */
	private void moveAGFAngle(ScannedRobotEvent e){
		//store enemy's points
		double absBearing = e.getBearingRadians() + robot.getHeadingRadians();
		enemyPoints[count] = new Point2D.Double(robot.getX() +
				e.getDistance()*Math.sin(absBearing), robot.getY() + e.getDistance()*Math.cos(absBearing));
		if(++count>=robot.getOthers()) {
			count=0;
		}
		
		//calculate anti-gravity force
		double xForce = 0;
		double yForce = 0;
		for(int i=0; i < robot.getOthers() && enemyPoints[i] != null ; i++) {
			absBearing = Utils.
					normalAbsoluteAngle(Math.atan2(enemyPoints[i].x-robot.getX(), 
							enemyPoints[i].y - robot.getY()));
			double distance = enemyPoints[i].distance(robot.getX(),robot.getY());
			xForce -= Math.sin(absBearing) / (distance * distance);
			yForce -= Math.cos(absBearing) / (distance * distance);
		}
		double angle = Math.atan2(xForce, yForce);
		
		// if robot will hit , move backwards
		if(willHitWall(300,Math.toDegrees(angle))) {
			robot.setTurnRightRadians(180-angle);
			robot.setAhead(300);
			return;
		}
		
		if (xForce == 0 && yForce == 0) {
		    // If no force, do nothing
		} else if(Math.abs(angle-robot.getHeadingRadians())<Math.PI/2){
			double rightAngle = angle-robot.getHeadingRadians();
			
		    robot.setTurnRightRadians(Utils.normalRelativeAngle(rightAngle+Math.random()*PI/4));
		    robot.setAhead(Double.POSITIVE_INFINITY);
		} else {
			double rightAngle = angle+Math.PI-robot.getHeadingRadians();
		    robot.setTurnRightRadians(Utils.normalRelativeAngle(rightAngle + Math.random()*PI/4));
		    robot.setAhead(Double.NEGATIVE_INFINITY);
		}
		
	}
	/***
	 * generate randomDistance range [-200,200] pixels
	 * @return randomRange
	 */
	private double generateRandomDistance() {
		double parity = Math.random();
		double randomDistance=0;
		// ahead(-100) moves 100 pixels back.
		if(parity>=0.5) {
			randomDistance += Math.random() * MAX_DISTANCE;
		}else {		
			randomDistance -= Math.random() * MAX_DISTANCE;
		}
		
		return randomDistance;
	}
	
	/***
	 * generate randomAngle range [-180,180] degrees
	 * @return nextRange
	 */
	private double generateRandomAngle() {
		double parity = Math.random();
		double randomAngle=0;
		
		// turnRight(-90) turns 90 degrees to the left.
		if(parity>=0.5) {
			randomAngle += Math.random() * MAX_RANGE;
		}else {		
			randomAngle -= Math.random() * MAX_RANGE;
		}
		
		return randomAngle;
	}
	
	
	
}
