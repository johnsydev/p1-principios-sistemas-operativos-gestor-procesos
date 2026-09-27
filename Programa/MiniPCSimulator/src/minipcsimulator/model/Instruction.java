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
    private ArrayList<String> operands;

    /**
     * Constructor de la clase Instruction.
     * @param originalInstructionText El texto de la instrucción original.
     * @param originalInstructionParts ArrayList de partes de la instrucción original.
     */
    public Instruction(String originalInstructionText, ArrayList<String> originalInstructionParts) {
        this.originalInstructionText = originalInstructionText.trim();
        this.instructionParts = originalInstructionParts != null ? originalInstructionParts : new ArrayList<>();

        this.operands = new ArrayList<>();

        if (!this.instructionParts.isEmpty()) {
            this.instructionType = this.instructionParts.get(0).toUpperCase();

            for (int i = 1; i < this.instructionParts.size(); i++) {
                this.operands.add(this.instructionParts.get(i).trim());
            }
        } else {
            this.instructionType = "";
        }
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
    public String getOperand(int index) {
        if (index >= 0 && index < operands.size()) {
            return operands.get(index);
        }
        return null;
    }

    /**
     * Obtiene la lista de operandos de la instrucción.
     * @return La lista de operandos de la instrucción.
     */
    public ArrayList<String> getOperands() {
        return operands;
    }

    public int getOperandsCount() {
        return operands.size();
    }

    public boolean isOperandRegister(int index) {
        String operand = getOperand(index);
        if (operand != null && !operand.isEmpty()) {
            if (operand.endsWith("X")) {
                return true;
            }
        }
        return false;
    }
}
