package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.Server;
import org.eclipse.wst.server.core.internal.ServerWorkingCopy;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ExportServerUtility;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerExportParticipant;

public class ServerPropertyExporter implements IServerExportParticipant {
	public void export(IServer server, ExportServerUtility utility, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Saving server properties", 100);
		try {
			XMLMemento memento = new CustomServerWorkingCopy((Server) server).asMemento();
			InputStream in = memento.getInputStream();
			byte[] all = getBytesFromInputStream(in);
			utility.write("server.txt", all);
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
