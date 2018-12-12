package pt.iscte.pidesco.hierarchy.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.hierarchy.model.ClassTreeElement;

public class ClassInfoVisitor extends ASTVisitor {

	ClassTreeElement cElement;

	public ClassInfoVisitor(ClassTreeElement cElement) {
		this.cElement = cElement;
	}

	// visits class/interface declaration
	// Names of classes/interfaces must start with an
	// uppercase letter and cannot have underscores;
	@Override
	public boolean visit(PackageDeclaration node) {
		String name = node.getName().toString();
		// System.out.println("Package " + name);
		cElement.setPackageName(name);

		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		String name = node.getName().toString();
		if (node.getSuperclassType() != null) {
			String superName = node.getSuperclassType().toString();

			// System.out.println("Class " + name);
			// System.out.println("Super Class " + node.getSuperclassType().toString());
			ClassTreeElement parent = new ClassTreeElement();
			parent.setClassName(superName);
			// parent.setPackageName(node.getSuperclassType().getP);
			cElement.setParent(parent);
		}
		// cElement.setPackageName(node.getSuperclassType().getP);
		cElement.setClassName(name);

		findHierarchy();

		return true;
	}

	private void findHierarchy() {
		// TODO Auto-generated method stub

	}

	// visit

	@Override
	public boolean visit(CompilationUnit node) {
		// System.out.println("entrou aqui " + node.getPackage());
		// for (Object type :unit.types()){

		/*
		 * TypeDeclaration typeDec = (TypeDeclaration) type; Type superClassType =
		 * typeDec.getSuperclassType(); TypeDeclaration superClazz; if
		 * (superClassType.equals(Object.class.getSimpleName())){ return continue;
		 * }else{ depthOfInheritanceTreeIndex++; superClazz = (TypeDeclaration)
		 * superClassType.getParent(); return super.visit(superClazz); }
		 */
		// }

		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {

		return true;
	}

}
