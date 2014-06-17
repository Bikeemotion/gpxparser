package com.mlab.gpx.impl;

import java.util.List;

import com.mlab.gpx.impl.util.Util;
import com.mlab.gpx.impl.util.XmlFactory;

/**
 * {longitude, latitude, altitude, speed, bearing, accuracy}
 * @author shiguera
 *
 */
public class AndroidWayPoint extends SimpleWayPoint {

	protected final String namespace = "mlab";
	protected final int SPEED_DECIMALS = 6;
	protected final int BEARING_DECIMALS = 1;
	protected final int ACCURACY_DECIMALS = 1;

	
	
	/**
	 * Velocidad del móvil medida en m/sg . Cuando no se conoce el dato
	 * se fija en '-1.0'
	 */
	protected double speed;	
	/**
	 * Rumbo seguido por el móvil medido en grados desde el Norte hacia el Este. 
	 * Cuando no se conoce el dato se 
	 * fija en '-1.0'
	 */
	protected double bearing;
	/** Precisión de las medidas de posición en metros. Cuando no se conoce
	 * se fija en '-1.0'
	 */
	protected double accuracy;


	public AndroidWayPoint() {
		super();
		this.speed = -1.0;
		this.bearing = -1.0;
		this.accuracy = -1.0;
	}

	public AndroidWayPoint(String name, String description, long time, 
			double longitude, double latitude, double altitude, 
			double speed, double bearing, double accuracy) {
		super(name,description,time,longitude,latitude,altitude);
		this.speed = speed;
		this.bearing = bearing;
		this.accuracy = accuracy;
	}
	
	public AndroidWayPoint(String name, String descrip, long time, List<Double> values) {
		super(name, descrip, time, values);
		this.speed = -1.0;
		this.bearing = -1.0;
		this.accuracy = -1.0;
		if(values!=null && values.size()>=6) {
			this.speed=(values.get(3)!=null?values.get(3):0.0);
			this.bearing=(values.get(4)!=null?values.get(4):0.0);
			this.accuracy=(values.get(5)!=null?values.get(5):0.0);
		}
	}
	
	/**
	 * Makes a copy of active object
	 */
	@Override
	public AndroidWayPoint clone() {
		AndroidWayPoint wp = new AndroidWayPoint();
		wp.name = this.getName();
		wp.description = this.getDescription();
		wp.time = this.getTime();
		wp.longitude = this.getLongitude();
		wp.latitude = this.getLatitude();
		wp.altitude = this.getAltitude();
		wp.tagname = this.getTag();
		wp.speed = this.speed;
		wp.bearing = this.bearing;
		wp.accuracy = this.accuracy;
		return wp;
	}
	
	@Override
	public double[] getValues() {		
		return new double[] {this.longitude, this.latitude, this.altitude,
				this.speed, this.bearing, this.accuracy};
	}
	
	@Override
	protected String extensionsAsGpx() {
		StringBuilder builder = new StringBuilder();
		builder.append("<extensions>");
		builder.append(XmlFactory.createDoubleTag(namespace, "speed", speed, 12, SPEED_DECIMALS));
		builder.append(XmlFactory.createDoubleTag(namespace, "bearing", bearing, 12, BEARING_DECIMALS));
		builder.append(XmlFactory.createDoubleTag(namespace, "accuracy", accuracy, 12, ACCURACY_DECIMALS));
		builder.append("</extensions>");
		return builder.toString();
	}
	@Override
	protected String extensionsAsCsv() {
		StringBuilder builder = new StringBuilder();
		builder.append(Util.doubleToString(this.speed, 12, SPEED_DECIMALS));
		builder.append(",");
		builder.append(Util.doubleToString(this.bearing, 12, BEARING_DECIMALS));
		builder.append(",");
		builder.append(Util.doubleToString(this.accuracy, 12, ACCURACY_DECIMALS));
		return builder.toString();
	}

	
	public String getNamespace() {
		return namespace;
	}

	
	public double getSpeed() {
		return speed;
	}


	public double getBearing() {
		return bearing;
	}


	public double getAccuracy() {
		return accuracy;
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}


	public void setBearing(double bearing) {
		this.bearing = bearing;
	}


	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

}
