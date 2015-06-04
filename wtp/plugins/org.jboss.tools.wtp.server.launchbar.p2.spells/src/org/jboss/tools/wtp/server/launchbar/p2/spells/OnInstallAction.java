package org.jboss.tools.wtp.server.launchbar.p2.spells;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class OnInstallAction extends ProvisioningAction {

	public OnInstallAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus execute(Map<String, Object> parameters) {
		System.out.println("Executing OnInstallAction");
		ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.launchbar.ui");
		store.setValue("enableLaunchBar", false);
		try {
			store.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
