package game.gameobjects.gameobjects.entities;

import game.Constants;
import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gameobjects.AbstractGameObject;
import game.util.TimeUtil;
import game.window.Drawable;
import game.window.Window;
import game.window.shader.ShaderType;
import game.window.shader.shader.BasicShader;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.sql.Time;

/**
 * An implementation of drawing, that draws a simple rectangle texture to the screen
 */
public abstract class BasicDrawingEntity extends AbstractGameObject implements Drawable {
	protected HitBox hitBox;
	protected Sprite sprite;
	protected Color color;
	private boolean useCamera;

	private float drawingPriority;

	private boolean playOnce = false;
	private long startTime;

	public BasicDrawingEntity(HitBox hitBox, float drawingPriority) {
		this.hitBox = hitBox;
		this.drawingPriority = drawingPriority;

		color = Color.BLACK;
		useCamera = true;
	}

	@Override
	public void setup(Window window) {

	}

	@Override
	public void draw(Window window, long time) {
		if (sprite == null) return;

		Rectangle bounds = playOnce? sprite.getTextureOnce(startTime, time): sprite.getTexture(startTime, time);
		BasicShader shader = (BasicShader) window.getShaderHandler().getShader(ShaderType.BASIC_SHADER);

		shader.start();
		shader.setUseCamera(useCamera);
		shader.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		shader.setTextureSheetBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		if (useCamera) {
			shader.setBounds(hitBox.getCenterX() - bounds.width / (2 * (float) Constants.PIXEL_PER_TILE), hitBox.getCenterY() - bounds.height / (2 * (float) Constants.PIXEL_PER_TILE), bounds.width / ((float) Constants.PIXEL_PER_TILE), bounds.height / ((float) Constants.PIXEL_PER_TILE));
		} else {
			shader.setBounds(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		}

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void cleanUp(Window window) {

	}

	@Override
	public float getDrawingPriority() {
		return drawingPriority;
	}

	protected void setDrawingPriority(float drawingPriority) {
		this.drawingPriority = drawingPriority;
	}

	protected void setSprite(Sprite sprite) {
		setSprite(sprite, false);
	}

	protected void setSprite(Sprite sprite, boolean playOnce) {
		this.sprite = sprite;
		this.startTime = TimeUtil.getTime();
		this.playOnce = playOnce;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setUseCamera(boolean useCamera) {
		this.useCamera = useCamera;
	}

	protected int getFrameOnce() {
		return sprite.getFrameOnce(startTime, TimeUtil.getTime());
	}

	protected boolean animationFinished() {
		return sprite.finished(startTime, TimeUtil.getTime());
	}
}
