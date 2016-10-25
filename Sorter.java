package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		if(lr == 'l')
		{
			int c = 0;
			Interval temp = new Interval(0,0,null);
			while(c < intervals.size())
			{
				for(int counter = 0; counter < intervals.size() - 1;counter++)
				{
					if(intervals.get(counter+1).leftEndPoint<intervals.get(counter).leftEndPoint)
					{
						temp = intervals.get(counter);
						intervals.set(counter,intervals.get(counter+1));
						intervals.set(counter+1,temp);
						
					}
				}
				c++;
			}
		}
		if(lr == 'r')
		{
			int c = 0;
			Interval temp = new Interval(0,0,null);
			while(c < intervals.size())
			{
				for(int counter = 0; counter < intervals.size() - 1;counter++)
				{
					if(intervals.get(counter+1).rightEndPoint<intervals.get(counter).rightEndPoint)
					{
						temp = intervals.get(counter);
						intervals.set(counter,intervals.get(counter+1));
						intervals.set(counter+1,temp);
						
					}
				}
				c++;
			}
		}
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		int c = leftSortedIntervals.size() + rightSortedIntervals.size();
		ArrayList<Integer> dup = new ArrayList<Integer>(c);
		for(int counter = 0;counter<c;counter++)
		{
			if(counter<leftSortedIntervals.size())
			{
				dup.add(leftSortedIntervals.get(counter).leftEndPoint);
			}
			if(counter>=leftSortedIntervals.size())
			{
				dup.add(rightSortedIntervals.get(counter - leftSortedIntervals.size()).rightEndPoint);
			}
		}
		int max = 0;
		int min = dup.get(0);
		for(int counter = 0;counter<dup.size();counter++)
		{
			if(dup.get(counter)>max)
			{
				max = dup.get(counter);
			}
			if(dup.get(counter)<min)
			{
				min = dup.get(counter);
			}
		}
		ArrayList<Integer> nodup = new ArrayList<Integer>();
		for(int counter = min;counter<=max;counter++)
		{
			if(dup.contains(counter))
			{
				nodup.add(counter);
			}
		}
		return nodup;
	}
}
