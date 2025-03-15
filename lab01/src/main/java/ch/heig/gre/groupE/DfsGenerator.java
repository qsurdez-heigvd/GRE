package ch.heig.gre.groupE;

import ch.heig.gre.groupE.strategy.AddWallStrategy;
import ch.heig.gre.groupE.strategy.RemoveWallStrategy;
import ch.heig.gre.groupE.strategy.WallManagementStrategy;
import ch.heig.gre.maze.MazeBuilder;
import ch.heig.gre.maze.MazeGenerator;
import ch.heig.gre.maze.Progression;

import java.util.*;

/**
 * Implements a maze generator using incremental Depth-First Search with the Strategy pattern
 * for wall management. Creates perfect mazes (exactly one path between any two points)
 * with randomized paths.
 *
 * Overall complexity:
 * - Time: O(V + E) where V is the number of vertices and E is the number of edges
 *   - DFS traversal: O(V + E)
 *   - Wall management: O(V + E) in worst case
 * - Space: O(V) auxiliary space regardless of maze density
 *   - Stack: O(V) in worst case (degenerate graph)
 *   - Visited array: O(V)
 *   - Parent array: O(V)
 *
 * @author Quentin Surdez
 * @author REDACTED
 */
public final class DfsGenerator implements MazeGenerator {

  /**
   * Generates a maze using incremental depth-first search with the appropriate
   * wall management strategy based on requireWalls().
   *
   * @param builder The maze builder interface
   * @param from The starting vertex for maze generation
   *
   * Time complexity: O(V + E)
   * - DFS traversal: O(V + E)
   * - Wall management:
   *   - When requireWalls() is true: O(V + E) - walls removed during traversal
   *   - When requireWalls() is false: O(V + E) - walls added during finalization
   *
   * Space complexity: O(V)
   * - Stack: O(V) in worst case
   * - Visited array: O(V)
   * - Parent array: O(V)
   */
  @Override
  public void generate(MazeBuilder builder, int from) {
    int vertices = builder.topology().nbVertices();

    // Select strategy based on whether we start with all walls
    WallManagementStrategy strategy = requireWalls() ? new RemoveWallStrategy() : new AddWallStrategy();

    // Stack for DFS - Space complexity: O(V) in worst case
    // ArrayDeque is used as Stack is not recommended https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Stack.html
    ArrayDeque<Integer> dfs = new ArrayDeque<>(vertices);

    // Track visited vertices - Space complexity: O(V)
    // An ArrayList is used as the get and set operations are O(1) https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html#set(int,E)
    // Space complexity might be worse than a basic array or a BitSet but I wanted something familiar to work with
    // If space complexity was very important, then a BitSet or a basic array would have been used
    // Initialized to the number of vertices in the graph
    ArrayList<Boolean> visited = new ArrayList<>(Collections.nCopies(vertices, false));

    // Parent vertices of the DFS Tree
    // ArrayList used for the same reasons as above
    // Initialized to the number of vertices in the graph
    ArrayList<Integer> parents = new ArrayList<>(Collections.nCopies(vertices, -1));

    // Initialize DFS - O(1)
    dfs.push(from);
    visited.set(from, true);
    builder.progressions().setLabel(from, Progression.PROCESSING);

    // Main DFS loop - O(V + E) time total across all iterations
    while (!dfs.isEmpty()) {

      // Peek to get the vertex on top of the stack, we remove vertices when backtracking
      int current = dfs.peek();

      // Get all neighbors with utility function
      List<Integer> adjacents = builder.topology().neighbors(current);

      // Randomize exploration order
      Collections.shuffle(adjacents);

      // Try to find an unvisited neighbor
      boolean foundUnvisitedNeighbor = false;
      for (int adjacent : adjacents) {
        if (!visited.get(adjacent)) {

          // Mark as processing for visualization
          builder.progressions().setLabel(adjacent, Progression.PROCESSING);

          // Mark neighbour as visited - O(1) according to the docs https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html#set(int,E)
          visited.set(adjacent, true);

          // Add to the stack for exploration - O(1) amortized according to the docs https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayDeque.html
          dfs.push(adjacent);

          // Mark parent relationship - O(1)
          parents.set(adjacent, current);

          // Process tree edge according to strategy
          strategy.processTreeEdge(builder, current, adjacent);

          foundUnvisitedNeighbor = true;
          // Process only one neighbor per iteration
          break;
        }
      }
      if (!foundUnvisitedNeighbor) {
        builder.progressions().setLabel(current, Progression.PROCESSED);
        dfs.pop();
      }
    }

    // Finalize the structure of the DFS
    strategy.finalize(builder, vertices, parents);
  }

  @Override
  public boolean requireWalls() {
    // return false
    return true;
  }
}
