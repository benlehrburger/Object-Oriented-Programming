import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @BenLehrburger
 * SA3
 * Create an image processing tool that modifies an image locally where the mouse is, as you move the mouse
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessor0 {
	private BufferedImage image;        // the current image being processed

	/**
	 * @param image the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Drags color from the previous location of the mouse as the mouse moves
	 * @param cx
	 * @param cy
	 * @param r
	 */
	public void dragColor(int cx, int cy, int r) {
		// Nested loop over nearby pixels
		int color = image.getRGB(cx, cy);
		for (int y = Math.max(0, cy-r); y < Math.min(image.getHeight(), cy+r); y++) {
			for (int x = Math.max(0, cx-r); x < Math.min(image.getWidth(), cx+r); x++) {
				image.setRGB(x, y, color);
			}
		}
	}

}

