package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Impurity {
    private double x, y;
    private double xVelocity, yVelocity;
    private Rectangle2D impurityTemp = new Rectangle2D.Double();
    private Random rand = new Random();
    public static Player p;
    
    public Impurity() {
        int side = rand.nextInt(4);
        switch(side) {
            case 0:
                x = 1;
                y = rand.nextInt(599) + 1;
                break;
            case 1:
                x = 599;
                y = rand.nextInt(599) + 1;
                break;
            case 2:
                x = rand.nextInt(599) + 1;
                y = 1;
                break;
            case 3:
                x = rand.nextInt(599) + 1;
                y = 599;
                break;
        }
        
        double deltaY = (y + 10) - p.getY();
        double deltaX = (x + 10) - p.getX();
        xVelocity = -deltaX/Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        yVelocity = -deltaY/Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
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
    
}
