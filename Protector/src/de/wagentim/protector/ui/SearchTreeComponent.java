package de.wagentim.protector.ui;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.wagentim.common.IConstants;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.Record;

public class SearchTreeComponent extends AbstractComposite
{

	private Tree recordTree;
	private TreeItem root;
	private TableComposite tableComposite;
	private TreeListener tl;
	private Menu rightClickMenu;
	
	public SearchTreeComponent(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style, controller, imageRegister);
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initComponent(controller);
	}

	protected void initComponent(ProtectorController controller)
	{
		new SearchComposite(this, SWT.BORDER, controller, imageRegister);
		
		recordTree = new Tree(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = IConstants.HEIGHT_HINT;
		recordTree.setLayoutData(gd);
		
		root = new TreeItem(recordTree, SWT.NONE);
		root.setText(IConstants.TXT_RECORD);
		root.setImage(imageRegister.getImage(IImageConstants.IMAGE_ROOT));
		
		tl = new TreeListener(recordTree, controller);
		recordTree.addMouseListener(tl);
		recordTree.addKeyListener(tl);
		recordTree.addSelectionListener(tl);
	}
	
	private void createRightMenu(Control control, SelectionListener listener)
	{
		rightClickMenu = new Menu(control);
		control.setMenu(rightClickMenu);
		
		rightClickMenu.addMenuListener(new MenuAdapter()
	    {
	        public void menuShown(MenuEvent e)
	        {
	        	
	            MenuItem[] items = rightClickMenu.getItems();
	            
	            for (int i = 0; i < items.length; i++)
	            {
	                items[i].dispose();
	            }
	            
	            if(recordTree.getSelectionCount() <= 0)
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
	
	private void addTreeItem(Record record, TreeItem parent, int index)
	{
		TreeItem it;
		
		if( index < 0 )
		{
			it = new TreeItem(parent, SWT.NONE);
			
		}
		else
		{
			it = new TreeItem(parent, SWT.NONE, index);
		}
		
		it.setText(record.getName());
		it.setData(record);
		it.setImage(imageRegister.getImage(IImageConstants.IMAGE_RECORD));
	}

	
	
	public void setTreeSelectedRecord(int index)
	{
		recordTree.select(root.getItem(index));
	}

	public TableComposite getTableComposite()
	{
		return tableComposite;
	}

	public void setTableComposite(TableComposite tableComposite)
	{
		this.tableComposite = tableComposite;
	}
	
	public void receivedAction(int type, Object content)
	{
		if( IProtectorActionType.ACTION_DATA_LOADED == type)
		{
			root.removeAll();

			@SuppressWarnings("unchecked")
			Collection<Record> records = (Collection<Record>) content;
			
			Iterator<Record> it = records.iterator();
			while(it.hasNext())
			{
				addTreeItem(it.next(), root, -1);
			}

			setTreeSelectedRecord(0);
		}
		
		if( IProtectorActionType.ACTION_ADD_NEW_RECORD == type )
		{
			Record record = (Record)content;
			addTreeItem(record, root, recordTree.indexOf(recordTree.getSelection()[0]) + 1);
		}
		
		if( type == IProtectorActionType.ACTION_EDITING_STATUS_CHANGED )
		{
			boolean isEditable = (boolean)content;
			
			if(isEditable)
			{
				createRightMenu(recordTree, tl);
			}
			else
			{
				recordTree.setMenu(null);
				
				if(null != rightClickMenu)
				{
					rightClickMenu.dispose();
				}
			}
		}
	}
}
