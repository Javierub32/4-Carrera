package jarras;

import mundosolitario.OverrideHashCode;
import mundosolitario.RepresentacionEstadoOptimizacion;
import puzzle8.Puzzle;

import java.util.ArrayList;
import java.util.List;

public class Jarras extends OverrideHashCode implements RepresentacionEstadoOptimizacion<Jarras> {
    public int g; // Contenido jarra grande
    public int p; // Contenido jarra pequeña

    final static int MAXP = 2;
    final static int MAXG = 5;

    public Jarras(int g, int p) {
        this.g = g;
        this.p = p;
    }

    public void ver() {
        System.out.println("(" + this.g + ", " + this.p + ")");
    }

    public void verSucesores(Jarras j) {
        List<Jarras> lista = j.calculaSucesores();
        System.out.println("La lista de sucesores es:");
        for(Jarras jarra : lista) {
            jarra.ver();
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public List<Jarras> calculaSucesores() {
        List<Jarras> lista = new ArrayList<Jarras>();

        // Descartar contenido de la jarra grande
        if (this.g > 0) {
            lista.add(new Jarras(0, this.p));
        }

        // Descartar contenido de la jarra grande
        if (this.p > 0) {
            lista.add(new Jarras(this.g, 0));
        }

        int total = this.g + this.p;

        // Traspaso de g a p
        if (this.g > 0 && this.p < MAXP && total <= MAXP) {
            lista.add(new Jarras(0, total));
        }

        // Traspaso de p a g
        if (this.p > 0 && this.g < MAXG && total <= MAXG) {
            lista.add(new Jarras(total, 0));
        }

        // Traspase parcial ARREGLAR
        if (this.g > 0 && this.p < MAXP) {
            int cantidad = Math.min(this.g, MAXP - this.p);
            lista.add(new Jarras(this.g - cantidad, this.p + cantidad));
        }

        if (this.p > 0 && this.g < MAXG) {
            int cantidad = Math.min(this.p, MAXG - this.g);
            lista.add(new Jarras(this.g + cantidad, this.p - cantidad));
        }


        return lista;
    }

    @Override
    public int costeArco(Jarras eDestino) {
        return 1;
    }

}
