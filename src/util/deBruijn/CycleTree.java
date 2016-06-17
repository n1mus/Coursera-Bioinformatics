package util.deBruijn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import util.deBruijn.DeBruijnGraph;
import util.deBruijn.DeBruijnGraph.*;







/**
 * Re-purposed from DirectedTree
 * 
 * 
 * 
 * 
 * 
 * 
 * Auxiliary to DeBruijnGraph
 * 
 * 
 * 
 * 
 * 
 */
public class CycleTree<P extends Comparable> {
	
	private DBCycleNode root;
	private ArrayList<DBCycleNode> cycleNodes = new ArrayList<DBCycleNode>();
	

	

	
	
	
	
	

	
	
	/**
	 * 
	 * 
	 * 
	 * find Eulerian cycle
	 * represented by nodes path
	 * 
	 * e.g., '6->8->7->9->6->5->4->2->1->0->3->2->6'
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
	 * @return
	 */
	public
	ArrayList<Integer> traverseEulerianCycle() {
		
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		
		traverseEulerianCycleRec(this.root, result);
		
		
		// append first node
		// to make it a proper cycle
		// starting and ending on same node
		result.add(result.get(0));
		
		
		return result;
		
		
	}
	
	
	
	// TODO sort cyclenode outgoing edges
	
	
	/**
	 * Postorder visit a DBCycleNode
	 * 
	 * First dB node in cycleNode is intersection with parent cycle node
	 * 
	 * 
	 * @param cycleNode
	 * @param $dBNodeIdsList global list of dB node ids, 
	 * 						starting with a dB node in the root cycle
	 */
	private
	void
	traverseEulerianCycleRec(DBCycleNode cycleNode, ArrayList<Integer> $dBNodeIdsList) {
		
		System.out.println("landed on cycle node " + cycleNode.getId());
		
		for(int dBCE=0, // iterate through DBCycleEdges and respective DBCycleNode children
				dBN=0;  // iterate through dB nodes in cycle itself (in cycleNode)
				dBN<cycleNode.numDBNodes()-1; // since cycleNode holds a cycle, 
											// begins and ends with same node
											// omit last node to avoid repeats
				dBN++)  
		{
			
			
			// next dB node
			Node dBNode = cycleNode.cycle.nodesList.get(dBN);
			int dBNodeId = dBNode.getId();
			
			
			
			
			// next cycle edge/child
			// default impossible values, in case doesn't exist
			// or all cycle edges/children traversed already
			DBCycleEdge nextCycleEdge = null;
			int nextIntersectionDBNodeId = -1; 
			
			
			// assign next cycle edge/child
			// if exists
			if(dBCE < cycleNode.numOutgoingEdges()) {
				nextCycleEdge = cycleNode.getOutgoingEdges().get(dBCE);
				nextIntersectionDBNodeId = nextCycleEdge.intersectionDBNodeId;
			}

			
			
			// if you've hit that next cycle edge/child
			// visit, starting at intersection node
			if(dBNodeId == nextIntersectionDBNodeId) {
				traverseEulerianCycleRec(nextCycleEdge.getChild(), $dBNodeIdsList);
				
				// return from visiting descendants of the DBCycleNode				
				dBCE++;
			}

			
			
			// visit this dB node
			// if traversed descendants of this DBCycleNode,
			// just returned to this dB node
			$dBNodeIdsList.add(dBNodeId);
			
			System.out.println("officially visiting in cycle " + cycleNode.getId() + " the dB node " + dBNode.id );

		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * DBCycleNode and DBCycleEdge are bare bones,
	 * so do all connecting here
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param parentDBCycleNode
	 * @param child
	 * @param intersectingDBNode
	 * @return 
	 */
	DBCycleNode
	addDBCycleNode(DBCycleNode parentDBCycleNode, Cycle child, int intersectingDBNode) {
		
		
		DBCycleNode newCycleNode = new DBCycleNode(child);
		
		// adding root node
		if(parentDBCycleNode == null) {
			this.root = newCycleNode;
		} 
		// adding child node
		// connect to parent
		else {
			DBCycleEdge cycleEdge = new DBCycleEdge(parentDBCycleNode, newCycleNode, intersectingDBNode);
			
			newCycleNode.setIncomingEdge(cycleEdge);
			parentDBCycleNode.addOutgoingEdge(cycleEdge);
		}
		
		cycleNodes.add(newCycleNode);
		
		return newCycleNode;
	}
	
	
	
	
	
	
	
	
	
//	/**
//	 * 
//	 * 
//	 * 
//	 * 
//	 * 
//	 * @param dBCycleNode
//	 * @return
//	 */
//	DeBruijnGraph.Node
//	getDBNodeWUnvisitedEdges(DBCycleNode dBCycleNode) {
//		
//		return dBCycleNode.getDBNodeWUnvisitedEdges();
//	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Searches for cycle nodes
	 * in order they were created
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	DBCycleNode
	getDBCycleNodeWUnvisitedEdges() {
		for(DBCycleNode cycleNode : cycleNodes) {
			if(cycleNode.hasUnvisitedEdges()) return cycleNode;
		}
		
		return null;
	}
	
	
	
	
	void
	print() {
		for(String str : getPrint()) {
			System.out.println(str);
		}
	}
	

	
	ArrayList<String> getPrint() {
		ArrayList<String> print = new ArrayList<String>();
		
		for(DBCycleNode cycleNode : cycleNodes) {
			print.add(cycleNode.getPrint());
		}
		
		
		print.add("cycle tree size is " + cycleNodes.size());
		print.add("total num dB nodes is " + totalNumDBNodes());
		print.add("average num dB nodes per cycle node is " + getAverageNumDBNodesPerCycleNode());
		return print;
	}
	
	


	int totalNumDBNodes() {
		int totalDBNodes = 0;
		
		for(DBCycleNode cycleNode : cycleNodes) {
			totalDBNodes += cycleNode.numDBNodes() - 1;
		}
		return totalDBNodes;
	}
	
	
	float getAverageNumDBNodesPerCycleNode() {
	
		return (float)(totalNumDBNodes()) / cycleNodes.size();
	}
	
	
	

		
		
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
class DBCycleNode {
	
	
	
	Cycle cycle;
	
	
	DBCycleEdge incomingEdge;
	ArrayList<DBCycleEdge> outgoingEdges = new ArrayList<DBCycleEdge>();

	
	
	
	

	DBCycleNode(Cycle cycle, int intersectionWParent) {
		this.cycle = cycle;
	}
	
	DBCycleNode(Cycle cycle) {
		this.cycle = cycle;
	}
	

	void setIncomingEdge(DBCycleEdge edge) {
		incomingEdge = edge;
	}
	
	public void addOutgoingEdge(DBCycleEdge edge) {
		outgoingEdges.add(edge);
	}
	
	ArrayList<DBCycleEdge> getOutgoingEdges() {
		return this.outgoingEdges;
	}

	 
	 
	
	int numOutgoingEdges() {
		return outgoingEdges.size();
	}

	 
	 

	
	public DBCycleNode getParent() {
		
		return this.incomingEdge.parent;
	}
	

	
	

	

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	boolean
	hasUnvisitedEdges() {
		return cycle.hasUnvisitedEdges();
	}
	
	
	
	/**
	 * Returns null if does not have dB node with unvisited edges
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @return
	 */
	Node
	getDBNodeWUnvisitedEdges() {
		return cycle.getDBNodeWUnvisitedEdges();
	}
	
	
	
	/**
	 * 
	 * index(start) <= index(end)
	 * 
	 * 
	 * 
	 * @param start - first node id
	 * @param end - end node id
	 * @return ids of all nodes inbetween (exclusive)
	 */
	
	ArrayList<Integer>
	getSubPath(int start, int end) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int startInd = cycle.getInternalInd(start);
		int endInd = cycle.getInternalInd(end);
		
		for(int i=startInd+1; i<endInd; i++) {
			int id = cycle.nodesList.get(i).getId();
			result.add(id);
		}
		
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	void addChild(DBCycleNode child) {
		DBCycleEdge childEdge = new DBCycleEdge(this, child, 0);
		outgoingEdges.add(childEdge);
	}
	
	ArrayList<DBCycleNode> getChildren() {
		ArrayList<DBCycleNode> children = new ArrayList<DBCycleNode> ();
		for(DBCycleEdge outgoingEdge : outgoingEdges) {
			DBCycleNode child = outgoingEdge.getChild();
			children.add(child);
		}
		
		return children;
	}
	

	
	
	
	int numDBNodes() {
		return cycle.nodesList.size();
	}
	
	
	
	
	/**
	 * 
	 * Should be located in DeBruijnGraph class,
	 * but locate here as well for convenience
	 * 
	 * 
	 */
	void
	resetEdgesVisitedGlobally() {
		cycle.resetEdgesVisitedGlobally();
	}

	
	String getId() {
		return cycle.getPrint();
	}
	
	String getPrint() {
		String res = "";
		
		for(DBCycleEdge edge : outgoingEdges) {
			res += edge.getPrint() + "\n";
		}
	
		return res;
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
 * 
 * 
 * 
 * 
 */
class DBCycleEdge {
	DBCycleNode parent;
	DBCycleNode child;
	int intersectionDBNodeId;
	
	
	DBCycleEdge() {
		
	}
	
	public DBCycleEdge(DBCycleNode parent, DBCycleNode child, int intersectionDBNodeId) {
		this.setParent(parent);
		this.setChild(child);
		this.intersectionDBNodeId = intersectionDBNodeId;
	}

	public DBCycleNode getParent() {
		return parent;
	}

	public void setParent(DBCycleNode parent) {
		this.parent = parent;
	}

	public DBCycleNode getChild() {
		return child;
	}

	public void setChild(DBCycleNode child) {
		this.child = child;
	}


	
	String getPrint() {
		String str = "parent cycle node ";
		str += parent.getId();
		str += " intersects child cycle node ";
		str += child.getId();
		str += " at dB node ";
		str += intersectionDBNodeId;
		
		return str;
	}
	
	
}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	


