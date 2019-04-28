package game.gameobjects.gameobjects.entities.entities;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.hitbox.HitBoxDirection;
import game.data.Sprite;
import game.data.script.Tree;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.entities.BasicStaticEntity;

/**
 * kill players and zombies on collision
 */
public class Spikes extends BasicStaticEntity {
	private static Sprite idle = new Sprite(200, "floor_trap_0","floor_trap_0","floor_trap_0","floor_trap_0", "floor_trap_1", "floor_trap_2", "floor_trap_1"),
						  attack= new Sprite(200, "floor_trap_0", "floor_trap_1", "floor_trap_2", "floor_trap_3", "floor_trap_4", "floor_trap_3", "floor_trap_2", "floor_trap_1"),
						  deactivated = new Sprite(1, "floor_trap_0");

	private boolean activated = true;

	private Tree condition;

	//TODO: proper "attack"
	public Spikes(float x, float y, float drawingPriority, Tree condition) {
		super(new HitBox(x, y, 1f, 1f), drawingPriority);

		this.condition = condition;
		setSprite(activated? idle: deactivated);
	}

	@Override
	public void init(Game game) {
		this.game = game;

		this.activated = (boolean) condition.get(game);
		setSprite(activated? idle: deactivated);
	}

	@Override
	public void collide(CollisionObject gameObject, HitBoxDirection direction, float velocity, boolean source) {
		if(activated) {
			if (gameObject instanceof Zombie) {
				game.removeGameObject((GameObject) gameObject);
			}
			if(gameObject instanceof Player) {
				Player p = (Player) gameObject;
				p.addKnockBack(0.2f*(p.getHitBox().x - this.hitBox.x), 0.2f*(p.getHitBox().y - this.hitBox.y));
				game.damagePlayer(Constants.PLAYER_TRAP_DAMAGE, false);
			}
		}
	}

	@Override
	public void interact(CollisionObject gameObject, HitBox hitBox, InteractionType interactionType) {

	}

	@Override
	public void update(Game game) {
		if((boolean) condition.get(game) != activated) {
			this.activated = (boolean) condition.get(game);
			setSprite(activated? idle: deactivated);
		}
	}

	@Override
	public float getPriority() {
		return 1;
	}

	@Override
	public float getCollisionPriority() {
		return -5;
	}
}
