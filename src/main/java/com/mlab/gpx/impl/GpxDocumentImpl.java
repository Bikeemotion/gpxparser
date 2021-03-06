package com.mlab.gpx.impl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.mlab.gpx.api.GpxDocument;
import com.mlab.gpx.api.GpxFactory;
import com.mlab.gpx.api.WayPoint;
import com.mlab.gpx.impl.util.XmlFactory;

/**
 * Clase utilitaria para manipulación de documentos Gpx
 * Básicamente se compone de un elemento Metadata, uno Extensions y unas colecciones
 * de WayPoint's, Route's y Track's
 * El esquema xsd es:<p>
 * <pre>
 * {@code
 * <xsd:complexType name="gpxType">
 *    <xsd:sequence>
 *        <xsd:element name="metadata" type="metadataType" minOccurs="0"/>
 *        <xsd:element name="wpt" type="wptType" minOccurs="0" maxOccurs="unbounded"/>
 *        <xsd:element name="rte" type="rteType" minOccurs="0" maxOccurs="unbounded"/>
 *        <xsd:element name="trk" type="trkType" minOccurs="0" maxOccurs="unbounded"/>
 *        <xsd:element name="extensions" type="extensionsType" minOccurs="0"/>
 *    </xsd:sequence>
 *    <xsd:attribute name="version" type="xsd:string" use="required" fixed="1.1"/>
 *    <xsd:attribute name="creator" type="xsd:string" use="required"/>
 * </xsd:complexType>
 * }
 * </pre>
 * 
 * @author shiguera
 */
public class GpxDocumentImpl implements GpxDocument {	
	private final String TAG_WAYPOINT = "wpt";
	
	final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
		"<gpx version=\"1.1\" creator=\"MercatorLab - http:mercatorlab.com\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
		"xmlns=\"http://www.topografix.com/GPX/1/1\" "+
		"xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" "+
		"xmlns:mlab=\"http://mercatorlab.com/downloads/mlab.xsd\">";
	final String FOOTER = "</gpx>";
	
	protected GpxFactory gpxFactory;
	
	/**
	 * DOM Document tipo gpx
	 */
	protected Document doc;

	/**
	 * Metadata del GpxDocument
	 */
	protected Metadata metadata;
	/**
	 * Colección de WayPoint's del GpxDocument
	 */
	protected List<WayPoint> wpts; 
	/**
	 * Colección de Route's del GpxDocument
	 */
	protected List<Route> routes; 

	/**
	 * Colección de Track's del GpxDocument
	 */
	protected List<Track> tracks;
	
	protected Extensions extensions;
	
	protected File gpxFile;
	/**
	 * Constructor de clase. Inicializa las colecciones.
	 * He añadido el parámetro para el factory,
	 * lo que rompe la compatibilidad con programas ya hechos
	 * 
	 */
	public GpxDocumentImpl() {
		this.gpxFactory = GpxFactory.getFactory(GpxFactory.Type.SimpleGpxFactory);
		this.doc = null;
		this.metadata= new Metadata();
		this.routes = new ArrayList<Route>();
		this.tracks = new ArrayList<Track>();
		this.wpts = new ArrayList<WayPoint>();
		this.extensions = new Extensions();
		this.gpxFile = null;
	}

	@Override
	public Document getDomDocument() {
		doc = XmlFactory.parseXmlDocument(this.asGpx());
		return doc;
	}

	@Override
	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public List<WayPoint> getWayPoints() {
		return wpts;
	}

	@Override
	public List<Route> getRoutes() {
		return routes;
	}

	@Override
	public List<Track> getTracks() {
		return tracks;
	}

	/**
	 * Añade un track a la colección de tracks del GpxDocument
	 * @param track Track que se quiere añadir
	 */
	@Override
	public boolean addTrack(Track track) {
		return this.tracks.add(track);
		
	}
	
	@Override
	public boolean removeTrack(Track track) {
		return this.tracks.remove(track);
	}
	@Override
	public Track getTrack(int index) {
		return this.tracks.get(index);
	}
	/**
	 * Devuelve el número de tracks en el documento
	 * @return Devuelve el número de tracks en el documento
	 */
	@Override
	public int trackCount() {
		return this.tracks.size();
	}
	@Override
	public boolean hasTracks() {
		return (this.tracks.size()>0);
	}

	
	/**
	 * Añade un WayPoint a la colección de WayPoints
	 * @param wp WayPoint que se quiere añadir
	 */
	@Override
	public boolean addWayPoint(WayPoint wp) {
		wp.setTag(TAG_WAYPOINT);
		return this.wpts.add(wp);
	}
	@Override
	public boolean removeWayPoint(WayPoint wp) {
		return this.wpts.remove(wp);
	}
	@Override
	public int wayPointCount() {
		return this.wpts.size();
	}
	/**
	 * Devuelve true si el GpxDocument tiene algún WayPoint individual
	 * @return true si existen WayPoints individuales en el documento.
	 * false en caso contrario
	 */
	@Override
	public boolean hasWayPoints() {
		return (this.wpts.size()>0);
	}
	@Override
	public WayPoint getWayPoint(int index) {
		return this.wpts.get(index);
	}
	/**
	 * Añade una Route a la colección de rutas del GpxDocument
	 * @param rte Route que se quiere añadir
	 */
	@Override
	public boolean addRoute(Route rte) {
		return this.routes.add(rte);
	}
	
	@Override 
	public boolean removeRoute(Route route) {
		return this.routes.remove(route);
	}
	@Override
	public int routeCount() {
		return this.routes.size();
	}
	/**
	 * Devuelve true si el GpxDocument tiene algún Route
	 * @return true si existe algún Route en el documento.
	 * false en caso contrario
	 */
	@Override
	public boolean hasRoutes() {
		return (this.routes.size()>0);
	}
	@Override
	public Route getRoute(int index) {
		return this.routes.get(index);
	}
	/**
	 * Devuelve una cadena con el documento xml
	 * @return Cadena gpx del documento, incluso cabecera xml
	 */
	@Override
	public String asGpx() {
		String cad = "";
		cad += HEAD;
		
		cad += this.metadata.asGpx();
		
		if(this.wpts.size()>0) {
			for(int i=0; i<this.wpts.size(); i++) {
				//System.out.println(this.wpts.get(i).asGpx());
				cad += this.wpts.get(i).asGpx();
			}
		}

		if(this.routes.size()>0) {
			for(int i=0; i<this.routes.size(); i++) {
				cad += this.routes.get(i).asGpx();
			}
		}
		//System.out.println(cad);

		if(this.tracks.size()>0) {
			for(int i=0; i<this.tracks.size(); i++) {
				cad += this.tracks.get(i).asGpx();
			}
		}
		
		// FIXME Falta resolver el elemento <extensions>
		
		
		cad += FOOTER;
		return cad;
	}
	
	
	public String format() {
		String xml=this.asGpx();
        return XmlFactory.format(xml);
    }

	@Override
	public Extensions getExtensions() {
		return this.extensions;
	}

	public File getGpxFile() {
		return gpxFile;
	}

	public void setGpxFile(File gpxFile) {
		this.gpxFile = gpxFile;
	}
		
}
