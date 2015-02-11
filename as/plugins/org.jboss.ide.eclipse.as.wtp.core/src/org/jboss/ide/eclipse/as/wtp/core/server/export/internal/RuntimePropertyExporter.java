package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.Runtime;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ExportServerUtility;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerExportParticipant;

public class RuntimePropertyExporter implements IServerExportParticipant {
	public void export(IServer server, ExportServerUtility utility, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Saving runtime properties", 100);
		try {
			IRuntime rt = server.getRuntime();
			if( rt != null ) {
				XMLMemento memento = new CustomRuntimerWorkingCopy((Runtime)rt).asMemento();
				InputStream in = memento.getInputStream();
				byte[] all = ServerPropertyExporter.getBytesFromInputStream(in);
				utility.write("runtime.txt", all);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			monitor.done();
		}
	}

	private class CustomRuntimerWorkingCopy extends RuntimeWorkingCopy {

		public CustomRuntimerWorkingCopy(Runtime runtime) {
			super(runtime);
		}

		public XMLMemento asMemento() {
			XMLMemento memento = XMLMemento.createWriteRoot(getXMLRoot());
			save(memento);
			return memento;
		}
	}
}
