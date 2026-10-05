package minipcsimulator.model;

/**
 * Clase que representa un registro de memoria, que puede ser un registro de datos o una instrucción.
 * Fue creada pues almacenar solo instrucciones limitaba la capacidad de guardar datos en memoria RAM con otras finalidades.
 */
public class MemoryRegister {
    public String name;
    public String value;
    public Instruction instruction;
    public FileIndex fileIndex;

    /**
     * Constructor manual de la clase MemoryRegister para registros de datos.
     * Almacena el valor entero junto con el nombre del registro.
     * @param name El nombre del registro de memoria.
     * @param value El valor entero del registro de memoria.
     */
    public MemoryRegister(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public MemoryRegister(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor de la clase MemoryRegister para registros de instrucciones (objeto Instruction).
     * @param instruction La instrucción que se almacenará en el registro de memoria.
     */
    public MemoryRegister(Instruction instruction) {
        this.instruction = instruction;
    }

    /**
     * Constructor de la clase MemoryRegister para registros de índices de archivos para el disco (objeto FileIndex).
     * @param fileIndex El índice de archivo que se almacenará en el registro de memoria.
     */
    public MemoryRegister(FileIndex fileIndex) {
        this.fileIndex = fileIndex;
    }

    /**
     * Obtiene el valor entero del registro de memoria.
     * @return El valor entero del registro de memoria.
     */
    public int getValue() {
        return Integer.parseInt(this.value);
    }

    public String getValueString() {
        return this.value;
    }

    /**
     * Obtiene el nombre del registro de memoria.
     * @return El nombre del registro de memoria.
     */
    public String getName() {
        return this.name;
    }
}
