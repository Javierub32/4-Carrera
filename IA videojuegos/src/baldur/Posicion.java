package baldur;

import mundosolitario.OverrideHashCode;
import mundosolitario.RepresentacionEstadoOptimizacion;
import puzzle8.Puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Posicion extends OverrideHashCode implements RepresentacionEstadoOptimizacion<Posicion> {
    int x;
    int y;

    private static final Path RUTA_MAPA = Paths.get("C:/Users/javie/Desktop/AR0011SR.map");

    static final char[][] MAPA = cargarMapa(RUTA_MAPA);

    private static char[][] cargarMapa(Path ruta) {
        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.US_ASCII)) {
            int alto = 0;
            int ancho = 0;
            String linea;
            boolean inicioDelMapa = false;

            // Lee la cabecera: type octile, height 512, width 512, map
            while ((linea = lector.readLine()) != null) {
                String cabecera = linea.trim();

                if ("map".equals(cabecera)) {
                    inicioDelMapa = true;
                    break;
                }

                String[] partes = cabecera.split("\\s+");
                if (partes.length == 2) {
                    if ("height".equals(partes[0])) alto = Integer.parseInt(partes[1]);
                    if ("width".equals(partes[0])) ancho = Integer.parseInt(partes[1]);
                }
            }

            if (!inicioDelMapa || alto <= 0 || ancho <= 0) {
                throw new IOException("Cabecera .map inválida");
            }

            char[][] mapa = new char[alto][ancho];
            for (int fila = 0; fila < alto; fila++) {
                linea = lector.readLine();
                if (linea == null || linea.length() != ancho) {
                    throw new IOException("Fila inválida en el mapa: " + fila);
                }
                mapa[fila] = linea.toCharArray();
            }

            return mapa;
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "No se pudo leer " + ruta.toAbsolutePath(), e);
        }
    }

    public static boolean esTransitable(int x, int y) {
        return y >= 0
                && y < MAPA.length
                && x >= 0
                && x < MAPA[y].length
                && MAPA[y][x] == '.';
    }

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int costeArco(Posicion destino) {
        int dx = Math.abs(this.x - destino.x);
        int dy = Math.abs(this.y - destino.y);

        if (dx == 1 && dy == 1) return 1414; // diagonal
        if (dx + dy == 1) return 1000;       // horizontal o vertical

        throw new IllegalArgumentException("Las posiciones no son vecinas");
    }

    @Override
    public List<Posicion> calculaSucesores() {
        List<Posicion> sucesores = new ArrayList<>();


        if (esTransitable(x + 1, y)) sucesores.add(new Posicion(x + 1, y));
        if (esTransitable(x + 1, y - 1)) sucesores.add(new Posicion(x + 1, y - 1));
        if (esTransitable(x, y - 1)) sucesores.add(new Posicion(x, y - 1));
        if (esTransitable(x - 1, y - 1)) sucesores.add(new Posicion(x - 1, y - 1));
        if (esTransitable(x - 1, y)) sucesores.add(new Posicion(x - 1, y));
        if (esTransitable(x - 1, y + 1)) sucesores.add(new Posicion(x - 1, y + 1));
        if (esTransitable(x, y + 1)) sucesores.add(new Posicion(x, y + 1));

        return sucesores;
    }

    public void ver() {
        System.out.println("X: " + x + " Y: " + y);
    }
}
