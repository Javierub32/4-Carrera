package solitario;

import mundosolitario.OverrideHashCode;
import mundosolitario.RepresentacionEstadoOptimizacion;
import puzzle8.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Solitario extends OverrideHashCode implements RepresentacionEstadoOptimizacion<Solitario> {
    int[][] cartas;
    int numCartas;
    int numFilas;

    public Solitario (int[][] cartas) {
        numFilas = cartas.length;
        this.cartas = new int[cartas.length][cartas[0].length];
        numCartas = 0;

        for (int i = 0; i < cartas.length; i++) {
            for (int j = 0; j < cartas[j].length; j++) {
                this.cartas[i][j] = cartas[i][j];
                numCartas++;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Solitario solitario = (Solitario) o;
        return numCartas == solitario.numCartas && numFilas == solitario.numFilas && Objects.deepEquals(cartas, solitario.cartas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(cartas), numCartas, numFilas);
    }

    @Override
    public int costeArco(Solitario eDestino) {
        return 1;
    }

    @Override
    public List<Solitario> calculaSucesores() {
        List<Solitario> sucesores = new ArrayList<>();
        int aux = 0;

        // Intercambio entre columnas
        Solitario s1 = new Solitario(this.cartas);
        aux = s1.cartas[numFilas - 1][0];
        s1.cartas[numFilas - 1][0] = s1.cartas[numFilas - 1][1];
        s1.cartas[numFilas - 1][1] = aux;
        sucesores.add(s1);

        // Rotamos columna 1
        Solitario s2 = new Solitario(this.cartas);
        aux = s2.cartas[numFilas - 1][0];
        for (int i = numFilas - 1; i > 0; i--) {
            s2.cartas[i][0] = s2.cartas[i - 1][0];
        }
        s2.cartas[0][0] = aux;
        sucesores.add(s2);


        // Rotamos columna 2
        Solitario s3 = new Solitario(this.cartas);
        aux = s3.cartas[numFilas - 1][1];
        for (int i = numFilas - 1; i > 0; i--) {
            s3.cartas[i][1] = s3.cartas[i - 1][1];
        }
        s3.cartas[0][1] = aux;
        sucesores.add(s3);


        return sucesores;
    }

    public void ver() {

        for (int i = 0; i < cartas.length; i++) {
            for (int j = 0; j < cartas[j].length; j++) {
                System.out.print(cartas[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
