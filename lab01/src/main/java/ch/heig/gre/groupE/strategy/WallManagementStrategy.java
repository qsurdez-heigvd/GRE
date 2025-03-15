package ch.heig.gre.groupE.strategy;

import ch.heig.gre.maze.MazeBuilder;

import java.util.ArrayList;


/**
 * Defines the interface for wall management strategies used during maze generation.
 * The Strategy pattern allows us to encapsulate different wall management approaches
 * while keeping the core DFS algorithm intact.
 *
 * While not perfect, ot allows us to reduce code duplication. Two distinct functions
 * could have done the job, but we wanted to spice things up a bit by using some fun pattern.
 * And if there is another way we want to expand this wall management, it would be easy.
 * Don't know what other way but eh the hypothesis is a good argument for this ^^
 *
 * And let's be honest I thought the implementation of the strategy pattern would look better
 * than it does now and well too bad won't reimplement it with another pattern.
 *
 * @author REDACTED
 * @author Quentin Surdez
 */
public interface WallManagementStrategy {
    /**
     * Processes a tree edge in the DFS spanning tree.
     * Called when a new vertex is discovered during traversal.
     *
     * @param builder The maze builder
     * @param parent The parent vertex in the DFS tree
     * @param child The newly discovered child vertex
     *
     * Time complexity: O(1) for both strategy implementations
     */
    void processTreeEdge(MazeBuilder builder, int parent, int child);

    /**
     * Finalizes the maze after DFS traversal is complete.
     * Handles any remaining wall operations needed.
     *
     * @param builder The maze builder
     * @param vertices Total number of vertices in the graph
     * @param parent Array containing parent relationships in the DFS tree
     *
     * Time complexity:
     * - RemoveWallsStrategy: O(1)
     * - AddWallsStrategy: O(V + E) where E is the number of edges
     */
    void finalize(MazeBuilder builder, int vertices, ArrayList<Integer> parent);

}