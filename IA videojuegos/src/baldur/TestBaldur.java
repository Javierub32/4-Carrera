package baldur;

import solitario.AgenteCartasSolitario;
import solitario.Solitario;

import java.util.List;

public class TestBaldur {
    public static void main(String[] args) {
        Posicion posicion = new Posicion(150, 114); // columna, fila
        Posicion posFinal = new Posicion(200, 84);  // columna, fila

        AgenteBaldur agenteBaldur = new AgenteBaldurA(posicion, posFinal);
        List<Posicion> solucion = agenteBaldur.aMono();
        System.out.println("Tamaño solucion: " + solucion.size());
        for (Posicion pos: solucion) {
            pos.ver();
        }
    }
}
