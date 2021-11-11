package BattleRobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Radar {
	BattleRobot robot;
	
	public Radar(BattleRobot r) {
		robot = r;
	}

	
	public void onScannedRobot(ScannedRobotEvent e) {
		// 1-vs-1 radar
		robot.setTurnRadarRight(2.0 * Utils
				.normalRelativeAngleDegrees(robot.getHeading() + e.getBearing() - robot.getRadarHeading()));
		
	}
}
