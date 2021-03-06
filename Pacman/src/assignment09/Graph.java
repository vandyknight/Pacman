/**
 * I pledge that the work done here was my own and that I have learned how to write
this program (such that I could throw it out and restart and finish it in a timely
manner).  I am not turning in any work that I cannot understand, describe, or
recreate.  Any sources (e.g., web sites) other than the lecture that I used to
help write the code are cited in my work.  When working with a partner, I have
contributed an equal share and understand all the submitted work.  Further, I have
helped write all the code assigned as pair-programming and reviewed all code that
was written separately.
	                      (Mark Van der Merwe, Andrew Haas)
 */
package assignment09;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * Graph object, links nodes with edges.
 * 
 * @author Mark Van der Merwe and Andrew Haas
 */
public class Graph {

	private ArrayList<Node> vertices;

	private int edges;

	/**
	 * Node object, referring to the vertices of our Graph.
	 * 
	 * @author Mark Van der Merwe and Andrew Haas
	 */
	class Node {

		int id;

		boolean marked = false;
		Node back;
		private LinkedHashSet<Node> adjacent;

		/**
		 * Default constructor.
		 */
		public Node(int id) {
			adjacent = new LinkedHashSet<>();
			this.id = id;
		}

		/**
		 * Returns the Node that points to this one.
		 * 
		 * @return - node that points to this one.
		 */
		public Node getBack() {
			return back;
		}

		/**
		 * Return the set of adjacent Nodes.
		 * 
		 * @return - set of adjacent Nodes.
		 */
		public LinkedHashSet<Node> getAdjacent() {
			return adjacent;
		}

		/**
		 * Add an edge for this Node.
		 * 
		 * @param Node2
		 *            - Node index to connect to.
		 */
		public void addAdjacent(Node Node2) {
			adjacent.add(Node2);
		}

		public String toString() {
			return String.valueOf(id);
		}
	}

	/**
	 * Constructor that defines the size of the graph.
	 * 
	 * @param size
	 *            - number of vertices.
	 */
	public Graph(int size) {
		vertices = new ArrayList<>(size);
	}
	
	/**
	 * Constructor that defines the size of the graph.
	 * 
	 */
	public Graph() {
		vertices = new ArrayList<>();
	}

	public void addVertex(int position) {
		vertices.add(position, new Node(position));
	}

	/**
	 * Adds an edge to our graph.
	 * 
	 * @param Node1
	 *            - position of first node
	 * @param Node2
	 *            - position of second node.
	 */
	public void addEdge(int node1, int node2) {
		vertices.get(node1).addAdjacent(vertices.get(node2));
		vertices.get(node2).addAdjacent(vertices.get(node1));
		edges++;
	}

	/**
	 * Adds multiple edges for a given Node.
	 * 
	 * @param node
	 * @param adjacentNodes
	 */
	public void addEdges(int node, int[] adjacentNodes) {
		for (int adjacentNode : adjacentNodes) {
			addEdge(node, adjacentNode);
		}
	}

	/**
	 * Return all adjacent Nodes to a provided Node.
	 * 
	 * @param Node
	 *            - Node whose adjacents we want to find.
	 * @return - adjacent nodes to provided Node.
	 */
	public LinkedHashSet<Node> getAdjacent(int Node) {
		return vertices.get(Node).getAdjacent();
	}
	
	public Node getVertex(int position) {
		return vertices.get(position);
	}

	/**
	 * Getter for num of edges.
	 * 
	 * @return num of edges.
	 */
	public int numOfEdges() {
		return edges;
	}

	/**
	 * Getter for num of vertices.
	 * 
	 * @return num of vertices.
	 */
	public int numOfVertices() {
		return vertices.size();
	}

	/**
	 * To string for debug purposes. Points each node to its adjacents.
	 */
	public String toString() {
		String toString = "";
		for (int index = 0; index < vertices.size(); index++) {
			toString += index + " -> " + vertices.get(index).getAdjacent().toString() + "\n";
		}
		return toString;
	}

}
