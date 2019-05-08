/**
 * Public Transit
 * Author: Sihang Li and Carolyn Yao
 * Does this compile? Yes
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the
 * solutions from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

	/**
	 * The algorithm that could solve for shortest travel time from a station S to a
	 * station T given various tables of information about each edge (u,v)
	 *
	 * @param S
	 *            the s th vertex/station in the transit map, start From
	 * @param T
	 *            the t th vertex/station in the transit map, end at
	 * @param startTime
	 *            the start time in terms of number of minutes from 5:30am
	 * @param lengths
	 *            lengths[u][v] The time it takes for a train to get between two
	 *            adjacent stations u and v
	 * @param first
	 *            first[u][v] The time of the first train that stops at u on its way
	 *            to v, int in minutes from 5:30am
	 * @param freq
	 *            freq[u][v] How frequently is the train that stops at u on its way
	 *            to v
	 * @return shortest travel time between S and T
	 */
	public int myShortestTravelTime(int S, int T, int startTime, int[][] lengths, int[][] first, int[][] freq) {
		
		// Your code along with comments here. Feel free to borrow code from any
		// of the existing method. You can also make new helper methods.
		int path[] = shortestTime(lengths, S, T); // finds the shortest path from starting station to target station
		int tempTarget = T;//We start from the target station and travel back to the starting station to find the shortest path. 		
		int shortestTime = 0; // keeps track of the shortest time it takes to get from starting station to	 target station
		int runningTime; // The running time for the train to travel to that station
		int stationNumber;
		int nextStation;
		int trainTime;
		int stationCount;
		int currentTime = startTime; 
		for (int i = tempTarget; i >= 0; i--) {
				stationNumber = path[tempTarget]; // current station number 
				nextStation = path[tempTarget - 1]; // next station number
				trainTime = first[stationNumber][nextStation];// the time of the first train
				stationCount = 0; 
				while (trainTime < currentTime) {
					trainTime = first[stationNumber][nextStation] + (stationCount * freq[stationNumber][nextStation]); 
					stationCount++;
				}
			runningTime=trainTime + lengths[stationNumber][nextStation];
			shortestTime  += (runningTime - currentTime);
			currentTime = runningTime;
		}
		return shortestTime;
	}

	/**
	 * A modified Dijkstra’s shortest Path algorithm that finds the shortest path between S and T
	 * 
	 * @param graph
	 * @param source
	 * @param target
	 * @return path
	 */
	public int[] shortestTime(int[][] graph, int source, int target) {
		int numVertices = graph[0].length;
		// This is the array where we'll store all the final shortest times
		int[] times = new int[numVertices]; // This array stores the shortest path from starting station source, to target station T
		int[] prev = new int[numVertices]; //This array stores the stations that have been visited. 
		prev[source] = -1;// Sets the previous station of source to -1 since it doesn't exist. 

		// processed[i] will be true if vertex i's shortest time is already finalized
		Boolean[] processed = new Boolean[numVertices];

		// Initialize all distances as INFINITE and processed[] as false
		for (int v = 0; v < numVertices; v++) {
			times[v] = Integer.MAX_VALUE;
			processed[v] = false;
		}

		times[source] = 0; //Time it takes to travel from source to itself is 0. 

		for (int count = 0; count < numVertices - 1; count++) { 		// Find shortest path to all the vertices
			// Pick the minimum distance vertex from the set of vertices not yet processed.
			// u is always equal to source in first iteration.
			// Mark u as processed.
			int u = findNextToProcess(times, processed);
			processed[u] = true;

			// Update time value of all the adjacent vertices of the picked vertex.
			for (int v = 0; v < numVertices; v++) {
				// Update time[v] only if is not processed yet, there is an edge from u to v,
				// and total weight of path from source to v through u is smaller than current
				// value of time[v]
				if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE && times[u] + graph[u][v] < times[v]) {
					times[v] = times[u] + graph[u][v];
					prev[v] = u; //Records u as the previous station of v. 
				}
			}
		}//borrowed from shortestTime 

		//Finds the shortest path time between T and source. 
		int i = 0;  
		int currentIndex = target; 
		while (currentIndex!=source) { //While the source station is not reached...
			times[i++] = currentIndex;
			currentIndex = prev[currentIndex]; //Backtracks from target to the source. 
		}
		times[i] = currentIndex;// adds the source station
		return times;

	}

	/**
	 * Finds the vertex with the minimum time from the source that has not been
	 * processed yet.
	 * 
	 * @param times
	 *            The shortest times from the source
	 * @param processed
	 *            boolean array tells you which vertices have been fully processed
	 * @return the index of the vertex that is next vertex to process
	 */
	public int findNextToProcess(int[] times, Boolean[] processed) {
		int min = Integer.MAX_VALUE;
		int minIndex = -1;

		for (int i = 0; i < times.length; i++) {
			if (processed[i] == false && times[i] <= min) {
				min = times[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	public void printShortestTimes(int times[]) {
		System.out.println("Vertex Distances (time) from Source");
		for (int i = 0; i < times.length; i++)
			System.out.println(i + ": " + times[i] + " minutes");
	}

	/**
	 * Given an adjacency matrix of a graph, implements
	 * 
	 * @param graph
	 *            The connected, directed graph in an adjacency matrix where if
	 *            graph[i][j] != 0 there is an edge with the weight graph[i][j]
	 * @param source
	 *            The starting vertex
	 */
	public void shortestTime(int graph[][], int source) {
		int numVertices = graph[0].length;

		// This is the array where we'll store all the final shortest times
		int[] times = new int[numVertices];

		// processed[i] will be true if vertex i's shortest time is already finalized
		Boolean[] processed = new Boolean[numVertices];

		// Initialize all distances as INFINITE and processed[] as false
		for (int v = 0; v < numVertices; v++) {
			times[v] = Integer.MAX_VALUE;
			processed[v] = false;
		}

		// Distance of source vertex from itself is always 0
		times[source] = 0;

		// Find shortest path to all the vertices
		for (int count = 0; count < numVertices - 1; count++) {
			// Pick the minimum distance vertex from the set of vertices not yet processed.
			// u is always equal to source in first iteration.
			// Mark u as processed.
			int u = findNextToProcess(times, processed);
			processed[u] = true;

			// Update time value of all the adjacent vertices of the picked vertex.
			for (int v = 0; v < numVertices; v++) {
				// Update time[v] only if is not processed yet, there is an edge from u to v,
				// and total weight of path from source to v through u is smaller than current
				// value of time[v]
				if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE
						&& times[u] + graph[u][v] < times[v]) {
					times[v] = times[u] + graph[u][v];
				}
			}
		}

		printShortestTimes(times);
	}

	public static void main(String[] args) {
		/* length(e) */
		int lengthTimeGraph[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
				{ 0, 8, 0, 7, 0, 4, 0, 0, 2 }, { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
				{ 0, 0, 4, 14, 10, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
				{ 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
		FastestRoutePublicTransit t = new FastestRoutePublicTransit();
		t.shortestTime(lengthTimeGraph, 0);

		// You can create a test case for your implemented method for extra credit below
		int first[][] = new int[][] { { 0, 2, 0, 7, 0, 4, 0, 5, 0 }, { 20, 0, 19, 0, 3, 14, 0, 5, 0 },
				{ 0, 7, 0, 13, 0, 0, 6, 0, 0 }, { 33, 0, 21, 0, 19, 0, 0, 8, 0 }, { 0, 0, 0, 29, 0, 31, 0, 15, 0 },
				{ 7, 13, 0, 0, 43, 0, 22, 0, 0 }, { 0, 0, 8, 0, 0, 1, 0, 12, 0 }, { 0, 31, 0, 12, 0, 0, 13, 0, 0 },
				{ 5, 0, 0, 0, 0, 0, 0, 8, 0 } };

		int freq[][] = new int[][] { { 0, 8, 0, 0, 0, 0, 0, 3, 0 }, { 1, 0, 4, 0, 0, 0, 0, 21, 0 },
			{ 0, 12, 0, 7, 0, 4, 0, 0, 2 }, { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
			{ 0, 0, 4, 14, 10, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
			{ 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
		int length[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
			{ 0, 8, 0, 7, 0, 4, 0, 0, 2 }, { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
			{ 0, 0, 4, 14, 10, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
			{ 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
		int shortestTime;
		int startStation=6;
		int targetstation=2;
		shortestTime = t.myShortestTravelTime(startStation, targetstation, 30, length, first, freq); 
		System.out.println(
				"The shortest travel time between station "+startStation+" and station " +targetstation +" is: " + shortestTime + " minutes.");

	}
}
