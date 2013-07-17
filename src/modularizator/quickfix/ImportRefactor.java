package modularizator.quickfix;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jface.text.Document;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;

public class ImportRefactor {
	/*
	private String name;
	
	public ImportRefactor(String name) {
		this.name = name;
	}
	

	public void searchImportInJavaProject(IProgressMonitor monitor,IJavaProject project, List<Change> changes){
		try {
			if (project.isOpen()) {
				IPackageFragment[] packages = project.getPackageFragments();
				// parse(JavaCore.create(project));
				for (IPackageFragment mypackage : packages) {
					searchImportInPackageFragment(monitor,mypackage, changes);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void searchImportInPackageFragment(IProgressMonitor monitor, IPackageFragment fragment, List<Change> changes) throws JavaModelException{
		if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
			for (IJavaElement element : fragment.getChildren()) {
				if (element instanceof ICompilationUnit) {
					ICompilationUnit unit = (ICompilationUnit) element;
					Change change = searchImportInCompilationUnit(monitor, unit);
					if(change != null){
						changes.add(change);
					}
				}
				if (element instanceof IPackageFragment) {
					IPackageFragment subFragment = (IPackageFragment) element;
					searchImportInPackageFragment(monitor, subFragment, changes);
				}
			}
		}
	}

	public Change searchImportInCompilationUnit(IProgressMonitor monitor,
			ICompilationUnit unit) throws JavaModelException {
		List<String> list = new ArrayList<String>();
		CompilationUnit cu = createCompilationUnit(unit);
		IImportDeclaration importDec = unit.getImport(name);
		if (list.size() > 0) {
			return renameImport(monitor, unit, importDec);
		} else {
			return null;
		}
	}

	private Change renameImport(IProgressMonitor monitor,
			ICompilationUnit unit, IImportDeclaration import2Rename)
			throws JavaModelException {
		// Get the current source code
		String source = unit.getSource();
		// Create a document
		Document document = new Document(source);
		// Create Compilation Unit
		CompilationUnit cu = createCompilationUnit(unit);
		int import2RenameInd = -1;
		List imports = cu.imports();
		for (int j = 0; j < imports.size(); j++) {
			ImportDeclaration importDeclaration = (ImportDeclaration) imports.get(j);
			if (importDeclaration.getName().getFullyQualifiedName().equalsIgnoreCase(name)) {
				import2RenameInd = j;
				break;
			}
		}
		// Delete this specific unused import
		if (import2RenameInd > 0) {
			ImportDeclaration impDec = (ImportDeclaration) cu.imports().get(import2RenameInd);
			impDec.
		}
		// No idea how to do it for the moment.
		MultiTextEdit edit = new MultiTextEdit();
		TextEdit cuEdit = cu.rewrite(document, unit.getJavaProject()
				.getOptions(true));
		// Apply the new source code to the document
		edit.addChild(cuEdit);
		// Get the source code.
		String newSource = document.get();
		// set the code source to the IcompilationUnit.
		unit.getBuffer().setContents(newSource);
		// Create a change
		TextFileChange change = new TextFileChange(unit.getElementName(),
				(IFile) unit.getResource());
		change.setTextType("java");
		change.setEdit(edit);
		change.setSaveMode(TextFileChange.FORCE_SAVE);
		return change;
	}

	*//**
	 * Create a compilationUnit based on a ICompilationUnit Record all the
	 * modification perform on this object
	 * 
	 * @param unit
	 * @return
	 * @throws JavaModelException
	 *//*
	protected CompilationUnit createCompilationUnit(ICompilationUnit unit)
			throws JavaModelException {
		// Create a parser
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		// Set source
		parser.setSource(unit);
		parser.setResolveBindings(true);
		// Create a compilation
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.recordModifications();

		return cu;
	}*/
}
