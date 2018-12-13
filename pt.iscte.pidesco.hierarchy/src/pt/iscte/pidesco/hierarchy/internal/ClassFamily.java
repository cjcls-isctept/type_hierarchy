package pt.iscte.pidesco.hierarchy.internal;

import java.util.ArrayList;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;

/**
 * 
 * @author cjclc
 *
 */
public class ClassFamily {

	ArrayList<ClassTreeElement> children ;
	ClassTreeElement parent;

/**
 * This class stores a Class' Parent and Children
 * 
 * @param children 
 * @param parent
 */
	public ClassFamily(ArrayList<ClassTreeElement> children, ClassTreeElement parent) {
		this.children = children;
		this.parent = parent;
	}


	public ArrayList<ClassTreeElement> getChildren() {
		return children;
	}


	public ClassTreeElement getParent() {
		return parent;
	}
	
	

}
