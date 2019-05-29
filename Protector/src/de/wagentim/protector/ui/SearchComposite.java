package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.wagentim.common.IConstants;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.common.IProtectorConstants;
import de.wagentim.protector.controller.ProtectorController;

public class SearchComposite extends AbstractComposite
{
	
	protected ProtectorController controller;
	private Text searchText;
	private final Color white;
	private Label cancelImage;
	
	public SearchComposite(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style, controller, imageRegister);
		
		
		white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		
		GridLayout layout = new GridLayout(3, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		layout.marginHeight = layout.marginWidth = 3;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumHeight = 30;
		this.setLayout(layout);
		this.setLayoutData(gd);
		this.setBackground(white);
		
		Label label = new Label(this, SWT.NONE);
		label.setImage(imageRegister.getImage(IImageConstants.IMAGE_SEARCH));
		gd = new GridData();
		label.setLayoutData(gd);
		label.setBackground(white);
		
		searchText = new Text(this, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = gd.horizontalSpan = 0;
		searchText.setLayoutData(gd);
		searchText.setMessage("Search");
		
		searchText.addModifyListener(new ModifyListener()
		{
			
			@Override
			public void modifyText(ModifyEvent event)
			{
				String text = searchText.getText();
				controller.setShowConfigBlocks(text);
				if(!text.isEmpty())
				{
					cancelImage.setVisible(true);
				}
				else
				{
					cancelImage.setVisible(false);
				}
				
				ProtectorActionManager.INSTANCE.sendAction(IConstants.ACTION_SET_SHOW_CONFIG_BLOCKS, text);
			}
		});
		
		cancelImage = new Label(this, SWT.NONE);
		cancelImage.setImage(imageRegister.getImage(IImageConstants.IMAGE_CANCEL));
		gd = new GridData();
		cancelImage.setLayoutData(gd);
		cancelImage.setBackground(white);
		cancelImage.setVisible(false);
		cancelImage.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseUp(MouseEvent arg0)
			{
				searchText.setText(IConstants.EMPTY_STRING);
				cancelImage.setVisible(false);
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

		});
		
		this.controller = controller;
	}
	
	@Override
	public void receivedAction(int type, Object content)
	{
		if( type == IConstants.ACTION_LOAD_DATA || type == IConstants.ACTION_DROP_NEW_FILE_SELECTED)
		{
			searchText.setText(IConstants.EMPTY_STRING);
		}
	}
}
