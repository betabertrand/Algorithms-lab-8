package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * 
 * @author kreimendahl
 */

// Provides a solution to the topological sorting problem



public class LAB8 {

	//private static LinkedList<Node> list = new LinkedList<>();
	
	// TODO: document this method
	public static Node[] FindTopo(Graph g) {
		// TODO: implement this method
		Node[] nodeArray = g.GetNodes();

		LinkedList<Node> list = new LinkedList<>();




			DFS(g, list);






		for (Node node : list) {
			System.out.print(node.toString() + " ");
		}


		Node[] arr;
		arr = list.toArray(new Node[list.size()]);

		Node node0 = new Node(0, "file0"), node1 = new Node(1, "file1"), node2 = new Node(2, "file2"), node3 = new Node(3, "file3");

		Node[] arr2 = new Node[] {node0, node1, node2, node3};

		return arr;
	}

	public static void DFS(Graph g, LinkedList<Node> list) {


		for (Node n: g.GetNodes()) {
			n.marker = 0;
		}
		for (Node n: g.GetNodes()) {
			if (n.marker == 0) {
				DFSvisit(g, n, list);

			}

		}



	}
	//n = u and t = v;
	private static void DFSvisit(Graph g, Node n, LinkedList<Node> list) {

		Node[] arr = g.GetEdges(n);

		n.marker = 1;

		for (Node t: arr) {
			if (t.marker == 0) {
				DFSvisit(g, t, list);

			}

		}
		n.marker = 2;
		list.addFirst(n);




	}
	
	
	/************************************************************
	 * A naive implementation of topological sort. This implementation
	 * uses the 'marker' field of a node to maintain a count of the number
	 * of unresolved dependencies. Then there is a double-for loop over
	 * all of the nodes, each time adding a node with 0 unresolved
	 * dependencies to the output list.
	 ************************************************************/
	public static Node[] FindNaive(Graph g) {
		
		int numNodes = g.size();
		int numFinished = 0;
		Node[] ret= new Node[numNodes];
		
		MarkDeps(g);
		while (numFinished < numNodes) {
			for (Node n : g.GetNodes()) {
				if (n.marker == 0) {
					UnmarkDeps(n, g);
					n.marker = -1;
					ret[numFinished] = n;
					numFinished++;
				}
			}
		}
		
		return ret;
	}
	
	// use each node's marker to count how many nodes depend on it
	private static void MarkDeps(Graph g) {
		
		for (Node n: g.GetNodes()) {
			for (Node next: g.GetEdges(n))
				next.marker++;
		}
	}
	
	// reduce dependency count for all dependencies of a specific node
	private static void UnmarkDeps(Node n, Graph g) {
		
		for (Node next: g.GetEdges(n))
			next.marker--;
	}
	
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		System.out.printf("Enter <dependencies file> <algorithm>, ([n]aive, [t]opological sort).\n");
		System.out.printf("(e.g: deps/small n)\n");
		file1 = s.next();

		// read in dependencies
		Graph g = InputGraph(file1);

		String algo = s.next();
		Node[] result = {};

		switch (algo.charAt(0)) {
		case 'n':
			result = FindNaive(g);
			break;
		case 't':
			result = FindTopo(g);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Order of files: ");
		for (int i = 0; i < result.length; i++)
			System.out.println(result[i].toString());
	}


	// reads in the graph from a specific file formatted with lines like:
	// FILENAME 1 2 10
	// This means that the file FILENAME depends on files on lines 1, 2,
	// and 10 of the file to be completed first.
	// This reads through the input file twice, first to get names for the
	// nodes and next to set up edges in the correct direction
	private static Graph InputGraph(String file1) {
		
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				Node n = new Node(i, lineScan.next());
				lineScan.close();
				g.AddNode(n);
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				lineScan.next();	// skip over file name
				while (lineScan.hasNextInt())	// for each dependency
					g.GetNodes()[lineScan.nextInt()].AddEdge(i);
				lineScan.close();
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}

}
