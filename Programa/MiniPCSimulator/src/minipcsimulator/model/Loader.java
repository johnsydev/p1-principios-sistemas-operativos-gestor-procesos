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
     */
    public static void loadProgram(ArrayList<String> lines, ArrayList<ArrayList<String>> asmArray, Process process) {
        int i = 0;
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            Instruction instruction = new Instruction(line, asmArray.get(i));
            instructions.add(instruction);
            instruction.printConversion();

            // Para GUI
            i++;
        }

        // Guarda las instrucciones en el proceso para usarlas después al cargar en memoria
        process.setInstructions(instructions);
    }

    /**
     * Carga las instrucciones del proceso en la memoria principal (RAM) directamente.
     * La carga la realiza iniciando en la primera posición de memoria de usuario definida en SystemConfig, pues actualmente es solo un proceso.
     * Con esto, el proceso pasa a estado READY y puede ser ejecutado por el CPU.
     * @param process El proceso al que se le asignarán las instrucciones.
     * @param memory La memoria principal del sistema.
     */
    public static void loadToMemory(Process process, MainMemory memory) {
        ArrayList<Instruction> instructions = process.getInstructions();
        int position = SystemConfig.getUserMemoryStart();
        for (Instruction instruction : instructions) {
            memory.setPositionInstruction(position, instruction);
            position++;
        }
    }
}
