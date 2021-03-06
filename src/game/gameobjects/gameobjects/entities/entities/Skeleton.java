package game.gameobjects.gameobjects.entities.entities;

import game.Constants;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

import java.awt.*;
import java.util.Optional;

public class Skeleton extends BasicWalkingEntity {
	private static Sprite walking_r = new Sprite(250, "skeleton_walking_r_0", "skeleton_walking_r_1", "skeleton_walking_r_0", "skeleton_walking_r_2");
	private static Sprite idle_r = new Sprite(250, "skeleton_walking_r_0", "skeleton_walking_r_1", "skeleton_walking_r_0", "skeleton_walking_r_2");
	private static Sprite walking_l = new Sprite(250, "skeleton_walking_l_0", "skeleton_walking_l_1", "skeleton_walking_l_0", "skeleton_walking_l_2");
	private static Sprite idle_l = new Sprite(250, "skeleton_walking_l_0", "skeleton_walking_l_1", "skeleton_walking_l_0", "skeleton_walking_l_2");
	private int lastAttack;

	private Tree onDead;

	public Skeleton(float x, float y, float drawingPriority, Tree onDead) {
		super(new HitBox(x, y, 0.5f, 0.875f), drawingPriority);

		lastAttack = 0;

		setSprite(idle_r);

		this.onDead = onDead;

		setMaxSpeed(0.25f);
	}

	@Override
	public void update(Game game) {
		super.update(game);

		Sprite newSprite = null;
		if (mx == 0) newSprite = (lastMX < 0 ? idle_l : idle_r);
		if (mx != 0) newSprite = (mx < 0 ? walking_l : walking_r);

		if (sprite !=(newSprite)) setSprite(newSprite);


		Optional<Player> nearestPlayer = game.getPlayers().stream().sorted((p1, p2) -> Float.compare(hitBox.distance(p1.getHitBox()), hitBox.distance(p2.getHitBox()))).findFirst();

		lastAttack--;
		if (nearestPlayer.isPresent()) {
			if (Math.random() < 0.005 && lastAttack < 120) {
				float vx = nearestPlayer.get().getHitBox().getCenterX() - hitBox.getCenterX();
				float vy = nearestPlayer.get().getHitBox().getCenterY() - hitBox.getCenterY();
				double length = Math.sqrt(vx*vx+vy*vy);
				vx /= length*7;
				vy /= length*7;
				game.addGameObject(new Arrow(hitBox.getCenterX(), hitBox.getCenterY(), getDrawingPriority(), new Sprite(20, "bone_0", "bone_1", "bone_2", "bone_3", "bone_4", "bone_5", "bone_6", "bone_7"), vx, vy, this));
				lastAttack = 0;
			}
		}

		setMx((game.getGameTick() % 121) - 60);
		setMy(((game.getGameTick()+30) % 121) - 60);
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);

		if (onDead != null) onDead.get(game);

		if (!mapChange) {
			game.damagePlayer(-Constants.PLAYER_MOB_HEAL, true);
			if (game.getDeadBodyHandler() != null)
				game.getDeadBodyHandler().addDeadBody((new DeadBody(getHitBox().x, getHitBox().y, getDrawingPriority(),"skeleton", Color.BLACK, lastMX > 0)));
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
