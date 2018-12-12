package pt.iscte.pidesco.hierarchy.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;
import pt.iscte.pidesco.hierarchy.visitor.ClassInfoVisitor;
import pt.iscte.pidesco.hierarchy.visitor.PackageVisitor;
import pt.iscte.pidesco.demo.extensibility.DemoAction;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement.Visitor;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class TypeHierarchyView implements PidescoView {

	private ClassInfoVisitor checkClassInfo;
	private ClassTreeElement openedClassElement;
	private PackageVisitor packageVisitor;
	private ArrayList<ClassTreeElement> fileList;
	private ArrayList<File> javaFileList;
	private Tree tree;
	/**
	 *familyMap tem o Elemento, e a lista de filhos
	 */
	private HashMap<ClassTreeElement, ClassFamily> familyMap;

	/**
	 * creates type hierarchy main component
	 * 
	 * @param viewArea is the area in which all the components are inserted
	 * @param imageMap
	 */
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.VERTICAL));
		BundleContext context = Activator.getContext();

		ServiceReference<ProjectBrowserServices> serviceReference = context
				.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projServ = context.getService(serviceReference);

		ServiceReference<JavaEditorServices> serviceReference2 = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaServ = context.getService(serviceReference2);

		tree = new Tree(viewArea, SWT.VIRTUAL);
		tree.setSize(500, 500);

		fileList = new ArrayList<ClassTreeElement>();
		javaFileList = new ArrayList<File>();
		openedClassElement = new ClassTreeElement();
		familyMap = new HashMap<>();

		// buildHierarchy(projServ.getRootPackage(),fileList);

		// getHierarchy(javaServ);

		if (javaServ.getOpenedFile() != (null)) {
			// getOpennedClassInfo(javaServ);
			buildHierarchy(projServ.getRootPackage(), javaServ);
			// updateTree(tree, viewArea);

		}

		javaServ.addListener(new JavaEditorListener.Adapter() {
			@Override
			public void fileOpened(File file) {
				/*
				 * tree.removeAll(); openedClassElement.clear(); getOpennedClassInfo(javaServ);
				 * buildHierarchy(projServ.getRootPackage(), javaServ);
				 */
				// updateTree(tree, viewArea);

			}
		});

		projServ.addListener(new ProjectBrowserListener.Adapter() {
			@Override
			public void doubleClick(SourceElement element) {

			}

		});

		// extensao(viewArea);

	}

	private void buildHierarchy(PackageElement rootPackage, JavaEditorServices javaServ) {
		packageVisitor = new PackageVisitor(fileList, javaFileList, tree);
		rootPackage.traverse(packageVisitor);
		buildTree(javaServ);

	}

	private void buildTree(JavaEditorServices javaServ) {

		for (ClassTreeElement classTreeElement : fileList) {
			findparents(javaFileList.get(fileList.indexOf(classTreeElement)), javaServ, classTreeElement);

			if (classTreeElement.getElementName().equals(openedClassElement.getParent().getElementName())) {
				//System.out.println("lista " + classTreeElement.getElementName());
				TreeItem treeItem0 = new TreeItem(tree, 0);
				treeItem0.setText(classTreeElement.getElementName());
				TreeItem treeItem2 = new TreeItem(treeItem0, 0);
				treeItem2.setText(openedClassElement.getCompleteName());
				
			}

		}

		for (TreeItem item : tree.getItems()) {
			item.setExpanded(true);
		}

	}

	private void findparents(File file, JavaEditorServices javaServ, ClassTreeElement element) {
		// for (File file: javaFileList) {
		// System.out.println(file.getName());

		parseInfo(file, javaServ, new ClassInfoVisitor(element), element);

		TreeItem item0 = new TreeItem(tree, 0);
		item0.setText(element.getParent().getElementName());
		// element.setTreeItem(item0);
		TreeItem item1 = new TreeItem(item0, 0);
		item1.setText(element.getElementName());
		element.setTreeItem(item1);
		findChildren(element);
		fillTreeChildren(element, item1);
		//System.out.println("aaaaa  " + element.getElementName());
		//System.out.println("bbbbb  " + element.getParent().getElementName());

		// }
	}

	private void findChildren(ClassTreeElement element) {
		for (ClassTreeElement classTreeElement : fileList) {
			if (element.getElementName().equals(classTreeElement.getParent().getElementName())) {
				element.addChild(classTreeElement);
				familyMap.put(element, new ClassFamily(element.getChildren(), element.getParent()));
			}
		}

	}

	private void fillTreeChildren(ClassTreeElement element, TreeItem itemparent) {
		for (ClassTreeElement child : element.getChildren()) {
			TreeItem item2 = new TreeItem(itemparent, 0);
			item2.setText(child.getElementName());
			child.setTreeItem(item2);
			fillTreeChildren(child, item2);
		}
		// fillTreeChildren(element, itemparent);
	}

	private void organizeTree(List<ClassTreeElement> sorted, ClassTreeElement parent) {
		
		for (ClassTreeElement element : familyMap.keySet()) {
			
		}

	}

	private void extensao(Composite viewArea) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.hierarchy.actions");
		for (IConfigurationElement e : elements) {
			String name = e.getAttribute("name");
			Button b = new Button(viewArea, SWT.PUSH);
			b.setText(name);
			try {
				DemoAction action = (DemoAction) e.createExecutableExtension("class");
				b.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						action.run(viewArea);
						viewArea.layout();
					}

				});
			} catch (CoreException e1) {
				e1.printStackTrace();
			}

		}

	}

	/*
	 * private void getHierarchy(JavaEditorServices javaServ) { IFile myJavaFile =
	 * (IFile) javaServ.getOpenedFile(); ICompilationUnit unit =
	 * JavaCore.createCompilationUnitFrom(myJavaFile); IType[] types; try { types =
	 * unit.getAllTypes(); for (IType type : types) { ITypeHierarchy th=
	 * type.newTypeHierarchy(null); System.out.println("type h test: "+
	 * th.getSuperclass(type).toString()); // do something with the hierarchy } }
	 * catch (JavaModelException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

	private void updateTree(Tree tree, Composite viewArea) {

		if (openedClassElement.getParent() != null) {
			TreeItem treeItem0 = new TreeItem(tree, 0);
			treeItem0.setText(openedClassElement.getParent().getCompleteName());

			TreeItem treeItem1 = new TreeItem(treeItem0, 0);
			treeItem1.setText(openedClassElement.getCompleteName());
		} else {
			TreeItem treeItem1 = new TreeItem(tree, 0);
			treeItem1.setText(openedClassElement.getCompleteName());
		}

		TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			item.setExpanded(true);
		}

		// treeItem0.setText(baseClassElement.getName());

		viewArea.layout();

	}

	private void getOpennedClassInfo(JavaEditorServices javaServ) {
		// System.out.println("getClassInfo.............. " + path);
		// checkClassInfo = new ClassInfoVisitor(baseClassElement);
		// javaServ.parseFile(javaServ.getOpenedFile(), checkClassInfo);
		parseInfo(javaServ.getOpenedFile(), javaServ, checkClassInfo, openedClassElement);

	}

	private void parseInfo(File file, JavaEditorServices javaServ, ClassInfoVisitor visitor, ClassTreeElement element) {
		visitor = new ClassInfoVisitor(element);
		javaServ.parseFile(file, visitor);
	}

}
