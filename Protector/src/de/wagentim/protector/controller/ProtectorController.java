package de.wagentim.protector.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.wagentim.common.IDManager;
import de.wagentim.protector.common.IProtectorActionType;
import de.wagentim.protector.common.IProtectorConstants;
import de.wagentim.protector.common.ProtectorActionManager;
import de.wagentim.protector.db.IDBController;
import de.wagentim.protector.db.SqliteDBController;
import de.wagentim.protector.entity.CellIndex;
import de.wagentim.protector.entity.Record;
import de.wagentim.protector.entity.RecordItem;

public class ProtectorController
{
	private final IDBController dbController;
	private boolean isEditingBlocked = true;
	private Record selectedRecord = null;
	private RecordItem selectedRecordItem = null;
	private Map<Integer, Record> records = Collections.emptyMap();
	private Map<Integer, List<RecordItem>> items = Collections.emptyMap();
	private Logger logger = LoggerFactory.getLogger(ProtectorController.class);
	
	public ProtectorController()
	{
		dbController = new SqliteDBController();
	}
	
	public void loadAllData()
	{
		records.clear();
		items.clear();
		IDManager.INSTANCE().clear();
		
		records = dbController.getAllRecords();
		items = dbController.getAllItems();
		
		Iterator<Integer> it = records.keySet().iterator();
		while(it.hasNext())
		{
			addExistedID(it.next());
		}
		
		Iterator<List<RecordItem>> itemListIt = (items.values()).iterator();
		
		while(itemListIt.hasNext())
		{
			Iterator<RecordItem> itemIt = itemListIt.next().iterator();
			
			while(itemIt.hasNext())
			{
				addExistedID(itemIt.next().getItemId());
			}
		}
		
		ProtectorActionManager.actionManager.sendAction(IProtectorActionType.ACTION_DATA_LOADED, records.values());
	}
	
	private void addExistedID(int id)
	{
		IDManager.INSTANCE().addID(id);
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

	public Record addNewRecord()
	{
		Record record = new Record(IDManager.INSTANCE().getRandomInteger(), IProtectorConstants.TXT_DEFAULT_RECORD_NAME);
		records.put(record.getId(), record);
		dbController.insertNewRecord(record.getId(), record.getName());
		logger.info("Add New Record: {}", record.toString());
		return record;
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
	
	public RecordItem getSelectedRecordItem()
	{
		return selectedRecordItem;
	}
	
	public void setSelectedRecordItem(RecordItem item)
	{
		this.selectedRecordItem = item;
	}

	public Record getSelectedRecord()
	{
		return selectedRecord;
	}
	
	public void updateSelectedRecordName(String oldValue, String newValue)
	{
		selectedRecord.setName(newValue);
		dbController.updateRecordName(selectedRecord.getId(), newValue);
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
