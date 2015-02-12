package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.IMemento;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipantDelegate;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;
import org.jboss.ide.eclipse.as.wtp.core.server.export.internal.RuntimePropertyImporter.PublicBase;

public class ServerPropertyImporter implements IServerImportParticipantDelegate {

	public void importFrom(ImportServerUtility utility, IProgressMonitor monitor)
			throws CoreException {
		try {
			String contents = utility.readString("server.txt");
			System.out.println(contents);
			if( contents != null )  {
				IMemento mem = XMLMemento.loadMemento(new ByteArrayInputStream(contents.getBytes()));
				PublicBase b = new PublicBase("server");
				b.load(mem);
				String serverType = b.getAttribute("server-type-id", (String)null);
				System.out.println(serverType);
				IServerType st = ServerCore.findServerType(serverType);
				IServerWorkingCopy swc = st.createServer(b.getAttribute("id", (String)null), null, monitor);
				
				Set<String> keys = b.getKeys();
				Iterator<String> i = keys.iterator();
				while(i.hasNext()) {
					String k = i.next();
					b.copyKeyTo(swc, k);
				}
				IServer serverFinal = swc.save(true, new NullProgressMonitor());
				utility.putSharedData(ImportServerUtility.SERVER_KEY, serverFinal);
			}
		} catch(IOException ioe) {
			ioe.printStackTrace(); // TODO 
		}
	}
	
}
