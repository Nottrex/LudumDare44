package game.gamemap;

import game.Ability;
import game.Constants;
import game.Game;
import game.Options;
import game.data.hitbox.HitBox;
import game.data.script.Parser;
import game.data.script.Tree;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.cameracontroller.Area;
import game.gameobjects.gameobjects.entities.entities.*;
import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;
import game.util.ErrorUtil;
import game.util.FileHandler;
import game.util.SaveHandler;
import game.util.TextureHandler;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MapLoader {

	private static final File mapFolder = new File(System.getProperty("user.dir") + File.separator + "maps" + File.separator);

	/**
	 * load a map
	 * @param g context
	 * @param mapName the name of the map
	 * @return a GameMap instance containing the data of the given level
	 */
	public static GameMap load(Game g, String mapName) {
		if (mapName.startsWith(Constants.SYS_PREFIX)) {
			if (mapName.endsWith("menu")) return createMenu(g);
			if (mapName.endsWith("load")) return createLoad(g);
			if (mapName.endsWith("options")) return createOptions(g);
			if (mapName.endsWith("world")) return createLobby(g, getMaps(mapFolder, false));
			if (mapName.endsWith("save")) return createSave(g);
		}

		if(mapName.endsWith("dungeon")) {
			return GameMapFloor.generateRoom(g);
		}

		Scanner fileScanner;
		GameMap map = new GameMap();
		Map<Integer, String> textureReplacements = new HashMap<>();
		Map<Float, Map<HitBox, String>> layers = new HashMap<>();

		if (!mapName.startsWith(Constants.SYS_PREFIX)) {
			File f = new File(mapFolder, mapName + ".map");
			if (!f.exists()) {
				GameMap returnMap = load(g, Constants.SYS_PREFIX + "menu");
				Text text = new Text(-0.9f, -0.9f, -100, "Something went wrong. We send you back to the Menu", 0.05f, false, 0f, 0f, null);
				text.setTimer(300);
				returnMap.addGameObject(text);
				return returnMap;
			}

			map.setMapInfo(mapFolder.getAbsolutePath(), mapName);
			fileScanner = new Scanner(FileHandler.loadFile(f));

		} else {
			fileScanner = new Scanner(ClassLoader.getSystemResourceAsStream("res/files/systemMaps/" + mapName.replace(Constants.SYS_PREFIX, "") + ".map"));
		}

		{
			String[] lineOne = fileScanner.nextLine().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("]", "").split(";");
			Constants.PIXEL_PER_TILE = Integer.valueOf(lineOne[0]);

			Map<String, String> tags = new HashMap<>();

			int i = 1;
			while (i < lineOne.length) {
				if (lineOne[i].equals("tag")) {
					tags.put(lineOne[i + 1], lineOne[i + 2].replaceAll("\\?", ";").replaceAll("δ", ";"));
					i++;
					i++;
				}

				i++;
			}

			if (tags.containsKey("update"))
				map.setOnUpdate(Parser.loadScript(Parser.COMMAND_BLOCK, tags.get("update")));
			if (tags.containsKey("load")) map.setOnLoad(Parser.loadScript(Parser.COMMAND_BLOCK, tags.get("load")));
		}
		float tileSize = Constants.PIXEL_PER_TILE;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			line = line.replaceAll(" ", "");

			if (line.startsWith("#")) {
				String[] values = line.substring("#".length()).split("-");

				int key = Integer.parseInt(values[0]);
				if (!values[1].startsWith("textures_")) ErrorUtil.printError("No such image: " + values[1]);
				String textureName = values[1].substring("textures_".length());

				textureReplacements.put(key, textureName);
			}

			if (line.startsWith("[layer;")) {
				String[] values = line.substring("[layer;".length(), line.length() - 1).split(";");

				float drawingPriority = Float.parseFloat(values[0]);
				int width = Integer.parseInt(values[1]);
				int height = Integer.parseInt(values[2]);

				Map<HitBox, String> hitBoxList;
				if (layers.containsKey(drawingPriority)) {
					hitBoxList = layers.get(drawingPriority);
				} else {
					hitBoxList = new HashMap<>();
					layers.put(drawingPriority, hitBoxList);
				}

				for (int x = 0; x < width; x++) {
					String[] valuesLine = values[3 + x].split(",");
					for (int y = 0; y < height; y++) {
						int tile = Integer.parseInt(valuesLine[y]);

						if (tile != 0) {
							String texture = textureReplacements.get(tile);
							Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);

							HitBox hitBox = new HitBox(x, -y - textureBounds.height / tileSize, textureBounds.width / tileSize, textureBounds.height / tileSize);

							hitBoxList.put(hitBox, texture);
						}
					}
				}
			}


			if (line.startsWith("[put;")) {
				String[] values = line.substring("[put;".length(), line.length() - 1).replaceAll("\\[", "").replaceAll("]", "").split(";");

				float drawingPriority = Float.parseFloat(values[0]);
				String texture = textureReplacements.get(Integer.parseInt(values[1]));
				Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);
				float x = Float.parseFloat(values[2]);
				float y = -Float.parseFloat(values[3]) - textureBounds.height / tileSize;

				Map<String, String> tags = new HashMap<>();

				int i = 4;
				while (i < values.length) {
					if (values[i].equals("tag")) {
						tags.put(values[i + 1], values[i + 2].replaceAll("\\?", ";").replaceAll("δ", ";"));
						i++;
						i++;
					}

					i++;
				}

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
					case "boss_attack_r_7":
						map.addGameObject(new Boss(x, y, drawingPriority));
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
						map.addGameObject(new Chest(x, y, drawingPriority, tags.getOrDefault("content", "key")));
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
					case "?":
						String text = tags.getOrDefault("text", "");
						map.addGameObject(new Text(x, y, drawingPriority, text, Float.valueOf(tags.getOrDefault("size", "0.5")), true, Float.valueOf(tags.getOrDefault("anchorX", "0.5")), Float.valueOf(tags.getOrDefault("anchorY", "0")), null));
						break;
					default:
						HitBox hitBox = new HitBox(x, y, textureBounds.width / tileSize, textureBounds.height / tileSize);
						add(layers, hitBox, texture, drawingPriority-0.1f);
				}
			}

			if (line.startsWith("[area;")) {
				String[] values = line.substring("[area;".length(), line.length() - 1).replaceAll("\\[", "").replaceAll("]", "").split(";");

				float x1 = Float.parseFloat(values[0]);
				float y2 = -Float.parseFloat(values[1]);
				float x2 = Float.parseFloat(values[2]);
				float y1 = -Float.parseFloat(values[3]);

				Map<String, String> tags = new HashMap<>();

				int i = 4;
				while (i < values.length) {
					if (values[i].equals("tag")) {
						tags.put(values[i + 1], values[i + 2].replaceAll("\\?", ";").replaceAll("δ", ";"));
						i++;
						i++;
					}

					i++;
				}

				map.getCameraController().addCameraArea(new Area(x1, y1, x2, y2));
			}
		}

		for (float drawingPriority : layers.keySet()) {
			Map<HitBox, String> layer = layers.get(drawingPriority);
			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}
		return map;
	}

	/**
	 * creates a lobby map that links to many maps
 	 * @param g context
	 * @param mapNames a list of all maps that should be linked
	 * @return the gameMap representing the lobby
	 */
	private static GameMap createLobby(Game g, String... mapNames) {
		GameMap map = new GameMap();
		Map<Float, Map<HitBox, String>> layers = new HashMap<>();
		final int sectionWidth = 10;
		final int sectionHeight = 5;
		final int sectionPerLine = 3;
		final int floors = (int) Math.ceil(1.0f * mapNames.length / sectionPerLine);

		for (int y = 0; y < floors; y++) {
			for (int x = 0; x < sectionPerLine; x++) {
				map.addGameObject(new Lantern(x*sectionWidth + 2.5f, -y*sectionHeight + 1, 0.6f, new Tree((t,g2) -> true)));
				map.addGameObject(new Lantern(x*sectionWidth + 7.5f, -y*sectionHeight + 1, 0.6f, new Tree((t,g2) -> true)));
				for (int width = 0; width < sectionWidth; width++) {
					for (int height = 0; height < sectionHeight; height++) {
						int xValue = x * sectionWidth + width;
						int yValue = -y * sectionHeight + height;
						int section = y * sectionPerLine + x;

						//map objects (door and texts)
						if (section < mapNames.length && width == sectionWidth / 2) {
							if (height == 1)
								map.addGameObject(new Exit(xValue, yValue, 1, mapNames[section], null, null));
							if (height == 3)
								map.addGameObject(new Text(xValue + 0.5f, yValue, 0.7f, mapNames[section].split("/")[1], 0.5f, true, 0.5f, 0f, null));
							if (height == 4)
								map.addGameObject(new Text(xValue + 0.5f, yValue, 0.7f, String.valueOf(g.getKeyAmount(mapNames[section].split("/")[0] + "_coin_", 1)) + "/" + String.valueOf(g.getKeyAmount(mapNames[section].split("/")[0] + "_coin_")), 0.5f, true, 0.5f, 0f, null));
						}

						//borders
						if (y == 0 && height == sectionHeight - 1)
							add(layers, new HitBox(xValue, yValue + 1, 1f, 1f), "block_stone_top", 0.5f);
						if (y == floors - 1 && height == 0)
							add(layers, new HitBox(xValue, yValue, 1f, 1f), "block_stone_bottom", 0.5f);
						if (x == 0 && width == 0 && y != 0)
							add(layers, new HitBox(xValue - 1, yValue, 1f, 1f), "block_stone_left", 0.5f);
						if (x == sectionPerLine - 1 && width == sectionWidth - 1)
							add(layers, new HitBox(xValue + 1, yValue, 1f, 1f), "block_stone_right", 0.5f);

						//background
						add(layers, new HitBox(xValue, yValue, 1f, 1f), "block_stone_middle", 100);
						add(layers, new HitBox(xValue, yValue, 1f, 1f), "black_5", 99);

						//camera
						if (x == 0 && width == 0 && height == 0)
							map.getCameraController().addCameraArea(new Area(0, yValue, sectionPerLine * sectionWidth, yValue + sectionHeight));
					}
				}
			}
		}
		map.addGameObject(new Lantern(-sectionWidth + 1.5f, 1, 0.6f, new Tree((t,g2) -> true)));
		map.addGameObject(new Lantern(-sectionWidth + 6.5f, 1, 0.6f, new Tree((t,g2) -> true)));
		for (int x = 0; x < sectionWidth; x++) {
			for (int y = 0; y < sectionHeight; y++) {
				int xValue = -x - 1;
				int yValue = y;

				//background
				add(layers, new HitBox(xValue, yValue, 1f, 1f), "block_stone_middle", 100);
				add(layers, new HitBox(xValue, yValue, 1f, 1f), "black_5", 99);

				//borders
				if (y == 0) add(layers, new HitBox(xValue, yValue, 1f, 1f), "block_stone_bottom", 0.52f);
				if (y == sectionHeight - 1)
					add(layers, new HitBox(xValue, yValue + 1, 1f, 1f), "block_stone_top", 0.52f);
				if (x == sectionWidth - 1) add(layers, new HitBox(xValue, yValue, 1f, 1f), "block_stone_left", 0.52f);

				//exit
				if (x == sectionWidth / 2 && y == 1) {
					map.addGameObject(new Exit(xValue, yValue, 0.6f, Constants.SYS_PREFIX + "save", null, null));
					map.addGameObject(new Text(xValue + 0.5f, yValue + 2f, 0.6f, "save", 0.5f, true, 0.5f, 0f, null));
					map.setSpawnPoint(xValue, yValue, 0.5f);
				}

				//camera
				if (x == 0 && y == 0)
					map.getCameraController().addCameraArea(new Area(0, 0, -sectionWidth, sectionHeight));
			}
		}

		for (float drawingPriority : layers.keySet()) {
			Map<HitBox, String> layer = layers.get(drawingPriority);
			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}
		return map;
	}

	/**
	 * creates a save map
	 * @param g context
	 * @return a gameMap representing the save level
	 */
	private static GameMap createSave(Game g) {
		GameMap map = load(g, Constants.SYS_PREFIX + "savesMap");
		Map<Integer, String> saves = SaveHandler.getSaves();

		for (int i = 1; i < 4; i++) {
			final int slot = i;
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.mm");
			map.addGameObject(new Text(i * 8 - 0.5f, -11, 1, saves.getOrDefault(i, "empty"), 0.5f, true, 0.5f, 0f, null));
			map.addGameObject(new Text(i * 8 - 0.5f, -10, 1, "slot " + i, 0.5f, true, 0.5f, 0f, null));
			map.addGameObject(new Exit(i * 8 - 1, -13, 1, Constants.SYS_PREFIX + "menu", new Tree((tree, game) -> {
				game.saveValues(slot + "-" + dateFormat.format(new Date()));
				game.clearValues();
				return null;
			}), null));
		}
		map.setSpawnPoint(15, -16, 0.5f);
		map.addGameObject(new Exit(15, -16, 0.6f, Constants.SYS_PREFIX + "world", null, null));
		return map;
	}

	/**
	 * creates an options map
	 * @param g context
	 * @return a gameMap representing the options level
	 */
	private static GameMap createOptions(Game g) {
		GameMap map = load(g, Constants.SYS_PREFIX + "optionsMap");

		List<GameObject> lever = map.getGameObjects().stream().filter(go -> go instanceof Lever).sorted((g1, g2) -> Float.compare(((Lever) g1).getCollisionBoxes().get(0).y, ((Lever) g2).getCollisionBoxes().get(0).y)).collect(Collectors.toList());

		{
			Lever fullscreenLever = (Lever) lever.get(0);

			fullscreenLever.setEnabled(new Tree((t, g2) -> true));
			fullscreenLever.setOnActivate(new Tree((t, g2) -> {
				Options.fullscreen = true;
				return null;
			}));
			fullscreenLever.setOnDeactivate(new Tree((t, g2) -> {
				Options.fullscreen = false;
				return null;
			}));
			fullscreenLever.setActivated(Options.fullscreen);

			HitBox textBox = fullscreenLever.getCollisionBoxes().get(0).clone();
			textBox.move(0, 1f);
			map.addGameObject(new Text(textBox.getCenterX(), textBox.getCenterY(), 0, "fullscreen", 0.5f, true, 0.5f, 0.5f, null));
		}
		{
			Lever soundLever = (Lever) lever.get(1);

			soundLever.setEnabled(new Tree((t, g2) -> true));
			soundLever.setOnActivate(new Tree((t, g2) -> {
				Options.stereo = true;
				return null;
			}));
			soundLever.setOnDeactivate(new Tree((t, g2) -> {
				Options.stereo = false;
				return null;
			}));
			soundLever.setActivated(Options.stereo);

			HitBox textBox = soundLever.getCollisionBoxes().get(0).clone();
			textBox.move(0, 1f);
			map.addGameObject(new Text(textBox.getCenterX(), textBox.getCenterY(), 0, "3d sound", 0.5f, true, 0.5f, 0.5f, null));
		}

		Slider musicSlider = new Slider(map.getSpawnX(), map.getSpawnX() + 8, Options.musicVolume, map.getSpawnY() + 4, 0.52f, null);
		musicSlider.setOnRelocate(new Tree(((tree, game) -> {
			Options.musicVolume = musicSlider.getSliderValue();
			return null;
		})));
		map.addGameObject(musicSlider);
		map.addGameObject(new Text(map.getSpawnX() + 2, map.getSpawnY() + 5.5f, 0.52f, "music volume", 0.5f, true, 0f, 0f, null));

		Slider effectSlider = new Slider(map.getSpawnX(), map.getSpawnX() + 8, Options.effectVolume, map.getSpawnY() + 8, 0.52f, null);
		effectSlider.setOnRelocate(new Tree(((tree, game) -> {
			Options.effectVolume = effectSlider.getSliderValue();
			return null;
		})));
		map.addGameObject(effectSlider);
		map.addGameObject(new Text(map.getSpawnX() + 2, map.getSpawnY() + 9.5f, 0.52f, "effect volume", 0.5f, true, 0f, 0f, null));


		((Exit) map.getGameObjects().stream().filter(go -> go instanceof Exit).findAny().get()).setTargetMap(Constants.SYS_PREFIX + "menu");
		((Exit) map.getGameObjects().stream().filter(go -> go instanceof Exit).findAny().get()).setOnEntrance(new Tree(((tree, game) -> {
			Options.applyOptions(game);
			return null;
		})));
		return map;
	}

	/**
	 * creates a menu map
	 * @param g context
	 * @return a gameMap representing the menu level
	 */
	private static GameMap createMenu(Game g) {
		GameMap map = load(g, Constants.SYS_PREFIX + "menuMap");

		Exit exitLoad = (Exit) map.getGameObjects().stream().filter(go -> go instanceof Exit).filter(go -> ((Exit) go).getTargetMap().endsWith("2")).findAny().get();
		exitLoad.setTargetMap(Constants.SYS_PREFIX + "load");
		exitLoad.setOnEntrance(new Tree(((tree, game) -> {
			game.clearValues();
			return null;
		})));
		Text textLoad = new Text(exitLoad.getCollisionBoxes().get(0).getCenterX(), exitLoad.getCollisionBoxes().get(0).y + 2, -0.25f, "LOAD", 0.5f, true, 0.5f, 0.5f, null);
		map.addGameObject(textLoad);

		Exit exitNew = (Exit) map.getGameObjects().stream().filter(go -> go instanceof Exit).filter(go -> ((Exit) go).getTargetMap().endsWith("3")).findAny().get();
		exitNew.setTargetMap(Constants.SYS_PREFIX + "world");
		exitNew.setOnEntrance(new Tree(((tree, game) -> {
			game.clearValues();
			loadAllMaps(game);
			return null;
		})));
		Text textNew = new Text(exitNew.getCollisionBoxes().get(0).getCenterX(), exitNew.getCollisionBoxes().get(0).y + 2, -0.25f, "NEW", 0.5f, true, 0.5f, 0.5f, null);
		map.addGameObject(textNew);

		Exit exitOptions = (Exit) map.getGameObjects().stream().filter(go -> go instanceof Exit).filter(go -> ((Exit) go).getTargetMap().endsWith("1")).findAny().get();
		exitOptions.setTargetMap(Constants.SYS_PREFIX + "options");
		Text textOptions = new Text(exitOptions.getCollisionBoxes().get(0).getCenterX(), exitOptions.getCollisionBoxes().get(0).y + 2, -0.25f, "OPTIONS", 0.5f, true, 0.5f, 0.5f, null);
		map.addGameObject(textOptions);

		map.addGameObject(new BeerBarrel(map.getSpawnX() + 1, map.getSpawnY(), 0.5f));
		map.addGameObject(new Text(map.getSpawnX() + 0.5f, map.getSpawnY() - 3, -100, "press <w> or <key_up> or <button_a> to spawn", 0.5f,true, 0.5f, 0f, null));
		return map;
	}

	/**
	 * creates the load map
	 * @param g context
	 * @return a gameMap representing the load level
	 */
	private static GameMap createLoad(Game g) {
		GameMap map = load(g, Constants.SYS_PREFIX + "savesMap");
		Map<Integer, String> saves = SaveHandler.getSaves();

		for (int i = 1; i < 4; i++) {
			map.addGameObject(new Text(i * 8 - 0.5f, -11, 1, saves.getOrDefault(i, "empty"), 0.5f, true, 0.5f, 0f, null));
			map.addGameObject(new Text(i * 8 - 0.5f, -10, 1, "slot " + i, 0.5f, true, 0.5f, 0f, null));
			if (saves.containsKey(i)) {
				final int slot = i;
				final String date = saves.get(i);
				map.addGameObject(new Exit(i * 8 - 1, -13, 1, Constants.SYS_PREFIX + "world", new Tree((tree, game) -> {
					game.loadValues(slot + "-" + date);
					return null;
				}), null));
			} else {
				//locked door
			}
		}
		map.setSpawnPoint(15, -16, 0.5f);
		map.addGameObject(new Exit(15, -16, 0.6f, Constants.SYS_PREFIX + "menu", null, null));
		return map;
	}

	/**
	 * adds a texture to a a list of layers
	 * @param layers the list of layers
	 * @param hitBox the hitBox of the texture
	 * @param texture the texture name
	 * @param drawingPriority the drawing priority of the texture
	 */
	protected static void add(Map<Float, Map<HitBox, String>> layers, HitBox hitBox, String texture, float drawingPriority) {
		if (layers.containsKey(drawingPriority)) {
			layers.get(drawingPriority).put(hitBox, texture);
		} else {
			Map<HitBox, String> layer = new HashMap<>();
			layer.put(hitBox, texture);
			layers.put(drawingPriority, layer);
		}
	}

	/**
	 * returns all files found in a folder
	 * @param folder the start folder
	 * @param all whether all or only the map files should be returned
	 * @return a String[] containing all of them
	 */
	private static String[] getMaps(File folder, boolean all) {
		File[] packages = folder.listFiles(File::isDirectory);

		List<String> maps = new ArrayList<>();

		if (packages == null) return maps.toArray(new String[0]);
		for (File f : packages) {
			File initialMap = new File(f.getAbsolutePath() + "/" + f.getName() + ".map");
			if (initialMap.exists()) maps.add(f.getName() + "/" + f.getName());
			if (all) {
				for (File f2 : f.listFiles()) {
					if (f2.getName().endsWith(".map")) maps.add(f.getName() + "/" + f2.getName().replace(".map", ""));
				}
			}
		}
		return maps.toArray(new String[0]);
	}

	/**
	 * loads all maps once
	 * @param game context
	 */
	public static void loadAllMaps(Game game) {
		String[] maps = getMaps(mapFolder, true);

		for (String mapName : maps) {
			try {
				load(game, mapName);
			} catch (Exception e) {
				ErrorUtil.printError("Loading Map " + mapName);
			}
		}
	}
}
