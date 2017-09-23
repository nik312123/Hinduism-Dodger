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
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import nikunj.classes.GradientButton;

public class Runner extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    private static int score = 0;
    private static int scoreCounter = 0;
    private static int impuritySpawnerCounter = 0;
    
    private static boolean injured = false;
    private static boolean resetting = false;
    public static boolean isFirstTime = true;
        
    private static Player p;
    
    private static GradientButton resetButton;
    private static GradientButton closeButton;
    private static GradientButton draggableButton;
    
    private static ArrayList<BadKarma> impurities;
        
    public static JFrame mainFrame;
    
    private static Timer invincibilityTimer;
    private static Timer impuritySpawner;
    private static Timer drawTimer;
    
    private static CountDownLatch drawingDone;
    
    private static Font hpFont;
    private static Font scoreFont;
    
    private static BufferedImage reset;
    private static BufferedImage close;
    private static BufferedImage draggable;
    public static BufferedImage[] badKarma = new BufferedImage[9];
    public static BufferedImage[] pulse = new BufferedImage[29];
    
    public static void main(String... args) throws FontFormatException, IOException {
        impurities = new ArrayList<BadKarma>();
        
        if(isFirstTime) {
            hpFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/deteSans.otf").openStream()).deriveFont(18.0f);
            scoreFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/8BO_JVE.ttf").openStream()).deriveFont(20.0f);
            for(int i = 0; i < 9; ++i)
                badKarma[i] = ImageIO.read(Runner.class.getResource("/images/karma/karma" + i + ".png"));
            for(int i = 0; i < 29; ++i)
                pulse[i] = ImageIO.read(Runner.class.getResource("/images/pulse/pulse" + i + ".png"));
            reset = ImageIO.read(Runner.class.getResource("/images/reset.png"));
            close = ImageIO.read(Runner.class.getResource("/images/close.png"));
            draggable = ImageIO.read(Runner.class.getResource("/images/draggable.png"));
        }
        
        Runner r = new Runner();
        r.setSize(600, 610);
                
        mainFrame = new JFrame();
        mainFrame.setSize(600, 610);
        
        p = new Player();
        BadKarma.p = p;
        
        resetButton = new GradientButton(reset, new Color(0, 191, 255), Color.GREEN, new RoundRectangle2D.Double(0, 0, 350, 100, 75, 75), 100, (mainFrame.getWidth() - 350)/2, 450, 350, 100) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isVisible())
                    reset();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        closeButton = new GradientButton(close, new Color(105, 105, 105), Color.RED, 40, 2, 2, 24, 24) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {}
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            
            @Override
            public void mouseEntered(MouseEvent e) {}
            
            @Override
            public void mouseExited(MouseEvent e) {}
            
            @Override
            public void mouseDragged(MouseEvent e) {}
            
            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        draggableButton = new GradientButton(draggable, new Color(105, 105, 105), Color.BLUE, 40, 30, 2, 24, 24) {
            private static final long serialVersionUID = 1L;
            private int xPos, yPos;
            
            @Override
            public void mouseClicked(MouseEvent e) {}
            
            @Override
            public void mousePressed(MouseEvent e) {
                xPos = e.getX();
                yPos = e.getY();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            
            @Override
            public void mouseEntered(MouseEvent e) {}
            
            @Override
            public void mouseExited(MouseEvent e) {}
            
            @Override
            public void mouseDragged(MouseEvent e) {
                mainFrame.setLocation((int) (mainFrame.getLocation().getX() + e.getX() - xPos), (int) (mainFrame.getLocation().getY() + e.getY() - yPos));
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        resetButton.setVisible(false);
        closeButton.setVisible(true);
        draggableButton.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(r);
        mainFrame.add(resetButton);
        mainFrame.add(closeButton);
        mainFrame.add(draggableButton);
        mainFrame.addKeyListener(r);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - mainFrame.getWidth() / 2, dim.height / 2 - mainFrame.getHeight() / 2);
        mainFrame.setLayout(null);
        mainFrame.setUndecorated(true);
        mainFrame.setVisible(true);
        
        drawTimer = new Timer(5, r);
        drawTimer.start();
        
        impuritySpawner = new Timer(1000, r);
        impuritySpawner.setActionCommand("spawn");
        impuritySpawner.start();
        
        invincibilityTimer = new Timer(1000, r);
        invincibilityTimer.setActionCommand("invincible");
        invincibilityTimer.setRepeats(false);
        
        drawingDone = new CountDownLatch(1);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if(!resetting) {
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
                g2d.fillRect(0, 0, mainFrame.getWidth(), 55);
                p.getHealthBar().draw(g2d);
                g2d.setColor(Color.WHITE);
                g2d.setFont(hpFont);
                g2d.drawString("HP:", 20, 48);
                g2d.setFont(scoreFont);
                g2d.drawString("Score: " + score, 450, 48);
                if(++scoreCounter % 100 == 0) {
                    score += 10;
                    scoreCounter = 0;
                }
            }
            else {
                g2d.setColor(new Color(96, 96, 96));
                g2d.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
                g2d.setFont(hpFont.deriveFont(124.0f));
                g2d.setColor(Color.WHITE);
                String gameOver = "Game Over";
                g2d.drawString(gameOver, (mainFrame.getWidth() - g2d.getFontMetrics().stringWidth(gameOver))/2 + 5, 200);
                g2d.setFont(scoreFont.deriveFont(90.0f));
                String scoreString = "Score: " + score;
                g2d.drawString(scoreString, (mainFrame.getWidth() - g2d.getFontMetrics().stringWidth(scoreString))/2, 400);
                resetButton.draw(g2d);
                resetButton.setVisible(true);
            }
            closeButton.draw(g2d);
            draggableButton.draw(g2d);
            g2d.dispose();
            if(++impuritySpawnerCounter % 1500 == 0 && impuritySpawner.getDelay() > 200) {
                impuritySpawner.setDelay(impuritySpawner.getDelay() - 50);
                impuritySpawnerCounter = 0;
            }
        }
        else
            drawingDone.countDown();
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
    
    public static void reset() {
        Thread t = new Thread() {
            @Override
            public void run() {
                resetting = true;
                try {
                    drawingDone.await();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                invincibilityTimer.stop();
                invincibilityTimer = null;
                impuritySpawner.stop();
                impuritySpawner = null;
                drawTimer.stop();
                drawTimer = null;
                score = 0;
                scoreCounter = 0;
                impuritySpawnerCounter = 0;
                injured = false;
                p = null;
                BadKarma.p = null;
                mainFrame.dispose();
                mainFrame = null;
                drawingDone = null;
                impurities = null;
                System.gc();
                resetting = false;
                try {
                    main();
                }
                catch(FontFormatException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    
}
