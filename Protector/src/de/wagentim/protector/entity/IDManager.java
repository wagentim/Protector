package de.wagentim.protector.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IDManager
{
	private static final IDManager generator = new IDManager();
	private final Random random;
	private final List<Integer> list;
	
	public static final IDManager INSTANCE()
	{
		return generator;
	}
	
	private IDManager()
	{
		random = new Random();
		list = new ArrayList<Integer>();
	}
	
	public int getRandomInteger()
	{
		int result;
		
		do
		{
			result = random.nextInt((999999 - 100000) + 1) + 100000;
		}
		while(list.contains(result));
		
		addID(result);
		
		return result;
	}

	private void addID(int id)
	{
		if(!list.contains(id))
		{
			list.add(id);
		}
	}
}
