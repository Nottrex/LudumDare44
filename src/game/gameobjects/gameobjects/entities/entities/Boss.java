package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.TimeUtil;

import java.awt.*;
import java.util.Optional;

public class Boss extends BasicWalkingEntity {
	private static final float BARREL_SPEED = 0.15f;

	private static Sprite 	idle_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_7", "boss_attack_l_8"),
							attack_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_1", "boss_attack_l_2", "boss_attack_l_3", "boss_attack_l_4", "boss_attack_l_5", "boss_attack_l_6", "boss_attack_l_7", "boss_attack_l_8"),
							grab_l = new Sprite(100, "boss_grab_l_0", "boss_grab_l_1", "boss_grab_l_2", "boss_grab_l_3", "boss_grab_l_4"),
							throw_player_l = new Sprite(100, "boss_throw_player_l_0", "boss_throw_player_l_1", "boss_throw_player_l_2", "boss_throw_player_l_3"),
							throw_barrel_l = new Sprite(100, "boss_throw_barrel_l_0", "boss_throw_barrel_l_1", "boss_throw_barrel_l_2", "boss_throw_barrel_l_3"),
							recover_throw_l = new Sprite(100, "boss_throw_recover_l_0", "boss_throw_recover_l_1", "boss_throw_recover_l_2", "boss_throw_recover_l_3");

	private static Sprite 	idle_r = new Sprite(100, "boss_attack_r_0", "boss_attack_r_7", "boss_attack_r_8"),
							attack_r = new Sprite(100, "boss_attack_r_0", "boss_attack_r_1", "boss_attack_r_2", "boss_attack_r_3", "boss_attack_r_4", "boss_attack_r_5", "boss_attack_r_6", "boss_attack_r_7", "boss_attack_r_8"),
							grab_r = new Sprite(100, "boss_grab_r_0", "boss_grab_r_1", "boss_grab_r_2", "boss_grab_r_3", "boss_grab_r_4"),
							throw_player_r = new Sprite(100, "boss_throw_player_r_0", "boss_throw_player_r_1", "boss_throw_player_r_2", "boss_throw_player_r_3"),
							throw_barrel_r = new Sprite(100, "boss_throw_barrel_r_0", "boss_throw_barrel_r_1", "boss_throw_barrel_r_2", "boss_throw_barrel_r_3"),
							recover_throw_r = new Sprite(100, "boss_throw_recover_r_0", "boss_throw_recover_r_1", "boss_throw_recover_r_2", "boss_throw_recover_r_3");

	private long lastHitTaken = TimeUtil.getTime();
	private int life = 10;
	private Text text;
	private float seismicX, seismicY;

	private BossAction currentAction;
	private int time = 0;
	public Boss(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 2f, 2f), drawingPriority);
		setSprite(idle_l);
	}

	@Override
	public void init(Game game) {
		super.init(game);
		text = new Text(0, 0.98f, -100, life + "", 0.1f, false, 0f, 1f, Color.RED);

		game.addGameObject(text);
		currentAction = BossAction.IDLE;
	}

	@Override
	public void update(Game game) {
		super.update(game);

		if (life <= 0) {
			game.removeGameObject(this);
			text.setText("0");
			return;
		}
		text.setText(life + "");

		if (time > currentAction.getTime()) {

			BossAction newAction;
			do {
				newAction = BossAction.values()[(int) (Math.random() * BossAction.values().length)];
			} while(newAction == currentAction);
			currentAction = newAction;
			System.out.println("Current Action: " + currentAction.toString());
			time = 0;
		}
		currentAction.doAction(time, this);
		time++;
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		super.interact(gameObject, hitBox, interactionType);

		if (TimeUtil.getTime()-1000 > lastHitTaken && gameObject instanceof Player && interactionType == InteractionType.ATTACK) {
			lastHitTaken = TimeUtil.getTime();
			life--;
		}
	}

	@Override
	public void remove(Game game, boolean mapChange) {
		super.remove(game, mapChange);
		game.removeGameObject(text);
		if(!mapChange) game.getPlayers().get(0).addItem("key");
	}

	@Override
	public float getCollisionPriority() {
		return 0;
	}

	@Override
	public float getPriority() {
		return 0;
	}

	@Override
	public void addKnockBack(float kx, float ky) {
		super.addKnockBack(kx/4, ky/4);
	}

	private enum BossAction {
		IDLE(90), STOMP(200), THROW_BARREL(192), FOLLOW_PLAYER(240), GRAB_PLAYER(240);

		private int time;
		BossAction(int time) {
			this.time = time;
		}

		public int getTime() {
			return time;
		}

		public void doAction(int currentTick, Boss b) {
			if (this == STOMP) {
				if (currentTick == 0) {
					b.setSprite(attack_l);
					b.setMx(0);
					b.setMy(0);

					b.seismicX = b.getHitBox().getCenterX();
					b.seismicY = b.getHitBox().getCenterY();
				}
				if (currentTick == 54) {
					b.setSprite(idle_l);
				}
				test:
				for (int i = 0; i < 100; i++) {
					float x = b.seismicX + (float) Math.cos((currentTick / 500f + i / 100f) * 2 * Math.PI) * (currentTick) / 20;
					float y = b.seismicY  + (float) Math.sin((currentTick / 500f + i / 100f) * 2 * Math.PI) * (currentTick) / 20;

					HitBox hitBox2 = new HitBox(x, y, 0, 0);

					CollisionObject target = null;
					t: for (CollisionObject co : b.game.getCollisionObjects()) {
						if (co == b) continue;
						for (HitBox hitBox1 : co.getCollisionBoxes()) {
							if (hitBox1.type == HitBox.HitBoxType.NOT_BLOCKING || !hitBox1.collides(hitBox2)) continue;
							target = co;
							break t;
						}
					}

					while (hitBox2.x != b.seismicX || hitBox2.y != b.seismicY ) {
						for (CollisionObject co : b.game.getCollisionObjects()) {
							if (co == b || co == target) continue;
							for (HitBox hitBox1 : co.getCollisionBoxes()) {
								if (hitBox1.type == HitBox.HitBoxType.NOT_BLOCKING) continue;
								if (hitBox1.collides(hitBox2)) continue test;
							}
						}
						float dx = b.seismicX - hitBox2.x;
						float dy = b.seismicY - hitBox2.y;
						double length = Math.sqrt(dx * dx + dy * dy);
						if (length == 0) {

						} else if (length > 0.5) {
							dx /= length * 2;
							dy /= length * 2;
							hitBox2.x = hitBox2.x + dx;
							hitBox2.y = hitBox2.y + dy;
						} else {
							break;
						}
					}

					if (target != null) {
						target.interact(b, new HitBox(0, 0, 0, 0), InteractionType.ATTACK);
					} else {
						b.game.getParticleSystem().createParticle(ParticleType.RED, x, y, 0, 0);
					}
				}
			} else if (this == IDLE) {
				if (currentTick == 0) {
					b.setSprite(idle_l);
					b.setMx(0);
					b.setMy(0);
				}
			} else if (this == THROW_BARREL) {
				if (currentTick %48 == 0) {
					b.setSprite(throw_barrel_l);
				}
				if (currentTick %48 == 24) {
					b.setSprite(recover_throw_l);
				}

				if (currentTick == 5 || currentTick == 53 || currentTick == 101) {
					Optional<Player> nearestPlayer = b.game.getPlayers().stream().sorted((p1, p2) -> Float.compare(b.hitBox.distance(p1.getHitBox()), b.hitBox.distance(p2.getHitBox()))).findFirst();
					float vx = 1;
					float vy = 0;
					if (nearestPlayer.isPresent()) {
						vx = nearestPlayer.get().getHitBox().getCenterX() - b.hitBox.getCenterX();
						vy = nearestPlayer.get().getHitBox().getCenterY() - b.hitBox.getCenterY();

					}
					double length = Math.sqrt(vx*vx+vy*vy);
					vx /= length/BARREL_SPEED;
					vy /= length/BARREL_SPEED;
					b.game.addGameObject(new Arrow(b.hitBox.getCenterX(), b.hitBox.getCenterY(), 0.1f, new Sprite(50, "barrel_td"), vx, vy, b));
				}
			} else if (this == FOLLOW_PLAYER || this == GRAB_PLAYER) {
				if (currentTick == 0) {
					b.setSprite(idle_l);
					b.setMaxSpeed(0.25f);
				}
				Optional<Player> nearestPlayer = b.game.getPlayers().stream().sorted((p1, p2) -> Float.compare(b.hitBox.distance(p1.getHitBox()), b.hitBox.distance(p2.getHitBox()))).findFirst();
				if (nearestPlayer.isPresent()) {
					b.setMx(nearestPlayer.get().getHitBox().x - b.getHitBox().x);
					b.setMy(nearestPlayer.get().getHitBox().y - b.getHitBox().y);
				}
			}

		}
	}
}
