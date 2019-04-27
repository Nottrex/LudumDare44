package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.hitbox.HitBox;
import game.data.Sprite;
import game.gameobjects.gameobjects.entities.BasicDrawingEntity;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

import java.awt.*;

/**
 * A DeadBody, that remains when a player or zombie dies
 */
public class DeadBody extends BasicDrawingEntity {

	public DeadBody(float x, float y, float drawing, String entity, Color color, boolean direction) {
		super(new HitBox(x, y, 0.75f, 1f, HitBox.HitBoxType.NOT_BLOCKING), drawing);
		setColor(color == null ? Color.BLACK : color);
		Sprite idle = new Sprite(100, entity + /*(direction ? "_r" : "_l") +*/ "_corpse");
		setSprite(idle);
	}
	public HitBox getHitBox() {
		return hitBox;
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public void update(Game game) {

	}
}
