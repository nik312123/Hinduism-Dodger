package dodger;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PowerUp {
    private int startingSide;
    private int powerUpType;
    private int glowCounter = 0;
    private int glowStep = 0;
    private int glowDirection = 1;
    private int glowIndex = 0;
    
    private double x, y;
    private double xVelocity, yVelocity;
    
    private static final Ellipse2D GLOW = new Ellipse2D.Double(0, 0, 50, 50);
    
    private BufferedImage powerUpImage;
    
    private static final Color[] RAINBOW = {Color.RED, new Color(255, 140, 0), Color.YELLOW, Color.GREEN, Color.BLUE, new Color(111, 0, 255), new Color(238, 130, 238)};
    
    PowerUp() {
        Random rand = new Random();
        powerUpType = rand.nextInt(3);
        powerUpImage = Runner.powerUps[powerUpType];
        int side = rand.nextInt(4);
        double angle = 0;
        switch(side) {
            case 0:
                x = -35;
                y = rand.nextInt(Runner.mainFrame.getHeight() - 30);
                startingSide = 0;
                angle = Runner.randomDouble(11 * Math.PI / 6 + (y - 290) / 290.0 * 7 * Math.PI / 24, 13 * Math.PI / 6 + (y - 290) / 290.0 * 7 * Math.PI / 24);
                break;
            case 1:
                x = Runner.mainFrame.getWidth() + 5;
                y = rand.nextInt(Runner.mainFrame.getHeight() - 30);
                startingSide = 1;
                angle = Runner.randomDouble(5 * Math.PI / 6 - (y - 290) / 290.0 * 7 * Math.PI / 24, 7 * Math.PI / 6 - (y - 290) / 290.0 * 7 * Math.PI / 24);
                break;
            case 2:
                x = rand.nextInt(Runner.mainFrame.getWidth() - 30);
                y = 20;
                startingSide = 2;
                angle = Runner.randomDouble(4 * Math.PI / 3 - (x - 285) / 285.0 * 7 * Math.PI / 24, 5 * Math.PI / 3 - (x - 285) / 285.0 * 7 * Math.PI / 24);
                break;
            case 3:
                x = rand.nextInt(Runner.mainFrame.getWidth() - 30);
                y = Runner.mainFrame.getHeight() + 5;
                startingSide = 3;
                angle = Runner.randomDouble(Math.PI / 3 + (x - 285) / 285.0 * 7 * Math.PI / 24, 2 * Math.PI / 3 + (x - 285) / 285.0 * 7 * Math.PI / 24);
                break;
        }
        xVelocity = Math.cos(angle);
        yVelocity = -Math.sin(angle);
    }
    
    private void move() {
        x += xVelocity;
        y += yVelocity;
    }
    
    public void draw(Graphics g) {
        if(++glowCounter % 3 == 0) {
            glowStep += glowDirection;
            if(glowStep == 75)
                glowDirection = -1;
            else if(glowStep == 0) {
                glowDirection = 1;
                ++glowIndex;
                if(glowIndex == 7)
                    glowIndex = 0;
            }
            glowCounter = 0;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Composite original = g2d.getComposite();
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) glowStep / 100);
        g2d.setComposite(alpha);
        AffineTransform trans = new AffineTransform();
        trans.translate(x + (double) powerUpImage.getWidth() / 2 - 25, y + (double) powerUpImage.getHeight() / 2 - 25);
        g2d.setColor(RAINBOW[glowIndex]);
        g2d.fill(trans.createTransformedShape(GLOW));
        g2d.setComposite(original);
        trans.translate(-((double) powerUpImage.getWidth() / 2 - 25), -((double) powerUpImage.getWidth() / 2 - 25));
        g2d.setColor(Color.BLACK);
        g2d.drawImage(powerUpImage, trans, null);
        move();
    }
    
    public Ellipse2D getCircle() {
        return new Ellipse2D.Double(x, y, 30, 30);
    }
    
    public boolean isOutOfBounds() {
        switch(startingSide) {
            case 0:
                return x >= Runner.mainFrame.getWidth() || y >= Runner.mainFrame.getHeight() || y <= 25;
            case 1:
                return x <= -30 || y >= Runner.mainFrame.getHeight() || y <= 25;
            case 2:
                return x >= Runner.mainFrame.getWidth() || x <= -30 || y >= Runner.mainFrame.getHeight();
            case 3:
                return x >= Runner.mainFrame.getWidth() || x <= -30 || y <= 25;
        }
        return false;
    }
    
    public int getPowerUp() {
        return powerUpType;
    }
    
}
