package org.jboss.tools.as.ui.bot.itests.parametized;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.as.ui.bot.itests.reddeer.ui.SearchingForRuntimesDialog;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;

public class CleanEnvironmentUtils {

	public static void cleanPaths() {
		// make sure that paths were removed
		for (RuntimePath path : RuntimeUIActivator.getDefault().getModel().getRuntimePaths()) {
			RuntimeUIActivator.getDefault().getModel().removeRuntimePath(path);
		}
	}

	public static void closeShells() {
		// close opened shells
		String[] openedShells = new String[] { SearchingForRuntimesDialog.DIALOG_TITLE,
				WorkbenchPreferenceDialog.DIALOG_TITLE };
		for (String title : openedShells) {
			if (new ShellWithTextIsAvailable(title).test()) {
				new DefaultShell(title);
				new PushButton("Cancel").click();
			}
		}
	}

	public static void cleanAll() {
		cleanPaths();
		cleanServers();
		cleanServerRuntimes();
		deleteAllProjects();
	}

	public static void deleteAllProjects() {
		IProject[] all = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(int i = 0; i < all.length; i++ ) {
			try {
				all[i].delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteServersAndRuntimes() {
		cleanServers();
		cleanServerRuntimes();
	}

	public static void cleanServerRuntimes() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RuntimePreferencePage runtimePage = new RuntimePreferencePage();
		preferenceDialog.select(runtimePage);
		runtimePage.removeAllRuntimes();
		preferenceDialog.ok();
	}

	public static void cleanServers() {
		ServersView serversView = new ServersView();
		serversView.open();
		List<Server> servers = serversView.getServers();
		for (Server server : servers) {
			server.delete();
		}
	}

}
