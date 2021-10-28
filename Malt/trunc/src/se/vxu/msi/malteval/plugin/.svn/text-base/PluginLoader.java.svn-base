package se.vxu.msi.malteval.plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * Loads MaltParser plug-ins and makes new instances of classes within these plug-ins.
 * 
 * @author Johan Hall, modified by Jens Nilsson
 * @since 1.0
 */
public class PluginLoader {
	private HashMap<String, Plugin> plugins;
	private TreeSet<String> pluginNames;
	private File[] directories;
	private JarLoader jarLoader;
	private String classPath_;

	/**
	 * Creates a PluginLoader
	 * 
	 * @throws PluginException
	 */
	public PluginLoader() {
		pluginNames = new TreeSet<String>();
		plugins = new HashMap<String, Plugin>();
		jarLoader = null;
		classPath_ = null;
	}

	/**
	 * Loads plug-ins from one directory
	 * 
	 * @param pluginDirectory
	 *            The directory that contains all plug-ins
	 * @throws MaltEvalException
	 */
	public void loadPlugins(File pluginDirectory) throws MaltEvalException {
		this.loadPlugins(new File[] {pluginDirectory});
	}

	/**
	 * Loads plug-ins from one directory
	 * 
	 * @param pluginDirectory
	 *            The directory that contains all plug-ins
	 * @throws MaltEvalException
	 */
	public void loadPlugins(File pluginDirectory, String classPath) throws MaltEvalException {
		classPath_ = classPath;
		this.loadPlugins(new File[] {pluginDirectory});
	}

	/**
	 * Loads plug-ins from one or more directories
	 * 
	 * @param pluginDirectories
	 *            An array of directories that contains all plug-ins
	 * @throws MaltEvalException
	 */
	public void loadPlugins(File[] pluginDirectories) throws MaltEvalException {
		directories = new File[pluginDirectories.length];
		for (int i = 0; i < directories.length; i++) {
			directories[i] = pluginDirectories[i];
		}

		try {
			Class<?> self = Class.forName("se.vxu.msi.malteval.plugin.PluginLoader");
			jarLoader = new JarLoader(self.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new MaltEvalException("The class \'se.vxu.msi.malteval.plugin.PluginLoader\' not found. ", this.getClass());
		}
		traverseDirectories();
	}

	/**
	 * Traverse all the plug-in directories
	 * 
	 * @throws MaltEvalException
	 */
	private void traverseDirectories() throws MaltEvalException {
		for (int i = 0; i < directories.length; i++) {
			traverseDirectory(directories[i]);
		}
	}

	/**
	 * Traverse all plug-ins and sub-directories within one plug-in directory.
	 * 
	 * @param directory
	 *            The directory that contains plug-ins
	 * @throws MaltEvalException
	 */
	private void traverseDirectory(File directory) throws MaltEvalException {
		if (!directory.isDirectory() && directory.getName().endsWith(".jar")) {
			pluginNames.add(directory.getAbsolutePath());
			Plugin plugin = new Plugin(directory);
			plugins.put(directory.getAbsolutePath(), plugin);
			jarLoader.readJarFile(plugin.getUrl(), classPath_);
		}

		if (directory.isDirectory()) {
			String[] children = directory.list();
			for (int i = 0; i < children.length; i++) {
				traverseDirectory(new File(directory, children[i]));
			}
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
		if (jarLoader != null) {
			return jarLoader.getClass(classname);
		} else {
			return null;
		}
	}

	/**
	 * Creates a new instance of a class within one of the plug-ins
	 * 
	 * @param classname
	 *            The fully qualified name of the desired class
	 * @param argTypes
	 *            An array of classes (fully qualified name) that specify the arguments to the constructor
	 * @param args
	 *            An array of objects that will be the actual parameters to the constructor (the type should corresponds to the argTypes).
	 * @return a reference to the created instance.
	 * @throws MaltEvalException
	 */
	public Object newInstance(String classname, Class<?>[] argTypes, Object[] args) throws MaltEvalException {
		try {
			if (jarLoader == null) {
				return null;
			}
			Class<?> clazz = jarLoader.getClass(classname);
			Object o = null;
			if (clazz == null)
				return null;
			if (argTypes != null) {
				Constructor<?> constructor = clazz.getConstructor(argTypes);
				o = constructor.newInstance(args);
			} else {
				o = clazz.newInstance();
			}
			return o;
		} catch (NoSuchMethodException e) {
			throw new MaltEvalException("The plugin loader was not able to create an instance of the class '" + classname + "'.\n"
					+ e.getMessage(), this.getClass());
		} catch (InstantiationException e) {
			throw new MaltEvalException("The plugin loader was not able to create an instance of the class '" + classname + "'.\n"
					+ e.getMessage(), this.getClass());
		} catch (IllegalAccessException e) {
			throw new MaltEvalException("The plugin loader was not able to create an instance of the class '" + classname + "'.\n"
					+ e.getMessage(), this.getClass());
		} catch (InvocationTargetException e) {
			throw new MaltEvalException("The plugin loader was not able to create an instance of the class '" + classname + "'.\n"
					+ e.getMessage(), this.getClass());
		}
	}

	public Set<String> getClassNames() {
		return jarLoader.getAllClassNames();
	}
}
