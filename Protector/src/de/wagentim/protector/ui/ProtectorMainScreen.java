package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.wagentim.common.IActionListener;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.common.IProtectorConstants;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.controller.InfoBlockWriter;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.RecordItem;

public class ProtectorMainScreen extends Composite implements IActionListener
{
	private final ProtectorController controller;
	private StyledText txtInfoBlock;
	private SashForm main;
	private final ImageRegister imageRegister;
	private ToolItem editToolItem;
	private ToolItem loadToolItem;
	private ToolItem addRecordItem;
	
	public ProtectorMainScreen(Composite parent, int style, final ImageRegister imageRegister)
	{
		super(parent, style);
		this.imageRegister = imageRegister;
		
		controller = new ProtectorController();
		initMainScreen(this);
		initMainComponents(this);
		
		ProtectorActionManager.actionManager.addActionListener(this);
	}
	
	private void initMainComponents(Composite shell)
	{
		main = new SashForm(shell, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		main.setLayoutData(gd);

		new ContentComposite(main, SWT.BORDER, controller, imageRegister);
		
		txtInfoBlock = new StyledText(main, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		txtInfoBlock.setLayoutData(gd);
		txtInfoBlock.setEditable(false);
		txtInfoBlock.setVisible(false);
		
		new InfoBlockWriter(txtInfoBlock, controller);

		main.setWeights(new int[]
		{ 1, 0 });
		
	}

	private void initMainScreen(Composite shell)
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		shell.setLayout(layout);
		
		ToolBar bar = new ToolBar(shell, SWT.FLAT);
		loadToolItem = new ToolItem(bar, SWT.PUSH);
		loadToolItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_LOAD_OUTLINE));
		loadToolItem.setText(IProtectorConstants.TXT_LOAD_RECORD_ITEM);
		loadToolItem.setHotImage(imageRegister.getImage(IImageConstants.IMAGE_LOAD_COLOR));
		loadToolItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				controller.loadAllData();
			}
		});
		
		new ToolItem(bar, SWT.SEPARATOR);
		
		editToolItem = new ToolItem(bar, SWT.PUSH);
		editToolItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_EDITABLE_OUTLINE));
		editToolItem.setText(IProtectorConstants.TXT_EDITABLE);
		editToolItem.setHotImage(imageRegister.getImage(IImageConstants.IMAGE_EDITABLE_COLOR));
		editToolItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				controller.setEditable();
				
				if(controller.isEditable())
				{
					editToolItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_EDITABLE_COLOR));			
				}
				else
				{
					editToolItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_EDITABLE_OUTLINE));
				}
			}
		});
		
		new ToolItem(bar, SWT.SEPARATOR);
		
		addRecordItem = new ToolItem(bar, SWT.PUSH);
		addRecordItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_ADD));
		addRecordItem.setText(IProtectorConstants.TXT_ADD_RECORD_ITEM);
		addRecordItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				RecordItem recordItem = controller.getNewRecordItem();
				ProtectorActionManager.INSTANCE().sendAction(IProtectorActionType.ACTION_ADD_NEW_RECORD_ITEM, recordItem);
			}
		});
		addRecordItem.setEnabled(false);
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		if( type == IProtectorActionType.ACTION_EDITING_STATUS_CHANGED )
		{
			boolean isEditable = (boolean)content;
			
			addRecordItem.setEnabled(isEditable);
		}
	}
}
