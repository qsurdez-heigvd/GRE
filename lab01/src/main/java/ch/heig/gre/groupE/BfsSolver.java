package ch.heig.gre.groupE;

import ch.heig.gre.graph.Graph;
import ch.heig.gre.graph.VertexLabelling;
import ch.heig.gre.maze.MazeSolver;
import ch.heig.gre.maze.Progression;

import java.util.*;

/**
 * Implements a maze solver using breadth-first search algorithm.
 * Finds the shortest path between two points in a maze.
 *
 * Overall complexity:
 * - Time: O(V + E) where V is the number of vertices and E is the number of edges
 *   - BFS traversal: O(V + E)
 *   - Path reconstruction: O(V) in worst case
 * - Space: O(V) auxiliary space
 *   - Queue: O(V) in worst case (level with maximum vertices)
 *   - Visited array: O(V)
 *   - Predecessors array: O(V)
 *
 * @author Quentin Surdez
 * @author REDACTED
 */
public final class BfsSolver implements MazeSolver {

  /**
   * Solves a maze represented as a graph using breadth-first search algorithm.
   * Finds the shortest path from source to destination vertex if one exists.
   *
   * @param graph The maze represented as a graph
   * @param source The starting vertex for path finding
   * @param destination The target vertex for path finding
   * @param treatments A vertex labelling interface to track progression
   * @return A list of vertices representing the shortest path from source to destination,
   *         or an empty list if no path exists
   *
   * Time complexity: O(V + E)
   * - BFS traversal: O(V + E) where each vertex and edge is visited at most once
   * - Path reconstruction: O(V) in worst case (path traverses entire maze)
   *
   * Space complexity: O(V)
   * - Queue: O(V) in worst case (level with maximum vertices)
   * - Visited array: O(V)
   * - Predecessors array: O(V)
   */
  @Override
  public List<Integer> solve(Graph graph, int source, int destination, VertexLabelling<Integer> treatments) {
    int vertices = graph.nbVertices();

    // Using ArrayDeque as the queue for BFS
    // ArrayDeque is preferred over LinkedList as it offers better performance
    // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayDeque.html
    // Initialized to the number of vertices in the graph
    ArrayDeque<Integer> bfs = new ArrayDeque<>(vertices);

    // Track visited vertices - Space complexity: O(V)
    // An ArrayList is used as the get and set operations are O(1) https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html#set(int,E)
    // Space complexity might be worse than a basic array or a BitSet but I wanted something familiar to work with
    // If space complexity was very important, then a BitSet or a basic array would have been used
    // Initialized to the number of vertices in the graph
    ArrayList<Boolean> visited = new ArrayList<>(Collections.nCopies(vertices, false));

    // Store predecessors to reconstruct the path - Space complexity: O(V)
    // Initialized to -1 (no predecessor)
    // ArrayList is used for the same reasons as above
    ArrayList<Integer> predecessors = new ArrayList<>(Collections.nCopies(vertices, -1));

    // Counter for vertex visualization
    int numberOfVerticesTreated = 0;

    // Initialize BFS - O(1)
    bfs.add(source);
    visited.set(source, true);

    // Main BFS loop - O(V + E) time total across all iterations
    while (!bfs.isEmpty()) {

      // Get next vertex from queue - O(1)
      int current = bfs.poll();

      // Set label with the depth of the BFS
      treatments.setLabel(current, numberOfVerticesTreated++);

      // If destination reached, terminate early
      if (current == destination) {
        break;
      }

      // Get all neighbors
      List<Integer> adjacents = graph.neighbors(current);

      // Process each neighbor
      for (int adjacent : adjacents) {
        if (!visited.get(adjacent)) {

          // Mark neighbour as visited - O(1) according to the docs https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html#set(int,E)
          visited.set(adjacent, true);

          // Store predecessor for path reconstruction - O(1)
          predecessors.set(adjacent, current);

          // Add to the stack - O(1) amortized according to the docs https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayDeque.html
          bfs.add(adjacent);
        }
      }
    }

    // If destination was never reached, return empty path
    if (!visited.get(destination)) {
      return Collections.emptyList();
    }

    // Reconstruct path from destination to source - O(V) in worst case
    ArrayList<Integer> path = new ArrayList<>();
    for (int at = destination; at != -1; at = predecessors.get(at)) {
      path.add(at);
    }

    // Reverse to get path from source to destination - O(V)
    Collections.reverse(path);
    return path;
  }
}
