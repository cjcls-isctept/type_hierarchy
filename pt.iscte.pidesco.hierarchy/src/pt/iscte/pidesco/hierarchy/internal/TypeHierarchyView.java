package pt.iscte.pidesco.hierarchy.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.hierarchy.extensibility.ExtInterface;
import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;
import pt.iscte.pidesco.hierarchy.visitor.ClassInfoVisitor;
import pt.iscte.pidesco.hierarchy.visitor.PackageVisitor;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class TypeHierarchyView implements PidescoView {

	private ClassInfoVisitor checkClassInfo;
	private ClassTreeElement openedClassElement;
	private PackageVisitor packageVisitor;
	private ArrayList<ClassTreeElement> fileList;
	private ArrayList<File> javaFileList;
	private Tree tree;

	private Color BLUE;
	private Color BLACK;
	private Font FONT_HIGHLIGHTED;
	private Font FONT_NORMAL;



	/**
	 * familyMap tem o PAI como Key, e o par Filho, lista de filhos dele
	 */
	//private HashMap<ClassTreeElement, ClassFamily> familyMap;
	/**
	 * parentsMap tem Uma classe como Key, e uma lista de filhos dele como value
	 */
	private static HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap;

	/**
	 * creates Hierarchy main component
	 * 
	 * @param viewArea is the area in which all the components are inserted
	 * @param imageMap
	 */
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new FillLayout(SWT.VERTICAL));
		BundleContext context = TypeHierarchyActivator.getContext();

		ServiceReference<ProjectBrowserServices> serviceReference = context
				.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projServ = context.getService(serviceReference);

		ServiceReference<JavaEditorServices> serviceReference2 = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReference2);

		tree = new Tree(viewArea, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		fileList = new ArrayList<ClassTreeElement>();
		javaFileList = new ArrayList<File>();
		openedClassElement = new ClassTreeElement();
		//familyMap = new HashMap<>();
		parentsMap = new HashMap<>();
		BLUE = tree.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		BLACK = tree.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		FONT_HIGHLIGHTED = new Font(tree.getDisplay(), "Tahoma", 9, SWT.BOLD);
		FONT_NORMAL = new Font(tree.getDisplay(), "Tahoma", 9, SWT.NORMAL);



		
		Composite viewAreaChild = new Composite(viewArea, 0);
		viewAreaChild.setLayout(new FillLayout(SWT.VERTICAL));
		if (javaServ.getOpenedFile() != (null)) {
			// getOpennedClassInfo(javaServ);
			buildHierarchy(projServ.getRootPackage(), javaServ);
			extension(viewAreaChild, javaServ);
			// updateTree(tree, viewArea);

		}

		addOpeningClosingListener(viewAreaChild, javaServ);



		//addDoubleClickTreeListener(tree, viewArea,  javaServ);
		
		
		highlightSelected(javaServ);
		
		

	}
	
	static HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> getParentsMap(){
		 HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap_copy = parentsMap;
		return parentsMap_copy;
	}
	

	private void addDoubleClickTreeListener(Tree tree, Composite viewArea, JavaEditorServices javaServ) {
		tree.addListener(SWT.MouseDoubleClick, new Listener() {

			@Override
			public void handleEvent(Event event) {
				
				Point point = new Point(event.x, event.y);

				TreeItem item = tree.getItem(point);

				for (File file : javaFileList) {
					if (file!=null && file.getName().split("\\.")[0].equals(item.getText())) {
						javaServ.openFile(file);
						//System.out.println("duplo clique!!!!!!!!!!!! " + item.getText());
					}
				}
				// javaServ.openFile(file);

			}
		});
		
	}

	
	/**
	 * Adds the listeners of closing/opening to the java editor
	 * @param viewArea
	 * @param javaServ
	 */
	private void addOpeningClosingListener(Composite viewArea, JavaEditorServices javaServ) {
		javaServ.addListener(new JavaEditorListener.Adapter() {
			@Override
			public void fileOpened(File file) {

				highlightSelected(javaServ);
				extension(viewArea, javaServ);
				 // tree.removeAll(); openedClassElement.clear(); getOpennedClassInfo(javaServ);
				  //buildHierarchy(projServ.getRootPackage(), javaServ);
				 
				// updateTree(tree, viewArea);

			}

			@Override
			public void fileClosed(File file) {

				//highlightSelected(javaServ);

				/**
				 * isto ta a dar barraca
				 */
				
			}
		});

		

	}

	/**
	 * Builds the main hierarchy of the tree, using a package visitor
	 * @param rootPackage
	 * @param javaServ
	 */
	private void buildHierarchy(PackageElement rootPackage, JavaEditorServices javaServ) {
		packageVisitor = new PackageVisitor(fileList, javaFileList, tree, parentsMap);
		rootPackage.traverse(packageVisitor);
		buildTree(javaServ);

		for (ClassTreeElement c : fileList) {
			for (ClassTreeElement c2 : fileList) {
				// System.out.println(" super: " + c.getParent().getElementName() + " ,abaixo
				// ta: " + c.getElementName());
				if (c.getElementName().equals(c2.getParent().getElementName())) {
					if (!c.getChildren().contains(c2)) {
						c.addChild(c2);
					}
				}
			}
			parentsMap.put(c, c.getChildren());

			/*
			 * System.out.println("-------"); System.out.println("pai  " +
			 * c.getParent().getElementName()); System.out.println("elemento  " +
			 * c.getElementName()); System.out.println("quantos filhos?  " +
			 * c.getChildren().size()); for (ClassTreeElement cChild : c.getChildren()) {
			 * System.out.println("filho  " + cChild.getElementName()); }
			 * System.out.println("-------");
			 */
		}
		fillTree();
		// printTree();

	}
	
	/**
	 * Highlights the opened file in the tree with a different color
	 * @param javaServ
	 */
	private void highlightSelected(JavaEditorServices javaServ) {
		getOpennedClassInfo(javaServ);

		for (TreeItem item2 : tree.getItems()) {
			if (openedClassElement.getElementName().equals(item2.getText())) {
				item2.setText(item2.getText());
				item2.setForeground(BLUE);
				item2.setFont(FONT_HIGHLIGHTED);
			} else {
				item2.setText(item2.getText());
				item2.setForeground(BLACK);
				item2.setFont(FONT_NORMAL);
			}
			iterateTree(item2);

		}
	}

	/**
	 * iterates recursively in the tree in order to find the element to be highlighted
	 * @param item
	 */
	private void iterateTree(TreeItem item) {

		for (TreeItem item2 : item.getItems()) {
			// System.out.println("------------"+openedClassElement.getElementName());
			if (openedClassElement.getElementName().equals(item2.getText().trim())) {
				// System.out.println("------------"+openedClassElement.getElementName());
				item2.setText(item2.getText());
				item2.setForeground(BLUE);
				item2.setFont(FONT_HIGHLIGHTED);
			} else {
				item2.setText(item2.getText());
				item2.setForeground(BLACK);
				item2.setFont(FONT_NORMAL);
				// item2.setForeground(tree.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				// item2.setFont(new Font(tree.getDisplay(), "Tahoma", 9, SWT.Pl));
			}
			iterateTree(item2);
		}

	}

	/**
	 * builds the tree structure
	 * @param javaServ
	 */
	private void buildTree(JavaEditorServices javaServ) {

		for (ClassTreeElement classTreeElement : fileList) {
			findparents(javaFileList.get(fileList.indexOf(classTreeElement)), javaServ, classTreeElement);

			if (classTreeElement.getElementName().equals(openedClassElement.getParent().getElementName())) {
				// System.out.println("lista " + classTreeElement.getParent().getElementName());
				TreeItem treeItem0 = new TreeItem(tree, 0);
				treeItem0.setText(classTreeElement.getElementName());
				TreeItem treeItem2 = new TreeItem(treeItem0, 0);
				treeItem2.setText(openedClassElement.getCompleteName());

			}
		}

		// printTree();

	}

	private void printTree() {
		for (ClassTreeElement superParent : parentsMap.keySet()) {
			System.out.println("--------------");
			System.out.println("Parent: " + superParent.getElementName());
			// System.out.println("Class:
			// "+parentsMap.get(superParent).getParent().getElementName());
			for (int i = 0; i < parentsMap.get(superParent).size(); i++) {
				System.out.println("Children: " + i + "  " + parentsMap.get(superParent).get(i).getElementName());
			}
			System.out.println("--------------");

		}

	}

	
	/**
	 * For each file found in the project, finds its parents
	 * @param file
	 * @param javaServ
	 * @param element
	 */
	private void findparents(File file, JavaEditorServices javaServ, ClassTreeElement element) {

		parseInfo(file, javaServ, new ClassInfoVisitor(element), element);
		findChildren(element);

	}
	
	/**
	 * Fills the parentsMap HashMap with (Parent, Children) values
	 * @param element
	 */
	private void findChildren(ClassTreeElement element) {
		for (ClassTreeElement classTreeElement : fileList) {
			if (element.getElementName().equals(classTreeElement.getParent().getElementName())) {
				element.addChild(classTreeElement);
				parentsMap.put(element, element.getChildren());
				//familyMap.put(element.getParent(), new ClassFamily(element.getChildren(), element));
			}

		}

	}

	
	private void fillTree() {
		TreeItem root = new TreeItem(tree, 0);
		root.setText("Object");

		for (TreeItem item : tree.getItems()) {
			for (ClassTreeElement element : parentsMap.keySet()) {
				if ((!item.getText().equals(element.getElementName()))
						&& element.getParent().getElementName().equals("Object")) {

					TreeItem treeItem0 = new TreeItem(root, 0);
					treeItem0.setText(element.getElementName());
					fillChildren(element, treeItem0);
					treeItem0.setExpanded(true);
				}

			}
			item.setExpanded(true);

		}

	}

	private void fillChildren(ClassTreeElement element, TreeItem treeItem0) {
		for (ClassTreeElement child : parentsMap.get(element)) {
			TreeItem treeItem1 = new TreeItem(treeItem0, 0);
			treeItem1.setText(child.getElementName());
			fillChildren(child, treeItem1);
			treeItem1.setExpanded(true);

		}

	}

	/**
	 * Finds the extensions 
	 * @param viewArea
	 * @param javaServ
	 */
	private void extension(Composite viewArea, JavaEditorServices javaServ) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.hierarchy.class_info");
		//Tree tree_copy = tree;
		//HashMap<ClassTreeElement, ArrayList<ClassTreeElement>> parentsMap_copy= parentsMap;
		for(IConfigurationElement e : elements) {
			String name = e.getAttribute("name");
			
			try {
				ExtInterface action = (ExtInterface) e.createExecutableExtension("class");
				clearArea(viewArea);
				action.run(viewArea, javaServ.getOpenedFile(), parentsMap);
				
				//updateOrCreate(viewArea, javaServ, action, tree_copy, option);
				viewArea.layout();

			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
		}

	}
	
	
	void clearArea(Composite viewArea){
		Control[] children = viewArea.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		
	}
	
	/**
	 * finds the info of the class opened in the Java Editor
	 * @param javaServ
	 */
	private void getOpennedClassInfo(JavaEditorServices javaServ) {
		parseInfo(javaServ.getOpenedFile(), javaServ, checkClassInfo, openedClassElement);

	}

	/**
	 * 
	 * @param file
	 * @param javaServ
	 * @param visitor
	 * @param element
	 */
	private void parseInfo(File file, JavaEditorServices javaServ, ClassInfoVisitor visitor, ClassTreeElement element) {
		visitor = new ClassInfoVisitor(element);
		if (file != null)
			javaServ.parseFile(file, visitor);
	}

}
