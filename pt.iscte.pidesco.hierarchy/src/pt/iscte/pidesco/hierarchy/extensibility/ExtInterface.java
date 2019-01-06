package pt.iscte.pidesco.hierarchy.extensibility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;

public interface ExtInterface {

	/**
	 * Runs the extension on the Type Hierarchy plugin. By default, it does nothing.
	 * 
	 * @param area the composite of the Type Hierarchy view
	 * @param file the opened file in de Java file Editor
	 */
	default void run(Composite area,File f, HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap) {
		
	}


}
