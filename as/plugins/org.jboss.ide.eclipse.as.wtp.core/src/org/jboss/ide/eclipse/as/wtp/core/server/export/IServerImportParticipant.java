package org.jboss.ide.eclipse.as.wtp.core.server.export;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IServer;

public interface IServerImportParticipant {
	public void importFrom(ImportServerUtility utility, 
			IProgressMonitor monitor) throws CoreException;
}
