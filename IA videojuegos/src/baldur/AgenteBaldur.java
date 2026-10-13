package baldur;

import agentesolitario.AgenteSolitario;
import solitario.Solitario;

public class AgenteBaldur extends AgenteSolitario<Posicion> {
    Posicion posFinal;

    /**
     * Guarda el estado de salida.
     *
     * @param posicion estado de salida
     */
    protected AgenteBaldur(Posicion posicion, Posicion posFinal) {
        super(posicion);
        this.posFinal = posFinal;
    }

    @Override
    public boolean esFinal(Posicion posicion) {
        return posicion.equals(this.posFinal);
    }
}
