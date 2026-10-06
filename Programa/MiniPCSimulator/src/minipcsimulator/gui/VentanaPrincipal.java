package minipcsimulator.gui;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import minipcsimulator.utils.SystemConfig;

public class VentanaPrincipal extends JFrame {

    // Paleta de colores
    private static final Color BG_MAIN = new Color(18, 20, 24);
    private static final Color BG_PANEL = new Color(25, 28, 34);
    private static final Color BG_CARD = new Color(31, 35, 42);
    private static final Color BG_INPUT = new Color(22, 25, 30);

    private static final Color BORDER = new Color(55, 61, 70);
    private static final Color TEXT = new Color(235, 238, 242);
    private static final Color TEXT_SECONDARY = new Color(155, 163, 174);

    // Colores de botones oscurecidos
    private static final Color BLUE = new Color(30, 95, 165);
    private static final Color GREEN = new Color(30, 130, 80);
    private static final Color RED = new Color(160, 45, 55);
    private static final Color CYAN = new Color(35, 120, 135);
    private static final Color ORANGE = new Color(175, 105, 35);
    private static final Color GRAY = new Color(50, 55, 65);
    private static final Color PURPLE = new Color(110, 55, 150);

    // Componentes de la interfaz
    private JButton btnSeleccionar, btnCargar, btnPasoAPaso, btnEjecutar, btnLimpiar;
    private JButton btnEstadisticas, btnAbrirConfig, btnGuardarConfig;
    private JSpinner spTamanoMemoria, spLimiteKernel, spTamanoDisco, spTamanoVirtual;

    private JDialog dialogConfig;
    private JDialog dialogEstadisticas;
    
    // Tablas
    private JTable tablaMemoria, tablaDisco, tablaVirtual, tablaTrabajos;
    private DefaultTableModel modeloTablaMemoria, modeloTablaDisco, modeloTablaVirtual, modeloTablaTrabajos;
    
    // Consola
    private JTextArea txtPantalla;
    private JTextField txtTeclado;
    
    // Registros CPU y BCP
    private JTextField txtPC, txtIR, txtAC, txtAX, txtBX, txtCX, txtDX;
    private JLabel lblEstadoBCP, lblProcessID, lblTicks;

    // Estado del botón de configuración
    private boolean configuracionHabilitada = true;
    private int ticksCount = 0;

    public VentanaPrincipal() {
        initComponents();
    }

    // Inicializa la ventana principal
    private void initComponents() {
        setTitle("Mini PC Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 780);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        JPanel contenedor = new JPanel(new BorderLayout(16, 16));
        contenedor.setBackground(BG_MAIN);
        contenedor.setBorder(new EmptyBorder(18, 18, 18, 18));

        contenedor.add(crearHeader(), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(16, 0));
        contenido.setBackground(BG_MAIN);

        contenido.add(crearPanelControles(), BorderLayout.WEST);
        contenido.add(crearPanelMemoria(), BorderLayout.CENTER);
        contenido.add(crearPanelBCP(), BorderLayout.EAST);

        contenedor.add(contenido, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
    }

    // Crea el panel superior (Header)
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_MAIN);
        header.setPreferredSize(new Dimension(0, 58));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setOpaque(false);
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("MINI PC SIMULATOR");
        titulo.setForeground(TEXT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitulo = new JLabel("Tarea 1 - Principios de Sistemas Operativos");
        subtitulo.setForeground(TEXT_SECONDARY);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(2));
        tituloPanel.add(subtitulo);

        JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        estadoPanel.setOpaque(false);
        
        lblTicks = new JLabel("TICKS: " + ticksCount);
        lblTicks.setForeground(TEXT_SECONDARY);
        lblTicks.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel estado = new JLabel("VERSIÓN 1.0");
        estado.setForeground(TEXT_SECONDARY);
        estado.setFont(new Font("SansSerif", Font.BOLD, 12));

        estadoPanel.add(lblTicks);
        estadoPanel.add(estado);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(estadoPanel, BorderLayout.EAST);

        return header;
    }

    // Crea el panel de controles izquierdo
    private JPanel crearPanelControles() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(BG_PANEL);
        panelPrincipal.setBorder(new EmptyBorder(16, 14, 16, 14));
        panelPrincipal.setPreferredSize(new Dimension(200, 0));

        JLabel titulo = crearTituloCentrado("CONTROLES");
        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(14));

        btnSeleccionar = crearBoton("Seleccionar .asm", BLUE);
        btnCargar = crearBoton("Cargar programa", ORANGE);
        btnPasoAPaso = crearBoton("Paso a paso", CYAN);
        btnEjecutar = crearBoton("Ejecutar todo", GREEN);
        btnLimpiar = crearBoton("Limpiar sistema", RED);

        panelPrincipal.add(btnSeleccionar);
        panelPrincipal.add(Box.createVerticalStrut(8));
        panelPrincipal.add(btnCargar);
        panelPrincipal.add(Box.createVerticalStrut(8));
        panelPrincipal.add(btnPasoAPaso);
        panelPrincipal.add(Box.createVerticalStrut(8));
        panelPrincipal.add(btnEjecutar);
        panelPrincipal.add(Box.createVerticalStrut(8));
        panelPrincipal.add(btnLimpiar);

        panelPrincipal.add(Box.createVerticalStrut(18));

        JSeparator separador = new JSeparator();
        separador.setForeground(BORDER);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        panelPrincipal.add(separador);
        panelPrincipal.add(Box.createVerticalStrut(14));

        // Botón de Estadísticas (Deshabilitado por defecto hasta finalizar la simulación)
        btnEstadisticas = crearBoton("Estadísticas", PURPLE);
        btnEstadisticas.setEnabled(false);
        panelPrincipal.add(btnEstadisticas);

        panelPrincipal.add(Box.createVerticalStrut(8));

        // Botón para abrir el diálogo modal de configuración
        btnAbrirConfig = crearBoton("Configuración", GRAY);
        panelPrincipal.add(btnAbrirConfig);

        return panelPrincipal;
    }

    private void abrirDialogoConfiguracion() {
        dialogConfig = new JDialog(this, "Configuración de Sistema", true);
        dialogConfig.setSize(340, 420);
        dialogConfig.setResizable(false);
        dialogConfig.setLocationRelativeTo(this);
        dialogConfig.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panelModal = new JPanel(new BorderLayout(10, 10));
        panelModal.setBackground(BG_PANEL);
        panelModal.setBorder(new EmptyBorder(16, 16, 16, 16));

        spTamanoMemoria = new JSpinner(new SpinnerNumberModel(SystemConfig.getMemorySize(), SystemConfig.MEMORY_SIZE_MIN, SystemConfig.MEMORY_SIZE_MAX, 16));
        spLimiteKernel = new JSpinner(new SpinnerNumberModel(SystemConfig.getUserMemoryStart(), SystemConfig.USER_MEMORY_START_MIN, SystemConfig.MEMORY_SIZE_MAX - 16, 8));
        spTamanoDisco = new JSpinner(new SpinnerNumberModel(512, 128, 2048, 32));
        spTamanoVirtual = new JSpinner(new SpinnerNumberModel(64, 16, 256, 16));

        estilitarSpinner(spTamanoMemoria);
        estilitarSpinner(spLimiteKernel);
        estilitarSpinner(spTamanoDisco);
        estilitarSpinner(spTamanoVirtual);

        JPanel cardRAM = crearCard();
        cardRAM.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRAM = crearLabel("Tamaño RAM");
        JLabel lblKernel = crearLabel("Espacio Kernel");
        JLabel lblDisco = crearLabel("Tamaño Disco");
        JLabel lblVirtual = crearLabel("Memoria Virtual");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; cardRAM.add(lblRAM, gbc);
        gbc.gridy = 1; cardRAM.add(spTamanoMemoria, gbc);
        gbc.gridy = 2; cardRAM.add(lblKernel, gbc);
        gbc.gridy = 3; cardRAM.add(spLimiteKernel, gbc);
        gbc.gridy = 4; cardRAM.add(lblDisco, gbc);
        gbc.gridy = 5; cardRAM.add(spTamanoDisco, gbc);
        gbc.gridy = 6; cardRAM.add(lblVirtual, gbc);
        gbc.gridy = 7; cardRAM.add(spTamanoVirtual, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        estilitarBotonSecundario(btnCancelar);
        btnCancelar.addActionListener(e -> dialogConfig.dispose());

        btnGuardarConfig = new JButton("Guardar");
        btnGuardarConfig.setPreferredSize(new Dimension(100, 32));
        btnGuardarConfig.setForeground(Color.WHITE);
        btnGuardarConfig.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardarConfig.setFocusPainted(false);
        btnGuardarConfig.setBorderPainted(false);
        btnGuardarConfig.putClientProperty(
                FlatClientProperties.STYLE,
                "background: " + convertirColor(BLUE) + "; foreground: #FFFFFF; font: bold;"
        );

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardarConfig);

        panelModal.add(cardRAM, BorderLayout.CENTER);
        panelModal.add(panelBotones, BorderLayout.SOUTH);

        dialogConfig.setContentPane(panelModal);
    }

    /**
     * Habilita el botón de estadísticas y despliega la ventana emergente modal
     * con la tabla de tiempos por cada proceso ejecutado.
     * @param datosEstadisticas Lista de filas [Proceso/PID, Hora Inicio, Hora Fin, Duración (s)]
     */
    public void mostrarVentanaEstadisticas(List<Object[]> datosEstadisticas) {
        if (btnEstadisticas != null) {
            btnEstadisticas.setEnabled(true);
        }

        dialogEstadisticas = new JDialog(this, "Estadísticas de Ejecución de Procesos", true);
        dialogEstadisticas.setSize(560, 380);
        dialogEstadisticas.setResizable(false);
        dialogEstadisticas.setLocationRelativeTo(this);

        JPanel panelModal = new JPanel(new BorderLayout(12, 12));
        panelModal.setBackground(BG_PANEL);
        panelModal.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Encabezado
        JLabel lblTituloModal = new JLabel("RESUMEN DE TIEMPOS DE EJECUCIÓN");
        lblTituloModal.setForeground(TEXT);
        lblTituloModal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTituloModal.setHorizontalAlignment(SwingConstants.CENTER);

        // Tabla de Estadísticas
        String[] columnasEst = {"Proceso", "Hora Inicio", "Hora Fin", "Duración (s)"};
        DefaultTableModel modeloEstadisticas = new DefaultTableModel(columnasEst, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        if (datosEstadisticas != null) {
            for (Object[] fila : datosEstadisticas) {
                modeloEstadisticas.addRow(fila);
            }
        }

        JTable tablaEstadisticas = crearTabla(modeloEstadisticas);
        JScrollPane scrollEstadisticas = new JScrollPane(tablaEstadisticas);
        scrollEstadisticas.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // Botón Cerrar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelInferior.setOpaque(false);

        JButton btnCerrar = new JButton("Cerrar");
        estilitarBotonSecundario(btnCerrar);
        btnCerrar.addActionListener(e -> dialogEstadisticas.dispose());
        panelInferior.add(btnCerrar);

        panelModal.add(lblTituloModal, BorderLayout.NORTH);
        panelModal.add(scrollEstadisticas, BorderLayout.CENTER);
        panelModal.add(panelInferior, BorderLayout.SOUTH);

        dialogEstadisticas.setContentPane(panelModal);
        dialogEstadisticas.setVisible(true);
    }

    // Crea el panel central con memorias y consola
    private JPanel crearPanelMemoria() {
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(BG_MAIN);

        String[] columnas = {"Posición", "Valor"};

        // 1. Memoria Principal (RAM)
        JPanel cardRAM = crearCard();
        cardRAM.setLayout(new BorderLayout(0, 10));
        cardRAM.add(crearEncabezadoSeccion("MEMORIA RAM", ""), BorderLayout.NORTH);

        modeloTablaMemoria = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMemoria = crearTabla(modeloTablaMemoria);
        JScrollPane scrollRAM = new JScrollPane(tablaMemoria);
        scrollRAM.setBorder(BorderFactory.createEmptyBorder());
        cardRAM.add(scrollRAM, BorderLayout.CENTER);

        // 2. Almacenamiento Secundario (Disco y Memoria Virtual anidados)
        JPanel panelSecundarioArriba = new JPanel(new GridLayout(1, 2, 8, 0));
        panelSecundarioArriba.setOpaque(false);

        // Subpanel Disco para Archivos
        JPanel cardDisco = crearCard();
        cardDisco.setLayout(new BorderLayout(0, 10));
        cardDisco.add(crearEncabezadoSeccion("DISCO", ""), BorderLayout.NORTH);

        modeloTablaDisco = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDisco = crearTabla(modeloTablaDisco);
        JScrollPane scrollDisco = new JScrollPane(tablaDisco);
        scrollDisco.setBorder(BorderFactory.createEmptyBorder());
        cardDisco.add(scrollDisco, BorderLayout.CENTER);

        // Subpanel Memoria Virtual
        JPanel cardVirtual = crearCard();
        cardVirtual.setLayout(new BorderLayout(0, 10));
        cardVirtual.add(crearEncabezadoSeccion("MEMORIA VIRTUAL", ""), BorderLayout.NORTH);

        modeloTablaVirtual = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaVirtual = crearTabla(modeloTablaVirtual);
        JScrollPane scrollVirtual = new JScrollPane(tablaVirtual);
        scrollVirtual.setBorder(BorderFactory.createEmptyBorder());
        cardVirtual.add(scrollVirtual, BorderLayout.CENTER);

        panelSecundarioArriba.add(cardDisco);
        panelSecundarioArriba.add(cardVirtual);

        // Subpanel Consola
        JPanel cardConsola = crearCard();
        cardConsola.setLayout(new BorderLayout(0, 8));
        cardConsola.add(crearEncabezadoSeccion("CONSOLA", ""), BorderLayout.NORTH);

        txtPantalla = new JTextArea();
        txtPantalla.setEditable(false);
        txtPantalla.setBackground(BG_INPUT);
        txtPantalla.setForeground(new Color(78, 201, 176));
        txtPantalla.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtPantalla.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scrollPantalla = new JScrollPane(txtPantalla);
        scrollPantalla.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        txtTeclado = new JTextField();
        txtTeclado.setBackground(BG_INPUT);
        txtTeclado.setForeground(TEXT);
        txtTeclado.setCaretColor(TEXT);
        txtTeclado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtTeclado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JPanel panelTeclado = new JPanel(new BorderLayout(6, 0));
        panelTeclado.setOpaque(false);
        JLabel lblINT09 = crearLabel(">");
        panelTeclado.add(lblINT09, BorderLayout.WEST);
        panelTeclado.add(txtTeclado, BorderLayout.CENTER);

        cardConsola.add(scrollPantalla, BorderLayout.CENTER);
        cardConsola.add(panelTeclado, BorderLayout.SOUTH);

        JPanel panelDerechoCentral = new JPanel(new GridBagLayout());
        panelDerechoCentral.setOpaque(false);

        GridBagConstraints gbcDerecho = new GridBagConstraints();
        gbcDerecho.fill = GridBagConstraints.BOTH;
        gbcDerecho.gridx = 0;

        // Disco y Memoria Virtual
        gbcDerecho.gridy = 0;
        gbcDerecho.weightx = 1.0;
        gbcDerecho.weighty = 0.70;
        gbcDerecho.insets = new Insets(0, 0, 6, 0);
        panelDerechoCentral.add(panelSecundarioArriba, gbcDerecho);

        // Consola
        gbcDerecho.gridy = 1;
        gbcDerecho.weightx = 1.0;
        gbcDerecho.weighty = 0.30;
        gbcDerecho.insets = new Insets(6, 0, 0, 0);
        panelDerechoCentral.add(cardConsola, gbcDerecho);

        // Layout general del panel central con 45% RAM, 55% almacenamiento secundario y consola
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(0, 0, 0, 6);
        panelCentral.add(cardRAM, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.55;
        gbc.insets = new Insets(0, 6, 0, 0);
        panelCentral.add(panelDerechoCentral, gbc);

        return panelCentral;
    }

    // Crea el panel derecho para BCP, registros del CPU y Lista de Trabajos
    private JPanel crearPanelBCP() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(BG_PANEL);
        panelPrincipal.setBorder(new EmptyBorder(16, 14, 16, 14));
        panelPrincipal.setPreferredSize(new Dimension(320, 0));

        JLabel titulo = crearTituloCentrado("PROCESO EN EJECUCIÓN");
        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JPanel cardProceso = crearCard();
        cardProceso.setLayout(new BorderLayout(8, 8));
        cardProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        lblProcessID = new JLabel("Sin proceso cargado");
        lblProcessID.setForeground(TEXT);
        lblProcessID.setFont(new Font("SansSerif", Font.BOLD, 15));

        lblEstadoBCP = new JLabel("ESTADO: ESPERANDO ARCHIVO");
        lblEstadoBCP.setForeground(ORANGE);
        lblEstadoBCP.setFont(new Font("SansSerif", Font.BOLD, 11));

        cardProceso.add(lblProcessID, BorderLayout.NORTH);
        cardProceso.add(lblEstadoBCP, BorderLayout.SOUTH);

        panelPrincipal.add(cardProceso);
        panelPrincipal.add(Box.createVerticalStrut(12));

        JLabel tituloCPU = crearTituloCentrado("REGISTROS CPU");
        panelPrincipal.add(tituloCPU);
        panelPrincipal.add(Box.createVerticalStrut(8));

        JPanel registros = new JPanel(new GridLayout(3, 2, 6, 6));
        registros.setOpaque(false);
        registros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        txtPC = crearCampoRegistro();
        txtAC = crearCampoRegistro();
        txtAX = crearCampoRegistro();
        txtBX = crearCampoRegistro();
        txtCX = crearCampoRegistro();
        txtDX = crearCampoRegistro();

        registros.add(crearRegistroCard("PC", txtPC));
        registros.add(crearRegistroCard("AC", txtAC));
        registros.add(crearRegistroCard("AX", txtAX));
        registros.add(crearRegistroCard("BX", txtBX));
        registros.add(crearRegistroCard("CX", txtCX));
        registros.add(crearRegistroCard("DX", txtDX));

        // Registro IR de ancho completo
        txtIR = crearCampoRegistro();
        JPanel cardIR = crearRegistroCard("IR (Instruction Register)", txtIR);
        cardIR.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        panelPrincipal.add(registros);
        panelPrincipal.add(Box.createVerticalStrut(6));
        panelPrincipal.add(cardIR);
        panelPrincipal.add(Box.createVerticalStrut(14));

        JLabel tituloTrabajos = crearTituloCentrado("LISTA DE TRABAJOS");
        panelPrincipal.add(tituloTrabajos);
        panelPrincipal.add(Box.createVerticalStrut(8));

        JPanel cardTrabajos = crearCard();
        cardTrabajos.setLayout(new BorderLayout(0, 8));

        String[] colsTrabajos = {"PID", "Archivo", "Estado"};
        modeloTablaTrabajos = new DefaultTableModel(colsTrabajos, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTrabajos = crearTabla(modeloTablaTrabajos);

        JScrollPane scrollTrabajos = new JScrollPane(tablaTrabajos);
        scrollTrabajos.setBorder(BorderFactory.createEmptyBorder());

        cardTrabajos.add(scrollTrabajos, BorderLayout.CENTER);
        panelPrincipal.add(cardTrabajos);

        return panelPrincipal;
    }

    private JPanel crearCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );
        return card;
    }

    // Crea el encabezado para las secciones de las tablas
    private JPanel crearEncabezadoSeccion(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(TEXT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setForeground(TEXT_SECONDARY);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 10));

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(lblDescripcion, BorderLayout.EAST);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(170, 38));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.putClientProperty(
                FlatClientProperties.STYLE,
                "background: " + convertirColor(color) + "; " +
                "foreground: #FFFFFF; " +
                "font: bold;"
        );

        return boton;
    }

    // Estiliza un botón secundario
    private void estilitarBotonSecundario(JButton boton) {
        boton.setPreferredSize(new Dimension(90, 32));
        boton.setFocusPainted(false);

        boton.putClientProperty(
                FlatClientProperties.STYLE,
                "background: #252B33; " +
                "foreground: #DDE2E8; " +
                "borderWidth: 1; " +
                "borderColor: #3B434E;"
        );
    }

    // Aplica estilos al JSpinner
    private void estilitarSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(190, 30));
        spinner.putClientProperty(
                FlatClientProperties.STYLE,
                "background: #16191E; " +
                "foreground: #E8EBEF; " +
                "borderColor: #414954;"
        );
    }

    // Configura y crea una tabla estándar con filas alternas
    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(BG_CARD);
        tabla.setForeground(TEXT);
        tabla.setSelectionBackground(new Color(45, 90, 140));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tabla.getTableHeader().setPreferredSize(new Dimension(0, 30));
        tabla.getTableHeader().setBackground(new Color(37, 42, 50));
        tabla.getTableHeader().setForeground(TEXT_SECONDARY);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tabla.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(BG_CARD);
                    } else {
                        c.setBackground(new Color(28, 32, 38));
                    }
                    c.setForeground(TEXT);
                }

                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return tabla;
    }

    private JTextField crearCampoRegistro() {
        JTextField tf = new JTextField("0");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tf.setFont(new Font("Monospaced", Font.BOLD, 13));

        tf.putClientProperty(
                FlatClientProperties.STYLE,
                "background: #161A20; " +
                "foreground: #4EC9B0; " +
                "caretColor: #4EC9B0;"
        );

        return tf;
    }

    // Crea el contenedor visual para un registro de CPU
    private JPanel crearRegistroCard(String nombre, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBackground(BG_CARD);

        JLabel label = new JLabel(nombre);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));

        panel.add(label, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    // Crea etiquetas de título centradas para los paneles principales
    private JLabel crearTituloCentrado(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return label;
    }

    // Convierte un objeto Color a su representación hexadecimal en String
    private String convertirColor(Color color) {
        return String.format(
                "#%02X%02X%02X",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }

    // Muestra un diálogo de error
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Actualiza la tabla de memoria RAM, ingresa los datos y selecciona/resalta automáticamente la fila indicada.
     * @param listaMemoria Una lista de arreglos de objetos donde cada posición es: [Posición, Valor]
     * @param indiceResaltado El índice de la fila que se desea seleccionar (-1 para limpiar la selección)
     */
    public void actualizarTablaMemoria(List<Object[]> listaMemoria, int indiceResaltado) {
        modeloTablaMemoria.setRowCount(0);
        for (Object[] fila : listaMemoria) {
            modeloTablaMemoria.addRow(fila);
        }

        if (indiceResaltado >= 0 && indiceResaltado < tablaMemoria.getRowCount()) {
            tablaMemoria.setRowSelectionInterval(indiceResaltado, indiceResaltado);
            tablaMemoria.scrollRectToVisible(tablaMemoria.getCellRect(indiceResaltado, 0, true));
        } else {
            tablaMemoria.clearSelection();
        }
    }

    /**
     * Actualiza la tabla del Disco secundario.
     * @param listaDisco Lista de arreglos de objetos: [Posición, Valor]
     */
    public void actualizarTablaDisco(List<Object[]> listaDisco) {
        modeloTablaDisco.setRowCount(0);
        for (Object[] fila : listaDisco) {
            modeloTablaDisco.addRow(fila);
        }
    }

    /**
     * Actualiza la tabla de Memoria Virtual.
     * @param listaVirtual Lista de arreglos de objetos: [Posición, Valor]
     */
    public void actualizarTablaMemoriaVirtual(List<Object[]> listaVirtual) {
        modeloTablaVirtual.setRowCount(0);
        for (Object[] fila : listaVirtual) {
            modeloTablaVirtual.addRow(fila);
        }
    }

    /**
     * Actualiza la tabla de la Lista de Trabajos.
     * @param listaTrabajos Lista de arreglos de objetos: [PID, Nombre Archivo, Estado (7 estados)]
     */
    public void actualizarTablaTrabajos(List<Object[]> listaTrabajos) {
        modeloTablaTrabajos.setRowCount(0);
        for (Object[] fila : listaTrabajos) {
            modeloTablaTrabajos.addRow(fila);
        }
    }

    /**
     * Deshabilita los controles de configuración de memoria y kernel para evitar cambios mientras un programa está cargado.
     */
    public void deshabilitarConfiguraciones() {
        configuracionHabilitada = false;
        if (btnAbrirConfig != null) btnAbrirConfig.setEnabled(false);
    }

    /**
     * Habilita los controles de configuración de memoria y kernel.
     */
    public void habilitarConfiguraciones() {
        configuracionHabilitada = true;
        if (btnAbrirConfig != null) btnAbrirConfig.setEnabled(true);
    }

    /**
     * Limpia la vista de la interfaz, reseteando las tablas, la consola y los registros del CPU a sus valores iniciales.
     */
    public void limpiarVista() {
        modeloTablaMemoria.setRowCount(0);
        modeloTablaDisco.setRowCount(0);
        modeloTablaVirtual.setRowCount(0);
        modeloTablaTrabajos.setRowCount(0);
        
        tablaMemoria.clearSelection();
        setEstadoBCP("ESPERANDO ARCHIVO");

        if (txtPantalla != null) txtPantalla.setText("");
        if (txtTeclado != null) txtTeclado.setText("");

        setPC(0);
        lblProcessID.setText("Sin proceso cargado");
        setIR("0");
        setAC(0);
        setAX(0);
        setBX(0);
        setCX(0);
        setDX(0);
        ticksCount = 0;
        lblTicks.setText("TICKS: " + ticksCount);
        lblTicks.setText("TICKS: " + ticksCount);

        if (btnEstadisticas != null) {
            btnEstadisticas.setEnabled(false);
        }
    }

    public void mostrarPanelConfig(boolean mostrar) {
        if (dialogConfig == null) {
            abrirDialogoConfiguracion();
        }
        if (mostrar) {
            dialogConfig.setVisible(true);
        }
    }


    public JButton getBtnSeleccionar() { return btnSeleccionar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JButton getBtnPasoAPaso() { return btnPasoAPaso; }
    public JButton getBtnEjecutar() { return btnEjecutar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnAbrirConfig() { return btnAbrirConfig; }
    public JButton getBtnGuardarConfig() { return btnGuardarConfig; }
    public JButton getBtnEstadisticas() { return btnEstadisticas; }

    public JDialog getDialogConfig() { return dialogConfig; }

    public int getTamanoMemoriaSeleccionado() { return spTamanoMemoria != null ? (int) spTamanoMemoria.getValue() : SystemConfig.getMemorySize(); }
    public int getLimiteKernelSeleccionado() { return spLimiteKernel != null ? (int) spLimiteKernel.getValue() : SystemConfig.getUserMemoryStart(); }
    public int getTamanoDiscoSeleccionado() { return spTamanoDisco != null ? (int) spTamanoDisco.getValue() : SystemConfig.getDiskSize(); }
    public int getTamanoVirtualSeleccionado() { return spTamanoVirtual != null ? (int) spTamanoVirtual.getValue() : SystemConfig.getDiskMemoryVirtualSize(); }

    public DefaultTableModel getModeloTablaMemoria() { return modeloTablaMemoria; }
    public DefaultTableModel getModeloTablaDisco() { return modeloTablaDisco; }
    public DefaultTableModel getModeloTablaVirtual() { return modeloTablaVirtual; }
    public DefaultTableModel getModeloTablaTrabajos() { return modeloTablaTrabajos; }

    public JTable getTablaMemoria() { return tablaMemoria; }
    public JTable getTablaDisco() { return tablaDisco; }
    public JTable getTablaVirtual() { return tablaVirtual; }
    public JTable getTablaTrabajos() { return tablaTrabajos; }

    public JTextArea getTxtPantalla() { return txtPantalla; }
    public JTextField getTxtTeclado() { return txtTeclado; }

    public void setEstadoBCP(String estado) { lblEstadoBCP.setText("ESTADO: " + estado); }
    public void setPC(int valor) { txtPC.setText(String.valueOf(valor)); }
    public void setProcessID(int pid) { lblProcessID.setText("PID " + pid); }
    public void setIR(String valor) { txtIR.setText(valor); }
    public void setAC(int valor) { txtAC.setText(String.valueOf(valor)); }
    public void setAX(int valor) { txtAX.setText(String.valueOf(valor)); }
    public void setAX(String valor) { txtAX.setText(String.valueOf(valor)); }
    public void setBX(int valor) { txtBX.setText(String.valueOf(valor)); }
    public void setCX(int valor) { txtCX.setText(String.valueOf(valor)); }
    public void setDX(int valor) { txtDX.setText(String.valueOf(valor)); }
    public void setDX(String valor) { txtDX.setText(String.valueOf(valor)); }
    public void setTicks(int valor) { ticksCount = valor; lblTicks.setText("TICKS: " + ticksCount); }
}