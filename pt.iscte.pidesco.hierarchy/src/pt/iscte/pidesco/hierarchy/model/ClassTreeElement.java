package pt.iscte.pidesco.hierarchy.model;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ClassTreeElement {

	private ClassTreeElement parent;

	private String name="";
	private String packageName="";
	private String className;
	ArrayList<ClassTreeElement> familyUpwards = new ArrayList<ClassTreeElement>();
	ArrayList<ClassTreeElement> children = new ArrayList<ClassTreeElement>();
	private Tree familyTree;
	private TreeItem treeItem;

	
	
	/**
	 * Represents a class element
	 * 
	 * @isMain represents if said instance is the one represented in the type
	 *         hierarchy view
	 * 
	 */
	public ClassTreeElement(/*ClassTreeElement parent, File file, boolean isMain Tree familyTree*/) {
		//this.familyTree=familyTree;
		if (parent == null) {
			// quer dizer que é root ?
		}/*
		
		this.parent = parent;
		this.file = file;
		this.isMain = isMain;*/

	}
	
	public void setTreeItem(TreeItem item) {
		this.treeItem=treeItem;
		
	}

	public void addParent(	ClassTreeElement e) {
		
	}

	public void addChild(	ClassTreeElement e) {
		children.add(e);
	}
	
	

	public ArrayList<ClassTreeElement> getChildren() {
		return children;
	}


	public void setParent(ClassTreeElement parent) {
		this.parent = parent;
	}



	
	/**
	 * apends name
	 * @param name
	 */
	public void appendName(String name) {
		this.name=this.name+name;
	}
	
	public void setClassName(String className) {
		this.className=className;
	}
	
	public void setPackageName(String packageName) {
		this.packageName=packageName;
	}
	
	public void clearName() {
		name="";
	}


	public String getCompleteName() {
		return className + " - " + packageName;
	}
	
	public String getElementName() {
		return className;
	}


	public ClassTreeElement getParent() {
		if(parent==null) {
			ClassTreeElement object = new ClassTreeElement();
			object.setClassName("Object");
			
			return object;
		}
		return parent;
	}


	public void clear() {
		parent=null;
		
		
	}



	
	
}