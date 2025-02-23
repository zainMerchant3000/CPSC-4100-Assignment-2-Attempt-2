import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FindSubsetCountUpdated {
    static long[][] psum;
    static int[] ss, qs;
    static int N;



    static long getsum(int x1, int y1, int x2, int y2) {
        // Make sure to stay within bounds when accessing the psum array
        if (x1 < 0 || y1 < 0 || x2 >= N || y2 >= N) {
            throw new IllegalArgumentException("Indices are out of bounds for the psum array.");
        }

        return psum[x2 + 1][y2 + 1] - psum[x2 + 1][y1] - psum[x1][y2 + 1] + psum[x1][y1];
    }

    static long solve(int[][] markedMatrix) {
        N = markedMatrix.length;
        // create preSum points
        psum = new long[N + 1][N + 1];
        //
        ss = new int[N];
        // O(N^2) time
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                // if (markedMatrix[i - 1][j - 1] > 0) -> marked point
                //
                if (markedMatrix[i - 1][j - 1] > 0) {
                    // ss[i-1] = j - 1; -> storing column indices of marked points
                    // ex) psum[1][4] = markedMatrix[0,3]
                    // ss[0] = 3
                    ss[i - 1] = j - 1;
                    psum[i][j] = markedMatrix[i - 1][j - 1] + psum[i - 1][j] + psum[i][j - 1] - psum[i - 1][j - 1];
                }
                psum[i][j] = markedMatrix[i - 1][j - 1] + psum[i - 1][j] + psum[i][j - 1] - psum[i - 1][j - 1];
            }
        }

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
               // System.out.print(psum[i][j] + " ");
            }
           // System.out.println();
        }
        for (int i = 0; i < N; i++) {
          //  System.out.print("ss[i]: " + ss[i] + " ");

        }
      //  System.out.println();

        for (int i = 0; i < N; i++) {
          //  System.out.print("qs[i]: " + qs[i] + " ");
        }
       // System.out.println();




        // Base case: #of subsets = N (number of singleton sets) + 1 empty set
        long sum = 1 + N;
        // iterate over pairs of points
        // p should be above q (is satisfied by min and max)
        // p will be lower row p < q (means that p is 'above q')
        // any pair of 'points' <= p (which means point above or that includes p will not be
        // valid
        for (int pr = 0; pr < N-1; pr++) {
            for (int qr = pr + 1; qr < N; qr++) {
                // Finding min column index between 2 points
                int pc = Math.min(ss[pr], ss[qr]);
              //  System.out.println("min: for  " + pc);
                // Finding max column index between the 2 points
                int qc = Math.max(qs[pr], qs[qr]);
              //  System.out.println("max: " + qc);

                // Calculate the product of the regions using the getSum function
                sum += handlePair(pr,pc,qr,qc);
            }

            //
        }

        /// TODO: compute all the points above <= pc
        /// TODO:
        ///  Haskell code translation:
        /// (flip ((+) .handlepair)) (1 + sz) pairs
            // fetch a pair from pairs and add it to the total of (1+sz) pairs
        ///  sz = N
        ///  low
        ///  high
        /// up
        ///  down
        /// pc = p
        ///  qc = q
        ///  if it contains p's column the rectangle must expand to the column of p
        return sum;
    }

    private static long handlePair(int pr, int pc, int qr, int qc) {
        if (pc < 0 || qc < 0 || pr < 0 || qr < 0 || pc >= N || qc >= N || pr >= N || qr >= N) {
            throw new IllegalArgumentException("Invalid coordinates in handlePair");
        }
        // base case:
        // recursive case:
        long up = getsum(0, pc, pr, qc);  // Calculate the 'up' region product
        long down = getsum(qr, pc, N - 1, qc);  // Calculate the 'down' region product
        return up * down;  // Return the product of the two regions
    }

    // returns: [rank] -> [value in ps]
    static HashMap<Integer, Integer> compressx(int[][] ps, int coord) {
        for (int i = 0; i < N; i++)
            // ps[i][0] -> retrieving x coordinate of each point
            // ps[i][1] -> retrieving y coordinate of each point
            // ex) ps[0][0] = 0, ps[1][0] = 2 ... etc)
            qs[i] = ps[i][coord];

           // System.out.println("qs[i]: " + Arrays.toString(qs));
        // sort the x and y coordinates
        Arrays.sort(qs);
       // System.out.println(" Sorted qs[i]: " + Arrays.toString(qs));
        // create mapping of coordinates
        // ex) m = {0: 0, 1:1}
        var m = new HashMap<Integer, Integer>();
        for (int i = 0; i < N; i++) {
            // (populate map with sorted x and y coordinates)
            Integer previousValue =  m.put(qs[i], i);
           // System.out.println("Inserted (" + qs[i] + ", " + i + "), previous value: " + previousValue);
        }
        return m;
    }

    // .[0] -> the scalar; .[1] -> order as found in original order
    static void compress(int[][] points) {
        qs = new int[N];
        // compress both x and y coordinates
        var xs = compressx(points, 0);
        var ys = compressx(points, 1);
        // Print xs
        /*
        System.out.println("xs:");
        for (Map.Entry<Integer, Integer> entry : xs.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        */


        // Print ys
        /*
        System.out.println("ys:");
        for (Map.Entry<Integer, Integer> entry : ys.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        */

        for (int i = 0; i < N; i++) {
            // iterate and update each point to compressed x-y values
            // i = 0
            // xs.get(points[i][0]) -> look up x-coordinate in map to find value
            // xs.get(points[0][i]) -> look up y-coordinate in map to find value
            Integer xValue = xs.get(points[i][0]);
            Integer yValue = ys.get(points[i][1]);
           // System.out.println("xValue: " + xValue + ", yValue: " + yValue);
            points[i][0] = xValue;
            points[i][1] = yValue;
        }

    }

    public static void main(String[] args) throws IOException {
        // Start Measuring Time:
        long startTime = System.currentTimeMillis();
        var BufferedReader = new BufferedReader(new FileReader(args[0]));
        // create StringBuilder object to build string
        var build = new StringBuilder();
        // store each line in file
        var line = "";
        //reading each line from file
        while ((line = BufferedReader.readLine()) != null) {
            //appending lines
            build.append(line).append(" ");
        }
        BufferedReader.close();
        //storing input as string of tokens
        // ex ["5", "0", "3", "2","2","1","1","3","0","4","4"]
        var tokens = build.toString().split(" ");
       // System.out.println(tokens[0]);
        // tokens[0] = "5"
        // parsing token (line) as Integer
        // ex) 5 -> tells us number of points
        N = Integer.parseInt(tokens[0]);
        // create 2D array points
        var points = new int[N][N];
        // k = 1 -> start at line 1 to process each pair of points
        // Integer.parseInt(tokens[k]) -> provides us value of points in given line
        for (int i = 0, k = 1; i < N; i++) {
            for (int j = 0; j < 2; j++, k++) {
                // points[0][0] = 0
                // points[0][1] = 3
                points[i][j] = Integer.parseInt(tokens[k]);
            }
        }

        // compress the points
        compress(points);

        // create the matrix:
        int[][] markedMatrix = createMarkedMatrix(points);
        // Print the marked matrix
       // System.out.println("marked matrix");
       // printMatrix(markedMatrix);
       // System.out.println("prefix sum: ");
        long result = solve(markedMatrix);
        System.out.println("result: " + result);
       // System.out.println(solve(markedMatrix));
        // End measuring time
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        // Output the time taken
        System.out.println("Time taken: " + totalTime + " milliseconds");
    }

     static int[][] createMarkedMatrix(int[][] points) {
         // Initialize the matrix with zeros
         int[][] matrix = new int[N][N];
         for (int i = 0; i < N; i++) {
             Arrays.fill(matrix[i], 0);
         }

         // Mark the points in the matrix
         for (int i = 0; i < points.length; i++) {
             int x = points[i][0];
             int y = points[i][1];
             matrix[x][y] = 1;
         }
         return matrix;
     }

    static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }


}
