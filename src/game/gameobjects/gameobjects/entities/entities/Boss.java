package game.gameobjects.gameobjects.entities.entities;

import game.Constants;
import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;
import game.util.TimeUtil;

import java.awt.*;

public class Boss extends BasicWalkingEntity {

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
	private float seismicX = -1, seismicY = -1;

	private int cooldown = 0;
	public Boss(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 2f, 2f), drawingPriority);
		setSprite(idle_l);
		setMaxSpeed(0.1f);
	}

	@Override
	public void init(Game game) {
		super.init(game);
		text = new Text(0, 0.98f, -100, life + "", 0.1f, false, 0f, 1f, Color.RED);

		game.addGameObject(text);
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

		if(cooldown > 0) cooldown--;
		if (cooldown <= 0 && Math.random() < 1 && (sprite == idle_l || sprite == idle_r)) {
			setSprite(attack_l, true);
		}

		if(sprite == attack_l || sprite == attack_r) {
			if(animationFinished()) setSprite(idle_l);
			if(getFrameOnce() >= 3 && cooldown <= 0) {
				cooldown = 200;
				seismicX = hitBox.getCenterX();
				seismicY = hitBox.getCenterY();
			}
		}

		if (cooldown > 0) {
			test: for (int i = 0; i < 100; i++) {
				float x = seismicX + (float) Math.cos((cooldown/500f + i/100f )* 2 * Math.PI)*(200-cooldown)/20;
				float y = seismicY + (float) Math.sin((cooldown / 500f + i/100f )* 2 * Math.PI)*(200-cooldown)/20;
				HitBox hitBox2 = new HitBox(x, y, 0, 0);
				while (hitBox2.x != seismicX || hitBox2.y != seismicY){
					for (CollisionObject co: game.getCollisionObjects()) {
						if (co == this) continue;
						for (HitBox hitBox1: co.getCollisionBoxes()) {
							if (hitBox1.collides(hitBox2)) continue test;
							if (hitBox1.type == HitBox.HitBoxType.NOT_BLOCKING) continue;
						}
					}
					float dx = seismicX - hitBox2.x;
					float dy = seismicY - hitBox2.y;
					double length = Math.sqrt(dx*dx+dy*dy);
					if (length == 0) {

					} else if (length > 0.5){
						dx /= length*2 ;
						dy /= length *2;
						hitBox2.x = hitBox2.x + dx;
						hitBox2.y = hitBox2.y + dy;
					} else {
						break;
					}

				}
				game.getParticleSystem().createParticle(ParticleType.RED, x, y, 0, 0);
			}
		}
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
}
