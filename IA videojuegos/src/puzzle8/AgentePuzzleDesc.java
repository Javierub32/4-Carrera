package puzzle8;

public class AgentePuzzleDesc extends AgentePuzzleH0{
    public AgentePuzzleDesc(Puzzle p) {
        super(p);
    }

    @Override
    public int h (Puzzle p) {
        int counter = 0;
        int pos = 0;
        for (int i = 0; i < p.numFilas; i++) {
            for (int j = 0; j < p.numCol; j++) {
                if (p.tab[i][j] != pos && p.tab[i][j] != 0) {
                    counter++;
                }
                pos++;
            }
        }

        return counter;
    }
}
