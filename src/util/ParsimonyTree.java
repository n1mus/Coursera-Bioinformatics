package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;





/*
 * rooted binary tree re-purposed from DirectedTree
 * also supports being unrooted, conversion to undirected tree
 * 
 * 
 */

public class ParsimonyTree {
	
	boolean debug = false;
	
	int height;
	public DynamicNodeArray nodesArray = new DynamicNodeArray();
	private Node root;
	static char[] ALPHABET  = {'A','C','G','T'};
	static int ALPHABET_SIZE = 4;
	
	public static int STRING_LEN;
	
	int numLeavesEncountered = 0;
	int NUM_LEAVES;
	
	
	
	public ParsimonyTree (int numLeaves) {
		
		NUM_LEAVES = numLeaves;

	}
	
	public ParsimonyTree( int numLeaves, int stringLen) {
		NUM_LEAVES = numLeaves;
		STRING_LEN = stringLen;
	}
	
	
	
	
	UndirectedTree createUndirectedTree() {
		
		
		UndirectedTree uTree = new UndirectedTree(this.STRING_LEN);

		for(Node node : nodesArray.getAllNodes()) {

			// get data fields

			String dnaSequence = node.isLeaf() ? node.sequence : null;
			int id = node.getId();

			// add/create the node in the undirected tree

			uTree.addNode(id, dnaSequence);

			// add/create the edges of the node

			uTree.addEdge(id,node.parent.getId());

			for(Edge edge : node.outgoingEdges) {

				uTree.addEdge(id,edge.getNext(node).getId());
			}

		}	
		
		
		
		return uTree;
	}
	
	
	
	
	
	
	
	
	

	/**
	 * Performs small parsimony and returns the total score of the tree
	 * 
	 * 
	 * 
	 * 
	 */
	public int smallParsimony() {
		this.root = nodesArray.findRoot();
		
		ArrayList<Node> postOrderNodes = new ArrayList<Node>();
		this.root.postOrderTraversalRec(postOrderNodes);
		
		
		
		
		for(Node node : postOrderNodes) {
			node.smallParsimonySequence();
		}
		

		ArrayList<Node> preOrderNodes = new ArrayList<Node>();
		this.root.preOrderTraversalRec(preOrderNodes);

		
		
		for(Node node : preOrderNodes) {
			node.backTraceString();
		}
		
		for(Node node : preOrderNodes) {
			node.setSequence();
			node.calculateEdgeLengths();
		}

		return this.root.height;
	}


	
	
	public void parseAddNodesEdge(String edgeStr) {
			
			String[] firstSplit = edgeStr.split("->");
			int parentId = Integer.parseInt(firstSplit[0]);
			
			
			if(STRING_LEN == 0) {
				STRING_LEN = firstSplit[1].length();
			}
			
			
			Node parent = nodesArray.exists(parentId) ? nodesArray.get(parentId) : new Node(parentId, null);
			Node child;

			if(numLeavesEncountered < NUM_LEAVES) {
				
				child =  new Node(numLeavesEncountered++, firstSplit[1]);
			} else {
				int childId = Integer.parseInt(firstSplit[1]);
				child = nodesArray.exists(childId) ? nodesArray.get(childId) : new Node(childId, null);
			}
			
			
			
			
			
			
			Edge edge = new Edge(parent, child, 0);
			
			parent.addOutgoingEdge(edge);
			child.setParent(parent);
			
			nodesArray.add(parent);
			nodesArray.add(child);
		}
	
	
	
	
	
	public int getRootId() {
		return root.getId();
	}
	
	

	public void unroot() {
		
		this.height = root.height;
		
		Node left = root.getLeftChild();
		Node right = root.getRightChild();
		
		int length = 0;
		
		for(Edge e : root.outgoingEdges) {
			length += e.getWeight();
		}
		
		left.parent = null;
		right.parent = null;
		
		nodesArray.underlyingArray[root.getId()] = null;
		root = null;
		
		Edge e = new Edge(left, right, length);
		
		left.addOutgoingEdge(e);
		right.addOutgoingEdge(e);
		
		
		
	}
	
	
	
	

	public void setRoot(Node root) {
		this.root = root;
	}
	
	
	private Node getRoot() {
		return this.root;
	}
	
	

	void setStringLength() {
		STRING_LEN = nodesArray.get(0).sequence.length();
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
		
		System.out.println(this.root.height);
		
		
		Node[] allNodes = this.getAllNodesSorted();
	
		
		for(Node node : allNodes) {
			node.print();
		}
		
		
		
	}
	
	
	
	/**
	 * Call after done with this tree, because must unroot before getting the print
	 * (root is added when converted from undirected tree)
	 */
	public ArrayList<String> getPrint() {


		this.unroot();


		ArrayList<String> res = new ArrayList<String>();
		
		res.add("" + this.height + "\n");
		
		
	
		
		for(Node node : nodesArray.underlyingArray) {
			if(node != null)
				res.addAll(node.getPrint());
		}
		
		return res;
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
	
	public  class Node {
		
		
		// structure
		
		private int id;
		private Node parent;
		private ArrayList<Edge> outgoingEdges = new ArrayList<Edge>();
		
		
		// the DNA sequence at a node
		// initialized for leaves
		// calculate for internal nodes
		String sequence;
		char[] sequenceLetters;

		
		

		int[][] ss; // ss[STRING_LEN][ALPHABET_SIZE];
		int[][][] backtracks; // backtracks[STRING_LEN][[ALPHABET_SIZE][2] holds the two characters corresponding to the value at s[i]
		
		
		int[] sequenceLettersInd;
		
		int height;
		
		
		
		/**
		 * SmallParsimonyTree.STRING_LEN must be initialized
		 * 
		 * 
		 * @param id
		 * @param sequence
		 */
		public Node(int id, String sequence ) {
			
			this.id = id;
			this.sequence = sequence;
			
			
			if(sequence == null) {
				sequenceLetters = new char[STRING_LEN];
			} else {
				sequenceLetters = sequence.toCharArray();
			}
			
			
			ss = new int[STRING_LEN][ALPHABET_SIZE];
			backtracks = new int[STRING_LEN][ALPHABET_SIZE][2];
			
			sequenceLettersInd = new int[STRING_LEN];
			
			nodesArray.add(this);
		}
		
		
		
		/**
		 * Calculates outgoing edge lengths
		 */
		void calculateEdgeLengths() {
			
			
			for(Edge e : outgoingEdges) {
				int length = this.diff(e.getChild());
				e.setWeight(length);
			}
		}
		
		
		int diff(Node node) {
			int diffs = 0;
			char[] otherSequenceLetters = node.sequenceLetters;
			
			for(int i=0; i<sequenceLetters.length; i++) {
				if(sequenceLetters[i] != otherSequenceLetters[i]) {
					diffs++;
				}
			}
			
			return diffs;
		}
		
		
		
		void setSequence() {
			if (this.sequence == null)
			{
				this.sequence = String.copyValueOf(sequenceLetters);
			}
		}
		
		
		/**
		 * 
		 */
		void smallParsimonySequence() {
			
	
			
			for(int index=0; index<STRING_LEN; index++) {
				
				
				
				
				int[] s = ss[index];
				
				if(this.isLeaf())
					smallParsimonySequenceLetter(s,null,null,null,index);
				else {
					int[][] backtrack = backtracks[index];
					
					int[] sLeft = this.getLeftChild().ss[index];
					int[] sRight = this.getRightChild().ss[index];
					
				
					smallParsimonySequenceLetter(s,backtrack,sLeft,sRight,index);
				}
				
			}
			
			if(debug) IOUtilities.printArray("ss " + this.id, ss);

		}
		
		
		
		
		
		/**
		 * For a letter in the sequence, 
		 * Calculate s[] and backtrack[][]
		 * 
		 */
		void smallParsimonySequenceLetter(int[] $s, int[][] $backtrack, int[] sLeft, int[] sRight,      int seqIndex) {
			
			
			
			if(this.isLeaf()) {
				
				char nodeLetter = sequenceLetters[seqIndex];
				
				for(int i=0; i<ALPHABET_SIZE; i++) {
					
					$s[i] =    ALPHABET[i] == nodeLetter ? 0 : Integer.MAX_VALUE/2;
					
					
				}
				return;
			} 

			
			
			// iterate through letters of alphabet and calculate s[] and backtrack[][]
			
			for(int h=0; h<ALPHABET_SIZE; h++) {
				
				
				int leftMin = Integer.MAX_VALUE;
				
				for(int i=0; i<ALPHABET_SIZE; i++) {
					
					int delta_ih = i == h ? 0 : 1;
					int sValue = sLeft[i] + delta_ih;
						
						if( sValue < leftMin ) {
							leftMin = sValue;
							$backtrack[h][0] = i;
						}
						
					}
				
			
			
				int rightMin = Integer.MAX_VALUE;
					
				for(int j=0; j<ALPHABET_SIZE; j++) {
					
					int delta_jh = j == h ? 0 : 1;
					int sValue = sRight[j] + delta_jh;
						
						if( sValue < rightMin ) {
							rightMin = sValue;
							$backtrack[h][1] = j;
						}
						
				}
				
				
				
				$s[h] = leftMin + rightMin;
				
			}
				
			
			
			
		}
		
		
		
		
		
		/**
		 * Called on all nodes in pre-order
		 * 
		 * Calculate the DNA sequence for internal nodes
		 * 
		 * sequenceLetters[] and sequenceLettersInd[] should be filled
		 * 
		 * 
		 */
		void backTraceString() {
			
			// case 1 : leaf
			
			if(this.isLeaf())
				return;
			
			
			// case 2 : root
			
			if(parent == null) {
				
				for(int index=0; index<STRING_LEN; index++) {
					
					int alphabetIndex = util.Utilities.argmin(ss[index]);
					height += ss[index][alphabetIndex];
					this.sequenceLetters[index] = ALPHABET[alphabetIndex];
					this.sequenceLettersInd[index] = alphabetIndex;
				}
				
				return;
			} 
			
			
			
			// case 3 : other internal node
			
			int backtrackIndex = -1;
			
			if(this.isLeftChild()) {
				backtrackIndex = 0;
			} else {
				backtrackIndex  = 1;
			}
			
			
			for(int index=0; index<STRING_LEN; index++) {
				int parentsChosenLetter = parent.sequenceLettersInd[index];
				int chosenLetter = parent.backtracks[index][parentsChosenLetter][backtrackIndex];
				
				this.sequenceLettersInd[index] = chosenLetter;
				this.sequenceLetters[index] = ALPHABET[chosenLetter];
			}
			
			
		}
		
		

		

		
		
		void preOrderTraversalRec(ArrayList<Node> $nodes) {
			
			// BC
			
			if (this.isLeaf()) {
				$nodes.add(this);
				return;
			}
			
			// RC
			
			ArrayList<Node> children = this.getChildren();
			Node left = children.get(0);
			Node right = children.get(1);

			$nodes.add(this);
			
			left.preOrderTraversalRec($nodes);
			right.preOrderTraversalRec($nodes);
			

		}
		
		
		void postOrderTraversalRec(ArrayList<Node> $nodes) {
			
			// BC
			
			if (this.isLeaf()) {
				$nodes.add(this);
				return;
			}
			
			// RC
			
			ArrayList<Node> children = this.getChildren();
			Node left = children.get(0);
			Node right = children.get(1);

			left.postOrderTraversalRec($nodes);
			right.postOrderTraversalRec($nodes);
			
			$nodes.add(this);
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
		
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		
		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? false : ((Node)o).id == this.id;
		}
		


		
		boolean isLeaf() {
			return outgoingEdges.size() == 0;
		}
		
		boolean isLeftChild() {
			if(parent == null) return false; // root
			
			ArrayList<Node> siblings = parent.getChildren();
			
			if( this == siblings.get(0)) return true;
			else return false;
		}
		
		public void addChild(Node child) {
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
		
		Node getLeftChild() {
			if(this.isLeaf()) return null;
			return outgoingEdges.get(0).getChild();
		}
		
		Node getRightChild() {
			return outgoingEdges.get(1).getChild();
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
			
			for(Edge e : sortedPseudoOutgoingEdges) {
				e.print();
			}
			
		}
		
		ArrayList<String>  getPrint() {
			ArrayList<String> res = new ArrayList<String>();
			ArrayList<Edge> sortedPseudoOutgoingEdges = this.getOrderedEdges();
			for(Edge e : sortedPseudoOutgoingEdges) {
				res.add(e.getPrint());
			}
			return res;
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
	private class Edge {
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
		
		public Node getNext(Node prev) {
			if(prev.equals(child)) return parent;
			if(prev.equals(parent)) return child;
			return null;
		}

		public void print() {
			String formattedWeight = String.format("%d", (int)weight);
			String str = "" + parent.sequence + "->" + child.sequence + ":" + formattedWeight;
			System.out.println(str);
		}
		
		String getPrint() {
			if(debug) return "" + parent.id + parent.sequence + "->" + child.id + child.sequence + ":" + (int) weight + "\n";
			
			return "" + parent.sequence + "->" + child.sequence + ":" + (int) weight + "\n";
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
	
	
	
	
	

	
	
	/**
	 * // not robust. space efficient if node ids start low and are consecutive
	* to be robust in space, use hash map
	* also does not handle negative id's
	 * 
	 *
	 */
	private class DynamicNodeArray {
		private Node[] underlyingArray; 
		private int capacity = 100;
		private int resizeFactor = 3;
		private int numNodes;
		
		public DynamicNodeArray() {
			underlyingArray = new Node[capacity];
		}
		
		/**
		 * Won't add if already exists
		 * All additions to underlyingArray should happen here to update numNodes
		 * 
		 * @param node
		 * @return
		 */
		boolean add(Node node) {
			int id = node.getId();
			
			if(id >= capacity) 
				resize(id);
			
			if (underlyingArray[id] == null){
				underlyingArray[id] = node;
				numNodes++;
				return true;
			} else
				return false;
		}
		
		public boolean exists(int index) {
			if(index >= capacity || underlyingArray[index] == null) return false;
			else return true;
		}
		
		public Node get(int index) {
			return underlyingArray[index];
		}
		
		ArrayList<Node> getAllNodes() {
			ArrayList<Node> allNodes = new ArrayList<Node>();
			for(int i=0; i<capacity; i++)
			{
				if( underlyingArray[i] != null) {
					allNodes.add(underlyingArray[i]);
				}
			}
			return allNodes;
		}
		
		
		
		public int getNumNodes() {
			return numNodes;
		}
		
		private void resize(int outOfBoundsIndex) {
			int newCapacity = outOfBoundsIndex * resizeFactor;
			Node[] newUnderlyingArray = new Node[newCapacity];
			
			// copy contents into new underlying array
			for(int i=0; i<capacity; i++) {
				newUnderlyingArray[i] = underlyingArray[i];
			}
			
			capacity = newCapacity;
			underlyingArray = newUnderlyingArray;
		}
		
		Node findRoot() {
			
			if(root != null) return root;
			
			for(int i=0; i<capacity; i++)
			{
				if( underlyingArray[i].parent == null) {
					return underlyingArray[i];
				}
			}
			
			return null;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}








