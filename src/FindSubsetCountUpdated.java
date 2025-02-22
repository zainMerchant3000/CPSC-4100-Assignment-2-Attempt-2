import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FindSubsetCountUpdated {
    static int[][] psum;
    static int[] ss, qs;
    static int N;

    static int go(int x1, int y1, int x2, int y2) {
        int sum = 1;
        for (int x3 = x1 + 1; x3 < N; x3++) {
            int y1_ = Math.min(y1, ss[x3]);
            int y3 = Math.max(y2, ss[x3]);
            if (getsum(x1, y1_, x3, y3) - getsum(x1, y1, x2, y2) == 1)
                sum += go(x1, y1_, x3, y3);
        }
        return sum;
    }

    static int getsum(int x1, int y1, int x2, int y2) {
        return psum[x2 + 1][y2 + 1] - psum[x2 + 1][y1] - psum[x1][y2 + 1] + psum[x1][y1];
    }

    static int solve(int[][] points) {
        N = points.length;
        // create preSum matrix
        psum = new int[N + 1][N + 1];
        ss = new int[N];
        // O(N^2) time
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                //
                if (points[i - 1][j - 1] > 0) ss[i - 1] = j - 1;
                psum[i][j] = points[i - 1][j - 1] - psum[i - 1][j] - psum[i][j - 1] + psum[i - 1][j - 1];
            }
        }
        int sum = 1 + N;
        for (int pr = 0; pr < N-1; pr++) {
            for (int qr = pr + 1; qr < N; qr++) {
                int pc = Math.min(ss[pr], ss[qr]);
                int qc = Math.max(qs[pr], qs[qr]);
            }
        }

        return sum;
    }

    // returns: [rank] -> [value in ps]
    static HashMap<Integer, Integer> compressx(int[][] ps, int coord) {
        for (int i = 0; i < N; i++)
            // ps[i][0] -> retrieving x coordinate of each point
            // ps[i][1] -> retrieving y coordinate of each point
            // ex) ps[0][0] = 0, ps[1][0] = 2 ... etc)
            qs[i] = ps[i][coord];

            System.out.println("qs[i]: " + Arrays.toString(qs));
        // sort the x and y coordinates
        Arrays.sort(qs);
        System.out.println(" Sorted qs[i]: " + Arrays.toString(qs));
        // create mapping of coordinates
        // ex) m = {0: 0, 1:1}
        var m = new HashMap<Integer, Integer>();
        for (int i = 0; i < N; i++) {
            // (populate map with sorted x and y coordinates)
            Integer previousValue =  m.put(qs[i], i);
            System.out.println("Inserted (" + qs[i] + ", " + i + "), previous value: " + previousValue);
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
        System.out.println("xs:");
        for (Map.Entry<Integer, Integer> entry : xs.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // Print ys
        System.out.println("ys:");
        for (Map.Entry<Integer, Integer> entry : ys.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        for (int i = 0; i < N; i++) {
            // iterate and update each point to compressed x-y values
            Integer xValue = xs.get(points[i][0]);
            Integer yValue = ys.get(points[i][1]);
            System.out.println("xValue: " + xValue + ", yValue: " + yValue);


           // points[i][0] = xs.get(points[i][1]);
          //  points[i][1] = ys.get(points[i][0]);
        }
    }

    public static void main(String[] args) throws IOException {
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
        System.out.println(tokens[0]);
        // tokens[0] = "5"
        // parsing token (line) as Integer
        // ex) 5 -> tells us number of points
        N = Integer.parseInt(tokens[0]);
        // create 2D array Points
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
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(points[i][j] + " ");
            }
            System.out.println();
        }
        compress(points);
        System.out.println(solve(points));
    }
}