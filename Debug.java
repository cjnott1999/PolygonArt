package PolygonArt;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.*;


public class Debug extends JFrame{
    JPanel filePanel, resetPanel, panelPanel;
    //BufferedImage panelImage, fileImage;

   BufferedImage fileImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
   BufferedImage panelImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);


    public Debug(){


        setPreferredSize(new Dimension(600, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        filePanel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int x = (this.getWidth() - fileImage.getWidth(null)) / 2;
                int y = (this.getHeight() - fileImage.getHeight(null)) / 2;
                g.drawImage(Art.resizeToScale(fileImage, filePanel), 0, 0, this);
            }
        };
        /*
        resetPanel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int x = (this.getWidth() - panelImage.getWidth(null)) / 2;
                int y = (this.getHeight() - panelImage.getHeight(null)) / 2;
                g.drawImage(getResetImage(), x, y, this);
            }
        };
        */
        panelPanel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int x = (this.getWidth() - panelImage.getWidth(null)) / 2;
                int y = (this.getHeight() - panelImage.getHeight(null)) / 2;
                g.drawImage(panelImage, 0, 0, this);
            }
        };

        add(filePanel, BorderLayout.CENTER);
        //add(resetPanel, BorederLayout.CENTER);
       // add(panelPanel, BorderLayout.WEST);

        pack();
        setVisible(true);
    }

    public void update(){
       // fileImage = Art.resizeToScale(ArtBoard.getFileImage(), filePanel);
       // panelImage = Art.resizeToScale(ArtBoard.getPanelImage(), panelPanel);
        fileImage = ArtBoard.getFileImage();
        panelImage = ArtBoard.getPanelImage();
        this.repaint();
    }
}