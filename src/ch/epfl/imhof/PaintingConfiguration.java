package ch.epfl.imhof;

import ch.epfl.imhof.bonus.LegendCouple;
import ch.epfl.imhof.painting.Painter;

import java.util.List;

/**
 * Classe qui représente une configuration de peintre.
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface PaintingConfiguration {

	Painter painter();

	List<LegendCouple> legendCouples();

}
