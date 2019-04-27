package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class DoorShade extends BasicStaticEntity {

	private Sprite open, closed;

	private boolean doorOpen = false;
	private Tree condition;

	public DoorShade(float x, float y, float drawingPriority, String dir, Tree condition) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		open = new Sprite(1, "floor_door_open_" + dir);
		closed = new Sprite(1, "floor_door_closed_" + dir);
		this.condition = condition;

		setSprite(doorOpen? open: closed);
	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {
		doorOpen = (boolean) condition.get(game);
		setSprite(doorOpen? open: closed);
	}
}
