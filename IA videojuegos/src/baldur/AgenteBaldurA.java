package baldur;

public class AgenteBaldurA extends AgenteBaldur{
    /**
     * Guarda el estado de salida.
     *
     * @param posicion estado de salida
     * @param posFinal
     */
    protected AgenteBaldurA(Posicion posicion, Posicion posFinal) {
        super(posicion, posFinal);
    }

    @Override
    public int h(Posicion p) {
        int incY = Math.abs(p.y - this.posFinal.y);
        int incX = Math.abs(p.x - this.posFinal.x);

        int min = Math.min(incY, incX);
        int max = Math.max(incY, incX);

        return 1414 * min + 1000 * (max - min);
    }
}
