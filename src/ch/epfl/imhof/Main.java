package ch.epfl.imhof;

import static java.lang.Math.*;
import static java.lang.Double.parseDouble;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.bonus.CompassAndScaleAdder;
import ch.epfl.imhof.bonus.GridGenerator;
import ch.epfl.imhof.bonus.LegendGenerator;
import ch.epfl.imhof.bonus.SwissPainter100_000;
import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.LineStyle;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe représentant le programme principal
 * (cette classe a été modifiée pour le rendu
 * BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Main {

    /**
     * Méthode statique qui constitue le programme principal
     * du projet. Elle dessine et sauvegarde une carte sous
     * forme d'une image du format png avec les étapes
     * suivantes :
     *
     *   1 - Le fichier OSM portant le nom passé en paramètre est lu
     *       afin de créer les entités OSM, des types définis dans ce
     *       projet, correspondants aux éléments de la carte décrite
     *       par le fichier lu
     *
     *   2 - Les entités OSM créées sont transformées en éléments
     *       géométriques, des types définis dans ce projet, et stockées
     *       en une "carte" (un objet du type Map qui sert à contenir
     *       les éléments qui seront utiles au dessin de la carte)
     *
     *   3 - Les dimensions de l'image dans laquelle la carte sera
     *       dessinée sont déterminés en fonction des deux points
     *       bas-gauche et haut-droite, dont les coordonnées
     *       sphériques sont passées en paramètre
     *
     *   4 - Une toile, du type défini dans ce projet, est créée avec
     *       les dimensions calculées
     *
     *   5 - Le peintre suisse est utilisé pour dessiner les éléments
     *       géométriques de la carte sur la toile créée
     *
     *   6 - Un générateur de reliefs ombrés colorés est créé avec les
     *       données passées en paramètre, et utilisé pour créer, sous
     *       forme d'image, un relief ombré flouté avec un rayon de
     *       floutage qui est déterminé en fonction de la résolution de
     *       l'image, passée en paramètre
     *
     *   7 - L'image de la carte et celle du relief ombré coloré sont
     *       combinées en une même image en effectuant le "produit" des
     *       couleurs pixel par pixel
     *
     *   8 - Le résultat de la combinaison des deux images, qui est une
     *       image, est sauvegardé sous la forme d'une image du format
     *       png
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param args
     *      La liste des paramètres à utiliser lors du dessin de la carte
     */
    public static void main(String[] args) throws Exception {
        Projection projection = new CH1903Projection();
        int scale = isInArgs(args, "1:100_000") ? 100_000 : 25_000;
        double left = parseDouble(args[2]);
        double bottom = parseDouble(args[3]);
        double right = parseDouble(args[4]);
        double top = parseDouble(args[5]);
        double blLat = toRadians(bottom);
        double trLat = toRadians(top);
        double res = (int)Double.parseDouble(args[6]);
        Point bl = projection.project(new PointGeo(toRadians(left), blLat));
        Point tr = projection.project(new PointGeo(toRadians(right), trLat));
        int h = (int)round(res * (trLat - blLat) * Earth.RADIUS / (0.0254 * scale));
        int w = (int)round((tr.x() - bl.x()) * h / (tr.y() - bl.y()));

        PaintingConfiguration paintingConfiguration = scale == 100_000 ? new SwissPainter100_000() : new SwissPainter();

        Java2DCanvas canvas = new Java2DCanvas(bl, tr, w, h, res, Color.WHITE);
        Painter painter = paintingConfiguration.painter();
        painter.drawMap(new OSMToGeoTransformer(projection).transform(OSMMapReader.readOSMFile(args[0], true)), canvas);
        BufferedImage image = canvas.image();
        BufferedImage relief = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        // Code BONUS!
        int lightSpecIndex = indexOfArgBeginningWithString(args, "lightfrom=");
        Vector3 lightSource = lightSpecIndex == -1 ? new Vector3(-1, 1, 1) : lightFrom(Integer.parseInt(args[lightSpecIndex].substring(10)));
        //
        if (!isInArgs(args, "no-relief")) {
            try (DigitalElevationModel model = new HGTDigitalElevationModel(new File(Main.class.getClassLoader().getResource(args[1]).toURI()))) {
                relief = new ReliefShader(projection, model, lightSource).shadedRelief(bl, tr, w, h, res * 1.7 / 25.4);
            }
            catch (IndexOutOfBoundsException exception) {
                System.out.println("Cannot generate map with the following parameters: " +
                    "the supplied coordinates cover an area that is outside the range of the input elevation (HGT) file.\n" +
                    "Specify different coordinates or add the 'no-relief' parameter to generate a map without elevation.");
                System.exit(1);
            }
        }
        BufferedImage map = new BufferedImage(w, h, image.getType());
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color imageColor = Color.rgb(image.getRGB(i, j));
                Color reliefColor = isInArgs(args, "no-relief") ? Color.WHITE : Color.rgb(relief.getRGB(i, j));
                Color color = !isInArgs(args, "no-relief") && isInArgs(args, "relief-only") ?
                    reliefColor : imageColor.multiply(reliefColor);
                int r = (int)(color.r() * 255.9999);
                int g = (int)(color.g() * 255.9999);
                int b = (int)(color.b() * 255.9999);
                map.setRGB(i, j, (r << 16) + (g << 8) + b);
            }
        }
        // Code BONUS!
        Graphics2D mapGraphics = map.createGraphics();
        int compassAndScaleLeftMargin = w / 20;
        int compassBottomMargin = 0;
        int compassFactor = ((w + h) / 14) / 32;
        int compassSize = compassFactor * 32;

        int gridSpecIndex = indexOfArgBeginningWithString(args, "grid=");
        if (gridSpecIndex != -1) {
            float factor = (float)res / 300;
            float[] dash = {factor * 24, factor * 16};
            String gridSpecValue = args[gridSpecIndex].substring(5);
            Color gridColor = "light".equals(gridSpecValue) ? Color.rgb(1, 1, 0.85) : Color.rgb(0, 0, 0.15);
            GridGenerator generator = new GridGenerator(new LineStyle(gridColor, LineStyle.LineCap.Round, LineStyle.LineJoin.Round, factor * 4, dash), scale == 100_000 ? 0.05 : 0.01);
            boolean drawStrings = ((h >= 800) && (w >= 800));
            mapGraphics.drawImage(generator.generateGridFor(w, h, bottom, top, left, right, drawStrings), 0, 0, null);
            compassAndScaleLeftMargin = generator.approximateStringWidth() + w / 30;
        }
        if (isInArgs(args, "scale")) {
            int cmInPixels = (int)(res / 2.54);
            double factor = (double)w / (28 * cmInPixels);
            int scaleWidth = 4 * (int)round(factor) * cmInPixels;
            if (factor > 0.5) {
                compassBottomMargin = scaleWidth / 35 + h / 26;
                map = CompassAndScaleAdder.mapWithScale(map, compassAndScaleLeftMargin + (compassSize - 7 * scaleWidth / 5) / 2, h / 26, cmInPixels, (int)round(factor), scaleWidth, res, scale);
            } else {
                System.out.println("Warning: Not drawing scale as space is limited.");
            }
        }
        if (isInArgs(args, "compass") && (compassFactor >= 8)) {
            map = CompassAndScaleAdder.mapWithCompass(map, compassAndScaleLeftMargin, h / 26 + compassBottomMargin, compassFactor, compassSize);
        }
        if (isInArgs(args, "legend")) {
            map = new LegendGenerator(w, h, res, paintingConfiguration).generateMapWithLegend(map);
        }
        //
        ImageIO.write(map, "png", new File(args[7]));
    }

    private static boolean isInArgs(String[] args, String s) {
        for (int i = 0 ; i < args.length ; i++) {
            if (s.equals(args[i])) {
                return true;
            }
        }
        return false;
    }

    private static int indexOfArgBeginningWithString(String[] args, String s) {
        int length = s.length();
        for (int i = 0 ; i < args.length ; i++) {
            String arg = args[i];
            if (arg.length() >= length) {
                for (int j = 0 ; j < length ; j++) {
                    if (arg.charAt(j) == s.charAt(j)) {
                        if (j == length - 1) {
                            return i;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return -1;
    }

    private static Vector3 lightFrom(int angle) {
        int trueAngle = angle < 0 ? (angle % 360) + 360 : angle % 360;
        double angleInRadians = toRadians(trueAngle);
        return new Vector3(-sqrt(2) * sin(angleInRadians), sqrt(2) * cos(angleInRadians), 1);
    }
}
