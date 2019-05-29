package de.wagentim.protector.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.wagentim.common.db.IResultSetExtractor;
import de.wagentim.protector.entity.Item;

public class ItemResultExtractor implements IResultSetExtractor<List<Item>>
{

	@Override
	public List<Item> extractData(ResultSet rs)
	{
		if( null != rs )
		{
			List<Item> result = new ArrayList<Item>();
			
			try
			{
				while(rs.next())
				{
					Item item = new Item(rs.getInt(ISQLConstants.RECORD_ID), rs.getInt(ISQLConstants.ITEM_ID), rs.getString(ISQLConstants.ITEM_KEY), rs.getString(ISQLConstants.ITEM_VALUE));
					result.add(item);
				}
				
				return result;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Collections.emptyList();
	}

}
