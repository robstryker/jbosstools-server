/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ui.console;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.ui.internal.view.servers.ShowInConsoleAction;
import org.jboss.ide.eclipse.as.core.server.UnitedServerListener;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.jboss.ide.eclipse.as.wtp.core.server.behavior.ServerProfileModel;

public class ShowConsoleServerStateListener extends UnitedServerListener {
	private static ShowConsoleServerStateListener instance;
	public static ShowConsoleServerStateListener getDefault() {
		if( instance == null )
			instance = new ShowConsoleServerStateListener();
		return instance;
	}
	public void serverChanged(ServerEvent event) {
		final IServer server = event.getServer();
		JBossServer jbs = (JBossServer)server.loadAdapter(JBossServer.class, new NullProgressMonitor());
		if( jbs != null && serverSwitchesToState(event, IServer.STATE_STARTING)) {
			// do not launch console for remotes, for now
			String type = ServerProfileModel.getProfile(server); 
			if( !ServerProfileModel.DEFAULT_SERVER_PROFILE.equals(type))
				return;
			
			new Thread() {
				public void run() {
					try {
						// delay to insure the server gets a chance to actually launch
						Thread.sleep(3000);
					} catch(InterruptedException ie) {}
					Display.getDefault().asyncExec(new Runnable() { 
						public void run() {
							ILaunch launch = server.getLaunch();
							if( launch != null && launch.getProcesses().length > 0)
								new ShowInConsoleAction(getNullSelectionProvider()).perform(server);
						}
					});
				}
			}.start();
		}
	}
	
	protected ISelectionProvider getNullSelectionProvider() {
		return new ISelectionProvider() {
			public void addSelectionChangedListener(
					ISelectionChangedListener listener) {
			}
			public ISelection getSelection() {
				return null;
			}
			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
			}
			public void setSelection(ISelection selection) {
			}
		};
	}
}
