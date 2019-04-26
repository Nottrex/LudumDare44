package game.window;

import game.Constants;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class Keyboard {

	private Map<Integer, Float> keys;			//Stores how much every key is pressed

	/**
	 * creates a Keyboard for the given glfw window
	 * @param window the glfw window
	 */
	public Keyboard(long window) {
		this.keys = new HashMap<>();

		GLFW.glfwSetKeyCallback(window, (window_, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_UNKNOWN) return;

			if (action == GLFW.GLFW_RELEASE) {
				keys.put(30*16 + key, 0f);
			} else if (action == GLFW.GLFW_PRESS) {
				keys.put(30*16 + key, 1f);
			}
		});
	}

	/**
	 * checks if a key is pressed
	 * @param keyCode the code of the key
	 * @return if the given keyCode is pressed more than 0.5
	 */
	public boolean isPressed(int keyCode) {
		return getPressed(keyCode) >= 0.5f;
	}

	/**
	 * checks how much a key is pressed
	 * @param keyCode the code of the key
	 * @return how much the given keyCode is pressed, or 0 if it was never pressed
	 */
	public float getPressed(int keyCode) {
		return keys.getOrDefault(keyCode, 0f);
	}

	/**
	 * updates the keyboard and checks for pressed keys 
	 */
	public void update() {
		GLFW.glfwPollEvents();

		for (int c = 0; c < 16; c++) {
			if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1 + c) && GLFW.glfwJoystickIsGamepad(GLFW.GLFW_JOYSTICK_1 + c)) {
				ByteBuffer values = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1 + c);
				for (int i = 0; i < values.limit(); i++) {
					keys.put(30*c + i, 1f * values.get(i));
				}

				FloatBuffer values2 = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1 + c);
				for (int j = 0; j < values2.limit(); j++) {
					if (j == GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER || j == GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER) {
						keys.put(30*c + 15 + j, (values2.get(j) + 1) / 2);
					} else {
						keys.put(30*c + 15 + j, -Math.min(0, values2.get(j) + Constants.DEAD_ZONE) / (1 - Constants.DEAD_ZONE));
						keys.put(30*c + 21 + j, Math.max(0, values2.get(j) - Constants.DEAD_ZONE) / (1 - Constants.DEAD_ZONE));
					}
				}
			}
		}

	}
	
	//All keys:
	
	//GamePad 1
	public static final int
			GAMEPAD_1_BUTTON_A = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_1_BUTTON_B = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_1_BUTTON_X = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_1_BUTTON_Y = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_1_BUTTON_LEFT_BUMPER = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_1_BUTTON_RIGHT_BUMPER = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_1_BUTTON_BACK = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_1_BUTTON_START = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_1_BUTTON_GUIDE = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_1_BUTTON_LEFT_THUMB = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_1_BUTTON_RIGHT_THUMB = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_1_BUTTON_DPAD_UP = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_1_BUTTON_DPAD_RIGHT = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_1_BUTTON_DPAD_DOWN = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_1_BUTTON_DPAD_LEFT = 30 * 0 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_1_LEFT_AXIS_LEFT = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_1_LEFT_AXIS_UP = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_1_RIGHT_AXIS_LEFT = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_1_RIGHT_AXIS_UP = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_1_LEFT_TRIGGER = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_1_RIGHT_TRIGGER = 15 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_1_LEFT_AXIS_RIGHT = 21 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_1_LEFT_AXIS_DOWN = 21 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_1_RIGHT_AXIS_RIGHT = 21 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_1_RIGHT_AXIS_DOWN = 21 + 30 * 0 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 2
	public static final int
			GAMEPAD_2_BUTTON_A = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_2_BUTTON_B = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_2_BUTTON_X = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_2_BUTTON_Y = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_2_BUTTON_LEFT_BUMPER = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_2_BUTTON_RIGHT_BUMPER = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_2_BUTTON_BACK = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_2_BUTTON_START = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_2_BUTTON_GUIDE = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_2_BUTTON_LEFT_THUMB = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_2_BUTTON_RIGHT_THUMB = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_2_BUTTON_DPAD_UP = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_2_BUTTON_DPAD_RIGHT = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_2_BUTTON_DPAD_DOWN = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_2_BUTTON_DPAD_LEFT = 30 * 1 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_2_LEFT_AXIS_LEFT = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_2_LEFT_AXIS_UP = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_2_RIGHT_AXIS_LEFT = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_2_RIGHT_AXIS_UP = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_2_LEFT_TRIGGER = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_2_RIGHT_TRIGGER = 15 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_2_LEFT_AXIS_RIGHT = 21 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_2_LEFT_AXIS_DOWN = 21 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_2_RIGHT_AXIS_RIGHT = 21 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_2_RIGHT_AXIS_DOWN = 21 + 30 * 1 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 3
	public static final int
			GAMEPAD_3_BUTTON_A = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_3_BUTTON_B = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_3_BUTTON_X = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_3_BUTTON_Y = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_3_BUTTON_LEFT_BUMPER = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_3_BUTTON_RIGHT_BUMPER = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_3_BUTTON_BACK = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_3_BUTTON_START = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_3_BUTTON_GUIDE = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_3_BUTTON_LEFT_THUMB = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_3_BUTTON_RIGHT_THUMB = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_3_BUTTON_DPAD_UP = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_3_BUTTON_DPAD_RIGHT = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_3_BUTTON_DPAD_DOWN = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_3_BUTTON_DPAD_LEFT = 30 * 2 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_3_LEFT_AXIS_LEFT = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_3_LEFT_AXIS_UP = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_3_RIGHT_AXIS_LEFT = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_3_RIGHT_AXIS_UP = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_3_LEFT_TRIGGER = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_3_RIGHT_TRIGGER = 15 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_3_LEFT_AXIS_RIGHT = 21 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_3_LEFT_AXIS_DOWN = 21 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_3_RIGHT_AXIS_RIGHT = 21 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_3_RIGHT_AXIS_DOWN = 21 + 30 * 2 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 4
	public static final int
			GAMEPAD_4_BUTTON_A = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_4_BUTTON_B = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_4_BUTTON_X = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_4_BUTTON_Y = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_4_BUTTON_LEFT_BUMPER = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_4_BUTTON_RIGHT_BUMPER = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_4_BUTTON_BACK = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_4_BUTTON_START = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_4_BUTTON_GUIDE = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_4_BUTTON_LEFT_THUMB = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_4_BUTTON_RIGHT_THUMB = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_4_BUTTON_DPAD_UP = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_4_BUTTON_DPAD_RIGHT = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_4_BUTTON_DPAD_DOWN = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_4_BUTTON_DPAD_LEFT = 30 * 3 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_4_LEFT_AXIS_LEFT = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_4_LEFT_AXIS_UP = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_4_RIGHT_AXIS_LEFT = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_4_RIGHT_AXIS_UP = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_4_LEFT_TRIGGER = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_4_RIGHT_TRIGGER = 15 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_4_LEFT_AXIS_RIGHT = 21 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_4_LEFT_AXIS_DOWN = 21 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_4_RIGHT_AXIS_RIGHT = 21 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_4_RIGHT_AXIS_DOWN = 21 + 30 * 3 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 5
	public static final int
			GAMEPAD_5_BUTTON_A = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_5_BUTTON_B = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_5_BUTTON_X = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_5_BUTTON_Y = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_5_BUTTON_LEFT_BUMPER = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_5_BUTTON_RIGHT_BUMPER = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_5_BUTTON_BACK = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_5_BUTTON_START = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_5_BUTTON_GUIDE = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_5_BUTTON_LEFT_THUMB = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_5_BUTTON_RIGHT_THUMB = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_5_BUTTON_DPAD_UP = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_5_BUTTON_DPAD_RIGHT = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_5_BUTTON_DPAD_DOWN = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_5_BUTTON_DPAD_LEFT = 30 * 4 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_5_LEFT_AXIS_LEFT = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_5_LEFT_AXIS_UP = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_5_RIGHT_AXIS_LEFT = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_5_RIGHT_AXIS_UP = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_5_LEFT_TRIGGER = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_5_RIGHT_TRIGGER = 15 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_5_LEFT_AXIS_RIGHT = 21 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_5_LEFT_AXIS_DOWN = 21 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_5_RIGHT_AXIS_RIGHT = 21 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_5_RIGHT_AXIS_DOWN = 21 + 30 * 4 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 6
	public static final int
			GAMEPAD_6_BUTTON_A = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_6_BUTTON_B = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_6_BUTTON_X = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_6_BUTTON_Y = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_6_BUTTON_LEFT_BUMPER = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_6_BUTTON_RIGHT_BUMPER = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_6_BUTTON_BACK = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_6_BUTTON_START = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_6_BUTTON_GUIDE = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_6_BUTTON_LEFT_THUMB = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_6_BUTTON_RIGHT_THUMB = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_6_BUTTON_DPAD_UP = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_6_BUTTON_DPAD_RIGHT = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_6_BUTTON_DPAD_DOWN = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_6_BUTTON_DPAD_LEFT = 30 * 5 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_6_LEFT_AXIS_LEFT = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_6_LEFT_AXIS_UP = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_6_RIGHT_AXIS_LEFT = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_6_RIGHT_AXIS_UP = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_6_LEFT_TRIGGER = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_6_RIGHT_TRIGGER = 15 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_6_LEFT_AXIS_RIGHT = 21 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_6_LEFT_AXIS_DOWN = 21 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_6_RIGHT_AXIS_RIGHT = 21 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_6_RIGHT_AXIS_DOWN = 21 + 30 * 5 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 7
	public static final int
			GAMEPAD_7_BUTTON_A = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_7_BUTTON_B = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_7_BUTTON_X = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_7_BUTTON_Y = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_7_BUTTON_LEFT_BUMPER = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_7_BUTTON_RIGHT_BUMPER = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_7_BUTTON_BACK = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_7_BUTTON_START = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_7_BUTTON_GUIDE = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_7_BUTTON_LEFT_THUMB = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_7_BUTTON_RIGHT_THUMB = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_7_BUTTON_DPAD_UP = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_7_BUTTON_DPAD_RIGHT = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_7_BUTTON_DPAD_DOWN = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_7_BUTTON_DPAD_LEFT = 30 * 6 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_7_LEFT_AXIS_LEFT = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_7_LEFT_AXIS_UP = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_7_RIGHT_AXIS_LEFT = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_7_RIGHT_AXIS_UP = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_7_LEFT_TRIGGER = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_7_RIGHT_TRIGGER = 15 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_7_LEFT_AXIS_RIGHT = 21 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_7_LEFT_AXIS_DOWN = 21 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_7_RIGHT_AXIS_RIGHT = 21 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_7_RIGHT_AXIS_DOWN = 21 + 30 * 6 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 8
	public static final int
			GAMEPAD_8_BUTTON_A = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_8_BUTTON_B = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_8_BUTTON_X = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_8_BUTTON_Y = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_8_BUTTON_LEFT_BUMPER = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_8_BUTTON_RIGHT_BUMPER = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_8_BUTTON_BACK = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_8_BUTTON_START = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_8_BUTTON_GUIDE = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_8_BUTTON_LEFT_THUMB = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_8_BUTTON_RIGHT_THUMB = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_8_BUTTON_DPAD_UP = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_8_BUTTON_DPAD_RIGHT = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_8_BUTTON_DPAD_DOWN = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_8_BUTTON_DPAD_LEFT = 30 * 7 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_8_LEFT_AXIS_LEFT = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_8_LEFT_AXIS_UP = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_8_RIGHT_AXIS_LEFT = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_8_RIGHT_AXIS_UP = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_8_LEFT_TRIGGER = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_8_RIGHT_TRIGGER = 15 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_8_LEFT_AXIS_RIGHT = 21 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_8_LEFT_AXIS_DOWN = 21 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_8_RIGHT_AXIS_RIGHT = 21 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_8_RIGHT_AXIS_DOWN = 21 + 30 * 7 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 9
	public static final int
			GAMEPAD_9_BUTTON_A = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_9_BUTTON_B = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_9_BUTTON_X = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_9_BUTTON_Y = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_9_BUTTON_LEFT_BUMPER = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_9_BUTTON_RIGHT_BUMPER = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_9_BUTTON_BACK = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_9_BUTTON_START = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_9_BUTTON_GUIDE = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_9_BUTTON_LEFT_THUMB = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_9_BUTTON_RIGHT_THUMB = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_9_BUTTON_DPAD_UP = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_9_BUTTON_DPAD_RIGHT = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_9_BUTTON_DPAD_DOWN = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_9_BUTTON_DPAD_LEFT = 30 * 8 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_9_LEFT_AXIS_LEFT = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_9_LEFT_AXIS_UP = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_9_RIGHT_AXIS_LEFT = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_9_RIGHT_AXIS_UP = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_9_LEFT_TRIGGER = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_9_RIGHT_TRIGGER = 15 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_9_LEFT_AXIS_RIGHT = 21 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_9_LEFT_AXIS_DOWN = 21 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_9_RIGHT_AXIS_RIGHT = 21 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_9_RIGHT_AXIS_DOWN = 21 + 30 * 8 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 10
	public static final int
			GAMEPAD_10_BUTTON_A = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_10_BUTTON_B = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_10_BUTTON_X = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_10_BUTTON_Y = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_10_BUTTON_LEFT_BUMPER = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_10_BUTTON_RIGHT_BUMPER = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_10_BUTTON_BACK = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_10_BUTTON_START = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_10_BUTTON_GUIDE = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_10_BUTTON_LEFT_THUMB = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_10_BUTTON_RIGHT_THUMB = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_10_BUTTON_DPAD_UP = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_10_BUTTON_DPAD_RIGHT = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_10_BUTTON_DPAD_DOWN = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_10_BUTTON_DPAD_LEFT = 30 * 9 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_10_LEFT_AXIS_LEFT = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_10_LEFT_AXIS_UP = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_10_RIGHT_AXIS_LEFT = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_10_RIGHT_AXIS_UP = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_10_LEFT_TRIGGER = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_10_RIGHT_TRIGGER = 15 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_10_LEFT_AXIS_RIGHT = 21 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_10_LEFT_AXIS_DOWN = 21 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_10_RIGHT_AXIS_RIGHT = 21 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_10_RIGHT_AXIS_DOWN = 21 + 30 * 9 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 11
	public static final int
			GAMEPAD_11_BUTTON_A = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_11_BUTTON_B = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_11_BUTTON_X = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_11_BUTTON_Y = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_11_BUTTON_LEFT_BUMPER = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_11_BUTTON_RIGHT_BUMPER = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_11_BUTTON_BACK = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_11_BUTTON_START = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_11_BUTTON_GUIDE = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_11_BUTTON_LEFT_THUMB = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_11_BUTTON_RIGHT_THUMB = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_11_BUTTON_DPAD_UP = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_11_BUTTON_DPAD_RIGHT = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_11_BUTTON_DPAD_DOWN = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_11_BUTTON_DPAD_LEFT = 30 * 10 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_11_LEFT_AXIS_LEFT = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_11_LEFT_AXIS_UP = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_11_RIGHT_AXIS_LEFT = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_11_RIGHT_AXIS_UP = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_11_LEFT_TRIGGER = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_11_RIGHT_TRIGGER = 15 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_11_LEFT_AXIS_RIGHT = 21 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_11_LEFT_AXIS_DOWN = 21 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_11_RIGHT_AXIS_RIGHT = 21 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_11_RIGHT_AXIS_DOWN = 21 + 30 * 10 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 12
	public static final int
			GAMEPAD_12_BUTTON_A = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_12_BUTTON_B = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_12_BUTTON_X = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_12_BUTTON_Y = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_12_BUTTON_LEFT_BUMPER = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_12_BUTTON_RIGHT_BUMPER = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_12_BUTTON_BACK = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_12_BUTTON_START = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_12_BUTTON_GUIDE = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_12_BUTTON_LEFT_THUMB = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_12_BUTTON_RIGHT_THUMB = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_12_BUTTON_DPAD_UP = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_12_BUTTON_DPAD_RIGHT = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_12_BUTTON_DPAD_DOWN = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_12_BUTTON_DPAD_LEFT = 30 * 11 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_12_LEFT_AXIS_LEFT = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_12_LEFT_AXIS_UP = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_12_RIGHT_AXIS_LEFT = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_12_RIGHT_AXIS_UP = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_12_LEFT_TRIGGER = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_12_RIGHT_TRIGGER = 15 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_12_LEFT_AXIS_RIGHT = 21 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_12_LEFT_AXIS_DOWN = 21 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_12_RIGHT_AXIS_RIGHT = 21 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_12_RIGHT_AXIS_DOWN = 21 + 30 * 11 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 13
	public static final int
			GAMEPAD_13_BUTTON_A = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_13_BUTTON_B = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_13_BUTTON_X = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_13_BUTTON_Y = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_13_BUTTON_LEFT_BUMPER = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_13_BUTTON_RIGHT_BUMPER = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_13_BUTTON_BACK = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_13_BUTTON_START = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_13_BUTTON_GUIDE = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_13_BUTTON_LEFT_THUMB = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_13_BUTTON_RIGHT_THUMB = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_13_BUTTON_DPAD_UP = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_13_BUTTON_DPAD_RIGHT = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_13_BUTTON_DPAD_DOWN = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_13_BUTTON_DPAD_LEFT = 30 * 12 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_13_LEFT_AXIS_LEFT = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_13_LEFT_AXIS_UP = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_13_RIGHT_AXIS_LEFT = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_13_RIGHT_AXIS_UP = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_13_LEFT_TRIGGER = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_13_RIGHT_TRIGGER = 15 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_13_LEFT_AXIS_RIGHT = 21 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_13_LEFT_AXIS_DOWN = 21 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_13_RIGHT_AXIS_RIGHT = 21 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_13_RIGHT_AXIS_DOWN = 21 + 30 * 12 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 14
	public static final int
			GAMEPAD_14_BUTTON_A = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_14_BUTTON_B = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_14_BUTTON_X = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_14_BUTTON_Y = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_14_BUTTON_LEFT_BUMPER = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_14_BUTTON_RIGHT_BUMPER = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_14_BUTTON_BACK = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_14_BUTTON_START = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_14_BUTTON_GUIDE = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_14_BUTTON_LEFT_THUMB = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_14_BUTTON_RIGHT_THUMB = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_14_BUTTON_DPAD_UP = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_14_BUTTON_DPAD_RIGHT = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_14_BUTTON_DPAD_DOWN = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_14_BUTTON_DPAD_LEFT = 30 * 13 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_14_LEFT_AXIS_LEFT = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_14_LEFT_AXIS_UP = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_14_RIGHT_AXIS_LEFT = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_14_RIGHT_AXIS_UP = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_14_LEFT_TRIGGER = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_14_RIGHT_TRIGGER = 15 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_14_LEFT_AXIS_RIGHT = 21 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_14_LEFT_AXIS_DOWN = 21 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_14_RIGHT_AXIS_RIGHT = 21 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_14_RIGHT_AXIS_DOWN = 21 + 30 * 13 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 15
	public static final int
			GAMEPAD_15_BUTTON_A = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_15_BUTTON_B = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_15_BUTTON_X = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_15_BUTTON_Y = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_15_BUTTON_LEFT_BUMPER = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_15_BUTTON_RIGHT_BUMPER = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_15_BUTTON_BACK = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_15_BUTTON_START = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_15_BUTTON_GUIDE = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_15_BUTTON_LEFT_THUMB = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_15_BUTTON_RIGHT_THUMB = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_15_BUTTON_DPAD_UP = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_15_BUTTON_DPAD_RIGHT = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_15_BUTTON_DPAD_DOWN = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_15_BUTTON_DPAD_LEFT = 30 * 14 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_15_LEFT_AXIS_LEFT = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_15_LEFT_AXIS_UP = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_15_RIGHT_AXIS_LEFT = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_15_RIGHT_AXIS_UP = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_15_LEFT_TRIGGER = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_15_RIGHT_TRIGGER = 15 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_15_LEFT_AXIS_RIGHT = 21 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_15_LEFT_AXIS_DOWN = 21 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_15_RIGHT_AXIS_RIGHT = 21 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_15_RIGHT_AXIS_DOWN = 21 + 30 * 14 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//GamePad 16
	public static final int
			GAMEPAD_16_BUTTON_A = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_A,
			GAMEPAD_16_BUTTON_B = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_B,
			GAMEPAD_16_BUTTON_X = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_X,
			GAMEPAD_16_BUTTON_Y = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_Y,
			GAMEPAD_16_BUTTON_LEFT_BUMPER = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GAMEPAD_16_BUTTON_RIGHT_BUMPER = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GAMEPAD_16_BUTTON_BACK = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_BACK,
			GAMEPAD_16_BUTTON_START = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_START,
			GAMEPAD_16_BUTTON_GUIDE = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_GUIDE,
			GAMEPAD_16_BUTTON_LEFT_THUMB = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GAMEPAD_16_BUTTON_RIGHT_THUMB = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,
			GAMEPAD_16_BUTTON_DPAD_UP = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP,
			GAMEPAD_16_BUTTON_DPAD_RIGHT = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
			GAMEPAD_16_BUTTON_DPAD_DOWN = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
			GAMEPAD_16_BUTTON_DPAD_LEFT = 30 * 15 + GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

	public static final int
			GAMEPAD_16_LEFT_AXIS_LEFT = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_16_LEFT_AXIS_UP = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_16_RIGHT_AXIS_LEFT = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_16_RIGHT_AXIS_UP = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GAMEPAD_16_LEFT_TRIGGER = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GAMEPAD_16_RIGHT_TRIGGER = 15 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER,
			GAMEPAD_16_LEFT_AXIS_RIGHT = 21 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_X,
			GAMEPAD_16_LEFT_AXIS_DOWN = 21 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y,
			GAMEPAD_16_RIGHT_AXIS_RIGHT = 21 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X,
			GAMEPAD_16_RIGHT_AXIS_DOWN = 21 + 30 * 15 + GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

	//Keyboard
	public static final int
			KEY_SPACE = 30 * 16 + GLFW.GLFW_KEY_SPACE,
			KEY_APOSTROPHE = 30 * 16 + GLFW.GLFW_KEY_APOSTROPHE,
			KEY_COMMA = 30 * 16 + GLFW.GLFW_KEY_COMMA,
			KEY_MINUS = 30 * 16 + GLFW.GLFW_KEY_MINUS,
			KEY_PERIOD = 30 * 16 + GLFW.GLFW_KEY_PERIOD,
			KEY_SLASH = 30 * 16 + GLFW.GLFW_KEY_SLASH,
			KEY_0 = 30 * 16 + GLFW.GLFW_KEY_0,
			KEY_1 = 30 * 16 + GLFW.GLFW_KEY_1,
			KEY_2 = 30 * 16 + GLFW.GLFW_KEY_2,
			KEY_3 = 30 * 16 + GLFW.GLFW_KEY_3,
			KEY_4 = 30 * 16 + GLFW.GLFW_KEY_4,
			KEY_5 = 30 * 16 + GLFW.GLFW_KEY_5,
			KEY_6 = 30 * 16 + GLFW.GLFW_KEY_6,
			KEY_7 = 30 * 16 + GLFW.GLFW_KEY_7,
			KEY_8 = 30 * 16 + GLFW.GLFW_KEY_8,
			KEY_9 = 30 * 16 + GLFW.GLFW_KEY_9,
			KEY_SEMICOLON = 30 * 16 + GLFW.GLFW_KEY_SEMICOLON,
			KEY_EQUAL = 30 * 16 + GLFW.GLFW_KEY_EQUAL,
			KEY_A = 30 * 16 + GLFW.GLFW_KEY_A,
			KEY_B = 30 * 16 + GLFW.GLFW_KEY_B,
			KEY_C = 30 * 16 + GLFW.GLFW_KEY_C,
			KEY_D = 30 * 16 + GLFW.GLFW_KEY_D,
			KEY_E = 30 * 16 + GLFW.GLFW_KEY_E,
			KEY_F = 30 * 16 + GLFW.GLFW_KEY_F,
			KEY_G = 30 * 16 + GLFW.GLFW_KEY_G,
			KEY_H = 30 * 16 + GLFW.GLFW_KEY_H,
			KEY_I = 30 * 16 + GLFW.GLFW_KEY_I,
			KEY_J = 30 * 16 + GLFW.GLFW_KEY_J,
			KEY_K = 30 * 16 + GLFW.GLFW_KEY_K,
			KEY_L = 30 * 16 + GLFW.GLFW_KEY_L,
			KEY_M = 30 * 16 + GLFW.GLFW_KEY_M,
			KEY_N = 30 * 16 + GLFW.GLFW_KEY_N,
			KEY_O = 30 * 16 + GLFW.GLFW_KEY_O,
			KEY_P = 30 * 16 + GLFW.GLFW_KEY_P,
			KEY_Q = 30 * 16 + GLFW.GLFW_KEY_Q,
			KEY_R = 30 * 16 + GLFW.GLFW_KEY_R,
			KEY_S = 30 * 16 + GLFW.GLFW_KEY_S,
			KEY_T = 30 * 16 + GLFW.GLFW_KEY_T,
			KEY_U = 30 * 16 + GLFW.GLFW_KEY_U,
			KEY_V = 30 * 16 + GLFW.GLFW_KEY_V,
			KEY_W = 30 * 16 + GLFW.GLFW_KEY_W,
			KEY_X = 30 * 16 + GLFW.GLFW_KEY_X,
			KEY_Y = 30 * 16 + GLFW.GLFW_KEY_Y,
			KEY_Z = 30 * 16 + GLFW.GLFW_KEY_Z,
			KEY_LEFT_BRACKET = 30 * 16 + GLFW.GLFW_KEY_LEFT_BRACKET,
			KEY_BACKSLASH = 30 * 16 + GLFW.GLFW_KEY_BACKSLASH,
			KEY_RIGHT_BRACKET = 30 * 16 + GLFW.GLFW_KEY_RIGHT_BRACKET,
			KEY_GRAVE_ACCENT = 30 * 16 + GLFW.GLFW_KEY_GRAVE_ACCENT,
			KEY_WORLD_1 = 30 * 16 + GLFW.GLFW_KEY_WORLD_1,
			KEY_WORLD_2 = 30 * 16 + GLFW.GLFW_KEY_WORLD_2;

	public static final int
			KEY_ESCAPE = 30 * 16 + GLFW.GLFW_KEY_ESCAPE,
			KEY_ENTER = 30 * 16 + GLFW.GLFW_KEY_ENTER,
			KEY_TAB = 30 * 16 + GLFW.GLFW_KEY_TAB,
			KEY_BACKSPACE = 30 * 16 + GLFW.GLFW_KEY_BACKSPACE,
			KEY_INSERT = 30 * 16 + GLFW.GLFW_KEY_INSERT,
			KEY_DELETE = 30 * 16 + GLFW.GLFW_KEY_DELETE,
			KEY_RIGHT = 30 * 16 + GLFW.GLFW_KEY_RIGHT,
			KEY_LEFT = 30 * 16 + GLFW.GLFW_KEY_LEFT,
			KEY_DOWN = 30 * 16 + GLFW.GLFW_KEY_DOWN,
			KEY_UP = 30 * 16 + GLFW.GLFW_KEY_UP,
			KEY_PAGE_UP = 30 * 16 + GLFW.GLFW_KEY_PAGE_UP,
			KEY_PAGE_DOWN = 30 * 16 + GLFW.GLFW_KEY_PAGE_DOWN,
			KEY_HOME = 30 * 16 + GLFW.GLFW_KEY_HOME,
			KEY_END = 30 * 16 + GLFW.GLFW_KEY_END,
			KEY_CAPS_LOCK = 30 * 16 + GLFW.GLFW_KEY_CAPS_LOCK,
			KEY_SCROLL_LOCK = 30 * 16 + GLFW.GLFW_KEY_SCROLL_LOCK,
			KEY_NUM_LOCK = 30 * 16 + GLFW.GLFW_KEY_NUM_LOCK,
			KEY_PRINT_SCREEN = 30 * 16 + GLFW.GLFW_KEY_PRINT_SCREEN,
			KEY_PAUSE = 30 * 16 + GLFW.GLFW_KEY_PAUSE,
			KEY_F1 = 30 * 16 + GLFW.GLFW_KEY_F1,
			KEY_F2 = 30 * 16 + GLFW.GLFW_KEY_F2,
			KEY_F3 = 30 * 16 + GLFW.GLFW_KEY_F3,
			KEY_F4 = 30 * 16 + GLFW.GLFW_KEY_F4,
			KEY_F5 = 30 * 16 + GLFW.GLFW_KEY_F5,
			KEY_F6 = 30 * 16 + GLFW.GLFW_KEY_F6,
			KEY_F7 = 30 * 16 + GLFW.GLFW_KEY_F7,
			KEY_F8 = 30 * 16 + GLFW.GLFW_KEY_F8,
			KEY_F9 = 30 * 16 + GLFW.GLFW_KEY_F9,
			KEY_F10 = 30 * 16 + GLFW.GLFW_KEY_F10,
			KEY_F11 = 30 * 16 + GLFW.GLFW_KEY_F11,
			KEY_F12 = 30 * 16 + GLFW.GLFW_KEY_F12,
			KEY_F13 = 30 * 16 + GLFW.GLFW_KEY_F13,
			KEY_F14 = 30 * 16 + GLFW.GLFW_KEY_F14,
			KEY_F15 = 30 * 16 + GLFW.GLFW_KEY_F15,
			KEY_F16 = 30 * 16 + GLFW.GLFW_KEY_F16,
			KEY_F17 = 30 * 16 + GLFW.GLFW_KEY_F17,
			KEY_F18 = 30 * 16 + GLFW.GLFW_KEY_F18,
			KEY_F19 = 30 * 16 + GLFW.GLFW_KEY_F19,
			KEY_F20 = 30 * 16 + GLFW.GLFW_KEY_F20,
			KEY_F21 = 30 * 16 + GLFW.GLFW_KEY_F21,
			KEY_F22 = 30 * 16 + GLFW.GLFW_KEY_F22,
			KEY_F23 = 30 * 16 + GLFW.GLFW_KEY_F23,
			KEY_F24 = 30 * 16 + GLFW.GLFW_KEY_F24,
			KEY_F25 = 30 * 16 + GLFW.GLFW_KEY_F25,
			KEY_KP_0 = 30 * 16 + GLFW.GLFW_KEY_KP_0,
			KEY_KP_1 = 30 * 16 + GLFW.GLFW_KEY_KP_1,
			KEY_KP_2 = 30 * 16 + GLFW.GLFW_KEY_KP_2,
			KEY_KP_3 = 30 * 16 + GLFW.GLFW_KEY_KP_3,
			KEY_KP_4 = 30 * 16 + GLFW.GLFW_KEY_KP_4,
			KEY_KP_5 = 30 * 16 + GLFW.GLFW_KEY_KP_5,
			KEY_KP_6 = 30 * 16 + GLFW.GLFW_KEY_KP_6,
			KEY_KP_7 = 30 * 16 + GLFW.GLFW_KEY_KP_7,
			KEY_KP_8 = 30 * 16 + GLFW.GLFW_KEY_KP_8,
			KEY_KP_9 = 30 * 16 + GLFW.GLFW_KEY_KP_9,
			KEY_KP_DECIMAL = 30 * 16 + GLFW.GLFW_KEY_KP_DECIMAL,
			KEY_KP_DIVIDE = 30 * 16 + GLFW.GLFW_KEY_KP_DIVIDE,
			KEY_KP_MULTIPLY = 30 * 16 + GLFW.GLFW_KEY_KP_MULTIPLY,
			KEY_KP_SUBTRACT = 30 * 16 + GLFW.GLFW_KEY_KP_SUBTRACT,
			KEY_KP_ADD = 30 * 16 + GLFW.GLFW_KEY_KP_ADD,
			KEY_KP_ENTER = 30 * 16 + GLFW.GLFW_KEY_KP_ENTER,
			KEY_KP_EQUAL = 30 * 16 + GLFW.GLFW_KEY_KP_EQUAL,
			KEY_LEFT_SHIFT = 30 * 16 + GLFW.GLFW_KEY_LEFT_SHIFT,
			KEY_LEFT_CONTROL = 30 * 16 + GLFW.GLFW_KEY_LEFT_CONTROL,
			KEY_LEFT_ALT = 30 * 16 + GLFW.GLFW_KEY_LEFT_ALT,
			KEY_LEFT_SUPER = 30 * 16 + GLFW.GLFW_KEY_LEFT_SUPER,
			KEY_RIGHT_SHIFT = 30 * 16 + GLFW.GLFW_KEY_RIGHT_SHIFT,
			KEY_RIGHT_CONTROL = 30 * 16 + GLFW.GLFW_KEY_RIGHT_CONTROL,
			KEY_RIGHT_ALT = 30 * 16 + GLFW.GLFW_KEY_RIGHT_ALT,
			KEY_RIGHT_SUPER = 30 * 16 + GLFW.GLFW_KEY_RIGHT_SUPER,
			KEY_MENU = 30 * 16 + GLFW.GLFW_KEY_MENU;
}
