package game.gameobjects.gameobjects.entities.entities;

import game.Constants;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.entities.BasicMovingEntity;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.gameobjects.gameobjects.wall.Wall;
import game.window.Window;
import game.window.light.Light;

/**
 * The player class
 */
public class Player extends BasicWalkingEntity implements Light {
	private static final float ATTACK_HITBOX_SIZE = 1f;
	private static final int ATTACK_TICKS = 40;
	private static final int INTERACT_TICKS = 5;

	private float knockbackReduction = 1;

	private static Sprite attack_r = new Sprite(90, "player_attack_r_0", "player_attack_r_1", "player_attack_r_2", "player_attack_r_3", "player_attack_r_4", "player_attack_r_5", "player_attack_r_6");
	private static Sprite walking_r = new Sprite(100, "player_idle_r", "player_walking_r_1", "player_idle_r", "player_walking_r_2");
	private static Sprite idle_r = new Sprite(250, "player_idle_r");

	private static Sprite attack_l = new Sprite(90, "player_attack_l_0", "player_attack_l_1", "player_attack_l_2", "player_attack_l_3", "player_attack_l_4", "player_attack_l_5", "player_attack_l_6");
	private static Sprite walking_l = new Sprite(100, "player_idle_l", "player_walking_l_1", "player_idle_l", "player_walking_l_2");
	private static Sprite idle_l = new Sprite(250, "player_idle_l");

	private static Sprite attack_lu = new Sprite(90, "player_attack_l_0", "player_attack_ul_1", "player_attack_ul_2", "player_attack_ul_3", "player_attack_ul_4", "player_attack_ul_5", "player_attack_l_6");
	private static Sprite attack_ld = new Sprite(90, "player_attack_l_0", "player_attack_dl_1", "player_attack_dl_2", "player_attack_dl_3", "player_attack_dl_4", "player_attack_dl_5", "player_attack_dl_6", "player_attack_dl_7", "player_attack_l_6");

	private static Sprite attack_ru = new Sprite(90, "player_attack_r_0", "player_attack_ur_1", "player_attack_ur_2", "player_attack_ur_3", "player_attack_ur_4", "player_attack_ur_5", "player_attack_r_6");
	private static Sprite attack_rd = new Sprite(90, "player_attack_r_0", "player_attack_dr_1", "player_attack_dr_2", "player_attack_dr_3", "player_attack_dr_4", "player_attack_dr_5", "player_attack_dr_6", "player_attack_dr_7", "player_attack_r_6");

	private static Sprite invisible = new Sprite(10, "particle_1");

	private boolean attackingLastTick, interactingLastTick, throwingLastTick;
	private boolean attacking, interacting, throwing;
	private int attack, interact, throw_, playerFlying;
	private Direction attackDirection;
	private Direction moveDirection;

	private static int keys = 0, potions = 0;

	public Player(float x, float y, float drawingPriority) {

		super(new HitBox(x, y, 0.75f, 0.875f), drawingPriority);

		attacking = false;
		interacting = false;
		throwing = false;
		throwingLastTick = false;
		attackingLastTick = false;
		interactingLastTick = false;

		attackDirection = Direction.RIGHT;
		moveDirection = Direction.RIGHT;
		attack = 0;
		interact = 0;
		throw_ = 0;
		playerFlying = 0;

		setSprite(idle_r);
	}

	@Override
	public void init(Game game) {
		super.init(game);
		if(hitBox != null) game.getParticleSystem().createParticle(ParticleType.EXPLOSION, hitBox.getCenterX(), hitBox.getCenterY(), 0, 0);
	}

	@Override
	public void setup(Window window) {
		super.setup(window);

		window.getLightHandler().addLight(this);
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);
		game.setGameMap("map1", true);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		super.collide(gameObject, direction, velocity, source);

		if (gameObject instanceof Zombie || gameObject instanceof Skeleton || gameObject instanceof Boss) {
			BasicMovingEntity zom = (BasicMovingEntity) gameObject;
			float dx = (this.hitBox.getCenterX() - zom.getHitBox().getCenterX());
			float dy = (this.hitBox.getCenterY() - zom.getHitBox().getCenterY());
			double l = Math.sqrt(dx * dx + dy * dy);
			dx /= l;
			dy /= l;
			addKnockBack(0.4f * dx, 0.4f * dy);

			game.damagePlayer(Constants.PLAYER_MOB_DAMAGE, false);
		}
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		super.interact(gameObject, hitBox, interactionType);

		if (gameObject instanceof Boss && interactionType == InteractionType.ATTACK) {
			game.damagePlayer(Constants.PLAYER_MOB_DAMAGE, false);
		}
	}

	@Override
	public void update(Game game) {
		super.update(game);
		Sprite newSprite = null;
		if (attack > 0) {
			if(playerFlying == 0) setMaxSpeed(0.15f);
			else setMaxSpeed(0);

			newSprite = (attackDirection.getSprite());
		}
		else {
			if (mx == 0) {
				if (my == 0) newSprite = (lastMX < 0 ? idle_l : idle_r);
				if (my != 0) {
					newSprite = (lastMX < 0 ? walking_l : walking_r);
				}
			}
			if (mx != 0) {
				newSprite = (mx < 0 ? walking_l : walking_r);
			}

			if(playerFlying == 0) setMaxSpeed(1f);
			else setMaxSpeed(0);
		}
		Direction newDirection = Direction.getDirection(mx, my);
		if (newDirection != Direction.VOID) moveDirection = newDirection;

		if (!sprite.equals(newSprite) && playerFlying != 1) setSprite(newSprite);

		if (attack > 0) {
			if (attack > 4) {
				HitBox attackHitBox = attackDirection.getHitBox(this);

				for (CollisionObject collisionObject : game.getCollisionObjects()) {
					if (collisionObject.equals(this)) continue;
					for (HitBox hitBox : collisionObject.getCollisionBoxes()) {
						if (hitBox.collides(attackHitBox)) {
							collisionObject.interact(this, attackHitBox, InteractionType.ATTACK);
							if (hitBox.type == HitBox.HitBoxType.BLOCKING)
								game.getCamera().addScreenshake(0.003f);

							if (collisionObject instanceof Wall) {
								game.getParticleSystem().createParticle(ParticleType.WHITE, attackHitBox.getCenterX(), attackHitBox.getCenterY(), -0.025f + 0.05f * (float) Math.random(), -0.025f + 0.05f * (float) Math.random());
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
			attackDirection = moveDirection;
			attack++;
		}

		if (throw_ > 0) {
			throw_++;
			if (throw_ > 10) throw_ = 0;
		} else if (throwing && !throwingLastTick && attack == 0) {
			float moveX = vx;
			float moveY = vy;
			if (moveX == 0 && moveY == 0) {
				float[] dir = moveDirection.getDirection();
				moveX = dir[0];
				moveY = dir[1];
			}
			//if (moveY != 0) moveX = 0;

			double length = Math.sqrt(moveX * moveX + moveY * moveY);
			moveX /= length * 7;
			moveY /= length * 7;
			game.addGameObject(new Arrow(hitBox.getCenterX(), hitBox.getCenterY(), 0.1f, new Sprite(1, Math.abs(moveX) > Math.abs(moveY) ? moveX > 0 ? "needle_r" : "needle_l" : moveY > 0 ? "needle_t" : "needle_d"), moveX, moveY, this));
			throw_++;
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
		throwingLastTick = throwing;
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
		setDrawingPriority(drawingPriority);
	}

	@Override
	public float getCollisionPriority() {
		return -10;
	}

	public void setThrowing(boolean throwing) {
		this.throwing = throwing;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public void setInteracting(boolean interacting) {
		this.interacting = interacting;
	}

	public void addItem(String item) {
		if(item.equalsIgnoreCase("key")) {
			game.getParticleSystem().createParticle(ParticleType.KEY, hitBox.getCenterX(), hitBox.getCenterY(), 0, 0.05f);
			keys++;
		}
		else if(item.equalsIgnoreCase("potion")) {
			game.getParticleSystem().createParticle(ParticleType.POTION, hitBox.getCenterX(), hitBox.getCenterY(), 0, 0.05f);
			potions++;
		} else if(item.equalsIgnoreCase("less_knockback")) {
			game.damagePlayer(20, true, true);
			knockbackReduction *= 0.9;
		}else if(item.equalsIgnoreCase("less_damage")) {
			game.damagePlayer(20, true, true);
			game.addLessDamage();
			game.addLessDamageTime();
		}else if(item.equalsIgnoreCase("less_damage_time")) {
			game.damagePlayer(20, true, true);

		}

	}

	public boolean removeItem(String item) {
		if(item.equalsIgnoreCase("key")) {
			if(keys > 0) {
				keys--;
				return true;
			}
		}
		else if(item.equalsIgnoreCase("potion")) {
			if(potions > 0) {
				potions--;
				return true;
			}
		}
		return false;
	}

	public int getItem(String item) {
		if(item.equalsIgnoreCase("key")) return keys;
		else if(item.equalsIgnoreCase("potion")) return potions;
		return -1;
	}

	private enum Direction {
		UP_RIGHT(attack_ru, 30), RIGHT(attack_r, 90), DOWN_RIGHT(attack_rd, 150), DOWN_LEFT(attack_ld, 210), LEFT(attack_l, 270), UP_LEFT(attack_lu, 330), VOID(null, 0);

		private Sprite sprite;
		private float deg;
		Direction(Sprite sprite, float deg) {
			this.sprite = sprite;
			this.deg = deg;
		}

		public Sprite getSprite() {
			return sprite;
		}

		public float[] getDirection() {
			return new float[] {(float) Math.cos(Math.toRadians(-(deg + 90))), (float) Math.sin(Math.toRadians(-(deg + 90)))};
		}

		public HitBox getHitBox(Player player) {
			HitBox hitBoxTarget = new HitBox(0, 0, ATTACK_HITBOX_SIZE, ATTACK_HITBOX_SIZE);

			switch (this){
				case DOWN_RIGHT:
					hitBoxTarget.x = player.getHitBox().getCenterX();
					hitBoxTarget.y = player.getHitBox().y - ATTACK_HITBOX_SIZE;
					break;
				case DOWN_LEFT:
					hitBoxTarget.x = player.getHitBox().getCenterX() - ATTACK_HITBOX_SIZE;
					hitBoxTarget.y = player.getHitBox().y - ATTACK_HITBOX_SIZE;
					break;
				case RIGHT:
					hitBoxTarget.x = player.getHitBox().x + player.getHitBox().width;
					hitBoxTarget.y = player.getHitBox().getCenterY() - ATTACK_HITBOX_SIZE / 2f;
					break;
				case LEFT:
					hitBoxTarget.x = player.getHitBox().x - ATTACK_HITBOX_SIZE;
					hitBoxTarget.y = player.getHitBox().getCenterY() - ATTACK_HITBOX_SIZE / 2f;
					break;
				case UP_LEFT:
					hitBoxTarget.x = player.getHitBox().getCenterX() - ATTACK_HITBOX_SIZE;
					hitBoxTarget.y = player.getHitBox().y + player.getHitBox().height;
					break;
				case UP_RIGHT:
					hitBoxTarget.x = player.getHitBox().getCenterX();
					hitBoxTarget.y = player.getHitBox().y + player.getHitBox().height;
					break;
			}

			return hitBoxTarget;
		}

		static Direction[] dir = {DOWN_LEFT, LEFT, UP_LEFT, UP_RIGHT, RIGHT, DOWN_RIGHT};
		static Direction getDirection(float mx, float my) {
			if (mx == 0 && my == 0) return VOID;

			int angle = (int) Math.floor(((Math.toDegrees(Math.atan2(mx, my)) + 180) % 360) / 60);
			return dir[angle];
		}
	}

	public void throwPlayer(int pThrown) {
		playerFlying = pThrown;
		attack = 0;
		if(pThrown == 1) {
			setMx(0);
			setMy(0);
			setSprite(invisible);
		}
		else setSprite(idle_l);
		if(pThrown == 0) game.damagePlayer(Constants.PLAYER_MOB_DAMAGE, false);
	}

	@Override
	public void addKnockBack(float kx, float ky) {
		super.addKnockBack(kx*knockbackReduction, ky*knockbackReduction);
	}
}
