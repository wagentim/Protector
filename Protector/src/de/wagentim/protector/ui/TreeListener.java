package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.etas.tef.app.IGlobalConstants;
import de.wagentim.common.ui.CellIndex;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.Record;

public class TreeListener extends CellEditingListener
{

	public TreeListener(Tree tree, ProtectorController controller)
	{
		super(tree, controller);
	}
	
	protected ControlEditor getNewEditor()
	{
		TreeEditor editor = new TreeEditor(getTree());
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.minimumWidth = 50;
		return editor;
	}
	
	private Tree getTree()
	{
		return (Tree)getComposite();
	}

	@Override
	protected void updateWithNewValue()
	{
		controller.updateSelectedRecordName(oldValue, newValue);
	}

	@Override
	protected Item getSelectedItem(TypedEvent event)
	{
		return getSelectedTreeItem();
	}

	@Override
	protected void setNewEditor(Text newEditor, Item item)
	{
		((TreeEditor)editor).setEditor(newEditor, (TreeItem)item);
	}

	@Override
	protected void setNewValueByModify()
	{
		getSelectedTreeItem().setText(newValue);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0)
	{
		
	}

	@Override
	public void widgetSelected(SelectionEvent event)
	{
		if(event.getSource() instanceof MenuItem)
		{
			String text = ((MenuItem)event.getSource()).getText();
			
			if( text.contentEquals(IGlobalConstants.TXT_ADD) )
			{
				Record record = controller.addNewRecord();
				ProtectorActionManager.actionManager.sendAction(IProtectorActionType.ACTION_ADD_NEW_RECORD, record);
			}
			else if( text.contentEquals(IGlobalConstants.TXT_DELETE) )
			{
				ProtectorActionManager.actionManager.sendAction(IGlobalConstants.ACTION_DELETE_BLOCK, getTree().getSelection()[0].getText());
			}
			else if( text.contentEquals(IGlobalConstants.TXT_COPY) )
			{
				sendCopyMessage();
			}
			else if( text.contentEquals(IGlobalConstants.TXT_PASTE) )
			{
				sendPasteMessage();
			}
		}
		else if(event.getSource() instanceof Tree)
		{
			Record record = (Record) getSelectedTreeItem().getData();
			controller.setSelectedRecord(record);
			ProtectorActionManager.actionManager.sendAction(IProtectorActionType.ACTION_RECORD_SELECTED, record);
		}
		
		
	}
	
	private TreeItem getSelectedTreeItem()
	{
		TreeItem ti = getTree().getSelection()[0];
		return ti;
	}
	
	private void sendCopyMessage()
	{
		ProtectorActionManager.actionManager.sendAction(IGlobalConstants.ACTION_COPY_BLOCK, null);
	}
	
	private void sendPasteMessage()
	{
		ProtectorActionManager.actionManager.sendAction(IGlobalConstants.ACTION_PASTE_BLOCK, null);
	}

	@Override
	protected CellIndex getCell()
	{
		return null;
	}
}
