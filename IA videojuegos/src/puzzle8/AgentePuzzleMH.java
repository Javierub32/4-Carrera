package puzzle8;

public class AgentePuzzleMH extends AgentePuzzleH0 {
    /**
     * Constructor for objects of class ProbPuzzleH0
     *
     * @param p
     */
    public AgentePuzzleMH(Puzzle p) {
        super(p);
    }

    @Override
    public int h (Puzzle p) {
        int counter = 0;
        int filaObj = -1;
        int colObj = -1;
        for (int i = 0; i < p.numFilas; i++) {
            for (int j = 0; j < p.numCol; j++) {
                if (p.tab[i][j] != 0) {
                    filaObj = p.tab[i][j] / p.numFilas;
                    colObj = p.tab[i][j] % p.numFilas;
                    counter += Math.abs(i - filaObj) + Math.abs(j - colObj);
                }
            }
        }

        return counter;
    }
}
