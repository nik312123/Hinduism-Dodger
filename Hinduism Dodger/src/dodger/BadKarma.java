package dodger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class BadKarma {
    private double x, y;
    private double xVelocity, yVelocity;
    
    private int startingSide;
    private int karmaFrame = 0;
    private int karmaFrameCounter = 0;
    private int pulseFrameCounter = 0;
    private int pulseFrame = 0;
    private int pulseStartCounter = 0;
    
    private boolean pulseStart = false;
    private boolean isSlow = false;
    
    public static Player p;
    
    private Rectangle2D impurityTemp = new Rectangle2D.Double();
    
    private Random rand = new Random();
    
    public BadKarma() {
        int side = rand.nextInt(4);
        switch(side) {
            case 0:
                x = -25;
                y = rand.nextInt(Runner.mainFrame.getHeight() - 20);
                startingSide = 0;
                break;
            case 1:
                x = Runner.mainFrame.getWidth() + 5;
                y = rand.nextInt(Runner.mainFrame.getHeight() - 20);
                startingSide = 1;
                break;
            case 2:
                x = rand.nextInt(Runner.mainFrame.getWidth() - 20);
                y = 25;
                startingSide = 2;
                break;
            case 3:
                x = rand.nextInt(Runner.mainFrame.getWidth() - 20);
                y = Runner.mainFrame.getHeight() + 5;
                startingSide = 3;
                break;
        }
        double deltaY = p.getY() - (y + 10);
        double deltaX = p.getX() - (x + 10);
        double angle = Math.atan2(deltaY, deltaX);
        if(angle < 0)
            angle += 2 * Math.PI;
        angle += Runner.randomDouble(-Math.PI/18, Math.PI/18);
        xVelocity = Math.cos(angle)/2;
        yVelocity = Math.sin(angle)/2;
    }
    
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        impurityTemp.setRect(x + 19, y + 19, 22, 22);
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        if(++karmaFrameCounter % 15 == 0) {
            ++karmaFrame;
            if(karmaFrame == 9)
                karmaFrame = 0;
            karmaFrameCounter = 0;
        }
        if(!pulseStart && ++pulseStartCounter % 100 == 0) {
            pulseStart = true;
            pulseStartCounter = 0;
        }
        else if(pulseStart && ++pulseFrameCounter % 15 == 0) {
            ++pulseFrame;
            if(pulseFrame == 29) {
                pulseFrame = 0;
                pulseStart = false;
            }
            pulseFrameCounter = 0;
        }
        g2d.drawImage(Runner.pulse[pulseFrame], transform, null);
        g2d.drawImage(Runner.badKarma[karmaFrame], transform, null);
        move();
    }
    
    public Rectangle2D getRect() {
        return impurityTemp;
    }
    
    public boolean isOutOfBounds() {
        switch(startingSide) {
            case 0:
                return x >= Runner.mainFrame.getWidth() || y >= Runner.mainFrame.getHeight() || y <= 0;
            case 1:
                return x <= -20 || y >= Runner.mainFrame.getHeight() || y <= 0;
            case 2:
                return x >= Runner.mainFrame.getWidth() || x <= -20 || y >= Runner.mainFrame.getHeight();
            case 3:
                return x >= Runner.mainFrame.getWidth() || x <= -20 || y <= 0;
        }
        return false;
    }
    
    public void slowSpeed() {
        if(!isSlow) {
            xVelocity /= 4;
            yVelocity /= 4;
            isSlow = true;
        }
    }
    
    public void increaseSpeed() {
        if(isSlow) {
            xVelocity *= 4;
            yVelocity *= 4;
            isSlow = false;
        }
    }
    
}
