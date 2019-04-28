package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

import java.awt.*;
import java.util.Optional;

/**
 * A zombie that follows the player
 */
public class Zombie extends BasicWalkingEntity {
	private static Sprite walking_r = new Sprite(250, "zombie_walking_r_0", "zombie_walking_r_1", "zombie_walking_r_2");
	private static Sprite idle_r = new Sprite(250, "zombie_walking_r_0");
	private static Sprite walking_l = new Sprite(250, "zombie_walking_l_0", "zombie_walking_l_1", "zombie_walking_l_2");
	private static Sprite idle_l = new Sprite(250, "zombie_walking_l_0");

	private Tree onDead;

	public Zombie(float x, float y, float drawingPriority, Tree onDead) {
		super(new HitBox(x, y, 0.5f, 0.875f), drawingPriority);

		setSprite(idle_r);

		this.onDead = onDead;

		setMaxSpeed(0.15f);
	}

	@Override
	public void update(Game game) {
		super.update(game);

		Sprite newSprite = null;
		if (mx == 0) newSprite = (lastMX < 0 ? idle_l : idle_r);
		if (mx != 0) newSprite = (mx < 0 ? walking_l : walking_r);

		if (sprite !=(newSprite)) setSprite(newSprite);


		Optional<Player> nearestPlayer = game.getPlayers().stream().sorted((p1, p2) -> Float.compare(hitBox.distance(p1.getHitBox()), hitBox.distance(p2.getHitBox()))).findFirst();

		if (nearestPlayer.isPresent()) {
			Player p = nearestPlayer.get();
			setMx(p.getHitBox().getCenterX() - hitBox.getCenterX());
			setMy(p.getHitBox().getCenterY() - hitBox.getCenterY());
		} else {
			setMx((game.getGameTick() % 121) - 60);
			setMy(((game.getGameTick()+30) % 121) - 60);
		}
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);

		if (onDead != null) onDead.get(game);

		if (!mapChange) {
			game.damagePlayer(-5, true);
			if (game.getDeadBodyHandler() != null)
				game.getDeadBodyHandler().addDeadBody((new DeadBody(getHitBox().x, getHitBox().y, getDrawingPriority(),"zombie", Color.BLACK, lastMX > 0)));
		}
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		super.interact(gameObject, hitBox, interactionType);

		if (gameObject instanceof Player && interactionType == InteractionType.ATTACK) game.removeGameObject(this);
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public float getCollisionPriority() {
		return -3;
	}

}
