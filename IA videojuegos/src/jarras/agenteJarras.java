package jarras;

import agentesolitario.AgenteSolitario;

public class agenteJarras extends AgenteSolitario<Jarras> {


    /**
     * Guarda el estado de salida.
     *
     * @param jarras estado de salida
     */
    protected agenteJarras(Jarras jarras) {
        super(jarras);
    }

    @Override
    public boolean esFinal(Jarras jarra) {
        return jarra.p == 1;
    }
}
