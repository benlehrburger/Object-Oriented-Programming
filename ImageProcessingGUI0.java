import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @BenLehrburger
 * SA3
 * Create an image processing tool that modifies an image locally where the mouse is, as you move the mouse
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessingGUI0 extends DrawingGUI {
	private ImageProcessor0 proc;		// handles the image processing
	private boolean brush = false;
	private int radius = 5;

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public ImageProcessingGUI0(ImageProcessor0 proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	public void handleKeyPress(char op) {
		System.out.println("Handling key '"+op+"'");
		if (op=='s') { // save a snapshot
			saveImage(proc.getImage(), "pictures/snapshot.png", "png");
		}
		else if (op=='q') {
			brush = true;
		}
		else if (op=='w') {
			brush = false;
		}

		repaint(); // Re-draw, since image has changed
	}

	@Override
	public void handleMouseMotion(int x, int y) {
		if (brush) {
			proc.dragColor(x, y, radius);
		}
		else {
			System.out.println("No tool selected");
		}
		repaint(); // Re-draw everything, since image has changed
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage("pictures/baker.jpg");
				// Create a new processor, and a GUI to handle it
				new ImageProcessingGUI0(new ImageProcessor0(baker));
			}
		});
	}
}
