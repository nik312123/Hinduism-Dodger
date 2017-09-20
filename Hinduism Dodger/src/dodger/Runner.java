package dodger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Runner extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    private static boolean injured = false;
        
    private static Player p;
    
    private ArrayList<BadKarma> impurities = new ArrayList<BadKarma>();
        
    public static JFrame mainFrame;
    
    private static Timer invincibilityTimer;
    
    public static void main(String[] args) {
        Runner r = new Runner();
        r.setSize(700, 700);
        
        mainFrame = new JFrame();
        mainFrame.setSize(700, 700);
        
        p = new Player();
        BadKarma.p = p;
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(r);
        mainFrame.addKeyListener(r);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - mainFrame.getWidth() / 2, dim.height / 2 - mainFrame.getHeight() / 2);
        mainFrame.setVisible(true);
        
        Timer drawTimer = new Timer(5, r);
        drawTimer.start();
        
        Timer impuritySpawner = new Timer(500, r);
        impuritySpawner.setActionCommand("spawn");
        impuritySpawner.start();
        
        invincibilityTimer = new Timer(1000, r);
        invincibilityTimer.setActionCommand("invincible");
        invincibilityTimer.setRepeats(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        p.draw(g2d);
        for(int i = 0; i < impurities.size(); ++i) {
            BadKarma bk = impurities.get(i);
            if(bk.isOutOfBounds()) {
                impurities.remove(i);
                --i;
            }
            else if(!injured && p.touchingImpurity(bk.getRect())) {
                injured = true;
                impurities.remove(i);
                --i;
                p.damage();
                invincibilityTimer.start();
            }
            else
                bk.draw(g2d);
        }
        g2d.dispose();
        g.dispose();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                p.setUp(true);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                p.setDown(true);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                p.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                p.setRight(true);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                p.setUp(false);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                p.setDown(false);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                p.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                p.setRight(false);
                break;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e == null || e.getActionCommand() == null || (!e.getActionCommand().equals("spawn") && !e.getActionCommand().equals("invincible")))
            repaint();
        else if(e.getActionCommand().equals("spawn"))
            impurities.add(new BadKarma());
        else
            injured = false;
    }
    
    public static boolean isInjured() {
        return injured;
    }
    
}
