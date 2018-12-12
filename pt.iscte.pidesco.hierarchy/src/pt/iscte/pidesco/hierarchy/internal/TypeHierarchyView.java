package pt.iscte.pidesco.hierarchy.internal;

import java.io.File;
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
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class TypeHierarchyView implements PidescoView {

	private ClassInfoVisitor checkClassInfo;
	private ClassTreeElement baseClassElement;
	private PackageVisitor packageVisitor;

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

		Tree tree = new Tree(viewArea, SWT.VIRTUAL);
		tree.setSize(500, 500);

		baseClassElement = new ClassTreeElement();

		packageVisitor = new PackageVisitor();
		//PackageElement root = s.getRootPackage();
		//root.traverse(new Visitor() {...});

		// getHierarchy(javaServ);

		

		if (javaServ.getOpenedFile() != (null)) {
			getOpennedClassInfo(javaServ.getOpenedFile().getPath(), javaServ);
			updateTree(tree, viewArea);

		}

		javaServ.addListener(new JavaEditorListener.Adapter() {
			@Override
			public void fileOpened(File file) {
				tree.removeAll();
				baseClassElement.clear();
				getOpennedClassInfo(javaServ.getOpenedFile().getPath(), javaServ);
				updateTree(tree, viewArea);
				

			}

		});

		projServ.addListener(new ProjectBrowserListener.Adapter() {
			@Override
			public void doubleClick(SourceElement element) {
					
			}

		});
		
		
		
		extensao(viewArea);

	}

	private void extensao(Composite viewArea) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.hierarchy.actions");
		for(IConfigurationElement e : elements) {
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
			
		
		if (baseClassElement.getParent()!=null) {
			TreeItem treeItem0 = new TreeItem(tree, 0);
			treeItem0.setText(baseClassElement.getParent().getName());
			
			
			TreeItem treeItem1 = new TreeItem(treeItem0, 0);
			treeItem1.setText(baseClassElement.getName());
		}else {
			TreeItem treeItem1 = new TreeItem(tree, 0);
			treeItem1.setText(baseClassElement.getName());
		}

		TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			item.setExpanded(true);
		}

		// treeItem0.setText(baseClassElement.getName());

		viewArea.layout();

	}

	/*private class CheckClassInfo extends ASTVisitor {

		// visits class/interface declaration
		// Names of classes/interfaces must start with an
		// uppercase letter and cannot have underscores;
		@Override
		public boolean visit(PackageDeclaration node) {
			String name = node.getName().toString();
			System.out.println("Package " + name);
			baseClassElement.setPackageName(name);

			return super.visit(node);
		}

		@Override
		public boolean visit(TypeDeclaration node) {
			String name = node.getName().toString();
			System.out.println("Class " + name);
			System.out.println("Class " + node.getSuperclassType());
			
			String file = node.getParent().toString();
			if (file.contains("extends")) {
				String[] splittedFile = file.split("extends ");
				String[] superclass = splittedFile[1].split("\\{");
				String superName = superclass[0].trim();
				System.out.println("Super classeeee " + superName + "  . ");
				ClassTreeElement parent = new ClassTreeElement();
				parent.setClassName(superName);
				baseClassElement.setParent(parent);
			}
			baseClassElement.setClassName(name);

			return true;
		}

		@Override
		public boolean visit(CompilationUnit node) {
			// System.out.println("entrou aqui " + node.getPackage());
			// for (Object type :unit.types()){

			
			 * TypeDeclaration typeDec = (TypeDeclaration) type; Type superClassType =
			 * typeDec.getSuperclassType(); TypeDeclaration superClazz; if
			 * (superClassType.equals(Object.class.getSimpleName())){ return continue;
			 * }else{ depthOfInheritanceTreeIndex++; superClazz = (TypeDeclaration)
			 * superClassType.getParent(); return super.visit(superClazz); }
			 
			// }/*

			return true;
		}

		@Override
		public boolean visit(ClassInstanceCreation node) {

			return true;
		}

	}*/

	private void getOpennedClassInfo(String path, JavaEditorServices javaServ) {
		System.out.println("getClassInfo.............. " + path);
		checkClassInfo = new ClassInfoVisitor(baseClassElement);
		javaServ.parseFile(javaServ.getOpenedFile(), checkClassInfo);

	}

}
