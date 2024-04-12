package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

import static ch.epfl.imhof.painting.Painter.*;

/**
 * Classe représenant un générateur de peintres
 * de routes à partir de spécifications de routes
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class RoadPainterGenerator {
    private static final LineCap CAP_ROUND = LineCap.Round;
    private static final LineCap CAP_BUTT = LineCap.Butt;
    private static final LineJoin JOIN_ROUND = LineJoin.Round;
    
    private RoadPainterGenerator() {}
    
    /**
     * Méthode statique qui retourne un peintre de routes
     * qui dessine ces derniers en fonction des spécifications
     * passées en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param specs
     *      La liste des spécifications de routes
     * 
     * @return bridgeInner.above(bridgeOuter.above(roadInner.above(roadOuter.above(tunnel))))
     *      Le peintre construit selon les spécifications
     */
    public static Painter painterForRoads(RoadSpec... specs) {
        RoadSpec first = specs[0];
        Color ci = first.innerColor();
        float wi = first.innerWidth();
        Color cc = first.outerColor();
        float wc = first.outerWidth();
        Predicate<Attributed<?>> pr = first.predicate();
        Predicate<Attributed<?>> bridgeFilter = Filters.tagged("bridge");
        Predicate<Attributed<?>> TunnelFilter = Filters.tagged("tunnel");
        Predicate<Attributed<?>> isBridge = pr.and(bridgeFilter);
        Predicate<Attributed<?>> isRoad = pr.and(bridgeFilter.negate().and(TunnelFilter.negate()));
        Predicate<Attributed<?>> isTunnel = pr.and(TunnelFilter);
        Painter bridgeInner = line(wi, ci, CAP_ROUND, JOIN_ROUND).when(isBridge);
        Painter bridgeOuter = line(wi + 2 * wc, cc, CAP_BUTT, JOIN_ROUND).when(isBridge);
        Painter roadInner = line(wi, ci, CAP_ROUND, JOIN_ROUND).when(isRoad);
        Painter roadOuter = line(wi + 2 * wc, cc, CAP_ROUND, JOIN_ROUND).when(isRoad);
        Painter tunnel = line(wi / 2, cc, CAP_BUTT, JOIN_ROUND, 2 * wi, 2 * wi).when(isTunnel);
        for (int i = 1 ; i < specs.length ; i++) {
            ci = specs[i].innerColor();
            wi = specs[i].innerWidth();
            cc = specs[i].outerColor();
            wc = specs[i].outerWidth();
            pr = specs[i].predicate();
            isBridge = pr.and(bridgeFilter);
            isRoad = pr.and(bridgeFilter.negate().and(TunnelFilter.negate()));
            isTunnel = pr.and(TunnelFilter);
            bridgeInner = bridgeInner.above(line(wi, ci, CAP_ROUND, JOIN_ROUND).when(isBridge));
            bridgeOuter = bridgeOuter.above(line(wi + 2 * wc, cc, CAP_BUTT, JOIN_ROUND).when(isBridge));
            roadInner = roadInner.above(line(wi, ci, CAP_ROUND, JOIN_ROUND).when(isRoad));
            roadOuter = roadOuter.above(line(wi + 2 * wc, cc, CAP_ROUND, JOIN_ROUND).when(isRoad));
            tunnel = tunnel.above(line(wi / 2, cc, CAP_BUTT, JOIN_ROUND, 2 * wi, 2 * wi).when(isTunnel));
        }
        return bridgeInner.above(bridgeOuter.above(roadInner.above(roadOuter.above(tunnel))));
    }
    
    /**
     * Classe imbriquée qui représente une spécification
     * de routes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static class RoadSpec {
        private final Predicate<Attributed<?>> PREDICATE;
        private final float INNER_WIDTH;
        private final Color INNER_COLOR;
        private final float OUTER_WIDTH;
        private final Color OUTER_COLOR;
        
        /**
         * Construit une spécification de routes
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param predicate
         *      Le prédicat qui sert à filtrer les routes concernées par
         *      cette spécification
         * @param innerWidth
         *      L'épaisseur de l'intérieur de la route
         * @param innerColor
         *      La couleur de l'intérieur de la route
         * @param outerWidth
         *      L'épaisseur de la bordure de la route
         * @param outerColor
         *      La couleur de la bordure de la route
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float innerWidth, Color innerColor, float outerWidth, Color outerColor) {
            PREDICATE = predicate;
            INNER_WIDTH = innerWidth;
            INNER_COLOR = innerColor;
            OUTER_WIDTH = outerWidth;
            OUTER_COLOR = outerColor;
        }
        
        /**
         * Getter du prédicat de la spécification
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return PREDICATE
         *      Le prédicat de la spécification
         */
        public Predicate<Attributed<?>> predicate() {
            return PREDICATE;
        }
        
        /**
         * Getter de l'épaisseur interne de la spécification
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return INNER_WIDTH
         *      L'épaisseur interne de la spécification
         */
        public float innerWidth() {
            return INNER_WIDTH;
        }
        
        /**
         * Getter de la couleur interne de la spécification
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return INNER_COLOR
         *      La couleur interne de la spécification
         */
        public Color innerColor() {
            return INNER_COLOR;
        }
        
        /**
         * Getter de l'épaisseur de bordure de la spécification
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return OUTER_WIDTH
         *      L'épaisseur de bordure de la spécification
         */
        public float outerWidth() {
            return OUTER_WIDTH;
        }
        
        /**
         * Getter de la couleur de bordure de la spécification
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return OUTER_COLOR
         *      La couleur de bordure de la spécification
         */
        public Color outerColor() {
            return OUTER_COLOR;
        }
    }
}
