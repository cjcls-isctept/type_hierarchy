package pt.iscte.pidesco.hierarchy.internal;



import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Tree;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;
import pt.iscte.pidesco.hierarchy.services.TypeHierarchyListener;
import pt.iscte.pidesco.hierarchy.services.TypeHierarchyServices;

public class TypeHierarchyServicesImpl implements TypeHierarchyServices{
	
	private HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap;
	
	/*public TypeHierarchyServicesImpl(HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap) {
		this.parentsMap=parentsMap;
	}*/
	
	@Override
	public void addListener(TypeHierarchyListener listener) {
		TypeHierarchyActivator.getInstance().addListener(listener);
		
	}

	@Override
	public void removeListener(TypeHierarchyListener listener) {
		TypeHierarchyActivator.getInstance().removeListener(listener);
		
	}
	
	
	/**
	 * 
	 * 
	 */
	@Override
	public HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> getDataFromTree() {
		return TypeHierarchyView.getParentsMap();
		//TypeHierarchyActivator.getInstance().getParentsMap();
		//return null;
		//return parentsMap;
	}



}
