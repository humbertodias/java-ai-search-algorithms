package com.aisearch.algorithm;

// Clase para el algoritmo: Busqueda en anch
import com.aisearch.Graph;
import com.aisearch.Node;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class BreadthFirstSearch implements Searchable{
    // variables del algoritmo

    int j, n, m, s;
    int Step;
    String miObjetivo;
    Node nodo, suc, actual;
    LinkedList<String> cola;
    Graphics g;
    private final Graph graph;

    // inicializa el algoritmo
    public BreadthFirstSearch(Graph graph) {
        this.graph = graph;
        // inicializa el algoritmo en caso de que haya uno cargado
        if (graph.getNodes().size() > 0) {
            inicio();
        }
    }

    // metodos para gestionar variables
    public String getCola() {
        int temp;
        String cadena;
        if ((Step == 0) || (Step > 3)) {
            cadena = "QUEUE={";
            for (temp = 0; temp < cola.size(); temp++) {
                cadena += cola.get(temp);
                if (temp < cola.size() - 1) {
                    cadena += ", ";
                }
            }
            cadena += "}";
        } else {
            cadena = "";
        }
        return cadena;
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
        actual = null;
        // inicializa objetos
        cola = new LinkedList<>();
        g = graph.getGraphics();
        // comienza por el nodo raiz
        nodo = graph.getNodes().get(0);
        cola.add(nodo.toString());
        // siguiente paso
        Step = 0;
    }

    // metodos para desglosar el algoritmo en pasos
    @Override
    public void paso0() {
        if (!cola.isEmpty()) {
            // extrae el primer elemento de la cola
            n = graph.getNodeIndices().indexOf(cola.poll());
            nodo = graph.getNodes().get(n);
            // establece el nodo actual
            nodo.setEstado(Node.State.CURRENT);
            nodo.pintarNodo(g);
            actual = nodo;
            // si el nodo raiz es objetivo
            if (nodo.getEsObjetivo()) {
                // establece el nodo objetivo
                miObjetivo = nodo.toString();
                // termina con exito
                Step = 4;
            } else {
                // explora todos los nodos sucesores
                m = nodo.maxSucesores();
                j = 0;
                // establece el siguiente paso
                Step = 1;
            }
        } else {
            // termina sin exito
            Step = 4;
        }
    }

    @Override
    public void paso1() {
        if (j < m) {
            s = graph.getNodeIndices().indexOf(nodo.getIdSucesor(j));
            suc = graph.getNodes().get(s);
            // establece puntero a nodo si el sucesor no se ha generado
            suc.setIdPuntero(nodo.toString());
            suc.setCostePuntero(nodo.getCosteSucesor(j));
            suc.setCosteCamino(nodo.getCosteSucesor(j)
                    + nodo.getCosteCamino());
            suc.setEstado(Node.State.OPENNED);
            suc.pintarNodo(g);
            cola.add(suc.toString());
            // comprueba si es objetivo
            if (suc.getEsObjetivo()) {
                // establece el nodo objetivo
                miObjetivo = suc.toString();
                // establece paso 2
                Step = 2;
            }
            j++;
        } else {
            // establece siguiente paso
            Step = 2;
            paso2();
        }
    }

    @Override
    public void paso2() {
        // actualiza el nodo actual
        actual.setEstado(Node.State.CLOSED);
        actual.pintarNodo(g);
        // establece el siguiente paso
        if (!miObjetivo.isEmpty()) {
            Step = 3;
        } else {
            Step = 0;
        }
    }

    @Override
    public void paso3() {
        if (!miObjetivo.isEmpty()) {
            // establece el estado de exito
            n = graph.getNodeIndices().indexOf(miObjetivo);
            nodo = graph.getNodes().get(n);
            nodo.setEstado(Node.State.CURRENT);
            nodo.pintarNodo(g);
        }
        // terminan con exito
        Step = 4;
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
