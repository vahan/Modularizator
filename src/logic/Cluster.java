package logic;


import java.util.HashMap;

import org.eclipse.jdt.core.IJavaElement;

public class Cluster implements Cloneable {
	
	private static int counter = 0;
	
	private static HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();
	
	private final int id;
	
	private final IJavaElement sourceElement;

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
