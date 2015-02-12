package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.debug.internal.core.LaunchManager;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.wtp.core.ASWTPToolsPlugin;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipantDelegate;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;

public class ServerLaunchPropertyImporter implements IServerImportParticipantDelegate {

	public void importFrom(ImportServerUtility utility, IProgressMonitor monitor)
			throws CoreException {
		try {
			String contents = utility.readString("launch.txt");
			System.out.println(contents);
			if( contents != null )  {
				IServer s = (IServer)utility.getSharedData(utility.SERVER_KEY);
				
				// Get the name for a temporary .launch file to write out to
				ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
				String launchName = getValidLaunchConfigurationName(s.getName());
				launchName = launchManager.generateUniqueLaunchConfigurationNameFrom(launchName); 
				String launchFileName = launchName + ".launch";
				
				// persist this to a temporary file
				IPath p = ASWTPToolsPlugin.getDefault().getStateLocation().append(".import").append(".launch");
				p.toFile().mkdirs();
				IPath filePath = p.append(launchFileName);
				File file =filePath.toFile();
				OutputStream os = new FileOutputStream(file);
				try {
					os.write(contents.getBytes());
				} finally {
					os.close();
				}
				
				// Instruct the launch manager to import it
				((LaunchManager)launchManager).importConfigurations(new File[]{file}, new NullProgressMonitor());
				
			}
		} catch(IOException ioe) {
			ioe.printStackTrace(); // TODO 
		}
	}
	
	protected static final char[] INVALID_CHARS = new char[] {'\\', '/', ':', '*', '?', '"', '<', '>', '|', '\0', '@', '&'};
	protected String getValidLaunchConfigurationName(String s) {
		if (s == null || s.length() == 0)
			return "1";
		int size = INVALID_CHARS.length;
		for (int i = 0; i < size; i++) {
			s = s.replace(INVALID_CHARS[i], '_');
		}
		return s;
	}

}
