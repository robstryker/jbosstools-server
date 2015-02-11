package org.jboss.ide.eclipse.as.wtp.ui.server.importwizard;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ImportServerUtility;


public class ServerImportPage extends WizardPage {

	private IStructuredSelection selection;
	private String sourceFileString;
	
	protected ServerImportPage(IStructuredSelection selection) {
		this("ServerExportWizard", selection);
	}
	
	protected ServerImportPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		this.selection = selection;
		setTitle("Import a Server");
		setDescription("Please select a server to import.");
		setImageDescriptor(ServerUIPlugin.imageDescriptorFromPlugin(ServerUIPlugin.PLUGIN_ID, "icons/wizban/new_server_wiz.png"));
	}

	public boolean finish() {
		System.out.println("Import here");
		new ImportServerUtility(new File(sourceFileString)).importServer(new NullProgressMonitor());
		return true;
	}

	public void createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.BORDER);
		c.setLayout(new GridLayout(1, true));
		Composite dest = new Composite(c, SWT.NONE);
		dest.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		dest.setLayout(new GridLayout(3, false));
		Label l = new Label(dest, SWT.NONE);
		l.setText("Destination: ");
		final Text t = new Text(dest, SWT.BORDER);
		Button browse = new Button(dest, SWT.PUSH);
		browse.setText("Browse...");
		t.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				sourceFileString = t.getText();
			}
		});
		setControl(c);
	}

}
