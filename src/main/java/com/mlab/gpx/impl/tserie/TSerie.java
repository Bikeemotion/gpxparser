package com.mlab.gpx.impl.tserie;

/**
 * Encapsula una TList junto con una DoublesList <br/>
 * Viene a ser una función del tiempo : <br/>
 *    f: T ---> double[]<br/>
 * Representa una serie temporal de datos. Cada fila de 
 * datos consta de un tiempo en milisegundos y un array 
 * de doubles que se corresponde con los datos para ese tiempo.<br/>
 * Dispone de un método 'getValues(long tt)' que devuelve el array 
 * de valores correspondientes a un tiempo t. Se interpolan 
 * los valores entre los tiempos anterior y posterior al solicitado.
 * @author shiguera
 *
 */ 
public class TSerie {
	
	protected TList tlist;

	protected DoublesList dlist;
	
	public TSerie() {
		tlist = new TList();
		dlist = new DoublesList();
	}
	
	public boolean canAdd(long t, double[] values) {
		//System.out.print("TSerie.canAdd(): t="+tlist.canAdd(t));
		//System.out.println("  values="+dlist.canAdd(values));
		return (tlist.canAdd(t) && dlist.canAdd(values));
	}
	
	/**
	 * Añade un tiempo y un vector de datos a la serie.<br/>
	 * 
	 * @param t long tiempo
	 * @param values double[] valores
	 * @return true si se añade, false si no (t no añadible o double[] no añadible)
	 */
	public boolean add(long t, double[] values) {
		if(tlist.canAdd(t) && dlist.canAdd(values)) {
			if(tlist.add(t)) {
				if(dlist.add(values)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Devuelve el número de filas de datos de la serie
	 * @return
	 */
	public int size() {
		return this.tlist.size();
	}

	/** 
	 * Devuelve el tiempo del indice index
	 * @param index
	 * @return
	 */
	public long getTime(int index) {
		return tlist.get(index);
	}
	
	/**
	 * Devuelve el primer tiempo de los almacenados en la serie temporal
	 * 
	 * @return
	 */
	public long firsTime() {
		return this.tlist.firstTime();
	}
	
	/**
	 * Devuelve el último tiempo de los almacenados en la serie temporal
	 * 
	 * @return
	 */
	public long lastTime() {
		return this.tlist.lastTime();
	}
	/**
	 * Devuelve los valores double[] del índice index
	 * @param index
	 * @return
	 */
	public double[] getValues(int index) {
		return dlist.get(index);
	}

	/**
	 * Si el tiempo es mayor o igual que el primero y
	 * menor o igual que el último, devuelve el valor 
	 * de doubles interpolados entre los de la serie,<br/>
	 * Si el tiempo es exactamente uno de los de la serie
	 * devuelve esos valores sin interpolación.
	 * En otras circunstancias devuelve null
	 * @param t
	 * @return
	 */
	public double[] getValues(long t) {
		int floorindex = tlist.indexOfFloor(t);
		if(floorindex==-1) {
			return null;
		}
		// Si coincide devolver
		if(t==tlist.get(floorindex)) {
			return dlist.get(floorindex);
		}
		double rel = (double)(t - tlist.get(floorindex))/
				(double)(tlist.get(floorindex+1)-tlist.get(floorindex));
		double[] result = new double[dlist.getDimension()];
		for(int i=0; i<dlist.getDimension();i++) {
			result[i]=dlist.get(floorindex)[i]+
				rel*(dlist.get(floorindex+1)[i]-dlist.get(floorindex)[i]);
		}
		return result;
	}

	public double[] getValues(long t, StrategyOnValues strategy) {
		return strategy.getValues(this, t);
	}
	public int indexOfFloor(long t) {
		return tlist.indexOfFloor(t);
	}
	public int indexOfCeiling(long t) {
		return tlist.indexOfCeiling(t);
	}
	public boolean isInRange(long t) {
		return tlist.isInRange(t);
	}
	public int getDimension() {
		return dlist.getDimension();
	}

	public void empty() {
		tlist.empty();
		dlist.empty();
	}
}
