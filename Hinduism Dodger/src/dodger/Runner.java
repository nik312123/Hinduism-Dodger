package dodger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import nikunj.classes.Sound;

public class Runner extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    private static boolean injured = false;
    
    private static Sound ouch;
    
    private static Player p;
    
    private ArrayList<Impurity> impurities = new ArrayList<Impurity>();
        
    public static JFrame mainFrame;
    
    private static Timer invincibilityTimer;
    
    public static void main(String[] args) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Runner r = new Runner();
        r.setSize(700, 700);
        mainFrame = new JFrame();
        mainFrame.setSize(700, 700);
        p = new Player();
        Impurity.p = p;
        Timer t = new Timer(5, r);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(r);
        mainFrame.addKeyListener(r);
        mainFrame.setLocation(dim.width / 2 - mainFrame.getWidth() / 2, dim.height / 2 - mainFrame.getHeight() / 2);
        mainFrame.setVisible(true);
        t.start();
        Timer impuritySpawner = new Timer(500, r);
        impuritySpawner.setActionCommand("spawn");
        impuritySpawner.start();
        try {
            ouch = new Sound(Runner.class.getResource("/audio/ouch.wav"), false);
        }
        catch(UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        invincibilityTimer = new Timer(1000, r);
        invincibilityTimer.setActionCommand("invincible");
        invincibilityTimer.setRepeats(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        p.draw(g);
        for(int i = 0; i < impurities.size(); ++i) {
            Impurity imp = impurities.get(i);
            if(!injured && imp.getRect().intersects(p.getRect())) {
                injured = true;
                impurities.remove(i);
                --i;
                ouch.play();
                invincibilityTimer.start();
            }
            else
                impurities.get(i).draw(g);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                p.setUp(true);
                break;
            case KeyEvent.VK_DOWN:
                p.setDown(true);
                break;
            case KeyEvent.VK_LEFT:
                p.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                p.setRight(true);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                p.setUp(false);
                break;
            case KeyEvent.VK_DOWN:
                p.setDown(false);
                break;
            case KeyEvent.VK_LEFT:
                p.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                p.setRight(false);
                break;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e == null || e.getActionCommand() == null || (!e.getActionCommand().equals("spawn") && !e.getActionCommand().equals("invincible")))
            repaint();
        else if(e.getActionCommand().equals("spawn"))
            impurities.add(new Impurity());
        else
            injured = false;
    }
    
    public static boolean isInjured() {
        return injured;
    }
    
}
