package dodger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class HealthBar extends RoundRectangle2D.Double {

    private static final long serialVersionUID = 1L;
    
    private double healthPercentage = 1.0;
    
    private boolean isHorizontal;
    
    private Color back, front;
        
    public HealthBar(Color back, Color front, double x, double y, double w, double h) {
        super(x, y, w, h, (w >= h) ? h : w, (w >= h) ? h : w);
        isHorizontal = w >= h;
        this.back = back;
        this.front = front;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(back);
        g2d.fill(this);
        g2d.setColor(front);
        if(isHorizontal)
            g2d.fill(new RoundRectangle2D.Double(x, y, width * healthPercentage, height, arcwidth, archeight));
        else
            g2d.fill(new RoundRectangle2D.Double(x, y, width, height * healthPercentage, arcwidth, archeight));
    }
    
    public void damage(double percent) {
        healthPercentage = Math.max(0, healthPercentage - percent);
    }
    
    public double getPercentage() {
        return healthPercentage;
    }
    
}
