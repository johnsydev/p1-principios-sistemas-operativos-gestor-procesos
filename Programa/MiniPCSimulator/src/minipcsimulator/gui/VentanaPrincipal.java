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

    // Colores de botones oscurecidos para optimizar el contraste con texto blanco
    private static final Color BLUE = new Color(30, 95, 165);
    private static final Color GREEN = new Color(30, 130, 80);
    private static final Color RED = new Color(160, 45, 55);
    private static final Color CYAN = new Color(35, 120, 135);
    private static final Color ORANGE = new Color(175, 105, 35);
    private static final Color GRAY = new Color(50, 55, 65);

    // Componentes de la interfaz
    private JButton btnSeleccionar, btnCargar, btnPasoAPaso, btnEjecutar, btnLimpiar;
    private JButton btnAbrirConfig;
    private JSpinner spTamanoMemoria, spLimiteKernel, spTamanoDisco, spTamanoVirtual;
    private JTable tablaInstrucciones;
    private DefaultTableModel modeloTablaInstrucciones;
    private JTable tablaMemoria;
    private DefaultTableModel modeloTablaMemoria;
    private JTextField txtPC, txtIR, txtAC, txtAX, txtBX, txtCX, txtDX;
    private JLabel lblEstadoBCP, lblProcessID;

    // Estado del botón de configuración
    private boolean configuracionHabilitada = true;

    public VentanaPrincipal() {
        initComponents();
    }

    // Inicializa la ventana principal
    private void initComponents() {
        setTitle("Mini PC Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 720);
        setMinimumSize(new Dimension(1100, 650));
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

        JLabel estado = new JLabel("VERSIÓN 1.0");
        estado.setForeground(TEXT_SECONDARY);
        estado.setFont(new Font("SansSerif", Font.BOLD, 12));

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
        panelPrincipal.setPreferredSize(new Dimension(220, 0));

        JLabel titulo = crearTituloCentrado("CONTROLES");
        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(14));

        btnSeleccionar = crearBoton("Seleccionar archivo .asm", BLUE);
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

        panelPrincipal.add(Box.createVerticalStrut(22));

        JSeparator separador = new JSeparator();
        separador.setForeground(BORDER);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        panelPrincipal.add(separador);
        panelPrincipal.add(Box.createVerticalStrut(18));

        // Botón para abrir el diálogo modal de configuración
        btnAbrirConfig = crearBoton("⚙ Configuración", GRAY);
        btnAbrirConfig.addActionListener(e -> abrirDialogoConfiguracion());
        panelPrincipal.add(btnAbrirConfig);

        return panelPrincipal;
    }

    /**
     * PENDIENTE: Muestra la ventana flotante modal de configuración de memoria.
     * Obliga al usuario a Guardar o Cancelar sin poder cerrar libremente la ventana.
     */
    private void abrirDialogoConfiguracion() {
        JDialog dialogConfig = new JDialog(this, "Configuración de Sistema", true);
        dialogConfig.setSize(340, 420);
        dialogConfig.setResizable(false);
        dialogConfig.setLocationRelativeTo(this);
        
        // Bloquear el botón 'X' de la ventana para forzar la toma de decisiones por botones
        dialogConfig.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panelModal = new JPanel(new BorderLayout(10, 10));
        panelModal.setBackground(BG_PANEL);
        panelModal.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Inicialización de Spinners dentro del modal con los valores actuales del sistema
        spTamanoMemoria = new JSpinner(new SpinnerNumberModel(SystemConfig.getMemorySize(), SystemConfig.MEMORY_SIZE_MIN, SystemConfig.MEMORY_SIZE_MAX, 16));
        spLimiteKernel = new JSpinner(new SpinnerNumberModel(SystemConfig.getUserMemoryStart(), SystemConfig.USER_MEMORY_START_MIN, SystemConfig.MEMORY_SIZE_MAX - 16, 8));
        spTamanoDisco = new JSpinner(new SpinnerNumberModel(512, 128, 2048, 32));
        spTamanoVirtual = new JSpinner(new SpinnerNumberModel(/*SystemConfig.getVirtualMemorySize() */ 64, 16, 256, 16)); //PENDIENTE

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        cardRAM.add(lblRAM, gbc);

        gbc.gridy = 1;
        cardRAM.add(spTamanoMemoria, gbc);

        gbc.gridy = 2;
        cardRAM.add(lblKernel, gbc);

        gbc.gridy = 3;
        cardRAM.add(spLimiteKernel, gbc);

        gbc.gridy = 4;
        cardRAM.add(lblDisco, gbc);

        gbc.gridy = 5;
        cardRAM.add(spTamanoDisco, gbc);

        gbc.gridy = 6;
        cardRAM.add(lblVirtual, gbc);

        gbc.gridy = 7;
        cardRAM.add(spTamanoVirtual, gbc);

        // Panel de botones (Guardar / Cancelar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBotonSecundario(btnCancelar);
        btnCancelar.addActionListener(e -> dialogConfig.dispose());

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(100, 32));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.putClientProperty(
                FlatClientProperties.STYLE,
                "background: " + convertirColor(BLUE) + "; foreground: #FFFFFF; font: bold;"
        );

        btnGuardar.addActionListener(e -> {
            // Aplicar configuraciones globalmente a SystemConfig
            SystemConfig.setMemorySize((int) spTamanoMemoria.getValue());
            SystemConfig.setUserMemoryStart((int) spLimiteKernel.getValue());
            
            mostrarInfo("Configuración guardada correctamente.");
            dialogConfig.dispose();
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelModal.add(cardRAM, BorderLayout.CENTER);
        panelModal.add(panelBotones, BorderLayout.SOUTH);

        dialogConfig.setContentPane(panelModal);
        dialogConfig.setVisible(true);
    }

    // Crea el panel central con las tablas de programa y memoria
    private JPanel crearPanelMemoria() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setBackground(BG_MAIN);

        JPanel cardPrograma = crearCard();
        cardPrograma.setLayout(new BorderLayout(0, 12));

        JPanel encabezadoPrograma = crearEncabezadoSeccion("PROGRAMA CARGADO", "ASM / BINARIO");
        cardPrograma.add(encabezadoPrograma, BorderLayout.NORTH);

        String[] colsInst = {"Instrucción ASM", "Código Binario"};
        modeloTablaInstrucciones = new DefaultTableModel(colsInst, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInstrucciones = crearTabla(modeloTablaInstrucciones);

        JScrollPane scrollInstrucciones = new JScrollPane(tablaInstrucciones);
        scrollInstrucciones.setBorder(BorderFactory.createEmptyBorder());
        cardPrograma.add(scrollInstrucciones, BorderLayout.CENTER);

        JPanel cardRAM = crearCard();
        cardRAM.setLayout(new BorderLayout(0, 12));

        JPanel encabezadoRAM = crearEncabezadoSeccion("MEMORIA PRINCIPAL", "RAM");
        cardRAM.add(encabezadoRAM, BorderLayout.NORTH);

        String[] colsMem = {"Posición", "Instrucción", "Valor en Memoria"};
        modeloTablaMemoria = new DefaultTableModel(colsMem, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMemoria = crearTabla(modeloTablaMemoria);

        JScrollPane scrollMemoria = new JScrollPane(tablaMemoria);
        scrollMemoria.setBorder(BorderFactory.createEmptyBorder());
        cardRAM.add(scrollMemoria, BorderLayout.CENTER);

        panel.add(cardPrograma);
        panel.add(cardRAM);

        return panel;
    }

    // Crea el panel derecho para BCP y registros del CPU
    private JPanel crearPanelBCP() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(BG_PANEL);
        panelPrincipal.setBorder(new EmptyBorder(16, 14, 16, 14));
        panelPrincipal.setPreferredSize(new Dimension(330, 0));

        JLabel titulo = crearTituloCentrado("PROCESO");
        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(14));

        JPanel cardProceso = crearCard();
        cardProceso.setLayout(new BorderLayout(8, 8));
        cardProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        lblProcessID = new JLabel("Sin proceso cargado");
        lblProcessID.setForeground(TEXT);
        lblProcessID.setFont(new Font("SansSerif", Font.BOLD, 17));

        lblEstadoBCP = new JLabel("ESTADO: ESPERANDO ARCHIVO");
        lblEstadoBCP.setForeground(ORANGE);
        lblEstadoBCP.setFont(new Font("SansSerif", Font.BOLD, 11));

        cardProceso.add(lblProcessID, BorderLayout.NORTH);
        cardProceso.add(lblEstadoBCP, BorderLayout.SOUTH);

        panelPrincipal.add(cardProceso);
        panelPrincipal.add(Box.createVerticalStrut(18));

        JLabel tituloCPU = crearTituloCentrado("REGISTROS CPU");
        panelPrincipal.add(tituloCPU);
        panelPrincipal.add(Box.createVerticalStrut(12));

        // Panel de registros con GridLayout (3 filas x 2 columnas)
        JPanel registros = new JPanel(new GridLayout(3, 2, 8, 8));
        registros.setOpaque(false);
        registros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

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
        cardIR.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        panelPrincipal.add(registros);
        panelPrincipal.add(Box.createVerticalStrut(8));
        panelPrincipal.add(cardIR);

        return panelPrincipal;
    }

    // Utilidad para crear tarjetas visuales contenedoras
    private JPanel crearCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(14, 14, 14, 14)
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
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setForeground(TEXT_SECONDARY);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 11));

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(lblDescripcion, BorderLayout.EAST);

        return panel;
    }

    // Crea un botón principal estilizado con colores optimizados
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(190, 38));
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
    private void estilizarBotonSecundario(JButton boton) {
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
        tabla.setRowHeight(30);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(BG_CARD);
        tabla.setForeground(TEXT);
        tabla.setSelectionBackground(new Color(45, 90, 140));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tabla.getTableHeader().setPreferredSize(new Dimension(0, 34));
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

                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return tabla;
    }

    // Crea un campo de texto para los registros del CPU
    private JTextField crearCampoRegistro() {
        JTextField tf = new JTextField("0");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tf.setFont(new Font("Monospaced", Font.BOLD, 14));

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
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BG_CARD);

        JLabel label = new JLabel(nombre);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));

        panel.add(label, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    // Crea etiquetas de título centradas para los paneles principales
    private JLabel crearTituloCentrado(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
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
     * Actualiza la tabla de instrucciones limpiando los datos previos e ingresando nuevos.
     * @param listaInstrucciones Una lista de arreglos de objetos donde cada posición es una columna: [Instrucción ASM, Código Binario]
     */
    public void actualizarTablaInstrucciones(List<Object[]> listaInstrucciones) {
        modeloTablaInstrucciones.setRowCount(0);
        for (Object[] fila : listaInstrucciones) {
            modeloTablaInstrucciones.addRow(fila);
        }
    }

    /**
     * Actualiza la tabla de memoria RAM, ingresa los datos y selecciona/resalta automáticamente la fila indicada.
     * @param listaMemoria Una lista de arreglos de objetos donde cada posición es: [Posición, Instrucción ASM, Valor en Memoria]
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
     * Limpia la vista de la interfaz, reseteando las tablas y los registros del CPU a sus valores iniciales.
     */
    public void limpiarVista() {
        modeloTablaInstrucciones.setRowCount(0);
        modeloTablaMemoria.setRowCount(0);
        tablaMemoria.clearSelection();
        setEstadoBCP("ESPERANDO ARCHIVO");

        setPC(0);
        lblProcessID.setText("Sin proceso cargado");
        setIR("0");
        setAC(0);
        setAX(0);
        setBX(0);
        setCX(0);
        setDX(0);
    }

    // Getters y Setters
    public JButton getBtnSeleccionar() { return btnSeleccionar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JButton getBtnPasoAPaso() { return btnPasoAPaso; }
    public JButton getBtnEjecutar() { return btnEjecutar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnAbrirConfig() { return btnAbrirConfig; }

    // Métodos delegados para mantener la compatibilidad con MiniPCController
    public JButton getBtnAplicarConfig() { return btnAbrirConfig; }
    public int getTamanoMemoriaSeleccionado() { return spTamanoMemoria != null ? (int) spTamanoMemoria.getValue() : SystemConfig.getMemorySize(); }
    public int getLimiteKernelSeleccionado() { return spLimiteKernel != null ? (int) spLimiteKernel.getValue() : SystemConfig.getUserMemoryStart(); }

    public DefaultTableModel getModeloTablaInstrucciones() { return modeloTablaInstrucciones; }
    public DefaultTableModel getModeloTablaMemoria() { return modeloTablaMemoria; }
    public JTable getTablaInstrucciones() { return tablaInstrucciones; }
    public JTable getTablaMemoria() { return tablaMemoria; }

    public void setEstadoBCP(String estado) { lblEstadoBCP.setText("ESTADO: " + estado); }
    public void setPC(int valor) { txtPC.setText(String.valueOf(valor)); }
    public void setProcessID(int pid) { lblProcessID.setText("PID " + pid); }
    public void setIR(String valor) { txtIR.setText(valor); }
    public void setAC(int valor) { txtAC.setText(String.valueOf(valor)); }
    public void setAX(int valor) { txtAX.setText(String.valueOf(valor)); }
    public void setBX(int valor) { txtBX.setText(String.valueOf(valor)); }
    public void setCX(int valor) { txtCX.setText(String.valueOf(valor)); }
    public void setDX(int valor) { txtDX.setText(String.valueOf(valor)); }
}