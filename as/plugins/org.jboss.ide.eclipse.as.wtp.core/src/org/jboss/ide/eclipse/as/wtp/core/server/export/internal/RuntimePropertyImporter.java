package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Base;
import org.eclipse.wst.server.core.internal.IMemento;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;
import org.eclipse.wst.server.core.internal.XMLMemento;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipant;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;

public class RuntimePropertyImporter implements IServerImportParticipant {

	public void importFrom(ImportServerUtility utility, IProgressMonitor monitor)
			throws CoreException {
		try {
			String contents = utility.readString("runtime.txt");
			System.out.println(contents);
			if( contents != null )  {
				IMemento mem = XMLMemento.loadMemento(new ByteArrayInputStream(contents.getBytes()));
				PublicBase b = new PublicBase("runtime");
				b.load(mem);
				String rtType = b.getAttribute("runtime-type-id", (String)null);
				System.out.println(rtType);
				IRuntimeType rtt = ServerCore.findRuntimeType(rtType);
				IRuntimeWorkingCopy rtwc = rtt.createRuntime(b.getAttribute("id", (String)null), monitor);
				
				Set<String> keys = b.getKeys();
				Iterator<String> i = keys.iterator();
				while(i.hasNext()) {
					String k = i.next();
					b.copyKeyTo(rtwc, k);
				}
				IRuntime rtFinal = rtwc.save(true, new NullProgressMonitor());
				utility.putSharedData(ImportServerUtility.RUNTIME_KEY, rtFinal);
			}
		} catch(IOException ioe) {
			ioe.printStackTrace(); // TODO 
		}
	}
	
	static class PublicBase extends Base {
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
		
		public Set<String> getKeys() {
			return map.keySet();
		}
		
		public void copyKeyTo(IRuntimeWorkingCopy rtwc, String k) {
			try {
				Object o = map.get(k);
				if( o != null ) {
					if( o instanceof Map ) {
						((RuntimeWorkingCopy)rtwc).setAttribute(k, (Map)o);
					} else if( o instanceof List) {
						((RuntimeWorkingCopy)rtwc).setAttribute(k, (List)o);
					} else if( o instanceof String) {
						((RuntimeWorkingCopy)rtwc).setAttribute(k, (String)o);
					}
					// Int and Bool are stored as strings. 
				}
			} catch(IllegalArgumentException iae) {
				// Ignore, read-only attribute
			}
		}
		
		public void copyKeyTo(IServerWorkingCopy rtwc, String k) {
			try {
				Object o = map.get(k);
				if( o != null ) {
					if( o instanceof Map ) {
						((IServerWorkingCopy)rtwc).setAttribute(k, (Map)o);
					} else if( o instanceof List) {
						((IServerWorkingCopy)rtwc).setAttribute(k, (List)o);
					} else if( o instanceof String) {
						((IServerWorkingCopy)rtwc).setAttribute(k, (String)o);
					}
					// Int and Bool are stored as strings. 
				}
			} catch(IllegalArgumentException iae) {
				// Ignore, probably 'timestamp' property
			}
		}

		
		// Ignored
		protected void saveState(IMemento memento) {
		}
		protected void loadState(IMemento memento) {
		}
		
	}
}
