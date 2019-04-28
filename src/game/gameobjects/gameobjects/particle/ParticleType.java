package game.gameobjects.gameobjects.particle;

import game.data.Sprite;

public enum ParticleType {
	EXPLOSION(new Sprite(100, "explosion_1", "explosion_2", "explosion_3", "explosion_4", "explosion_5", "explosion_6", "explosion_7"), 30, 2f, 2f, false),
	RED(new Sprite(100, "particle_2"), 15, 0.125f, 0.125f, false),
	LIGHT_RED(new Sprite(100, "particle_1"), 15, 0.125f, 0.125f, false),
	WHITE(new Sprite(100, "particle_0"), 15, 0.125f, 0.125f, false),
	DARK_RED(new Sprite(100, "particle_3"), 15, 0.125f, 0.125f, false),
	POTION(new Sprite(100, "potion"), 30, 1f, 1f, false),
	KEY(new Sprite(100, "key"), 30, 1f, 1f, false);

	private int lifeTime;
	private boolean gravity;
	private Sprite sprite;
	private float width, height;

	ParticleType(Sprite sprite, int lifeTime, float width, float height, boolean gravity) {
		this.lifeTime = lifeTime;
		this.gravity = gravity;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public boolean isGravity() {
		return gravity;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
}
