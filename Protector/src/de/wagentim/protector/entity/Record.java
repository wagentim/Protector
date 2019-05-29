package de.wagentim.protector.entity;

import java.io.Serializable;

import de.wagentim.common.IConstants;


public class Record implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private String name = IConstants.EMPTY_STRING;
	
	public void setName(String name)
	{
		this.name = name;
	}

	public Record(final int id)
	{
		this.id = id;
	}
	
	public Record(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "BlockEntity [id=" + id + ", name=" + name + "]";
	}
}
