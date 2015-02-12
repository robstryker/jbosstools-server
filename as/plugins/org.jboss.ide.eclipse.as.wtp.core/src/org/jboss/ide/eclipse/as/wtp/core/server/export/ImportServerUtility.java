package org.jboss.ide.eclipse.as.wtp.core.server.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.jboss.ide.eclipse.archives.core.util.internal.TrueZipUtil;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.ImportExportExtensionManager;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.RuntimePropertyImporter;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.ServerLaunchPropertyImporter;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.ServerPropertyImporter;

public class ImportServerUtility {
	/**
	 * Key for shared data where the server will be stored
	 */
	public static final String SERVER_KEY = "server";
	/**
	 * Key for shared data where the runtime will be stored
	 */
	public static final String RUNTIME_KEY = "runtime";
	
	
	private File jar;
	
	private HashMap<String, Object> sharedData;
	
	public ImportServerUtility(File f) {
		this.jar = f;
		sharedData = new HashMap<String, Object>();
	}
	public boolean importServer(IProgressMonitor monitor) {
		IServerImportParticipantDelegate[] all = getParticipants();
		monitor.beginTask("Importing Server from file: " + jar.getName(), all.length * 100);
		try {
			for( int i = 0; i < all.length; i++ ) {
				SubProgressMonitor mon = new SubProgressMonitor(monitor, 100);
				all[i].importFrom(this, mon);
				mon.done();
			}
		} catch(CoreException ce) {
			// TODO handle
		}
		return true;
	}
	
	private IServerImportParticipant[] getParticipants() {
		return ImportExportExtensionManager.getDefault().getImportParticipants();
//		return new IServerImportParticipant[] {
//				new RuntimePropertyImporter(), 
//				new ServerPropertyImporter(), 
//				new ServerLaunchPropertyImporter()
//		};
	}
	
	
	public Object getSharedData(String key) {
		return sharedData.get(key);
	}
	
	public void putSharedData(String key, Object val) {
		sharedData.put(key, val);
	}
	
	public byte[] readBytes(String path) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		read(path, os);
		return os.toByteArray();
	}

	public String readString(String path) throws IOException {
		return new String(readBytes(path));
	}
	
	public OutputStream read(String path) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		read(path, os);
		return os;
	}
	
	public void read(String path, OutputStream os) throws IOException {
		File jar = TrueZipUtil.getFile(new Path(this.jar.getAbsolutePath()), TrueZipUtil.getJarArchiveDetector());
		de.schlichtherle.io.File innerFile = TrueZipUtil.getFileInArchive((de.schlichtherle.io.File)jar, new Path(path));
		TrueZipUtil.readFile(os, innerFile);
	}
}
