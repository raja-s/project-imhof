package ch.epfl.imhof;

import static ch.epfl.imhof.painting.Filters.tagged;
import static ch.epfl.imhof.painting.Painter.*;

import java.util.Arrays;
import java.util.List;

import ch.epfl.imhof.bonus.LegendCouple;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.RoadPainterGenerator;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.painting.RoadPainterGenerator.RoadSpec;

/**
 * Classe qui représente un exemple de peintre modifié.
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public class ExampleCustomPainter implements PaintingConfiguration {

    //ROADS
    private static final Color HIGHWAY_ORANGE = Color.rgb(0.98, 0.62, 0.15);
    private static final Color PRIMARY_YELLOW = Color.rgb(1, 0.85, 0.2);
    private static final Color SECONDARY_YELLOW = Color.rgb(0.97, 0.9, 0.73);
    private static final Color TERTIARY_WHITE = Color.WHITE;
    //OTHER LINES
	private static final Color RAIL_RED = Color.rgb(0.9, 0.2, 0.2);
    private static final Color FOOTWAY_GRAY = Color.gray(0.65);
    private static final Color PIER_BROWN = Color.rgb(0.55, 0.4, 0.1);
	//AREAS
	private static final Color BUILDING_GRAY = Color.gray(0.5);
    private static final Color MEADOW_GREEN = Color.rgb(0.84, 0.97, 0.81);
    private static final Color FOREST_GREEN = Color.rgb(0.78, 0.91, 0.75);
    private static final Color WATER_BLUE = Color.rgb(0.4, 0.85, 1);
    private static final Color SWIMMING_POOL_BLUE = Color.rgb(0.8, 0.9, 0.95);
    //OUTLINES
	private static final Color OUTLINE_ORANGE = Color.rgb(0.85, 0.5, 0.05);
	private static final Color OUTLINE_DARK_YELLOW = Color.rgb(0.9, 0.75, 0.1);
	private static final Color OUTLINE_LIGHT_YELLOW = Color.rgb(0.85, 0.8, 0.6);
	private static final Color OUTLINE_WHITE = Color.gray(0.8);
    private static final Color OUTLINE_BLUE = Color.rgb(0, 0.6, 0.9);
	//

    static RoadSpec motorway = new RoadSpec(tagged("highway", "motorway", "trunk"), 2f, HIGHWAY_ORANGE, 0.4f, OUTLINE_ORANGE);
    static RoadSpec primary = new RoadSpec(tagged("highway", "primary"), 1.7f, PRIMARY_YELLOW, 0.27f, OUTLINE_DARK_YELLOW);
    static RoadSpec motorwayLink = new RoadSpec(tagged("highway", "motorway_link", "trunk_link"), 1.7f, HIGHWAY_ORANGE, 0.27f, OUTLINE_ORANGE);
    static RoadSpec secondary = new RoadSpec(tagged("highway", "secondary"), 1.7f, SECONDARY_YELLOW, 0.27f, OUTLINE_LIGHT_YELLOW);
    static RoadSpec primaryLink = new RoadSpec(tagged("highway", "primary_link"), 1.7f, PRIMARY_YELLOW, 0.27f, OUTLINE_DARK_YELLOW);
    static RoadSpec tertiary = new RoadSpec(tagged("highway", "tertiary"), 1.7f, TERTIARY_WHITE, 0.2f, OUTLINE_WHITE);
    static RoadSpec secondaryLink = new RoadSpec(tagged("highway", "secondary_link"), 1.7f, SECONDARY_YELLOW, 0.27f, OUTLINE_LIGHT_YELLOW);
    static RoadSpec residential = new RoadSpec(tagged("highway", "residential", "living_street", "unclassified"), 1.2f, TERTIARY_WHITE, 0.2f, OUTLINE_WHITE);
    static RoadSpec service = new RoadSpec(tagged("highway", "service", "pedestrian"), 0.5f, TERTIARY_WHITE, 0.07f, OUTLINE_WHITE);

    private static final Painter ROADS = RoadPainterGenerator.painterForRoads(motorway, primary, motorwayLink, secondary, primaryLink, tertiary, secondaryLink, residential, service);

    private static final Painter FOREGROUND =
        polygon(BUILDING_GRAY).when(tagged("building"))
        .above(ROADS)
        .above(line(0.4f, FOOTWAY_GRAY, LineCap.Round, LineJoin.Round, 0.6f, 1.2f).when(tagged("highway", "footway", "steps", "path", "track", "cycleway")))
        .above(polygon(SWIMMING_POOL_BLUE).when(tagged("leisure", "swimming_pool")))
        .above(line(0.6f, RAIL_RED, LineCap.Round, LineJoin.Round, 1f, 0.8f).when(tagged("railway", "rail", "turntable")))
        .above(line(0.3f, RAIL_RED, LineCap.Round, LineJoin.Round, 0.8f, 0.64f).when(tagged("railway", "subway", "narrow_gauge", "light_rail")))
        .above(polygon(MEADOW_GREEN).when(tagged("leisure", "pitch")))
        .above(line(1, PIER_BROWN).when(tagged("man_made", "pier")))
		.above(line(0.7f, OUTLINE_BLUE, LineCap.Round, LineJoin.Round, 0.8f, 1.6f).when(tagged("route", "ferry")))
        .layered();

    private static final Painter BACKGROUND =
        outline(0.25f, OUTLINE_BLUE).above(polygon(WATER_BLUE)).when(tagged("natural", "water").or(tagged("waterway", "riverbank")))
        .above(line(1, WATER_BLUE)/*.above(line(1.5f, OUTLINE_BLUE))*/.when(tagged("waterway", "river", "canal")))
        .above(line(1, WATER_BLUE)/*.above(line(1.5f, OUTLINE_BLUE))*/.when(tagged("waterway", "stream")))
        .above(polygon(FOREST_GREEN).when(tagged("natural", "wood").or(tagged("landuse", "forest"))))
        .above(polygon(MEADOW_GREEN).when(tagged("landuse", "grass", "recreation_ground", "meadow", "cemetery").or(tagged("leisure", "park"))))
        .above(polygon(Color.WHITE).when(tagged("landuse", "residential", "industrial")))
        .layered();

    public static final Painter PAINTER = FOREGROUND.above(BACKGROUND);

    @Override
    public Painter painter() {
        return PAINTER;
    }

    @Override
    public List<LegendCouple> legendCouples() {
        float[] footwayDash = {1f, 2f};
        float[] railwayDash = {1.4f, 1.12f};
        float[] ferryDash = {1.2f, 2.4f};
        float motorwayInner = motorway.innerWidth();
        float primaryInner = primary.innerWidth();
        float secondaryInner = secondary.innerWidth();
        float tertiaryInner = tertiary.innerWidth();
        float residentialInner = residential.innerWidth();
        float serviceInner = service.innerWidth();
        return Arrays.asList(
                new LegendCouple("Motorway", new LineStyle(motorwayInner + 2 * motorway.outerWidth(), motorway.outerColor()), new LineStyle(motorwayInner, motorway.innerColor())),
                new LegendCouple("Primary road", new LineStyle(primaryInner + 2 * primary.outerWidth(), primary.outerColor()), new LineStyle(primaryInner, primary.innerColor())),
                new LegendCouple("Secondary road", new LineStyle(secondaryInner + 2 * secondary.outerWidth(), secondary.outerColor()), new LineStyle(secondaryInner, secondary.innerColor())),
                new LegendCouple("Tertiary road", new LineStyle(tertiaryInner + 2 * tertiary.outerWidth(), tertiary.outerColor()), new LineStyle(tertiaryInner, tertiary.innerColor())),
                new LegendCouple("Residential street", new LineStyle(residentialInner + 2 * residential.outerWidth(), residential.outerColor()), new LineStyle(residentialInner, residential.innerColor())),
                new LegendCouple("Pedestrian street", new LineStyle(serviceInner + 2 * service.outerWidth(), service.outerColor()), new LineStyle(serviceInner, service.innerColor())),
                new LegendCouple("Footway / Cycleway", new LineStyle(FOOTWAY_GRAY, LineCap.Round, LineJoin.Round, 0.5f, footwayDash)),
                new LegendCouple("Railway / Subway", new LineStyle(RAIL_RED, LineCap.Round, LineJoin.Round, 0.7f, railwayDash)),
                new LegendCouple("Ferry route", new LineStyle(OUTLINE_BLUE, LineCap.Round, LineJoin.Round, 0.7f, ferryDash)),
                new LegendCouple("Pier", new LineStyle(1, PIER_BROWN)),
                new LegendCouple("River / stream", new LineStyle(1, WATER_BLUE)),
                new LegendCouple("Building", BUILDING_GRAY),
                new LegendCouple("Water", WATER_BLUE),
                new LegendCouple("Pitch", MEADOW_GREEN),
                new LegendCouple("Forest", FOREST_GREEN),
                new LegendCouple("Meadow / Park / Cemetery", MEADOW_GREEN),
                new LegendCouple("Residential / Industrial area", Color.WHITE)
        );
    }
}
