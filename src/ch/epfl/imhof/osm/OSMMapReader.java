package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

/**
 * Classe représenant un lecteur de fichiers OSM
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class OSMMapReader {
    private static final String NODE = "node";
    private static final String WAY = "way";
    private static final String RELATION = "relation";
    private static final String ROLE = "role";
    private static final String REF = "ref";
    private static final String ID = "id";
    private static final String K = "k";
    private static final String V = "v";

    private OSMMapReader() {}

    /**
     * Méthode qui lit le fichier OSM ayant le nom passé
     * en paramètre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param fileName
     *      Le nom du fichier à lire
     * @param unGZip
     *      Valeur booléenne qui détermine si le fichier est
     *      compressé, et doit donc être décompressé avant
     *      d'être lu, ou pas
     *
     * @throws SAXException
     *      si une erreur est rencontrée dans le format
     *      du fichier XML contenant la carte
     * @throws IOException
     *      si une erreur d'entrée/sortie est rencontrée
     *
     * @return map.build()
     *      une version finale et immuable d'une OSMMap
     *      à partir de la OSMMap bâtie
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException {
        OSMMap.Builder map = new OSMMap.Builder();
        InputStream stream;
        if (unGZip) {
            stream = new GZIPInputStream(OSMMapReader.class.getClassLoader().getResourceAsStream(fileName));
        } else {
            stream = OSMMapReader.class.getClassLoader().getResourceAsStream(fileName);
        }
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(new DefaultHandler() {
            private OSMNode.Builder node = null;
            private OSMWay.Builder way = null;
            private OSMRelation.Builder relation = null;

            /**
             * Méthode qui redéfinit la méthode startElement
             * de la superclasse DefaultHandler et qui effectue
             * les opérations nécessaires quand une balise ouvrante
             * d'un élément est rencontrée
             *
             * @author Andrey Batasov (250149)
             * @author Raja Soufi (247680)
             *
             * @param uri
             * @param lName
             * @param qName
             *      le nom de l'élément ouvert
             * @param atts
             *      les attributs de l'élément ouvert
             *
             * @throws SAXException
             *      si une erreur est rencontrée dans le format
             *      du fichier XML contenant la carte
             */
            @Override
            public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                switch (qName) {
                    case NODE:
                        double longitude = Math.toRadians(parseDouble(atts.getValue("lon")));
                        double latitude = Math.toRadians(parseDouble(atts.getValue("lat")));
                        node = new OSMNode.Builder(parseLong(atts.getValue(ID)), new PointGeo(longitude, latitude));
                        break;
                    case WAY:
                        way = new OSMWay.Builder(parseLong(atts.getValue(ID)));
                        break;
                    case "nd":
                        OSMNode nd = map.nodeForId(parseLong(atts.getValue(REF)));
                        if (nd == null) {
                            way.setIncomplete();
                        } else {
                            way.addNode(nd);
                        }
                        break;
                    case RELATION:
                        relation = new OSMRelation.Builder(parseLong(atts.getValue(ID)));
                        break;
                    case "member":
                        switch (atts.getValue("type")) {
                            case NODE:
                                OSMNode n = map.nodeForId(parseLong(atts.getValue(REF)));
                                if (n == null) {
                                    relation.setIncomplete();
                                } else {
                                    relation.addMember(Type.NODE, atts.getValue(ROLE), n);
                                }
                                break;
                            case WAY:
                                OSMWay w = map.wayForId(parseLong(atts.getValue(REF)));
                                if (w == null) {
                                    relation.setIncomplete();
                                } else {
                                    relation.addMember(Type.WAY, atts.getValue(ROLE), w);
                                }
                                break;
                            case RELATION:
                                OSMRelation r = map.relationForId(parseLong(atts.getValue(REF)));
                                if (r == null) {
                                    relation.setIncomplete();
                                } else {
                                    relation.addMember(Type.RELATION, atts.getValue(ROLE), r);
                                }
                                break;
                            default: break;
                        }
                        break;
                    case "tag":
                        if (way != null) {
                            way.setAttribute(atts.getValue(K), atts.getValue(V));
                        } else if (relation != null) {
                            relation.setAttribute(atts.getValue(K), atts.getValue(V));
                        }
                        break;
                    default: break;
                }
            }

            /**
             * Méthode qui redéfinit la méthode endElement
             * de la superclasse DefaultHandler et qui effectue
             * les opérations nécessaires quand une balise fermante
             * d'un élément est rencontrée
             *
             * @author Andrey Batasov (250149)
             * @author Raja Soufi (247680)
             *
             * @param uri
             * @param lName
             * @param qName
             *      le nom de l'élément fermé
             *
             * @throws SAXException
             *      si une erreur est rencontrée dans le format
             *      du fichier XML contenant la carte
             */
            @Override
            public void endElement(String uri, String lName, String qName) throws SAXException {
                switch (qName) {
                    case NODE:
                        if (!node.isIncomplete()) {
                            map.addNode(node.build());
                        }
                        node = null;
                        break;
                    case WAY:
                        if (!way.isIncomplete()) {
                            map.addWay(way.build());
                        }
                        way = null;
                        break;
                    case RELATION:
                        if (!relation.isIncomplete()) {
                            map.addRelation(relation.build());
                        }
                        relation = null;
                        break;
                    default: break;
                }
            }
        });
        reader.parse(new InputSource(stream));
        stream.close();
        return map.build();
    }

}