package com.aisearch;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

public class Graph extends Panel {

    private static final long serialVersionUID = 1L;

    private final List<String> IndiceNodos;
    private final List<Node> ListaNodos;
    public String NodoInicial, NodoObjetivo;

    private Boolean showLegend;
    public long timer;

    // constructor
    public Graph() {
        // constructor padre
        super();
        // inicializa
        IndiceNodos = new ArrayList<>();
        ListaNodos = new ArrayList<>();
        NodoInicial = "";
        NodoObjetivo = "";
        showLegend = true;
        timer = 1000;
    }

    public List<Node> getNodes() {
        return ListaNodos;
    }

    public List<String> getNodeIndices() {
        return IndiceNodos;
    }

    // metodo carga un grafo de un archivo
    public void loadGraph(String configuracion) throws Exception {
        Node nodo;
        ReadNodes entrada;

        // inicializa el grafo
        ListaNodos.clear();
        IndiceNodos.clear();
        NodoInicial = "";
        NodoObjetivo = "";

        // lectura de datos
        entrada = new ReadNodes();
        int numNodos = entrada.open(configuracion);
        // lee los nodos indicados en cabecera
        for (int i = 0; i < numNodos; i++) {
            nodo = entrada.read();
            // agrega la clave de busqueda del nodo
            IndiceNodos.add(nodo.toString());
            // agrega el nodo a la lista de nodos
            ListaNodos.add(nodo);
        }
        entrada.close();
        // establece el nodo inicial
        NodoInicial = IndiceNodos.get(0);
        // lo selecciona como canditato a la basura
        nodo = null;
    }

    // metodo para reiniciar el grafo
    public void reset() {
        // reinicia elementos cambiantes de cada nodo
        Node nodo;
        for (int i = 0; i < ListaNodos.size(); i++) {
            nodo = ListaNodos.get(i);
            nodo.setEstado(Node.State.NO_GENERATED);
            nodo.setIdPuntero("");
            nodo.setCostePuntero(0);
            nodo.setCosteCamino(0);
        }
        nodo = null;
        NodoObjetivo = "";
    }

    // metodos para gestionar la finromacion del grafo
    public String getNodoInicial() {
        return NodoInicial;
    }

    public String getNodoObjetivo() {
        return NodoObjetivo;
    }

    public String[] getEstadisticas() {
        String cadena[] = new String[4];
        int no_generated, openned, closed;
        // inicializa
        no_generated = 0;
        openned = 0;
        closed = 0;
        // bucle para contar
        for (int temp = 0; temp < ListaNodos.size(); temp++) {
            switch ((((Node) (ListaNodos.get(temp))).getEstado()).ordinal()) {
                case 0:
                    no_generated++;
                    break;
                case 2:
                    openned++;
                    break;
                case 3:
                    closed++;
                    break;
            }
        }
        // construye la cadena
        cadena[0] = "NOT GENERATED: " + Integer.toString(no_generated)
                + " <not visited nodes>";
        cadena[1] = "OPENED: " + Integer.toString(openned)
                + " <visited and not expanded nodes>";
        cadena[2] = "CLOSED: " + Integer.toString(closed)
                + " <visited and expanded nodes>";
        cadena[3] = "GENERATED: " + Integer.toString(openned + closed)
                + " <total visited nodes>";

        return cadena;
    }

    public void setVerLeyenda(boolean VerLeyenda) {
        this.showLegend = VerLeyenda;
    }

    public void setTemporizador(long Temporizador) {
        this.timer = Temporizador;
    }

    // metodos para pintar
    @Override
    public void paint(Graphics g) {
        paintGrafo();
        paintLeyenda();
    }

    private void paintLeyenda() {
        if (showLegend) {
            // Dibuja la leyenda
            Graphics g = this.getGraphics();
            int esquinax = this.getWidth() - 115;
            int esquinay = 15;
            g.setColor(new Color(255, 255, 225));
            g.fillRect(esquinax, esquinay, 100, 95);
            g.setColor(Color.black);
            g.drawRect(esquinax, esquinay, 100, 95);
            Font fuente = g.getFont();
            g.setFont(new Font(fuente.getFontName(), Font.BOLD, fuente
                    .getSize()));
            g.setColor(Color.black);
            g.drawString("Legend:", esquinax + 5, esquinay + 15);
            g.setFont(fuente);
            g.setColor(Color.gray);
            g.drawString("Not visited", esquinax + 20, esquinay + 32);
            g.fillRect(esquinax + 10, esquinay + 26, 5, 5);
            g.setColor(Color.red.darker());
            g.drawString("Current", esquinax + 20, esquinay + 48);
            g.fillRect(esquinax + 10, esquinay + 42, 5, 5);
            g.setColor(Color.green.darker());
            g.drawString("Opened", esquinax + 20, esquinay + 65);
            g.fillRect(esquinax + 10, esquinay + 59, 5, 5);
            g.setColor(Color.orange.darker());
            g.drawString("Closed", esquinax + 20, esquinay + 82);
            g.fillRect(esquinax + 10, esquinay + 76, 5, 5);
        }
    }

    public void paintGrafo() {
        if (ListaNodos.size() > 0) {
            paintArcos();
            paintCamino();
            paintNodos();
        }
    }

    private void paintNodos() {
        Node nodo;
        int i, n;
        Graphics g = this.getGraphics();
        // recorre los nodos
        n = ListaNodos.size();
        // pinta el el grafo
        for (i = 0; i < n; i++) {
            nodo = ListaNodos.get(i);
            // pinta el nodo actual
            nodo.pintarNodo(g);
        }
        nodo = null;
    }

    private void paintArco(String IdOrigen, String IdDestino, Color color,
            double Coste) {
        Node nodo;
        int numero;
        int xo, yo, xd, yd;
        int xc, yc;

        // resuelve el nodo orgien
        numero = IndiceNodos.indexOf(IdOrigen);
        nodo = ListaNodos.get(numero);
        xo = nodo.getX();
        yo = nodo.getY();

        // resuelve el nodo destino
        numero = IndiceNodos.indexOf(IdDestino);
        nodo = ListaNodos.get(numero);
        xd = nodo.getX();
        yd = nodo.getY();

        // pinta el arco
        Graphics g = this.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1.0f));
        g2.setColor(color);

        // pinta la linea y la flecha
        xc = (int) ((distanciaCoords(xo, yo, xd, yo) * 10) / distanciaCoords(
                xo, yo, xd, yd));
        yc = (int) ((distanciaCoords(xo, yo, xo, yd) * 10) / distanciaCoords(
                xo, yo, xd, yd));
        if ((xo > xd) && (yo <= yd)) {
            xc = xd + xc;
            yc = yd - yc;
        } else if ((xo <= xd) && (yo > yd)) {
            xc = xd - xc;
            yc = yd + yc;
        } else if ((xo <= xd) && (yo <= yd)) {
            xc = xd - xc;
            yc = yd - yc;
        } else if ((xo > xd) && (yo > yd)) {
            xc = xd + xc;
            yc = yd + yc;
        }
        drawArrow(g2, xo, yo, xc, yc);

        // pita el coste
        g2.setColor(color);
        xc = (xo + xd) / 2;
        yc = (yo + yd) / 2;
        g2.drawString(Double.toString(Coste), xc, yc);

        // objetos usados para la basura
        nodo = null;
    }

    private void paintArcos() {
        Node nodo;
        int i, j, n, m;
        // recorre los nodos
        n = ListaNodos.size();
        // pinta el el grafo
        for (i = 0; i < n; i++) {
            nodo = ListaNodos.get(i);
            // recorre los sucesores
            m = nodo.maxSucesores();
            for (j = 0; j < m; j++) {
                // pinta el arco
                paintArco(nodo.toString(), nodo.getIdSucesor(j), Color.white,
                        nodo.getCosteSucesor(j));
            }
        }
        // objetos usados para la basura
        nodo = null;
    }

    private void paintCamino() {
        // variables para el procesamiento
        int n;
        double c;
        Node no, nd;

        if (!NodoObjetivo.isEmpty()) {
            // localiza el nodo objetivo
            n = IndiceNodos.indexOf(NodoObjetivo);
            nd = ListaNodos.get(n);
            // pinta el camino que tenga
            while (!nd.getIdPuntero().isEmpty()) {
                n = IndiceNodos.indexOf(nd.getIdPuntero());
                no = ListaNodos.get(n);
                c = nd.getCostePuntero();
                paintArco(no.toString(), nd.toString(), Color.red, c);
                nd = no;
            }
        }
        // objetos usados para la basura
        nd = no = null;
    }

    // METODOS PARA PINTAR FLECHAS
    int al = 10; // Arrow length
    int aw = 9; // Arrow width
    int haw = aw / 2; // Half arrow width
    int xValues[] = new int[3];
    int yValues[] = new int[3];

    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        // Draw line
        g.drawLine(x1, y1, x2, y2);
        // Calculate x-y values for arrow head
        calcValues(x1, y1, x2, y2);
        g.fillPolygon(xValues, yValues, 3);
    }

    /* CALC VALUES: Calculate x-y values. */
    public void calcValues(int x1, int y1, int x2, int y2) {
        // North or south
        if (x1 == x2) {
            // North
            if (y2 < y1) {
                arrowCoords(x2, y2, x2 - haw, y2 + al, x2 + haw, y2 + al);
            } // South
            else {
                arrowCoords(x2, y2, x2 - haw, y2 - al, x2 + haw, y2 - al);
            }
            return;
        }
        // East or West
        if (y1 == y2) {
            // East
            if (x2 > x1) {
                arrowCoords(x2, y2, x2 - al, y2 - haw, x2 - al, y2 + haw);
            } // West
            else {
                arrowCoords(x2, y2, x2 + al, y2 - haw, x2 + al, y2 + haw);
            }
            return;
        }
        // Calculate quadrant
        calcValuesQuad(x1, y1, x2, y2);
    }

    /*
	 * CALCULATE VALUES QUADRANTS: Calculate x-y values where direction is not
	 * parallel to eith x or y axis.
     */
    public void calcValuesQuad(int x1, int y1, int x2, int y2) {
        double arrowAng = Math.toDegrees(Math.atan((double) haw / (double) al));
        double dist = Math.sqrt(al * al + aw);
        double lineAng = Math.toDegrees(Math.atan(((double) Math.abs(x1 - x2))
                / ((double) Math.abs(y1 - y2))));
        // Adjust line angle for quadrant
        if (x1 > x2) {
            // South East
            if (y1 > y2) {
                lineAng = 180.0 - lineAng;
            }
        } else // South West
        {
            if (y1 > y2) {
                lineAng = 180.0 + lineAng;
            } // North West
            else {
                lineAng = 360.0 - lineAng;
            }
        }
        // Calculate coords
        xValues[0] = x2;
        yValues[0] = y2;
        calcCoords(1, x2, y2, dist, lineAng - arrowAng);
        calcCoords(2, x2, y2, dist, lineAng + arrowAng);
    }

    /*
	 * CALCULATE COORDINATES: Determine new x-y coords given a start x-y and a
	 * distance and direction
     */
    public void calcCoords(int index, int x, int y, double dist, double dirn) {
        while (dirn < 0.0) {
            dirn = 360.0 + dirn;
        }
        while (dirn > 360.0) {
            dirn = dirn - 360.0;
        }
        // North-East
        if (dirn <= 90.0) {
            xValues[index] = x + (int) (Math.sin(Math.toRadians(dirn)) * dist);
            yValues[index] = y - (int) (Math.cos(Math.toRadians(dirn)) * dist);
            return;
        }
        // South-East
        if (dirn <= 180.0) {
            xValues[index] = x
                    + (int) (Math.cos(Math.toRadians(dirn - 90)) * dist);
            yValues[index] = y
                    + (int) (Math.sin(Math.toRadians(dirn - 90)) * dist);
            return;
        }
        // South-West
        if (dirn <= 90.0) {
            xValues[index] = x
                    - (int) (Math.sin(Math.toRadians(dirn - 180)) * dist);
            yValues[index] = y
                    + (int) (Math.cos(Math.toRadians(dirn - 180)) * dist);
        } // Nort-West
        else {
            xValues[index] = x
                    - (int) (Math.cos(Math.toRadians(dirn - 270)) * dist);
            yValues[index] = y
                    - (int) (Math.sin(Math.toRadians(dirn - 270)) * dist);
        }
    }

    // ARROW COORDS: Load x-y value arrays */
    public void arrowCoords(int x1, int y1, int x2, int y2, int x3, int y3) {
        xValues[0] = x1;
        yValues[0] = y1;
        xValues[1] = x2;
        yValues[1] = y2;
        xValues[2] = x3;
        yValues[2] = y3;
    }

    // DISTANCIA ENTRE DOS PUNTOS
    public double distanciaCoords(int xo, int yo, int xd, int yd) {
        return Math.sqrt(Math.pow(xd - xo, 2) + Math.pow(yd - yo, 2));
    }

    public void sleep() {
        // retardo de un segundo
        try {
            Thread.sleep(timer);
        } catch (Exception e) {
        }
    }
}
