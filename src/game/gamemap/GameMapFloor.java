package game.gamemap;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.script.Parser;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.cameracontroller.Area;
import game.gameobjects.gameobjects.entities.entities.*;
import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;
import game.util.MathUtil;
import game.util.TextureHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMapFloor {
	private List<GameMap> gameMaps;
	private GameMap start;

	public GameMapFloor(int size) {


	}

	//TODO: Pretty bad, should be changed
	public static GameMap generateRoom(Game g) {
		GameMap map = new GameMap();
		map.setMapInfo("", "");
		Map<Float, Map<HitBox, String>> layers = new HashMap<>();
		Map<String, String> tags = new HashMap<>();	//TODO: Might change this one

		FloorMaker m = new FloorMaker(0, 0);
		while(!m.finished()) m.update();

		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
		for(int[] i: m.floorTiles) {
			if(i[0] < minX) minX = i[0];
			if(i[1] < minY) minY = i[1];
			if(i[0] > maxX) maxX = i[0];
			if(i[1] > maxY) maxY = i[1];
		}

		boolean[][] bMap = new boolean[maxX-minX+3][maxY-minY+3];
		for(int[] i: m.floorTiles) {
			bMap[i[0]-minX+1][i[1]-minY+1] = true;
		}

		int cy = m.lastDeath[1]-minY, cx = m.lastDeath[0]-minX;
		while(bMap[cx][cy]) cy++;
		map.addGameObject(new Exit(cx, cy, 0.6f, "dungeon", null, Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s+1);", "door", "door"))));
		map.addGameObject(new DoorShade(cx, cy-1, 0.46f, "t", Parser.loadScript(Parser.BOOLEAN, "#door")));
		g.setValue("door", 0);

		for(int x = 0; x < bMap.length; x++) {
			for(int y = 0; y < bMap[0].length; y++) {
				if(bMap[x][y]) add(g, map, layers, tags, "floor_1", x, y, 0.7f, null);
				else {
					if(x == cx && y == cy) {
						add(g, map, layers, tags, "block_border_" + getIndex(bMap, x, y), x, y+2, 0.3f, null);
						add(g, map, layers, tags, "wall", x, y+1, 0.5f, null);
						continue;
					}
					add(g, map, layers, tags, "block_border_" + getIndex(bMap, x, y), x, y+1, 0.3f, null);
					add(g, map, layers, tags, "wall", x, y, 0.5f, null);
				}
			}
		}

		int chests = 0;
		for(int[] i: m.deaths) {
			if((bMap[i[0]-minX][i[1]-minY] && (Math.random() < 0.15 || chests == 0) && MathUtil.distance(i[0]-minX, i[1]-minX, -minX, -minY+2) > 5)) {
				add(g, map, layers, tags, "chest_open", i[0]-minX, i[1]-minY, 0.4f, chests == 0? "key": null);
				chests++;
			}
		}
		if(chests == 0) g.getPlayers().get(0).addItem("key");


		for (float drawingPriority : layers.keySet()) {
			Map<HitBox, String> layer = layers.get(drawingPriority);
			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}

		for(int[] i: m.turns) {
			double f = Math.random();
			if(f < 0.25 && MathUtil.distance(i[0]-minX, i[1]-minX, -minX, -minY+2) > 5) add(g, map, layers, tags, "skeleton", i[0]-minX, i[1]-minY+1, 0.5f, null);
			else if(f < 0.5 && MathUtil.distance(i[0]-minX, i[1]-minX, -minX, -minY+2) > 5) add(g, map, layers, tags, "zombie_walking_r_0", i[0]-minX, i[1]-minY+1, 0.5f, null);
		}

		map.getCameraController().addCameraArea(new Area(0, 0, maxX-minX+5, maxY-minY+5));	//TODO: this
		map.setSpawnPoint(-minX, -minY+0.5f, 0.4f);
		return map;
	}

	private static int getIndex(boolean[][] tileNames, int x, int y) {
		int out=0;
		if (y == 0 || !tileNames[x][y - 1])
			out ^= 4;
		if (y == tileNames[0].length - 1 || !tileNames[x][y + 1])
			out ^= 12;
		if (x == 0 || !tileNames[x - 1][y])
			out ^= 3;
		if (x == tileNames.length - 1 || !tileNames[x + 1][y])
			out ^= 1;
		return out;
	}

	private static void add(Game g, GameMap map, Map<Float, Map<HitBox, String>> layers, Map<String, String> tags, String texture, int x, int y, float drawingPriority, String extra) {
		Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);

		switch (texture) {
			case "player_idle_l":
			case "player_idle_r":
			case "player_walking_l_1":
			case "player_walking_l_2":
			case "player_walking_r_1":
			case "player_walking_r_2":
			case "player_attack_l_0":
			case "player_attack_l_1":
			case "player_attack_l_2":
			case "player_attack_l_3":
			case "player_attack_l_4":
			case "player_attack_l_5":
			case "player_attack_l_6":
			case "player_attack_r_0":
			case "player_attack_r_1":
			case "player_attack_r_2":
			case "player_attack_r_3":
			case "player_attack_r_4":
			case "player_attack_r_5":
			case "player_attack_r_6":
				map.setSpawnPoint(x, y, drawingPriority);
				map.getCameraController().setSpawn(x, y);
				break;
			case "door_side":
			case "wall_door_closed":
				String target = Constants.SYS_PREFIX + "world";
				if (tags.containsKey("target")) target = map.getDirectory() + "/" + tags.get("target");
				String tag2 = tags.getOrDefault("tag", "door");
				map.addGameObject(new Exit(x, y, drawingPriority, target, null, Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s+1);", tag2, tag2))));
				g.setValue(tag2, 0);
				break;
			case "floor_door_closed_t":
			case "floor_door_closed_d":
			case "floor_door_closed_l":
			case "floor_door_closed_r":
			case "floor_door_open_t":
			case "floor_door_open_d":
			case "floor_door_open_l":
			case "floor_door_open_r":
				map.addGameObject(new DoorShade(x, y, drawingPriority,  texture.charAt(texture.length()-1)+"", Parser.loadScript(Parser.BOOLEAN, tags.getOrDefault("condition", "#door"))));
				break;
			case "floor_trap_0":
			case "floor_trap_1":
			case "floor_trap_2":
			case "floor_trap_3":
			case "floor_trap_4":
				map.addGameObject(new Spikes(x, y, drawingPriority, Parser.loadScript(Parser.BOOLEAN, tags.getOrDefault("condition", "#lever"))));
				break;
			case "chest_open":
			case "chest_closed":
				map.addGameObject(new Chest(x, y, drawingPriority, extra == null? "potion": extra));
				break;
			case "lever_on":
			case "lever_off":
				String tag = tags.getOrDefault("tag", "lever");
				map.addGameObject(new Lever(x, y, drawingPriority, false, Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s+1);", tag, tag)), Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s-1);", tag, tag)), null));
				g.setValue(tag, 0);
				break;
			case "zombie_walking_l_0":
			case "zombie_walking_l_1":
			case "zombie_walking_l_2":
			case "zombie_walking_r_0":
			case "zombie_walking_r_1":
			case "zombie_walking_r_2":
			case "zombie_l_idle_0":
			case "zombie_r_idle_0":
				map.addGameObject(new Zombie(x, y, drawingPriority, Parser.loadScript(Parser.COMMAND_BLOCK, tags.getOrDefault("onDead", ""))));
				break;
			case "skeleton":
				map.addGameObject(new Skeleton(x, y, drawingPriority, Parser.loadScript(Parser.COMMAND_BLOCK, tags.getOrDefault("onDead", ""))));
				break;
			case "a":
			case "b":
			case "c":
			case "d":
			case "e":
			case "f":
			case "g":
			case "h":
			case "i":
			case "j":
			case "k":
			case "l":
			case "m":
			case "n":
			case "o":
			case "p":
			case "q":
			case "r":
			case "s":
			case "t":
			case "u":
			case "v":
			case "w":
			case "x":
			case "y":
			case "z":
				map.addGameObject(new Text(x, y, drawingPriority, tags.getOrDefault("text", ""), Float.valueOf(tags.getOrDefault("size", "0.5")), true, Float.valueOf(tags.getOrDefault("anchorX", "0")), Float.valueOf(tags.getOrDefault("anchorY", "0")), null));
				break;
			default:
				HitBox hitBox = new HitBox(x, y, textureBounds.width / 8.0f, textureBounds.height / 8.0f);
				MapLoader.add(layers, hitBox, texture, drawingPriority);
		}
	}
}
