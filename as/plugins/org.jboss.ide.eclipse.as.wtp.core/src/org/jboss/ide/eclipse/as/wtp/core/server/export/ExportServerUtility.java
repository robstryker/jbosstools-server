package org.jboss.ide.eclipse.as.wtp.core.server.export;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.archives.core.util.internal.TrueZipUtil;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.RuntimePropertyExporter;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.ServerLaunchPropertyExporter;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.ServerPropertyExporter;

public class ExportServerUtility {
	private File output;
	public ExportServerUtility(File f) {
		this.output = f;
	}
	public boolean exportServer(IServer server, IProgressMonitor monitor) {
		output.delete();
		TrueZipUtil.createArchive(new Path(output.getAbsolutePath()));
		IServerExportParticipant[] all = getParticipants();
		monitor.beginTask("Exporting Server: " + server.getName(), all.length * 100);
		try {
			for( int i = 0; i < all.length; i++ ) {
				SubProgressMonitor mon = new SubProgressMonitor(monitor, 100);
				all[i].export(server, this, mon);
				mon.done();
			}
		} catch(CoreException ce) {
			// TODO handle
		}
		TrueZipUtil.umount();
		return true;
	}
	
	private IServerExportParticipant[] getParticipants() {
		return new IServerExportParticipant[] {
				new RuntimePropertyExporter(), new ServerPropertyExporter(), new ServerLaunchPropertyExporter()
		};
	}
	
	public void write(String path, byte[] contents) throws IOException {
		write(path, new ByteArrayInputStream(contents));
	}

	public void write(String path, String contents) throws IOException {
		write(path, new ByteArrayInputStream(contents.getBytes()));
	}
	
	public void write(String path, File f) throws IOException {
		write(path, new FileInputStream(f));
	}
	
	public void write(String path, InputStream is) throws IOException {
		File jar = TrueZipUtil.getFile(new Path(output.getAbsolutePath()), TrueZipUtil.getJarArchiveDetector());
		de.schlichtherle.io.File innerFile = TrueZipUtil.getFileInArchive((de.schlichtherle.io.File)jar, new Path(path));
		TrueZipUtil.writeFile(is, innerFile, true);
	}
}
