package pt.iscte.pidesco.hierarchy.visitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<ClassTreeElement, ArrayList<ClassTreeElement>>  parentsMap;
	
	public PackageVisitor(ArrayList<ClassTreeElement> fileList, ArrayList<File> javaFileList, Tree tree, 
			HashMap<ClassTreeElement, ArrayList<ClassTreeElement>>  parentsMap) {
		this.fileList=fileList;
		this.javaFileList=javaFileList;
		this.tree=tree;
		this.parentsMap=parentsMap;
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



	
	
}
