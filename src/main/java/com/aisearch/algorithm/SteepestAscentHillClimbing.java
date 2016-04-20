package com.aisearch.algorithm;

// Clase para el algoritmo: Escalada por la maxima pendie
import com.aisearch.Graph;
import com.aisearch.Node;
import java.awt.Graphics;
import java.util.List;

public class SteepestAscentHillClimbing implements Searchable {
    // variables del algoritmo

    int j, n, m, s;
    int Step;
    String miObjetivo;
    String actual;
    Node nodo, suc, succ;
    Graphics g;
    private final Graph graph;

    // inicializa el algoritmo
    public SteepestAscentHillClimbing(Graph graph) {
        this.graph = graph;
        // inicializa el algoritmo en caso de que haya uno cargado
        if (graph.getNodes().size() > 0) {
            inicio();
        }
    }

    // reinicia el algoritmo
    @Override
    public void reset() {
        // inicia el algoritmo;
        if (graph.getNodes().size() > 0) {
            inicio();
        }
    }

    // ejecuta el algoritmo en modo temporizador
    @Override
    public void run() {
        while (Step < 4) {
            switch (Step) {
                case 0:
                    paso0();
                    break;
                case 1:
                    paso1();
                    break;
                case 2:
                    paso2();
                    break;
                case 3:
                    paso3();
                    break;
                default:
                    graph.NodoObjetivo = miObjetivo;
                    graph.paintGrafo();
            }
            // retardo de un segundo
            graph.sleep();
        }
        graph.NodoObjetivo = miObjetivo;
        graph.paintGrafo();
    }

    // ejecuta el algoritmo en modo paso por paso
    @Override
    public boolean step() {
        boolean ejecutandose = true;
        switch (Step) {
            case 0:
                paso0();
                break;
            case 1:
                paso1();
                break;
            case 2:
                paso2();
                break;
            case 3:
                paso3();
                break;
            default: {
                ejecutandose = false;
                graph.NodoObjetivo = miObjetivo;
                graph.paintGrafo();
            }
        }
        return ejecutandose;
    }

    // metodos para desglosar el algoritmo en pasos
    @Override
    public void inicio() {
        // inicializa el objetivo
        miObjetivo = "";
        // inicializa variables para explorar sucesores
        j = 0;
        m = 0;
        nodo = null;
        suc = null;
        succ = null;
        // inicializa objetos
        actual = "";
        g = graph.getGraphics();
        // comienza por el primer nodo
        nodo = graph.getNodes().get(0);
        actual = nodo.toString();
        // siguiente paso
        Step = 0;
    }

    @Override
    public void paso0() {
        // extrae el nodo actual
        n = graph.getNodeIndices().indexOf(actual);
        nodo = graph.getNodes().get(n);
        nodo.setEstado(Node.State.CURRENT);
        nodo.pintarNodo(g);
        // comprueba si es objetivo
        if (nodo.getEsObjetivo()) {
            // establece el objetivo encontrado
            miObjetivo = nodo.toString();
            // termina con exito
            Step = 4;
        } else {
            // se prepara para explorar los sucesores
            m = nodo.maxSucesores();
            j = 0;
            // siguiente paso
            Step = 1;
        }
    }

    @Override
    public void paso1() {
        if ((j < m) && (j == 0)) {
            // asigna el primer sucesor a succ para comparar
            s = graph.getNodeIndices().indexOf(nodo.getIdSucesor(0));
            succ = graph.getNodes().get(s);
            // establece puntero a nodo
            succ.setIdPuntero(nodo.toString());
            succ.setCostePuntero(nodo.getCosteSucesor(j));
            succ.setCosteCamino(nodo.getCosteSucesor(j)
                    + nodo.getCosteCamino());
            // pinta el nodo openned
            succ.setEstado(Node.State.OPENNED);
            succ.pintarNodo(g);
            j++;
            // siguiente paso
            Step = 2;
        } else {
            // termina con fracaso
            Step = 4;
        }
    }

    @Override
    public void paso2() {
        // explora los sucesores
        if (j < m) {
            s = graph.getNodeIndices().indexOf(nodo.getIdSucesor(j));
            suc = graph.getNodes().get(s);
            // establece puntero a nodo
            suc.setIdPuntero(nodo.toString());
            suc.setCostePuntero(nodo.getCosteSucesor(j));
            suc.setCosteCamino(nodo.getCosteSucesor(j)
                    + nodo.getCosteCamino());
            // pinta el nodo openned
            suc.setEstado(Node.State.OPENNED);
            suc.pintarNodo(g);
            // si es un estado objetivo, devolverlo y terminar
            if (suc.getEsObjetivo()) {
                // selecciona el mejor estado
                succ = suc;
                // siguiente paso
                Step = 3;
            } else if (suc.getValor() < succ.getValor()) {
                // selecciona el mejor estado
                succ = suc;
            }
            // siguiente sucesor
            j++;
        } else if (succ.getValor() < nodo.getValor()) {
            // siguiente paso
            Step = 3;
            paso3();
        } else {
            // termina con fracaso
            Step = 4;
        }
    }

    @Override
    public void paso3() {
        // pinta el nodo
        nodo.setEstado(Node.State.CLOSED);
        nodo.pintarNodo(g);
        // actualiza el nodo actual
        actual = succ.toString();
        // siguiente paso
        Step = 0;
    }

    @Override
    public void reordenar(List<String> vector) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCerrada() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAbierta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
