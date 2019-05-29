package de.wagentim.protector.ui;


import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.wagentim.common.IConstants;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.Record;
import de.wagentim.protector.entity.RecordItem;

public class TableComposite extends AbstractComposite
{
	private Table table;
	protected Color tableBackgroudColor;
	private Menu rightClickMenu;
	private SearchTreeComponent searchTree;
	private TableListener tableListener;
	
	public TableComposite(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style, controller, imageRegister);

		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initMainComposite(this, controller);
		tableBackgroudColor = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}
	
	protected void initMainComposite(Composite comp, ProtectorController controller)
	{
		SashForm sf = new SashForm(comp, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		sf.setLayoutData(gd);
		
		searchTree = new SearchTreeComponent(sf, SWT.NONE, controller, imageRegister);
		searchTree.setTableComposite(this);
		
		Composite tableComposite = new Composite(sf, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		tableComposite.setLayout(layout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		table = new Table(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = IConstants.HEIGHT_HINT;
		table.setLayoutData(gd);
		
		tableListener = new TableListener(table, controller);
	    
		for (int i = 0; i < 2; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(true);
			column.setWidth(150);
		}
		
		table.addMouseListener(tableListener);
		table.addKeyListener(tableListener);
		table.addSelectionListener(tableListener);
		sf.setWeights(new int[]{1, 2});
	}
	
	private void createRightMenu(Control control, SelectionListener listener)
	{
		rightClickMenu = new Menu(table);
		table.setMenu(rightClickMenu);
		
		rightClickMenu.addMenuListener(new MenuAdapter()
	    {
	        public void menuShown(MenuEvent e)
	        {
	            MenuItem[] items = rightClickMenu.getItems();
	            
	            for (int i = 0; i < items.length; i++)
	            {
	                items[i].dispose();
	            }
	            
	            if(table.getSelectionCount() <= 0)
	            {
	            	return;
	            }
	            
	            MenuItem copyItem = new MenuItem(rightClickMenu, SWT.NONE);
	            copyItem.setText(IConstants.TXT_COPY);
	            copyItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_COPY));
	            copyItem.addSelectionListener(listener);
	            
	            MenuItem pasteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            pasteItem.setText(IConstants.TXT_PASTE);
	            pasteItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_PASTE));
	            pasteItem.addSelectionListener(listener);
	            
	            new MenuItem(rightClickMenu, SWT.SEPARATOR);
	            
	            MenuItem newItem = new MenuItem(rightClickMenu, SWT.NONE);
	            newItem.setText(IConstants.TXT_ADD);
	            newItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_ADD));
	            newItem.addSelectionListener(listener);
	            
	            MenuItem deleteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            deleteItem.setText(IConstants.TXT_DELETE);
	            deleteItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_REMOVE));
	            deleteItem.addSelectionListener(listener);
	        }
	    });
	}

	protected void deleteSelectedItems()
	{
		int[] selectedItems = table.getSelectionIndices();
		
		if( null != selectedItems && selectedItems.length > 0 )
		{
			
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);

			mb.setMessage("Do you really want to delete?");

			boolean done = mb.open() == SWT.YES;
			
			if( done )
			{
//				controller.deleteParameters(selectedItems, searchTree.getSelectedTreeItem().getText());
			}
		}
	}


	public void updateParameters(List<RecordItem> items)
	{
		clearTable();

		if( null == table || null == items || items.isEmpty() )
		{
			return;
		}
		
		Iterator<RecordItem> it = items.iterator();
		
		while(it.hasNext())
		{
			addTableItem(it.next());
		}
		
		resize();
	}
	
	private void addTableItem(RecordItem item)
	{
		TableItem ti = new TableItem(table, SWT.NONE);
		ti.setText(0, item.getKey());
		ti.setText(1, item.getValue());
		ti.setData(item);
		ti.setBackground(tableBackgroudColor);
	}
	
	public void resize()
	{
		for (TableColumn tc : table.getColumns())
		{
	        resizeColumn(tc);
		}
	}
	
	private static void resizeColumn(TableColumn tableColumn_)
	{
	    tableColumn_.pack();

	}
	
	protected void clearTable()
	{
		if( null == table )
		{
			return;
		}
			
		TableItem[] items = table.getItems();
		
		if( items.length <= 0 )
		{
			return;
		}
		
		for(int i = items.length - 1; i >= 0; i--)
		{
			TableItem item = items[i];
			table.remove(i);
			item.dispose();
		}
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		if( type == IProtectorActionType.ACTION_RECORD_SELECTED )
		{
			updateParameters(controller.getItems((Record)content));
		}

		else if( type == IProtectorActionType.ACTION_DATA_LOADED )
		{
			updateParameters(null);
		}
		
		else if( type == IProtectorActionType.ACTION_EDITING_STATUS_CHANGED)
		{
			boolean isEditable = (boolean)content;
			
			if(isEditable)
			{
				createRightMenu(table, tableListener);
			}
			else
			{
				table.setMenu(null);
				
				if( null != rightClickMenu )
				{
					rightClickMenu.dispose();
				}
			}
		}
		else if( type == IProtectorActionType.ACTION_ADD_NEW_RECORD_ITEM )
		{
			RecordItem ri = (RecordItem)content;
			
			addTableItem(ri);
		}
		
	}
}
