package pt.iscte.pidesco.hierarchy.services;

import java.util.ArrayList;
import java.util.HashMap;


import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;

public interface TypeHierarchyServices {
	
	/**
	 * Add a Type Hierarchy listener. If listener already added, does nothing.
	 * @param listener (non-null) reference to the listener
	 */
	void addListener(TypeHierarchyListener listener);

	/**
	 * Remove a Type Hierarchy listener. If listener does not exist, does nothing.
	 * @param listener (non-null) reference to the listener
	 */
	void removeListener(TypeHierarchyListener listener);

	/**
	 * Returns the Type Hierarchy main tree data in the form of <Parent, Children>
	 * 
	 * @return HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> 
	 */
	HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> getDataFromTree();
	
}
