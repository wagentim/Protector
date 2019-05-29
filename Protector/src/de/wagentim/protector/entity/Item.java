package de.wagentim.protector.entity;

import java.io.Serializable;

import de.wagentim.common.IConstants;

public class Item implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8554133090987357957L;
	
	private String key = IConstants.EMPTY_STRING;
	private String value = IConstants.EMPTY_STRING;
	private final int recordId;
	private final int itemId;
	
	public Item(final int blockId, final int itemId)
	{
		super();
		this.recordId = blockId;
		this.itemId = itemId;
	}
	
	public int getRecordId()
	{
		return recordId;
	}

	public int getItemId()
	{
		return itemId;
	}

	public Item(final int blockId, final int itemId, String key, String value)
	{
		super();
		this.key = key;
		this.value = value;
		this.recordId = blockId;
		this.itemId = itemId;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "KeyValuePair [key=" + key + ", value=" + value + ", blockId=" + recordId + ", itemId=" + itemId + "]";
	}
}
