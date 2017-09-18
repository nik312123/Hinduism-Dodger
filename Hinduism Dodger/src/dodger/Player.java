package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Player {
    private double x = Runner.mainFrame.getWidth()/2, y = Runner.mainFrame.getHeight()/2;
    private double xChange = 0, yChange = 0;
    
    private int invincibilityFrame = 0;
    private int invincibilitySwitchCounter = 20;
    
    private boolean left = false, right = false, up = false, down = false;
    private boolean isVisible = true;
    
    private Rectangle2D playerTemp = new Rectangle2D.Double(x - 20, y - 20, 40, 40);
    
    public void draw(Graphics g) {
        if(left)
            xChange -= 0.05;
        if(right)
            xChange += 0.05;
        if(up)
            yChange -= 0.05;
        if(down)
            yChange += 0.05;
        x += xChange;
        y += yChange;
        xChange *= 0.98;
        yChange *= 0.98;
        if(Runner.isInjured() && ++invincibilityFrame % invincibilitySwitchCounter == 0) {
            isVisible = !isVisible;
            invincibilityFrame = 0;
            if(invincibilitySwitchCounter == 20)
                invincibilitySwitchCounter = 10;
            else
                invincibilitySwitchCounter = 20;
        }
        else if(!isVisible && !Runner.isInjured())
            isVisible = true;
        Graphics2D g2d = (Graphics2D) g.create();
        playerTemp.setRect(x - playerTemp.getWidth()/2, y - playerTemp.getWidth()/2, playerTemp.getWidth(), playerTemp.getHeight());
        constraints();
        g2d.setColor(Color.BLACK);
        if(isVisible)
            g2d.fill(playerTemp);
        g2d.dispose();
    }
    
    public void constraints() {
        if(x >= Runner.mainFrame.getWidth() - playerTemp.getWidth()/2) {
            x = Runner.mainFrame.getWidth() - playerTemp.getWidth()/2;
            xChange = 0;
        }
        else if(x <= 0 + playerTemp.getWidth()/2) {
            x = 0 + playerTemp.getWidth()/2;
            xChange = 0;
        }
        if(y >= Runner.mainFrame.getHeight() - playerTemp.getWidth()) {
            y = Runner.mainFrame.getHeight() - playerTemp.getWidth();
            yChange = 0;
        }
        else if(y <= 0 + playerTemp.getWidth()/2) {
            y = 0 + playerTemp.getWidth()/2;
            yChange = 0;
        }
    }
    
    public void setRight(boolean right) {
        this.right = right;
    }
    
    public void setLeft(boolean left) {
        this.left = left;
    }
    
    public void setDown(boolean down) {
        this.down = down;
    }
    
    public void setUp(boolean up) {
        this.up = up;
    }
    
    public double getY() {
        return y;
    }
    
    public double getX() {
        return x;
    }
    
    public Rectangle2D getRect() {
        return playerTemp;
    }
    
}
