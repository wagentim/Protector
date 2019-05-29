package de.wagentim.protector.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.db.IDBController;
import de.wagentim.protector.db.SqliteDBController;
import de.wagentim.protector.entity.CellIndex;
import de.wagentim.protector.entity.RecordItem;
import de.wagentim.protector.entity.Record;

public class ProtectorController
{
	private List<Record> itemList = Collections.emptyList();
	private final IDBController dbController;
	private boolean isEditingBlocked = true;
	private Record selectedRecord = null;
	private RecordItem selectedRecordItem = null;
	private Map<Integer, Record> records = Collections.emptyMap();
	private Map<Integer, List<RecordItem>> items = Collections.emptyMap();
	
	public ProtectorController()
	{
		dbController = new SqliteDBController();
	}
	
	public void loadAllData()
	{
		records = dbController.getAllRecords();
		items = dbController.getAllItems();
		ProtectorActionManager.actionManager.sendAction(IProtectorActionType.ACTION_DATA_LOADED, records.values());
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

	public void setSelectedRecord(Record record)
	{
		selectedRecord = record;
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

	public void recordItemChanged(CellIndex cell, String newValue)
	{
		int column = cell.getColumn();
		
		switch (column)
		{
		case 0:	selectedRecordItem.setKey(newValue);
				break;
		case 1: selectedRecordItem.setValue(newValue);
				break;
		}
		
		dbController.updateItem(selectedRecordItem.getRecordId(), selectedRecordItem.getItemId(), selectedRecordItem.getKey(), selectedRecordItem.getValue());
		
	}
	
	public void setSelectedRecordItem(RecordItem item)
	{
		this.selectedRecordItem = item;
	}

	public void updateSelectedRecordName(String oldValue, String newValue)
	{
		selectedRecord.setName(newValue);
		dbController.updateRecordName(selectedRecord.getId(), newValue);
	}
	
	public RecordItem getSelectedItem()
	{
		return selectedRecordItem;
	}

	public List<RecordItem> getItems(Record record)
	{
		if( null == record )
		{
			return Collections.emptyList();
		}
		
		return items.get(record.getId());
	}
	
}
