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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Determines the shortest path given a scenario using Graph path finding,
 * specifically Breadth First Search.
 * 
 * @author Mark Van der Merwe and Andrew Haas
 */
public class PathFinder {

	private static int runCounter;
	private static double[] runData = new double[100];

	static int[][] maze;
	private static int[] dimensions;
	private static BufferedReader mazeReader;
	static Graph mazeGraph;

	static int start;
	static int finish;

	private static double wallDensity;

	public static void main(String[] args) {
		// Below is used in the shell script we wrote -> grabs data from command
		// line call.
		// System.out.println("Writing from " + args[0] + " to " + args[1]);

		// Change the names of the input and output files here to solve
		// different maps.
		solveMaze("mazes/tinyMaze.txt", "mazeSolutions/tinyMazeSolution.txt");

		// The code below is what we used to run the tests on wall density vs.
		// time.
		// Run our test 100 times to time compared to wall density.
		for (int run = 0; run < 100; run++) {
			solveMaze("mazes/map1.txt", "mazeSolutions/map1Solution.txt");
			runCounter++;
		}

		double total = 0;
		// Average time.
		for (int run = 0; run < 100; run++) {
			total += runData[run];
		}
		System.out.println(wallDensity + ", " + (total / 100));

	}

	/**
	 * Solve the pacman maze by turning the provided maze file into a graph,
	 * using BFS to find shortest path, then writing the solution onto an
	 * outputfile.
	 * 
	 * @param inputFileName
	 *            - txt file to read maze from.
	 * @param outputFileName
	 *            - txt file to write solution to.
	 */
	public static void solveMaze(String inputFileName, String outputFileName) {
		// Read the vals into our maze array.
		try {
			readFromFile(inputFileName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		mazeGraph = new Graph();

		// Add all the vertices in our maze into the graph.
		for (int row = 0; row < dimensions[0]; row++) {
			for (int col = 0; col < dimensions[1]; col++) {
				if (maze[row][col] != -1) {
					mazeGraph.addVertex(maze[row][col]);
				}
			}
		}

		// Add the edges.
		for (int row = 1; row < dimensions[0] - 1; row++) {
			for (int col = 1; col < dimensions[1] - 1; col++) {
				if (maze[row][col] != -1) {
					if (maze[row + 1][col] != -1) {
						mazeGraph.addEdge(maze[row][col], maze[row + 1][col]);
					}
					if (maze[row - 1][col] != -1) {
						mazeGraph.addEdge(maze[row][col], maze[row - 1][col]);
					}
					if (maze[row][col + 1] != -1) {
						mazeGraph.addEdge(maze[row][col], maze[row][col + 1]);
					}
					if (maze[row][col - 1] != -1) {
						mazeGraph.addEdge(maze[row][col], maze[row][col - 1]);
					}
				}
			}
		}
		// System.out.println(mazeGraph.toString());

		// Perform breadth first search on our start and finish.
		BreadthFirstSearch search = new BreadthFirstSearch(mazeGraph, start, finish);

		//Time our BFS for empirical analysis.
		long startTime = System.nanoTime();
		Integer[] path = search.breadthFirstSearch();
		long endTime = System.nanoTime();
		// System.out.println(wallDensity + ", " + (endTime - startTime));
		runData[runCounter] = endTime - startTime;

		// System.out.println(Arrays.toString(path));

		writeSolutionToFile(path, outputFileName);
	}

	/**
	 * Reads the values from the provided file into our Maze array. Represents
	 * Xs as -1 and each open space or Start/Goal as its index in the Graph's
	 * array.
	 * 
	 * @param file
	 *            - txt file to grab the maze from.
	 * @throws Exception
	 *             - If the maze doesn't have a rectangular border of Xs, throw
	 *             exception.
	 */
	public static void readFromFile(String file) throws Exception {
		try {
			// Set up our buffered reader with the provided file name.
			mazeReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(0);
		}

		try {
			// Read and determine the dimensions of our maze based on the first
			// line.
			String firstLine = mazeReader.readLine();
			Scanner scanner = new Scanner(firstLine);
			dimensions = new int[2];
			for (int index = 0; scanner.hasNext(); index++) {
				dimensions[index] = scanner.nextInt();
			}

			maze = new int[dimensions[0]][dimensions[1]];
			scanner.close();
		} catch (IOException e) {
			System.out.println("First line not set up correctly.");
			System.exit(0);
		}

		int wallCount = 0;

		try {
			int position = 0;
			for (int row = 0; row < dimensions[0]; row++) {
				for (int col = 0; col < dimensions[1]; col++) {
					char letter = (char) mazeReader.read();

					// If the boundaries are not Xs, the file is illegal and we
					// throw an exception.
					if ((row == 0 || row == dimensions[0] - 1 || col == 0 || col == dimensions[1] - 1) && letter != 'X')
						throw new Exception("Illegal File Format");

					if (letter == ' ') {
						maze[row][col] = position;
						position++;
					} else if (letter == 'X') {
						wallCount++;
						maze[row][col] = -1;
					} else if (letter == 'S') {
						start = position;
						maze[row][col] = position;
						position++;
					} else if (letter == 'G') {
						finish = position;
						maze[row][col] = position;
						position++;
					}
				}
				// Skip new line at the end of each row.
				mazeReader.skip(1);
			}
		} catch (IOException e) {
			System.out.println("Failed to read from file.");
			System.exit(0);
		}

		wallDensity = ((double) wallCount) / (dimensions[0] * dimensions[1]);
	}

	/**
	 * Writes the provided solution path to the provided output file.
	 * 
	 * @param path
	 *            - path from the start to the finish.
	 * @param outputFileName
	 *            - name of the output file.
	 */
	public static void writeSolutionToFile(Integer[] path, String outputFileName) {

		StringBuilder solvedMaze = new StringBuilder();

		// Sort the path so that we can easily add dots by iterating through our
		// array.
		if (path != null) {
			Arrays.sort(path);
		} else {
			path = new Integer[1];
			path[0] = -1;
		}
		
		solvedMaze.append(String.valueOf(dimensions[0]) + " " + String.valueOf(dimensions[1]) + "\n");

		// Index corresponds to the index in the path array.
		int index = 0;

		for (int row = 0; row < dimensions[0]; row++) {
			for (int col = 0; col < dimensions[1]; col++) {
				// Convert each position to what we want it to be in the output
				// file.
				if (maze[row][col] == -1) {
					solvedMaze.append('X');
				} else if (maze[row][col] == start) {
					solvedMaze.append('S');
					if (index < path.length - 1) {
						index++;
					}
				} else if (maze[row][col] == finish) {
					solvedMaze.append('G');
					if (index < path.length - 1) {
						index++;
					}
				} else if (maze[row][col] == path[index]) {
					solvedMaze.append('.');
					if (index < path.length - 1) {
						index++;
					}
				} else {
					solvedMaze.append(' ');
				}
			}
			solvedMaze.append("\n");
		}

		// Write our solution to the actual file.
		try {
			FileWriter txtWriter = new FileWriter(outputFileName);
			txtWriter.write(solvedMaze.toString());
			txtWriter.close();
			System.out.println("Solution written to " + outputFileName);
		} catch (IOException e) {
			// If we can't write, dump the info into the console.
			System.out.println("Unable to write to file. Maze dump:");
			System.out.print(solvedMaze.toString());
		}
	}

}
