package de.wagentim.protector.common;

public interface IProtectorActionType
{
	public static final int ACTION_DATA_LOADED = 0x00;
	public static final int ACTION_RECORD_SELECTED = 0x01;
	public static final int ACTION_LOG_WRITE_INFO = 0x02;
	public static final int ACTION_LOG_WRITE_ERROR = 0x03;
	public static final int ACTION_LOG_WRITE_WARNING = 0x04;
	public static final int ACTION_RECORD_ITEM_SELECTED = 0x05;
	public static final int ACTION_ADD_NEW_RECORD = 0x06;
	public static final int ACTION_EDITING_STATUS_CHANGED = 0x07;
	public static final int ACTION_ADD_NEW_RECORD_ITEM = 0x08;
	public static final int ACTION_SEARCH_RECORD_UPDATED = 0x09;
	public static final int ACTION_FOCUS_SEARCH = 0x0A;
	public static final int ACTION_FOCUS_TREE = 0x0B;
	public static final int ACTION_FOCUS_TABLE = 0x0C;
}
