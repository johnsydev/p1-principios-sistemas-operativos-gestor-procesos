package minipcsimulator.model;

import minipcsimulator.utils.SystemConfig;

/**
 * Clase que representa el PCB (Process Control Block) de un proceso en el sistema operativo simulado.
 */
public class PCB {

    // Estados del proceso posibles
    public enum ProcessState {
        NEW("NEW"), //al seleccionar el archivo
        READY("READY"), //al cargar el programa a memoria (sin suspensión)
        READY_SUSPENDED("READY_SUSPENDED"), //al suspenderlo
        RUNNING("RUNNING"), //al ejecutarlo
        BLOCKED("BLOCKED"), //esperando I/O (sin suspensión)
        BLOCKED_SUSPENDED("BLOCKED_SUSPENDED"), //al bloquearlo por espera de I/O y suspenderlo
        EXIT("EXIT"); //al terminar de ejecutarse

        private final String displayName;

        ProcessState(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public boolean isSuspended() {
            return this == READY_SUSPENDED || this == BLOCKED_SUSPENDED;
        }
    }

    // Posiciones en memoria
    private int memoryPosition;
    private int startPosition;
    private int endPosition;
    
    // Información del PCB
    private int PID = 0;
    private ProcessState state = ProcessState.NEW;
    private int PC = 0; // Program Counter
    private int AC = 0; // Acumulador

    // Registros
    private int AX = 0;
    private int BX = 0;
    private int CX = 0;
    private int DX = 0;

    // Otros atributos

    private int priority = 1; // por defecto 1 para todos durante este proyecto
    
    // al iniciar el proceso

    /**
     * Constructor de la clase PCB.
     * Inicializa el PCB con el ID del proceso (los procesos inician en 100) y la posición de inicio en memoria.
     * @param id El identificador del proceso (número simple que inicia en 1 y es asignado por el Kernel).
     * @param startPosition La posición de inicio en memoria del proceso.
     */
    public PCB(int id, int startPosition) {
        this.PID = 100+id;                            
        this.startPosition = startPosition;         // 4 es el tamaño fijo de PCB + cantidad de registros
        this.memoryPosition = (SystemConfig.KERNEL_MEMORY_START + (id-1)) * (4 + SystemConfig.REGISTERS_COUNT);
        this.state = ProcessState.NEW;
        this.PC = startPosition;
    }

    /**
     * Calcula la posición final del proceso en memoria, basada en la cantidad de instrucciones que tiene, para evitar desbordamiento de memoria.
     * @param instructionsCount La cantidad de instrucciones del proceso.
     */
    public void configEndPosition(int instructionsCount) {
        this.endPosition = startPosition + instructionsCount;
    }

    /**
     * Obtiene la posición de inicio en memoria del proceso.
     * @return La posición de inicio en memoria del proceso.
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * Obtiene la posición final en memoria del proceso, para validar que el PC no se salga del rango de memoria del proceso.
     * @return La posición final en memoria del proceso.
     */
    public int getEndPosition() {
        return endPosition;
    }

    /**
     * Obtiene el ID del proceso.
     * @return El ID del proceso.
     */
    public int getPID() {
        return PID;
    }

    /**
     * Obtiene el estado actual del proceso.
     * @return El estado actual del proceso.
     */
    public ProcessState getState() {
        return state;
    }

    /**
     * Obtiene la posición en memoria del proceso.
     * @return La posición en memoria del proceso.
     */
    public int getMemoryPosition() {
        return memoryPosition;
    }

    /**
     * Cambia el estado del proceso.
     * @param state El nuevo estado del proceso.
     */
    public void setState(ProcessState state) {
        this.state = state;
    }

    // Getters / Setters para registros:

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }    

    public int getAC() {
        return AC;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public int getAX() {
        return AX;
    }

    public void setAX(int AX) {
        this.AX = AX;
    }

    public int getBX() {
        return BX;
    }

    public void setBX(int BX) {
        this.BX = BX;
    }

    public int getCX() {
        return CX;
    }

    public void setCX(int CX) {
        this.CX = CX;
    }

    public int getDX() {
        return DX;
    }

    public void setDX(int DX) {
        this.DX = DX;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
