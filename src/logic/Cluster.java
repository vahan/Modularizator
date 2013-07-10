package logic;

import java.util.ArrayList;

import org.eclipse.jdt.core.IJavaElement;

public class Cluster {
	
	private final ArrayList<Object> vertices;
	
	private final IJavaElement sourceElement;

	public Cluster(ArrayList<Object> vertices, IJavaElement elem) {
		super();
		this.vertices = vertices;
		this.sourceElement = elem;
	}

	public ArrayList<Object> getVertices() {
		return vertices;
	}

	public IJavaElement getModel() {
		return sourceElement;
	}
	
	
	

}
