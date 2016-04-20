package com.aisearch;

import com.aisearch.algorithm.AStar;
import com.aisearch.algorithm.BestFirstSearch;
import com.aisearch.algorithm.BreadthFirstSearch;
import com.aisearch.algorithm.DepthFirstSearch;
import com.aisearch.algorithm.SimpleHillClimbing;
import com.aisearch.algorithm.SteepestAscentHillClimbing;
import java.util.Date;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

public final class AISearch extends Frame {

    private static final long serialVersionUID = 1L;

    Dimension screen;
    int width, height, x, y;

    MenuBar MMenu;
    MenuItem MOpen, MExit, MAbout, MClean;
    Menu MTimer;
    CheckboxMenuItem CMITimer, CMIStep, CMILegend;
    CheckboxMenuItem CMIVeryFast, CMIFast, CMIMedium, CMISlow;
    Choice CSearch;
    Button BRun, BRestart;
    TextArea TAInformation;
    Panel PNorte, PSur;
    FileDialog FDOpen;
    Dialog DAbout;

    private final Graph grafo;
    private BreadthFirstSearch bfs;
    private DepthFirstSearch dfs;
    private SimpleHillClimbing shc;
    private SteepestAscentHillClimbing sahc;
    private BestFirstSearch bf;
    private AStar ae;

    // clase main
    public static void main(String args[]) {
        File graphPathFile = null;
        if (args.length > 0) {
            graphPathFile = new File(args[0]);
        }
        // instancia una ventana
        new AISearch(graphPathFile);
    }

    // constructor de la clase (ventana del programa)
    public AISearch(File graphPathFile) {
        // ventana principal
        super("AI-Search Algorithms");
        this.setLayout(new BorderLayout());
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        // incorpora el controlador de eventos a la ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // menu de la ventana principal
        MMenu = new MenuBar();
        Menu mfile = new Menu("File"), mver = new Menu("View"), mejecucion = new Menu("Run"), mabout = new Menu("Help");
        MTimer = new Menu("Speed timer");
        MOpen = new MenuItem("Open");
        MExit = new MenuItem("Exit");
        MClean = new MenuItem("Clear message panel");
        MAbout = new MenuItem("About AI-Search Algorithms");
        CMILegend = new CheckboxMenuItem("Legend for nodes");
        CMILegend.setState(true);
        CMIStep = new CheckboxMenuItem("Step by step");
        CMITimer = new CheckboxMenuItem("Timer");
        CMIVeryFast = new CheckboxMenuItem("Very fast (0s)");
        CMIFast = new CheckboxMenuItem("Fast (0.1s)");
        CMIMedium = new CheckboxMenuItem("Medium (0.5s)");
        CMISlow = new CheckboxMenuItem("Slow (1s)");
        CMIStep.setState(true);
        CMITimer.setState(false);
        CMIVeryFast.setState(false);
        CMIFast.setState(false);
        CMIMedium.setState(false);
        CMISlow.setState(true);
        MTimer.setEnabled(false);
        mfile.add(MOpen);
        mfile.addSeparator();
        mfile.add(MExit);
        mver.add(CMILegend);
        mver.addSeparator();
        mver.add(MClean);
        mejecucion.add(CMIStep);
        mejecucion.add(CMITimer);
        mejecucion.addSeparator();
        mejecucion.add(MTimer);
        MTimer.add(CMIVeryFast);
        MTimer.add(CMIFast);
        MTimer.add(CMIMedium);
        MTimer.add(CMISlow);
        mabout.add(MAbout);
        MMenu.add(mfile);
        MMenu.add(mver);
        MMenu.add(mejecucion);
        MMenu.add(mabout);
        MOpen.addActionListener(new ControladorMenuItem(MOpen));
        MExit.addActionListener(new ControladorMenuItem(MExit));
        MClean.addActionListener(new ControladorMenuItem(MClean));
        MAbout.addActionListener(new ControladorMenuItem(MAbout));
        CMILegend.addItemListener(new ControladorCheckBox(CMILegend));
        CMIStep.addItemListener(new ControladorCheckBox(CMIStep));
        CMITimer.addItemListener(new ControladorCheckBox(CMITimer));
        CMIVeryFast.addItemListener(new ControladorCheckBox(CMIVeryFast));
        CMIFast.addItemListener(new ControladorCheckBox(CMIFast));
        CMIMedium.addItemListener(new ControladorCheckBox(CMIMedium));
        CMISlow.addItemListener(new ControladorCheckBox(CMISlow));

        // ventana de abrir archivos
        FDOpen = new FileDialog(this, "Open graph file", FileDialog.LOAD);

        // ventana de acerca
        DAbout = new Dialog(this, "About AI-Search Algorithms");
        DAbout.setLayout(new GridLayout(12, 1));
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        width = 400;
        height = 250;
        x = (screen.width - width) / 2;
        y = (screen.height - height) / 2;
        DAbout.setBounds(x, y, width, height);
        DAbout.setResizable(false);
        DAbout.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DAbout.setVisible(false);
            }
        });

        // incorpora los elementos al dialog
        DAbout.add(new Label(""));
        DAbout.add(new Label(" AI-Search Algorithm Animation Software"));
        DAbout.add(new Label(""));
        DAbout.add(new Label(""));

        // lista de seleccion
        CSearch = new Choice();
        CSearch.addItem(" Breadth-First Search ");
        CSearch.addItem(" Depth-First Search ");
        CSearch.addItem(" Simple Hill Climbing ");
        CSearch.addItem(" Steepest Ascent Hill Climbing ");
        CSearch.addItem(" Best First Search ");
        CSearch.addItem(" A* Search ");
        CSearch.addItemListener(new ControladorChoice(CSearch));
        CSearch.setEnabled(false);

        // botones
        BRun = new Button(" Fine step ");
        BRestart = new Button(" Reset search");
        BRun.setEnabled(false);
        BRestart.setEnabled(false);
        BRun.addActionListener(new ControladorButton(BRun));
        BRestart.addActionListener(new ControladorButton(BRestart));

        // area de texto
        TAInformation = new TextArea("", 5, 70, TextArea.SCROLLBARS_VERTICAL_ONLY);
        TAInformation.setEditable(false);
        TAInformation.setBackground(Color.white);

        // paneles contenedores
        PNorte = new Panel();
        PNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
        PNorte.setBackground(Color.lightGray);
        PNorte.add(CSearch);
        PNorte.add(BRun);
        PNorte.add(BRestart);
        PSur = new Panel();
        PSur.setLayout(new BorderLayout());
        PSur.setBackground(Color.lightGray);
        PSur.add("North", new Label("Message panel"));
        PSur.add("Center", TAInformation);

        // componente grafo extiende de panel
        grafo = new Graph();
        grafo.setBackground(new Color(90, 138, 212));
        // incorpora componentes a la venta principal
        this.setMenuBar(MMenu);
        this.add("North", PNorte);
        this.add("Center", grafo);
        this.add("South", PSur);

        // muestra la ventana principal
        this.setVisible(true);

        if (graphPathFile != null) {
            loadGraph(graphPathFile.getAbsolutePath());
        }
    }

    // clases para controlar eventos
    class ControladorMenuItem implements ActionListener {

        MenuItem mi;

        ControladorMenuItem(MenuItem mip) {
            mi = mip;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (mi.equals(MOpen)) {
                FDOpen.setVisible(true);
                if (FDOpen.getFile() != null) {
                    String graphPathFile = FDOpen.getDirectory() + FDOpen.getFile();
                    setTitle("AI-Search Algorithms - " + FDOpen.getFile());
                    loadGraph(graphPathFile);
                }
            } else if (mi.equals(MClean)) {
                TAInformation.setText("");
            } else if (mi.equals(MAbout)) {
                DAbout.setVisible(true);
            } else if (mi.equals(MExit)) {
                System.exit(0);
            }
        }
    }

    void loadGraph(String graphPathFile) {
        try {
            // carga el grafo del archivo
            grafo.loadGraph(graphPathFile);
            TAInformation.append((new Date()).toString() + ": Graph file successfully loaded.\n");
            // por defecto primer algoritmo de busqueda
            CSearch.select(0);
            bfs = null;
            dfs = null;
            shc = null;
            sahc = null;
            bf = null;
            ae = null;
            bfs = new BreadthFirstSearch(grafo);

            TAInformation.append("\n" + (new Date()).toString() + ": Breadth-First Search algorithm selected.\n");
            // activa los controles
            CSearch.setEnabled(true);
            BRun.setEnabled(true);
            BRestart.setEnabled(true);
            // redibuja el grafo
            grafo.repaint();
        } catch (Exception exception) {
            CSearch.setEnabled(false);
            BRun.setEnabled(false);
            BRestart.setEnabled(false);
            TAInformation.append((new Date()).toString() + ": Error reading graph from file.\n");
        }
    }

    class ControladorChoice implements ItemListener {
        Choice c;
        ControladorChoice(Choice cp) {
            c = cp;
        }
        @Override
        public void itemStateChanged(ItemEvent evt) {
            // resetea el grafo y los algoritmos
            grafo.reset();
            bfs = null;
            dfs = null;
            sahc = null;
            shc = null;
            bf = null;
            ae = null;
            // establece el algoritmo seleccionado
            switch (c.getSelectedIndex()) {
                case 0:
                    bfs = new BreadthFirstSearch(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": Breadth-First Search algorithm selected.\n");
                    break;
                case 1:
                    dfs = new DepthFirstSearch(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": Depth-First Search algorithm selected.\n");
                    break;
                case 2:
                    shc = new SimpleHillClimbing(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": Simple Hill Climbing algorithm selected.\n");
                    break;
                case 3:
                    sahc = new SteepestAscentHillClimbing(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": Steepest Ascent Hill Climbing algorithm selected.\n");
                    break;
                case 4:
                    bf = new BestFirstSearch(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": Best First Search algorithm selected.\n");
                    break;
                case 5:
                    ae = new AStar(grafo);
                    TAInformation.append("\n" + (new Date()).toString() + ": A* Search algorithm selected.\n");
                    break;
            }
            // redibuja el grafo
            grafo.repaint();
            // activa el boton de ejecucion
            BRun.setEnabled(true);
        }

    }

    class ControladorCheckBox implements ItemListener {

        CheckboxMenuItem cmi;

        ControladorCheckBox(CheckboxMenuItem cmip) {
            cmi = cmip;
        }

        @Override
        public void itemStateChanged(ItemEvent evt) {
            if (cmi.equals(CMILegend)) {
                grafo.setVerLeyenda(cmi.getState());
                grafo.repaint();
            } else if (cmi.equals(CMITimer)) {
                CMITimer.setState(true);
                MTimer.setEnabled(true);
                CMIStep.setState(false);
                BRun.setLabel("Start");
            } else if (cmi.equals(CMIStep)) {
                CMIStep.setState(true);
                CMITimer.setState(false);
                MTimer.setEnabled(false);
                BRun.setLabel("Next step");
            } else if (cmi.equals(CMIVeryFast)) {
                CMIVeryFast.setState(true);
                CMIFast.setState(false);
                CMIMedium.setState(false);
                CMISlow.setState(false);
                grafo.setTemporizador(0);
            } else if (cmi.equals(CMIFast)) {
                CMIVeryFast.setState(false);
                CMIFast.setState(true);
                CMIMedium.setState(false);
                CMISlow.setState(false);
                grafo.setTemporizador(100);
            } else if (cmi.equals(CMIMedium)) {
                CMIVeryFast.setState(false);
                CMIFast.setState(false);
                CMIMedium.setState(true);
                CMISlow.setState(false);
                grafo.setTemporizador(500);
            } else if (cmi.equals(CMISlow)) {
                CMIVeryFast.setState(false);
                CMIFast.setState(false);
                CMIMedium.setState(false);
                CMISlow.setState(true);
                grafo.setTemporizador(1000);
            }
        }
    }

    class ControladorButton implements ActionListener {

        Button b;

        ControladorButton(Button bp) {
            b = bp;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String cadena;
            String[] estadisticas;
            if (b.equals(BRun)) {
                switch (CSearch.getSelectedIndex()) {
                    case 0:
                        if (CMIStep.getState()) {
                            // muestra ejecucion
                            cadena = bfs.getCola();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            if (!bfs.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString() + ": Breadth-First Search stadicstics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            bfs.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString() + ": Breadth-First Search stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                    case 1:
                        if (CMIStep.getState()) {
                            cadena = dfs.getPila();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            if (!dfs.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString() + ": Depth-First Search algorithm stadistics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            dfs.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString() + ": Depth-First Search algorithm stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                    case 2:
                        if (CMIStep.getState()) {
                            if (!shc.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString() + ": Simple Hill Climbing algorithm stadistics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            shc.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString() + ": Simple Hill Climbing algorithm stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                    case 3:
                        if (CMIStep.getState()) {
                            if (!sahc.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString()
                                        + ": Steepest Ascent Hill Climbing algorithm stadistics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            sahc.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString()
                                    + ": Steepest Ascent Hill Climbing algorithm stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                    case 4:
                        if (CMIStep.getState()) {
                            cadena = bf.getAbierta();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            cadena = bf.getCerrada();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            if (!bf.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString() + ": Best First Search algorithm stadistics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            bf.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString() + ": Best First Search algorithm stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                    case 5:
                        if (CMIStep.getState()) {
                            cadena = ae.getAbierta();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            cadena = ae.getCerrada();
                            if (!cadena.isEmpty()) {
                                TAInformation.append((new Date()).toString() + ": " + cadena + "\n");
                            }
                            if (!ae.step()) {
                                // muestra estatidsticas
                                estadisticas = grafo.getEstadisticas();
                                TAInformation.append((new Date()).toString() + ": A* Search algorithm stadistics.\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                                TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                                // desactiva el boton de ejecucion
                                BRun.setEnabled(false);
                            }
                        } else {
                            ae.run();
                            // muestra estatidsticas
                            estadisticas = grafo.getEstadisticas();
                            TAInformation.append((new Date()).toString() + ": A* Search algorithm stadistics.\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[0] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[1] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[2] + "\n");
                            TAInformation.append((new Date()).toString() + ": " + estadisticas[3] + "\n");
                            // desactiva el boton de ejecucion
                            BRun.setEnabled(false);
                        }
                        break;
                }
                grafo.repaint();
            }
            if (b.equals(BRestart)) {
                grafo.reset();
                switch (CSearch.getSelectedIndex()) {
                    case 0:
                        bfs.reset();
                        TAInformation.append("\n" + (new Date()).toString() + ": Breadth-First Search algorithm restarted.\n");
                        break;
                    case 1:
                        dfs.reset();
                        TAInformation.append("\n" + (new Date()).toString() + ": Depth-First Search algorithm restarted.\n");
                        break;
                    case 2:
                        shc.reset();
                        TAInformation.append("\n" + (new Date()).toString() + ": Simple Hill Climbing algorithm restarted.\n");
                        break;
                    case 3:
                        sahc.reset();
                        TAInformation.append("\n" + (new Date()).toString()
                                + ": Steepest Ascent Hill Climbing algorithm restarted.\n");
                        break;
                    case 4:
                        bf.reset();
                        TAInformation.append("\n" + (new Date()).toString() + ": Best First Search algorithm restarted.\n");
                        break;
                    case 5:
                        ae.reset();
                        TAInformation.append("\n" + (new Date()).toString() + ": A* Search algorithm restarted.\n");
                        break;
                }
                grafo.repaint();
                // activa el boton de ejecucion
                BRun.setEnabled(true);
            }
        }
    }
}
