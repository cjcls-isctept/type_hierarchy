package pt.iscte.pidesco.hierarchy.extensibility;

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public interface ExtInterface {

	/**
	 * Runs the extension on the Type Hierarchy plugin. By default, it does nothing.
	 * 
	 * @param area the composite of the Type Hierarchy view
	 * @param file the opened file in de Java file Editor
	 */
	default void run(Composite area,File f, Tree t) {
		
	}

	default void update(Composite viewArea, File openedFile, Tree tree_copy) {
		
	}
}
