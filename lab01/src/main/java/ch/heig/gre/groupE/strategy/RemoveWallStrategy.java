package ch.heig.gre.groupE.strategy;

import ch.heig.gre.maze.MazeBuilder;

import java.util.ArrayList;

/**
 * Wall management strategy that starts with all walls present and selectively removes them.
 *
 * Approach: Remove walls along edges of the DFS spanning tree during traversal.
 *
 * Time complexity:
 * - processTreeEdge: O(1) per edge
 * - finalize: O(1) - no additional work needed
 * Space complexity: O(1) - no additional storage beyond method parameters
 *
 * @author Quentin Surdez
 * @author REDACTED
 */
public class RemoveWallStrategy implements WallManagementStrategy {
    @Override
    public void processTreeEdge(MazeBuilder builder, int parent, int child) {
        // Remove the wall for this tree edge to create a passage in the maze
        builder.removeWall(parent, child);
    }

    @Override
    public void finalize(MazeBuilder builder, int vertices, ArrayList<Integer> parent) {
        // No finalization needed - walls already removed during traversal
    }
}
