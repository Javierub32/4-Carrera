package solitario;

import pancakes.AgentePancakes;
import pancakes.Pancakes;

import java.util.List;

public class TestSolitario {
    public static void main(String[] args) {
        int[][] matrix = {{1,2},{3,6},{5,4}};
        Solitario solitario = new Solitario(matrix);

        AgenteCartasSolitario agenteCartasSolitario = new AgenteCartasSolitario(solitario);
        List<Solitario> solucion = agenteCartasSolitario.amplitud();
        System.out.println("Solución (" + solucion.size() + " movimientos):");
        for (Solitario s: solucion) {
            s.ver();
        }
    }
}
