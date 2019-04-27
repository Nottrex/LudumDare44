package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

public class Chest extends BasicStaticEntity {

	private static Sprite open = new Sprite(100, "chest_open");
	private static Sprite closed = new Sprite(100, "chest_closed");

	private String content;
	private boolean opened = false;

	public Chest(float x, float y, float drawingPriority, String content) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);
		hitBox.type = HitBox.HitBoxType.BLOCKING;
		setSprite(opened? open: closed);
		this.content = content;
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
		if(!opened) {
			if(gameObject instanceof Player) {
				Player p = (Player) gameObject;
				p.addItem(content);
				opened = true;
			}
		}
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void update(Game game) {
		setSprite(opened? open: closed);
	}
}
