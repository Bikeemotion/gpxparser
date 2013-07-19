package com.mlab.tesis.java.gpx.data;

import java.util.List;

/**
 * Interface for GpxElements that are composite elements
 * @author shiguera
 *
 */
public interface GpxElement extends GpxNode {
	
	public boolean add(GpxNode node);
	public List<GpxNode> nodes();
	public int size();

}
