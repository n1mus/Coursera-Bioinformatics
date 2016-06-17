package util;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;















/**
 * 
 * 
 * Re-purposed from DAG
 * 
 * 
 * 
 * 
 * 
 *
 */
public class DirectedGraph<T> {

	
	private DynamicNodeArray nodesArray = new DynamicNodeArray(); // keeps references to all added nodes
	
	

	
	
	
	
	
	void addNode(int id, T payload) {
		Node<T> node = new Node<T>(id, payload);
		nodesArray.add(id, node);
	}
	

	
	
	
	
	/**
	 * Node id is index in ArrayList<T>
	 * T payload is corresponding element in the ArrayList<T>
	 * 
	 * 
	 * @param payloads
	 */
	public void addNodes(ArrayList<T> payloads) {
		
		for(int i=0; i<payloads.size(); i++) {
			Node<T> node = new Node<T>(i, payloads.get(i));
			nodesArray.add(i, node);
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
	 * Find traversal that visits each node once
	 * Start at a source node -- node with no incoming edges
	 * 
	 * 
	 * 
	 */
	public int[] findTraversal() {
		
		// find source node
		
		Node sourceNode = null;
		
		for(Node node : nodesArray.getNodesList()) {
			if( node.isSource() ) {
				sourceNode = node;
			}
		}
		
		
		if( sourceNode == null ) {
			System.err.println("no source node in graph");
		}
		
		
		// DFS
		
		int totalNumNodes = nodesArray.getSize();
		
		Path path = visitDFS(sourceNode, new Path(totalNumNodes));
		
		return path.getNodeIds();
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param node
	 * @param currPath
	 * @return
	 */
	Path visitDFS( Node node, Path currPath ) {
		
		
		/*
		 * BC
		 * 
		 * already visited this node
		 */
		
		
		if( currPath.getVisited(node)) {
			return null;
		}
		
		
		
		currPath.appendNode(node);
		
		
		/*
		 * BC 
		 * 
		 * traversal complete
		 */
		
		
		if (currPath.isComplete()) {
			return currPath;
		}
		
		
		
		
		/*
		 * RC
		 * 
		 */
		
		
		
		
		ArrayList<Node> children = node.getChildren();
		
		for(Node child : children) {
			
			Path currPathCopy = currPath.clone();
			
			Path subsequentPath = visitDFS(child, currPathCopy);
			
			
			// found the complete traversal!
			if( subsequentPath != null) {
				return subsequentPath;
			}

		}
		

		return null; // no valid traversals from this node
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Print edge-strings
	 * 
	 * 
	 * 
	 * 
	 * @param id
	 * @param payload
	 * @param weight
	 */
	public void print(boolean id, boolean payload, boolean weight) {
		for(int i=0; i<nodesArray.size(); i++) {
			Node n = nodesArray.get(i);
			if(n != null )
				n.print(id, payload, weight);
		}
	}
	
	
	
	/**
	 * Aggregate edge-strings
	 * 
	 * 
	 * 
	 * 
	 * @param id
	 * @param payload
	 * @param weight
	 * @return
	 */
	public ArrayList<String> getPrint(boolean id, boolean payload, boolean weight) {
		
		ArrayList<String> edgeStrList = new ArrayList<String> ();
		
		for(int i=0; i<nodesArray.size(); i++) {
			
			Node n = nodesArray.get(i);
			
			if(n != null ) {
				ArrayList<String> smallEdgeStrList = n.getPrint(id, payload, weight);
				edgeStrList.addAll(smallEdgeStrList);
			}
			
		}
		
		return edgeStrList;
	}
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *
	 */
	private class Node<T> {
		
		
		private int id;
		private T payload;
		private ArrayList<Edge> incomingEdges;
		private ArrayList<Edge> outgoingEdges;
		
		
		Node(int id, T payload) {
			this(id);
			this.payload = payload;
		}
		
		
		Node(int id) {
			this.setId(id);
			incomingEdges = new ArrayList<Edge>();
			outgoingEdges = new ArrayList<Edge>();
		}
		
		
		void addIncomingEdge(Edge edge) {
			incomingEdges.add(edge);
		}

		
		void addOutgoingEdge(Edge edge) {
			outgoingEdges.add(edge);
		}
		
		
		ArrayList<Edge> getIncomingEdges() {
			return incomingEdges;
		}
		
		
		ArrayList<Edge> getNonDeadEndIncomingEdges(boolean[] deadEnd) {
			ArrayList<Edge> nonDeadEndIncomingEdges = new ArrayList<Edge>();
			
			for(Edge e : incomingEdges) {
				int parentId = e.getParent().getId();
				if(! deadEnd[parentId]) {
					nonDeadEndIncomingEdges.add(e);
				}
			}
			
			return nonDeadEndIncomingEdges;
		}

		
		T getPayload() {
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
		
		
		
		/**
		 * Print edge-strings
		 * 
		 * 
		 * 
		 * @param id
		 * @param payload
		 * @param weight
		 */
		void print(boolean id, boolean payload, boolean weight) {
			for(Edge e : outgoingEdges) {
				e.print(id, payload, weight);
			}
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
	private class Edge {
		
		
		private Node parent;
		private Node child;
		private int weight;
		
		
		/**
		 * Nodes already added
		 * Reference to this edge is given to the nodes
		 * 
		 * 
		 * @param parentId
		 * @param childId
		 */
		Edge(int parentId, int childId) {
			Node<T> parentNode = nodesArray.get(parentId);
			Node<T> childNode = nodesArray.get(childId);
			
			this.setParent(parentNode);
			this.setChild(childNode);
			
			parentNode.addOutgoingEdge(this);
			childNode.addIncomingEdge(this);
		}
		
		
		Edge(Node parent, Node child, int weight) {
			this.setParent(parent);
			this.setChild(child);
			this.setWeight(weight);
			
			parent.addOutgoingEdge(this);
			child.addIncomingEdge(this);
		}

		
		Node getParent() {
			return parent;
		}
		

		void setParent(Node parent) {
			this.parent = parent;
		}

		
		Node getChild() {
			return child;
		}

		
		void setChild(Node child) {
			this.child = child;
		}

		
		int getWeight() {
			return weight;
		}

		
		void setWeight(int weight) {
			this.weight = weight;
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
	 * Nodes have ids (preferrably 0, 1, 2, ... )
	 * 
	 * Store node references in array, where its index == its id
	 * 
	 *
	 */
	private class DynamicNodeArray {
		
		
		private Node[] underlyingArray;
		private int capacity = 100;
		private int resizeFactor = 3;
		private int size = 0;
		private TreeSet<Integer> nodeIds;
		
		
		DynamicNodeArray() {
			underlyingArray = new Node[capacity];
			nodeIds = new TreeSet<Integer>();
		}
		
		
		void add(int index, Node e) {
			
			size++;
			nodeIds.add(new Integer(index));
			
			if(index >= capacity) 
			
				resize(index);
			
			underlyingArray[index] = e;
		}
		 
		
		boolean exists(int index) {
			return underlyingArray[index] != null;
			
		}
		
		
		Node get(int index) {
			return underlyingArray[index];
		}
		
		
		int getSize() {
			return size;
		}
		
		
		TreeSet<Integer> getNodeIds() {
			return nodeIds;
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
	 * Path between two nodes
	 * Nodes ids must be 0, 1, 2 ...
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private class Path {

		
		private ArrayList<Node> nodesList = new ArrayList<Node>(); // list of nodes representing steps in path,
																	// starting at the source node
		private int totalNumNodes; // num nodes in graph
		private boolean[] visited; // whether a node in graph has been visited already
		
		
		public Path(int totalNumNodes) {
			this.totalNumNodes = totalNumNodes;
			visited = new boolean[totalNumNodes];
		}
		
		
		public boolean getVisited(Node node) {
			int id = node.getId();
			return visited[id];
		}
		
		
		public Path clone() {
			Path theClone = new Path(totalNumNodes);
			
			// copy nodesList and visited[]
			
			for (Node node : this.nodesList) {
				theClone.appendNode(node);
			}
		
			return theClone;
		}

		
		boolean isComplete() {
			return nodesList.size() == totalNumNodes;
		}
		
		
		void appendNode(Node node) {
			nodesList.add(node);
			
			int id = node.getId();
			visited[id] = true;
		}
		
		
		/**
		 * 
		 * @return
		 */
		int[] getNodeIds() {
			int[] nodeIds = new int[totalNumNodes];
			
			for(int i=0; i<totalNumNodes; i++) {
				nodeIds[i] = nodesList.get(i).getId();
			}
			
			return nodeIds;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}








