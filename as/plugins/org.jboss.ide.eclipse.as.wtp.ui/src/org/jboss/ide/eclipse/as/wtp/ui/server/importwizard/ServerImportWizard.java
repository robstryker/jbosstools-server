package org.jboss.ide.eclipse.as.wtp.ui.server.importwizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.jboss.ide.eclipse.as.wtp.ui.server.export.ServerExportPage;

public class ServerImportWizard extends Wizard implements IImportWizard {
	private IStructuredSelection selection;
	private ServerImportPage mainPage;
	
	public ServerImportWizard() {
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("ServerImportWizard");//$NON-NLS-1$
		if (section == null) {
			section = workbenchSettings.addNewSection("ServerImportWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ServerImportPage(selection);
		addPage(mainPage);
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.selection = currentSelection;
		setWindowTitle("Import Server");
		//        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	@Override
	public boolean performFinish() {
		return mainPage.finish();
	}

}
