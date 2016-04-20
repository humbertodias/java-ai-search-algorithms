package com.aisearch.algorithm;

import com.aisearch.Graph;
import com.aisearch.Node;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Clase para el algoritmo: Primero el me
public class BestFirstSearch implements Searchable {
    // variables del algoritmo

    int j, n, m, s;
    int Step;
    String miObjetivo;
    Node nodo, suc;
    List<String> abiertos;
    List<String> cerrados;
    Graphics g;

    private final Graph graph;

    // inicializa el algoritmo
    public BestFirstSearch(Graph graph) {
        this.graph = graph;

        // inicializa el algoritmo en caso de que haya uno cargado
        if (graph.getNodes().size() > 0) {
            inicio();
        }
    }

    // metodos para gestionar variables
    @Override
    public String getAbierta() {
        int temp;
        String cadena;
        int itemp;
        Node ntemp;
        if (Step == 0) {
            cadena = "OPENED={";
            for (temp = 0; temp < abiertos.size(); temp++) {
                cadena += abiertos.get(temp) + "(";
                itemp = graph.getNodeIndices().indexOf(abiertos.get(temp));
                ntemp = graph.getNodes().get(itemp);
                cadena += Double.toString(ntemp.getValor());
                cadena += ")";
                if (temp < abiertos.size() - 1) {
                    cadena += ", ";
                }
            }
            cadena += "}";
        } else {
            cadena = "";
        }
        return cadena;
    }

    @Override
    public String getCerrada() {
        int temp;
        String cadena;
        if (Step == 0) {
            cadena = "CLOSED={";
            for (temp = 0; temp < cerrados.size(); temp++) {
                cadena += cerrados.get(temp);
                if (temp < cerrados.size() - 1) {
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
        while (Step < 2) {
            switch (Step) {
                case 0:
                    paso0();
                    break;
                case 1:
                    paso1();
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
        // inicializa objetos
        abiertos = new ArrayList<>();
        cerrados = new ArrayList<>();
        g = graph.getGraphics();
        // comienza por el primer nodo
        nodo = graph.getNodes().get(0);
        // encola el primer nodo
        abiertos.add(nodo.toString());
        // siguiente paso
        Step = 0;
    }

    @Override
    public void paso0() {
        if (!abiertos.isEmpty()) {
            // extrae el primer nodo de la pila abiertos
            n = graph.getNodeIndices().indexOf(abiertos.remove(0));
            nodo = graph.getNodes().get(n);
            nodo.setEstado(Node.State.CURRENT);
            nodo.pintarNodo(g);
            if (!nodo.getEsObjetivo()) {
                // se inserta en la lista de cerrados
                cerrados.add(nodo.toString());
                // se prepara para explorar los sucesores
                m = nodo.maxSucesores();
                j = 0;
                // siguiente paso
                Step = 1;
            } else {
                // establece el objetivo encontrado
                miObjetivo = nodo.toString();
                // termina con exito
                Step = 2;
            }
        } else {
            // termina con fracaso
            Step = 2;
        }
    }

    @Override
    public void paso1() {
        if (j < m) {
            // obtiene el nodo sucesor
            s = graph.getNodeIndices().indexOf(nodo.getIdSucesor(j));
            suc = graph.getNodes().get(s);
            switch (suc.getEstado()) {
                case NO_GENERATED:
                    // establece puntero a nodo si el sucesor no se ha generado
                    suc.setIdPuntero(nodo.toString());
                    suc.setCostePuntero(nodo.getCosteSucesor(j));
                    suc.setCosteCamino(nodo.getCosteSucesor(j)
                            + nodo.getCosteCamino());
                    // inserta el elemento en la lista ABIERTOS
                    suc.setEstado(Node.State.OPENNED);
                    suc.pintarNodo(g);
                    // inserta el sucesor en la lista de abiertos
                    abiertos.add(suc.toString());
                    break;
                case OPENNED:
                    // modifica el puntero del sucesor a nodo
                    suc.setIdPuntero(nodo.toString());
                    suc.setCostePuntero(nodo.getCosteSucesor(j));
                    suc.setCosteCamino(nodo.getCosteSucesor(j)
                            + nodo.getCosteCamino());
                    suc.pintarNodo(g);
                    break;
                // no se que hacer
                case CLOSED:
                    break;
                default:
                    break;
            }
            j++;
        } else if (!abiertos.isEmpty()) {
            // pinta el nodo
            nodo.setEstado(Node.State.CLOSED);
            nodo.pintarNodo(g);
            // reordena la lista de abiertos en funcion de: f = h + g
            reordenar(abiertos);
            // siguiente paso
            Step = 0;
        } else {
            // termina con fracaso
            Step = 2;
        }
    }

    @Override
    public void reordenar(List<String> vector) {
        // Ordenacion por seleccion
        Node ntemp;
        int itemp;
        String nodoi, nodoj, minn;
        double valori, valorj;
        int minj;
        double minx;
        int N = vector.size();

        for (int i = 0; i < N - 1; i++) {
            nodoi = (String) vector.get(i);
            itemp = graph.getNodeIndices().indexOf(nodoi);
            ntemp = graph.getNodes().get(itemp);
            valori = ntemp.getValor();

            minj = i;
            minx = valori;
            minn = nodoi;
            for (int j = i; j < N; j++) {
                nodoj = vector.get(j);
                itemp = graph.getNodeIndices().indexOf(nodoj);
                ntemp = graph.getNodes().get(itemp);
                valorj = ntemp.getValor();
                if (valorj <= minx) {
                    minj = j;
                    minx = valorj;
                    minn = nodoj;
                }
            }
            vector.set(minj, nodoi);
            vector.set(i, minn);
        }
    }

    @Override
    public void paso2() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void paso3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
