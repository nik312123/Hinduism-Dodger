package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class BadKarma {
    private double x, y;
    private double xVelocity, yVelocity;
    
    private int startingSide;
    
    private Rectangle2D impurityTemp = new Rectangle2D.Double();
    
    private Random rand = new Random();
    
    public static Player p;
    
    public BadKarma() {
        int side = rand.nextInt(4);
        switch(side) {
            case 0:
                x = -25;
                y = rand.nextInt(680);
                startingSide = 0;
                break;
            case 1:
                x = 705;
                y = rand.nextInt(680);
                startingSide = 1;
                break;
            case 2:
                x = rand.nextInt(680);
                y = -25;
                startingSide = 2;
                break;
            case 3:
                x = rand.nextInt(680);
                y = 705;
                startingSide = 3;
                break;
        }
        
        double deltaY = p.getY() - (y + 10);
        double deltaX = p.getX() - (x + 10);
        double angle = Math.atan2(deltaY, deltaX);
        if(angle < 0)
            angle += 2 * Math.PI;
        angle += randomDouble(-Math.PI/12, Math.PI/12);
        xVelocity = Math.cos(angle)/2;
        yVelocity = Math.sin(angle)/2;
    }
    
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        impurityTemp.setRect(x, y, 20, 20);
        g2d.setColor(Color.RED);
        g2d.fill(impurityTemp);
        move();
    }
    
    public Rectangle2D getRect() {
        return impurityTemp;
    }
    
    public double randomDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }
    
    public boolean isOutOfBounds() {
        switch(startingSide) {
            case 0:
                return x >= 700 || y >= 700 || y <= -20;
            case 1:
                return x <= -20 || y >= 700 || y <= -20;
            case 2:
                return x >= 700 || x <= -20 || y >= 700;
            case 3:
                return x >= 700 || x <= -20 || y <= -20;
        }
        return false;
    }
    
}
