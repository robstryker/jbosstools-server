package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.debug.internal.core.LaunchConfigurationInfo;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.ServerType;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipant;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;

public class ServerLaunchPropertyImporter implements IServerImportParticipant {

	public void importFrom(ImportServerUtility utility, IProgressMonitor monitor)
			throws CoreException {
		try {
			String contents = utility.readString("launch.txt");
			System.out.println(contents);
			if( contents != null )  {
				IServer s = (IServer)utility.getSharedData(utility.SERVER_KEY);
				
				// Create launch config
				ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
				ILaunchConfiguration lc = s.getLaunchConfiguration(true, new NullProgressMonitor());
				IFileStore fs = ((LaunchConfiguration)lc).getFileStore();
				lc.delete();
				
				
				// write the file
				OutputStream os = fs.openOutputStream(0, new NullProgressMonitor());
				try {
					os.write(contents.getBytes());
				} finally {
					os.close();
				}
				
				lc.hasAttribute("test");
				
				
				//s.getLaunchConfiguration(true, new NullProgressMonitor());
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
