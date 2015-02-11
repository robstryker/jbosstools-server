package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.Server;
import org.eclipse.wst.server.core.internal.ServerWorkingCopy;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ExportServerUtility;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerExportParticipant;

public class ServerLaunchPropertyExporter implements IServerExportParticipant {
	public void export(IServer server, ExportServerUtility utility, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Saving server launch configuration", 100);
		try {
			ILaunchConfiguration config = server.getLaunchConfiguration(true, new SubProgressMonitor(monitor, 10));
			if( config != null ) {
				IFileStore fs = ((LaunchConfiguration)config).getFileStore();
				if( fs != null ) {
					InputStream is = fs.openInputStream(EFS.NONE, new SubProgressMonitor(monitor, 10));
					utility.write("launch.txt", is);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			monitor.done();
		}
	}

	static byte[] getBytesFromInputStream(InputStream is) {
		if (is == null)
			return null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			return buffer.toByteArray();
		} catch (IOException ioe) {
			return null;
		}
	}

	private class CustomServerWorkingCopy extends ServerWorkingCopy {

		public CustomServerWorkingCopy(Server server) {
			super(server);
		}

		public XMLMemento asMemento() {
			XMLMemento memento = XMLMemento.createWriteRoot(getXMLRoot());
			save(memento);
			return memento;
		}
	}
}
