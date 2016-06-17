package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;


/*
 * Re-purposed from DAG
 * 
 * 
 * 
 */

public class DirectedTree {
	
	private Node root;
	

	
	
	public DirectedTree (int rootId, int age) {
		
		root = new Node(rootId, age);

	}
	
	
	public DirectedTree(Node root) {
		this.root = root;
	}


	
	
	
	
	
	
	public int getRootId() {
		return root.getId();
	}
	
	

	
	
	
	
	

	
	
	
	private Node getRoot() {
		return this.root;
	}
	
	
	/**
	 * UPGMA ?
	 * 
	 * @param left
	 * @param right
	 * @param distanceMatrix
	 * @return
	 */
	
	public static DirectedTree join(DirectedTree left, DirectedTree right, int[][] distanceMatrix, int newRootId) {
		
		double height = calcJoinHeight(left, right, distanceMatrix);
		
		Node newRoot = new Node(newRootId, height);
		
		newRoot.addChild(left.getRoot());
		newRoot.addChild(right.getRoot());

		left.getRoot().setParent(newRoot);
		right.getRoot().setParent(newRoot);
		
		
		
		return new DirectedTree(newRoot);
	}
	
	/**
	 * 
	 * 
	 * @param left
	 * @param right
	 * @param distanceMatrix
	 * @return
	 */
	private static double calcJoinHeight(DirectedTree left, DirectedTree right, int[][] distanceMatrix) {
		
		ArrayList<Node> leftLeaves = left.getLeaves();
		ArrayList<Node> rightLeaves = right.getLeaves();
		
		double totalDist = 0;
		
		for(Node leftLeaf : leftLeaves) {
			for(Node rightLeaf : rightLeaves ) {
				int leftId = leftLeaf.getId();
				int rightId = rightLeaf.getId();
				
				totalDist += distanceMatrix[leftId][rightId];
			}
		}
		
		
		
		return totalDist / (leftLeaves.size() * rightLeaves.size()) / 2;
	}
	
	
	/**
	 * When you have heights of all nodes, but not the edge lengths
	 */
	public void calculateEdgeLengths() {
		
		calculateEdgeLengthsRec(this.root);
		
	}
	
	
	private void calculateEdgeLengthsRec(Node currNode) {
		
		
		// BC reached leaf
		
		if(currNode.isLeaf())
			return;
		
		
		// RC calculate lengths of edges to children
		
		double currHeight = currNode.getHeight();
		
		for(Edge childEdge : currNode.getOutgoingEdges()) {
			
			Node child = childEdge.getChild();
			double length = currHeight - child.getHeight();
			
			childEdge.setWeight(length);
			
			calculateEdgeLengthsRec(child);
		}
		
		
	}
	
	
	
	
	private int numLeaves() {
		return root.numLeavesRec();
	}
	
	
	
	private ArrayList<Node> getLeaves() {
		
		return root.getLeavesRec();
	}
	
	public int[] getLeafIds() {
		
		
		int[] leafIds = new int[this.numLeaves()];
		ArrayList<Node> leaves = this.getLeaves();
		
		for(int i=0; i<leaves.size(); i++) {
			leafIds[i] = leaves.get(i).getId();
		}
		
		return leafIds;
	}
	
	
	private ArrayList<Node> getAllNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		return this.root.getAllDescendantsRec();
		
		
	}
	
	
	
	
	/**
	 * ordered by the nodes' id's
	 * 
	 * @return
	 */
	private Node[] getAllNodesSorted() {
		
		
		ArrayList<Node> allNodes = this.getAllNodes();

		Node[] nodesArray = new Node[allNodes.size()];
		
		
		for(Node node : allNodes) {
			
			int id = node.getId();
			nodesArray[id] = node;
			
		}
		
		return nodesArray;
	}
	
	
	
	/**
	 * 
	 */
	public void print() {
		

		Node[] allNodes = this.getAllNodesSorted();
	
		
		for(Node node : allNodes) {
			node.print();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * Keeps reference to parent,
	 * reference to edges to children
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	private static class Node {
		private int id;
		private double height = -1;
		private Node parent;
		private ArrayList<Edge> outgoingEdges = new ArrayList<Edge>();
		
		
		public Node(int id) {
			
			this.id = id;
		}
		
		public Node(int id, double height) {
			this.id = id;
			this.height = height;
		}
		


		
		public void addOutgoingEdge(Edge edge) {
			outgoingEdges.add(edge);
		}
		
		ArrayList<Edge> getOutgoingEdges() {
			return this.outgoingEdges;
		}
		
		/**
		 * Edges in order of ascending child node id's
		 */
		 ArrayList<Edge> getOrderedOutgoingEdges() 
		 {
			ArrayList<Edge> outgoingEdgesCopy = new ArrayList<Edge>();
			outgoingEdgesCopy.addAll(this.outgoingEdges);
			
			Collections.sort(outgoingEdgesCopy, new EdgeComparator());
			return outgoingEdgesCopy;
		}
		 
		 
		/**
		 * Edges in order of ascending neighboring node id's
		 * 
		 */
		 ArrayList<Edge> getOrderedEdges() 
		 {
			 
			
			 
			ArrayList<Edge> pseudoOutgoingEdges = new ArrayList<Edge>();
			pseudoOutgoingEdges.addAll(this.outgoingEdges);
			
			if(parent != null) {
				Edge pseudoOutgoingEdge = new Edge(this, parent, parent.getOutgoingEdgeWeight(this)); 
				pseudoOutgoingEdges.add(pseudoOutgoingEdge);
			}
			
			Collections.sort(pseudoOutgoingEdges, new EdgeComparator());
			return pseudoOutgoingEdges;
		}

		double getOutgoingEdgeWeight(Node child) {
			for( Edge e : outgoingEdges) {
				if(e.getChild().getId() == child.getId())
					return e.getWeight();
			}
			
			new Exception().printStackTrace(System.out);
			return -1;
		}
		 
		 
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public Node getParent() {
			
			return this.parent;
		}
		
		void setParent(Node parent) {
			this.parent = parent;
		}
		
		
		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? false : ((Node)o).id == this.id;
		}
		

		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		
		boolean isLeaf() {
			return outgoingEdges.size() == 0;
		}
		
		void addChild(Node child) {
			Edge childEdge = new Edge(this, child, 0);
			outgoingEdges.add(childEdge);
		}
		
		ArrayList<Node> getChildren() {
			ArrayList<Node> children = new ArrayList<Node> ();
			for(Edge outgoingEdge : outgoingEdges) {
				Node child = outgoingEdge.getChild();
				children.add(child);
			}
			
			return children;
		}
		
		/**
		 * 
		 * @return number of leaves descended from this node, or 1 if this is a leaf
		 */
		int numLeavesRec() {
			if(this.isLeaf()) return 1;
			
			int numLeaves = 0;
			
			for(Edge outgoingEdge : outgoingEdges) {
				Node child = outgoingEdge.getChild();
				numLeaves += child.numLeavesRec();
			}
			
			return numLeaves;
		}
		
		/**
		 * 
		 * @return all leaves descended from this node, or itself if it is leaf
		 */
		ArrayList<Node> getLeavesRec() {
			
			ArrayList<Node> leaves = new ArrayList<Node>();
			
			if(this.isLeaf()) {
			
				leaves.add(this);

			} else {

				for(Node child : this.getChildren()) {
					leaves.addAll(child.getLeavesRec());
				}
			}
			
			return leaves;
		}
		
		
		
		/**
		 * Descendants defined as yourself and all nodes descended from you
		 * 
		 * 
		 * @return
		 */
		ArrayList<Node> getAllDescendantsRec() {
			
			ArrayList<Node> descendants = new ArrayList<Node>();
			
			// add self as descendant
			
			descendants.add(this);
			
			
			// if not leaf, then get children's descendants
			
			if(!this.isLeaf()) {
				for(Node child : this.getChildren()) {
					descendants.addAll(child.getAllDescendantsRec());
				}
					
			}
			
			return descendants;
			
		}
		
		/**
		 * 
		 */
		public void print() {

			ArrayList<Edge> sortedPseudoOutgoingEdges = this.getOrderedEdges();
			ArrayList<Edge> sortedOutgoingEdges = this.getOrderedOutgoingEdges();
			
			for(Edge e : sortedOutgoingEdges) {
				e.print();
			}
			
		}
		
	}

	


	
	
	
	
	
	/*
	 * 
	 * 
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
	private static class Edge {
		private Node parent;
		private Node child;
		private double weight;
		
		
		Edge() {
			
		}
		
		public Edge(Node parent, Node child, double weight) {
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

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}
		
		public void print() {
			String formattedWeight = String.format("%.3f",weight);
			String str = "" + parent.getId() + "->" + child.getId() + ":" + formattedWeight;
			System.out.println(str);
		}
	}

	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 */
	private static class NodeComparator implements Comparator<Node> {
		
		@Override
		public int compare(Node first, Node second) {
			int firstId = first.getId();
			int secondId = second.getId();
			
			return Integer.compare(firstId, secondId);
		}
		
		/**
		 * Not implemented
		 */
		@Override
		public boolean equals(Object o) {
			return false;
		}
	}
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 */
	private static class EdgeComparator implements Comparator<Edge> {
		
		@Override
		public int compare(Edge first, Edge second) {
			int firstParentId = first.getParent().getId();
			int secondParentId = second.getParent().getId();
			
			
			
			// different parents, compare by parent id's
			
			if( firstParentId != secondParentId ) {
				return Integer.compare(firstParentId, secondParentId);
			}
			
			
			
			// else same parents, compare by child id's
				
			int firstChildId = first.getChild().getId();
			int secondChildId = second.getChild().getId();
				
			return Integer.compare(firstChildId, secondChildId);
			
			
		}
		
		/**
		 * Not implemented
		 */
		@Override
		public boolean equals(Object o) {
			return false;
		}
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}








