package de.wagentim.protector.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.wagentim.common.db.SqliteDBHandler;
import de.wagentim.protector.entity.Item;
import de.wagentim.protector.entity.Record;

public class SqliteDBController implements IDBController
{
	private final SqliteDBHandler handler;
	private final StringBuffer sb;
	
	public SqliteDBController()
	{
		handler = new SqliteDBHandler();
		sb = new StringBuffer();
	}

	public boolean createRecordTable()
	{
		String state = "CREATE TABLE record (record_id INTEGER PRIMARY KEY, record_name STRING NOT NULL);";
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(state) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean createItemTable()
	{
		String state = "CREATE TABLE item (record_id INTEGER, item_id INTEGER, item_key STRING NOT NULL, item_value STRING NOT NULL);";
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(state) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean insertNewRecord(int id, String name)
	{
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO record (").append("record_id, ").append("record_name) ")
			.append("VALUES (").append(id).append(", '").append(name).append("')");
		
//		System.out.println(sb.toString());
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean insertNewItem(int recordId, int itemId, String key, String value)
	{
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO item (").append("record_id, ").append("item_id, ").append("item_key, ").append("item_value) ")
			.append("VALUES (").append(recordId).append(", ").append(itemId).append(", '").append(key).append("', '").append(value).append("')");
		
//		System.out.println(sb.toString());
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Map<Integer, Record> getAllRecords()
	{
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM record");
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			List<Record> records = handler.executeQuery(sb.toString(), new RecordResultExtractor());
			
			if( !records.isEmpty() )
			{
				Map<Integer, Record> result = new HashMap<Integer, Record>();
				
				Iterator<Record> it = records.iterator();
				
				while(it.hasNext())
				{
					Record r = it.next();
					result.put(r.getId(), r);
				}
				
				return result;
			}
			
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return Collections.emptyMap();
	}
	
	public Map<Integer, List<Item>> getAllItems()
	{
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM item");
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			List<Item> items = handler.executeQuery(sb.toString(), new ItemResultExtractor());
			
			if( !items.isEmpty() )
			{
				Map<Integer, List<Item>> result = new HashMap<Integer, List<Item>>();
				
				Iterator<Item> it = items.iterator();
				
				while(it.hasNext())
				{
					Item i = it.next();
					int recordID = i.getRecordId();
					
					List<Item> list = result.get(recordID);
					
					if( null == list )
					{
						list = new ArrayList<Item>();
						result.put(recordID, list);
					}
					
					list.add(i);
				}
				
				return result;
			}
			
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return Collections.emptyMap();
	}

	@Override
	public boolean updateRecordName(int id, String newValue)
	{
		sb.delete(0, sb.length());
		
		sb.append("UPDATE record SET record_name = '").append(newValue).append("' WHERE record_id = ").append(id).append(";");
		
//		System.out.println(sb.toString());
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateItem(int record_id, int item_id, String newKey, String newValue)
	{
		sb.delete(0, sb.length());
		
		sb.append("UPDATE item SET item_key = '").append(newKey).append("', item_value = '").append(newValue).append("' WHERE record_id = ").append(record_id).append(" AND item_id = ").append(item_id).append(";");
		
//		System.out.println(sb.toString());
		
		handler.openDB(ISQLConstants.SQLITE_JDBC, ISQLConstants.SQLITE_CONNECTION);
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
