package util;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import util.IOUtilities;
import util.Utilities;
import util.ParsimonyTree.Node;






/**
 * Undirected tree
 * 
 * 
 * 
 * keeps track of all nodes in nodesArray
 * nodes keep their edges
 * 
 * one instance of Node per node
 * one instance of UndirectedEdge per edge
 * only supports up to 1 edge betweeen nodes
 * 
 * 
 * Design flaw: when adding node/edge, should just have a Node.addNeighbor(Node) method that looks up the edge if it already exists
 */
public class UndirectedTree {

	private DynamicNodeArray nodesArray = new DynamicNodeArray(); // keeps references to all added nodes
	private int numLeavesEncountered = 0;
	public int STRING_LEN;
	
	
	boolean weighted = false;
	
	
	
	boolean debug = false;
	
	
	
	
	
	
	
	
	
	
	public UndirectedTree() {
		
	}
	
	
	
	
	
	public UndirectedTree(int stringLen) {
		this.STRING_LEN = stringLen;
	}
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param edgeStr "0->1:7" means "an edge from node 0 to node 1 with weight 7"
	 *                "0->1" means "an edge from node 0 to node 1"
	 */
	public void parseAddNodesEdge(String edgeStr) {
		
		String[] firstSplit = edgeStr.split("->");			
		String[] secondSplit = firstSplit[1].split(":");

		
		int parentId = Integer.parseInt(firstSplit[0]);
		int childId, weight = -1;
		
		
		
		if(secondSplit.length == 1) { // no weight
			childId = Integer.parseInt(firstSplit[1]);

		} else {
			childId = Integer.parseInt(secondSplit[0]);;
			weight = Integer.parseInt(secondSplit[1]);
		}
		
		
		
		
		
		// retrieve or create the nodes of edge
		
		
		
		
		
		
		
		Node parent = nodesArray.exists(parentId) ? nodesArray.get(parentId) : new Node(parentId);
		Node child = nodesArray.exists(childId) ? nodesArray.get(childId) : new Node(childId);

		
		UndirectedEdge edge = new UndirectedEdge(parent, child, weight);
		
		
		boolean b1 = parent.addUndirectedEdge(edge);
		boolean b2 = child.addUndirectedEdge(edge);

	}



	
	
	
	/**
	 * 
	 * @param edgeStr
	 */
	public void parseAddNodesEdgeWithSequences(String edgeStr) {
		
		String[] split = edgeStr.split("->");
		
		Node parent, child;
		
		
		
		
		try { // parent is in id form
			
			int parentId = Integer.parseInt(split[0]);
			parent = nodesArray.exists(parentId) ? nodesArray.get(parentId) : new Node(parentId, null);
			
		} catch (Exception e) { // parent is in dna sequence form
			
			if(STRING_LEN == 0) STRING_LEN = split[0].length();
			
			int parentId = nodesArray.idFromDnaSequence(split[0]);
			parent = nodesArray.exists(parentId) ? nodesArray.get(parentId) : new Node(parentId, split[0]);
			
		}
		

		
		try {// child is in id form
			
			int childId = Integer.parseInt(split[1]);
			child = nodesArray.exists(childId) ? nodesArray.get(childId) : new Node(childId, null);
			
		} catch (Exception e) { // child is in dna sequence form
			
			int childId = nodesArray.idFromDnaSequence(split[1]);
			child =  nodesArray.exists(childId) ? nodesArray.get(childId) : new Node(childId, split[1]);
			
		}
		
		
		
		parent.addNeighbor(child,0);
		child.addNeighbor(parent,0);
		
		
		
		
	}
	
	
	
	
	
	
	/**
	 * Clone the nodesArray (which clones and fills with all nodes, upon creation)
	 * Then clone edges (which attaches to its cloned nodes, upon creation)
	 * 
	 * 
	 */
	public UndirectedTree clone() {
		
		UndirectedTree clone = new UndirectedTree();
		
		clone.nodesArray = this.nodesArray.clone();
		clone.STRING_LEN = this.STRING_LEN;
		
		ArrayList<UndirectedEdge> allEdges = this.getAllEdges();
		
		for(UndirectedEdge edge : allEdges) {
			edge.clone(clone.nodesArray);
		}
		
		return clone;
	}
	
	
	
	
	
	/**
	 * Won't add if already exists (but will set dnaSequence)
	 * Creates and returns new node if does not exist
	 */	
	Node addNode(int id, String ... dnaSequenceArg) {

		String dnaSequence = null;

		if(dnaSequenceArg.length > 0) {
			dnaSequence = dnaSequenceArg[0];
		}


		
		Node node = nodesArray.exists(id) ? nodesArray.get(id) : new Node(id, dnaSequence);
		node.dnaSequence = dnaSequence;

		return node;

	}
	
	
	


	UndirectedEdge addEdge(int alphaId, int betaId, int ... weightArg) {

		int weight = (weightArg.length > 0) ? weightArg[0] : 0;

		Node alpha = nodesArray.exists(alphaId) ? nodesArray.get(alphaId) : new Node(alphaId);
		Node beta = nodesArray.exists(betaId) ? nodesArray.get(betaId) : new Node(betaId);

		UndirectedEdge edge = alpha.getEdge(beta);

		if(edge == null) {
			edge = new UndirectedEdge(alpha, beta, weight);
		}

		return edge;

	}






	/**
	 * Finds nearest neighbors for all internal edges in tree
	 *
	 */	
	public ArrayList<UndirectedTree> nearestNeighbors() {


		ArrayList<UndirectedTree> nearestNeighbors = new ArrayList<UndirectedTree>();

		ArrayList<UndirectedEdge> allInternalEdges = this.getAllInternalEdges();


		for(UndirectedEdge edge : allInternalEdges) {

			int alphaId = edge.getAlpha().getId();
			int betaId = edge.getBeta().getId();

//System.out.println(alphaId);
//System.out.println(betaId);
			ArrayList<UndirectedTree> localNearestNeighbors = this.nearestNeighbors(alphaId, betaId);

			nearestNeighbors.addAll(localNearestNeighbors);

		}

		return nearestNeighbors;

	}




	
	/**
	 * Find two nearest neighbor trees using the edge between alphaId and betaId
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param alphaId
	 * @param betaId
	 * @return
	 */
	public ArrayList<UndirectedTree> nearestNeighbors(int alphaId, int betaId) {
		

		ArrayList<UndirectedTree> nearestNeighbors = new ArrayList<UndirectedTree>();
		
		UndirectedTree firstClone = this.clone();
		UndirectedTree secondClone = this.clone();
		
		nearestNeighbors.add(firstClone);
		nearestNeighbors.add(secondClone);
		
		Node alpha = nodesArray.get(alphaId);
		Node beta = nodesArray.get(betaId);
		
		
		// get the id's of the children of the edge between alpha and beta
		
		
		int alphaRightChildId = alpha.getRightChild(beta).getId();
		int betaLeftChildId = beta.getLeftChild(alpha).getId();
		int betaRightChildId = beta.getRightChild(alpha).getId();
		
		
		
		// switch alpha's right child with beta's left child

		firstClone.switchChildren(alphaId, alphaRightChildId, betaId, betaLeftChildId);
		
		
		// switch alpha's right child with beta's right child
		
		secondClone.switchChildren(alphaId, alphaRightChildId, betaId, betaRightChildId);

		
		
		return nearestNeighbors;
	}
	
	
	
	
	
	
	
	
	
	
	public ParsimonyTree createParsimonyTree() {
		
		ParsimonyTree pTree = new ParsimonyTree(this.numLeaves(), this.STRING_LEN);
		
		
		int numNodes = this.numNodes();
		
		
		// create root, since parsimony trees are rooted
		
		ParsimonyTree.Node root = pTree.new Node(numNodes, null);
		pTree.setRoot(root);
		
		
		// find an internal edge to insert root
		
		UndirectedEdge edge = this.getAllInternalEdges().get(0);
		//UndirectedTree.UndirectedEdge edge = this.getEdge(numNodes-1, numNodes-2);

		
		UndirectedTree.Node left = edge.getAlpha();
		UndirectedTree.Node right = edge.getBeta();
		
		ParsimonyTree.Node leftSubTree = getSubTreeRec(left,right,pTree);
		ParsimonyTree.Node rightSubTree = getSubTreeRec(right,left,pTree);
		
		root.addChild(leftSubTree);
		root.addChild(rightSubTree);
		
		leftSubTree.setParent(root);
		rightSubTree.setParent(root);
		
		return pTree;
	}
	
	
	
	
	
	
	
	
	/**
	 * Return subtree rooted at currNode (in ParsimonyTree form)
	 * 
	 * Create  currNode
	 * Get left and right subtrees, attach to currNode
	 * 
	 * 
	 * @param currNode
	 * @param prevNode
	 * @return subtree rooted at currNode
	 */
	ParsimonyTree.Node getSubTreeRec(Node currNode, Node prevNode, ParsimonyTree pTree) {
		

		ParsimonyTree.Node currSPTNode = pTree.new Node(currNode.getId(), currNode.getDnaSequence());
		
		
		// BC
		
		if(currNode.isLeaf()) {
			return currSPTNode;
		}
		
		
		// RC
		
		
		ParsimonyTree.Node leftSubTree = getSubTreeRec(currNode.getLeftChild(prevNode), currNode, pTree);
		ParsimonyTree.Node rightSubTree = getSubTreeRec(currNode.getRightChild(prevNode), currNode, pTree);
		
		
		currSPTNode.addChild(leftSubTree);
		currSPTNode.addChild(rightSubTree);
		
		leftSubTree.setParent(currSPTNode);
		rightSubTree.setParent(currSPTNode);
		
		
		return currSPTNode;
	}
	
	
	
	
	

	/**
	 * Finds distance matrix for distances between all leaves
	 */
	public void distanceMatrix() {
		ArrayList<Node> leaves = this.leaves();
		int numLeaves = leaves.size();
		
		
		int[][] distanceMatrix = new int[numLeaves][numLeaves];
		
		
		for( Node root : leaves) { // select arbitrary leaf as "root"
			
			int rootId = root.getId();
			
			ArrayList<Path> paths = measurePathsRecursive(null, root, root);
			
			System.out.println("num paths " + paths.size());
			
			for (Path path : paths) {
				Node leaf = path.getLeaf();
				int leafId = leaf.getId();
				int length = path.getLength();
				
				distanceMatrix[rootId][leafId] = length;
			}

			
		}

		util.IOUtilities.printArray("distance", distanceMatrix);
		
	}
	
	
	/**
	 * Call initially with an arbitrary leaf as the root
	 * Finds all paths (and their lengths) between all leaves
	 * 
	 * @param prevEdge the edge you just came from
	 * @param currNode
	 * @return all paths stemming from currNode (away from root), with all lengths added in
	 */
	private ArrayList<Path> measurePathsRecursive(UndirectedEdge prevEdge, Node currNode, Node root) {
		
		
		// BC reached leaf
		if( currNode.isLeaf() && currNode != root) {
			Path terminalPath = new Path(currNode);
			ArrayList<Path> paths = new ArrayList<Path>();
			paths.add(terminalPath);
			return paths;
		}
		
		// RC
		
		
		
		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		for(UndirectedEdge edge : currNode.getUndirectedEdges()) {	// visit all edges
												// stemming from currNode
			if(edge != prevEdge) { 				// except edge you just came from
				
				ArrayList<Path> newPaths = measurePathsRecursive(edge, edge.next(currNode), root);
				
				for (Path path : newPaths) {
					
					path.addWeight(edge.getWeight());
					
					
				}
				
				paths.addAll(newPaths);
				
			}
			
		}
		
		
		
		return paths;
	}
	

	public int numNodes() {
		return nodesArray.numNodes;
	}
	
	
	
	/**
	 * return edge between alpha and beta
	 * 
	 * @param alphaId
	 * @param betaId
	 * @return
	 */
	public UndirectedEdge getEdge(int alphaId, int betaId) {
		Node alpha = nodesArray.get(alphaId);
		Node beta = nodesArray.get(betaId);
		return alpha.getEdge(beta);
	}
	
	
	
	public ArrayList<UndirectedEdge> getAllEdges() {
		
		Node root = nodesArray.getInternalNode();
		
		ArrayList<UndirectedEdge> allEdges = new ArrayList<UndirectedEdge>();
		
		root.getAllEdgesRec(null, allEdges);
		
		return allEdges;
		
	}
	
	




	public ArrayList<UndirectedEdge> getAllInternalEdges() {


		Node root = nodesArray.getInternalNode();
//System.out.println("root is " + root.getId());
		
		ArrayList<UndirectedEdge> allInternalEdges = new ArrayList<UndirectedEdge>();
		
		root.getAllEdgesRec(null, allInternalEdges,true);
		
		return allInternalEdges;
		

	}	



	
	
	
	void switchChildren(int parent1Id, int child1Id, int parent2Id, int child2Id) {
		
		Node child1 = nodesArray.get(child1Id),
				child2 = nodesArray.get(child2Id);
		
		
		
		
		UndirectedEdge edge1 = getEdge(parent1Id, child1Id);
		UndirectedEdge edge2 = getEdge(parent2Id, child2Id);
		
		
		child1.replaceEdge(edge1, edge2);
		child2.replaceEdge(edge2, edge1);
		
		
		edge1.replaceNode(child1, child2);
		edge2.replaceNode(child2, child1);
		
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @return all leaf Nodes
	 */
	private ArrayList<Node> leaves() {
		ArrayList<Node> leaves = new ArrayList<Node>();
		
		for(Node n : nodesArray.getAllNodes()) {
			if(n.isLeaf())
				leaves.add(n);
		}
		
		return leaves;
	}
	
	
	private int numLeaves() {
		return this.leaves().size();
	}
	
	
	/**
	 * Prints all edges
	 */
	public void print() {
		if(debug) System.out.println("Number of nodes: " + nodesArray.getNumNodes());
		for(Node n : nodesArray.getAllNodes()) {
			if(n != null)
				n.print();
		}
		
		System.out.println();
	}
	
	
	
	
	
	
	
	
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * One instance of Node per id
	 * 
	 * 
	 */
	public class Node {
		private int id;
		private ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>();
		
		
		String dnaSequence;
		
		
		
		public Node(int id) {
			this.setId(id);
			
			nodesArray.add(this);
		}
		
		
		public Node(int id, String dnaSequence) {
			this.id = id;
			this.dnaSequence = dnaSequence;
			
			nodesArray.add(this);
		}
		
		
		
		/**
		 * For binary tree structure
		 * 
		 * 
		 * @param parent
		 * @return
		 */
		public Node getLeftChild(Node parent) {
			for(UndirectedEdge edge : edges) {
				Node neighbor = edge.next(this);
				if(neighbor.equals(parent))
					continue;
				else
					return neighbor;
			}
			
			return null;
		}
		
		
		/**
		 * 
		 * @param parent
		 * @return
		 */
		public Node getRightChild(Node parent) {
			boolean encounteredLeft = false;
			
			for(UndirectedEdge edge : edges) {
				Node neighbor = edge.next(this);
				if(neighbor.equals(parent))
					continue;
				else if( !encounteredLeft )
					encounteredLeft = true;
				else
					return neighbor;
			}
			
			return null;
		}
		
		
		
		/**
		 * Will use existing edge if edge already made
		 * Won't add edge to this node again if already has
		 * 
		 * 
		 * 
		 * 
		 * @param neighbor
		 * @param weight
		 */
		void addNeighbor(Node neighbor, int ... weightArg) {
			
			int weight = 0;

			if(weightArg.length > 0) weight = weightArg[0];


			UndirectedEdge edge = this.getEdge(neighbor);
			
			if(edge == null) {
				edge = new UndirectedEdge(this,neighbor,weight);
			}


			
			this.addUndirectedEdge(edge);
			
		}
		
		
		

		/**
		 * Won't add if already contains
		 * 
		 * @param edge
		 * @return true if added, false if already existed
		 */
		boolean addUndirectedEdge(UndirectedEdge edge) {
			if (this.hasEdge(edge)) {
				return false;
			} else {
				edges.add(edge);
				return true;
			}
			
		}
		
		
		boolean hasEdge(UndirectedEdge edge) {
			
			for(UndirectedEdge e : edges) {
				if(e.equals(edge))
					return true;
			}
			
			return false;
		}
		
		
		
		boolean hasEdgeTo(Node next) {
			return this.getEdge(next) != null;
		}
		
		
		
		/**
		 * Get edge connecting this to next
		 * 
		 * Looks through next's edges too
		 *
		 * @param next
		 * @return
		 */
		UndirectedEdge getEdge(Node next) {
			for(UndirectedEdge edge : edges) {
				if(edge.has(next))
					return edge;
			}

			for(UndirectedEdge edge : next.edges) {
				if(edge.has(this))
					return edge;
			}

			return null;
		}


		/**
		 * remove edge connected this node to the param
		 * 
		 * @param next
		 */
		void removeEdge(Node next) {
			for(int i=0; i<edges.size(); i++) {
				UndirectedEdge edge = edges.get(i);
				
				if(edge.has(next)) {
					edges.remove(i);
					return;
				}
			}
		}
		
		void replaceEdge(UndirectedEdge original, UndirectedEdge replacement) {
			
			
			for(int i=0; i<edges.size(); i++) {
				UndirectedEdge edge = edges.get(i);
				
				if(edge.equals(original)) {
					edges.remove(i);
					edges.add(i,replacement);
				}
			}
			
		}
		
		public ArrayList<UndirectedEdge> getNonDeadEndIncomingEdges(boolean[] deadEnd) {
			ArrayList<UndirectedEdge> nonDeadEndIncomingEdges = new ArrayList<UndirectedEdge>();
			
			for(UndirectedEdge e : edges) {
				int parentId = e.getAlpha().getId();
				if(! deadEnd[parentId]) {
					nonDeadEndIncomingEdges.add(e);
				}
			}
			
			return nonDeadEndIncomingEdges;
		}
		
		
		ArrayList<UndirectedEdge> getUndirectedEdges() {
			
			return this.edges;
		}
		
		
		/**
		 * Call for the first time using internal node (makes BC easier)
		 * 
		 * 
		 * Add all "next" edges, then visit "next" nodes
		 * 
		 * 
		 * 
		 * 
		 * 
		 * @param internalOnly only add internal edges (ones not connected to leaves) 
		 */
		void getAllEdgesRec(Node prev, ArrayList<UndirectedEdge> $allEdges, boolean ... internalOnlyArg) {
			

			boolean internalOnly = internalOnlyArg.length > 0 ? internalOnlyArg[0] : false;


//System.out.println(internalOnlyArg.length);
//System.out.println(internalOnly);
			// BC
			
			if(this.isLeaf()) return;
			
			
			
			for(UndirectedEdge edge : edges) {
				
				// don't add/visit edge you just came from
				if(edge.has(prev))
					continue;

				Node next = edge.next(this);


				// if only adding internal nodes, don't add if next is a leaf
				if(internalOnly && next.isLeaf())
					continue;

//System.out.println(edge.toString());				
				$allEdges.add(edge);
			
				edge.next(this).getAllEdgesRec(this, $allEdges, internalOnlyArg);
			}
		}
		
		
		
		public boolean isLeaf() {
			return this.numUndirectedEdges() == 1;
		}
				
		int numUndirectedEdges() {
			return this.getUndirectedEdges().size();
		}

		public String getDnaSequence() {
			return this.dnaSequence;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		
		protected Node clone() {
			
			return new Node(this.id, this.dnaSequence);
			
		}

		

		
		@Override
		public boolean equals(Object o ){
			return o instanceof Node ? ((Node)o).id == this.id : false;
		}
		
		public String toString() {
			return String.valueOf(this.id);
		}
		
		public void print() {
			for(UndirectedEdge e : edges) {
				e.print(this);
			}
		}
	}
	
	
	

	public class UndirectedEdge {
		private Node alpha;
		private Node beta;
		private int weight;
		
		public UndirectedEdge(Node parent, Node child, int weight) {
			this.setAlpha(parent);
			this.setBeta(child);
			this.setWeight(weight);
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
		
		boolean has(Node node) {
			if(alpha.equals(node))
				return true;
			if(beta.equals(node))
				return true;
			return false;
		}
		
		public Node getAlpha() {
			return alpha;
		}

		public void setAlpha(Node alpha) {
			this.alpha = alpha;
		}

		public Node getBeta() {
			return beta;
		}

		public void setBeta(Node beta) {
			this.beta = beta;
		}

		/**
		 * Replace original with replacement
		 * 
		 * @param original
		 * @param replacement
		 */
		void replaceNode(Node original, Node replacement) {
			if(alpha.equals(original)) {
				alpha = replacement;
			} else
			if(beta.equals(original)) {
				beta = replacement;
			}
		}
		
		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
		
		public void print() {
			String str = "" + alpha.getId() + "->" + beta.getId();
			if(weighted) str += ":" + weight;
			System.out.println(str);
		}
		
		
		public void print(Node printer) {
			String str;
			
			if(printer.equals(alpha))
				str = "" + alpha.getId() + "->" + beta.getId();
			else
				str = "" + beta.getId() + "->" + alpha.getId();
			if(weighted) str += ":" + weight;
			System.out.println(str);
		}
		
		
		
		/**
		 * Create clone edge from clone nodes
		 * Attach to clone nodes
		 * 
		 * 
		 * 
		 * @param cloneNodesArray
		 */
		void clone(DynamicNodeArray cloneNodesArray) {
			
			int alphaId = alpha.getId();
			int betaId = beta.getId();
			
			Node cloneAlpha = cloneNodesArray.get(alphaId);
			Node cloneBeta = cloneNodesArray.get(betaId);
			
			
			UndirectedEdge cloneEdge =  new UndirectedEdge(cloneAlpha, cloneBeta, this.weight);
			
			cloneAlpha.addUndirectedEdge(cloneEdge);
			cloneBeta.addUndirectedEdge(cloneEdge);
						
		}
		
		
		@Override
		public boolean equals(Object o) {
			if(! (o instanceof UndirectedEdge) )
				return false;
			
			if(((UndirectedEdge)o).getWeight() != this.getWeight()) {
				return false;
			}

			Node alpha = ((UndirectedEdge)o).getAlpha();
			Node beta = ((UndirectedEdge)o).getBeta();

			
			if(  alpha.equals(this.alpha) & beta.equals(this.beta) ) {
				return true;
			}
			
			if(  alpha.equals(this.beta) & beta.equals(this.alpha) ) {
				
				return true;
			}	

			
			return false;
		}


		public String toString() {
			return "" + alpha.getId() + "->" + beta.getId();
		}	
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * not robust. space efficient if node id's start low and are consecutive
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
		
		DynamicNodeArray(int capacity) {
			this.capacity = capacity;
			underlyingArray = new Node[capacity];
		}
		
		
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
		
		
		/**
		 * 
		 * @return any internal node
		 */
		public Node getInternalNode() {
			for(Node node : underlyingArray) {
				if(node == null) continue;
				if(node.isLeaf()) continue;
				
				return node;
			}
			
			return null;
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
		
		int idFromDnaSequence(String dnaSequence) {
			
			for(Node node : underlyingArray) {
				if(node != null) {
					if(dnaSequence.equals(node.dnaSequence))
						return node.id;
				}
			}
			
			
			return numLeavesEncountered++;
		}
		
		
		
		/**
		 * Call from UndirectedTree.clone()
		 * 
		 * 
		 * Create clone with underlying array of same capacity
		 * fill with clones of nodes
		 * 
		 */
		protected DynamicNodeArray clone() {
			
			DynamicNodeArray clone = new DynamicNodeArray(this.capacity);
			
			for(int i=0; i<capacity; i++) {
				Node node = underlyingArray[i];
				if(node != null) {
					clone.underlyingArray[i] = node.clone();
				}
			}
			
			clone.numNodes = this.numNodes;
			
			return clone;
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * For finding path between two leaves
	 *
	 */
	private class Path {
		private int length = 0;
		private Node originLeaf;
		private Node terminalLeaf;
		
		Path(Node leaf) {
			this.terminalLeaf = leaf;
		}
		
		int getLength() {
			return this.length;
		}
		
		void addWeight(int weight) {
			length += weight;
		}
		
		public Node getLeaf() {
			return terminalLeaf;
		}
		public void setLeaf(Node leaf) {
			this.terminalLeaf = leaf;
		}
		
		
		
	}
	
	
}








