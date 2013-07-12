package org.jboss.ide.eclipse.as.rse.core;

import org.eclipse.core.runtime.IStatus;
import org.jboss.ide.eclipse.as.core.server.IJBossBehaviourDelegate;

public class RSEWildfly8BehaviourDelegate extends RSEJBoss7BehaviourDelegate
		implements IJBossBehaviourDelegate {

	public RSEWildfly8BehaviourDelegate() {
	}
	
	protected IStatus gracefullStop() {
		return gracefullStopViaManagement();
	}

}
