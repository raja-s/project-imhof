package ch.epfl.imhof.bonus;

import static java.awt.Font.PLAIN;
import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.*;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;

import ch.epfl.imhof.painting.LineStyle;

/**
 * Classe représentant un générateur de quadrillages
 * (cette classe fait partie du code BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class GridGenerator {
    private final LineStyle STYLE;
    private final double DIV;
    private final double ROUNDING_FACTOR;
    private int approximateStringWidth = 0;

    /**
     * Construit un générateur de quadrillages
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param style
     *      Le style de lignes à utiliser pour dessiner les lignes
     *      du quadrillage
     * @param div
     *      La longueur, en degrés, des divisions du quadrillage
     */
    public GridGenerator(LineStyle style, double div) {
        STYLE = style;
        DIV = div;
        ROUNDING_FACTOR = DIV == 0.01 ? 100 : 10;
    }

    /**
     * Méthode qui retourne une image quadrillée, à fond transparent,
     * et ayant les dimensions passées en paramètre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param width
     *      La largeur en pixels de la carte à quadriller
     * @param height
     *      La heuteur en pixels de la carte à quadriller
     * @param bottom
     *      La latitude en degrés du point bas-gauche de la carte
     * @param top
     *      La latitude en degrés du point haut-droite de la carte
     * @param left
     *      La longitude en degrés du point bas-gauche de la carte
     * @param right
     *      La longitude en degrés du point haut-droite de la carte
     *
     * @return grid
     *      L'image quadrillée sous forme d'une BufferedImage
     */
    public BufferedImage generateGridFor(int width, int height, double bottom, double top, double left, double right, boolean drawStrings) throws Exception {
        BufferedImage grid = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D gridGraphics = grid.createGraphics();
        gridGraphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        gridGraphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        gridGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        gridGraphics.setColor(STYLE.color().convert());
        gridGraphics.setStroke(new BasicStroke(STYLE.width(), STYLE.cap().ordinal(), STYLE.join().ordinal(), 10.0f, STYLE.dash(), 0f));
        int space = width / 150;
        int halfSpace = space / 2;
        double lon = floor(left * ROUNDING_FACTOR) / ROUNDING_FACTOR;
        double lat = floor(bottom * ROUNDING_FACTOR) / ROUNDING_FACTOR;
        int deltaX = (int)(DIV * width / (right - left));
        int deltaY = (int)(DIV * height / (top - bottom));
        int x = (int)((lon - left) * width / (right - left));
        int y = height - (int)((lat - bottom) * height / (top - bottom));
        File fontFile = new File(GridGenerator.class.getClassLoader().getResource("imhof-font.ttf").toURI());
        gridGraphics.setFont(Font.createFont(TRUETYPE_FONT, fontFile).deriveFont(PLAIN, deltaX / 8));
        FontMetrics metrics = gridGraphics.getFontMetrics();
        int fontHeight = metrics.getHeight();
        while (y >= 0) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, y);
            path.lineTo(width, y);
            gridGraphics.draw(path);
            if (drawStrings) {
                String yCoordinate = convertToCoordinates(lat, true);
                int yLeft = y + halfSpace + fontHeight;
                int yRight = y - space;
                approximateStringWidth = metrics.stringWidth(yCoordinate);
                if (yLeft < height - space) {
                    gridGraphics.drawString(yCoordinate, space, yLeft);
                }
                if ((yRight < height - space) && (yRight - fontHeight > space)) {
                    gridGraphics.drawString(yCoordinate, width - space - approximateStringWidth, yRight);
                }
            }
            lat += DIV;
            y -= deltaY;
        }
        while (x <= width) {
            Path2D path = new Path2D.Double();
            path.moveTo(x, 0);
            path.lineTo(x, height);
            gridGraphics.draw(path);
            if (drawStrings) {
                String xCoordinate = convertToCoordinates(lon, false);
                int stringWidth = metrics.stringWidth(xCoordinate);
                int xTop = x - space - stringWidth;
                int xBottom = x + space;
                if (xTop > space) {
                    gridGraphics.drawString(xCoordinate, xTop, halfSpace + fontHeight);
                }
                if ((x > space) && (xBottom + stringWidth < width - space)) {
                    gridGraphics.drawString(xCoordinate, xBottom, height - space);
                }
            }
            lon += DIV;
            x += deltaX;
        }
        return grid;
    }

    private String convertToCoordinates(double value, boolean vertical) {
        StringBuilder result = new StringBuilder();
        char sign;
        if (vertical) {
            sign = value < 0 ? 'S' : 'N';
        } else {
            sign = value < 0 ? 'W' : 'E';
        }
        double decimal = round((value - (int)value) * 100) / 100d;
        int totalSeconds = (int)round(decimal / DIV) * (int)(DIV * 3600);
        int remainingSeconds = totalSeconds % 60;
        int totalMinutes = (totalSeconds - remainingSeconds) / 60;
        int remainingMinutes = totalMinutes % 60;
        String minutesString = remainingMinutes < 10 ? "0" + remainingMinutes : "" + remainingMinutes;
        String secondsString = remainingSeconds < 10 ? "0" + remainingSeconds : "" + remainingSeconds;

        result.append(sign);
        result.append('\u0020');
        if (remainingMinutes == 0 && remainingSeconds == 0) {
            result.append((int)round(abs(value)));
        } else {
            result.append((int)abs(value));
        }
        result.append('\u00B0');
        result.append('\u0020');
        result.append(minutesString + "\u0027" + '\u0020' + secondsString + '\u0022');
        return result.toString();
    }

    public int approximateStringWidth() {
        return approximateStringWidth;
    }
}
