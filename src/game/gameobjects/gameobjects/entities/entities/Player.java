package game.gameobjects.gameobjects.entities.entities;

import game.Ability;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.gameobjects.gameobjects.wall.Wall;
import game.window.Window;
import game.window.light.Light;

import java.util.HashSet;
import java.util.Set;

/**
 * The player class
 */
public class Player extends BasicWalkingEntity implements Light {
	private static final int ATTACK_TICKS = 37;
	private static final int INTERACT_TICKS = 5;

	private static Sprite attack_r = new Sprite(90, "player_attack_r_0", "player_attack_r_1", "player_attack_r_2", "player_attack_r_3", "player_attack_r_4", "player_attack_r_5", "player_attack_r_6");
	private static Sprite walking_r = new Sprite(100, "player_idle_r", "player_walking_r_1", "player_idle_r", "player_walking_r_2");
	private static Sprite idle_r = new Sprite(250, "player_idle_r");

	private static Sprite attack_l = new Sprite(90, "player_attack_l_0", "player_attack_l_1", "player_attack_l_2", "player_attack_l_3", "player_attack_l_4", "player_attack_l_5", "player_attack_l_6");
	private static Sprite walking_l = new Sprite(100, "player_idle_l", "player_walking_l_1", "player_idle_l", "player_walking_l_2");
	private static Sprite idle_l = new Sprite(250, "player_idle_l");
	
	private Set<Ability> abilities;								//The abilities of the player
	private boolean attackingLastTick, interactingLastTick;
	private boolean attacking, interacting;
	private int attack, interact;
	private boolean attackLeft;

	public Player(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 0.75f, 0.999f), drawingPriority);

		abilities = new HashSet<>();
		attacking = false;
		interacting = false;
		attackingLastTick = false;
		interactingLastTick = false;

		attack = 0;
		interact = 0;

		setSprite(idle_r);
	}

	@Override
	public void init(Game game) {
		super.init(game);

		game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.getCenterY(), 0, 0);
	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);

		if (game.getDeadBodyHandler() != null)
			game.getDeadBodyHandler().addDeadBody((new DeadBody(getHitBox().x, getHitBox().y, "player", color, lastMX > 0)));
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		super.collide(gameObject, direction, velocity, source);

		if (gameObject instanceof Zombie) {
			Zombie zom = (Zombie) gameObject;
			float dx = (this.hitBox.getCenterX() - zom.getHitBox().getCenterX());
			float dy = (this.hitBox.getCenterY() - zom.getHitBox().getCenterY());
			double l = Math.sqrt(dx * dx + dy * dy);
			dx /= l;
			dy /= l;
			addKnockBack(0.4f * dx, 0.4f * dy);
		}
	}

	@Override
	public void update(Game game) {
		super.update(game);

		if (game.getMap().getDirectory() == null)
			this.addAbility(Ability.DOUBLE_JUMP);

		Sprite newSprite = null;
		if (attack > 0) newSprite = (attackLeft ? attack_l : attack_r);
		else {
			if (mx == 0) newSprite = (lastMX < 0 ? idle_l : idle_r);
			if (mx != 0) newSprite = (mx < 0 ? walking_l : walking_r);
		}

		if (!sprite.equals(newSprite)) setSprite(newSprite);

		if (attack > 0) {
			if (attack > 20) {
				HitBox attackHitBox = new HitBox(hitBox.getCenterX() + (attackLeft ? -0.875f : 0), hitBox.y, 0.875f, 0.875f);

				for (CollisionObject collisionObject : game.getCollisionObjects()) {
					if (collisionObject.equals(this)) continue;
					for (HitBox hitBox : collisionObject.getCollisionBoxes()) {
						if (hitBox.collides(attackHitBox)) {
							collisionObject.interact(this, attackHitBox, InteractionType.ATTACK);
							if (hitBox.type == HitBox.HitBoxType.BLOCKING)
								game.getCamera().addScreenshake(0.003f);

							if (collisionObject instanceof Wall) {
								game.getParticleSystem().createParticle(ParticleType.GRAY, attackHitBox.getCenterX(), attackHitBox.getCenterY(), -0.025f + 0.05f * (float) Math.random(), -0.025f + 0.05f * (float) Math.random());
							}

							if (collisionObject instanceof Player || collisionObject instanceof Zombie) {
								game.getParticleSystem().createParticle(ParticleType.RED, attackHitBox.getCenterX(), attackHitBox.getCenterY(), -0.025f + 0.05f * (float) Math.random(), -0.025f + 0.05f * (float) Math.random());
							}

							break;
						}
					}
				}
			}
			attack++;

			if (attack > ATTACK_TICKS) {
				attack = 0;
			}

		} else if (attacking && !attackingLastTick && interact == 0) {
			attackLeft = lastMX < 0;
			attack++;
		}

		if (interact > 0) {
			for (CollisionObject collisionObject : game.getCollisionObjects()) {
				if (collisionObject.equals(this)) continue;
				for (HitBox hitBox2 : collisionObject.getCollisionBoxes()) {
					if (hitBox2.collides(hitBox)) {
						collisionObject.interact(this, hitBox, InteractionType.INTERACT);
						break;
					}
				}
			}

			interact++;

			if (interact > INTERACT_TICKS) {
				interact = 0;
			}

		} else if (interacting && !interactingLastTick && attack == 0) {
			interact++;
		}

		interactingLastTick = interacting;
		attackingLastTick = attacking;
	}

	@Override
	public void cleanUp(Window window) {
		super.cleanUp(window);

		window.getLightHandler().removeLight(this);
	}

	@Override
	public float getPriority() {
		return 1;
	}


	@Override
	public void getLightColor(float[] values) {
		values[0] = 0.5f;
		values[1] = 0.5f;
		values[2] = 0f;
	}

	@Override
	public void getLightPosition(float[] values) {
		values[0] = hitBox.x + hitBox.width / 2;
		values[1] = hitBox.y + hitBox.height / 2;
		values[2] = 0.9f;
	}

	@Override
	public boolean updateLight() {
		return true;
	}

	public void respawn(float x, float y, float drawingPriority) {
		hitBox.x = x;
		hitBox.y = y;
		vx = 0;
		vy = 0;
		removeAllAbilities();
		setDrawingPriority(drawingPriority);
	}

	@Override
	public float getCollisionPriority() {
		return -10;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public void setInteracting(boolean interacting) {
		this.interacting = interacting;
	}

	public void addAbility(Ability ability) {
		abilities.add(ability);
	}

	public void removeAbility(Ability ability) {
		abilities.remove(ability);
	}

	public void removeAllAbilities() {
		abilities = new HashSet<>();
	}

	public boolean hasAbility(Ability ability) {
		return abilities.contains(ability);
	}
}
