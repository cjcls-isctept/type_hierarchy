package pt.iscte.pidesco.demo.ext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;

import pt.iscte.pidesco.hierarchy.extensibility.ExtInterface;
import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;

public class TestAction2 implements ExtInterface {

	private List list2;
	
	@Override
	public void run(Composite area,File f, HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap) {
		//new Label(area, SWT.NONE).setText("!!!");
		
		String[] list = {f.getName(), "Method 1", "Method 2", "Method 3", "Method 4"};
		list2 = new List(area, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		
		for (String s: list) {
			
			list2.setItems (list);
		}
		
		
	}


}
