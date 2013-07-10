package logic;


import org.eclipse.jdt.core.IJavaElement;

public class Cluster {
	
	private final IJavaElement sourceElement;

	public Cluster(IJavaElement elem) {
		super();
		this.sourceElement = elem;
	}

	public IJavaElement getModel() {
		return sourceElement;
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
