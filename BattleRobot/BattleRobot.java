package BattleRobot;


import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

/***
 * Melee rule
* – Number of Rounds: 3(default とは異なる) 
* – Gun Cooling Rate: 0.3 (default とは異なる) – 
* Inactivity Time: 450(default)
* Sentry Border Size: 100 (default)
*– Hide Enemy Names: disable (default)
*– Battle Field Size: 2000 × 2000(default とは異なる)
*/
public class BattleRobot extends AdvancedRobot {
	private Gun gun;
	private Move move;
	private Radar radar;
	
	public void run() {
		move = new Move(this);
		gun = new Gun(this);
		radar = new Radar(this);
		
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		/**
		 *  default behavior
		 */
		while(true) {
			setTurnRadarRight(90);
			move.randomMove();
			
			execute();
			
		}
	}
	
	/**
	 * called when you see another robot 
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		radar.onScannedRobot(e);
		gun.onScannedRobot(e);
		
		move.onScannedRobot(e);
		
		execute();
		
		
	}
	public void onHitByBullet(HitByBulletEvent e) {
		move.randomMove();
		execute();
		
	}
	public void onHitRobot(HitRobotEvent e) {
	
	}
	public void onHitWall(HitWallEvent e) {
		setBack(50);
		setTurnRight(90);
		
	}
}
