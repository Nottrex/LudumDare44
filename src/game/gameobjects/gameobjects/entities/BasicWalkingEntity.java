package game.gameobjects.gameobjects.entities;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.util.MathUtil;

/**
 * A simple entity, that implements gravity, walking and jumping
 */
public abstract class BasicWalkingEntity extends BasicMovingEntity {
	protected float lastMX;
	protected float mx;
	protected float lastMY;
	protected float my;

	private float maxSpeed = 1;

	public BasicWalkingEntity(HitBox hitBox, float drawingPriority) {
		super(hitBox, drawingPriority);

		lastMX = -1;
		mx = 0;
		lastMY = -1;
		my = 0;
	}

	@Override
	public void update(Game game) {
		double speed = Math.sqrt(mx*mx + my*my);
		if (speed > 1) {
			mx /= speed;
			my /= speed;
		}

		vx = (2*vx + mx * Constants.MAX_WALKING_SPEED * maxSpeed)/3;
		vy = (2*vy + my * Constants.MAX_WALKING_SPEED * maxSpeed)/3;
		if (Math.abs(mx) >= 0.2f) lastMX = mx;
		if (Math.abs(my) > 0.2f) lastMY = my;

		super.update(game);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		super.collide(gameObject, direction, velocity, source);

	}

	public void setMy(float my) {
		this.my = MathUtil.clamp(my, -1, 1);
	}

	public void setMx(float mx) {
		this.mx = MathUtil.clamp(mx, -1, 1);
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
