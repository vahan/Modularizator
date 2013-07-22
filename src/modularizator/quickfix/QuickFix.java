package modularizator.quickfix;

import java.util.concurrent.ExecutionException;

import modularizator.Fixer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {

	public final static String ATTRIBUTE_NEWSOURCE = "newsource";
	
	private IJavaElement newContainer;
	
	public QuickFix(IJavaElement newContainer) {
		this.newContainer = newContainer;
	}
	
	@Override
	public String getLabel() {
		String label = "Move to " + newContainer.getElementName();
		return label;
	}

	@Override
	public void run(IMarker marker) {
		Fixer fixer = new Fixer(newContainer);
		fixer.fix(marker);
		
	}
	
	
	public Object searchPackage(String name) throws ExecutionException {
		System.out.println("Searching for " + name);
		SearchPattern pattern = SearchPattern.createPattern(name,
				IJavaSearchConstants.PACKAGE, IJavaSearchConstants.DECLARATIONS,
				SearchPattern.R_EXACT_MATCH);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {
				System.out.println(match.getElement().toString() + "  -  " + match.getElement().getClass());
			}
		};
		
		SearchEngine searchEngine = new SearchEngine();
		try {
			searchEngine.search(pattern, 
					new SearchParticipant[] {
						SearchEngine.getDefaultSearchParticipant()
					},
					scope, requestor, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	
		return null;
	}

}
