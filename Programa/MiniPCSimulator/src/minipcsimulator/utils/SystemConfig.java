package minipcsimulator.utils;

/**
 * Clase que contiene la configuración del sistema.
 */
public class SystemConfig {

    // VALORES POR DEFECTO, EDITABLES
    private static int memorySize = 256;
    private static int userMemoryStart = 64; // tamaño del kernel
    private static int diskSize = 512;
    private static int diskMemoryVirtualSize = 64; // tamaño de la memoria virtual

    // NO EDITABLES
    //RAM
    public static final int MEMORY_SIZE_MIN = 128; //minimo según enunciado
    public static final int MEMORY_SIZE_MAX = 65536; //maximo (64 KB)

    // Separación con Kernel
    public static final int KERNEL_MEMORY_START = 0;
    public static final int USER_MEMORY_START_MIN = 16; // lo más mínimo
    
    //Para instrucciones y registros
    public static final int STACK_SIZE = 5;
    public static final int REGISTERS_COUNT = 4;

    /**
     * Obtiene el tamaño total de la memoria del sistema.
     * @return El tamaño total de la memoria del sistema.
     */
    public static int getMemorySize() {
        return memorySize;
    }

    /**
     * Obtiene el tamaño total del disco del sistema.
     * @return El tamaño total del disco del sistema.
     */
    public static int getDiskSize() {
        return diskSize;
    }

    /**
     * Obtiene la posición de inicio de la memoria de usuario.
     * @return La posición de inicio de la memoria de usuario.
     */
    public static int getUserMemoryStart() {
        return userMemoryStart;
    }

    /**
     * Obtiene la posición de inicio de la memoria de archivos.
     * @return La posición de inicio de la memoria de archivos.
     */
    public static int getDiskMemoryVirtualStart() {
        return diskSize - diskMemoryVirtualSize;
    }

    /**
     * Establece el tamaño total de la memoria del sistema.
     * @param memorySize El tamaño total de la memoria del sistema.
     * @throws IllegalArgumentException Si el tamaño de memoria está fuera del rango permitido.
     */
    public static void setMemorySize(int memorySize) {
        if (memorySize < MEMORY_SIZE_MIN || memorySize > MEMORY_SIZE_MAX) {
            throw new IllegalArgumentException("El tamaño de memoria debe estar entre " + MEMORY_SIZE_MIN + " y " + MEMORY_SIZE_MAX);
        }
        SystemConfig.memorySize = memorySize;
    }

    /**
     * Establece la posición de inicio de la memoria de usuario.
     * @param userMemoryStart La posición de inicio de la memoria de usuario.
     * @throws IllegalArgumentException Si la posición de inicio de memoria de usuario está fuera del rango permitido.
     */
    public static void setUserMemoryStart(int userMemoryStart) {
        if (userMemoryStart < USER_MEMORY_START_MIN || userMemoryStart >= memorySize) {
            throw new IllegalArgumentException("El inicio de memoria de usuario debe estar entre " + USER_MEMORY_START_MIN + " y " + (memorySize - 1));
        }
        SystemConfig.userMemoryStart = userMemoryStart;
    }

    /**
     * Obtiene el tamaño de la memoria disponible para los procesos de usuario.
     * @return El tamaño de la memoria disponible para los procesos de usuario.
     */
    public static int getUserMemorySize() {
        return memorySize - userMemoryStart;
    }
}
