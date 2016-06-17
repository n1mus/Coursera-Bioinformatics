package util.deBruijn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import util.deBruijn.CycleTree;
















/**
 * 
 * 
 * Re-purposed from DirectedTree
 * 
 * 
 * 
 * T is the type of the payload in the nodes/edges
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */
public class DeBruijnGraph<P extends Comparable> {

	
	private DynamicNodeArray nodesTracker = new DynamicNodeArray(); // keeps references to all added nodes
	
	
	/*
	 * For constructing a global Eulerian cycle
	 * cycleTree is data structure,
	 * in which each node is an Eulerian subcycle
	 * 
	 * 
	 */
	CycleTree cycleTree; 
	
	
	
	
	/**
	 * 
	 * 
	 * Eulerian graphs are "balanced" and "strongly connected".
	 * Balanced means the indegree and outdegree are the same for each node.
	 * A directed graph is strongly connected 
	 * if it is possible to reach any node from every other node
	 * 
	 * 
	 * 
	 * @param adjacencyList represents an Eulerian directed graph, e.g.,
	 *	  		"	0 -> 3
	 *			     1 -> 0
	 *			     2 -> 1,6
	 *			     3 -> 2
	 *			     4 -> 2
	 *			     5 -> 4
	 *			     6 -> 5,8
	 *			     7 -> 9
	 *			     8 -> 7
	 *			     9 -> 6      "
	 * 			 
	 * 
	 */
	public
	void
	constructEulerianDirectedGraph(ArrayList<String> adjacencyList) {
		for(String adjacency : adjacencyList) {
			
			adjacency = adjacency.trim();
			
			
			String[] nodeIds = adjacency.split("->");
			
			int parentNodeId = Integer.parseInt(nodeIds[0].trim());
			Node parentNode = nodesTracker.exists(parentNodeId)? 
					nodesTracker.get(parentNodeId) :
					new Node(parentNodeId, new Integer(parentNodeId));
			
			
			String[] childrenNodeIdsStr = nodeIds[1].split(",");
			

			for(String childNodeIdStr : childrenNodeIdsStr) {
				int childNodeId = Integer.parseInt(childNodeIdStr.trim());
				
				Node childNode = nodesTracker.exists(childNodeId) ? 
						nodesTracker.get(childNodeId) : 
							new Node(childNodeId, new Integer(childNodeId));

				new Edge(parentNode, childNode);
			}
		}
	}

	
	
	
	
	
	/**
	 * k from 
	 * composition_k(dnaSequence)
	 * 
	 * j  = k-1
	 * 
	 * 
	 * kmers go into edges
	 * "jmers" go into nodes
	 * 
	 * 
	 * 
	 * kmers and "jmers" are directly obtained
	 * from a dna sequence, in the order in which
	 * they are fragmented
	 * 
	 * 
	 * @param kmersList Strings
	 * @param jmersList Strings
	 */
	public void constructDBGraph(ArrayList<P> kmersList, ArrayList<P> jmersList) {
		
		
		/*
		 * create nodes from jmers
		 */
		
		this.addNodes(jmersList);
		
		
		
		/*
		 * iterate through,
		 * create edges from appropriate nodes
		 */
		
		for (int i = 0; i < kmersList.size(); i++) {
			P kmer = kmersList.get(i);
			new Edge<P>(getNode(i), getNode(i+1), kmer);
		}
		
		
	}
	
	
	
	/**
	 * 
	 * kmers are randomly ordered
	 * 
	 * (at noob level, kmers are composition(dna sequence) )
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public
	void constructDBGraph(ArrayList<String> kmersList) {
		
		for(String kmer : kmersList) {
			
			Node node1 = new Node(prefix(kmer));
			Node node2 = new Node(suffix(kmer));
			
			Edge edge = new Edge(node1, node2, kmer);
			
			
		}
		
				
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * Given a strongly connected, balanced dB graph,
	 * construct a tree of Eulerian subcycles
	 * (cycles where each edge is visited just once in total).
	 * The tree will be traversed
	 * to find
	 * a global Eulerian cycle
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public void
	constructEulerianCyclesTree() {
		
		cycleTree = new CycleTree();
		
		
		Node dBNode = nodesTracker.getFirstNode(); // first cycle can start at any dB node
		DBCycleNode parentCycleNode = null; // first cycle parent is null
		
		
		// reset 'visited' flag or all dB edges
		resetEdgesVisitedGlobally();
		
		
		
		while(true) {

			Cycle cycle = createCycle(dBNode);
			cycleTree.addDBCycleNode(parentCycleNode, cycle, dBNode.id);
			
			// prep vars for next round
			parentCycleNode = cycleTree.getDBCycleNodeWUnvisitedEdges();
			if(parentCycleNode == null) break; // no more unvisited edges!
			dBNode = parentCycleNode.getDBNodeWUnvisitedEdges();
		}
		
		
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * Follow any cycle
	 * of unvisited edges,
	 * starting at dBNodeStart,
	 * 
	 * 
	 * Because we're talking about a strongly connected, balanced, directed graph,
	 * any cycle can only end on the node it started with
	 * 
	 * 
	 * 
	 * @param dBNodeStart the intersection with parent cycle
	 * @return
	 */
	private
	Cycle
	createCycle(Node dBNodeStart) {
		
		
		Cycle cycle = new Cycle();

		Node dBNode = dBNodeStart;
		Edge unvisitedEdge = null;
		
		do {
			

			// officially visit node
			cycle.appendNode(dBNode);

		
			// find unvisited edge
			unvisitedEdge = dBNode.getUnvisitedOutgoingEdge();
			
			
			
			// officially visit outgoing edge
			unvisitedEdge.visited = true;
			
			// move node cursor to child
			dBNode = unvisitedEdge.getChild();
			
			
		}
		// stop after you've visited starting node again
		while(dBNode != dBNodeStart); 
		
		
		// complete cycle
		// visiting node you started on
		cycle.appendNode(dBNode);
		

		
		
		return cycle;
	}
	
	
	
	
	public ArrayList<Integer> traverseEulerianCycle() {
		resetEdgesVisitedGlobally();
		
		return cycleTree.traverseEulerianCycle();
	}
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param id
	 * @param payload
	 */
	void addNode(int id, P payload) {
		Node<P> node = new Node<P>(id, payload);
	}
	

	void deleteNode(Node node) {
		nodesTracker.delete(node);
	}
	
	
	
	private Node<P> getNode(int id) {
		return nodesTracker.get(id);
	}
	
	
	
	/**
	 * Add payloaded nodes, 
	 * with ids corresponding to 
	 * the order in payload list (starting from 0)
	 * 
	 * 
	 * 
	 * 
	 * Node id is index in ArrayList<T>
	 * T payload is corresponding element in the ArrayList<T>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param payloads
	 */
	public void addNodes(ArrayList<P> payloads) {
		
		for(int i=0; i<payloads.size(); i++) {
			Node<P> node = new Node<P>(i, payloads.get(i));
			nodesTracker.add(i, node);
		}
		
	}
	
	
	
	
	
	
	/**
	 * Nodes already added
	 * Reference to this edge is given to the nodes
	 * 
	 * 
	 * @param parentId
	 * @param childId
	 */
	public void addEdge(int parentId, int childId) {
		new Edge(parentId, childId);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * For nodes that have same payload,
	 * merge the nodes into 1
	 * that has all the incoming and outgoing edges
	 * 
	 * 
	 */
	public void mergeRepeatNodes() {
		
		ArrayList<Node> nodesList = nodesTracker.getNodesList();
		Collections.sort(nodesList);
		
		/// iterate through nodes list,
		// except last element
		for(int i=0; i<nodesList.size()-1; i++) {
			Node currNode = nodesList.get(i);
			Node nextNode = nodesList.get(i+1);
			
			
			// hit a streak (2+) of repeated nodes
			if(currNode.hasSamePayload(nextNode)) {
				
				
				int j=i+1; // index of farthest known repeat
				
				// increment j until
				// j is at last repeat
				while(true) {
					int k = j+1; // next unknown element
					if(k == nodesList.size()) break; // no next element
													// end of list
													// end of repeats
					Node unknownNode = nodesList.get(k);
					
					// hit another repeat
					// still in streak
					if(currNode.hasSamePayload(unknownNode)) {
						j++;// j points to farthest known repeat
					}
					// didn't hit repeat
					// j at end of streak
					else {
						break;
					}
					
				} // END WHILE
				
				
				// aggregate the repeat nodes
				ArrayList<Node> repeatNodes = new ArrayList<Node>();
				for(int l=i; l<=j; l++) {
					repeatNodes.add(nodesList.get(l));
				}
				
				// move i to farthest known repeat
				i = j;
				
				// lastly, merge
				mergeRepeatNodes(repeatNodes);	
				
			}
			else { // didn't hit a repeat streak 
				// (of 2+ repeats)
				// keep iterating through nodes list
			}
		}
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private void mergeRepeatNodes(ArrayList<Node> repeatNodes) {
		ArrayList<Edge> allIncomingEdges = new ArrayList<Edge>();
		ArrayList<Edge> allOutgoingEdges = new ArrayList<Edge>();

		// iterate through repeat nodes
		// aggregate incoming and outgoing edges
		// then nodesTracker "forgets" about these repeat nodes
		for(Node node : repeatNodes) {
			allIncomingEdges.addAll(node.getIncomingEdges());
			allOutgoingEdges.addAll(node.getOutgoingEdges());
			nodesTracker.delete(node);
		}
		
		// create new node
		// to rule them all
		int newNodeId = nodesTracker.getNextIndex();
		Node newNode = new Node(newNodeId, repeatNodes.get(0).getPayload());
		
		
		// redirect the edges
		// to new node
		newNode.takeIncomingEdges(allIncomingEdges);
		newNode.takeOutgoingEdges(allOutgoingEdges);
				
	}
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private
	static
	String prefix(String str) {
		int length = str.length();
		return str.substring(0,length-1);
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private
	static
	String suffix(String str) {
		return str.substring(1,str.length());
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 */
	public void print() {
		for(String str : getPrint()) {
			System.out.println(str);
		}
	}
	
	public void printCycleTree() {
		cycleTree.print();
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	public ArrayList<String> getPrint() {

		ArrayList<String> print = new ArrayList<String>();
		
		
		ArrayList<Node> nodesList = nodesTracker.getNodesList();
		Collections.sort(nodesList);;
		for(Node node : nodesList) {

			if(node.hasChildren()) {
				String nodeStr = node.getPayload().toString() + " -> " + node.getChildrenPrint();
				print.add(nodeStr);
			}
		}
		
		return print;
	}
	
	
	
	
	
	
	
	
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * Set the 'visited' field 
	 * in all edges 
	 * back to false
	 * 
	 * 
	 * 
	 */
	void
	resetEdgesVisitedGlobally() {
		for(Node node : nodesTracker.getNodesList()) {
			ArrayList<Edge> incomingEdges = node.getIncomingEdges();
			for(Edge edge : incomingEdges) {
				edge.visited = false;
			}
		}
	}





	/**
	 * 
	 * 
	 * Each node is automatically registered with nodesTracker
	 * upon creation
	 * 
	 * 
	 * 
	 * 
	 * 
	 *
	 */
	class Node<P extends Comparable> implements Comparable<Node> {
		
		
		int id;
		private P payload;
		private ArrayList<Edge> incomingEdges = new ArrayList<Edge>();
		private ArrayList<Edge> outgoingEdges = new ArrayList<Edge>();;
		
		
		Node(P payload) {
			int id = nodesTracker.getNextIndex();
			this.id = id;
			this.payload = payload;
			
			nodesTracker.add(id, this);
		}
		
		
		Node(int id, P payload) {
			this.id = id;
			this.payload = payload;
			
			nodesTracker.add(id, this);
		}
		
		
		Node(int id) {
			this.id = id;
			
			nodesTracker.add(id, this);
		}
		
		
		void addIncomingEdge(Edge edge) {
			incomingEdges.add(edge);
		}
		
		void addIncomingEdges(ArrayList<Edge> edges) {
			incomingEdges.addAll(edges);
		}
		
		/**
		 * 
		 * 
		 * 
		 */
		void takeIncomingEdges(ArrayList<Edge> edges) {
			incomingEdges.addAll(edges);
			for(Edge edge : edges) {
				edge.replaceChild(this);
			}
		}
		
		void addOutgoingEdge(Edge edge) {
			outgoingEdges.add(edge);
		}
		
		
		/**
		 * Take reference to all these edges
		 * Replace parent on all these edges
		 * 
		 */
		void takeOutgoingEdges(ArrayList<Edge> edges) {
			outgoingEdges.addAll(edges);
			for(Edge edge : edges) {
				edge.replaceParent(this);
			}
		}
		
		void addOutgoingEdges(ArrayList<Edge> edges) {
			outgoingEdges.addAll(edges);
		}
		
		ArrayList<Edge> getIncomingEdges() {
			return incomingEdges;
		}
		
		ArrayList<Edge> getOutgoingEdges() {
			return outgoingEdges;
		}
		
		Edge getOutgoingEdge(Node nextNode) {
			for(Edge outgoingEdge : outgoingEdges) {
				Node child = outgoingEdge.getChild();
				if(child.equals(nextNode)) {
					return outgoingEdge;
				}
			}
			
			return null;
		}
		
		
		
		/**
		 * 
		 * 
		 * 
		 * 
		 * @return
		 */
		Edge
		getUnvisitedOutgoingEdge() {
			for(Edge edge : outgoingEdges) {
				
				if(!edge.visited()) return edge;
			}
			
			return null;
		}


		P getPayload() {
			return payload;
		}

		
		int getId() {
			return id;
		}

		
		void setId(int id) {
			this.id = id;
		}
		
		
		/**
		 * Source node?
		 * 
		 * @return
		 */
		boolean isSource() {
			return incomingEdges.size() == 0;
		}
		
		
		boolean hasChildren() {
			return outgoingEdges.size() > 0;
		}
		
		
		/**
		 * Graph is balanced (indegree = outdegree for each node)
		 * So in a strongly connected graph,
		 * any unvisited edges would exist in pairs (incoming + outgoing)
		 * So only need to check one set (either incoming or outgoing)
		 * 
		 * 
		 * 
		 * @return
		 */
		boolean hasUnvisitedEdges() {
			for(Edge edge : incomingEdges) {
				if(!edge.visited()) return true;
			}
			
			return false;
		}
		
		
		
		ArrayList<Node> getParents() {
			
			ArrayList<Node> parents = new ArrayList<Node>();
			
			for(Edge e : incomingEdges) {
				parents.add(e.getParent());
			}
			
			return parents;
		}
		
		
		ArrayList<Node> getChildren() {
			ArrayList<Node> children = new ArrayList<Node>();
			
			for(Edge e : outgoingEdges) {
				children.add(e.getChild());
			}
			
			return children;
		}
		
		
		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? false : ((Node)o).id == this.id;
		}
		
		
		
		public boolean hasSamePayload(Node n) {
			return this.payload.equals(n.payload);
		}
		
		
		
		/**
		 * 
		 * 
		 * Compares nodes by their payloads
		 * 
		 * 
		 * 
		 */
		@Override
		public
		int compareTo(Node other) {
			return payload.compareTo(other.payload);
		}
		
		
		
		
		
		/**
		 * 
		 * Print comma separated list of
		 * all nodes that this node is directed to
		 * (repeats inc)
		 * 
		 */
		String getChildrenPrint() {
			
			ArrayList<String> childrenPayload = new ArrayList<String>();
			
			for(Edge edge: outgoingEdges) {
				Node child = edge.getChild();
				childrenPayload.add(child.getPayload().toString());
			}
			
			if(childrenPayload.size() == 0 ) { // no outgoing edges
				return "";
			}
			
			Collections.sort(childrenPayload);
			
			
			String childrenStr = childrenPayload.get(0);
			for(int i=1; i<childrenPayload.size(); i++){
				childrenStr += "," + childrenPayload.get(i).toString();
			}
			
			return childrenStr;
		}

		
		String getPrint() {


			return "" + this.id + " -> " + getChildrenPrint();
		}
		

		/**
		 * Aggregate edge-strings
		 * 
		 * 
		 * 
		 * @param id
		 * @param payload
		 * @param weight
		 * @return
		 */
		ArrayList<String> getPrint(boolean id, boolean payload, boolean weight) {
			
			ArrayList<String> edgeStrList = new ArrayList<String>();
			
			for(Edge e : outgoingEdges) {
				String edgeStr = e.getPrint(id, payload, weight);
				edgeStrList.add(edgeStr);
			}
			
			return edgeStrList;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
	

	
	
	
	
	
	
	
	
	
	/**
	 * 
	 *
	 *
	 *
	 *
	 *
	 *
	 */
	private class Edge<P> {
		
		P payload;
		
		private Node parent;
		private Node child;
		private int weight;
		
		boolean visited = false;

		
		
		
		/**
		 * This edge is given to parent+child as reference
		 * 
		 * 
		 * @param parent
		 * @param child
		 */
		Edge(Node parent, Node child) {
			this.parent = parent;
			this.child = child;
			
			parent.addOutgoingEdge(this);
			child.addIncomingEdge(this);
		}
		
		
		
		/**
		 * This edge is given to parent+child as reference
		 * 
		 * 
		 * @param parentId
		 * @param childId
		 */
		Edge(Node parent, Node child, P payload) {
			
			this(parent, child);
			this.payload = payload;
		}
		
		
		
		/**
		 * 
		 * This edge is given to parent+child as reference
		 * 
		 * @param parent
		 * @param child
		 * @param weight
		 */
		Edge(Node parent, Node child, int weight) {
			
			this(parent, child);
			this.setWeight(weight);
		}

		
		
		
		/**
		 * Assumes: nodes already added
		 * This edge is given to parent+child as reference
		 * 
		 * 
		 * @param parentId
		 * @param childId
		 */
		Edge(int parentId, int childId) {
			Node parentNode = nodesTracker.get(parentId);
			Node childNode = nodesTracker.get(childId);
			
			this.setParent(parentNode);
			this.setChild(childNode);
			
			parentNode.addOutgoingEdge(this);
			childNode.addIncomingEdge(this);
		}
		
		
		
		
		
		
		
		
		
		
		Node getParent() {
			return parent;
		}
		

		void setParent(Node parent) {
			this.parent = parent;
		}

		void replaceParent(Node newParent) {
			this.parent = newParent;
		}
		
		Node getChild() {
			return child;
		}
		
		
		void setChild(Node child) {
			this.child = child;
		}


		void replaceChild(Node newChild) {
			this.child = newChild;
		}
		

		
		P getPayload() {
			return this.payload;
		}
		
		int getWeight() {
			return weight;
		}

		
		void setWeight(int weight) {
			this.weight = weight;
		}
		
		
		
		boolean
		visited() {
			return this.visited;
		}
		
		
		
		/**
		 * 
		 * @param id
		 * @param payload
		 * @param weight
		 */
		void print(boolean id, boolean payload, boolean weight) {

			String str = getPrint(id, payload, weight);


			System.out.println(str);
		}
		
		
		
		
		String getPrint() {
			return getPrint(true, false, false);
		}
		
		
		/**
		 * 
		 * 
		 * 
		 * @param id
		 * @param payload
		 * @param weight
		 * @return
		 */
		String getPrint(boolean id, boolean payload, boolean weight) {
			String parentStr = "";
			String childStr = "";
			
			if(id) {
				parentStr += parent.getId();
				childStr += child.getId();
			}
			
			if(payload) {
				parentStr += parent.getPayload();
				childStr += child.getPayload();
			}
			
			
			String str = parentStr + " -> " + childStr;
			
			
			if(weight) {
				str += ": " + weight;
			}
			
			return str;
		}
		

	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 *
	 * Nodes have ids (preferably 0, 1, 2, 3, ... for space/time efficiency)
	 * 
	 * Store node references in array, where its index == its id
	 * 
	 *
	 */
	private class DynamicNodeArray {
		
		
		private Node[] underlyingArray;
		private int capacity = 100;
		private int resizeFactor = 3;
		private int size = 0; // includes nodes that have been deleted
		
		
		
		DynamicNodeArray() {
			underlyingArray = new Node[capacity];
		}
		
		
		void add(int id, Node e) {
			
			size++;
			
			if(id >= capacity) 
			
				resize(id);
			
			underlyingArray[id] = e;
		}
		 
		
		void delete(Node node) {
			int id = node.getId();
			
			underlyingArray[id] = null;
		}
		
		
		boolean exists(int index) {
			if(index >= capacity) resize(index);
			
			return underlyingArray[index] != null;
			
		}
		
		
		Node get(int index) {
			return underlyingArray[index];
		}
		
		
		int getSize() {
			return size;
		}
		
		
		/**
		 * 
		 * 
		 * 
		 */
		int getNextIndex() {
			
			
			if(size >= capacity) {
				resize(capacity);
			}
			
			return size;
			
		}
		
		
		
		ArrayList<Node> getNodesList() {
			
			ArrayList<Node> nodesList = new ArrayList<Node> ();
			
			for(int i=0; i<capacity; i++) {
				if(underlyingArray[i] != null) {
					nodesList.add(underlyingArray[i]);
				}
			}
			
			return nodesList;
		}
		
		
		
		
		/**
		 * Get first node in data structure
		 * 
		 * 
		 * 
		 */
		Node
		getFirstNode() {
			for(int i=0; i<size; i++) {
				if(underlyingArray[i] != null) return underlyingArray[i];
			}
			
			return null;
		}
		
		
		
		
		
		int numNodes() {
			return size;
		}
		
		
		int size() {
			return size;
		}
		
		
		private void resize(int outOfBoundsIndex) {
			int newCap = outOfBoundsIndex * resizeFactor;
			Node[] newUnderlyingArray = new Node[newCap];
			
			// copy contents into new underlying array
			for(int i=0; i<capacity; i++) {
				newUnderlyingArray[i] = underlyingArray[i];
			}
			
			capacity = newCap;
			underlyingArray = newUnderlyingArray;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * Cycle on a directed graph
	 * e.g.,
	 * '4 -> 10 -> 11 -> 3 -> 6 -> 5 -> 12 -> 6 -> 4'
	 * 
	 * 
	 * So, shortest cycle would consist of 2 nodes
	 * '4 -> 4'
	 * 
	 * 
	 * First node is intersection with parent cycle.
	 * Then nodes proceed in orderly fashion,
	 * following the edges in the cycle,
	 * ending on the start node.
	 * 
	 * 
	 * 
	 * In general: Node ids don't have to be consecutive and beginning at 0
	 * Can have "holes" in ids procession
	 * 
	 * 
	 */
	class Cycle {
	
		
		
		// list of nodes representing directed cycle,
		// pretend it's a circular list
		// 
		// FIRST ELEMENT in nodesList is node intersecting with parent
		// then nodesList is ordered by order in directed cycle
		protected ArrayList<Node> nodesList = new ArrayList<Node>(); 
		
		
		
		
		public Cycle() {
			
		}
		
		
		
		
		
		
		/**
		 * Set the 'visited' field 
		 * in all edges 
		 * back to false
		 * 
		 * Should be in DeBruijnGraph class,
		 * but this is more convenient
		 * for coding in CycleTree
		 * 
		 * 
		 */
		void
		resetEdgesVisitedGlobally() {
			for(Node node : nodesTracker.getNodesList()) {
				ArrayList<Edge> incomingEdges = node.getIncomingEdges();
				for(Edge edge : incomingEdges) {
					edge.visited = false;
				}
			}
		}




		/**
		 * Finds next node in directed cycle
		 * 
		 * 
		 * 
		 * @return
		 */
		Node
		getNextNode(Node node) {
			int ind = getInternalInd(node);
			
			int nextInd = (ind + 1) % nodesList.size();
			
			return nodesList.get(nextInd);
		}
		
		
		
		
		/**
		 * Finds previous node in directed cycle
		 * 
		 * 
		 * 
		 * @return
		 */
		Node
		getPrecedingNode(Node node) {
			int ind = getInternalInd(node);
			
			int precedingInd = (ind - 1) % nodesList.size();
			
			return nodesList.get(precedingInd);
		}
		
		
		
		
		/**
		 * Get id of node in nodesList
		 * 
		 * 
		 * 
		 * 
		 * @return
		 */
		private
		int
		getInternalInd(Node n) {
			
			for(int i=0; i<nodesList.size(); i++) {
				if(nodesList.get(i) == n) return i;
			}
			
			
			// if reach this line
			// error / unexpected behavior
			return 0;
		}
		
		
		
		int
		getInternalInd(int id) {
			for(int i=0; i<nodesList.size(); i++) {
				if(nodesList.get(i).getId() == id) return i;
			}
			
			
			// if reached this line
			// error, unexpected behavior
			return -1;
		}
		
		
		
		
		
		
		/**
		 * 
		 * 
		 * 
		 * 
		 * @return
		 */
		boolean
		hasUnvisitedEdges() {
			for(Node node : nodesList) {
				if(node.hasUnvisitedEdges()) {
					return true;
				}
			}
			
			return false;
		}
		
		

		
		/**
		 * Starts searching at intersection with parent cycle
		 * Then searches in order directed cycle was created
		 * 
		 * 
		 * 
		 * 
		 * @return null if no unvisited edges
		 */
		Node getDBNodeWUnvisitedEdges() {
			for(Node node : nodesList) {
				if(node.hasUnvisitedEdges()) {
					return node;
				}
			}
			
			return null;
		}
		
		
		
		
		
		/**
		 * Not actually keeping list of edges
		 * Just mark edge as visited
		 * 
		 * 
		 * 
		 */
		void
		appendEdge(Edge edge) {
			edge.visited = true;
		}
		
		
		
		
		
		public Cycle clone() {
			Cycle theClone = new Cycle();
			
			// copy nodesList
			
			for (Node node : this.nodesList) {
				theClone.appendNode(node);
			}
		
			return theClone;
		}
	

		
		
		void appendNode(Node node) {
			nodesList.add(node);

		}
		
		
		
		String
		getPrint() {
			String str = String.valueOf(nodesList.get(0).id);
			
			for(int i=1; i<nodesList.size(); i++) {
				str += "->" + nodesList.get(i).id;
			}
			
			return str;
		}
		
		
	}







	
	
	
	
}








