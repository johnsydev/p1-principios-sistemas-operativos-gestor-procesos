/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minipcsimulator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Map;

import minipcsimulator.gui.VentanaPrincipal;

import minipcsimulator.model.CPU;
import minipcsimulator.model.Dispatcher;
import minipcsimulator.model.Loader;
import minipcsimulator.model.MainMemory;
import minipcsimulator.model.Disk;
import minipcsimulator.model.MemoryRegister;
import minipcsimulator.model.PCB;
import minipcsimulator.model.Process.ProcessState;
import minipcsimulator.model.Process;
import minipcsimulator.model.FileIndex;
import minipcsimulator.model.Job;
import minipcsimulator.model.SystemClock;
import minipcsimulator.model.JobList;
import minipcsimulator.model.ProcessList;
import minipcsimulator.model.Scheduler;
import minipcsimulator.model.InterruptHandler;

import minipcsimulator.services.AsmParser;
import minipcsimulator.services.FileManager;
import minipcsimulator.utils.SystemConfig;

/**
 * Clase MiniPCController que actúa como Kernel del sistema simulado.
 * @author johnsydev
 */
public class MiniPCController {
    //private MiniPCModel modelo;
    private VentanaPrincipal vista;
    private MainMemory memory;
    private Disk disk;
    private CPU cpu;
    private SystemClock systemClock;
    private JobList jobList;
    private ProcessList processList;
    private Scheduler scheduler;
    private InterruptHandler interruptHandler;
    // lista de procesos: [1, 0, 3, ...], los procesos en 0 terminaron y se pueden reemplazar
    // los que tienen número ese es su ID

    /**
     * Constructor de la clase MiniPCController.
     * Inicializa la vista, la memoria principal y el CPU.
     */
    public MiniPCController() {
        this.vista = new VentanaPrincipal();
        this.memory = new MainMemory();
        this.disk = new Disk();
        this.systemClock = new SystemClock();
        this.jobList = new JobList();
        this.processList = new ProcessList(this.memory);
        this.scheduler = new Scheduler(this.jobList, this.processList, this.memory, this.disk, this.systemClock);
        this.interruptHandler = new InterruptHandler(this.memory, this.processList, this.scheduler);
        this.cpu = new CPU(this.memory, interruptHandler);

        actualizarVista();
        agregarListeners();

        // Muestra GUI
        this.vista.setVisible(true);
    }

    private void tick() {
        systemClock.tick();
        vista.setTicks(systemClock.getTicks());
    }
    
    /**
     * Agrega los listeners a los botones de la GUI para manejar las acciones del usuario.
     * Cuando se presiona un botón en la UI, se ejecuta la acción que se defina en esta sección.
     */
    public void agregarListeners() {
        // Botón para seleccionar archivo .asm
        vista.getBtnSeleccionar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArchivos();
            }
        });

        // Botón para cargar el programa en memoria RAM
        vista.getBtnCargar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPrograma();
            }
        });

        // Botón para ejecutar un paso del programa (modo paso a paso)
        vista.getBtnPasoAPaso().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarPasoAPaso();
            }
        });

        // Botón para ejecutar todo el programa de una vez
        vista.getBtnEjecutar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarTodoPrograma();
            }
        });

        // Botón para aplicar las configuraciones de memoria principal y espacio de kernel
        vista.getBtnAplicarConfig().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarConfiguraciones();
            }
        });

        // Botón para reiniciar el sistema, limpiando la memoria y los procesos
        vista.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarSistema();
            }
        });
    }
    
    public void actualizarVista() {
        
    }

    private void procesarArchivo(Map.Entry<ArrayList<String>, ArrayList<String>> fileData) {
        String filePath;
        String fileName;
        ArrayList<String> lines = new ArrayList<>();
        try {
            
            filePath = fileData.getKey().get(0);
            fileName = fileData.getKey().get(1);
            lines = fileData.getValue();
        } catch (Exception exc) {
            vista.mostrarError(exc.getMessage());
            return;
        }
        
        ArrayList<ArrayList<String>> asmArray = new ArrayList<>();
        
        try {
            asmArray = AsmParser.verifySyntax(lines);
        } catch (Exception exc) {
            vista.mostrarError(exc.getMessage());
            return;
        }

        if (asmArray.isEmpty()) {
            vista.mostrarError("El archivo + " + fileName + " está vacío o no contiene instrucciones válidas.");
            return;
        }

        if (asmArray.size() > SystemConfig.getUserMemorySize()) {
            vista.mostrarError("El programa " + fileName + " ingresado es demasiado grande para la memoria disponible. Tamaño máximo permitido: " + SystemConfig.getUserMemorySize() + " instrucciones.");
            return;
        }

        System.out.println("Archivo cargado y verificado correctamente.");
        System.out.println(asmArray);

        // Aquí se hace el trabajo y las instrucciones se cargan, sin RAM aún

        FileIndex fileIndex = new FileIndex(fileName, -1, asmArray.size());
        int startAddress = Loader.loadProgram(lines, asmArray, this.disk, fileIndex); // sin ponerlo en tabla

        Job job = new Job(filePath, fileName, lines, asmArray);
        job.setDiskStartAddress(startAddress);

        this.jobList.addJob(job);
        actualizarVistaListaProcesos();

        vista.deshabilitarConfiguraciones();
        ArrayList<List<Object[]>> diskLists = this.disk.getAllDiskRows();
        vista.actualizarTablaDisco(diskLists.get(0));
        vista.actualizarTablaMemoriaVirtual(diskLists.get(1));
    }

    /**
     * Esto abre el cuadro de diálogo para seleccionar un archivo .asm, lo carga y verifica su sintaxis.
     * Genera el proceso (estado NEW)
     * Genera la tabla de instrucciones en la GUI
     */
    private void seleccionarArchivos() {
        if (this.cpu.getPCB() != null) {
            vista.mostrarError("Ya hay un programa cargado. Debe finalizarlo y limpiar el sistema antes de cargar otro.");
            return;
        }

        List<Map.Entry<ArrayList<String>, ArrayList<String>>> filesData = FileManager.loadFiles();

        for (Map.Entry<ArrayList<String>, ArrayList<String>> fileData : filesData) {
            procesarArchivo(fileData);
        }
    }

    /**
     * Esto carga el programa en la memoria principal (RAM) y actualiza la vista de la memoria.
     * Estado del proceso READY
     */
    private void cargarPrograma() {
        if (!this.scheduler.hasPendingJobs()) {
            vista.mostrarError("No hay un programa cargado. Seleccione un archivo .asm primero.");
            return;
        }
        /*
        if (this.process.getPCB().getState() != Process.ProcessState.NEW) {
            vista.mostrarError("El programa ya ha sido cargado en memoria. No se puede cargar nuevamente.");
            return;
        }
             */

        //Loader.loadToMemory(this.process, this.memory, this.disk);

        //this.process.getPCB().setState(Process.ProcessState.READY);
        this.scheduler.checkAdmitJob();
        //this.process = this.processList.getFirstProcess();
        vista.setEstadoBCP("READY");
        actualizarVistaListaProcesos();

        List<Object[]> memoryRows = this.memory.getAllMemoryRows();
        vista.actualizarTablaMemoria(memoryRows, -1);
    }

    /**
     * Esto valida que el proceso esté en un estado válido para ejecutar (READY o RUNNING).
     * Si el proceso está en estado EXIT, NEW o BLOCKED, se muestra un mensaje de error y no se permite la ejecución.
     * @return true si el proceso está en un estado válido para ejecutar, false de lo contrario.
     */
    private boolean validarParaEjecutar() {
        if (this.cpu.getPCB() == null && !this.processList.hasProcesses()) {
            vista.mostrarError("No hay un programa cargado. Seleccione un archivo .asm primero.");
            return false;
        }
        if (this.cpu.getPCB() != null) {
            if (this.cpu.getPCB().getState() == ProcessState.EXIT) {
                vista.setEstadoBCP("EXIT");
                vista.mostrarError("El proceso ya ha terminado. Seleccione un nuevo archivo .asm para cargar otro programa.");
                return false;
            }
            else if (this.cpu.getPCB().getState() == ProcessState.NEW) {
                vista.mostrarError("El proceso aún no ha sido cargado en memoria. Cargue el programa primero.");
                return false;
            }
            else if (this.cpu.getPCB().getState() == ProcessState.BLOCKED) {
                vista.mostrarError("El proceso está bloqueado. No se puede ejecutar hasta que se desbloquee.");
                return false;
            }
        }
        
        return true;
    }

    /**
     * Esto ejecuta el programa paso a paso, actualizando la vista de la memoria y el estado del proceso.
     * Estado del proceso RUNNING
     */
    private void ejecutarPasoAPaso() {
        if (!validarParaEjecutar()) {
            return;
        }

        tick();

        // Si el proceso es válido, vamos a ejecutarlo.

        Process currentProcess = this.processList.getFirstProcess();


        if (currentProcess == null) {
            vista.mostrarError("No hay un programa cargado. Seleccione un archivo .asm primero.");
            vista.setEstadoBCP("EXIT"); // PENDIENTE
            return;
        }

        // Para ejecutar primer paso se debe llamar al dispatcher
        if (currentProcess.getState() == ProcessState.READY) {
            Dispatcher.dispatch(currentProcess, this.cpu);
            vista.setEstadoBCP("RUNNING");
            actualizarVistaListaProcesos();
        }

        // Ejecutar la instrucción actual
        this.cpu.executeInstruction();

        
        if (this.cpu.getPCB() == null || this.cpu.getPCB().getState() == ProcessState.EXIT) {

            //this.processList.removeProcess(currentProcess.getPCB().getPID());

            // Admitir automáticamente el siguiente Job en espera si cabe en la RAM liberada
            this.scheduler.checkAdmitJob();
            actualizarVistaListaProcesos();

            // Actualizar vistas
            vista.actualizarTablaMemoria(this.memory.getAllMemoryRows(), -1);
            vista.actualizarTablaDisco(this.disk.getAllDiskRows().get(0));
            actualizarVistaCPU();

            validarParaEjecutar();
        }
        else {
            Dispatcher.saveContext(currentProcess, this.cpu, this.memory); // PENDIENTE, NO DEBERIA PERO PREGUNTAR A PROFE
            actualizarVistaCPU();
            List<Object[]> memoryRows = this.memory.getAllMemoryRows();
            vista.actualizarTablaMemoria(memoryRows, this.cpu.getCurrentInstructionAddress()+1 - (SystemConfig.getUserMemoryStart()-SystemConfig.PCB_SIZE*this.processList.getProcessCount())); // el segundo parámetro es para resaltar instrucción actual en la tabla de memoria

        }
    }

    /**
     * Esto ejecuta todo el programa de una vez, actualizando la vista de la memoria y el estado del proceso.
     * Utiliza un bucle llamando a ejecutarPasoAPaso() hasta que el proceso termine (estado EXIT).
     * Si el proceso ya está en estado EXIT, se muestra un mensaje de error y no se permite la continuar la ejecución.
     * Estado del proceso RUNNING
     */
    private void ejecutarTodoPrograma() {
        if (!validarParaEjecutar()) {
            return;
        }

        new Thread(() -> { // para poder actualizar GUI con delays
            while (this.cpu.getPCB().getState() != ProcessState.EXIT) {
                //time sleep
                try {
                    Thread.sleep(1000); // 1 segundo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                ejecutarPasoAPaso();
            }
            javax.swing.SwingUtilities.invokeLater(() -> { // para asegurarse
                if (this.cpu.getPCB() != null && this.cpu.getPCB().getState() == ProcessState.EXIT) {
                    vista.setEstadoBCP("EXIT");
                }
            });
        }).start();

        
    }

    private void actualizarVistaCPU() {
        if (this.cpu.getPCB() != null) {
            vista.setProcessID(this.cpu.getPCB().getPID());
            vista.setEstadoBCP(this.cpu.getPCB().getState().toString());
            vista.setPC(this.cpu.getPC());
            vista.setIR(this.cpu.getIR());
            vista.setAC(this.cpu.getAC());
            vista.setAX(this.cpu.getAX());
            vista.setBX(this.cpu.getBX());
            vista.setCX(this.cpu.getCX());
            vista.setDX(this.cpu.getDX());
        }
    }


    // Utils

    /**
     * Aplica las configuraciones seleccionadas por el usuario.
     */
    private void aplicarConfiguraciones() {
        int memorySize = vista.getTamanoMemoriaSeleccionado();
        int kernelSize = vista.getLimiteKernelSeleccionado();

        if (memorySize <= 0 || kernelSize < 0 || kernelSize >= memorySize || kernelSize < SystemConfig.USER_MEMORY_START_MIN 
            || memorySize > SystemConfig.MEMORY_SIZE_MAX || memorySize < SystemConfig.MEMORY_SIZE_MIN || kernelSize > SystemConfig.MEMORY_SIZE_MAX-16) {
            vista.mostrarError("Rango de memoria inválido. Asegúrese de que el inicio sea menor que el fin y ambos estén dentro del rango permitido.");
            return;
        }

        SystemConfig.setMemorySize(memorySize);
        SystemConfig.setUserMemoryStart(kernelSize);

        this.memory = null; //sacamos memoria vieja
        this.cpu = null; //sacamos cpu vieja

        this.memory = new MainMemory(); // ponemos memoria nueva

        this.jobList = new JobList();
        this.processList = new ProcessList(this.memory);
        this.scheduler = new Scheduler(this.jobList, this.processList, this.memory, this.disk, this.systemClock);
        this.interruptHandler = new InterruptHandler(this.memory, this.processList, this.scheduler);

        this.cpu = new CPU(this.memory, this.interruptHandler); // ponemos cpu nueva

        System.out.println("Configuraciones aplicadas correctamente.");
        vista.mostrarInfo("Configuraciones aplicadas correctamente.");
    }

    /**
     * Reinicia el sistema, limpiando la memoria y el CPU, y actualizando la vista.
     */
    private void reiniciarSistema() {
        systemClock.reset();
        this.memory = null; //sacamos memoria vieja
        this.disk = null; //sacamos disco viejo
        this.cpu = null; //sacamos cpu vieja

        this.memory = new MainMemory(); // ponemos memoria nueva

        this.disk = new Disk(); // ponemos disco nuevo

        // reescribimos las listas
        this.jobList = new JobList();
        this.processList = new ProcessList(this.memory);

        this.scheduler = new Scheduler(this.jobList, this.processList, this.memory, this.disk, this.systemClock);
        this.interruptHandler = new InterruptHandler(this.memory, this.processList, this.scheduler);

        this.cpu = new CPU(this.memory, this.interruptHandler); // ponemos cpu nueva


        vista.limpiarVista();
        vista.habilitarConfiguraciones();

        System.out.println("Sistema reiniciado correctamente.");
        vista.mostrarInfo("Sistema reiniciado correctamente.");
    }

    private void actualizarVistaListaProcesos() {
        List<Object[]> tableDataJob = this.jobList.getTableData();
        List<Object[]> tableDataProcess = this.processList.getTableData();
        List<Object[]> tableDataFinal = new ArrayList<>();
        tableDataFinal.addAll(tableDataProcess);
        tableDataFinal.addAll(tableDataJob);

        vista.actualizarTablaTrabajos(tableDataFinal);
    }
}
