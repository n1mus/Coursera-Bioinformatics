package util;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;



public class DAG {
	private Node source;
	private Node sink;
	private DynamicNodeArray nodesArray; // keeps references to all added nodes
	
	
	public DAG (int sourceId, int sinkId) {
		
		nodesArray = new DynamicNodeArray();
		
		 source = new Node(sourceId);
		 sink = new Node(sinkId);
		
		nodesArray.add(sourceId, source);
		nodesArray.add(sinkId, sink);

	}
	
	
	
	/**
	 * All editing of nodes/edges must occur here or ctor
	 * 
	 * @param edgeStr "0->1:7" means "an edge from node 0 to node 1 with weight 7"
	 */
	public void parseAddNodesEdge(String edgeStr) {
		
		String[] firstSplit = edgeStr.split("->");
		
		int parentId = Integer.parseInt(firstSplit[0]);

		String[] secondSplit = firstSplit[1].split(":");
		
		int childId = Integer.parseInt(secondSplit[0]);;
		int weight = Integer.parseInt(secondSplit[1]);
		
		
		Node parent = nodesArray.exists(parentId) ? nodesArray.get(parentId) : new Node(parentId);
		Node child = nodesArray.exists(childId) ? nodesArray.get(childId) : new Node(childId);

		
		Edge edge = new Edge(parent, child, weight);
		
		parent.addOutgoingEdge(edge);
		child.addIncomingEdge(edge);
		
		nodesArray.add(parentId, parent);
		nodesArray.add(childId, child);
	}



	private void addNode(int id) {
		
	}
	
	
	
	
	
	
	
	public void printDAG() {
		for(int i=0; i<nodesArray.size(); i++) {
			Node n = nodesArray.get(i);
			if(n != null)
				n.print();
		}
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param $longestPath first index is set to longest path in format "1->5->60->" etc.
	 * @return length of longest path
	 */
	public int longestPath(String[] $longestPath) {
		
		int[] s = new int[nodesArray.size()];
		int[] backtrack = new int[nodesArray.size()];
		boolean[] deadEnd = new boolean[nodesArray.size()];
		
		
		// initialize longest paths to -1
		// -1 means node_i has not been visited yet by the the recursive algorithm
		for(int i=0; i<s.length; i++) {
			s[i] = -1;
		}
		
		
		longestPathLengthRecursive(this.sink.getId(), s, backtrack, deadEnd);
		$longestPath[0] = longestPathRecursive(this.sink.getId(), backtrack);
		
		return s[this.sink.getId()];
	}
	
	
	/**
	 * 
	 * @param n
	 * @param backtrack
	 * @return
	 */
	private String longestPathRecursive(int n, int[] backtrack) {
		
		// BC
		if( n == this.source.getId() ) return String.valueOf(this.source.getId());
		
		// RC
		
		
		int parentId = backtrack[n];
		String antePath = longestPathRecursive(parentId, backtrack);
		
		
		return antePath + "->" + n;
	}
	
	
	
	
	
	
	
	
	/**
	 * Call using the sink node
	 * Starting from sink node, backtracks
	 * visiting each node
	 * and fills s[n] with longest path leading to node n
	 * 
	 * 
	 * @param n index of current node being visited
	 * @param s s[i] holds length of longest path leading to node_i
	 * @param backtrack backtrack[i] holds index of parent that has longest path leading from source node to node_i
	 * 
	 */
	private void longestPathLengthRecursive(int n, int[] s, int[] backtrack, boolean[] deadEnd) {
		
		if(s[n] > -1) return; // BC already visited this node
		if(n==this.source.getId()) { // BC hit the source node
			s[n] = 0;
			return;
		}
		
		
		
		Node currentNode = nodesArray.get(n);
		
		
		ArrayList<Node> parents = currentNode.getParents();

		
		// RC
		// visit all parent nodes
		
		for(Node parent : parents) {
			longestPathLengthRecursive(parent.getId(), s, backtrack, deadEnd);
		}
		

		
		
		ArrayList<Edge> nonDeadEndIncomingEdges = currentNode.getNonDeadEndIncomingEdges(deadEnd);

		// BC again - all parents dead ends or no parents
		// so dead end
		if( nonDeadEndIncomingEdges.size() == 0) {
			deadEnd[n] = true;
			s[n] = 0;
			return;
		}
		
		
		// find parent+length with longest path
		// hold answer in this array
		
		int $parentIdAndMaxLength[] = {-1, -1};
		
		longestPathAndParent(nonDeadEndIncomingEdges, s, $parentIdAndMaxLength);
		
		int parentId = $parentIdAndMaxLength[0];
		int maxLength = $parentIdAndMaxLength[1];
		
		s[n] = maxLength;
		backtrack[n] = parentId;
		
		
	}
	
	
	
	
	/**
	 * 
	 * @param incomingEdges to current node
	 * @param s holds the lengths (nonnegative) of longest paths
	 * @param $parentIdAndMaxLength initialized to {-1,-1}, set first element to id of parent with greatest path to current node, set second element to max length
	 */
	private void longestPathAndParent(ArrayList<Edge> incomingEdges, int[] s,  int[] $parentIdAndMaxLength) {
		ArrayList<Integer> lengths = new ArrayList<Integer>();
		
		for(Edge e : incomingEdges) {
			int length = e.getWeight() + s[e.getParent().getId()];
			lengths.add(new Integer(length));
		}
		
		argmaxAndMax(lengths,  $parentIdAndMaxLength); // find argmax and max
		
		// change parentIndex into parentId
		
		$parentIdAndMaxLength[0] = incomingEdges.get($parentIdAndMaxLength[0]).getParent().getId();
	}
	
	/**
	 * Find argmax and max
	 * 
	 * 
	 * @param values nonnegative
	 * @param $argmaxAndMax initialized to {-1,-1}, set first element to argmax, set second element to max
	 */
	private void argmaxAndMax(ArrayList<Integer> values, int[] $argmaxAndMax) {
		for(int i=0; i<values.size(); i++) {
			int value = values.get(i).intValue();
			if (value > $argmaxAndMax[1]) {
				$argmaxAndMax[0] = i;
				$argmaxAndMax[1] = value;
			}
		}
	}
	
	
	
	
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	private class Node {
		private int id;
		private ArrayList<Edge> incomingEdges;
		private ArrayList<Edge> outgoingEdges;
		
		public Node(int id) {
			this.setId(id);
			incomingEdges = new ArrayList<Edge>();
			outgoingEdges = new ArrayList<Edge>();
		}
		
		public void addIncomingEdge(Edge edge) {
			incomingEdges.add(edge);
		}

		
		public void addOutgoingEdge(Edge edge) {
			outgoingEdges.add(edge);
		}
		
		public ArrayList<Edge> getIncomingEdges() {
			return incomingEdges;
		}
		
		public ArrayList<Edge> getNonDeadEndIncomingEdges(boolean[] deadEnd) {
			ArrayList<Edge> nonDeadEndIncomingEdges = new ArrayList<Edge>();
			
			for(Edge e : incomingEdges) {
				int parentId = e.getParent().getId();
				if(! deadEnd[parentId]) {
					nonDeadEndIncomingEdges.add(e);
				}
			}
			
			return nonDeadEndIncomingEdges;
		}


		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public ArrayList<Node> getParents() {
			
			ArrayList<Node> parents = new ArrayList<Node>();
			
			for(Edge e : incomingEdges) {
				parents.add(e.getParent());
			}
			
			return parents;
		}
		
		
		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? false : ((Node)o).id == this.id;
		}
		
		public void print() {
			for(Edge e : outgoingEdges) {
				e.print();
			}
		}
	}

	private class Edge {
		private Node parent;
		private Node child;
		private int weight;
		
		public Edge(Node parent, Node child, int weight) {
			this.setParent(parent);
			this.setChild(child);
			this.setWeight(weight);
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public Node getChild() {
			return child;
		}

		public void setChild(Node child) {
			this.child = child;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
		
		public void print() {
			String str = "" + parent.getId() + "->" + child.getId() + ":" + weight;
			System.out.println(str);
		}
	}

	private class DynamicNodeArray {
		private Node[] underlyingArray;
		private int size = 100;
		private int resizeFactor = 3;
		private TreeSet<Integer> nodes;
		
		
		public DynamicNodeArray() {
			underlyingArray = new Node[size];
			nodes = new TreeSet<Integer>();
		}
		
		
		public void add(int index, Node e) {
			nodes.add(new Integer(index));
			
			if(index >= size) 
			
				resize(index);
			
			underlyingArray[index] = e;
		}
		
		public boolean exists(int index) {
			if(underlyingArray[index] == null) return false;
			else return true;
		}
		
		public Node get(int index) {
			return underlyingArray[index];
		}
		
		public TreeSet<Integer> getNodes() {
			return nodes;
		}
		
		public int numNodes() {
			return nodes.size();
		}
		
		public int size() {
			return size;
		}
		
		private void resize(int outOfBoundsIndex) {
			int newSize = outOfBoundsIndex * resizeFactor;
			Node[] newUnderlyingArray = new Node[newSize];
			
			// copy contents into new underlying array
			for(int i=0; i<size; i++) {
				newUnderlyingArray[i] = underlyingArray[i];
			}
			
			size = newSize;
			underlyingArray = newUnderlyingArray;
		}
		
	}
}








