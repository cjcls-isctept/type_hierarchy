package pt.iscte.pidesco.hierarchy.model;

import java.io.File;
import java.util.ArrayList;

public class ClassTreeElement {

	private ClassTreeElement parent;
	private File file;
	private boolean isMain;
	private String name="";
	private String packageName="";
	private String className;
	ArrayList<ClassTreeElement> familyUpwards = new ArrayList<ClassTreeElement>();
	ArrayList<ClassTreeElement> familyDownwards = new ArrayList<ClassTreeElement>();

	
	
	/**
	 * Represents a class element
	 * 
	 * @isMain represents if said instance is the one represented in the type
	 *         hierarchy view
	 * 
	 */
	public ClassTreeElement(/*ClassTreeElement parent, File file, boolean isMain*/) {
		if (parent == null) {
			// quer dizer que é root ?
		}/*
		this.parent = parent;
		this.file = file;
		this.isMain = isMain;*/

	}

	public void addParent(	ClassTreeElement e) {
		familyUpwards.add(e);
	}

	public void addChild(	ClassTreeElement e) {
		familyDownwards.add(e);
	}

	public void setParent(ClassTreeElement parent) {
		this.parent = parent;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public void setMain(boolean isMain) {
		this.isMain = isMain;
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


	public String getName() {
		return className + " - " + packageName;
	}


	public ClassTreeElement getParent() {
		return parent;
	}


	public void clear() {
		parent=null;
		
		
	}



	
	
}