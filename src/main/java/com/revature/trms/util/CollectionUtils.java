package com.revature.trms.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtils
{
	public static <T> Set<T> setOf(T... things)
	{
		Set<T> set = new HashSet<>();
		for (T thing : things)
		{
			set.add(thing);
		}
		return set;
	}
}
