package bloodasp.galacticgreg.auxiliary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bloodasp.galacticgreg.api.ModDimensionDef;

/**
 * A simple FIFO-storage for Long-values
 * Will keep 50 values for each dimension in memory
 * Doesn't need to be changed when adding new planets/mods
 */
public class ProfilingStorage {
	private Map<String, List<Long>> mProfilingMap;
	
	public ProfilingStorage()
	{
		mProfilingMap = new HashMap<String, List<Long>>();
	}
	
	/**
	 * Add a new time to the list of pDimension. Will be ignored it tTotalTime == 0
	 * @param pDimension
	 * @param tTotalTime
	 */
	public void AddTimeToList(ModDimensionDef pDimension, long pTotalTime)
	{
		try
		{
			if (pTotalTime == 0)
				return;
			
			if(!mProfilingMap.containsKey(pDimension.getDimIdentifier()))
				mProfilingMap.put(pDimension.getDimIdentifier(), new LinkedList<Long>());
	
			LinkedList<Long> ll = (LinkedList<Long>) mProfilingMap.get(pDimension.getDimIdentifier());
			
			ll.addLast(pTotalTime);
			
			while(ll.size() > 50)
				ll.removeFirst();
		} catch (Exception e)
		{
			// Just do nothing. profiling is for debug purposes only anyways...
		}
	}
	
	/**
	 * Return the average time required to execute the oregen in Dimension pDimension
	 * @param pDimension The DimensionType in question
	 * @return
	 */
	public long GetAverageTime(ModDimensionDef pDimension)
	{
		try
		{
			if (!mProfilingMap.containsKey(pDimension.getDimIdentifier()))
				return -1;
			
			int tTotalVal = 0;
			long tAverage = 0;
			long tReturnVal = 0;
			
			LinkedList ll = (LinkedList) mProfilingMap.get(pDimension.getDimIdentifier());
			
			if(ll != null)
			{
				Iterator<Long> qItr = ll.iterator();
				while(qItr.hasNext())
				{
					tAverage += qItr.next();
					tTotalVal++;
				}
				
				tReturnVal = (long)((float)(tAverage / tTotalVal));
			}
			return tReturnVal;
		}
		catch (Exception e)
		{
			return -1;
		}
	}
}
