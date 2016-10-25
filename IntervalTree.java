package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
			
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
		
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		
		for (int p : endPoints){
			IntervalTreeNode T = new IntervalTreeNode((float)p,(float)p,(float)p);
			T.leftIntervals = new ArrayList<Interval>();
			T.rightIntervals = new ArrayList<Interval>();
			Q.enqueue(T);
		}
			
		int s = Q.size;
				
		while (s > 0){
			if (s == 1){
				return Q.dequeue();
			}	
			else {
				int temps = s;
				while (temps > 1){
					IntervalTreeNode T1 = Q.dequeue();
					IntervalTreeNode T2 = Q.dequeue();
					float v1 = T1.maxSplitValue;
					float v2 = T2.minSplitValue;
					float x = (v1+v2)/(2);
					IntervalTreeNode N = new IntervalTreeNode(x, T1.minSplitValue, T2.maxSplitValue);
					N.leftIntervals = new ArrayList<Interval>();
					N.rightIntervals = new ArrayList<Interval>();
					N.leftChild = T1;
					N.rightChild = T2;
					Q.enqueue(N);
					temps = temps-2;
				}
				if (temps == 1){
					IntervalTreeNode dequeue = Q.dequeue();
					Q.enqueue(dequeue);
				}
			s = Q.size;
			}	
		}		
		return Q.dequeue();
	}
		
	private void mapLeft(Interval x, IntervalTreeNode r){
		if (x.contains(r.splitValue)){
			r.leftIntervals.add(x);
			return;
		}
		if (r.splitValue < x.leftEndPoint){
			mapLeft(x, r.rightChild);
		}
		else {
			mapLeft(x, r.leftChild);
		}
	}
	
	private void mapRight(Interval x, IntervalTreeNode r){
		if (x.contains(r.splitValue)){
			r.rightIntervals.add(x);
			return;
		}
		if (r.splitValue < x.leftEndPoint){
			mapRight(x, r.rightChild);
		}
		else {
			mapRight(x, r.leftChild);
		}
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		for (Interval x : leftSortedIntervals){
			mapLeft(x, root);
		}
		for (Interval x : rightSortedIntervals){
			mapRight(x, root);
		}
		return;
	}
		
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		return Query(root, q);
	}
	
	private ArrayList<Interval> Query(IntervalTreeNode R, Interval q){
		ArrayList<Interval> ResultList = new ArrayList<Interval>();

		float splitVal = R.splitValue;
		ArrayList<Interval> Llist = R.leftIntervals;
		ArrayList<Interval> Rlist = R.rightIntervals;
		IntervalTreeNode Lsub = R.leftChild;
		IntervalTreeNode Rsub = R.rightChild;
			
		if(Lsub == null && Rsub == null)
		{
			return ResultList;
		}
		else if (q.contains(splitVal)){
			for (Interval x : Llist){
				ResultList.add(x);
			}
			ResultList.addAll(Query(Rsub, q));
			ResultList.addAll(Query(Lsub, q));
		}		
		else if (splitVal < q.leftEndPoint){
			int i = Rlist.size()-1;
			while (i >= 0 && (Rlist.get(i).intersects(q))){
				ResultList.add(Rlist.get(i));
				i = i-1;
			}
			ResultList.addAll(Query(Rsub, q));
		}		
		else if (splitVal > q.rightEndPoint){
			int i = 0;
			while ((i < Llist.size()) && (Llist.get(i).intersects(q))){
				ResultList.add(Llist.get(i));						
				i = i+1;
			}
			ResultList.addAll(Query(Lsub, q));
		}
		return ResultList;
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

