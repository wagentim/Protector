package de.wagentim.protector.db;

public interface ISQLConstants
{
	public static final String RECORD_ID = "record_id";
	public static final String RECORD_NAME = "record_name";
	public static final String ITEM_ID = "item_id";
	public static final String ITEM_KEY = "item_key";
	public static final String ITEM_VALUE = "item_value";
	public static final String SQLITE_JDBC = "org.sqlite.JDBC";
	public static final String SQLITE_FILE_PATH = "D:\\tmp\\myDB.sqlite3";
	public static final String SQLITE_CONNECTION = "jdbc:sqlite:" + SQLITE_FILE_PATH;
}
