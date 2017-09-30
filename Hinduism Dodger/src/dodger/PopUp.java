package dodger;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class PopUp extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;

    private double x, y, width, height;
    
    private int steps;
    private int expansionStage = 0;
    
    private boolean isExpanding = false;
    
    private Color color;
    
    private GradientPaint gradient;
    
    private Rectangle2D popUp;
    
    public PopUp(double x, double y, double w, double h, int expansionSteps, Color popUpColor) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        color = popUpColor;
        steps = expansionSteps;
        popUp = new Rectangle2D.Double(0, 0, width, height);
        addMouseListener(this);
    }
    
    public PopUp(double x, double y, double w, double h, int expansionSteps, Color topColor, Color bottomColor) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        steps = expansionSteps;
        gradient = new GradientPaint((float) (x + width/2), (float) y, topColor, (float) (x + width/2), (float) (y + height), bottomColor);
        popUp = new Rectangle2D.Double(0, 0, width, height);
        addMouseListener(this);
    }
    
    public void draw(Graphics g) {
        if(isExpanding)
            expansionStage = Math.min(expansionStage + 1, steps);
        else
            expansionStage = Math.max(expansionStage - 1, 0);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform trans = new AffineTransform();
        trans.translate(x + width/2 * (1 - (double) expansionStage/steps), y + height/2 * (1 - (double) expansionStage/steps));
        trans.scale((double) expansionStage/steps, (double) expansionStage/steps);
        setBounds((int) trans.getTranslateX(), (int) trans.getTranslateY(), (int) (width * expansionStage/steps), (int) (height * expansionStage/steps));
        if(color != null)
            g2d.setColor(color);
        else
            g2d.setPaint(gradient);
        g2d.fill(trans.createTransformedShape(popUp));
    }
    
    public void setExpanding(boolean expanding) {
        isExpanding = expanding;
    }
    
    public boolean getExpanding() {
        return isExpanding;
    }
    
    public double percentageExpanded() {
        return (double) expansionStage/steps;
    }
    
    public double getExpandedX() {
        return x;
    }
    
    public double getExpandedY() {
        return y;
    }
    
    public double getExpandedWidth() {
        return width;
    }
    
    public double getExpandedHeight() {
        return height;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        isExpanding = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
