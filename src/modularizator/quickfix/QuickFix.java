package modularizator.quickfix;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.internal.quickaccess.SearchField;

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
		IResource source = marker.getResource();
		IProject project = source.getProject();
		IJavaElement javaElem = JavaCore.create(source);
		ICompilationUnit compUnit = (ICompilationUnit) javaElem;
		
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(compUnit);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		cu.recordModifications();
		AST ast = cu.getAST();
		
		
		ASTVisitor visitor = new ASTVisitor() {
            Set<String> names = new HashSet<String>();
            
            @Override
            public boolean visit(VariableDeclarationFragment node) {
                SimpleName name = node.getName();
                this.names.add(name.getIdentifier());
                System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
                return false; // do not continue to avoid usage info
            }
            @Override
            public boolean visit(SimpleName node) {
                if (this.names.contains(node.getIdentifier())) {
                    System.out.println("Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
                }
                return true;
            }
            
        };
		
		cu.accept(visitor);
		visitor.visit(cu.getPackage());
		
		/*parser.
		
		try {
			file.move(destination, true, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*IPath destination = newSource.getProjectRelativePath();
		destination.append("/src");
		destination = destination.append(source.getName());
		try {
			source.move(destination, false, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
