package BattleRobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Gun {

	BattleRobot robot;
	
	
	public Gun(BattleRobot r) {
		robot = r;
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		//gunの向き調整
		double absoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		robot.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing - 
				robot.getGunHeadingRadians() + (e.getVelocity() * Math.sin(e.getHeadingRadians() - 
						absoluteBearing) / 13.0)));
		robot.setFire(1.5);
	}

	
	
}
