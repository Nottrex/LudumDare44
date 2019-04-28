package game.gameobjects.gameobjects.entities.entities;

import game.Game;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.CollisionObject;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;
import game.gameobjects.gameobjects.particle.ParticleType;

import java.awt.*;

public class Boss extends BasicWalkingEntity {

	private static Sprite 	idle_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_7", "boss_attack_l_8"),
							attack_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_1", "boss_attack_l_2", "boss_attack_l_3", "boss_attack_l_4", "boss_attack_l_5", "boss_attack_l_6", "boss_attack_l_7", "boss_attack_l_8"),
							grab_l = new Sprite(100, "boss_grab_l_0", "boss_grab_l_1", "boss_grab_l_2", "boss_grab_l_3", "boss_grab_l_4"),
							throw_player = new Sprite(100, "boss_throw_player_l_0", "boss_throw_player_l_1", "boss_throw_player_l_2", "boss_throw_player_l_3"),
							throw_barrel = new Sprite(100, "boss_throw_barrel_l_0", "boss_throw_barrel_l_1", "boss_throw_barrel_l_2", "boss_throw_barrel_l_3"),
							recover_throw = new Sprite(100, "boss_throw_recover_l_0", "boss_throw_recover_l_1", "boss_throw_recover_l_2", "boss_throw_recover_l_3");

	private int life = 10;
	private Text text;

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

		cooldown--;
		if (cooldown <= 0 && Math.random() < 1) {
			cooldown = 200;
			setSprite(attack_l);
		}

		if (cooldown > 0) {
			test: for (int i = 0; i < 100; i++) {
				float x = hitBox.getCenterX() + (float) Math.cos((cooldown/500f + i/100f )* 2 * Math.PI)*(200-cooldown)/20;
				float y = hitBox.getCenterY() + (float) Math.sin((cooldown / 500f + i/100f )* 2 * Math.PI)*(200-cooldown)/20;
				HitBox hitBox2 = new HitBox(x, y, 0, 0);
				while (hitBox2.x != hitBox.x || hitBox2.y != hitBox.y){
					for (CollisionObject co: game.getCollisionObjects()) {
						if (co == this) continue;
						for (HitBox hitBox1: co.getCollisionBoxes()) {
							if (hitBox1.type == HitBox.HitBoxType.NOT_BLOCKING) continue;
							if (hitBox1.collides(hitBox2)) continue test;
						}
					}
					float dx = hitBox.x - hitBox2.x;
					float dy = hitBox.y - hitBox2.y;
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
		} else {
			setSprite(idle_l);
		}

	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {
		super.interact(gameObject, hitBox, interactionType);

		if (gameObject instanceof Player && interactionType == InteractionType.ATTACK) life--;
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
