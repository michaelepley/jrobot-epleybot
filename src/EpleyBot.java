/**
 * This is a sample bot for testing building and deploying.
 * JJRobots (c) 2000 L.Boselli - boselli@uno.it
 */

import com.mepley.demo.games.jrobots.classic.JJRobot;

/**
 * The Epley bot.
 *  
 * 
 * Based on Platoon by JJRobots (c) 2000 L.Boselli - boselli@uno.it
 * 
 * @author mepley
 *
 */
public class EpleyBot extends JJRobot {

	/** The count. */
	private static int count;
	
	/** The corner X. */
	private static int[] cornerX = { 50, 950, 950, 50 };
	
	/** The corner Y. */
	private static int[] cornerY = { 50, 50, 950, 950 };
	
	/** The target X. */
	private static int targetX = 500;
	
	/** The target Y. */
	private static int targetY = 500;
	
	/** The loc X. */
	private static int locX[] = new int[8];
	
	/** The loc Y. */
	private static int locY[] = new int[8];
	
	/** The corner 1. */
	private static int corner1;

	/** The n corner. */
	private int nCorner;
	
	/** The scan. */
	private int scan;
	
	/** The id. */
	private int id;

	/**
	 * Creates a new platoon bot.
	 */
	public EpleyBot() {
		super();
	}

	/**
	 * @see com.mepley.demo.games.jrobots.classic.JJRobot#main()
	 */
	@Override
	public void main() {
		if ((this.id = this.id()) == 0) {
			EpleyBot.count = 1;
			EpleyBot.corner1 = JJRobot.rand(4);
		} else {
			EpleyBot.count = this.id + 1;
		}
		this.nCorner = EpleyBot.corner1;
		int dx = EpleyBot.cornerX[this.nCorner] - (EpleyBot.locX[this.id] = this.loc_x());
		int dy = EpleyBot.cornerY[this.nCorner] - (EpleyBot.locY[this.id] = this.loc_y());
		int angle;
		if (dx == 0) {
			angle = dy > 0 ? 90 : 270;
		} else {
			angle = JJRobot.atan((dy * 100000) / dx);
		}
		if (dx < 0) {
			angle += 180;
		}
		this.drive(angle, 100);
		switch (this.nCorner) {
		default:
		case 0:
			while ((EpleyBot.locX[this.id] > 150) || (EpleyBot.locY[this.id] > 150)) {
				this.fire2();
			}
			break;
		case 1:
			while ((EpleyBot.locX[this.id] < 850) || (EpleyBot.locY[this.id] > 150)) {
				this.fire2();
			}
			break;
		case 2:
			while ((EpleyBot.locX[this.id] < 850) || (EpleyBot.locY[this.id] < 850)) {
				this.fire2();
			}
			break;
		case 3:
			while ((EpleyBot.locX[this.id] > 150) || (EpleyBot.locY[this.id] < 850)) {
				this.fire2();
			}
			break;
		}
		do {
			this.drive(0, 0);
			while (this.speed() >= 50) {
				this.fire1();
			}
			if (++this.nCorner == 4) {
				this.nCorner = 0;
			}
			dx = EpleyBot.cornerX[this.nCorner] - this.loc_x();
			dy = EpleyBot.cornerY[this.nCorner] - this.loc_y();
			if (dx == 0) {
				angle = dy > 0 ? 90 : 270;
			} else {
				angle = JJRobot.atan((dy * 100000) / dx);
			}
			if (dx < 0) {
				angle += 180;
			}
			this.drive(angle, 100);
			switch (this.nCorner) {
			default:
			case 0:
				while (EpleyBot.locY[this.id] > 150) {
					this.fire1();
				}
				break;
			case 1:
				while (EpleyBot.locX[this.id] < 850) {
					this.fire1();
				}
				break;
			case 2:
				while (EpleyBot.locY[this.id] < 850) {
					this.fire1();
				}
				break;
			case 3:
				while (EpleyBot.locX[this.id] > 150) {
					this.fire1();
				}
				break;
			}
		} while (true);
	}

	/**
	 * Fire 1.
	 */
	private void fire1() {
		switch (this.nCorner) {
		default:
		case 0:
			if ((++this.scan > 470) || (this.scan < 240)) {
				this.scan = 250;
			}
			break;
		case 1:
			if ((++this.scan > 200) || (this.scan < -30)) {
				this.scan = -20;
			}
			break;
		case 2:
			if ((++this.scan > 290) || (this.scan < 60)) {
				this.scan = 70;
			}
			break;
		case 3:
			if ((++this.scan > 380) || (this.scan < 150)) {
				this.scan = 160;
			}
			break;
		}
		this.fire();
	}

	/**
	 * Fire 2.
	 */
	private void fire2() {
		if (++this.scan > 360) {
			this.scan = 0;
		}
		this.fire();
	}

	/**
	 * Fire.
	 */
	private void fire() {
		EpleyBot.locX[this.id] = this.loc_x();
		EpleyBot.locY[this.id] = this.loc_y();
		int range;
		if (((range = this.scan(this.scan, 1)) > 40) && (range <= 740)) {
			if (EpleyBot.count > 1) {
				boolean shot = true;
				int shotX = EpleyBot.locX[this.id] + ((range * JJRobot.cos(this.scan)) / 100000);
				int shotY = EpleyBot.locY[this.id] + ((range * JJRobot.sin(this.scan)) / 100000);
				for (int ct = 0; ct < EpleyBot.count; ct++) {
					if (ct != this.id) {
						int dx = shotX - EpleyBot.locX[ct];
						int dy = shotY - EpleyBot.locY[ct];
						if (((dx * dx) + (dy * dy)) < 1600) {
							shot = false;
							break;
						}
					}
				}
				if (shot) {
					EpleyBot.targetX = shotX;
					EpleyBot.targetY = shotY;
					this.cannon(this.scan, range);
					this.scan -= 10;
				} else {
					int dx = EpleyBot.targetX - EpleyBot.locX[this.id];
					int dy = EpleyBot.targetY - EpleyBot.locY[this.id];
					int dist2 = (dx * dx) + (dy * dy);
					if ((dist2 > 1600) && (dist2 <= 547600)) {
						int angle;
						if (dx == 0) {
							angle = dy > 0 ? 90 : 270;
						} else {
							angle = JJRobot.atan((dy * 100000) / dx);
							if (dx < 0) {
								angle += 180;
							}
						}
						this.cannon(angle, JJRobot.sqrt(dist2));
					}
				}
			} else {
				this.cannon(this.scan, range);
				this.scan -= 10;
			}
		}
	}
}
