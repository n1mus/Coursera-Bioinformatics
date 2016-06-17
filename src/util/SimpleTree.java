package util;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Simple tree
 * 
 *
 * Keeps array of nodes (are automatically added upon instantiation)
 * Nodes keep their edges
 * 
 * 
 *
 * 
 *
 */
public class SimpleTree {

	private DynamicNodeArray nodesArray = new DynamicNodeArray(); // keeps references to all added nodes
	private int NUM_LEAVES;
	private int numInnerNodes = 0;
	
	
	/**
	 * Instantiate simple tree with two nodes (with id's 0 and 1)connected by limb with length
	 * Also initialize the number of leaves
	 * 
	 * @param length
	 */
	public SimpleTree(double length, int numLeaves) {
		Node leaf0 = new Node(0);
		Node leaf1 = new Node(1);
		
		Limb limb = new Limb(leaf0, leaf1, length);
		
		leaf0.addLimb(limb);
		leaf1.addLimb(limb);
		
		this.NUM_LEAVES = numLeaves;
	}
	
	
	/**
	 * Instantiate simple tree with two nodes connected by limb with length
	 * 
	 * @param length
	 */
	public SimpleTree(int id0, int id1, double length) {
		Node leaf0 = new Node(id0);
		Node leaf1 = new Node(id1);
		
		Limb limb = new Limb(leaf0, leaf1, length);
		
		leaf0.addLimb(limb);
		leaf1.addLimb(limb);
		
	}
	
	
	/**
	 * 
	 * @param leafId
	 * @param parentId
	 * @param limbLength
	 */
	public void attachLeafNode(int leafId, int parentId, double limbLength) {
		Node newNode = new Node(leafId);
		Node parent = nodesArray.get(parentId);
		
		Limb limb = new Limb(parent, newNode, limbLength);
		
		parent.addLimb(limb);
		newNode.addLimb(limb);

	}


	
	/**
	 * Insert inner node on path between leaves i and k, a distance x after leaf i
	 * 
	 * 
	 * @param i id of leaf node representing the origin of the path
	 * @param k id of leaf node representing the terminal of the path
	 * @param x length past origin node to insert v
	 * 
	 * @return id of the node that was inserted, or id of the existing node if no node was inserted
	 */
	public int insertNode(int i, int k, int x) {
		
		Path path = getPath(i,k);
		
		
		
		Node originLeaf = path.getOriginLeaf();
		Node terminalLeaf = path.getTerminalLeaf();
		
		
		
		// initialize node cursors
		
		Node firstNode = originLeaf;
		Node secondNode = path.nextNode(originLeaf);
		
		
//		System.out.println("first node " + firstNode);
//		System.out.println("second node " + secondNode);
		
		// initialize accumulative variable
		int lengthAccumulated = 0; 
		double limbLength = -1;
				
		
		// iterate while still on path
		
		while( !firstNode.equals(terminalLeaf) ) {
			
			
			// update accumulative variable
			
			limbLength = path.nextLimbLength(firstNode);
			lengthAccumulated += limbLength;
			
//			System.out.println("limb length " + limbLength);
//			System.out.println("length accumulated " + lengthAccumulated);
			
			// stop iterating node cursors once you've iterated through an adequate length
			
			if( lengthAccumulated >= x) break; 
			
			
			// increment node cursors
			
			firstNode = path.nextNode(firstNode);
			secondNode = path.nextNode(secondNode);

			
//			System.out.println("first node " + firstNode);
//			System.out.println("second node " + secondNode);

			
		} 
		
		
		// insert new node node in midst of existing limb
		
		if( lengthAccumulated > x ) { 
			
			// get id of next inner node to be inserted
			
			int newNodeId = this.NUM_LEAVES + this.numInnerNodes++;
			
			// instantiate new inner node
			
			Node v = new Node(newNodeId);
			
			// determine 2 new limb lengths from breaking old limb
			
			double secondBreakLength = lengthAccumulated - x;
			double firstBreakLength = limbLength - secondBreakLength;
			
			
			// remove old limb
			
			Limb oldLimb = firstNode.getLimb(secondNode);
			firstNode.removeLimb(oldLimb);
			secondNode.removeLimb(oldLimb);
			
			
			// attach  new inner node v  with new limbs
			
			Limb firstLimb = new Limb(firstNode, v, firstBreakLength);
			Limb secondLimb = new Limb(v, secondNode, secondBreakLength);
			
			
			firstNode.addLimb(firstLimb);
			v.addLimb(firstLimb);
			
			v.addLimb(secondLimb);
			secondNode.addLimb(secondLimb);
			
			
			
			
			return v.getId();
			
		} 
		
		// node to be inserted at existing node -- do nothing
		
		else if( lengthAccumulated == x) {
			
			
			return secondNode.getId();
		}
		
		
		
		// error
		
		
//		path.print();
//		System.out.println("i " + i);
//		System.out.println("k " + k);
//		System.out.println("x " + x);
//		System.out.println("length accumulated " + lengthAccumulated);

		
		
		
		new Exception().printStackTrace(System.out);
		return -1;
	}
	
	
	

	

	

	
	
	public void print() {

		ArrayList<Node> allNodes = nodesArray.getAllNodes();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("##################################################");
		System.out.println("##################################################");
		System.out.println("############ PRINTING TREE #######################");
		System.out.println("##################################################");
		System.out.println("##################################################");
		System.out.println("Number of nodes: " + allNodes.size());
		for(Node n : allNodes) {
			n.print();
		}

		System.out.println("##################################################");
		System.out.println("##################################################");
		System.out.println("##################################################");
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ArrayList<Node> leaves() {
		ArrayList<Node> leaves = new ArrayList<Node>();
		
		for(Node n : nodesArray.getAllNodes()) {
			if(n.isLeaf())
				leaves.add(n);
		}
		
		return leaves;
	}
	
	
	
	/**
	 * Return unique path between leaves i and k (with i as origin, k as terminal)
	 * 
	 * @param i
	 * @param k
	 * @return
	 */
	private Path getPath(int i, int k) {
		
		Node originLeaf = nodesArray.get(i);
		Node terminalLeaf = nodesArray.get(k);
		
		Path path = new Path(originLeaf, terminalLeaf);
		
		getPathRec(null, originLeaf, path);
		
		
		return path;
	}
	
	
	/**
	 * Flesh out path with the inner nodes between its origin and terminal leaves
	 * 
	 * @param prev the node you just came from in this recursive path exploration. might be null.
	 * @param curr current node being investigated in DFS
	 * @param $path unique path between origin and terminal leaves. will only be altered if current node lies to path to terminal leaf
	 * @return whether this node leads to the terminal leaf (is on the path)
	 */
	private boolean getPathRec(Node prev, Node curr, Path $path) {
		
		Node originLeaf = $path.getOriginLeaf();
		Node terminalLeaf = $path.getTerminalLeaf();
		
		// BC reached leaf that is not the terminal leaf
		
		if(curr.isLeaf() && !curr.equals(originLeaf) && !curr.equals(terminalLeaf))
			return false;
		
		
		// BC reached the terminal leaf
		
		if(curr.equals(terminalLeaf)) {
			
			
			return true;
		}
		
		
		// RC
		
		ArrayList<Node> neighbors = curr.getNeighbors(prev);
		
		
		for(Node neighbor : neighbors) {
			if( getPathRec(curr, neighbor, $path) ) {
				
				// found unique path to terminal leaf
				
				if(! curr.equals(originLeaf)) // don't add origin leaf to list of inner nodes on path
					$path.insertInnerNode(0, curr);
				
				return true;
			}
		}
		
		// not on path
		return false;
	}
	
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	/**
	 * Path between two leaves, in (arbitrary) direction from originLeaf to terminalLeaf
	 * 
	 * To record which nodes are on a path, and in what order
	 * 
	 * Calculated ad hoc using SimpleTree.getPath(int,int)
	 * 
	 * 
	 * 
	 */
	private class Path {
		
		// arbitrary designations
		private Node originLeaf;  
		private Node terminalLeaf;
		
		// kept in order from origin to terminal leaf (exclusive of leaves)
		ArrayList<Node> innerNodes = new ArrayList<Node>(); 
		
		
		
	
		
		Path(Node originLeaf, Node terminalLeaf) {
			this.originLeaf = originLeaf;
			this.terminalLeaf = terminalLeaf;
		}
		

		
		
		
		/**
		 * Get length of next limb on path
		 * 
		 * @param currNode
		 * @return
		 */
		double nextLimbLength(Node currNode) {
			
			// next node on path
			
			Node nextNode = this.nextNode(currNode);
			
			ArrayList<Limb> currentLimbs = currNode.getLimbs();
			
			double length = -1;
			
			// find limb of current node that connects to next node
			
			for(Limb limb : currentLimbs) {
				if (limb.hasNode(nextNode))
					length = limb.getLength();
			}
			
			if(length < 0) new Exception().printStackTrace();
			
			
			return length;
			
		}
		
		
		
		
		
		/**
		 * Get next node on path
		 * 
		 * @param currNode 
		 * @return
		 */
		Node nextNode(Node currNode) {
			
			
			// case : origin leaf -> first inner node
			
			if(currNode.equals(originLeaf) && innerNodes.size() > 0) {
				
				
				return innerNodes.get(0);
				
			}

			
			// case : origin leaf -> terminal leaf
			
			if(currNode.equals(originLeaf) && innerNodes.size()==0) {
				return terminalLeaf;
			}
			
			
			
			// case: inner node -> inner node
			
			for(int i=0; i<innerNodes.size()-1; i++) {
				if(currNode.equals(innerNodes.get(i)))
						return innerNodes.get(i+1);
			}
			
			
			// case : last inner node -> terminal leaf
			if(currNode.equals(innerNodes.get(innerNodes.size()-1))) return terminalLeaf; 
			
			
			
			// case : terminal leaf -> null
			
			if( currNode.equals(terminalLeaf) ) return null;
			
			
			// error
			
			new Exception().printStackTrace();
			return null;
		}
		
		void insertInnerNode(int index, Node innerNode) {
			innerNodes.add(index,innerNode);
		}
		
		
	
		
		void appendInnerNode(Node innerNode) {
			innerNodes.add(innerNode);
		}
		
		Node getOriginLeaf() {
			return originLeaf;
		}
		
		Node getTerminalLeaf() {
			return terminalLeaf;
		}
		
		boolean isOnPath(Node node) {
			if(node.equals(originLeaf)) return true;
			else if(node.equals(terminalLeaf)) return true;
			else {
				for (Node innerNode : innerNodes) {
					if (node.equals(innerNode)) return true;
				}
			}
			
			return false;
		}
		
		
		void print() {
			StringBuilder res = new StringBuilder();
			
			res.append(originLeaf.toString());
			res.append("--(" + this.nextLimbLength(originLeaf) + ")-->");
			
			for(Node innerNode: this.innerNodes) {
				res.append(innerNode.toString());
				res.append("--(" + this.nextLimbLength(innerNode) + ")-->");
			}
			
			res.append(terminalLeaf.toString());
			
			System.out.println("Printing path: " + res.toString());
		}
		
	}
	
	
	
	
	
	
	/**
	 * One instance of Node per id
	 * 
	 * 
	 */
	private class Node {
		private int id;
		private ArrayList<Limb> limbs = new ArrayList<Limb>();
		
		
		
		/**
		 * Automatically adds node to nodesArray
		 * @param id
		 */
		public Node(int id) {
			this.id = id;
			nodesArray.add(this);
		}
		
		
		boolean removeLimb(Limb limb) {
			for(int i=0; i<limbs.size(); i++ ) {
				if(limb.equals(limbs.get(i))) {
					limbs.remove(i);
					return true;
				}
			}
			
			return false;
		}

		/**
		 * Won't add if already contains
		 * 
		 * @param edge
		 * @return true if added, false if already existed
		 */
		boolean addLimb(Limb edge) {
			if (this.hasEdge(edge)) {
				return false;
			} else {
				limbs.add(edge);
				return true;
			}
			
		}
		
		boolean hasEdge(Limb edge) {
			
			for(Limb e : limbs) {
				if(e.equals(edge))
					return true;
			}
			
			return false;
		}
		
		/**
		 * Return neighbors, excluding prevNeighbor
		 * 
		 * @param prevNeighbor previous neighbor. might be null
		 * @return
		 */
		ArrayList<Node> getNeighbors(Node prevNeighbor) {
			
			ArrayList<Node> neighbors = new ArrayList<Node>();
			
			for(Limb limb : limbs) {
				Node neighbor = limb.next(this);
				if(!neighbor.equals(prevNeighbor))
					neighbors.add(neighbor);
			}
			
			return neighbors;
		}
		
		/**
		 * 
		 * @return all neighbors to this node
		 */
		ArrayList<Node> getNeighbors() {
			
			ArrayList<Node> neighbors = new ArrayList<Node>();
			
			for(Limb limb : limbs) {
				Node neighbor = limb.next(this);
				neighbors.add(neighbor);
			}
			
			return neighbors;
		}
		
		
		public ArrayList<Limb> getNonDeadEndLimbs(boolean[] deadEnd) {
			ArrayList<Limb> nonDeadEndLimbs = new ArrayList<Limb>();
			
			for(Limb e : limbs) {
				int alphaId = e.getAlpha().getId();
				if(! deadEnd[alphaId]) {
					nonDeadEndLimbs.add(e);
				}
			}
			
			return nonDeadEndLimbs;
		}
		
		
		
		/**
		 * Get the limb connecting this node to nextNode
		 * 
		 * @param nextNode
		 * @return
		 */
		Limb getLimb(Node nextNode) {
			
			for(Limb limb : this.limbs) {
				if(limb.hasNode(nextNode))
					return limb;
			}
			
			
			// Error, no limb connecting this node to nextNode
			
			new Exception().printStackTrace();
			return null;
		}
		
		ArrayList<Limb> getLimbs() {
			
			return this.limbs;
		}
		
		boolean isLeaf() {
			return this.numLimbs() == 1;
		}
				
		int numLimbs() {
			return this.getLimbs().size();
		}


		public int getId() {
			return id;
		}

		

		

		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? ((Node)o).id == this.id : false;
		}
		
		public String toString() {
			return String.valueOf(this.id);
		}
		
		public void print() {
			for(Limb e : limbs) {
				e.print(this);
			}
		}
	}
	
	
	

	private class Limb {
		private Node alpha;
		private Node beta;
		private double length;
		
		public Limb(Node alpha, Node beta, double length) {
			this.alpha = alpha;
			this.beta = beta;
			this.length = length;
		}

		/**
		 * Return "next" Node on this edge, given that the arg is the "previous" node
		 * 
		 * 
		 * @param prev
		 * @return
		 */
		Node next(Node prev) {
			if(prev.equals(alpha)) return beta;
			else if(prev.equals(beta)) return alpha;
			
			else {
				new Exception().printStackTrace(System.err);
				return null;
			}
		}
		
		public Node getAlpha() {
			return alpha;
		}


		public Node getBeta() {
			return beta;
		}


		public double getLength() {
			return length;
		}

		
		public void print() {
			String str = "" + alpha.getId() + "->" + beta.getId() + ":" + length;
			System.out.println(str);
		}
		
		void print(Node firstNode) {
			String str = "" + firstNode.getId() + "->" + this.next(firstNode).getId() + ":" + String.format("%.3f", this.length);
			System.out.println(str);
		}
		
		boolean hasNode(Node node) {
			if( alpha.equals(node)) return true;
			if( beta.equals(node)) return true;
			
			return false;
		}
		
		@Override
		public boolean equals(Object o) {
			if(! (o instanceof Limb) )
				return false;
			
			if(((Limb)o).getLength() != this.getLength()) {
				return false;
			}

			Node alpha = ((Limb)o).getAlpha();
			Node beta = ((Limb)o).getBeta();

			
			if(  alpha.equals(this.alpha) & beta.equals(this.beta) ) {
				return true;
			}
			
			if(  alpha.equals(this.beta) & beta.equals(this.alpha) ) {
				
				return true;
			}	

			
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
			if(underlyingArray[index] == null) return false;
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
		
	}

	
	
}








