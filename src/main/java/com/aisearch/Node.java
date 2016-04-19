package com.aisearch;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class Node {
    // define un nuevo tipo anidado

    public enum State {
        NO_GENERATED, CURRENT, OPENNED, CLOSED
    };

    // elementos del profesor
    private final String id;
    private int x;
    private int y;
    private double Valor;
    private boolean EsObjetivo;
    private final List<Sucesor> successors;
    private State state;

    // mis movidas
    private final Sucesor pointer; // puntero a nodo de nivel superior
    private double costWay; // coste del camino del nodo actual a la raiz

    // constructor
    public Node(String Identificacion) {
        this.id = Identificacion;
        x = 0;
        y = 0;
        Valor = 0.0f;
        EsObjetivo = false;
        successors = new ArrayList<>();

        state = State.NO_GENERATED;
        pointer = new Sucesor("", 0);
        costWay = 0;
    }

    // metodos de inicializacion
    public void setPosicionX(int PosicionX) {
        this.x = PosicionX;
    }

    public void setPosicionY(int PosicionY) {
        this.y = PosicionY;
    }

    public void setEstado(State Estado) {
        this.state = Estado;
    }

    public void setValor(double Valor) {
        this.Valor = Valor;
    }

    public void setEsObjetivo(boolean EsObjetivo) {
        this.EsObjetivo = EsObjetivo;
    }

    // metodos de obtencion de datos
    @Override
    public String toString() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColorEstado() {
        Color color;
        switch (state.ordinal()) {
            case 0:
                color = Color.white;
                break;
            case 1:
                color = Color.red;
                break;
            case 2:
                color = Color.green;
                break;
            case 3:
                color = Color.orange;
                break;
            default:
                color = Color.white;
        }
        return color;
    }

    public State getEstado() {
        return state;
    }

    public boolean getEsObjetivo() {
        return EsObjetivo;
    }

    public double getValor() {
        return Valor;
    }

    // metodos para los sucesores
    public void addSucesor(String IdSucesor, int CosteSucesor) {
        Sucesor miSucesor = new Sucesor(IdSucesor, CosteSucesor);
        successors.add(miSucesor);
    }

    public int maxSucesores() {
        return successors.size();
    }

    public String getIdSucesor(int indice) {
        return ((Sucesor) successors.get(indice)).IdSucesor;
    }

    public double getCosteSucesor(int indice) {
        return ((Sucesor) successors.get(indice)).CosteSucesor;
    }

    // metodos para la gestion del puntero al nodo de nivel superior
    public String getIdPuntero() {
        return pointer.getIdSucesor();
    }

    public double getCostePuntero() {
        return pointer.getCosteSucesor();
    }

    public double getCosteCamino() {
        return costWay;
    }

    public void setIdPuntero(String IdPuntero) {
        this.pointer.setIdSucesor(IdPuntero);
    }

    public void setCostePuntero(double CostePuntero) {
        this.pointer.setCosteSucesor(CostePuntero);
    }

    public void setCosteCamino(double CosteCamino) {
        this.costWay = CosteCamino;
    }

    // metodos para pintar por pantalla
    public void pintarNodo(Graphics g) {
        Color color;
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint colorGradiente;

        color = this.getColorEstado();
        if (color == Color.black) {
            colorGradiente = new GradientPaint(x - 10, y - 10, color, x + 50, y + 50,
                    Color.white);
        } else {
            colorGradiente = new GradientPaint(x - 10, y - 10, color, x + 50, y + 50,
                    Color.black);
        }

        g2.setPaint(colorGradiente);
        g2.fill(new Ellipse2D.Double(x - 10, y - 10, 2.0 * 10, 2.0 * 10));

        Font fuente = g.getFont();
        g.setFont(new Font(fuente.getFontName(), Font.BOLD, fuente.getSize()));
        g2.setColor(color.brighter());
        g2.drawString(Double.toString(Valor), (int) x + 6, (int) y - 8);

        if (color == Color.black) {
            g2.setColor(Color.white);
        } else {
            g2.setColor(Color.black);
        }
        g2.drawString(id, (int) x - 4, (int) y + 4);
        g.setFont(fuente);

        if (EsObjetivo) {
            g2.setColor(Color.red);
            g2.drawOval((int) x - 13, (int) y - 13, 2 * 13, 2 * 13);
        }
    }

    // gestion de la lista de pares de sucesores
    public class Sucesor {

        private String IdSucesor;
        private double CosteSucesor;

        public Sucesor(String IdSucesor, int CosteSucesor) {
            this.IdSucesor = IdSucesor;
            this.CosteSucesor = CosteSucesor;
        }

        public String getIdSucesor() {
            return IdSucesor;
        }

        public double getCosteSucesor() {
            return CosteSucesor;
        }

        public void setIdSucesor(String IdSucesor) {
            this.IdSucesor = IdSucesor;
        }

        public void setCosteSucesor(double CosteSucesor) {
            this.CosteSucesor = CosteSucesor;
        }
    }
}
