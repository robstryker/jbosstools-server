package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.internal.Base;
import org.eclipse.wst.server.core.internal.IMemento;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipant;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;

public class RuntimePropertyImporter implements IServerImportParticipant {

	public void importFrom(ImportServerUtility utility, IProgressMonitor monitor)
			throws CoreException {
		try {
			String contents = utility.readString("runtime.txt");
			if( contents != null )  {
				IMemento mem = XMLMemento.loadMemento(new ByteArrayInputStream(contents.getBytes()));
				PublicBase b = new PublicBase("runtime");
				b.load(mem);
				String rtType = b.getAttribute("runtime-type-id", (String)null);
				System.out.println(rtType);
			}
			System.out.println(contents);
		} catch(IOException ioe) {
			ioe.printStackTrace(); // TODO 
		}
	}
	
	private static class PublicBase extends Base {
		private String readRoot;
		public PublicBase(String readRoot) {
			super(null);
			this.readRoot = readRoot;
		}
		protected String getXMLRoot() {
			return readRoot;
		}
		
		public void load(IMemento mem) {
			super.load(mem);
		}
		
		
		// Ignored
		protected void saveState(IMemento memento) {
		}
		protected void loadState(IMemento memento) {
		}
		
	}
}
