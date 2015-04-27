import java.util.Scanner;
import java.util.Stack;

public class StarterMaze {

    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    public static int Size;

    public static class Point {  // a Point is a position in the maze

        public int x, y;
        public Point parent;

        public boolean visited;   // for DFS
        public boolean used;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object other) {
            return other instanceof Point && x == ((Point) other).x && y == ((Point) other).y;
        }

        public String toString() {
            return "( " + x + " , " + y + ")";
        }
    }

    public static class Edge {
        // an Edge is a link between two Points:
        // For the grid graph, an edge can be represented by a point and a direction.
        Point point;
        int direction;
        boolean used;     // for maze creation
        boolean deleted;  // for maze creation

        public Edge(Point p, int d) {
            this.point = p;
            this.direction = d;
            this.used = false;
            this.deleted = false;
        }
    }

    // A board is an SizexSize array whose values are Points                                                                                                           
    public static Point[][] board;

    // A graph is simply a set of edges: graph[i][d] is the edge 
    // where i is the index for a Point and d is the direction 
    public static Edge[][] graph;
    public static int N;   // number of points in the graph

    public static void main(String[] args) throws InterruptedException {

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("What's the size of your maze? ");
            Size = scan.nextInt();

            Edge dummy = new Edge(new Point(0, 0), RIGHT);
            dummy.used = true;
            dummy.point.visited = true;

            board = new Point[Size][Size];
            N = Size * Size;  // number of points
            graph = new Edge[N][4];

            for (int i = 0; i < Size; ++i) {
                for (int j = 0; j < Size; j++) {
                    Point p = new Point(i, j);
                    int pindex = i * Size + j;   // Point(i, j)'s index is i*Size + j

                    board[i][j] = p;

                    //                          if              then              else
                    graph[pindex][RIGHT] = (j < Size - 1) ? new Edge(p, RIGHT) : dummy;
                    graph[pindex][DOWN] = (i < Size - 1) ? new Edge(p, DOWN) : dummy;
                    graph[pindex][LEFT] = (j > 0) ? graph[pindex - 1][RIGHT] : dummy;
                    graph[pindex][UP] = (i > 0) ? graph[pindex - Size][DOWN] : dummy;

                }
            }

            int numSets = graph.length;
            while (numSets > 1) {
                Edge edge = getRandomUnusedEdge(graph);
                Point u = find(edge.point);
                Point v = find(getOtherPoint(edge, edge.direction));
                if (!u.equals(v)) {
                    union(u, v);
                    graph[edge.point.x * Size + edge.point.y][edge.direction].deleted = true;
                    numSets--;
//                    displayMaze();
//                    Thread.sleep(500);
                } else {
                    graph[edge.point.x * Size + edge.point.y][edge.direction].used = true;
                }
            }


            Point start = board[Size - 1][Size - 1];
            Point end = board[0][0];
            start.parent = null;
            start.used = true;
            Point solution = solveMaze(start, end);

            while (solution.parent != null)
            {
                solution.used = true;
                solution = solution.parent;
            }
            displayMaze();
        }
//        scan.close();
    }

    private static Point solveMaze(Point start, Point end) {
        Stack<Point> points = new Stack<Point>();
        points.push(start);

        while (!points.empty()) {
            Point p = points.pop();
            p.visited = true;
            if (p.x == end.x && p.y == end.y) return p;
            for (int i = 0; i < 4; i++) {
                Edge edge = graph[p.x * Size + p.y][i];
                if (edge.deleted) {
                    Point next = getOtherPoint(edge, i);
                    if (!next.visited) {
                        next.parent = p;
                        points.push(next);
                    }
                }
            }
        }
        return null;
    }

    private static void displayMaze() {
        for (int j = 0; j < Size; j++)
            System.out.print("+---");
        System.out.println("+");
        for (int i = 0; i < Size; ++i) {
            if (i > 0) System.out.print("|");
            else System.out.print(" ");
            for (int j = 0; j < Size; j++) {
                int pindex = i * Size + j;   // Point(i, j)'s index is i*Size + j

                if (board[i][j].used) System.out.print(" @ ");
                else System.out.print("   ");

                if (!graph[pindex][RIGHT].deleted && (j < Size - 1 || i < Size - 1))
                    System.out.print("|");
                else
                    System.out.print(" ");
            }
            System.out.print("\n+");
            for (int j = 0; j < Size; j++) {
                int pindex = i * Size + j;   // Point(i, j)'s index is i*Size + j
                if (!graph[pindex][DOWN].deleted)
                    System.out.print("---+");
                else
                    System.out.print("   +");
            }
            System.out.println();
        }
    }

    private static void union(Point u, Point v) {
        u.parent = v;
    }

    private static Point find(Point i) {
        Point r = i;
        while (r.parent != null) //find root
            r = r.parent;
        if (!r.equals(i)) { //compress path//
            Point k = i.parent;
            while (!k.equals(r)) {
                i.parent = r;
                i = k;
                k = k.parent;
            }
        }
        return r;
    }

    private static Point getOtherPoint(Edge edge, int direction) {
        if (direction == RIGHT)
            return board[edge.point.x][edge.point.y + 1];
        else if (direction == DOWN)
            return board[edge.point.x + 1][edge.point.y];
        else
            return board[edge.point.x][edge.point.y];
    }

    private static Edge getRandomUnusedEdge(Edge[][] graph) {
        Edge result = null;
        int count = 0;
        while (result == null ||
                result.deleted ||
                result.used ||
                (result.point.x == 0 && result.point.y == 0)) {
            result = graph[(int)(Math.random() * graph.length)][(int)(Math.random() * 4)];
            count++;
            if (count > graph.length * 4)
                break;
        }
        if (count > graph.length * 4) {
            for (Edge[] aGraph : graph)
                for (int j = 0; j < 4; j++)
                    if (!aGraph[j].used || !aGraph[j].deleted)
                        return aGraph[j];
        }
        return result;
    }
}