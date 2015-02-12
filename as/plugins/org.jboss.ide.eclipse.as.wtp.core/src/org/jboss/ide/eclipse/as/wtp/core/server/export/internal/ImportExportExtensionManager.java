package org.jboss.ide.eclipse.as.wtp.core.server.export.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.wtp.core.ASWTPToolsPlugin;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ExportServerUtility;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerExportParticipant;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerExportParticipantDelegate;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipant;
import org.jboss.ide.eclipse.as.wtp.core.server.export.IServerImportParticipantDelegate;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;

public class ImportExportExtensionManager {
	private static ImportExportExtensionManager instance;
	public static synchronized ImportExportExtensionManager getDefault() {
		if( instance == null ) 
			instance = new ImportExportExtensionManager();
		return instance;
	}
	
	ImportExportExtensionManager() {
		load();
	}
	
	private static class ImportWrapper implements IServerImportParticipant {
		private String id; 
		private int weight;
		private IServerImportParticipantDelegate participant;
		private IConfigurationElement element;
		private boolean resolutionFailed = false;
		public ImportWrapper(IConfigurationElement element) {
			this.element = element;
			this.id = element.getAttribute("id");
			try {
				this.weight = Integer.parseInt(element.getAttribute("weight"));
			} catch(NumberFormatException nfe) {
				this.weight = 0;
			}
		}
		public int getWeight() {
			return weight;
		}
		public String getId() {
			return id;
		}
		public IServerImportParticipantDelegate getImportParticipant() {
			if( participant == null && !resolutionFailed) {
				try {
					participant = (IServerImportParticipantDelegate)element.createExecutableExtension("class");
				} catch(CoreException ce) {
					// TODO log
					resolutionFailed = true;
				}
			}
			return participant;
		}
		public void importFrom(ImportServerUtility utility, IProgressMonitor monitor) throws CoreException {
			IServerImportParticipantDelegate del = getImportParticipant();
			if( del != null )
				del.importFrom(utility, monitor);
		}
	}
	
	private static class ExportWrapper implements IServerExportParticipant {
		private String id; 
		private int weight;
		private IServerExportParticipantDelegate participant;
		private IConfigurationElement element;
		private boolean resolutionFailed = false;
		public ExportWrapper(IConfigurationElement element) {
			this.element = element;
			this.id = element.getAttribute("id");
			try {
				this.weight = Integer.parseInt(element.getAttribute("weight"));
			} catch(NumberFormatException nfe) {
				this.weight = 0;
			}
		}
		public int getWeight() {
			return weight;
		}
		public String getId() {
			return id;
		}
		public IServerExportParticipantDelegate getExportParticipant() {
			if( participant == null && !resolutionFailed) {
				try {
					participant = (IServerExportParticipantDelegate)element.createExecutableExtension("class");
				} catch(CoreException ce) {
					// TODO log
					resolutionFailed = true;
				}
			}
			return participant;
		}
		public void export(IServer server, ExportServerUtility utility, IProgressMonitor monitor) throws CoreException {
			IServerExportParticipantDelegate del = getExportParticipant();
			if( del != null ) {
				del.export(server, utility, monitor);
			}
		}
	}
	
	private ArrayList<ImportWrapper>  imports;
	private ArrayList<ExportWrapper>  exports;
	
	private synchronized void load() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				ASWTPToolsPlugin.PLUGIN_ID, "serverImportParticipant"); //$NON-NLS-1$
		ArrayList<ImportWrapper> imports = new ArrayList<ImportWrapper>();
		for( int i = 0; i < cf.length; i++ ) {
			ImportWrapper iw = new ImportWrapper(cf[i]);
			imports.add(iw);
		}

		
		cf = registry.getConfigurationElementsFor(
				ASWTPToolsPlugin.PLUGIN_ID, "serverExportParticipant"); //$NON-NLS-1$
		ArrayList<ExportWrapper> exports = new ArrayList<ExportWrapper>();
		for( int i = 0; i < cf.length; i++ ) {
			ExportWrapper ew = new ExportWrapper(cf[i]);
			exports.add(ew);
		}
		
		// sort
		Collections.sort(exports, new Comparator<ExportWrapper>() {
			public int compare(ExportWrapper o1, ExportWrapper o2) {
				return o2.getWeight() - o1.getWeight();
			}
		});

		
		Collections.sort(imports, new Comparator<ImportWrapper>() {
			public int compare(ImportWrapper o1, ImportWrapper o2) {
				return o2.getWeight() - o1.getWeight();
			}
		});

		// persist
		this.exports = exports;
		this.imports = imports;
	}
	
	public IServerImportParticipant[] getImportParticipants() {
		return (ImportWrapper[]) imports.toArray(new ImportWrapper[imports.size()]);
	}

	public IServerExportParticipant[] getExportParticipants() {
		return (ExportWrapper[]) exports.toArray(new ExportWrapper[exports.size()]);
	}

}
