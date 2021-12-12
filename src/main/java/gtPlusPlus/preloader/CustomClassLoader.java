package gtPlusPlus.preloader;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class CustomClassLoader extends ClassLoader {

	private HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();

	@Override

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return findClass(name);
	}

	@Override

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		ClassNode node = classes.get(name.replace('.', '/'));
		if (node != null)
			return nodeToClass(node);
		else
			return super.findClass(name);
	}

	public final void addNode(ClassNode node) {
		classes.put(node.name, node);
	}

	private final Class<?> nodeToClass(ClassNode node) {
		if (super.findLoadedClass(node.name) != null)
			return findLoadedClass(node.name);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(cw);
		byte[] b = cw.toByteArray();
		return defineClass(node.name.replace('/', '.'), b, 0, b.length,	getDomain());
	}

	private final ProtectionDomain getDomain() {
		CodeSource code = new CodeSource(null, (Certificate[]) null);
		return new ProtectionDomain(code, getPermissions());
	}

	private final Permissions getPermissions() {
		Permissions permissions = new Permissions();
		permissions.add(new AllPermission());
		return permissions;
	}

}