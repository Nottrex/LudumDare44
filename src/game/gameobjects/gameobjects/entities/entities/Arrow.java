package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;

public class Arrow extends BasicMovingEntity {

	private GameObject owner;
	private float vx2;
	private float vy2;

	public Arrow(float x, float y, float drawingPriority, Sprite sprite, float vx, float vy, GameObject owner) {
		super(new HitBox(x-0.125f, y-0.125f, 0.25f, 0.25f), drawingPriority);
		this.setSprite(sprite);
		this.owner = owner;
		this.vx2 = vx;
		this.vy2 = vy;
		this.hitBox.type = HitBox.HitBoxType.NOT_BLOCKING;
	}

	@Override
	public void update(Game game) {
		super.update(game);

		this.vx = vx2;
		this.vy = vy2;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if (gameObject == owner) return;
		super.collide(gameObject, direction, velocity, source);

		game.removeGameObject(this);
		if (gameObject instanceof Player) {
			((Player) gameObject).addKnockBack(vx, vy);
		}
		if (gameObject instanceof Zombie || gameObject instanceof Skeleton) {
			game.removeGameObject((GameObject) gameObject);
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
