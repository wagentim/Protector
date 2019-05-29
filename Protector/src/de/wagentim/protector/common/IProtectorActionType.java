package de.wagentim.protector.common;

public interface IProtectorActionType
{
	public static final int ACTION_DATA_LOADED = 0x00;
	public static final int ACTION_RECORD_SELECTED = 0x01;
	public static final int ACTION_LOG_WRITE_INFO = 0x02;
	public static final int ACTION_LOG_WRITE_ERROR = 0x03;
	public static final int ACTION_LOG_WRITE_WARNING = 0x04;
}
