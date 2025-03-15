package ch.heig.gre.groupE.strategy;

import ch.heig.gre.maze.MazeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Wall management strategy that starts with no walls and selectively adds them.
 *
 * Approach: During finalization, identify all non-tree edges and add walls for them.
 * Uses the parent array to efficiently determine which edges are part of the solution path.
 *
 * Time complexity:
 * - processTreeEdge: O(1) - no operation during traversal
 * - finalize: O(V + E) where E is the number of edges
 * Space complexity: O(1) - uses only the existing parent array
 *
 * @author Quentin Surdez
 * @author REDACTED
 */
public class AddWallStrategy implements WallManagementStrategy {
    @Override
    public void processTreeEdge(MazeBuilder builder, int parent, int child) {
        // No action during traversal - we'll add walls in finalization
    }

    @Override
    public void finalize(MazeBuilder builder, int vertices, ArrayList<Integer> parent) {
        // Add walls by examining each potential edge in the grid
        for (int v = 0; v < vertices; v++) {
            // Get all neighbors of this vertex
            List<Integer> neighbors = builder.topology().neighbors(v);

            for (int neighbor : neighbors) {
                // Process each edge only once (when v < neighbor)
                if (v < neighbor) {
                    // Check if this edge is part of the DFS spanning tree
                    // An edge is part of the tree if one vertex is the parent of the other
                    boolean isTreeEdge = (parent.get(v) == neighbor || parent.get(neighbor) == v);

                    // If not a tree edge, add a wall
                    if (!isTreeEdge) {
                        builder.addWall(v, neighbor);
                    }
                }
            }
        }
    }
}
