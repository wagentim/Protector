package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.wagentim.common.ImageRegister;
import de.wagentim.protector.controller.ProtectorController;

public class ContentComposite extends AbstractComposite
{
	public ContentComposite(Composite composite, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(composite, style, controller, imageRegister);

		GridLayout layout = new GridLayout(1, false);
		this.setLayout(layout);
		GridData cgd = new GridData(GridData.FILL_HORIZONTAL);
		this.setLayoutData(cgd);
		
		TableComposite tc = new TableComposite(this, SWT.NONE, controller, imageRegister);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tc.setLayoutData(gd);
		
	}
}
