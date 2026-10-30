package conectak;

import java.util.List;


import jugadores.*;
import mundoadversario.Juego;

public class TestConectaK {

	public static void main(String[] args) {
		
		
     	TestTorneo();
//		aleatorioHumano();
//		evaluarHumano();
	//	evaluarTVHumano();
	}

    public static void TestTorneo() {
        ConectaK e = new ConectaK(3,3,3);
        Jugador<ConectaK> jAleatorio1 = new JugadorAleatorio<ConectaK>();
        Jugador<ConectaK> jAleatorio2 = new JugadorAleatorio<ConectaK>();
        Jugador<ConectaK> jEvaluar = new JugadorEvaluar<>(new EvaluadorCK());
        Jugador<ConectaK> jAlfaBeta2 = new JugadorAlfaBeta<>(new EvaluadorCK(), 2);
        Jugador<ConectaK> jAlfaBeta3 = new JugadorAlfaBeta<>(new EvaluadorCK(), 3);
        Jugador<ConectaK> jAlfaBeta4 = new JugadorAlfaBeta<>(new EvaluadorCK(), 4);
        Jugador<ConectaK> jAlfaBeta5 = new JugadorAlfaBeta<>(new EvaluadorCK(), 5);
        Jugador<ConectaK> jAlfaBeta6 = new JugadorAlfaBeta<>(new EvaluadorCK(), 6);

        Juego juego1 = new Juego(jAleatorio1, jAleatorio2, e);
        Juego juego2 = new Juego(jAleatorio1, jEvaluar, e);
        Juego juego3 = new Juego(jAleatorio1, jAlfaBeta2, e);
        Juego juego4 = new Juego(jAleatorio1, jAlfaBeta3, e);
        Juego juego5 = new Juego(jAleatorio1, jAlfaBeta4, e);
        Juego juego6 = new Juego(jAleatorio1, jAlfaBeta5, e);
        Juego juego7 = new Juego(jAleatorio1, jAlfaBeta6, e);

        System.out.printf("%-30s%-12s%-12s%-12s%n", "", "Gana 1", "Empate", "Gana 2");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-30s%s%n", "Aleatorio vs Aleatorio", juego1.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs Evaluar", juego2.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs AlfaBeta (p=2)", juego3.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs AlfaBeta (p=3)", juego4.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs AlfaBeta (p=4)", juego5.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs AlfaBeta (p=5)", juego6.torneo(1000));
        System.out.printf("%-30s%s%n", "Aleatorio vs AlfaBeta (p=6)", juego7.torneo(1000));
    }
	
    
    public static void aleatorio(){
        ConectaK e = new ConectaK(3,3,3);
        Jugador j1 = new JugadorAleatorio<ConectaK>();
        Jugador j2 = new JugadorAleatorio<ConectaK>();
        Juego juego1 = new Juego(j1, j2, e);

        System.out.println(juego1.torneo(20));
        //verPartida(juego1);
    }
    
    public static void aleatorioHumano(){
        ConectaK e = new ConectaK(3,3,3);
        Jugador j1 = new JugadorAleatorio<ConectaK>();
        Jugador j2 = new JugadorHumanoCK();
        Juego juego1 = new Juego(j1, j2, e);
        
        verPartida(juego1);       
    }
    
    
    public static void evaluarHumano(){
        ConectaK e = new ConectaK(3,3,3);
        Jugador j1 = new JugadorEvaluar(new EvaluadorCK());
        Jugador j2 = new JugadorHumanoCK();
        Juego juego1 = new Juego(j1, j2, e);
        
        verPartida(juego1);       
    }
    
    public static void evaluarTVHumano(){
        ConectaK e = new ConectaK(3,3,3);
        JugadorEvaluarTV<ConectaK> j1 = new JugadorEvaluarTV();
        
        
        //entrenamos
        double epsilon = 0.1;//probabilidad de exploración
        int nPartidas = 5000;
        Jugador ja = new JugadorAleatorio();
        System.out.println("Entrenando...");
        for(int i = 0; i< nPartidas; i++){
        	j1.aprendeTurno1(ja, e, epsilon);
        }
        System.out.println("OK");
        
        //jugamos
        Jugador j2 = new JugadorHumanoCK();
        Juego juego1 = new Juego(j1, j2, e);

        verPartida(juego1);
    }
    
      
public static void verPartida (Juego juego){
        int i =  juego.jugarPartida(true);
        
        if (i == 0){
            System.out.println("Empate.");
        } else if (i == 1) {
            System.out.println("Gana el primer jugador.");
        } else {
            System.out.println("Gana el segundo jugador.");
        }
    }

	
	
	

}
