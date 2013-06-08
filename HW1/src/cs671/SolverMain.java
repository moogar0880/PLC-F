package cs671.solve;

import java.awt.Point;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

class SolverMain{
    private final Solver<?> solver;
    private final PrintWriter out;

    /** Builds a new user interface for the given solver. Results are
     * displayed on <code>output</code>.
     */
    public SolverMain(Solver<?> s, Writer output) {
        solver = s;
        out = new PrintWriter(output, true);
    }

    /** Builds a new user interface for the given solver. Results are
     * displayed on <code>System.out</code>.
     */
    public SolverMain(Solver<?> s) {
        this(s,
             new java.io.OutputStreamWriter(System.out));
    }
    
    /** Begins the solving process with the current solver and
     * world. Prints results on <code>out</code>.
     */
    public void solve(){
        solver.initialize();
        while(!solver.hasSolved()){
            solver.nextMove();
        }
        
        System.out.println(solver.getSolution().pathString());
    }
    
    // This method should be modified if the bonus question is implemented
    private static void usage(){
        System.err.println("java solve.SolverMain -[domain] -[algorithm]");
        System.err.println("\tDomains: Maze (-maze), Sliding Tile (-tile)");
        System.err.println("\tAlgorithms: depth-first (-df), breadth-first (-bf)");
        System.exit(-1);
    }

    /** Creates a new "maze" world object, using the input from
     * <code>fileName</code>.
     *
     * @param fileName for problem input
     * @throws java.io.IOException if the file cannot be opened.
     */
    public static MazeWorld makeMaze(String fileName) 
        throws java.io.IOException{
        MazeWorld w = null;
        
        try{
            Scanner scan = new Scanner(new java.io.File(fileName));

            Point startLoc=null, goalLoc=null;
            ArrayList<Point> blocked = new ArrayList<Point>();
            ArrayList<String> lines = new ArrayList<String>();
            while(scan.hasNext()){
                lines.add(scan.nextLine());
            }
            int x=0, y=lines.size()-1;
            for(String l: lines){
                x = l.length();
                for(x=0; x < l.length(); x++){
                    switch(l.charAt(x)){
                    case '@':
                        startLoc = new Point(x, y);
                        break;
                    case 'G':
                        goalLoc = new Point(x, y);
                        break;
                    case 'X':
                        blocked.add(new Point(x, y));
                        break;
                    }
                }
                y--;
            }
            w = new MazeWorld(startLoc, goalLoc, 
                              blocked.toArray(new Point[blocked.size()]), 
                              x-1, lines.size()-1);
        }
        catch(java.io.IOException ex){
            System.err.printf("Cannot open file %s%n", fileName);
        }

        return w;
    }

    /** Creates a new "sliding tile" world object, using the input
     * from <code>fileName</code>.
     *
     * @param fileName for problem input
     * @throws java.io.IOException if the file cannot be opened.
     */
    public static TileWorld makeTile(String fileName) 
        throws java.io.IOException{
        TileWorld w = null;
        try{
            Scanner scan = new Scanner(new java.io.File(fileName));

            int[][] tiles;
            ArrayList<String> lines = new ArrayList<String>();
            int colCount = 0;
            while(scan.hasNext()){
                lines.add(scan.nextLine());
                colCount++;
            }
            int rowCount = lines.get(0).trim().length();
            tiles = new int[colCount][rowCount];
            int x = 0, i = 0, j = 0;
            for(String line: lines){// iterate over each line
                x = line.length();
                for(x=0; x < line.length(); x++){// iterate over each char
                    char c = line.charAt(x);
                    if (Character.isDigit(c))
                        tiles[i][j] = Character.getNumericValue(c);
                    else if( Character.isSpaceChar(c) ){}// do nothing
                    else //_ char
                        tiles[i][j] = 0;
                    j++;
                }
                i++;
            }
            w = new TileWorld(tiles);
        }
        catch(java.io.IOException ex){
            System.err.printf("Cannot open file %s%n", fileName);
        }

        return w;
    }

    /** Starts the command-line program. It can be started several
     * different ways:
     * java solve.SolverMain -[domain] -[algorithm]
     *    Domains: Maze (-maze), Sliding Tile (-tile)
     *    Algorithms: depth-first (-df), breadth-first (-bf)
     * 
     * @param args command-line parameters
     * @throws java.io.IOException if the files cannot be opened and read
     */
    public static void main(String[] args) throws java.io.IOException{
        if (args.length != 3){
            usage();
        }
        else{
            Solver<?> solver = null;

            if(args[1].equals("-df")){
                if(args[0].equals("-maze")){
                    MazeWorld.MState start = makeMaze(args[2]).getStart();
                    solver = new DFSolver<MazeWorld.MState>(start);
                }
                else if(args[0].equals("-tile")){
                    TileWorld.TState start = makeTile(args[2]).getStart();
                    solver = new DFSolver<TileWorld.TState>(start);
                }
                else{
                    usage();
                }
            }
            else if(args[1].equals("-bf")){
                if(args[0].equals("-maze")){
                    MazeWorld.MState start = makeMaze(args[2]).getStart();
                    solver = new BFSolver<MazeWorld.MState>(start);
                }
                else if(args[0].equals("-tile")){
                    TileWorld.TState start = makeTile(args[2]).getStart();
                    solver = new BFSolver<TileWorld.TState>(start);
                }
                else{
                    usage();
                }
            }
            else{
                usage();
            }

            (new SolverMain(solver)).solve();
        }
    }
}