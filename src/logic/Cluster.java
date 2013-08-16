package logic;


import java.util.HashMap;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Represents clusters of a network
 * @author vahan
 *
 */
public class Cluster implements Cloneable {
	/**
	 * Static counter to make unique IDs for each cluster
	 */
	private static int counter = 0;
	/**
	 * All so far created clusters stored to be easily accessible by IDs
	 */
	private static HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();
	/**
	 * Unique ID
	 */
	private final int id;
	/**
	 * The Java source element that is represented by the cluster. Usually a package
	 */
	private final IJavaElement sourceElement;

	/**
	 * Constructor
	 * @param elem
	 */
	public Cluster(IJavaElement elem) {
		id = counter;
		this.sourceElement = elem;
		clusters.put(id, this);
		counter++;
	}
	
	
	public static Cluster getCluster(int id) {
		return clusters.get(id);
	}
	
	public static int getClustersCount() {
		return counter;
	}
	
	public int getId() {
		return id;
	}
	
	public IJavaElement getModel() {
		return sourceElement;
	}
	
	@Override
	/**
	 * Makes a new cluster object with the same source element, but a new ID.
	 * Needed for cloning networks
	 */
	public Object clone() {
		Cluster cloned = new Cluster(sourceElement);
		
		return cloned;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceElement == null) ? 0 : sourceElement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (sourceElement == null) {
			if (other.sourceElement != null)
				return false;
		} else if (!sourceElement.equals(other.sourceElement))
			return false;
		return true;
	}
	

}
