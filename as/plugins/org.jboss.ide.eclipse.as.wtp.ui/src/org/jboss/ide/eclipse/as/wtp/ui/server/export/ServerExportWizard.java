package org.jboss.ide.eclipse.as.wtp.ui.server.export;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.WorkbenchPlugin;

public class ServerExportWizard extends Wizard implements IExportWizard {
	private IStructuredSelection selection;
	private ServerExportPage mainPage;
	
	public ServerExportWizard() {
        IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection("ServerExportWizard");//$NON-NLS-1$
        if (section == null) {
			section = workbenchSettings.addNewSection("ServerExportWizard");//$NON-NLS-1$
		}
        setDialogSettings(section);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public void addPages() {
        super.addPages();
        mainPage = new ServerExportPage(selection);
        addPage(mainPage);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.selection = currentSelection;
//        setWindowTitle(DataTransferMessages.DataTransfer_export);
//        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performFinish() {
        return mainPage.finish();
    }
}
