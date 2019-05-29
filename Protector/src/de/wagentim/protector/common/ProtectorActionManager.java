package de.wagentim.protector.common;

import java.util.ArrayList;
import java.util.List;

import de.wagentim.common.IActionListener;

public class ProtectorActionManager
{
	public static ProtectorActionManager INSTANCE = new ProtectorActionManager();
	private List<IActionListener> listenerList = null;
	
	public ProtectorActionManager()
	{
		listenerList = new ArrayList<IActionListener>();
	}
	
	public void addActionListener(IActionListener listener)
	{
		if( !listenerList.contains(listener) )
		{
			listenerList.add(listener);
		}
	}
	
	public void removeActionListener(IActionListener listener)
	{
		if( !listenerList.isEmpty() )
		{
			listenerList.remove(listener);
		}
	}
	
	public void sendAction(final int type, final Object content)
	{
		if( !listenerList.isEmpty() )
		{
			for( int i = 0; i < listenerList.size(); i++ )
			{
				listenerList.get(i).receivedAction(type, content);
			}
		}
	}
}
