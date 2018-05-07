package PolygonArt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import PolygonArt.Art;



public class ArtBoard extends JFrame {
    //Variables to hold all the resource the ArtBoard needs to render  
	JPanel mainPanel, checkPanel;
    JPanel buttonPanel;
    JButton b_1, b_2, b_3, b_4;
    JSlider slider;
    JRadioButton triangleButton, squareButton;
    BufferedImage fileImage, panelImage, currentImage, originalImage;

    public ArtBoard() {

        //Initialize the ArtBoard parameter, we are using a BorderLayout
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        

        //When the JFrame size is changed, re-draw the image to fit in the JFrame
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                panelImage = Art.resizeToScale(currentImage, mainPanel);
                mainPanel.repaint();
            }
        });

        //Initialize the panel to hold our buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 3));

        //The panelImage is what is rendered to mainPanel 
        panelImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

        //The currentImage is stored as a copy of the panelImage to handle resize events
        currentImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

        //The mainPanel will render the panelImage in whatever form it is
        mainPanel = new JPanel() {

            //Paint the panelImage to the mainPanel
			public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (this.getWidth() - panelImage.getWidth(null)) / 2;
                int y = (this.getHeight() - panelImage.getHeight(null)) / 2;
                g.drawImage(panelImage, x, y, this);
            }
        };

        //Add mainPanel to to the ArtBoard
        add(mainPanel, BorderLayout.CENTER);

        //Initialize our buttons
        b_2 = new JButton("Poly-ize");
        b_3 = new JButton("Load Image");
        b_4 = new JButton("Save Image");

        //Add buttons to the buttonPanel
        buttonPanel.add(b_3);
        buttonPanel.add(b_2);
        buttonPanel.add(b_4);

        
        //Add the buttonPanel to the ArtBoard
        add(buttonPanel, BorderLayout.SOUTH);

        //Create a checkPanel and initialize it.
        //The checkPanel will hold the radioButtons for the options 
        checkPanel = new JPanel();
        checkPanel.setLayout(new GridLayout(2, 0));

        //Initialize the JRadioButtons
        squareButton = new JRadioButton("Squares");
        triangleButton = new JRadioButton("Triangles");

        //Add them to a button group so they are mutually exclusive options
        ButtonGroup group = new ButtonGroup();
        group.add(squareButton);
        group.add(triangleButton);

        //Add the buttons to the checkPanel
        checkPanel.add(squareButton);
        checkPanel.add(triangleButton);

        //Add a border to the checkPanel
        checkPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        //Add the checkPanel to the artBoard
        add(checkPanel, BorderLayout.WEST);

        /*The button will wait to be clicked/ After clicking it will check to see which (if any) of triangleButton and squareButton
        are clicked. It will then call the corresponsing method on the fileImage and set the panelImage. However, the fileImage needs
        to be protected and restored before every method call to prevent side-effects caused by the Graphics library */
        b_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (triangleButton.isSelected() == true) {
                    //Boolean to hold the status of the while loop
                    boolean complete = false;
                    do{
                        try {
                            //Protect the fileImage
                            originalImage = Art.protect(fileImage);
                            //Ask user for input
                            String input = (String) JOptionPane.showInputDialog(mainPanel, "Please enter the number of triangles");
                            //If they click the cancel button, break out of the loop and reset
                            if (input == null) break;

                            //Attempt to parse the String into an Interger - may throw NumberFormatException
                            int numberOfTriangles = Integer.parseInt(input);

                            //If it has not thrown NumberFormatException, the loop is done 
                            complete = true;
                            //Triangulate the panelImage and re-size
                            panelImage = Art.resizeToScale(Art.triangulate(numberOfTriangles, fileImage), mainPanel);
                            //Set the currentImage
                            currentImage = panelImage;
                            //Restore the fileImage
                            fileImage = originalImage;

                            //Re-draw the mainPanel
                            mainPanel.repaint();
                        } catch (NumberFormatException excpetion) {
                            JOptionPane.showMessageDialog(mainPanel, "Please enter an integer value");
                        }
                    }while(complete == false);
                    

                } else if (squareButton.isSelected() == true) {
                    //Boolean to hold the status of the while loop
                    boolean complete = false;
                    do{
                        try {

                            //Protect the fileImage
                            originalImage = Art.protect(fileImage);
                            //Ask user for input
                            String input = (String) JOptionPane.showInputDialog(mainPanel, "Please enter the number of squares");
                            //If they click the cancel button, break out of the loop and reset
                            if (input == null) break;

                            //Attempt to parse the String into an Interger - may throw NumberFormatException
                            int baseInput = Integer.parseInt(input);
                            int squaredInput = nearestSquare(baseInput);
                            int numberOfSquares = (int) Math.sqrt(squaredInput);

                            //If it has not thrown NumberFormatException, the loop is done 
                            complete = true;

                            //Squarify the panelImage and resize it 
                            panelImage = Art.resizeToScale(Art.squares(numberOfSquares, fileImage), mainPanel);
                            //Set the currentImage
                            currentImage = panelImage;
                            //Restore the fileImage
                            fileImage = originalImage;

                            //Re-draw the mainPanel
                            mainPanel.repaint();
                        } catch (NumberFormatException exception) {
                            //If NumberFormatException is throw, inform the user that they need to provide valid input 
                            JOptionPane.showMessageDialog(mainPanel, "Please enter an integer value");

                        }
                    } while(complete == false);
                    


                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a Polygon to begin");
                }
            }
        });

        //When the button is clicked, set the fileImage
        b_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    setFile();
                    panelImage = Art.resizeToScale(fileImage, mainPanel);
                    mainPanel.repaint();
                } catch (NullPointerException exception) {
                    System.out.println(exception);
                }



            }
        });

        //When the button is clicked, write the panelImage to the current direcotry 
        b_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File outputfile = new File("PolyImage.png");
                    ImageIO.write(panelImage, "png", outputfile);
                } catch (IOException error) {

                }
            }
        });
        pack();
        this.setLocationRelativeTo(null);
        setVisible(true);

    }

    //Sets the file - credit to Dr. Hochberg from the Steganography package
    void setFile() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                fileImage = ImageIO.read(file);
                currentImage = fileImage;


            } catch (IOException exception) {
                System.out.println("Not found");
            }
        }
        this.repaint();
    }

    //Computes the nearest perfect square of a given integer's square value
    public int nearestSquare(int sq) {
        double sqrt = Math.sqrt(sq);
        int floor = (int) sqrt;
        int ceiling = floor + 1;
        int floorSquared = floor * floor;
        int ceilingSquared = ceiling * ceiling;

        if ((sq - floorSquared) < (ceilingSquared - sq)) {
            return floorSquared;
        } else {
            return ceilingSquared;
        }

    }
    


}