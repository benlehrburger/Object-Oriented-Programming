import javax.swing.plaf.synth.Region;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * @BenLehrburger, @NobleRai
 * CS10
 * Problem Set 1
 *
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
    private static final int maxColorDiff = 1000;                // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50;                // how many points in a region to be worth considering
    private static int radius = 1;                          // radius of a pixel's neighborhood

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions;            // a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinder() {
        this.image = null;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     * Loop through pixels, keep track of those within bounds of targetColor, and loop through their neighbors to do the same.
     */
    public void findRegions(Color targetColor) {
        // TODO: YOUR CODE HERE
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        regions = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));

                if (visited.getRGB(x, y) == 0 && colorMatch(color, targetColor)) {
                    ArrayList<Point> newRegion = new ArrayList<Point>();
                    ArrayList<Point> toVisit = new ArrayList<Point>();
                    Point point = new Point(x, y);
                    toVisit.add(point);

                    while (toVisit.size() > 0) {
                        Point first = toVisit.get(0);
                        toVisit.remove(0);

                        if (visited.getRGB(first.x, first.y) == 0) {
                            visited.setRGB(first.x, first.y, 1);
                            newRegion.add(first);

                            for (int ny = Math.max(0, first.y - radius);
                                 ny < Math.min(image.getHeight(), first.y + 1 + radius);
                                 ny++) {
                                for (int nx = Math.max(0, first.x - radius);
                                     nx < Math.min(image.getWidth(), first.x + 1 + radius);
                                     nx++) {

                                    Point neighbor = new Point(nx, ny);
                                    Color neighborColor = new Color(image.getRGB(nx, ny));

                                    if (colorMatch(neighborColor, targetColor) && visited.getRGB(nx, ny) == 0 && (ny != first.y || nx != first.x)) {
                                        toVisit.add(neighbor);
                                    }
                                }
                            }
                        }
                    }

                    if (newRegion.size() > minRegion) {
                        regions.add(newRegion);
                    }
                }
            }
        }
    }


    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    private static boolean colorMatch(Color c1, Color c2) {
        // TODO: YOUR CODE HERE
        int colNum = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
                + (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
                + (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
        if (colNum <= maxColorDiff) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        // TODO: YOUR CODE HERE

        ArrayList<Point> biggestRegion = new ArrayList<>();

        for (int i = 0; i < regions.size(); i++) {
            if (biggestRegion.size() < regions.get(i).size()) {
                biggestRegion = regions.get(i);
            }
        }
        return biggestRegion;
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        // Now recolor the regions in it
        // TODO: YOUR CODE HERE
        if (regions != null) {
            for (ArrayList<Point> i : regions) {
                Color randomColor = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                for (Point j : i) {
                    int xpos = (int) j.getX();
                    int ypos = (int) j.getY();
                    recoloredImage.setRGB(xpos, ypos, randomColor.getRGB());
                }
            }

        }
    }
}

