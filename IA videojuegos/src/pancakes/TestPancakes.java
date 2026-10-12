package pancakes;

import jarras.Jarras;
import jarras.agenteJarras;

import java.util.ArrayList;
import java.util.List;

public class TestPancakes {
    public static void main(String[] args) {
        Pancakes pancakes = new Pancakes(new ArrayList<>(List.of(7,3,1,6,2,4,5)));

        AgentePancakes agentePancakes = new AgentePancakes(pancakes);
        List<Pancakes> solucion = agentePancakes.amplitud();
        System.out.println("Solución (" + solucion.size() + " movimientos):");
        for (Pancakes pancake: solucion) {
            System.out.println(pancake.lista);
        }
    }
}
