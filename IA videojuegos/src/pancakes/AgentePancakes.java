package pancakes;

import agentesolitario.AgenteSolitario;

public class AgentePancakes extends AgenteSolitario<Pancakes> {
    /**
     * Guarda el estado de salida.
     *
     * @param pancakes estado de salida
     */
    protected AgentePancakes(Pancakes pancakes) {
        super(pancakes);
    }

    @Override
    public boolean esFinal(Pancakes pancakes) {
        for (int i = 0; i < pancakes.lista.size() - 1; i++) {
            if (pancakes.lista.get(i) > pancakes.lista.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
