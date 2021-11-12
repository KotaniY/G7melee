package G7melee;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Radar {
	G7MeleeRobot robot;
	
	public Radar(G7MeleeRobot r) {
		robot = r;
	}

	
	public void onScannedRobot(ScannedRobotEvent e) {
		// 1-vs-1 radar
		robot.setTurnRadarRight(2.0 * Utils
				.normalRelativeAngleDegrees(robot.getHeading() + e.getBearing() - robot.getRadarHeading()));
		
	}
	

	public void onHitByBullet(HitByBulletEvent e) {
		
	}
	public void onHitRobot(HitRobotEvent e) {
	
	}
	public void onHitWall(HitWallEvent e) {

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
}
