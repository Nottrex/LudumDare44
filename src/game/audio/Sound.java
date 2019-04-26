package game.audio;

public enum Sound {
	EP("EP"),
	COIN("Pickup_Coin11"),
	ATTACK("Hit_Hurt2"),
	JUMP("Jump7"),
	EXPLOSION("Explosion4"),
	PIANO_0("piano0"),
	PIANO_1("piano1"),
	PIANO_2("piano2");

	public String fileName;

	Sound(String fileName) {
		this.fileName = fileName;
	}
}
