package ch.epfl.imhof.bonus;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;

import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.PLAIN;

public final class CompassAndScaleAdder {
    private static final Color DARK_BLUE = Color.rgb(0, 0, 0.15);
    private static final java.awt.Color LIGHT_YELLOW = Color.rgb(1, 1, 0.85).convert();


    private CompassAndScaleAdder() {}

    public final static BufferedImage mapWithCompass(BufferedImage map, int deltaX, int deltaY, int factor, int size) throws Exception {
        int width = map.getWidth();
        int height = map.getHeight();
        float fontSize = 3.2f;

        File fontFile = new File(CompassAndScaleAdder.class.getClassLoader().getResource("imhof-font.ttf").toURI());

        BufferedImage mapWithCompass = new BufferedImage(width, height, TYPE_INT_RGB);
        BufferedImage compass = new BufferedImage(size, size, TYPE_INT_ARGB);
        Graphics2D compassGraphics = compass.createGraphics();
        Graphics2D mapGraphics = mapWithCompass.createGraphics();
        compassGraphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        compassGraphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        compassGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        compassGraphics.scale(factor, factor);
        compassGraphics.setFont(Font.createFont(TRUETYPE_FONT, fontFile).deriveFont(PLAIN, fontSize));
        compassGraphics.setColor(DARK_BLUE.convert());
        LineStyle circle = new LineStyle(DARK_BLUE, LineCap.Round, LineJoin.Round, 0.4f, new float[0]);
        compassGraphics.setStroke(new BasicStroke(circle.width(), circle.cap().ordinal(), circle.join().ordinal(), 10.0f, circle.dash(), 0f));

        //dark shapes
        compassGraphics.drawOval(9, 9, 14, 14);
        Path2D path = new Path2D.Double();
        path.moveTo(15, 16);
        path.lineTo(10, 10);
        path.lineTo(16, 15);
        path.lineTo(22, 10);
        path.lineTo(17, 16);
        path.lineTo(22, 22);
        path.lineTo(16, 17);
        path.lineTo(10, 22);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(16, 4);
        path.lineTo(18, 14);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(28, 16);
        path.lineTo(18, 18);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(16, 28);
        path.lineTo(14, 18);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(4, 16);
        path.lineTo(14, 14);
        path.closePath();
        compassGraphics.fill(path);

        //light shapes
        compassGraphics.setColor(LIGHT_YELLOW);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(18, 14);
        path.lineTo(28, 16);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(18, 18);
        path.lineTo(16, 28);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(14, 18);
        path.lineTo(4, 16);
        path.closePath();
        compassGraphics.fill(path);
        path = new Path2D.Double();
        path.moveTo(16, 16);
        path.lineTo(14, 14);
        path.lineTo(16, 4);
        path.closePath();
        compassGraphics.fill(path);

        //stroke
        compassGraphics.setColor(DARK_BLUE.convert());
        path = new Path2D.Double();
        path.moveTo(16, 4);
        path.lineTo(18, 14);
        path.lineTo(28, 16);
        path.lineTo(18, 18);
        path.lineTo(16, 28);
        path.lineTo(14, 18);
        path.lineTo(4, 16);
        path.lineTo(14, 14);
        path.closePath();
        compassGraphics.setStroke(new BasicStroke(circle.width() / 4, circle.cap().ordinal(), circle.join().ordinal(), 10.0f, circle.dash(), 0f));
        compassGraphics.draw(path);

        //text
        float weYPos = 16 + 0.35f * fontSize;

        compassGraphics.drawString("N", 16 - 35 * fontSize / 96, 3.2f);
        compassGraphics.drawString("S", 16 - 7 * fontSize / 32, 31f);
        compassGraphics.drawString("W", 0.15f, weYPos);
        compassGraphics.drawString("E", 28.8f, weYPos);

        mapGraphics.drawImage(map, 0, 0, null);
        mapGraphics.drawImage(compass, deltaX, height - deltaY - compass.getHeight(), null);
        return mapWithCompass;
    }

    public final static BufferedImage mapWithScale(BufferedImage map, int deltaX, int deltaY, int cmInPixels, int factor, int scaleWidth, double res, int scale) throws Exception {
        int width = map.getWidth();
        int height = map.getHeight();
        BufferedImage mapWithScale = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics2D m = mapWithScale.createGraphics();

        BufferedImage scaleImage = new BufferedImage(7 * scaleWidth / 5, 7 * scaleWidth / 5, TYPE_INT_ARGB);

        File fontFile = new File(CompassAndScaleAdder.class.getClassLoader().getResource("imhof-font.ttf").toURI());

        Graphics2D s = scaleImage.createGraphics();
        s.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        s.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        s.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        s.setFont(Font.createFont(TRUETYPE_FONT, fontFile).deriveFont(PLAIN, scaleWidth / 16));
        FontMetrics metrics = s.getFontMetrics();
        int fontHeight = metrics.getHeight();

        scaleImage = scaleImage.getSubimage(0, 0, scaleImage.getWidth(), fontHeight + scaleWidth / 16 + 3 * fontHeight / 5);

        s.setColor(LIGHT_YELLOW);
        s.fillRect(9 * scaleWidth / 20,  7 * fontHeight / 5, factor * cmInPixels, scaleWidth / 16);
        s.fillRect(19 * scaleWidth / 20,  7 * fontHeight / 5, factor * cmInPixels, scaleWidth / 16);
        s.setColor(DARK_BLUE.convert());
        s.fillRect(scaleWidth / 5,  7 * fontHeight / 5, factor * cmInPixels, scaleWidth / 16);
        s.fillRect(7 * scaleWidth / 10,  7 * fontHeight / 5, factor * cmInPixels, scaleWidth / 16);
        LineStyle style = new LineStyle(DARK_BLUE, LineCap.Butt, LineJoin.Miter, scaleImage.getHeight() / 25, new float[0]);
        s.setStroke(new BasicStroke(style.width(), style.cap().ordinal(), style.join().ordinal(), 10.0f, style.dash(), 0f));
        s.drawRect(scaleWidth / 5,  7 * fontHeight / 5, scaleWidth, scaleWidth / 16);

        double division = factor * scale / 100_000d;
        float textY = 6 * fontHeight / 5;

        for (int i = 0; i <= 4; i++) {
            String string = value(division, i);
            s.drawString(string + (i != 4 ? "" : " km"), scaleWidth / 5 + i * factor * cmInPixels - metrics.stringWidth(string) / 2, textY);
        }
        m.drawImage(map, 0, 0, null);
        m.drawImage(scaleImage, deltaX, height - deltaY - scaleImage.getHeight(), null);
        return mapWithScale;
    }

    private static String value(double d, int factor) {
        double product = d * factor;
        return (double)(int)product == product ? "" + (int)product : "" + product;
    }
}
