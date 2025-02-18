import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

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
        psum = new int[N + 1][N + 1];
        ss = new int[N];
        // O(N^2) time
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (points[i - 1][j - 1] > 0) ss[i - 1] = j - 1;
                psum[i][j] = points[i - 1][j - 1] - psum[i - 1][j] - psum[i][j - 1] + psum[i - 1][j - 1];
            }
        }
        int sum = 1;
        for (int x1 = 0; x1 < N; x1++)
            sum += go(x1, ss[x1], x1, ss[x1]);
        return sum;
    }

    // returns: [rank] -> [value in ps]
    static HashMap<Integer, Integer> compressx(int[][] ps, int coord) {
        for (int i = 0; i < N; i++)
            qs[i] = ps[i][coord];
        Arrays.sort(qs);
        var m = new HashMap<Integer, Integer>();
        for (int i = 0; i < N; i++)
            m.put(qs[i], i);
        return m;
    }

    // .[0] -> the scalar; .[1] -> order as found in original order
    static void compress(int[][] points) {
        qs = new int[N];
        var xs = compressx(points, 0);
        var ys = compressx(points, 1);
        for (int i = 0; i < N; i++) {
            points[i][0] = xs.get(points[i][1]);
            points[i][1] = ys.get(points[i][0]);
        }
    }

    public static void main(String[] args) throws IOException {
        var bufread = new BufferedReader(new FileReader(args[0]));
        var build = new StringBuilder();
        var line = "";
        while ((line = bufread.readLine()) != null) {
            build.append(line).append(" ");
        }
        bufread.close();
        var tokens = build.toString().split(" ");
        N = Integer.parseInt(tokens[0]);
        var points = new int[N][N];
        for (int i = 0, k = 1; i < N; i++) {
            for (int j = 0; j < 2; j++, k++) {
                points[i][j] = Integer.parseInt(tokens[k]);
            }
        }
        compress(points);
        System.out.println(solve(points));
    }
}