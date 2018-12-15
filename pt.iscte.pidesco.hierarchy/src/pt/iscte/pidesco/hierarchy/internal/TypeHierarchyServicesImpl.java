package pt.iscte.pidesco.hierarchy.internal;



import pt.iscte.pidesco.hierarchy.services.TypeHierarchyListener;
import pt.iscte.pidesco.hierarchy.services.TypeHierarchyServices;

public class TypeHierarchyServicesImpl implements TypeHierarchyServices{

	@Override
	public void addListener(TypeHierarchyListener listener) {
		TypeHierarchyActivator.getInstance().addListener(listener);
		
	}

	@Override
	public void removeListener(TypeHierarchyListener listener) {
		TypeHierarchyActivator.getInstance().removeListener(listener);
		
	}



}
