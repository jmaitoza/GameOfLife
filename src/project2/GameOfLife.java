package project2;

import java.util.Arrays;

public class GameOfLife implements GameInterface {
    private boolean[][] originalMap,    // The initial cell configuration
                                map,    // The current cell configuration
                             newMap;    // The next generation configuration
    private GameGUI gui;
    private int generation = 0;
    private FileIO fileIO;

    // GameOfLife constructor
    public GameOfLife() {
        originalMap = new boolean[MAX_ROWS][MAX_COLS];
        map = new boolean[MAX_ROWS][MAX_COLS];
        newMap = new boolean[MAX_ROWS][MAX_COLS];
        gui = new GameGUI(this, map);
        gui.setTitle("CIS 181 Array Based Game Of Life");
        fileIO = new FileIO("life", "Game of Life Text Files");
        readExample(2);
    }

    //  ====>>>>> Complete the methods below this line! <<<<<====

    // copyMap:
    // Precondtions: None.
    // Postcondtion: 'map' is a deep copy of 'sourceMap'.
    //
    private void copyMap(boolean sourceMap[][]) {

        // ==> 1. Add your code here!
        for (int i = 0; i < MAX_ROWS; i++)
        {
            System.arraycopy(sourceMap[i], 0, map[i], 0, MAX_COLS);
        }
    }

    // clearMap:
    // Precondtions: None.
    // Postcondtion: Sets all cells of the 'targetMap' to DEAD.
    //
    private void clearMap(boolean targetMap[][]) {

        // ==> 2. Add your code here!
        for (int i = 0; i < MAX_ROWS; i++)
        {
            for (int j = 0; j < MAX_COLS; j++)
            {
                targetMap[i][j] = false; // sets the cell to dead
            }
        }

    }

    //  getFlatNeighborCount:
    //  Precondtions:  0 <= row < MAX_ROWS and 0 <= col < MAX_COLS.
    //  Postcondtion:  A count of all LIVE neighbors of the cell at [row, col] is
    //                 returned where its neighbors are all the ADJACENT cells
    //                 including those
    //                 a) In the rows BELOW and ABOVE the cell (if any exist).
    //                 b) In the columns LEFT and RIGHT of the cell (if any exist).
    //                 Thus, a cell adjacent to a board edge (or corner) has
    //                 fewer neighbors than other cells.
    //
    private int getFlatNeighborCount(int row, int col){
        int count = 0;

        // ==> 3. Add your code here!
        // up = (row,col-1)
        // down = (row,col+1)
        // left = (row-1,col)
        // right = (row+1,col)

        for (int i = row-1; i <= row+1; i++)
        {
            if (i >= 0 && i < map.length)
            {
                for (int j = col-1; j <= col+1; j++)
                {
                    if ((j >= 0 && j < map[i].length) && (i != row || j != col) && map[i][j])
                    {
                        count++;
                    }
                }
            }
        }

        return count;
    }


    // nextGenerationForFlatGrid:
    // Precondtions: None
    // Postcondtion: The next generation of live and dead cells is calculated using
    //               a) the FLAT neighbor counts.
    //               b) the current birth, survival and death count rules.
    //               c) the rules are applied to the counts obtained from the current
    //                  generation's configuration of live and dead cells.
    //               The current 'map' is updated to the next generation's configuration
    //               of live and dead cells.
    //
    public  void nextGenerationForFlatGrid() {

        // ==> 4. Add your code here!

        // Birth rule:
        // If a cell has exactly 3 live neighbors, it becomes alive in the next generation.
        //
        // Survival rule:
        // If a cell has 2 or 3 live neighbors, it remains alive in the next generation.
        //
        // Death rule:
        // If a cell has less than 2 live neighbors, it dies in the next generation.
        // If a cell has more than 3 live neighbors, it dies in the next generation.

        for (int i = 0; i < MAX_ROWS; i++)
        {
            for (int j = 0; j < MAX_COLS; j++)
            {
                int count = getFlatNeighborCount(i,j);

                if (map[i][j])
                {
                    if (count < 2 || count > 3)
                    {
                        newMap[i][j] = false;
                    }
                    else
                    {
                        newMap[i][j] = true;
                    }
                }
                else
                {
                    if (count == 3)
                    {
                        newMap[i][j] = true;
                    }
                    else
                    {
                        newMap[i][j] = false;
                    }
                }
            }
        }

        copyMap(newMap);
        generation++;
    }

    // ==> 5. Implement the game of life for torus grid.

    //getTorusNeighborCount:
    // Precondition: -1 <= row < MAX_ROWS and -1 <= col < MAX_COLS.
    // Postcondition: A count of all LIVE neighbors of the cell at [row, col] is
    //                returned where its neighbors are all the ADJACENT cells
    //                including those
    //                a) In the rows BELOW and ABOVE the cell (if any exist).
    //                b) In the columns LEFT and RIGHT of the cell (if any exist).
    //                c) As well as the edges of the board.
    //                Thus, a cell adjacent to a board edge (or corner) has
    //                the same neighbors as other cells.
    //
    private int getTorusNeighborCount(int row, int col) {
        int count = 0;
        int tempI;
        int tempJ;

        for (int i = row-1; i <= row+1; i++)
        {
            tempI = i;
            if (i == -1)
            {
                tempI = MAX_ROWS-1;
            }
            if (i == MAX_ROWS)
            {
                tempI = 0;
            }
            for (int j = col-1; j <= col+1; j++)
            {
                tempJ = j;
                if (j == -1)
                {
                    tempJ = MAX_COLS-1;
                }
                if (j == MAX_COLS)
                {
                    tempJ = 0;
                }
                if ((tempI != row || tempJ != col) && map[tempI][tempJ])
                {
                    count++;
                }
            }
        }

        return count;
    }


    // nextGenerationForTorusGrid:
    // Precondtions: None
    // Postcondition: The next generation of live and dead cells is calculated using
    //               a) the TORUS neighbor counts.
    //               b) the current birth, survival and death count rules.
    //               c) the rules are applied to the counts obtained from the current
    //                  generation's configuration of live and dead cells.
    //               The current 'map' is updated to the next generation's configuration
    //               of live and dead cells.
    //
    public  void nextGenerationForTorusGrid() {
        int count = 0;
        // Birth rule:
        // If a cell has exactly 3 live neighbors, it becomes alive in the next generation.
        //
        // Survival rule:
        // If a cell has 2 or 3 live neighbors, it remains alive in the next generation.
        //
        // Death rule:
        // If a cell has less than 2 live neighbors, it dies in the next generation.
        // If a cell has more than 3 live neighbors, it dies in the next generation.

        for (int i = 0; i < MAX_ROWS; i++)
        {
            for (int j = 0; j < MAX_COLS; j++)
            {
                count = getTorusNeighborCount(i,j);
                if (map[i][j])
                {
                    if (count < 2 || count > 3)
                    {
                        newMap[i][j] = false;
                    }
                    else
                    {
                        newMap[i][j] = true;
                    }
                }
                else
                {
                    if (count == 3)
                    {
                        newMap[i][j] = true;
                    }
                    else
                    {
                        newMap[i][j] = false;
                    }
                }
            }
        }

        copyMap(newMap);
        generation++;
    }


    //  ====>>>>> Don't touch the code below this line! <<<<<====

    // Return the next generation
    public int getGeneration() {
        return generation;
    }

    // Reset the map to the original map
    public void reset() {
        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    // Game of life examples 1-4: Fish, Plus, Glider, FlyingMachine
    public void readExample(int n) {
        System.out.println("Initializing with example " + n + " ...");
        clearMap(originalMap);

        switch (n) {
           case 1: // Example 1: Fish
             for (int col = 23; col <= 26; col++)
                 originalMap[13][col] = ALIVE;
             originalMap[14][22] = ALIVE;
             originalMap[14][26] = ALIVE;
             originalMap[15][26] = ALIVE;
             originalMap[16][22] = ALIVE;
             originalMap[16][25] = ALIVE;
             break;
           case 2: // Example 2: Plus
               for (int col = 6; col < 43; col++)
                 originalMap[24][col] = ALIVE;
             for (int row = 6; row < 43; row++)
                 originalMap[row][24] = ALIVE;
             break;
           case 3: // Example 3: Glider
                originalMap[14][23] = ALIVE;
             originalMap[15][24] = ALIVE;
             for (int row = 13; row <= 15; row++)
                 originalMap[row][25] = ALIVE;
             break;
           case 4: // Example 4: FlyingMachine
                for (int col = 22; col <= 25; col++) {
                 originalMap[11][col] = ALIVE;
                 originalMap[19][col] = ALIVE;
             }
             for (int row = 14; row <= 16; row++)
                 for (int col = 17; col <= 18; col++)
                     originalMap[row][col] = ALIVE;
             originalMap[15][19] = ALIVE;
             for (int row = 12; row <= 18; row = row+2)
                 originalMap[row][21] = ALIVE;
             originalMap[14][24] = ALIVE;
             originalMap[16][24] = ALIVE;
             originalMap[12][25] = ALIVE;
             originalMap[13][25] = ALIVE;
             originalMap[17][25] = ALIVE;
             originalMap[18][25] = ALIVE;
             break;
           default: // Default Example: ClearSpace
             break;
        }

        copyMap(originalMap);
        generation = 0;
        gui.repaint();
    }

    //  Read map from file
    public void readInMap() {
        clearMap(originalMap);
        if (fileIO.read(originalMap)) {
            copyMap(originalMap);
            generation = 0;
        } else
            readExample(2);
        gui.repaint();
    }

    // Write map to file
    public void writeMap() {
       fileIO.write(map);
    }

    // Change the state of a cell
    public void updateMap(int row, int col) {
        map[row][col] = !map[row][col];

    }

    // Destroy the GUI window
    public void destroy() {
        gui.dispose();
    }

    // The main method of GameOfLife
    public static void  main(String[] args) {
        GameOfLife game = new GameOfLife();
    }
}
