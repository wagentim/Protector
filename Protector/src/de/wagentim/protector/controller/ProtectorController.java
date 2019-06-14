package de.wagentim.protector.controller;

import java.util.ArrayList;
import java.util.Collection;
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
import de.wagentim.protector.ui.IStatusBarUpdate;

public class ProtectorController
{
	private final IDBController dbController;
	private boolean isEditable = false;
	private Record selectedRecord = null;
	private RecordItem selectedRecordItem = null;
	private Map<Integer, Record> records = Collections.emptyMap();
	private Map<Integer, List<RecordItem>> items = Collections.emptyMap();
	private final Logger logger = LoggerFactory.getLogger(ProtectorController.class);
	private final Collection<Record> showSearchRecords = new ArrayList<Record>();
	private final IStatusBarUpdate statusbar;
	
	public ProtectorController(IStatusBarUpdate statusbar)
	{
		dbController = new SqliteDBController();
		this.statusbar = statusbar;
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
		
		statusbar.statusbarUpdate("Total Items: " + records.size());
	}
	
	public Collection<Record> getAllRecords()
	{
		return records.values();
	}
	
	private void addExistedID(int id)
	{
		IDManager.INSTANCE().addID(id);
	}
	
	public boolean isEditable()
	{
		return isEditable;
	}
	
	public void setEditable()
	{
		isEditable = !isEditable;
	}
	
	public Record addNewRecord()
	{
		Record record = new Record(IDManager.INSTANCE().getRandomInteger(), IProtectorConstants.TXT_DEFAULT_RECORD_NAME);
		records.put(record.getId(), record);
		dbController.insertNewRecord(record.getId(), record.getName());
		logger.info("Add New Record: {}", record.toString());
		return record;
	}

	public void setSelectedRecord(Record record)
	{
		selectedRecord = record;
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

	public RecordItem getNewRecordItem() {
		if( null == selectedRecord )
		{
			return null;
		}
		
		RecordItem recordItem = new RecordItem(selectedRecord.getId(), IDManager.INSTANCE().getRandomInteger(), IProtectorConstants.TXT_DEFAULT_RECORD_ITEM_NAME, IProtectorConstants.TXT_DEFAULT_RECORD_ITEM_NAME);
		dbController.insertNewItem(recordItem.getRecordId(), recordItem.getItemId(), recordItem.getKey(), recordItem.getValue());
		List<RecordItem> recordItems = items.get(recordItem.getRecordId());
		
		if( null == recordItems )
		{
			recordItems = new ArrayList<RecordItem>();
			items.put(recordItem.getRecordId(), recordItems);
		}
		
		recordItems.add(recordItem);
		
		logger.info("Add New Record Item: {}", recordItem.toString());
		
		return recordItem;
	}

	public void createSearchRecords(String text)
	{
		Collection<Record> rds = records.values();
		showSearchRecords.clear();
		
		Iterator<Record> it = rds.iterator();
		
		while(it.hasNext())
		{
			Record rd = it.next();
			
			if(rd.getName().toLowerCase().contains(text.toLowerCase()))
			{
				showSearchRecords.add(rd);
			}
		}
	}
	
	public Collection<Record> getSearchRecords()
	{
		return showSearchRecords;
	}
}
