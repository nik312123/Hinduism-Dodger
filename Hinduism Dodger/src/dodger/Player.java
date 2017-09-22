package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
    private double x = Runner.mainFrame.getWidth()/2, y = Runner.mainFrame.getHeight()/2 - 10;
    private double xVelocity = 0, yVelocity = 0;
    
    private int invincibilityFrame = 0;
    private int invincibilitySwitchCounter = 20;
    private int monkFrame = 0;
    private int monkFrameCounter = 0;
    
    private static int monkPixels[][][] = new int[8][60][60];
    
    private boolean left = false, right = false, up = false, down = false;
    private boolean isVisible = true;
    
    private HealthBar hb = new HealthBar(new Color(192, 192, 192), Color.GREEN, 50, 5, 200, 15);
            
    private static BufferedImage[] monk = new BufferedImage[8];
    
    public Player() {
        if(Runner.isFirstTime) {
            try {
                for(int i = 0; i < 8; ++i)
                    monk[i] = ImageIO.read(Runner.class.getResource("/images/monk" + i + ".png"));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < monkPixels.length; ++i)
                for(int j = 0; j < monkPixels[i].length; ++j)
                    for(int k = 0; k < monkPixels[i][j].length; ++k)
                        monkPixels[i][j][k] = monk[i].getRGB(j, k);
        }
    }
    
    public void draw(Graphics g) {
        changeVelocity();
        checkVisibility();
        adjustMonkFrame();
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transform = new AffineTransform();
        transform.translate(x - monk[monkFrame].getWidth()/2.0, y - monk[monkFrame].getHeight()/2.0);
        constrain();
        double angle = Math.atan2(yVelocity, xVelocity);
        if(angle < 0)
            angle += 2 * Math.PI;
        transform.rotate(angle + Math.PI/2, monk[monkFrame].getWidth()/2, monk[monkFrame].getHeight()/2);
        if(isVisible) {
            if(!up && !down && !left && !right)
                monkFrame = 0;
            g2d.drawImage(monk[monkFrame], transform, null);
        }
        g2d.dispose();
    }
    
    private void changeVelocity() {
        if(left)
            xVelocity -= 0.05;
        if(right)
            xVelocity += 0.05;
        if(up)
            yVelocity -= 0.05;
        if(down)
            yVelocity += 0.05;
        x += xVelocity;
        y += yVelocity;
        xVelocity *= 0.98;
        yVelocity *= 0.98;
    }
    
    private void checkVisibility() {
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
    }
    
    private void adjustMonkFrame() {
        if(++monkFrameCounter % 10 == 0) {
            if(monkFrame != 7)
                ++monkFrame;
            else
                monkFrame = 0;
            monkFrameCounter = 0;
        }
    }
    
    private void constrain() {
        if(x >= Runner.mainFrame.getWidth() - 21) {
            x = Runner.mainFrame.getWidth() - 21;
            xVelocity = -xVelocity;
        }
        else if(x <= 21) {
            x = 21;
            xVelocity = -xVelocity;
        }
        if(y >= Runner.mainFrame.getHeight() - 40) {
            y = Runner.mainFrame.getHeight() - 40;
            yVelocity = -yVelocity;
        }
        else if(y <= 40) {
            y = 40;
            yVelocity = -yVelocity;
        }
    }
    
    public HealthBar getHealthBar() {
        return hb;
    }
    
    public boolean touchingImpurity(Rectangle2D impurityRect) {
        for(int j = 0; j < 60; j++) {
            for(int k = 0; k < 60; k++) {
                if(monkPixels[monkFrame][j][k] != 0 && impurityRect.contains(new Point2D.Double(x - monk[monkFrame].getWidth()/2.0 + k, y - monk[monkFrame].getHeight()/2.0 + j))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void damage() {
        hb.damage(0.21);
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
    
}
