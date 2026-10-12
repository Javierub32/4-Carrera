package puzzle8;

import java.util.List;
/**
 * Pruebas para el puzzle 2D de piezas deslizantes.
 * 
 * @Jose Miguel Horcas Aguilera, Lorenzo Mandow
 *
 */
public class TestPuzzle {
    
    public static void main(String[] args) {
    
    	//testH0();
    	
    	//testDPB();
        testPractica();
    }

    public static void testPractica(){
        int[][] ttt1 = {{3,5,0},{6,2,4},{7,8,1}};
        int[][] ttt2 = {{8,0,7},{4,1,6},{2,5,3}};

        Puzzle puzzle1 = new Puzzle(ttt1);
        Puzzle puzzle2 = new Puzzle(ttt2);

        AgentePuzzleH0 problemaH0_1 = new AgentePuzzleH0(puzzle1);
        AgentePuzzleH0 problemaH0_2 = new AgentePuzzleH0(puzzle2);

        AgentePuzzleDesc problemaDesc_1 = new AgentePuzzleDesc(puzzle1);
        AgentePuzzleDesc problemaDesc_2 = new AgentePuzzleDesc(puzzle2);

        AgentePuzzleMH problemaMH_1 = new AgentePuzzleMH(puzzle1);
        AgentePuzzleMH problemaMH_2 = new AgentePuzzleMH(puzzle2);

        AgentePuzzleBDP problemaBDP4_1 = new AgentePuzzleBDP(puzzle1, 4);
        AgentePuzzleBDP problemaBDP4_2 = new AgentePuzzleBDP(puzzle2, 4);


        AgentePuzzleBDP problemaBDP3_1 = new AgentePuzzleBDP(puzzle1, 3);
        AgentePuzzleBDP problemaBDP3_2 = new AgentePuzzleBDP(puzzle2, 3);

        System.out.println("P1 H0: "+ problemaH0_1.amplitud() + " DESC: "+ problemaDesc_1.amplitud().size() + " MANH: "+ problemaMH_1.amplitud().size()  +" 4COM: "+ problemaBDP4_1.amplitud().size() +" 3COM: "+ problemaBDP3_1.amplitud().size());
        System.out.println("P2 H0: "+ problemaH0_2.amplitud().size() + " DESC: "+ problemaDesc_2.amplitud().size() + " MANH: "+ problemaMH_2.amplitud().size()  +" 4COM: "+ problemaBDP4_2.amplitud().size() +" 3COM: "+ problemaBDP3_2.amplitud().size());

    }
    
    public static void testH0(){
    //    Puzzle s = new Puzzle(3,3);
    	int[][] ttt = {{1,2,0},{3,4,5},{6,7,8}};
        Puzzle s = new Puzzle(ttt);
		AgentePuzzleH0 problema = new AgentePuzzleH0(s);

		List<Puzzle> l =  problema.aMono();
		
		if (l != null) {
			System.out.println("Profundidad de la solucion: " + (l.size()-1));
            System.out.println("Camino Solucion:");
            for (Puzzle e : l) {
               e.ver();
            }
        } else {
            System.out.println("No se ha podido encontrar la Solucion");
        }        
	}
	
	public static void testDPB() {
        Puzzle s = new Puzzle(3,3);
		AgentePuzzleBDP problema = new AgentePuzzleBDP(s, 5);
		
		List<Puzzle> l = problema.aMono();
		
		if (l != null) {
			System.out.println("Profundidad de la solucion: " + (l.size()-1));
            System.out.println("Camino Solucion:");
            for (Puzzle e : l) {
               e.ver();
            }
        } else {
            System.out.println("No se ha podido encontrar la Solucion");
        }        
	}
	
}
