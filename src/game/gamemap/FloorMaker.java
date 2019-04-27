package game.gamemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloorMaker {

	private static float SPACE_2 = 0.11f, SPACE_3 = 0.5f, TURN_90 = 0.45f, TURN_180 = 0.05f, SPAWN = 0.005f, DIE = 0.0025f;

	private Random r;
	private List<Entity> entities;

	public List<int[]> floorTiles;
	public List<int[]> turns;
	public List<int[]> deaths;
	public List<int[]> spawns;
	public int[] lastDeath, firstSpawn;


	//Simular to Nuclear Throne
	public FloorMaker(int x, int y) {
		firstSpawn = new int[]{x, y};

		entities =  new ArrayList<>();
		floorTiles = new ArrayList<>();
		turns = new ArrayList<>();
		deaths = new ArrayList<>();
		floorTiles = new ArrayList<>();
		spawns = new ArrayList<>();

		r = new Random();
		add(new int[]{x,y});
		add(new int[]{x+1,y});
		add(new int[]{x+1,y+1});
		add(new int[]{x+1,y-1});
		add(new int[]{x,y+1});
		add(new int[]{x,y-1});
		add(new int[]{x-1,y+1});
		add(new int[]{x-1,y-1});
		add(new int[]{x-1,y});

		entities.add(new Entity(x, y, r.nextInt(4)));
		spawns.add(new int[]{x, y});
	}

	public void update() {
		if(entities.size() == 0) return;
		for(int i = entities.size()-1; i >= 0; i--) {
			Entity e = entities.get(i);

			float turn = (float)Math.random();
			if(turn <= TURN_90) {
				e.dir = (1+e.dir)%4;
			}
			else if(turn <= TURN_90 + TURN_180) {
				turns.add(new int[]{e.x, e.y});
				e.dir = (2+e.dir)%4;
			}

			if(turn <= TURN_90 + TURN_180)

			if(e.dir == 0) e.y -= 1;
			if(e.dir == 1) e.x += 1;
			if(e.dir == 2) e.y += 1;
			if(e.dir == 3) e.x -= 1;

			turn = (float) Math.random();
			if(turn <= SPACE_2) {
				add(new int[]{e.x, e.y});
				add(new int[]{e.x+1, e.y});
				add(new int[]{e.x, e.y+1});
				add(new int[]{e.x+1, e.y+1});
			} else if (turn <= SPACE_2 + SPACE_3) {
				add(new int[]{e.x, e.y});
				add(new int[]{e.x+1, e.y});
				add(new int[]{e.x, e.y+1});
				add(new int[]{e.x+1, e.y+1});
				add(new int[]{e.x-1, e.y});
				add(new int[]{e.x, e.y-1});
				add(new int[]{e.x-1, e.y-1});
				add(new int[]{e.x+1, e.y-1});
				add(new int[]{e.x-1, e.y+1});
			} else {
				add(new int[]{e.x, e.y});
			}

			turn = (float) Math.random();
			if(turn <= SPAWN/(float)entities.size()) {
				entities.add(new Entity(e.x, e.y, -e.dir));
				spawns.add(new int[]{e.x, e.y});
			}
			turn = (float) Math.random();
			if(floorTiles.size() > 50 && (floorTiles.size() > 150 || turn <= DIE*(float)entities.size()/2.0f)) {
				entities.remove(i);
				deaths.add(new int[]{e.x, e.y});
				lastDeath = new int[]{e.x, e.y};
			}
		}
	}

	public boolean finished() {
		return entities.size() == 0;
	}

	private void add(int[] a) {
		for(int[] i: floorTiles) {
			if(i[0] == a[0] && a[1] == i[1]) return;
		}
		floorTiles.add(a);
	}

	private class Entity {
		protected int x, y;
		protected int dir;

		public Entity(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.dir = d;
		}
	}
}
