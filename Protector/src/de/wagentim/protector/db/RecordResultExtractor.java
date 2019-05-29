package de.wagentim.protector.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.wagentim.common.db.IResultSetExtractor;
import de.wagentim.protector.entity.Record;

public class RecordResultExtractor implements IResultSetExtractor<List<Record>>
{

	@Override
	public List<Record> extractData(ResultSet rs)
	{
		if( null != rs )
		{
			List<Record> result = new ArrayList<Record>();
			
			try
			{
				while(rs.next())
				{
					Record record = new Record(rs.getInt(ISQLConstants.RECORD_ID), rs.getString(ISQLConstants.RECORD_NAME));
					result.add(record);
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
