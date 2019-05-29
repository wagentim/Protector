package de.wagentim.protector.ui;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.wagentim.common.IConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.Item;
import de.wagentim.protector.entity.Record;

public class TableComposite extends AbstractComposite
{
	private Table table;
//	private Button btnAdd;
//	private Button btnDelete;
//	private Button btnSave;
//	private Button btnLock;
	protected Color tableBackgroudColor;
	private Menu rightClickMenu;
	
	public final Image IMAGE_ADD;
	public final Image IMAGE_REMOVE;
	public final Image IMAGE_COPY;
	public final Image IMAGE_PASTE;
	
	private SearchTreeComponent searchTree;
	
	public TableComposite(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style, controller, imageRegister);

		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initMainComposite(this, controller);
		
		IMAGE_ADD = new Image(parent.getDisplay(), "icons/add.png");
		IMAGE_REMOVE = new Image(parent.getDisplay(), "icons/remove.png");
		IMAGE_COPY = new Image(parent.getDisplay(), "icons/copy.png");
		IMAGE_PASTE = new Image(parent.getDisplay(), "icons/paste.png");
		
		tableBackgroudColor = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}
	
	public Table getTable()
	{
		return table;
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
		
		TableListener tl = new TableListener(getTable(), controller);
	    
		for (int i = 0; i < 2; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(true);
			column.setWidth(150);
		}
		
		table.addMouseListener(tl);
		table.addKeyListener(tl);
		addTableSelectedListener();
		sf.setWeights(new int[]{1, 2});
	}
	
	private void btnAddAction()
	{
		if ( null == controller.getSelectedItem())
		{
			return;
		}
	}
	
	private void btnDeleteAction()
	{
		deleteSelectedItems();
//		treeItemSelected(searchTree.getSelectedTreeItem().getText().trim());
	}

	protected void addTableSelectedListener()
	{
		getTable().addSelectionListener(new SelectionAdapter()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				ProtectorActionManager.INSTANCE.sendAction(IConstants.ACTION_PARAMETER_SELECTED, getTable().getSelectionIndex());

				String text = getTable().getItem(getTable().getSelectionIndex()).getText(1);
					
				ProtectorActionManager.INSTANCE.sendAction(IConstants.ACTION_SOURCE_PARAMETER_SELECTED, text);
			}
			
		});
	}

	protected void setTreeSelectedBlock(String blockName)
	{
//		searchTree.setTreeSelectedBlock(blockName);
	}
	
	protected void setBlockList(String[] blockList)
	{
//		searchTree.setItems(blockList);
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
	            copyItem.setImage(IMAGE_COPY);
	            copyItem.addSelectionListener(listener);
	            
	            MenuItem pasteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            pasteItem.setText(IConstants.TXT_PASTE);
	            pasteItem.setImage(IMAGE_PASTE);
	            pasteItem.addSelectionListener(listener);
	            
	            new MenuItem(rightClickMenu, SWT.SEPARATOR);
	            
	            MenuItem newItem = new MenuItem(rightClickMenu, SWT.NONE);
	            newItem.setText(IConstants.TXT_BTN_ADD);
	            newItem.setImage(IMAGE_ADD);
	            newItem.addSelectionListener(listener);
	            
	            MenuItem deleteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            deleteItem.setText(IConstants.TXT_BTN_DELETE);
	            deleteItem.setImage(IMAGE_REMOVE);
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


	public void updateParameters(List<Item> items)
	{
		clearTable();

		if( null == table || null == items || items.isEmpty() )
		{
			return;
		}
		
		Iterator<Item> it = items.iterator();
		
		while(it.hasNext())
		{
			addTableItem(it.next());
		}
		
//		resize();
	}
	
	private void addTableItem(Item item)
	{
		TableItem ti = new TableItem(table, SWT.NONE);
		ti.setText(0, item.getKey());
		ti.setText(1, item.getValue());
		ti.setData(item);
		ti.setBackground(tableBackgroudColor);
	}
	
	private void resize()
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

	protected String fileSave(Shell shell)
	{
		FileDialog fd = new FileDialog(shell, SWT.APPLICATION_MODAL | SWT.SAVE);
		fd.setFilterExtensions(IConstants.CONFIG_FILE_EXTENSION);
		fd.setFilterNames(IConstants.CONFIG_FILE_NAME);
		String result = fd.open();
		if( null != result )
		{
			Path file = Paths.get(result);
	        if (Files.exists(file))
	        {
	          MessageBox mb = new MessageBox(fd.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);

	          mb.setMessage(result + " already exists. Do you want to replace it?");

	          boolean done = mb.open() == SWT.YES;
	          
	          if( !done )
	          {
	        	return null;  
	          }
	        }
		}
		
		return result;
	}
	
	private void toSave() 
	{
		String fileSavePath = fileSave(this.getShell());

		if (null == fileSavePath)
		{
			return;
		}

		saveAction(fileSavePath);
	}
	
	protected void saveAction(String targetFilePath)
	{
		controller.saveFile(targetFilePath);
		ProtectorActionManager.INSTANCE.sendAction(IConstants.ACTION_LOG_WRITE_INFO, "Source Write to: " + targetFilePath + " finished!");
	}
	
	@Override
	public void receivedAction(int type, Object content)
	{
		if( type == IProtectorActionType.ACTION_RECORD_SELECTED )
		{
			updateParameters(controller.getSelectedItems((Record)content));
		}

		if( type == IProtectorActionType.ACTION_DATA_LOADED )
		{
			updateParameters(null);
		}
		
	}
}
