package de.wagentim.protector.common;

import de.wagentim.common.AbstractActionManager;

public class ProtectorActionManager extends AbstractActionManager
{
	public static ProtectorActionManager actionManager = new ProtectorActionManager();
	
	public ProtectorActionManager()
	{
		super();
	}
	
	public static ProtectorActionManager INSTANCE()
	{
		return actionManager;
	}
	
}
