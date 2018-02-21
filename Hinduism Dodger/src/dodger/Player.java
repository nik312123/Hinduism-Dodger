package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
    private double x = Runner.mainFrame.getWidth() / 2, y = (Runner.mainFrame.getHeight() - 55) / 2 + 55;
    private double xVelocity = 0, yVelocity = 0;
    private static double angle;
    
    private int invincibilityFrame = 0;
    private int invincibilitySwitchCounter = 20;
    private int monkFrame = 0;
    private int monkFrameCounter = 0;
    private int punchingFrame = 0;
    private int punchingFrameCounter = 0;
    
    private static int monkPixels[][][] = new int[8][60][60];
    
    private boolean isPunching = false;
    private boolean shouldHurt = false;
    private boolean left = false, right = false, up = false, down = false;
    private boolean isVisible = true;
    
    private HealthBar hb = new HealthBar(new Color(192, 192, 192), Color.GREEN, 50, 35, 200, 15);
    
    private static BufferedImage[] monk = new BufferedImage[8];
    private static BufferedImage[] monkBody = new BufferedImage[8];
    private static BufferedImage[] monkArms = new BufferedImage[8];
    private static BufferedImage[] punchingArms = new BufferedImage[10];
    
    Player() {
        if(Runner.isFirstTime) {
            try {
                for(int i = 0; i < 8; ++i) {
                    monk[i] = ImageIO.read(Runner.class.getResource("/images/monk/fullMonk/monk" + i + ".png"));
                    monkBody[i] = ImageIO.read(Runner.class.getResource("/images/monk/monkBody/body" + i + ".png"));
                    monkArms[i] = ImageIO.read(Runner.class.getResource("/images/monk/monkArms/arms" + i + ".png"));
                    punchingArms[i] = ImageIO.read(Runner.class.getResource("/images/monk/monkPunching/punching" + i + ".png"));
                }
                for(int i = 8; i <= 9; ++i)
                    punchingArms[i] = ImageIO.read(Runner.class.getResource("/images/monk/monkPunching/punching" + i + ".png"));
                for(int i = 0; i < monkPixels.length; ++i)
                    for(int j = 0; j < monkPixels[i].length; ++j)
                        for(int k = 0; k < monkPixels[i][j].length; ++k)
                            monkPixels[i][j][k] = monk[i].getRGB(k, j);
                monk = null;
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void draw(Graphics g) {
        changeVelocity();
        checkVisibility();
        adjustMonkFrame();
        adjustPunchingFrame();
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transform = new AffineTransform();
        BufferedImage shadow = Runner.shadow;
        transform.translate(x - shadow.getWidth() / 2, y - shadow.getHeight() / 2);
        transform.rotate(angle, shadow.getWidth() / 2, shadow.getHeight() / 2);
        if(isVisible)
            g2d.drawImage(shadow, transform, null);
        constrain();
        transform.setToIdentity();
        transform.translate(x - monkBody[monkFrame].getWidth() / 2.0, y - monkBody[monkFrame].getHeight() / 2.0);
        angle = Math.atan2(yVelocity, xVelocity);
        if(angle < 0)
            angle += 2 * Math.PI;
        angle += Math.PI / 2;
        transform.rotate(angle, monkBody[monkFrame].getWidth() / 2, monkBody[monkFrame].getHeight() / 2);
        if(isVisible) {
            if(!up && !down && !left && !right)
                monkFrame = 0;
            if(!isPunching)
                g2d.drawImage(monkArms[monkFrame], transform, null);
            else
                g2d.drawImage(punchingArms[punchingFrame], transform, null);
            g2d.drawImage(monkBody[monkFrame], transform, null);
        }
        transform.setToIdentity();
        g2d.dispose();
    }
    
    private int[][] rotatedGrid() {
        int[][] rotatedGrid = new int[60][60];
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double centerX = 29.5;
        double centerY = centerX;
        for(int x = 0; x < 60; ++x) {
            for(int y = 0; y < 60; ++y) {
                double newX = x - centerX;
                double newY = y - centerY;
                int xx = (int) Math.round(newX * cos - newY * sin + centerX);
                int yy = (int) Math.round(newX * sin + newY * cos + centerY);
                if(xx >= 0 && xx < 60 && yy >= 0 && yy < 60)
                    rotatedGrid[x][y] = monkPixels[monkFrame][xx][yy];
            }
        }
        return rotatedGrid;
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
    
    private void adjustPunchingFrame() {
        if(++punchingFrameCounter % 2 == 0) {
            if(punchingFrame != 9)
                ++punchingFrame;
            else
                punchingFrame = 0;
            punchingFrameCounter = 0;
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
        if(y >= Runner.mainFrame.getHeight() - 20) {
            y = Runner.mainFrame.getHeight() - 20;
            yVelocity = -yVelocity;
        }
        else if(y <= 70) {
            y = 70;
            yVelocity = -yVelocity;
        }
    }
    
    public HealthBar getHealthBar() {
        return hb;
    }
    
    public boolean touchingImpurity(Rectangle2D impurityRect) {
        int[][] rotatedGrid = rotatedGrid();
        for(int i = 0; i < 60; i++) {
            for(int j = 0; j < 60; j++) {
                if(rotatedGrid[i][j] != 0 && impurityRect.contains(new Point2D.Double(x - 60 / 2.0 + j, y - 60 / 2.0 + i))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean touchingPowerUp(Ellipse2D powerUpCircle) {
        int[][] rotatedGrid = rotatedGrid();
        for(int i = 0; i < 60; i++) {
            for(int j = 0; j < 60; j++) {
                if(rotatedGrid[i][j] != 0 && powerUpCircle.contains(new Point2D.Double(x - 60 / 2.0 + j, y - 60 / 2.0 + i))) {
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
    
    public void setPunching(boolean punching) {
        isPunching = punching;
        if(punching)
            shouldHurt = true;
    }
    
    public void setHurting() {
        shouldHurt = false;
    }
    
    public boolean isPunching() {
        return isPunching;
    }
    
    public boolean getHurting() {
        return shouldHurt;
    }
    
}
