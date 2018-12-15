package pt.iscte.pidesco.hierarchy.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Tree;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.hierarchy.services.TypeHierarchyListener;
import pt.iscte.pidesco.hierarchy.services.TypeHierarchyServices;

public class TypeHierarchyActivator implements BundleActivator {

	private static TypeHierarchyActivator instance;
	private static BundleContext context;
	private Set<TypeHierarchyListener> listeners;
	private TypeHierarchyServices services;
	private ServiceRegistration<TypeHierarchyServices> service;
	private PidescoServices pidescoServices;
	private Tree tree;
	


	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;/*
		listeners = new HashSet<TypeHierarchyListener>();
		service = context.registerService(TypeHierarchyServices.class, new TypeHierarchyServicesImpl(), null);
		ServiceReference<PidescoServices> ref = context.getServiceReference(PidescoServices.class);
		pidescoServices = context.getService(ref);*/
		TypeHierarchyActivator.context = bundleContext;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		TypeHierarchyActivator.context = null;
	}

	public static TypeHierarchyActivator getInstance() {
		
		return instance;
	}

	public void removeListener(TypeHierarchyListener listener) {
		listeners.add(listener);
		
	}

	public void addListener(TypeHierarchyListener listener) {
		listeners.add(listener);
		
	}


	
}
