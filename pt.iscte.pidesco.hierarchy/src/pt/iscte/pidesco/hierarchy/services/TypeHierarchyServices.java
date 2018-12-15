package pt.iscte.pidesco.hierarchy.services;



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


}
