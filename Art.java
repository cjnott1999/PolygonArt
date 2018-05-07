package PolygonArt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.lang.Math;
import io.github.jdiemke.triangulation.*;

/**
 * A class of static methods useful for Art
 * 
 * @author Cameron Nottingham
 */

public class Art {
    private static Random rand = new Random();


    /**
     * Rezizes a BufferedImage relative to a given JPanel
     * 
     * @param BufferedImage
     *      The BufferedImage to be resized 
     * 
     * @param JPanel
     *      The JPanel that the BufferedImage will be fitted to
     * 
     * @return Returns a new, resized BufferedImage 
     * 
     */
    public static BufferedImage resizeToScale(BufferedImage img, JPanel mainPanel) {
        double scale = Math.min(((double) mainPanel.getWidth() / (double) img.getWidth()),
            ((double) mainPanel.getHeight() / (double) img.getHeight()));


        int newW = (int)(img.getWidth() * scale);
        int newH = (int)(img.getHeight() * scale);

        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    /**
     * Triangulates a given BufferedImage 
     * Credit to jdiemke for his Delaunay Triangulation classes -- jdiemke.github.io
     * 
     * @param int 
     *      The number of points to be triangulated excluding the four corners
     * 
     * @param BufferedImage
     *      The BufferedImage to be triangulated 
     * 
     * @return The triangulated BufferedImage
     */

    public static BufferedImage triangulate(int sl, BufferedImage img) {
        BufferedImage drawnImage = img;
        Vector < Vector2D > pointSet = new Vector < Vector2D > ();
        List < Triangle2D > triangleSoup;
        Graphics2D graphics;
        generatePoints(sl, pointSet, drawnImage);
        try {

            DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
            delaunayTriangulator.triangulate();
            triangleSoup = delaunayTriangulator.getTriangles();
            int[] y = new int[3];
            int[] x = new int[3];


            for (Iterator < Triangle2D > iter = triangleSoup.iterator(); iter.hasNext();) {
                Triangle2D element = iter.next();
                x[0] = element.a.xAsInt();
                x[1] = element.b.xAsInt();
                x[2] = element.c.xAsInt();

                y[0] = element.a.yAsInt();
                y[1] = element.b.yAsInt();
                y[2] = element.c.yAsInt();



                graphics = drawnImage.createGraphics();
                graphics.setPaint(getColor(drawnImage, element));

                graphics.fillPolygon(x, y, 3);
                graphics.dispose();



            }

        } catch (NotEnoughPointsException e) {}


        pointSet.clear();


        return drawnImage;

    }

    /**
     * Breaks down a BufferedImage into squares and draws it
     * 
     * @param int
     *      The number of rows and columns
     * 
     * @param BufferedImage
     *      The BufferedImage to be squared 
     * 
     * @return A squared BufferedImage 
     */

    public static BufferedImage squares(int rc, BufferedImage img) {

        if (rc == 0) {
            return img;
        }
        BufferedImage image = img;

        int rows = rc;
        int cols = rc;


        int pieceWidth = image.getWidth() / cols;
        int pieceHeight = image.getHeight() / rows;
    

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                BufferedImage colorImage = image.getSubimage((x * pieceWidth), (y * pieceHeight), pieceWidth, pieceHeight);
                Graphics2D graphics = colorImage.createGraphics();
                graphics.setPaint(getColor(colorImage));
                graphics.fillRect(0, 0, colorImage.getWidth(), colorImage.getHeight());
                graphics.dispose();
            }
        }


        return image;



    }

    /**
     * A private method to get the average color from a paricular image
     * 
     * @param BufferedImage
     *      The BufferedImage to find the color of
     * 
     * @return The Color 
     */
    private static Color getColor(BufferedImage img) {
 
        int redTotal = 0;
        int blueTotal = 0;
        int greenTotal = 0;
        int divisor = img.getHeight() * img.getWidth();

        //Go through the pixels and add the RGB values
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int color = img.getRGB(x, y);
                redTotal += (color >> 16) & 0xFF;
                blueTotal += (color & 0xFF);
                greenTotal += (color >> 8) & 0xFF;
            }
        }


        int blue = blueTotal / divisor;
        int red = redTotal / divisor;
        int green = greenTotal / divisor;

        return new Color(red, green, blue);
    }

    /**
     * Private method to get the average color from a Triangle2D
     * 
     * @param BufferedImage 
     *      The BufferedImage from which the colors are sampled
     * 
     * @param Triangle2D
     *      The Triangle2D to find the color of
     * 
     * @return The Color 
     */

    private static Color getColor(BufferedImage img, Triangle2D element) {
        int redTotal = 0;
        int blueTotal = 0;
        int greenTotal = 0;
        for (int i = 0; i < 10; i++) {
            double r1 = rand.nextDouble();
            double r2 = rand.nextDouble();

            double x = (1 - Math.sqrt(r1)) * element.a.x + (Math.sqrt(r1) * (1 - r2)) * element.b.x + (Math.sqrt(r1) * r2) * element.c.x;
            double y = (1 - Math.sqrt(r1)) * element.a.y + (Math.sqrt(r1) * (1 - r2)) * element.b.y + (Math.sqrt(r1) * r2) * element.c.y;

            int color = img.getRGB((int) x, (int) y);
            redTotal += (color >> 16) & 0xFF;
            blueTotal += (color & 0xFF);
            greenTotal += (color >> 8) & 0xFF;
        }

        int blue = blueTotal / 10;
        int red = redTotal / 10;
        int green = greenTotal / 10;

        return new Color(red, green, blue);

    }

    /**
     * Generates the points for a triangulation 
     * 
     * @param int 
     *      The number of points to generate
     * 
     * @param Vector<Vector2D>
     *      The Vector to add the points to
     * 
     * @param BufferedImage
     *      The BufferedImage that will be triangulated. Used to grab the four corners of the image as points/
     * 
     * 
     */
    private static void generatePoints(int sl, Vector < Vector2D > pointSet, BufferedImage img) {

        pointSet.add(new Vector2D(0.0, 0.0));
        pointSet.add(new Vector2D((double) img.getWidth(), 0.0));
        pointSet.add(new Vector2D(0.0, (double) img.getHeight()));
        pointSet.add(new Vector2D((double) img.getWidth(), (double) img.getHeight()));
        for (int i = sl; i > 0; i--) {
            pointSet.add(new Vector2D((rand.nextDouble() * img.getWidth()), (rand.nextDouble() * img.getHeight())));
        }
    }
    /**
     * Creates an identical copy of a BufferedImage in memory
     * 
     * @param BufferedImage
     *      The image to be protected
     * 
     * @return 
     *      A new, identical copy of the original image 
     */
    public static BufferedImage protect(BufferedImage img) {
        ColorModel c = img.getColorModel();
        boolean iap = c.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        BufferedImage original = new BufferedImage(c, raster, iap, null);
        return original;
    }


}