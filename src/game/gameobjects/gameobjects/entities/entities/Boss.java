package game.gameobjects.gameobjects.entities.entities;

import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.gameobjects.entities.BasicWalkingEntity;

public class Boss extends BasicWalkingEntity {

	private static Sprite 	idle_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_7", "boss_attack_l_8"),
							attack_l = new Sprite(100, "boss_attack_l_0", "boss_attack_l_1", "boss_attack_l_2", "boss_attack_l_3", "boss_attack_l_4", "boss_attack_l_5", "boss_attack_l_6", "boss_attack_l_7", "boss_attack_l_8"),
							grab_l = new Sprite(100, "boss_grab_l_0", "boss_grab_l_1", "boss_grab_l_2", "boss_grab_l_3", "boss_grab_l_4"),
							throw_player = new Sprite(100, "boss_throw_player_l_0", "boss_throw_player_l_1", "boss_throw_player_l_2", "boss_throw_player_l_3"),
							throw_barrel = new Sprite(100, "boss_throw_barrel_l_0", "boss_throw_barrel_l_1", "boss_throw_barrel_l_2", "boss_throw_barrel_l_3"),
							recover_throw = new Sprite(100, "boss_throw_recover_l_0", "boss_throw_recover_l_1", "boss_throw_recover_l_2", "boss_throw_recover_l_3");

	public Boss(float x, float y, float drawingPriority) {
		super(new HitBox(x, y, 2f, 2f), drawingPriority);
		setSprite(idle_l);
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
