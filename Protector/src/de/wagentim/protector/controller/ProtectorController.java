package de.wagentim.protector.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.wagentim.common.IConstants;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.db.IDBController;
import de.wagentim.protector.db.SqliteDBController;
import de.wagentim.protector.entity.CellIndex;
import de.wagentim.protector.entity.Item;
import de.wagentim.protector.entity.Record;

public class ProtectorController
{
	private List<Record> itemList = Collections.emptyList();
	private final IDBController dbController;
	private boolean isEditingBlocked = true;
	private String selectedItem = IConstants.EMPTY_STRING;
	private Map<Integer, Record> records = Collections.emptyMap();
	private Map<Integer, List<Item>> items = Collections.emptyMap();
	
	public ProtectorController()
	{
		dbController = new SqliteDBController();
	}
	
	public void loadAllData()
	{
		records = dbController.getAllRecords();
		items = dbController.getAllItems();
		ProtectorActionManager.INSTANCE.sendAction(IProtectorActionType.ACTION_DATA_LOADED, records.values());
	}
	
	public String[] getItemNames()
	{
		String[] result = new String[itemList.size()];
		
		for(int i = 0; i < itemList.size(); i++)
		{
//			result[i] = itemList.get(i).getName();
		}
		
		return result;
	}
	
	public void exit()
	{
	}
	
	public boolean isEditingLocked()
	{
		return isEditingBlocked;
	}
	
	public void setEditingLocked()
	{
		isEditingBlocked = !isEditingBlocked;
	}
	
	public void setShowConfigBlocks(String text)
	{
		
	}
	
	public void removeBlock(String name)
	{
		
	}
	
	public void copyBlock(String name)
	{
		
	}

	public Record getCopyBlock()
	{
		return null;
	}

	public void addConfigBlock(Record newBlock)
	{
		// TODO Auto-generated method stub
		
	}

	public Record getSelectedItem()
	{
		if(null == selectedItem || selectedItem.isEmpty())
		{
			return null;
		}
		
		return findEntity(selectedItem);
	}
	
	private Record findEntity(String item)
	{
		Iterator<Record> it = itemList.iterator();
		
		while(it.hasNext())
		{
			Record entity = it.next();
			
//			if(entity.getName().equals(item))
//			{
//				return entity;
//			}
		}
		
		return null;
	}

	public int getFocusedElement()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void setInputConfigFile(String currFilePath)
	{
		// TODO Auto-generated method stub
		
	}

	public void setSelectedItem(String item)
	{
		selectedItem = item;
	}

	public void deleteParameters(int[] selectedItems, String text)
	{
		// TODO Auto-generated method stub
		
	}

	public void saveFile(String targetFilePath)
	{
		// TODO Auto-generated method stub
		
	}

	public String[] getAllBlocks()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void copyParameters(int[] selectedItems)
	{
		// TODO Auto-generated method stub
		
	}

	public void parameterChanged(CellIndex cell, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	public void updateBlockName(String oldValue, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	public List<Item> getSelectedItems(Record record)
	{
		if( null == record )
		{
			return Collections.emptyList();
		}
		
		return items.get(record.getId());
	}
	
}
