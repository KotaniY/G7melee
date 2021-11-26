package G7melee;

import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

/***
 * Melee rule – Number of Rounds: 3(default とは異なる) – Gun Cooling Rate: 0.3
 * (default とは異なる) – Inactivity Time: 450(default) Sentry Border Size: 100
 * (default) – Hide Enemy Names: disable (default) – Battle Field Size: 2000 ×
 * 2000(default とは異なる)
 */
public class G7MeleeRobot extends AdvancedRobot {
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
		 * default behavior
		 */
		while (true) {
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
		move.onHitByBullet(e);
		execute();

	}

	public void onHitRobot(HitRobotEvent e) {

	}

	public void onHitWall(HitWallEvent e) {

		move.onHitWall(e);
		execute();
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

}
