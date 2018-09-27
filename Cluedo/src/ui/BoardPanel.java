package ui;

import javax.imageio.ImageIO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import gameElements.Token;
import gameElements.Weapon;
import gameElements.Tokens;
import gameElements.Weapons;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

class BoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 750, FRAME_HEIGHT = 750;  // must be even
    private static final float COL_OFFSET = 52f, ROW_OFFSET = 29f;
    private static final float COL_SCALE = 26.9f, ROW_SCALE = 27.1f;
    private static final int TOKEN_RADIUS = 12;   // must be even

    private final Tokens tokens;
    private final Weapons weapons;
    private BufferedImage boardImage;

    BoardPanel(Tokens tokens, Weapons weapons) {
        this.tokens = tokens;
        this.weapons = weapons;
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setBackground(Color.WHITE);
        try {
            boardImage = ImageIO.read(this.getClass().getResource("cluedo board.jpg"));
        } catch (IOException ex) {
            System.out.println("Could not find the image file " + ex.toString());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        /*
         * Images each Shrek character which is loaded over 2D spherical token as a fail safe
         */
        Image donkeyIcon = null;
        try {
            donkeyIcon = ImageIO.read(this.getClass().getResource("donkey.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image farquadIcon = null;
        try {
            farquadIcon = ImageIO.read(this.getClass().getResource("farquad.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image dragonIcon = null;
        try {
            dragonIcon = ImageIO.read(this.getClass().getResource("dragon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image cookieIcon = null;
        try {
            cookieIcon = ImageIO.read(this.getClass().getResource("cookie.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image fionaIcon = null;
        try {
            fionaIcon = ImageIO.read(this.getClass().getResource("fiona.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image shrekIcon = null;
        try {
            shrekIcon = ImageIO.read(this.getClass().getResource("shrek.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.drawImage(boardImage, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);
        for (Token token : tokens) {
            int x = Math.round(token.getPosition().getCol() * COL_SCALE + COL_OFFSET);
            int y = Math.round(token.getPosition().getRow() * ROW_SCALE + ROW_OFFSET);
            g2.setColor(Color.BLACK);
            Ellipse2D.Double ellipseBlack = new Ellipse2D.Double(x, y, 2 * TOKEN_RADIUS, 2 * TOKEN_RADIUS);
            g2.fill(ellipseBlack);
            Ellipse2D.Double ellipseColour = new Ellipse2D.Double(x + 2, y + 2, 2 * TOKEN_RADIUS - 4, 2 * TOKEN_RADIUS - 4);
            g2.setColor(token.getColor());
            g2.fill(ellipseColour);

            // Donkey Icon onto White Token / Mrs. White
            if (token.getColor().equals(Color.WHITE)) {
                g2.drawImage(donkeyIcon, x, y, 25, 25, this);
            }
            // Farquad Icon onto Red Token / Scarlett
            else if (token.getColor().equals(Color.RED)) {
                g2.drawImage(farquadIcon, x, y, 25, 25, this);
            }
            // Shrek Icon onto Green Token / Reverend Green
            else if (token.getColor().equals(Color.GREEN)) {
                g2.drawImage(shrekIcon, x, y, 25, 25, this);
            }
            //Fiona Icon onto Yellow Token / Colonel Mustard
            else if (token.getColor().equals(Color.YELLOW)) {
                g2.drawImage(fionaIcon, x, y, 25, 25, this);
            }
            //Dragon Icon onto Magenta Token / Professor Plum
            else if (token.getColor().equals(new Color(142, 69, 133))) {
                g2.drawImage(dragonIcon, x, y, 25, 25, this);
            }
            //Cookie Icon onto Blue Token / Mrs. Peacock
            else if (token.getColor().equals(Color.BLUE)) {
                g2.drawImage(cookieIcon, x, y, 25, 25, this);
            }
        }
        for (Weapon weapon : weapons) {
            int x = Math.round(weapon.getPosition().getCol() * COL_SCALE + COL_OFFSET);
            int y = Math.round(weapon.getPosition().getRow() * ROW_SCALE + ROW_OFFSET);

            g2.setColor(Color.BLACK);
            Rectangle2D.Double rectangleBlack = new Rectangle2D.Double(x, y, 2 * TOKEN_RADIUS, 2 * TOKEN_RADIUS);
            g2.fill(rectangleBlack);
            Rectangle2D.Double rectangleColor = new Rectangle2D.Double(x + 2, y + 2, 2 * TOKEN_RADIUS - 4, 2 * TOKEN_RADIUS - 4);
            g2.setColor(Color.lightGray);
            g2.fill(rectangleColor);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("default", Font.BOLD, 16));
            g2.drawString(String.valueOf(weapon.getName().charAt(0)), x + 7, y + 17);


        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }

}
