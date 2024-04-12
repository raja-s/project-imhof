package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un polygone constitué d'une enveloppe
 * contenant un ensemble de polylignes fermées
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Polygon {
    private final ClosedPolyLine SHELL;
    private final List<ClosedPolyLine> HOLES;
    
    /**
     * Construit un polygone à partir d'une enveloppe (polyligne fermée)
     * et d'une liste de polylignes fermées
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param shell
     *      la polyligne fermée qui va jouer le rôle d'enveloppe
     * @param holes
     *      la liste des polylignes fermées à partir de laquelle
     *      on voudrait créer l'ensemble des polylignes fermées
     *      contenues dans l'enveloppe
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        SHELL = shell;
        HOLES = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }
    
    /**
     * Construit un polygone qui ne contient pas de polylignes
     * fermées, à partir d'une enveloppe (polyligne fermée)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param shell
     *      la polyligne fermée qui va jouer le rôle d'enveloppe
     */
    public Polygon(ClosedPolyLine shell) {
        this(shell, Collections.emptyList());
    }
    
    /**
     * Getter de l'enveloppe
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return SHELL
     *      retourne l'enveloppe
     */
    public ClosedPolyLine shell() {
        return SHELL;
    }
    
    /**
     * Getter de la liste des polylignes fermées
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return HOLES
     *      retourne la liste des polylignes fermées
     */
    public List<ClosedPolyLine> holes() {
        return HOLES;
    }
}
