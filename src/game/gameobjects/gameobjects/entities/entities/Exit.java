package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

/**
 * A door used to go into a new map
 */
public class Exit extends BasicStaticEntity {
	private static Sprite doorClosed = new Sprite(1, "wall_door_closed");
	private static Sprite doorOpen = new Sprite(100, "door_side");

	private boolean opened = false;

	private String targetMap;				//name of the new map
	private Tree onEntrance, onOpen;

	public Exit(float x, float y, float drawingPriority, String targetMap, Tree onEntrance, Tree onOpen) {
		super(new HitBox(x + 0.125f, y, 0.75f, 1), drawingPriority);

		this.targetMap = targetMap;
		this.onEntrance = onEntrance;
		this.onOpen = onOpen;

		this.hitBox.type = opened? HitBox.HitBoxType.NOT_BLOCKING: HitBox.HitBoxType.BLOCKING;
		setSprite(opened? doorOpen: doorClosed);
	}

	@Override
	public void update(Game game) {
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		if (gameObject instanceof Player) {
			Player p = (Player) gameObject;

			if(opened && interactionType == InteractionType.INTERACT) {
				if (game.setGameMap(targetMap, true)) {
					setSprite(doorOpen);
					if (onEntrance != null) onEntrance.get(game);
				}
			} else game.getCamera().addScreenshake(0.001f);

			if(!opened && p.removeItem("key")) {
				opened = true;
				if (onOpen != null) onOpen.get(game);
				this.hitBox.type = opened? HitBox.HitBoxType.NOT_BLOCKING: HitBox.HitBoxType.BLOCKING;
				setSprite(opened? doorOpen: doorClosed);
			}

		}
	}

	@Override
	public float getCollisionPriority() {
		return -1;
	}

	public String getTargetMap() {
		return targetMap;
	}

	public void setTargetMap(String targetMap) {
		this.targetMap = targetMap;
	}

	public void setOnEntrance(Tree onEntrance) {
		this.onEntrance = onEntrance;
	}
}
