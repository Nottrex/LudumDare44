package game.gamemap;

import java.util.List;

public class GameMapFloor {
	private List<GameMap> gameMaps;
	private GameMap start;

	public GameMapFloor(int size) {


	}

	public static GameMap generateRoom() {
		GameMap map = new GameMap();
		return map;
	}
}
