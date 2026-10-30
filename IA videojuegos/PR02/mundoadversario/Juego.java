package mundoadversario;

import jugadores.Jugador;

import java.util.List;

/**
 * @version 2017-12-21
 * @author Lorenzo Mandow
 * 
 * Entorno formado por el estado de un juego y dos jugadores cuyos
 * movimientos se alternan por turnos.
 */
public class Juego<E extends EstadoJuego<E>, J1 extends Jugador<E>, J2 extends Jugador<E>> {
    public J1 jug1;
    public J2 jug2;
    E eIni;
    
    /**
     * Constructor. Recibe los dos jugadores y el estado inicial del juego.
     * 
     * @param jug1  Jugador 1 (empieza el juego).
     * @param jug2  Jugador 2.
     * @param eIni  Estado inicial de un juego.
     * 
     */
    
    public Juego (J1 j1, J2 j2, E eIni){
        this.jug1 = j1;
        this.jug2 = j2;
        this.eIni = eIni;
    }
    
    /**
     * Controla el desarrollo de la partida hasta que haya terminado.
     * 
     * @param eco   Permite opcionalmente mostrar por pantalla el desarrollo del juego.
     * @return      1, 0, -1, según gane el primer jugador, haya empate, o gane el segundo jugador, respectivamente.
     */
    public int jugarPartida (boolean eco) {
        
    	E e = this.eIni;
        Jugador<E> jug;     
        
        if(eco){e.ver();}
        
        //Desarrollo de la partida
        while(!(e.ganaActual() || e.ganaOtro() || e.agotado())){
        	 jug = e.turno1() ? this.jug1 : this.jug2;     // Le toca a jug.
             e = jug.mueve(e);
             if (eco) {e.ver();}
        }//while
        
        //Mostramos el último tablero
        if (eco) {
        	System.out.println("\nPOSICIÓN FINAL DEL JUEGO:\n");
        	e.ver();
        }
        
        //Resultado
        if(e.ganaActual()){
        	if(eco){System.out.println("GANA EL JUGADOR ACTUAL");}
        	return e.turno1() ? 1 : -1;
        } else if (e.ganaOtro()){
        	if(eco){System.out.println("GANA EL OTRO");}
        	return e.turno1() ? -1 : 1;
        } else { //agotado
        	return 0;
        }
    }

    public String torneo(Integer n) {
        int i = 0;
        int j = 0;
        int resultadoPartida;
        // Se inicializa a 0 por defecto
        int [] resultados = new int[3];
        double [] resultadosFinales = new double[3];

        while (i < n) {
            resultadoPartida = this.jugarPartida(false);
            resultados[resultadoPartida + 1]++;
            i++;
        }

        while (j < 3) {
            resultadosFinales[j] = (double) resultados[j] / n * 100;
            j++;
        }

        return String.format(java.util.Locale.US, "%-12s%-12s%-12s",
                String.format(java.util.Locale.US, "%.2f%%", resultadosFinales[2]),
                String.format(java.util.Locale.US, "%.2f%%", resultadosFinales[1]),
                String.format(java.util.Locale.US, "%.2f%%", resultadosFinales[0]));
    }
}
