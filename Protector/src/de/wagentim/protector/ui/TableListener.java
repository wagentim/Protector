package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.wagentim.common.IConstants;
import de.wagentim.common.ui.CellIndex;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.RecordItem;

public class TableListener extends CellEditingListener
{

	private CellIndex cell = null;
	
	public TableListener(Table table, ProtectorController controller)
	{
		super(table, controller);
	}
	
	private CellIndex getDoubleClickPosIndex(Point pt)
	{
		int columnCount = getTable().getColumnCount();
		int rowCount = getTable().getItemCount();

		for( int j = 0; j < columnCount; j++)
		{
			for (int i = 0; i < rowCount; i++)
			{
				Rectangle rect = getTable().getItem(i).getBounds(j);
				if (rect.contains(pt))
				{
					return new CellIndex(i, j);
				}
			}
		}
		return null;
	}
	
	private Table getTable()
	{
		return (Table)getComposite();
	}

	@Override
	protected ControlEditor getNewEditor()
	{
		TableEditor editor = new TableEditor(getTable());
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.minimumWidth = 50;
		return editor;
	}

	@Override
	protected void updateWithNewValue()
	{
		controller.recordItemChanged(cell, newValue);
	}
	
	protected void disposeEditor()
	{
		super.disposeEditor();
		this.cell = null;
	}

	@Override
	protected Item getSelectedItem(TypedEvent event)
	{
		if(event instanceof MouseEvent)
		{
			MouseEvent mevent =(MouseEvent)event;
			Point pt = new Point(mevent.x, mevent.y);
	        cell = getDoubleClickPosIndex(pt);
	        
	        if( null == cell )
	        {
	        	return null;
	        }
	        
	        final TableItem item = getTable().getItem(cell.getRow());
	        
	        if (item == null)
	        {
	          return null;
	        }
			return item;
		}
		else if(event instanceof KeyEvent || event instanceof SelectionEvent)
		{
			int row = getTable().getSelectionIndices()[0];
			
			cell = new CellIndex(row, 1);
			return getTable().getItem(row);
		}
		
		return null;
	}

	@Override
	protected void setNewEditor(Text newEditor, Item item)
	{
		((TableEditor)editor).setEditor(newEditor, (TableItem)item, cell.getColumn());
	}

	@Override
	protected void setNewValueByModify()
	{
		((TableEditor)editor).getItem().setText(cell.getColumn(), newValue);
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
			
			if( text.contentEquals(IConstants.TXT_ADD) )
			{
				ProtectorActionManager.actionManager.sendAction(IConstants.ACTION_MENU_ADD, IConstants.EMPTY_STRING);
			}
			else if( text.contentEquals(IConstants.TXT_DELETE) )
			{
				ProtectorActionManager.actionManager.sendAction(IConstants.ACTION_MENU_DELETE, IConstants.EMPTY_STRING);
			}
			else if( text.contentEquals(IConstants.TXT_COPY) )
			{
			}
			else if( text.contentEquals(IConstants.TXT_PASTE) )
			{
			}
		}
		else if(event.getSource() instanceof Table)
		{
			RecordItem item = (RecordItem) getSelectedItem(event).getData();
			controller.setSelectedRecordItem(item);
			ProtectorActionManager.INSTANCE().sendAction(IProtectorActionType.ACTION_RECORD_ITEM_SELECTED, item);
		}
	}
	
	@Override
	protected CellIndex getCell()
	{
		return cell;
	}


}
