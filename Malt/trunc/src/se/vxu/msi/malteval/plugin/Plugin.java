package se.vxu.msi.malteval.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * The class Plugin contains information about a plug-in that comply to the the MaltParser Plugin Standard.
 * 
 * @author Johan Hall, modified by Jens Nilsson
 * @since 1.0
 */
public class Plugin {
	private JarFile archive;
	private URL url;
	private String pluginName;

	/**
	 * Creates a plug-in container.
	 * 
	 * @param filename
	 *            The file name that contains the plugin
	 * @throws MaltEvalException
	 */
	public Plugin(String filename) throws MaltEvalException {
		this(new File(filename));
	}

	/**
	 * Creates a plug-in container.
	 * 
	 * @param file
	 *            The jar file that contains the plugin
	 * @throws MaltEvalException
	 */
	public Plugin(File file) throws MaltEvalException {
		try {
			setArchive(new JarFile(file));
			setUrl(new URL("file", null, file.getAbsolutePath()));
			register();
		} catch (FileNotFoundException e) {
			throw new MaltEvalException("The file '" + file.getPath() + File.separator + file.getName() + "' cannot be found. ", this
					.getClass());
		} catch (MalformedURLException e) {
			throw new MaltEvalException("Malformed URL to the jar file '" + archive.getName() + "'. ", this.getClass());
		} catch (IOException e) {
			throw new MaltEvalException("The jar file '" + file.getPath() + File.separator + file.getName() + "' cannot be initialized. ",
					this.getClass());
		}
	}

	/**
	 * @throws MaltEvalException
	 */
	private void register() throws MaltEvalException {
		try {
			Attributes atts = archive.getManifest().getMainAttributes();
			pluginName = atts.getValue("Plugin-Name");
			if (pluginName == null) {
				pluginName = archive.getName();
			}
		} catch (IOException e) {
			throw new MaltEvalException("Could not get the 'Plugin-Name' in the manifest for the plugin (jar-file). ", this.getClass());
		}
	}

	/**
	 * Returns the archive.
	 * 
	 * @return the jar archive.
	 */
	public JarFile getArchive() {
		return archive;
	}

	/**
	 * Sets the archive to set.
	 * 
	 * @param archive
	 *            The archive to set.
	 */
	public void setArchive(JarFile archive) {
		this.archive = archive;
	}

	/**
	 * Returns the plug-in name.
	 * 
	 * @return the plug-in name.
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * Sets the plug-in name
	 * 
	 * @param pluginName
	 *            the plug-in name
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	/**
	 * Returns the URL
	 * 
	 * @return the URL
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Sets the URL.
	 * 
	 * @param url
	 *            the URL
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(pluginName + ": " + url.toString() + "\n");
		return sb.toString();
	}
}