package ch.epfl.imhof.bonus;

import static ch.epfl.imhof.painting.Color.gray;
import static ch.epfl.imhof.painting.Color.rgb;
import static ch.epfl.imhof.painting.Filters.tagged;
import static ch.epfl.imhof.painting.Painter.line;
import static ch.epfl.imhof.painting.Painter.outline;
import static ch.epfl.imhof.painting.Painter.polygon;

import java.util.Arrays;
import java.util.List;

import ch.epfl.imhof.PaintingConfiguration;
import ch.epfl.imhof.bonus.LegendCouple;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.RoadPainterGenerator;
import ch.epfl.imhof.painting.RoadPainterGenerator.RoadSpec;

/**
 * Classe qui représente une autre variante
 * du peintre suisse utilisé dans ce projet,
 * adaptée pour le dessin de cartes à l'échelle
 * 1:100'000
 * (cette classe fait partie du code BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class SwissPainter100_000 implements PaintingConfiguration {
    private static final Painter PAINTER;

    static Color black = Color.BLACK;
    static Color darkGray = gray(0.2);
    static Color darkGreen = rgb(0.75, 0.85, 0.7);
    static Color darkRed = rgb(0.7, 0.15, 0.15);
    static Color darkBlue = rgb(0.45, 0.7, 0.8);
    static Color lightGreen = rgb(0.85, 0.9, 0.85);
    static Color lightGray = gray(0.9);
    static Color orange = rgb(1, 0.75, 0.2);
    static Color lightYellow = rgb(1, 1, 0.5);
    static Color lightRed = rgb(0.95, 0.7, 0.6);
    static Color lightBlue = rgb(0.8, 0.9, 0.95);
    static Color white = Color.WHITE;

    // Couleurs supplémentaires
    static Color orangeBorder = rgb(0.85, 0.6, 0.05);
    static Color yellowBorder = rgb(0.85, 0.85, 0.35);
    static Color lightRedBorder = rgb(0.8, 0.55, 0.45);
    static Color whiteBorder = gray(0.8);
    //

    static RoadSpec motorway = new RoadSpec(tagged("highway", "motorway", "trunk"), 2f, orange, 0.3f, orangeBorder);
    static RoadSpec primary = new RoadSpec(tagged("highway", "primary"), 1.7f, lightRed, 0.21f, lightRedBorder);
    static RoadSpec motorwayLink = new RoadSpec(tagged("highway", "motorway_link", "trunk_link"), 1.7f, orange, 0.21f, orangeBorder);
    static RoadSpec secondary = new RoadSpec(tagged("highway", "secondary"), 1.7f, lightYellow, 0.21f, yellowBorder);
    static RoadSpec primaryLink = new RoadSpec(tagged("highway", "primary_link"), 1.7f, lightRed, 0.21f, lightRedBorder);
    static RoadSpec tertiary = new RoadSpec(tagged("highway", "tertiary"), 1.7f, white, 0.21f, whiteBorder);
    static RoadSpec secondaryLink = new RoadSpec(tagged("highway", "secondary_link"), 1.7f, lightYellow, 0.21f, yellowBorder);

    static {
        Painter roadPainter = RoadPainterGenerator.painterForRoads(motorway, primary, motorwayLink, secondary, primaryLink, tertiary, secondaryLink);

        Painter fgPainter =
                roadPainter
                .above(line(0.6f, darkRed).when(tagged("railway", "rail", "turntable")))
                .above(polygon(lightGreen).when(tagged("leisure", "pitch")))
                .above(line(0.5f, darkGray).when(tagged("man_made", "pier")))
                .layered();

        Painter bgPainter =
                outline(0.4f, darkBlue).above(polygon(lightBlue)).when(tagged("natural", "water").or(tagged("waterway", "riverbank")))
                .above(line(0.8f, lightBlue).above(line(1.2f, darkBlue)).when(tagged("waterway", "river", "canal")))
                .above(line(0.5f, darkBlue).when(tagged("waterway", "stream")))
                .above(polygon(darkGreen).when(tagged("natural", "wood").or(tagged("landuse", "forest"))))
                .above(polygon(lightGreen).when(tagged("landuse", "grass", "recreation_ground", "meadow", "cemetery").or(tagged("leisure", "park"))))
                .above(polygon(lightGray).when(tagged("landuse", "residential", "industrial")))
                .layered();

        PAINTER = fgPainter.above(bgPainter);
    }

    /**
     * Getter du peintre suisse
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return PAINTER
     *      Le peintre suisse
     */
    @Override
    public Painter painter() {
        return PAINTER;
    }

    /**
     * Getter de la liste des couples de légende, utile
     * pour la création de légendes pour les cartes
     * utilisant ce peintre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return Arrays.asList(-------)
     *      La liste des couples de légende
     */
    @Override
    public List<LegendCouple> legendCouples() {
        float[] dash = {1f, 2f};
        float motorwayInner = motorway.innerWidth();
        float primaryInner = primary.innerWidth();
        float secondaryInner = secondary.innerWidth();
        float tertiaryInner = tertiary.innerWidth();
        return Arrays.asList(
                new LegendCouple("Motorway", new LineStyle(motorwayInner + 2 * motorway.outerWidth(), motorway.outerColor()), new LineStyle(motorwayInner, motorway.innerColor())),
                new LegendCouple("Primary road", new LineStyle(primaryInner + 2 * primary.outerWidth(), primary.outerColor()), new LineStyle(primaryInner, primary.innerColor())),
                new LegendCouple("Secondary road", new LineStyle(secondaryInner + 2 * secondary.outerWidth(), secondary.outerColor()), new LineStyle(secondaryInner, secondary.innerColor())),
                new LegendCouple("Tertiary road", new LineStyle(tertiaryInner + 2 * tertiary.outerWidth(), tertiary.outerColor()), new LineStyle(tertiaryInner, tertiary.innerColor())),
                new LegendCouple("Footway / Cycleway", new LineStyle(darkGray, LineCap.Round, LineJoin.Miter, 0.5f, dash)),
                new LegendCouple("Railway / Subway", new LineStyle(0.7f, darkRed)),
                new LegendCouple("Pier", new LineStyle(1, darkGray)),
                new LegendCouple("River", new LineStyle(1.5f, darkBlue), new LineStyle(1, lightBlue)),
                new LegendCouple("Stream", new LineStyle(1, darkBlue)),
                new LegendCouple("Building", darkGray),
                new LegendCouple("Water", lightBlue),
                new LegendCouple("Pitch", lightGreen),
                new LegendCouple("Forest", darkGreen),
                new LegendCouple("Meadow / Park / Cemetery", lightGreen),
                new LegendCouple("Residential / Industrial area", lightGray)
        );
    }

}
