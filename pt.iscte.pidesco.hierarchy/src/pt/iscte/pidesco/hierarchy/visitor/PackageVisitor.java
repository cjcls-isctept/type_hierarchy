package pt.iscte.pidesco.hierarchy.visitor;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement.Visitor;

public class PackageVisitor implements Visitor{
	
	private ArrayList<ClassTreeElement> fileList;
	private ArrayList<File> javaFileList;
	private Tree tree;
	
	public PackageVisitor(ArrayList<ClassTreeElement> fileList, ArrayList<File> javaFileList, Tree tree) {
		this.fileList=fileList;
		this.javaFileList=javaFileList;
		this.tree=tree;
	}

	@Override
	public boolean visitPackage(PackageElement packageElement) {
		
		//System.out.println("tou no visitor, e o nome do package é: "+packageElement.getName());
		return true;
	}

	@Override
	public void visitClass(ClassElement classElement) {
		//System.out.println("tou no visitor, e o nome da classe é: "+classElement.getName());
		if(classElement.isClass()) {
			ClassTreeElement elem = new ClassTreeElement();
			String[] nameArr = classElement.getName().split("\\.");
			String name=nameArr[0];
			//System.out.println("este é o nome   "+name[0]);
			//ClassTreeElement elemParent = new ClassTreeElement();
			//System.out.println("este é o problema? " + classElement.getParent().getName());
			//elemParent.setClassName(classElement.getParent().getName());
			//elem.setParent(elemParent);
			//System.out.println("este é o ficheiro" + classElement.getFile().getName());
			
			elem.setClassName(name);
			javaFileList.add(classElement.getFile());
			fileList.add(elem);
		
			
		}
		
	}

	private void addToTree(ClassTreeElement elem) {

		
	}

	
	/*
	 * 
	 * portanto agora é criar um visitor para os packages como o stor disse
	 * ProjectBrowserServices s = ...

PackageElement root = s.getRootPackage();

root.traverse(new Visitor() {...});


desta forma, obténs a package raiz (default), e fazes passar um visitor por toda a hierarquia de ficheiros.
	 * 
	 * 
	 * 
	 * 
	 */
}
