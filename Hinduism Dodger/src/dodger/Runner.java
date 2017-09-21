package dodger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Runner extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    private static int score = 0;
    private static int scoreCounter = 0;
    private static int impuritySpawnerCounter = 0;
    
    private static boolean injured = false;
        
    private static Player p;
    
    private ArrayList<BadKarma> impurities = new ArrayList<BadKarma>();
        
    public static JFrame mainFrame;
    
    private static Timer invincibilityTimer;
    private static Timer impuritySpawner;
    
    private static Font hpFont;
    private static Font scoreFont;
    
    public static void main(String[] args) throws FontFormatException, IOException {
        hpFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/deteSans.otf").openStream()).deriveFont(18.0f);
        scoreFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/8BO_JVE.ttf").openStream()).deriveFont(20.0f);
        
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
        
        impuritySpawner = new Timer(1000, r);
        impuritySpawner.setActionCommand("spawn");
        impuritySpawner.start();
        
        invincibilityTimer = new Timer(1000, r);
        invincibilityTimer.setActionCommand("invincible");
        invincibilityTimer.setRepeats(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(p.getHealthBar().getPercentage() != 0) {
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
            g2d.setColor(new Color(105, 105, 105));
            g2d.fillRect(0, 0, mainFrame.getWidth(), 25);
            p.getHealthBar().draw(g2d);
            g2d.setColor(Color.WHITE);
            g2d.setFont(hpFont);
            g2d.drawString("HP:", 20, 18);
            g2d.setFont(scoreFont);
            g2d.drawString("Score: " + score, 550, 18);
            g2d.dispose();
            if(++scoreCounter % 100 == 0) {
                score += 10;
                scoreCounter = 0;
            }
        }
        else {
            g2d.setColor(new Color(96, 96, 96));
            g2d.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
            g2d.setFont(hpFont.deriveFont(150.0f));
            g2d.setColor(Color.WHITE);
            String gameOver = "Game Over";
            g2d.drawString(gameOver, (mainFrame.getWidth() - g2d.getFontMetrics().stringWidth(gameOver))/2 + 5, 200);
            g2d.setFont(scoreFont.deriveFont(90.0f));
            String scoreString = "Score: " + score;
            g2d.drawString(scoreString, (mainFrame.getWidth() - g2d.getFontMetrics().stringWidth(scoreString))/2, 400);
        }
        g2d.dispose();
        if(++impuritySpawnerCounter % 1500 == 0 && impuritySpawner.getDelay() > 200) {
            impuritySpawner.setDelay(impuritySpawner.getDelay() - 50);
            impuritySpawnerCounter = 0;
        }
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
