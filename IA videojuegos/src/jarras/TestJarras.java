package jarras;

import java.util.List;

public class TestJarras {
    public static void main(String[] args) {
        Jarras jj = new Jarras(5,0);
        jj.ver();

        jj.verSucesores(jj);

        agenteJarras ag = new agenteJarras(jj);
        List<Jarras> solucion = ag.amplitud();
        System.out.println("Solucion");
        for(Jarras j : solucion){
            j.ver();
        }
    }
}
