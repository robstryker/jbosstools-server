package org.jboss.ide.eclipse.as.wtp.ui.server.export;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;
import org.eclipse.wst.server.ui.internal.ServerUIPlugin;
import org.jboss.ide.eclipse.as.wtp.core.server.export.ExportServerUtility;


public class ServerExportPage extends WizardPage {

	private IStructuredSelection selection;
	private String destinationString;
	private IServer toExport;
	
	protected ServerExportPage(IStructuredSelection selection) {
		this("ServerExportWizard", selection);
	}
	
	protected ServerExportPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		this.selection = selection;
		setTitle("Export a Server");
		setDescription("Please select a server to export.");
		setImageDescriptor(ServerUIPlugin.imageDescriptorFromPlugin(ServerUIPlugin.PLUGIN_ID, "icons/wizban/new_server_wiz.png"));
	}

	public boolean finish() {
		System.out.println("Export here");
		new ExportServerUtility(new File(destinationString)).exportServer(toExport, new NullProgressMonitor());
		return true;
	}

	public void createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.BORDER);
		c.setLayout(new GridLayout(1, true));
		final TreeViewer tv = new TreeViewer(c, SWT.BORDER);
		tv.setLabelProvider(ServerUICore.getLabelProvider());
		tv.setContentProvider(new ITreeContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			public void dispose() {
			}
			public boolean hasChildren(Object element) {
				return false;
			}
			public Object getParent(Object element) {
				return null;
			}
			public Object[] getElements(Object inputElement) {
				return ServerCore.getServers();
			}
			public Object[] getChildren(Object parentElement) {
				return new Object[0];
			}
		});
		tv.setInput(new Object());
		tv.getTree().setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		Composite dest = new Composite(c, SWT.NONE);
		dest.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		dest.setLayout(new GridLayout(3, false));
		Label l = new Label(dest, SWT.NONE);
		l.setText("Destination: ");
		final Text t = new Text(dest, SWT.BORDER);
		Button browse = new Button(dest, SWT.PUSH);
		browse.setText("Browse...");
		
		tv.getTree().addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				ISelection sel = tv.getSelection();
				if( sel instanceof IStructuredSelection ) {
					toExport = (IServer) ((IStructuredSelection)sel).getFirstElement();
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		t.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				destinationString = t.getText();
			}
		});
		setControl(c);
	}

}
