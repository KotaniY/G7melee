package G7melee;

import java.awt.geom.Point2D;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/***
 * turnRightしか使わない(左に回すときは引数を負にする)
 * 
 * @author sugiurahajime
 *
 */
public class Move {
	private G7MeleeRobot robot;
	// final long seed =100;
	private final double MAX_RANGE = 180;
	private final double MAX_DISTANCE = 500; // 小刻みに動く方が予測されにくいため.
	private final double PI = 180;
	private static final int NUMBER_OF_ENEMIES = 9;
	private final double FIELD_HEIGHT = 2000;
	private final double FIELD_WIDTH = 2000;

	// Anti-gravity
	private static Point2D.Double[] enemyPoints = new Point2D.Double[NUMBER_OF_ENEMIES];
	private int count;

	public Move(G7MeleeRobot r) {
		robot = r;
	}

	/*ランダムな角度にターンしながら500進む*/
	public void randomMove() {
		double randomAngle = this.generateRandomAngle();
		double randomDistance = this.generateRandomDistance();
		if (willHitWall(randomDistance, randomAngle)) {
			robot.setTurnRight(180 - randomAngle);
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

	public void onHitByBullet(HitByBulletEvent e) {
		randomMove();
	}

	public void onHitWall(HitWallEvent e) {
		robot.setTurnRight(45);
		robot.setBack(300);
		
	}

	public void onHitRobot(HitRobotEvent e) {

	}

	// This method is called when one of your bullets hits another robot.
	public void onBulletHit(BulletHitEvent event) {

	}

	// This method is called when one of your bullets hits another bullet.
	public void onBulletHitBullet(BulletHitBulletEvent event) {

	}

	// This method is called when one of your bullets misses, i.e. hits a wall.
	public void onBulletMissed(BulletMissedEvent event) {

	}

	/***
	 * 
	 * @param distance
	 * @param Angle:   degree angle
	 * @return
	 */
	private boolean willHitWall(double distance, double Angle) {
		double nextRobotX = 0;
		double nextRobotY = 0;
		nextRobotX = robot.getX() + distance * Math.sin(Angle);
		nextRobotY = robot.getY() + distance * Math.cos(Angle);
		if (!(0 < nextRobotX && nextRobotX < FIELD_WIDTH) || !(0 < nextRobotY && nextRobotY < FIELD_HEIGHT)) {
			return true;
		} else {
			return false;
		}
	}

	// 右に回す時の最適化
	private double optimizeAngle(double angle) {
		if (angle >= PI) {
			return 2 * PI - angle;
		} else {
			return angle;
		}
	}

	/***
	 * angle[0,360], x is
	 */
	private double calcReverseAngle(double angle, double x, double y) {
		double reverseAngle = 0;
		if (y == 0 || y == FIELD_HEIGHT) {
			reverseAngle = PI - angle;
		} else if (x == 0 || x == FIELD_WIDTH) {
			reverseAngle = 2 * PI - angle;
		} else {// 壁についてなかったら真後ろに回転
			reverseAngle = PI;
		}
		return reverseAngle;
	}

	/***
	 * calculate anti-gravity force. How to calculatioon: sum force vectors to find
	 * the direction we're being pushd in.
	 */
	private void moveAGFAngle(ScannedRobotEvent e) {
		// store enemy's points
		double absBearing = e.getBearingRadians() + robot.getHeadingRadians();
		enemyPoints[count] = new Point2D.Double(robot.getX() + e.getDistance() * Math.sin(absBearing),
				robot.getY() + e.getDistance() * Math.cos(absBearing));
		if (++count >= robot.getOthers()) {
			count = 0;
		}

		// calculate anti-gravity force
		double xForce = 0;
		double yForce = 0;
		for (int i = 0; i < robot.getOthers() && enemyPoints[i] != null; i++) {
			absBearing = Utils
					.normalAbsoluteAngle(Math.atan2(enemyPoints[i].x - robot.getX(), enemyPoints[i].y - robot.getY()));
			double distance = enemyPoints[i].distance(robot.getX(), robot.getY());
			xForce -= Math.sin(absBearing) / (distance * distance);
			yForce -= Math.cos(absBearing) / (distance * distance);
		}
		double angle = Math.atan2(xForce, yForce);

		// if robot will hit , move reverse angle
		if (willHitWall(300, Math.toDegrees(angle))) {
			robot.setTurnRight(Math.PI / 2 - angle - robot.getHeadingRadians());
			robot.setAhead(300);
			return;
		}

		if (xForce == 0 && yForce == 0) {
			// If no force, do nothing
		} else if (Math.abs(angle - robot.getHeadingRadians()) < Math.PI / 2) {
			double rightAngle = angle - robot.getHeadingRadians();

			robot.setTurnRightRadians(Utils.normalRelativeAngle(rightAngle + Math.random() * Math.PI / 2));
			robot.setAhead(300);
		} else {
			double rightAngle = angle + Math.PI - robot.getHeadingRadians();
			robot.setTurnRightRadians(Utils.normalRelativeAngle(rightAngle + Math.random() * Math.PI / 2));
			robot.setAhead(-300);
		}

	}

	/***
	 * generate randomDistance range [-MAX_DISTANCE,MAX_DISTANCE] pixels
	 * 
	 * @return randomRange
	 */
	private double generateRandomDistance() {
		double parity = Math.random();
		double randomDistance = 0;
		// ahead(-100) moves 100 pixels back.
		if (parity >= 0.5) {
			randomDistance += Math.random() * MAX_DISTANCE;
		} else {
			randomDistance -= Math.random() * MAX_DISTANCE;
		}

		return randomDistance;
	}

	/***
	 * generate randomAngle range [-180,180] degrees
	 * 
	 * @return nextRange
	 */
	private double generateRandomAngle() {
		double parity = Math.random();
		double randomAngle = 0;

		// turnRight(-90) turns 90 degrees to the left.
		if (parity >= 0.5) {
			randomAngle += Math.random() * MAX_RANGE;
		} else {
			randomAngle -= Math.random() * MAX_RANGE;
		}
		return randomAngle;
	}

}
