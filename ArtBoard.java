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

	JPanel mainPanel, checkPanel;
    JPanel buttonPanel;
    JButton b_1, b_2, b_3, b_4;
    JSlider slider;
    JRadioButton triangleButton, squareButton;
    BufferedImage fileImage, panelImage, currentImage, originalImage;

    public ArtBoard() {
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                panelImage = Art.resizeToScale(currentImage, mainPanel);
                mainPanel.repaint();
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 3));

        panelImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        currentImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

        // fileImage = resetImage;
        //currentImage = resetImage;


        mainPanel = new JPanel() {


			public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (this.getWidth() - panelImage.getWidth(null)) / 2;
                int y = (this.getHeight() - panelImage.getHeight(null)) / 2;
                g.drawImage(panelImage, x, y, this);
            }
        };


        add(mainPanel, BorderLayout.CENTER);

        // b_1 = new JButton("Reset");
        b_2 = new JButton("Poly-ize");
        b_3 = new JButton("Load Image");
        b_4 = new JButton("Save Image");

        // buttonPanel.add(b_1);
        buttonPanel.add(b_3);
        buttonPanel.add(b_2);
        buttonPanel.add(b_4);



        add(buttonPanel, BorderLayout.SOUTH);

        checkPanel = new JPanel();
        checkPanel.setLayout(new GridLayout(2, 0));
        squareButton = new JRadioButton("Squares");
        triangleButton = new JRadioButton("Triangles");
        ButtonGroup group = new ButtonGroup();
        group.add(squareButton);
        group.add(triangleButton);
        checkPanel.add(squareButton);
        checkPanel.add(triangleButton);

        add(checkPanel, BorderLayout.WEST);

        /*   b_1.addActionListener(new ActionListener() { 
           public void actionPerformed(ActionEvent e) {
               panelImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
               mainPanel.repaint();
           } 
           } ); */

        b_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (triangleButton.isSelected() == true) {
                    boolean complete = false;
                    do{
                        try {

                            originalImage = Art.protect(fileImage);
                            String input = (String) JOptionPane.showInputDialog(mainPanel, "Please enter the number of triangles");
    
                            int numberOfTriangles = Integer.parseInt(input);
                            panelImage = Art.resizeToScale(Art.triangulate(numberOfTriangles, fileImage), mainPanel);
                            currentImage = panelImage;
                            fileImage = originalImage;
                            mainPanel.repaint();
                        } catch (NumberFormatException excpetion) {
                            JOptionPane.showMessageDialog(mainPanel, "Please enter an integer value");
                        }
                    }while(complete = false);
                    

                } else if (squareButton.isSelected() == true) {
                    boolean complete = false;
                    do{
                        try {

                            originalImage = Art.protect(fileImage);
                            String input = (String) JOptionPane.showInputDialog(mainPanel, "Please enter the number of squares");
                            int baseInput = Integer.parseInt(input);
                            int squaredInput = nearestSquare(baseInput);
                            int numberOfSquares = (int) Math.sqrt(squaredInput);
                            complete = true;
                            panelImage = Art.resizeToScale(Art.squares(numberOfSquares, fileImage), mainPanel);
                            currentImage = panelImage;
                            fileImage = originalImage;
                            mainPanel.repaint();
                        } catch (NumberFormatException exception) {
                            JOptionPane.showMessageDialog(mainPanel, "Please enter an integer value");

                        }
                    } while(complete == false);
                    


                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a Polygon to begin");
                }
            }
        });

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
        setVisible(true);

    }

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