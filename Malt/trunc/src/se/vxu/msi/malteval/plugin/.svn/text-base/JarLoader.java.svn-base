package se.vxu.msi.malteval.plugin;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * The jar class loader loads the content of a jar file that complies with a MaltParser Plugin.
 * 
 * @author Johan Hall
 * @since 1.0
 */
public class JarLoader extends SecureClassLoader {
	private HashMap<String, byte[]> classByteArrays;
	private HashMap<String, Class<?>> classes;
	
	private String classPath_;

	/**
	 * Creates a new class loader that is specialized for loading jar files.
	 * 
	 * @param parent
	 *            The parent class loader
	 */
	public JarLoader(ClassLoader parent) {
		super(parent);
		classByteArrays = new HashMap<String, byte[]>();
		classes = new HashMap<String, Class<?>>();
		classPath_ = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	protected Class<?> findClass(String name) {
		String urlName = name.replace('.', '/');
		byte buf[];

		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			int i = name.lastIndexOf('.');
			if (i >= 0) {
				sm.checkPackageDefinition(name.substring(0, i));
			}
		} else {
			/*
			 * if (logger.isDebugEnabled()) { logger.debug("JarLoader: The SecurityManager cannot be found. \n"); }
			 */
		}

		buf = (byte[]) classByteArrays.get(urlName);
		if (buf != null) {
			return defineClass(null, buf, 0, buf.length);
		} else {
			// if (logger.isDebugEnabled()) {
			// logger.debug("JarLoader: The Byte array is null. \n");
			// }
		}
		return null;
	}

	public void readJarFile(URL jarUrl, String classPath) throws MaltEvalException {
		classPath_ = classPath;
		readJarFile(jarUrl);
	}

		
		/**
	 * Loads the content of a jar file that comply with a MaltParser Plugin
	 * 
	 * @param jarUrl
	 *            The URL to the jar file
	 * @throws PluginException
	 */
	public void readJarFile(URL jarUrl) throws MaltEvalException {
		JarInputStream jis;
		JarEntry je;
		HashSet<URL> pluginXMLs = new HashSet<URL>();

		/*
		 * if (logger.isDebugEnabled()) { logger.debug("Loading jar " + jarUrl+"\n"); }
		 */

		try {
			jis = new JarInputStream(jarUrl.openConnection().getInputStream());

			while ((je = jis.getNextJarEntry()) != null) {
				String jarName = je.getName();
				if (jarName.endsWith(".class") && (classPath_ == null || jarName.startsWith(classPath_))) {
					/*
					 * if (logger.isDebugEnabled()) { logger.debug(" Loading class: " + jarName+"\n"); }
					 */
					loadClassBytes(jis, jarName);
					Class<?> clazz = findClass(jarName.substring(0, jarName.length() - 6));
					classes.put(jarName.substring(0, jarName.length() - 6).replace('/', '.'), clazz);
					loadClass(jarName.substring(0, jarName.length() - 6).replace('/', '.'));
				}
				if (jarName.endsWith("plugin.xml")) {
					pluginXMLs.add(new URL("jar:" + jarUrl.getProtocol() + ":/" + jarUrl.getPath() + "!/" + jarName));
				}
				jis.closeEntry();
			}
			// for (URL url : pluginXMLs) {
			// /* if (logger.isDebugEnabled()) {
			// logger.debug(" Loading "+url+"\n");
			// }*/
			// MaltConsoleEngine.getOptionManager().parseOptionDescriptionXMLfile(url);
			// }
		} catch (MalformedURLException e) {
			throw new MaltEvalException("The URL to the plugin.xml is wrong. ", this.getClass());
		} catch (IOException e) {
			throw new MaltEvalException("cannot open jar file " + jarUrl + ". ", this.getClass());
		} catch (ClassNotFoundException e) {
			throw new MaltEvalException("The class " + e.getMessage() + " can't be found. ", this.getClass());
		}
	}

	/**
	 * Returns the Class object for the class with the specified name.
	 * 
	 * @param classname
	 *            the fully qualified name of the desired class
	 * @return the Class object for the class with the specified name.
	 */
	public Class<?> getClass(String classname) {
		return (Class<?>) classes.get(classname);
	}

	/**
	 * Returns a set containing all class names found in the jar-file.
	 * @return a set containing all class names found in the jar-file
	 */
	public Set<String> getAllClassNames() {
		return classes.keySet();
	}

	/**
	 * Reads a jar file entry into a byte array.
	 * 
	 * @param jis
	 *            The jar input stream
	 * @param jarName
	 *            The name of a jar file entry
	 * @throws PluginException
	 */
	private void loadClassBytes(JarInputStream jis, String jarName) throws MaltEvalException {
		BufferedInputStream jarBuf = new BufferedInputStream(jis);
		ByteArrayOutputStream jarOut = new ByteArrayOutputStream();
		int b;
		try {
			while ((b = jarBuf.read()) != -1) {
				jarOut.write(b);
			}
			classByteArrays.put(jarName.substring(0, jarName.length() - 6), jarOut.toByteArray());
		} catch (IOException e) {
			throw new MaltEvalException("Error reading entry " + jarName + ". ", this.getClass());
		}
	}

	/**
	 * Checks package access
	 * 
	 * @param name
	 *            the package name
	 */
	protected void checkPackageAccess(String name) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPackageAccess(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("The MaltParser Plugin Loader (JarLoader)\n");
		sb.append("---------------------------------------------------------------------\n");
		for (String entry : new TreeSet<String>(classes.keySet())) {
			sb.append("   " + entry + "\n");
		}
		return sb.toString();
	}
}
