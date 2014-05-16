/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jmx.local.internal;


import org.jboss.tools.foundation.ui.plugin.BaseUISharedImages;
import org.jboss.tools.jmx.commons.ImagesActivatorSupport;
import org.jboss.tools.jmx.commons.logging.RiderLogFacade;
import org.jboss.tools.jmx.jvmmonitor.core.JvmModel;
import org.osgi.framework.BundleContext;


public class Activator extends ImagesActivatorSupport {

	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// prefill the process info storage to save time
		JvmConnectionWrapper.refreshProcessInformationStore();
	}

	public static void registerNodeProviders() {
		// lets start early loading of the model
		@SuppressWarnings("unused")
		JvmModel model = JvmModel.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	// TODO remove
	public static RiderLogFacade getLogger() {
		return RiderLogFacade.getLog(getDefault().getLog());
	}
	
    
    private BaseUISharedImages sharedImages = null;
    
	/**
	 * Access the shared images for the plugin
	 * @return
	 */
	public BaseUISharedImages getSharedImages() {
		if( sharedImages == null ) {
			sharedImages = createSharedImages();
			
		}
		return sharedImages;
	}
	
	/**
	 * Create your shared images instance. Clients are expected to override this
	 */
	protected BaseUISharedImages createSharedImages() {
		return new LocalVMSharedImages(getBundle());
	}
}
