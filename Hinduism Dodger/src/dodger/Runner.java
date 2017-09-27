package dodger;

import java.awt.BasicStroke;
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import nikunj.classes.GradientButton;
import nikunj.classes.Sound;

public class Runner extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    
    private static int score = 0;
    private static int scoreCounter = 0;
    private static int impuritySpawnerCounter = 0;
    private static int nameStringX = 610;
    private static int nameStringCounter = 0;
    
    private static double mokshaY = 0;
    private static double mokshaTheta = 0;
    
    private static boolean injured = false;
    private static boolean resetting = false;
    private static boolean isBeginning = true;
    private static boolean musicMuted = false;
    private static boolean sfxMuted = false;
    public static boolean isFirstTime = true;
        
    private static Player p;
    
    private static Sound menu;
    private static Sound main;
    private static Sound gameOver;
    private static Sound hit;
    private static Sound buttonHover;
    private static Sound buttonClick;
    
    private static GradientButton resetButton;
    private static GradientButton closeButton;
    private static GradientButton draggableButton;
    private static GradientButton normalButton;
    private static GradientButton powerUpButton;
    private static GradientButton creditsButton;
    private static GradientButton storyButton;
    private static GradientButton instructionsButton;
    private static GradientButton musicButton;
    private static GradientButton sfxButton;
    
    private static ArrayList<BadKarma> impurities;
        
    public static JFrame mainFrame;
    
    private static Timer invincibilityTimer;
    private static Timer impuritySpawner;
    private static Timer drawTimer;
    
    private static CountDownLatch drawingDone;
    
    private static Font hpFont;
    private static Font scoreFont;
    
    private static final Color BUTTON_COLOR_INITIAL = new Color(110, 110, 110);
    private static final Color BUTTON_COLOR_FINAL = new Color(255, 140, 0);
    
    private static BufferedImage reset;
    private static BufferedImage close;
    private static BufferedImage draggable;
    private static BufferedImage music;
    private static BufferedImage sfx;
    private static BufferedImage normal;
    private static BufferedImage powerUp;
    private static BufferedImage credits;
    private static BufferedImage story;
    private static BufferedImage instructions;
    private static BufferedImage menuScreen;
    private static BufferedImage moksha;
    public static BufferedImage[] badKarma = new BufferedImage[9];
    public static BufferedImage[] pulse = new BufferedImage[29];
    
    public static void main(String... args) throws FontFormatException, IOException, UnsupportedAudioFileException {
        impurities = new ArrayList<BadKarma>();
        
        if(isFirstTime) {
            hpFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/deteSans.otf").openStream()).deriveFont(18.0f);
            scoreFont = Font.createFont(Font.TRUETYPE_FONT, Runner.class.getResource("/fonts/8BO_JVE.ttf").openStream()).deriveFont(20.0f);
            for(int i = 0; i < 9; ++i)
                badKarma[i] = ImageIO.read(Runner.class.getResource("/images/karma/karma" + i + ".png"));
            for(int i = 0; i < 29; ++i)
                pulse[i] = ImageIO.read(Runner.class.getResource("/images/pulse/pulse" + i + ".png"));
            reset = ImageIO.read(Runner.class.getResource("/images/reset.png"));
            close = ImageIO.read(Runner.class.getResource("/images/topBarButtons/close.png"));
            draggable = ImageIO.read(Runner.class.getResource("/images/topBarButtons/draggable.png"));
            music = ImageIO.read(Runner.class.getResource("/images/topBarButtons/music.png"));
            sfx = ImageIO.read(Runner.class.getResource("/images/topBarButtons/sfx.png"));
            normal = ImageIO.read(Runner.class.getResource("/images/startScreenButtons/normal.png"));
            powerUp = ImageIO.read(Runner.class.getResource("/images/startScreenButtons/powerup.png"));
            credits = ImageIO.read(Runner.class.getResource("/images/startScreenButtons/credits.png"));
            story = ImageIO.read(Runner.class.getResource("/images/startScreenButtons/story.png"));
            instructions = ImageIO.read(Runner.class.getResource("/images/startScreenButtons/instructions.png"));
            menuScreen = ImageIO.read(Runner.class.getResource("/images/menuScreen.png"));
            moksha = ImageIO.read(Runner.class.getResource("/images/moksha.png"));
            menu = new Sound(Runner.class.getResource("/audio/menu.wav"), true);
            main = new Sound(Runner.class.getResource("/audio/main.wav"), true);
            gameOver = new Sound(Runner.class.getResource("/audio/gameOver.wav"), true);
            hit = new Sound(Runner.class.getResource("/audio/hit.wav"), false);
            buttonHover = new Sound(Runner.class.getResource("/audio/buttonHover.wav"), false);
            buttonClick = new Sound(Runner.class.getResource("/audio/buttonClick.wav"), false);
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
                if(isVisible()) {
                    buttonClick.play();
                    reset();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

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
        musicButton = new GradientButton(music, new Color(105, 105, 105), new Color(0, 208, 208), 545, 2, 24, 24) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if(musicMuted) {
                    menu.changeVolume(1);
                    main.changeVolume(1);
                    gameOver.changeVolume(1);
                }
                else {
                    menu.changeVolume(0);
                    main.changeVolume(0);
                    gameOver.changeVolume(0);
                }
                musicMuted = !musicMuted;
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
            
            @Override
            public void afterDraw(Graphics g) {
                if(musicMuted) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(new Line2D.Float(549, 22, 565, 4));
                }
            }
            
        };
        
        sfxButton = new GradientButton(sfx, new Color(105, 105, 105), Color.GREEN, 573, 2, 24, 24) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if(sfxMuted) {
                    hit.changeVolume(1);
                    buttonHover.changeVolume(1);
                    buttonClick.changeVolume(1);
                }
                else {
                    hit.changeVolume(0);
                    buttonHover.changeVolume(0);
                    buttonClick.changeVolume(0);
                }
                sfxMuted = !sfxMuted;
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
            
            @Override
            public void afterDraw(Graphics g) {
                if(sfxMuted) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(new Line2D.Float(577, 22, 593, 4));
                }
            }
            
        };
        normalButton = new GradientButton(normal, BUTTON_COLOR_INITIAL, BUTTON_COLOR_FINAL, 101, 254, normal.getWidth(), normal.getHeight()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                isBeginning = false;
                buttonClick.play();
                impuritySpawner.start();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        powerUpButton = new GradientButton(powerUp, BUTTON_COLOR_INITIAL, BUTTON_COLOR_FINAL, 351, 254, powerUp.getWidth(), powerUp.getHeight()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isVisible())
                    buttonClick.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        creditsButton = new GradientButton(credits, BUTTON_COLOR_INITIAL, BUTTON_COLOR_FINAL, 39, 393, credits.getWidth(), credits.getHeight()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isVisible())
                    buttonClick.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        storyButton = new GradientButton(story, BUTTON_COLOR_INITIAL, BUTTON_COLOR_FINAL, 226, 393, story.getWidth(), story.getHeight()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isVisible())
                    buttonClick.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        instructionsButton = new GradientButton(instructions, BUTTON_COLOR_INITIAL, BUTTON_COLOR_FINAL, 413, 393, instructions.getWidth(), instructions.getHeight()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(isVisible())
                    buttonClick.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                if(isVisible())
                    buttonHover.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
            
        };
        resetButton.setVisible(false);
        closeButton.setVisible(true);
        draggableButton.setVisible(true);
        musicButton.setVisible(true);
        sfxButton.setVisible(true);
        normalButton.setVisible(true);
        powerUpButton.setVisible(true);
        creditsButton.setVisible(true);
        storyButton.setVisible(true);
        instructionsButton.setVisible(true);
        
        mainFrame.add(r);
        mainFrame.add(resetButton);
        mainFrame.add(closeButton);
        mainFrame.add(draggableButton);
        mainFrame.add(musicButton);
        mainFrame.add(sfxButton);
        mainFrame.add(normalButton);
        mainFrame.add(powerUpButton);
        mainFrame.add(creditsButton);
        mainFrame.add(storyButton);
        mainFrame.add(instructionsButton);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        invincibilityTimer = new Timer(1000, r);
        invincibilityTimer.setActionCommand("invincible");
        invincibilityTimer.setRepeats(false);
        
        drawingDone = new CountDownLatch(1);
        menu.play();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        if(!resetting) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(isBeginning) {
                g2d.drawImage(menuScreen, 0, 0, null);
                AffineTransform mokshaTransform = new AffineTransform();
                mokshaTransform.translate(0, mokshaY);
                g2d.drawImage(moksha, mokshaTransform, null);
                mokshaTheta += Math.PI/360;
                mokshaY = 10 * Math.sin(mokshaTheta);
                if(mokshaTheta >= 2 * Math.PI - Math.PI/3600)
                    mokshaTheta = 0;
                normalButton.draw(g2d);
                powerUpButton.draw(g2d);
                creditsButton.draw(g2d);
                storyButton.draw(g2d);
                instructionsButton.draw(g2d);
                drawNames(g2d);
            }
            else if(p.getHealthBar().getPercentage() != 0) {
                normalButton.setVisible(false);
                powerUpButton.setVisible(false);
                creditsButton.setVisible(false);
                storyButton.setVisible(false);
                instructionsButton.setVisible(false);
                if(!menu.isStopped()) {
                    menu.stop();
                    main.play();
                }
                p.draw(g2d);
                drawAndModifyImpurities(g2d);
                drawTopBar(g2d);
                if(++scoreCounter % 100 == 0) {
                    score += 10;
                    scoreCounter = 0;
                }
                if(++impuritySpawnerCounter % 1500 == 0 && impuritySpawner.getDelay() > 200) {
                    impuritySpawner.setDelay(impuritySpawner.getDelay() - 50);
                    impuritySpawnerCounter = 0;
                }
            }
            else {
                if(!main.isStopped()) {
                    main.stop();
                    gameOver.play();
                }
                drawGameOver(g2d);
            }
            closeButton.draw(g2d);
            draggableButton.draw(g2d);
            musicButton.draw(g2d);
            sfxButton.draw(g2d);
            g2d.dispose();
        }
        else
            drawingDone.countDown();
    }
    
    private void drawNames(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(scoreFont.deriveFont(28f));
        g2d.drawString("Made by Nikunj Chawla", nameStringX, 575);
        if(++nameStringCounter % 4 == 0) {
            --nameStringX;
            nameStringCounter = 0;
            if(nameStringX == -260)
                nameStringX = 610;
        }
    }
    
    private void drawAndModifyImpurities(Graphics2D g2d) {
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
                hit.play();
                invincibilityTimer.start();
            }
            else
                bk.draw(g2d);
        }
    }
    
    private void drawTopBar(Graphics2D g2d) {
        g2d.setColor(new Color(105, 105, 105));
        g2d.fillRect(0, 0, mainFrame.getWidth(), 55);
        p.getHealthBar().draw(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setFont(hpFont);
        g2d.drawString("HP:", 20, 48);
        g2d.setFont(scoreFont);
        g2d.drawString("Score: " + score, 450, 48);
    }
    
    private void drawGameOver(Graphics2D g2d) {
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
                gameOver.stop();
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
                resetButton = null;
                closeButton = null;
                draggableButton = null;
                normalButton = null;
                powerUpButton = null;
                creditsButton = null;
                storyButton = null;
                instructionsButton = null;
                isBeginning = true;
                System.gc();
                resetting = false;
                try {
                    main();
                }
                catch(FontFormatException | IOException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    
}
