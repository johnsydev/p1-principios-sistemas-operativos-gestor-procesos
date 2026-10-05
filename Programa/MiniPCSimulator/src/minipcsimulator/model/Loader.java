package minipcsimulator.model;

import java.util.ArrayList;
import minipcsimulator.utils.SystemConfig;

/**
 * Clase Loader que se encarga de cargar programas en memoria y convertir instrucciones a su representación interna.
 * @author johnsydev
 */
public class Loader {

    /**
     * Carga el programa en disco (no en la memoria principal), crea las instrucciones para almacenarlas en el proceso y tenerlas listas para cargar en memoria posteriormente.
     * @param lines Lista de líneas del archivo .asm originales.
     * @param asmArray Matriz de instrucciones del archivo .asm tratadas por el parser (divididas en partes separadas por comas).
     * @param process El proceso al que se le asignarán las instrucciones.
     * @param disk El disco donde se almacenarán los programas.
     * @param fileIndex El índice del archivo que se almacenará en el disco.
     * @return La dirección de inicio en el disco donde se almacenó el programa.
     * @throws Exception Si no hay espacio libre suficiente en el disco para almacenar el programa.
     */
    public static int loadProgram(ArrayList<String> lines, ArrayList<ArrayList<String>> asmArray, Disk disk, FileIndex fileIndex) {
        int i = 0;
        ArrayList<Instruction> instructions = new ArrayList<>();
        int freeSpaceAddress;
        try {
            freeSpaceAddress = getFreeSpaceInDisk(asmArray.size(), disk);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el programa: " + e.getMessage());
        }

        int startAddress = freeSpaceAddress;
        fileIndex.setStartPosition(startAddress);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            Instruction instruction = new Instruction(line, asmArray.get(i));
            instructions.add(instruction);

            disk.setPositionInstruction(freeSpaceAddress, instruction);

            freeSpaceAddress++;
            // Para GUI
            i++;
        }

        try {
            int posIndex = getFreeSpaceInFileIndex(disk);
            disk.setFileIndex(posIndex, fileIndex);
            fileIndex.setDiskIndexPosition(posIndex);
        } catch (Exception e) {
            throw new RuntimeException("Error al almacenar el índice del archivo en el disco: " + e.getMessage());
        }
        return startAddress;
    }

    /**
     * Carga las instrucciones del proceso en la memoria principal (RAM) directamente.
     * La carga la realiza iniciando en la primera posición de memoria de usuario definida en SystemConfig, pues actualmente es solo un proceso.
     * Con esto, el proceso pasa a estado READY y puede ser ejecutado por el CPU.
     * @param process El proceso al que se le asignarán las instrucciones.
     * @param memory La memoria principal del sistema.
     * @param disk El disco donde se encuentran las instrucciones.
     * @param startAddress La dirección de inicio en la memoria principal donde se cargarán las instrucciones.
     */
    public static void loadToMemory(Process process, MainMemory memory, Disk disk) {
        int size = process.getPCB().getDiskProgramSize(); // Cantidad de instrucciones
        int startAddressDisk = process.getPCB().getDiskStartPosition(); // posición inicio en DISCO

        ArrayList<Instruction> instructions = new ArrayList<>();
        try {
            int position = getFreeSpaceInMemory(size, memory); // posición inicio en RAM, i y position son RAM

            for (int i = position; i < position + size; i++) {
                Instruction instruction = disk.getInstruction(startAddressDisk);
                memory.setPositionInstruction(i, instruction);

                instructions.add(instruction);
                startAddressDisk++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el programa en memoria: " + e.getMessage());
        }
        process.setInstructions(instructions);
    }

    /**
     * Busca un bloque continuo de celdas libres en el Disco a partir de la
     * dirección reservada para programas de usuario.
     * 
     * @param size Cantidad de instrucciones consecutivas que requiere el programa.
     * @param disk El Disco Virtual donde se buscará el espacio libre.
     * @return La dirección de memoria del Disco donde inicia el espacio libre encontrado.
     * @throws Exception Si el disco está lleno o no hay un bloque contiguo del tamaño requerido.
     */
    public static int getFreeSpaceInDisk(int size, Disk disk) throws Exception {
        int startAddress = SystemConfig.getStartDiskForPrograms();
        int totalDiskSize = SystemConfig.getDiskSize();

        int consecutiveFree = 0;
        int startIndex = -1;

        for (int i = startAddress; i < totalDiskSize; i++) {
            // Verificamos si la celda actual en el disco está libre
            if (disk.getPosition(i) == null) {
                if (consecutiveFree == 0) {
                    startIndex = i; // marca el inicio si no había uno
                }
                consecutiveFree++;

                // si se encuentra bloque continuo con la cantidad de celdas necesarias
                if (consecutiveFree == size) {
                    return startIndex;
                }
            } else {
                // si se rompe la continuidad, reiniciamos el conteo
                consecutiveFree = 0;
                startIndex = -1;
            }
        }

        // Si después de recorrer todo el disco no se encontró un bloque contiguo suficiente
        throw new Exception("No hay espacio libre contiguo suficiente en el Disco. " +
                            "Se requerían " + size + " celdas libres.");
    }

    /**
     * Busca un bloque continuo de celdas libres en la Memoria Principal (RAM) a partir de la
     * dirección reservada para programas de usuario.
     * 
     * @param size Cantidad de instrucciones consecutivas que requiere el programa.
     * @param memory La Memoria Principal donde se buscará el espacio libre.
     * @return La dirección de memoria RAM donde inicia el espacio libre encontrado.
     * @throws Exception Si la memoria está llena o no hay un bloque contiguo del tamaño requerido.
     */
    public static int getFreeSpaceInMemory(int size, MainMemory memory) throws Exception {
        int startAddress = SystemConfig.getUserMemoryStart();
        int totalMemorySize = SystemConfig.getMemorySize();

        int consecutiveFree = 0;
        int startIndex = -1;

        for (int i = startAddress; i < totalMemorySize; i++) {
            // Verificamos si la celda actual en la memoria está libre
            if (memory.getPosition(i) == null) {
                if (consecutiveFree == 0) {
                    startIndex = i; // marca el inicio si no había uno
                }
                consecutiveFree++;

                // si se encuentra bloque continuo con la cantidad de celdas necesarias
                if (consecutiveFree == size) {
                    return startIndex;
                }
            } else {
                // si se rompe la continuidad, reiniciamos el conteo
                consecutiveFree = 0;
                startIndex = -1;
            }
        }

        // Si después de recorrer toda la memoria no se encontró un bloque contiguo suficiente
        throw new Exception("No hay espacio libre contiguo suficiente en la Memoria Principal. " +
                            "Se requerían " + size + " celdas libres.");
    }

    
    /**
     * Busca un bloque continuo de celdas libres en la Memoria Principal (RAM) a partir de la
     * dirección reservada para programas de usuario.
     * 
     * @param size Cantidad de instrucciones consecutivas que requiere el programa.
     * @param memory La Memoria Principal donde se buscará el espacio libre.
     * @return La dirección de memoria RAM donde inicia el espacio libre encontrado.
     * @throws Exception Si la memoria está llena o no hay un bloque contiguo del tamaño requerido.
     */
    public static int getFreeSpaceInMemoryForPCB(MainMemory memory) throws Exception {
        int startAddress = SystemConfig.PCB_MEMORY_START;
        int totalKernelSize = SystemConfig.getUserMemoryStart();

        int size = SystemConfig.PCB_SIZE;
        int consecutiveFree = 0;
        int startIndex = -1;

        for (int i = startAddress; i < totalKernelSize; i++) {
            // Verificamos si la celda actual en la memoria está libre
            if (memory.getPosition(i) == null) {
                if (consecutiveFree == 0) {
                    startIndex = i; // marca el inicio si no había uno
                }
                consecutiveFree++;

                // si se encuentra bloque continuo con la cantidad de celdas necesarias
                if (consecutiveFree == size) {
                    return startIndex;
                }
            } else {
                // si se rompe la continuidad, reiniciamos el conteo
                consecutiveFree = 0;
                startIndex = -1;
            }
        }

        // Si después de recorrer toda la memoria no se encontró un bloque contiguo suficiente
        throw new Exception("No hay espacio libre contiguo suficiente en la Memoria Principal para el PCB. " +
                            "Se requerían " + size + " celdas libres.");
    }

    /**
     * Busca un espacio libre en el disco para almacenar un índice de archivo.
     * @param disk El disco donde se buscará el espacio libre.
     * @return La dirección de memoria del disco donde se puede almacenar el índice de archivo.
     * @throws Exception Si no hay espacio libre en el disco para almacenar el índice de archivo.
     */
    public static int getFreeSpaceInFileIndex(Disk disk) throws Exception {
        int startAddress = 0;
        int totalSpaceForIndex = SystemConfig.getStartDiskForPrograms();

        for (int i = startAddress; i < totalSpaceForIndex; i++) {
            // Verificamos si la celda actual en el disco está libre
            if (disk.getPosition(i) == null) {
                return i; // marca el inicio si no hatten uno
            }
        }
        
        // Si después de recorrer todo el disco no se encontró un bloque contiguo suficiente
        throw new Exception("No hay espacio libre en el Disco para índices de archivos.");
    }

    public static int getFreeSpaceForJobInfo(MainMemory memory) throws Exception {
        int startAddress = 0;
        int totalSpaceForJobs = SystemConfig.PCB_MEMORY_START; //PENDIENTE

        int consecutiveFree = 0;
        for (int i = startAddress; i < totalSpaceForJobs; i++) {
            // Verificamos si la celda actual en el disco está libre
            if (memory.getPosition(i) == null) {
                consecutiveFree++;
            }
            if (consecutiveFree == 2) {
                return i-1; // marca el inicio si no tienen uno
            }
        }
        
        // Si después de recorrer todo el disco no se encontró un bloque contiguo suficiente
        throw new Exception("No hay espacio libre en el Disco para trabajos.");
    }
}
