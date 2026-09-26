package minipcsimulator.model;

import java.util.ArrayList;

/**
 * Clase que representa una instrucción en el sistema (traducida desde el .asm).
 * @author johnsydev
 */
public class Instruction {

    private String originalInstructionText;
    private ArrayList<String> instructionParts;

    private String instructionType;
    private String register;
    private String value;

    /**
     * Constructor de la clase Instruction.
     * @param originalInstructionText El texto de la instrucción original.
     * @param originalInstructionParts ArrayList de partes de la instrucción original.
     */
    public Instruction(String originalInstructionText, ArrayList<String> originalInstructionParts) {
        this.originalInstructionText = originalInstructionText.trim();
        this.instructionParts = originalInstructionParts;

        if (originalInstructionParts != null && !originalInstructionText.isEmpty()) {
            // Instrucción separada
            this.instructionType = originalInstructionParts.get(0);
            this.register = originalInstructionParts.get(1);
            if (originalInstructionParts.size() > 2) this.value = originalInstructionParts.get(2);
        }
    }

    /**
     * Imprime la conversión de la instrucción en consola.
     */
    public void printConversion() {
        System.out.print(originalInstructionText);
        System.out.print("   ->   ");
    }

    /**
     * Obtiene el texto original de la instrucción.
     * @return El texto original de la instrucción.
     */
    public String getOriginalInstructionText() {
        return originalInstructionText;
    }

    // Getters

    /**
     * Obtiene el tipo de la instrucción.
     * @return El tipo de la instrucción.
     */
    public String getInstructionType() {
        return instructionType;
    }

    /**
     * Obtiene el registro de la instrucción.
     * @return El registro de la instrucción.
     */
    public String getRegister() {
        return register;
    }

    /**
     * Obtiene el valor de la instrucción.
     * @return El valor de la instrucción.
     */
    public String getValue() {
        return value;
    }
}
