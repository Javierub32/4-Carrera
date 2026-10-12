package pancakes;

import mundosolitario.OverrideHashCode;
import mundosolitario.RepresentacionEstadoOptimizacion;
import puzzle8.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Pancakes extends OverrideHashCode implements RepresentacionEstadoOptimizacion<Pancakes> {
    public List<Integer> lista;

    public Pancakes(List<Integer> lista) {
        this.lista = lista;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pancakes pancakes = (Pancakes) o;
        return Objects.equals(lista, pancakes.lista);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lista);
    }

    @Override
    public int costeArco(Pancakes eDestino) {
        return 1;
    }

    public List<Pancakes> calculaSucesores2() {
        List<Pancakes> sucesores = new ArrayList<Pancakes>();

        for (int i = 0; i < this.lista.size() - 1; i++) {
            List<Integer> sucesor = new ArrayList<>();
            for (int j = 0; j < this.lista.size(); j++) {
                if (j == i) {
                    sucesor.add(this.lista.get(j + 1));
                    sucesor.add(this.lista.get(j));
                    j++;
                } else {
                    sucesor.add(this.lista.get(j));
                }
            }
            Pancakes pancake = new Pancakes(sucesor);
            sucesores.add(pancake);
        }

        return sucesores;
    }

    @Override
    public List<Pancakes> calculaSucesores() {
        List<Pancakes> sucesores = new ArrayList<Pancakes>();

        // Intercambios dejando el medio como estaba
        for (int i = 1; i < this.lista.size() - 1; i++) {
            List<Integer> sucesor = new ArrayList<>(lista);
            List<Integer> sucesorAux = new ArrayList<>(lista);
            Integer tamEspatula = i < this.lista.size() / 2 ? i : this.lista.size() - 1 - i;

            // Mitad izquierda
            for (int j = i - tamEspatula; j < i; j++) {
                sucesor.set(j, sucesorAux.get(2 * i - j));
            }

            // Mitad derecha
            for (int j = i + 1; j <= i + tamEspatula; j++) {
                sucesor.set(j, sucesorAux.get(2 * i - j));
            }

            Pancakes pancake = new Pancakes(sucesor);
            sucesores.add(pancake);
        }

        // Permutaciones 2 a 2
        for (int i = 0; i < this.lista.size() - 1; i++) {
            List<Integer> sucesor = new ArrayList<>();
            for (int j = 0; j < this.lista.size(); j++) {
                if (j == i) {
                    sucesor.add(this.lista.get(j + 1));
                    sucesor.add(this.lista.get(j));
                    j++;
                } else {
                    sucesor.add(this.lista.get(j));
                }
            }
            Pancakes pancake = new Pancakes(sucesor);
            sucesores.add(pancake);
        }

        return sucesores;
    }
}
