package org.jboss.tools.jmx.local.internal;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.foundation.ui.plugin.BaseUISharedImages;
import org.jboss.tools.jmx.ui.JMXUIActivator;
import org.osgi.framework.Bundle;

public class LocalVMSharedImages extends BaseUISharedImages {

	public static final String CONTAINER_GIF = "image/container.gif";//$NON-NLS-1$
	public static final String CONTAINER_PNG = "image/container.png";//$NON-NLS-1$
	public static final String CAMEL_PNG = "image/camel.png";//$NON-NLS-1$
	public static final String FABRIC_PNG = "image/fabric.png";//$NON-NLS-1$
	public static final String FUSE_PNG = "image/fuse_server.png";//$NON-NLS-1$
	public static final String MQ_PNG = "image/mq_server.png";//$NON-NLS-1$
	public static final String SMX_PNG = "image/smx_server.png";//$NON-NLS-1$

	private static LocalVMSharedImages shared;
	public static LocalVMSharedImages getDefault() {
		if( shared == null )
			shared = new LocalVMSharedImages();
		return shared;
	}
	
	
	public LocalVMSharedImages(Bundle pluginBundle) {
		super(pluginBundle);
	}
	
	private LocalVMSharedImages() {
		super(JMXUIActivator.getDefault().getBundle());
		addImage(CONTAINER_GIF, CONTAINER_GIF);
	}

	public static Image getImage(String k) {
		return getDefault().image(k);
	}
}
