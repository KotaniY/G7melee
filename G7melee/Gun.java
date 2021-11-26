package G7melee;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Gun {

	G7MeleeRobot robot;//コメント

	
	public Gun(G7MeleeRobot r) {
		robot = r;
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		//gunの向き調整
		double absoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		robot.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing - 
				robot.getGunHeadingRadians() + (e.getVelocity() * Math.sin(e.getHeadingRadians() - 
						absoluteBearing) / 13.0)));
		robot.setFire(1);
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
