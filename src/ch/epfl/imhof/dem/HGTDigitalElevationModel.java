package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Classe représentant un modèle numérique du terrain
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {
    private static final Set<Character> NS = new HashSet<Character>(Arrays.asList('N', 'S'));
    private static final Set<Character> WE = new HashSet<Character>(Arrays.asList('W', 'E'));
    private final FileInputStream STREAM;
    private final ShortBuffer BUFFER;
    private final int RES;
    private final double LON, LAT;
    
    /**
     * Construit un modèle numérique du terrain à partir d'un
     * fichier du format hgt passé en paramètre
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @throws IllegalArgumentException
     *      Si le nom du fichier passé en paramètre n'est
     *      pas valide
     * @throws FileNotFoundException
     *      Si le fichier passé en paramètre n'est pas
     *      trouvé
     * @throws IOException
     *      S'il y a une erreur d'entrée/sortie
     * 
     * @param dgm
     *      Le fichier à partir duquel on veut construire
     *      le modèle numérique du terrain
     */
    public HGTDigitalElevationModel(File dgm) throws FileNotFoundException, IOException {
        String name = dgm.getName();
        long l = dgm.length();
        double sqrt = Math.sqrt(l / 2);
        if ((name.length() != 11) || !NS.contains(name.charAt(0)) || !WE.contains(name.charAt(3)) || !name.substring(7, 11).equals(".hgt") || ((double)((int)sqrt) != sqrt)) {
            throw new IllegalArgumentException("Invalid DGM file name!");
        } else {
            if (name.charAt(0) == 'S') {
                LAT = Math.toRadians(- Integer.parseInt(name.substring(1, 3)));
            } else {
                LAT = Math.toRadians(Integer.parseInt(name.substring(1, 3)));
            }
            if (name.charAt(3) == 'W') {
                LON = Math.toRadians(- Integer.parseInt(name.substring(4, 7)));
            } else {
                LON = Math.toRadians(Integer.parseInt(name.substring(4, 7)));
            }
            STREAM = new FileInputStream(dgm);
            BUFFER = STREAM.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
            RES = (int)sqrt;
        }
    }
    
    /**
     * Méthode qui retourne une approximation du vecteur normal
     * à la terre en le point (en coordonnées sphériques) passé
     * en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param point
     *      Le point où on cherche une approximation du vecteur
     *      normal à la terre
     */
    @Override
    public Vector3 normalAt(PointGeo point) {
        double delta = Math.toRadians(1d / (RES - 1));
        double s = Earth.RADIUS * delta;
        int i = (int)((point.longitude() - LON) / delta);
        int j = 3600 - (int)((point.latitude() - LAT) / delta);
        double deltaA = BUFFER.get(j * RES + i + 1) - BUFFER.get(j * RES + i);
        double deltaB = BUFFER.get((j - 1) * RES + i) - BUFFER.get(j * RES + i);
        double deltaC = BUFFER.get((j - 1) * RES + i) - BUFFER.get((j - 1) * RES + i + 1);
        double deltaD = BUFFER.get(j * RES + i + 1) - BUFFER.get((j - 1) * RES + i + 1);
        return new Vector3(s * (deltaC - deltaA) / 2, s * (deltaD - deltaB) / 2, Math.pow(s, 2));
    }
    
    /**
     * Méthode qui ferme le flot correspondant au fichier
     * du modèle numérique du terrain
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @throws IOException
     *      S'il y a une erreur d'entrée/sortie
     */
    @Override
    public void close() throws IOException {
        STREAM.close();
    }
}
