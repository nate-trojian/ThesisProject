package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class HashBucket<T1,T2,T3>
{
	HashMap<T1,HashMap<T2,T3>> map;
	public HashBucket()
	{
		map = new HashMap<T1,HashMap<T2,T3>>();
	}

	public void addBucket(T1 name)
	{
		map.put(name, new HashMap<T2,T3>());
	}

	public void addAllBuckets(Collection<T1> c)
	{
		Iterator<T1> it = c.iterator();
		while(it.hasNext())
		{
			addBucket(it.next());
		}
	}

	public void addToBucket(T1 name, T2 key, T3 value)
	{
		if(!map.containsKey(name) || key==null || value==null)
			return;
		map.get(name).put(key, value);
	}

	public void removeFromBucket(T1 name, T2 key)
	{
		map.get(name).remove(key);
	}

	public void removeBucket(T1 name)
	{
		map.remove(name);
	}

	public void removeAllBuckets(Collection<T1> c)
	{
		Iterator<T1> it = c.iterator();
		while(it.hasNext())
		{
			removeBucket(it.next());
		}
	}

	public void dumpBucket(T1 name)
	{
		map.get(name).clear();
	}
	
	public void dumpAllBuckets(Collection<T1> c)
	{
		Iterator<T1> it = c.iterator();
		while(it.hasNext())
		{
			dumpBucket(it.next());
		}
	}

	public HashMap<T2,T3> getBucket(T1 name)
	{
		return map.get(name);
	}

	public T3 getFromBucket(T1 name, T2 key)
	{
		return map.get(name).get(key);
	}

	public boolean bucketContains(T1 name, T3 value)
	{
		return map.get(name).containsValue(value);
	}

	public ArrayList<T1> bucketsContaining(T2 t)
	{
		ArrayList<T1> ret = new ArrayList<T1>();
		Iterator<T1> it = map.keySet().iterator();
		while(it.hasNext())
		{
			T1 temp = it.next();
			if(map.get(temp).containsKey(t))
			{
				ret.add(temp);
			}
		}
		return ret;
	}

	public Iterator<T1> keyIterator()
	{
		return map.keySet().iterator();
	}

	public Iterator<HashMap<T2,T3>> bucketIterator()
	{
		return map.values().iterator();
	}

	public Iterator<T3> valuesIterator(T1 name)
	{
		return map.get(name).values().iterator();
	}
}