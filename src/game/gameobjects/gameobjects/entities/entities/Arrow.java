package game.gameobjects.gameobjects.entities.entities;

import game.Constants;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

public class Arrow extends BasicMovingEntity {

	private GameObject owner;

	public Arrow(float x, float y, float drawingPriority, Sprite sprite, float vx, float vy, GameObject owner) {
		super(new HitBox(x-0.125f, y-0.125f, 0.25f, 0.25f), drawingPriority);
		this.setSprite(sprite);
		this.owner = owner;
		this.vx = vx;
		this.vy = vy;
		this.hitBox.type = HitBox.HitBoxType.NOT_BLOCKING;
	}

	@Override
	public void update(Game game) {
		super.update(game);

	}

	@Override
	public void addKnockBack(float kx, float ky) {
		vx += kx;
		vy += ky;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject == owner) return;
		super.collide(gameObject, direction, velocity, source);

		game.removeGameObject(this);
		if (gameObject instanceof Player) {
			((Player) gameObject).addKnockBack(vx, vy);
			game.damagePlayer(Constants.PLAYER_MOB_DAMAGE, false);
		}
		if (gameObject instanceof Zombie || gameObject instanceof Skeleton) {
			if(owner instanceof  Player) {
				((BasicWalkingEntity) gameObject).addKnockBack(vx, vy);
				((BasicWalkingEntity) gameObject).setMaxSpeed(0.07f);
			} else {
				game.removeGameObject((GameObject) gameObject);
			}
		}
	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public float getPriority() {
		return 0;
	}
}
