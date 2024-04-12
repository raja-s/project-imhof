package ch.epfl.imhof.bonus;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.awt.Font.*;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import ch.epfl.imhof.PaintingConfiguration;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle;

/**
 * Classe représentant un générateur de légendes
 * (cette classe fait partie du code BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class LegendGenerator {
    private final List<LegendCouple> COUPLES;
    private final Color ADJUSTER = Color.gray(0.9);
    private final java.awt.Color DARK_BLUE = Color.rgb(0, 0, 0.15).convert();
    private final float LINE_SCALE;
    private final float FONT_SIZE;
    private final int WIDTH;
    private final int HEIGHT;
    private final int X_POS;
    private final int ELEMENT_WIDTH;
    private final int ELEMENT_HEIGHT;
    private final boolean ENOUGH_SPACE;

    /**
     * Construit un générateur de légendes
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param width
     *      La largeur de la carte qu'on veut légender
     * @param height
     *      La hauteur de la carte qu'on veut légender
     * @param res
     *      La résolution de l'image correspondant à la
     *      carte qu'on veut légender
     */
    public LegendGenerator(int width, int height, double res, PaintingConfiguration configuration) {
        COUPLES = configuration.legendCouples();
        if ((height >= 1800) && (width >= height) && (width <= 4 * height)) {
            ENOUGH_SPACE = true;
            HEIGHT = height < 2400 ? 1200 : height / 2;
            WIDTH = width < 3000 ? 500 : width / 6;
            X_POS = WIDTH / 12;
            ELEMENT_WIDTH = WIDTH / 7;
            ELEMENT_HEIGHT = ELEMENT_WIDTH / 2;
            FONT_SIZE = (float)ELEMENT_HEIGHT * 0.8f;
        } else {
            ENOUGH_SPACE = false;
            HEIGHT = 0;
            WIDTH = 0;
            X_POS = 0;
            ELEMENT_WIDTH = 0;
            ELEMENT_HEIGHT = 0;
            FONT_SIZE = 0f;
        }
        LINE_SCALE = (float)(res / 72);
    }

    /**
     * Méthode qui prend en argument l'image correspondant
     * à la carte qu'on veut légender, dessine sur cette dernière
     * la légende correspondante, et retourne l'image finale
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param map
     *      L'image correspondant à la carte qu'on veut légender
     *
     * @return mapWithLegend
     *      L'image finale avec la légende, sous forme d'une
     *      BufferedImage
     */
    public BufferedImage generateMapWithLegend(BufferedImage map) throws Exception {
        if (!ENOUGH_SPACE) {
            return map;
        } else {
            int mapWidth = map.getWidth();
            int mapHeight = map.getHeight();
            int space = mapHeight / 40;

            File fontFile = new File(LegendGenerator.class.getClassLoader().getResource("imhof-font.ttf").toURI());

            BufferedImage mapWithLegend = new BufferedImage(mapWidth, mapHeight, TYPE_INT_RGB);
            BufferedImage legend = new BufferedImage(WIDTH, HEIGHT, TYPE_INT_ARGB);
            Graphics2D legendGraphics = legend.createGraphics();
            Graphics2D resultGraphics = mapWithLegend.createGraphics();
            legendGraphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            legendGraphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            legendGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            legendGraphics.setFont(Font.createFont(TRUETYPE_FONT, fontFile).deriveFont(PLAIN, FONT_SIZE));
            Path2D border = new Path2D.Double();
            border.moveTo(0, 0);
            border.lineTo(WIDTH, 0);
            border.lineTo(WIDTH, HEIGHT);
            border.lineTo(0, HEIGHT);
            border.closePath();
            legendGraphics.setColor(Color.rgb(1, 1, 0.9).convert());
            legendGraphics.fill(border);
            legendGraphics.setColor(DARK_BLUE);
            legendGraphics.setStroke(new BasicStroke((float)ELEMENT_HEIGHT / 5));
            legendGraphics.draw(border);
            int coupleCount = COUPLES.size();
            int deltaY = HEIGHT / (coupleCount + 1);
            int yPos = deltaY;
            for (LegendCouple couple : COUPLES) {
                drawElement(legendGraphics, yPos, couple.elements());
                legendGraphics.setColor(DARK_BLUE);
                legendGraphics.drawString(couple.message(), X_POS + 5 * ELEMENT_WIDTH / 4, yPos + (FONT_SIZE / 3));
                yPos += deltaY;
            }
            resultGraphics.drawImage(map, 0, 0, null);
            resultGraphics.drawImage(legend, mapWidth - space - WIDTH, mapHeight - space - HEIGHT, null);
            return mapWithLegend;
        }
    }

    private void drawElement(Graphics2D graphics, int yPos, List<LegendElement> elements) {
        if (elements.get(0) instanceof LineStyle) {
            for (LegendElement element : elements) {
                LineStyle style = (LineStyle)element;
                Path2D path = new Path2D.Double();
                path.moveTo(X_POS, yPos);
                path.lineTo(X_POS + ELEMENT_WIDTH, yPos);
                float[] dash = null;
                if (style.dash() != null) {
                    dash = new float[style.dash().length];
                    for (int k = 0 ; k < style.dash().length ; k++) {
                        dash[k] = style.dash()[k] * LINE_SCALE;
                    }
                }
                graphics.setColor(style.color().multiply(ADJUSTER).convert());
                graphics.setStroke(new BasicStroke(LINE_SCALE * style.width(), style.cap().ordinal(), style.join().ordinal(), 10.0f, dash, 0f));
                graphics.draw(path);
            }
        } else if (elements.get(0) instanceof Color) {
            Color color = (Color)elements.get(0);
            Path2D path = new Path2D.Double();
            path.moveTo(X_POS, yPos - (ELEMENT_HEIGHT / 2));
            path.lineTo(X_POS + ELEMENT_WIDTH, yPos - (ELEMENT_HEIGHT / 2));
            path.lineTo(X_POS + ELEMENT_WIDTH, yPos + (ELEMENT_HEIGHT / 2));
            path.lineTo(X_POS, yPos + (ELEMENT_HEIGHT / 2));
            path.closePath();
            graphics.setColor(color.multiply(ADJUSTER).convert());
            graphics.fill(path);
            graphics.setColor(DARK_BLUE);
            graphics.setStroke(new BasicStroke(ELEMENT_HEIGHT / 20));
            graphics.draw(path);
        }
    }
}
