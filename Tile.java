package finalGame;

//TÃ i liá»‡u tham kháº£o: FaTal Cubez
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tile {
	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDE_SPEED = 30;
	public static final int ARC_WIDTH = 15;
	public static final int ARC_HEIGHT = 15;
	private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private Font font;
	private Point slideTo;
	private int x;
	private int y;
	private boolean beginningAnimation;
	private double scaleFirst;
	private BufferedImage beginningImage;
	private boolean combineAnimation;
	private double scaleCombine;
	private BufferedImage combineImage;
	private boolean canCombine;

	public Tile(int value, int x, int y) {
		beginningAnimation = true;
		scaleFirst = 0.10000000000000001D;
		combineAnimation = false;
		scaleCombine = 1.2D;
		canCombine = true;
		this.value = value;
		this.x = x;
		this.y = y;
		slideTo = new Point(x, y);
		tileImage = new BufferedImage(80, 80, 2);
		beginningImage = new BufferedImage(80, 80, 2);
		combineImage = new BufferedImage(160, 160, 2);
		drawImage();
	}

	public void drawImage() {
		Graphics2D g = (Graphics2D) tileImage.getGraphics();
		if (value == 2) {
			background = new Color(0xeffe5e5);
			text = new Color(0);
		} else if (value == 4) {
			background = new Color(0xeffb2b2);
			text = new Color(0);
		} else if (value == 8) {
			background = new Color(0xeff7f7f);
			text = new Color(0xffffff);
		} else if (value == 16) {
			background = new Color(0xeff4c4c);
			text = new Color(0xffffff);
		} else if (value == 32) {
			background = new Color(0xeff1919);
			text = new Color(0xffffff);
		} else if (value == 64) {
			background = new Color(0xecc0000);
			text = new Color(0xffffff);
		} else if (value == 128) {
			background = new Color(0xeb20000);
			text = new Color(0xffffff);
		} else if (value == 256) {
			background = new Color(0xe7f0000);
			text = new Color(0xffffff);
		} else if (value == 512) {
			background = new Color(0xe660000);
			text = new Color(0xffffff);
		} else if (value == 1024) {
			background = new Color(0xe4c0000);
			text = new Color(0xffffff);
		} else if (value == 2048) {
			background = new Color(0xe190000);
			text = new Color(0xffffff);
		} else {
			background = Color.black;
			text = Color.white;
		}
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, 80, 80);
		g.setColor(background);
		g.fillRoundRect(0, 0, 80, 80, 15, 15);
		g.setColor(text);
		if (value <= 64)
			font = Game.main.deriveFont(36F);
		else
			font = Game.main;
		g.setFont(font);
		int drawX = 40 - DrawUtils.getMessageWidth((new StringBuilder()).append(value).toString(), font, g) / 2;
		int drawY = 40 + DrawUtils.getMessageHeight((new StringBuilder()).append(value).toString(), font, g) / 2;
		g.drawString((new StringBuilder()).append(value).toString(), drawX, drawY);
		g.dispose();
	}

	public void update() {
		if (beginningAnimation) {
			// Biến đổi hình
			AffineTransform transform = new AffineTransform();
			transform.translate(40D - (scaleFirst * 80D) / 2D, 40D - (scaleFirst * 80D) / 2D);
			// Co giãn ô số
			transform.scale(scaleFirst, scaleFirst);
			Graphics2D g2d = (Graphics2D) beginningImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, 80, 80);
			g2d.drawImage(tileImage, transform, null);
			scaleFirst += 0.10000000000000001D;
			g2d.dispose();
			if (scaleFirst >= 1.0D)
				beginningAnimation = false;
		} else if (combineAnimation) {
			AffineTransform transform = new AffineTransform();
			transform.translate(40D - (scaleCombine * 80D) / 2D, 40D - (scaleCombine * 80D) / 2D);
			transform.scale(scaleCombine, scaleCombine);
			Graphics2D g2d = (Graphics2D) beginningImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, 80, 80);
			g2d.drawImage(tileImage, transform, null);
			scaleCombine -= 0.10000000000000001D;
			g2d.dispose();
			if (scaleCombine <= 1.0D)
				combineAnimation = false;
		}
	}

	public void render(Graphics2D g) {
		if (beginningAnimation)
			g.drawImage(beginningImage, x, y, null);
		else if (combineAnimation)
			g.drawImage(combineImage, (int) ((double) (x + 40) - (scaleCombine * 80D) / 2D),
					(int) ((double) (y + 40) - (scaleCombine * 80D) / 2D), null);
		else
			g.drawImage(tileImage, x, y, null);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		drawImage();
	}

	public boolean canCombine() {
		return canCombine;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}

	public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public BufferedImage getTileImage() {
		return tileImage;
	}

	public void setTileImage(BufferedImage tileImage) {
		this.tileImage = tileImage;
	}

	public double getScaleFirst() {
		return scaleFirst;
	}

	public void setScaleFirst(double scaleFirst) {
		this.scaleFirst = scaleFirst;
	}

	public boolean isCombineAnimation() {
		return combineAnimation;
	}

	public void setCombineAnimation(boolean combineAnimation) {
		this.combineAnimation = combineAnimation;
		if (combineAnimation)
			scaleCombine = 1.3D;
	}

	public double getScaleCombine() {
		return scaleCombine;
	}

	public void setScaleCombine(double scaleCombine) {
		this.scaleCombine = scaleCombine;
	}

}
