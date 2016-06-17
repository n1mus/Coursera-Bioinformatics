package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;
import util.ParsimonyTree;
import util.UndirectedTree;

public class UndirectedGraphToParsimonyTree {

	static boolean debug = false;
	
	
	static ParsimonyTree pTree;
	static UndirectedTree uTree = new UndirectedTree();
	static int numLeaves;
	
	
	
	
	public static void main(String[] args) {

		
		
		
		
		
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			numLeaves = reader.nextInt();
	
			
			
			while(reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if( line.length() > 0)
					uTree.parseAddNodesEdgeWithSequences(line);
				
			}
			
			
			pTree = new ParsimonyTree(numLeaves);
			pTree.STRING_LEN = uTree.STRING_LEN;

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		
		
		
		
		
		uTree.print();
		System.out.println("\n\n");
		
		
		
		
		
		
		
		
		convert();
		
		
		pTree.smallParsimony();
		pTree.unroot();
		ArrayList<String> print = pTree.getPrint();
		
		
		if(debug) util.IOUtilities.printAbstractCollection("small parsimony tree", "", print);
		
		util.IOUtilities.writeToTxtFile(print, "undirectedGraphToSmallParsimony");		
		
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Build spTree from uTree
	 * 
	 * 
	 */
	static void convert() {
		
		int numNodes = uTree.numNodes();
		
		
		ParsimonyTree.Node root = pTree.new Node(numNodes, null);
		pTree.setRoot(root);
		
		
		// find edge in uTree to insert root
		
		UndirectedTree.UndirectedEdge edge = uTree.getEdge(numNodes-1, numNodes-2);
		
		UndirectedTree.Node left = edge.getAlpha();
		UndirectedTree.Node right = edge.getBeta();
		
		ParsimonyTree.Node leftSubTree = getSubTreeRec(left,right);
		ParsimonyTree.Node rightSubTree = getSubTreeRec(right,left);
		
		root.addChild(leftSubTree);
		root.addChild(rightSubTree);
		
		leftSubTree.setParent(root);
		rightSubTree.setParent(root);
		
		
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
	static ParsimonyTree.Node getSubTreeRec(UndirectedTree.Node currNode, UndirectedTree.Node prevNode) {
		

		ParsimonyTree.Node currSPTNode = pTree.new Node(currNode.getId(), currNode.getDnaSequence());
		
		
		// BC
		
		if(currNode.isLeaf()) {
			return currSPTNode;
		}
		
		
		// RC
		
		
		ParsimonyTree.Node leftSubTree = getSubTreeRec(currNode.getLeftChild(prevNode), currNode);
		ParsimonyTree.Node rightSubTree = getSubTreeRec(currNode.getRightChild(prevNode), currNode);
		
		
		currSPTNode.addChild(leftSubTree);
		currSPTNode.addChild(rightSubTree);
		
		leftSubTree.setParent(currSPTNode);
		rightSubTree.setParent(currSPTNode);
		
		
		return currSPTNode;
	}
	
	
	
	
	
	
	
}
