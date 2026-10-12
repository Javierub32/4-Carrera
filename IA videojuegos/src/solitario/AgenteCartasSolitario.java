package solitario;

import agentesolitario.AgenteSolitario;
import puzzle8.Puzzle;

public class AgenteCartasSolitario extends AgenteSolitario<Solitario> {
    /**
     * Guarda el estado de salida.
     *
     * @param solitario estado de salida
     */
    protected AgenteCartasSolitario(Solitario solitario) {
        super(solitario);
    }

    @Override
    public boolean esFinal(Solitario solitario) {
        int [][] cartas = solitario.cartas;
        int min = cartas[solitario.numFilas - 1][0];
        boolean esFinal = true;

        for (int i = solitario.numFilas - 1; i >= 0; i--) {
            if (min <= cartas[i][0]) {
                min = cartas[i][0];
            } else {
                esFinal = false;
            }
        }

        for (int i = solitario.numFilas - 1; i >= 0; i--) {
            if (min <= cartas[i][1]) {
                min = cartas[i][1];
            } else {
                esFinal = false;
            }
        }

        return esFinal;
    }
}
