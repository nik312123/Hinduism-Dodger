/*
 * Copyright (c) 2017, Nikunj and/or his affiliates. All rights reserved.
 * NIKUNJ PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package dodger;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

/**
 * The {@code GradientButtonAlwaysVisible} class is a JPanel with a certain
 * graphic representing a button. When hovering over the button,
 * the background changes color from the initial color to the final color.
 * <p>
 * Here is an example of how the {@code GradientButtonAlwaysVisible} object can be initialized:
 * <blockquote><pre>
 *     GradientButtonAlwaysVisible gb = new GradientButtonAlwaysVisible(buttonImage, Color.BLACK, Color.WHITE, 0, 0, 20, 20) {
 *
 *          @Override
 *          public void mouseClicked(MouseEvent e) {
 *              if(onButton()) {
 *                  //Do stuff
 *              }
 *          }
 *
 *          @Override
 *          public void mousePressed(MouseEvent e) {}
 *
 *          @Override
 *          public void mouseReleased(MouseEvent e) {}
 *
 *          @Override
 *          public void mouseEntered(MouseEvent e) {}
 *
 *          @Override
 *          public void mouseExited(MouseEvent e) {}
 *
 *          @Override
 *          public void mouseDragged(MouseEvent e) {}
 *
 *          @Override
 *          public void mouseMoved(MouseEvent e) {}
 *
 *     };
 * </pre></blockquote>
 * <p>
 * The class {@code GradientButtonAlwaysVisible} includes methods for drawing the button and determining whether on the button
 *
 * @author  Nikunj Chawla
 */
public abstract class GradientButtonAlwaysVisible extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    /**
     * The image that will be used behind the button
     */
    private Image buttonIcon;

    /**
     * The initial button color
     */
    private Color initialColor;
    /**
     * The color the button changes to when hovered over
     */
    private Color finalColor;

    /**
     * The shape that is filled with the colors (button's shape)
     */
    private Shape s;
    /**
     * Clone of shape s but at (0, 0)
     */
    private Shape relativeS;

    /**
     * x coordinate of button
     */
    private int x;
    /**
     * y coordinate of button
     */
    private int y;
    /**
     * This divided by steps is the percentage the initial color has been transitioned to the final color
     */
    private int colorChange = 0;
    /**
     * The number of steps it takes to change the colors
     */
    private int steps = 25;

    /**
     * Initializes a newly created {@code GradientButtonAlwaysVisible} with a rectangular shape and 25 steps to a color change
     * @param buttonIcon        An image that is used for the button's icon
     * @param initialColor      The initial button color
     * @param finalColor        The color the button changes to when on the button
     * @param x                 The x coordinate of the button
     * @param y                 The y coordinate of the button
     * @param width             The width of the button
     * @param height            The height of the button
     */
    public GradientButtonAlwaysVisible(Image buttonIcon, Color initialColor, Color finalColor, int x, int y, int width, int height) {
        this.buttonIcon = buttonIcon;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        this.x = x;
        this.y = y;
        s = new Rectangle(x, y, width, height);
        relativeS = new Rectangle(0, 0, width, height);
        setBounds(x, y, width, height);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Initializes a newly created {@code GradientButtonAlwaysVisible} with a rectangular shape
     * @param buttonIcon        An image that is used for the button's icon
     * @param initialColor      The initial button color
     * @param finalColor        The color the button changes to when on the button
     * @param stepsToChange     The number of steps it takes to change the colors
     * @param x                 The x coordinate of the button
     * @param y                 The y coordinate of the button
     * @param width             The width of the button
     * @param height            The height of the button
     */
    public GradientButtonAlwaysVisible(Image buttonIcon, Color initialColor, Color finalColor, int stepsToChange, int x, int y, int width, int height) {
        this.buttonIcon = buttonIcon;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        this.x = x;
        this.y = y;
        steps = stepsToChange;
        s = new Rectangle(x, y, width, height);
        relativeS = new Rectangle(0, 0, width, height);
        setBounds(x, y, width, height);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Initializes a newly created {@code GradientButtonAlwaysVisible} with a rectangular shape
     * @param buttonIcon        An image that is used for the button's icon
     * @param initialColor      The initial button color
     * @param finalColor        The color the button changes to when on the button
     * @param s                 The shape that is filled with the colors (button's shape)
     * @param x                 The x coordinate of the button
     * @param y                 The y coordinate of the button
     * @param width             The width of the button
     * @param height            The height of the button
     */
    public GradientButtonAlwaysVisible(Image buttonIcon, Color initialColor, Color finalColor, Shape s, int x, int y, int width, int height) {
        this.buttonIcon = buttonIcon;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        this.x = x;
        this.y = y;
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        this.s = transform.createTransformedShape(s);
        transform.setToIdentity();
        transform.translate(0, 0);
        relativeS = transform.createTransformedShape(s);
        setBounds(x, y, width, height);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Initializes a newly created {@code GradientButtonAlwaysVisible}
     * @param buttonIcon        An image that is used for the button's icon
     * @param initialColor      The initial button color
     * @param finalColor        The color the button changes to when on the button
     * @param s                 The shape that is filled with the colors (button's shape)
     * @param stepsToChange     The number of steps it takes to change the colors
     * @param x                 The x coordinate of the button
     * @param y                 The y coordinate of the button
     * @param width             The width of the button
     * @param height            The height of the button
     */
    public GradientButtonAlwaysVisible(Image buttonIcon, Color initialColor, Color finalColor, Shape s, int stepsToChange, int x, int y, int width, int height) {
        this.buttonIcon = buttonIcon;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
        this.x = x;
        this.y = y;
        steps = stepsToChange;
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        this.s = transform.createTransformedShape(s);
        transform.setToIdentity();
        transform.translate(0, 0);
        relativeS = transform.createTransformedShape(s);
        setBounds(x, y, width, height);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Draws the button
     * @param g    Graphics object from paintComponent
     */
    public final void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        beforeDraw(g2d);
        if(onButton())
            colorChange = Math.min(colorChange + 1, steps);
        else
            colorChange = Math.max(colorChange - 1, 0);
        g2d.setColor(initialColor);
        g2d.fill(s);
        Composite original = g2d.getComposite();
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) colorChange/steps);
        g2d.setComposite(alpha);
        g2d.setColor(finalColor);
        g2d.fill(s);
        g2d.setComposite(original);
        g2d.drawImage(buttonIcon, x, y, null);
        afterDraw(g2d);
        g2d.dispose();
    }

    /**
     * Whether or not the mouse is on the button, can be overridden for different specifications
     */
    public boolean onButton() {
        if(!isVisible() || !isDisplayable())
            return false;
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        mousePos = new Point((int) (mousePos.getX() - getLocationOnScreen().getX()), (int) (mousePos.getY() - getLocationOnScreen().getY()));
        return relativeS.contains(mousePos.x, mousePos.y);
    }

    /**
     * Sets the x coordinate to the given integer
     * @param x    The new x coordinate of the GradientButtonAlwaysVisible
     */
    public void setX(int x) {
        this.x = x;
        setBounds(x, y, getWidth(), getHeight());
    }

    /**
     * Sets the y coordinate to the given integer
     * @param y    The new y coordinate of the GradientButtonAlwaysVisible
     */
    public void setY(int y) {
        this.y = y;
        setBounds(x, y, getWidth(), getHeight());
    }

    /**
     * Sets the width to the given integer
     * @param width    The new width of the GradientButtonAlwaysVisible
     */
    public void setWidth(int width) {
        setBounds(x, y, width, getHeight());
    }

    /**
     * Sets the height to the given integer
     * @param height    The new height of the GradientButtonAlwaysVisible
     */
    public void setHeight(int height) {
        setBounds(x, y, getWidth(), height);
    }

    /**
     * Overridable method to do something before every time the button is drawn
     * @param g    Graphics object from paintComponent
     */
    public void beforeDraw(Graphics g) {}

    /**
     * Overridable method to do something after every time the button is drawn
     * @param g    Graphics object from paintComponent
     */
    public void afterDraw(Graphics g) {}

}
