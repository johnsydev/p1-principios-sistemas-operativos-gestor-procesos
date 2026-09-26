package minipcsimulator.model;

/**
 * Clase CPU que se encarga de ejecutar las instrucciones de un proceso.
 * @author johnsydev
 */
public class CPU {

    // Codigos de operación
    public enum OpCode {
        // Instrucciones
        LOAD(1, "INSTRUCTION"), 
        STORE(2, "INSTRUCTION"), 
        ADD(3, "INSTRUCTION"), 
        SUB(4, "INSTRUCTION"), 
        MOV(5, "INSTRUCTION"),

        // Registros
        AX(5, "REGISTER"),
        BX(6, "REGISTER"),
        CX(7, "REGISTER"),
        DX(8, "REGISTER");

        private final int id;
        private final String type;

        // Constructor
        OpCode(int id, String type) {
            this.id = id;
            this.type = type;
        }

        public String getCode() {
            return this.name();
        }

        public String getType() {
            return type;
        }

        /**
         * Obtiene un OpCode por su código.
         * @param code El código del OpCode a buscar.
         * @return El OpCode correspondiente al código, o null si no se encuentra.
         */
        public static OpCode getByCode(String code) {
            for (OpCode op : values()) {
                if (op.getCode().equals(code.trim().toUpperCase())) {
                    return op;
                }
            }
            return null;
        }
    }

    private MainMemory memory;
    private PCB pcb;

    private int PC = 0;
    private int AC = 0;
    private Instruction IR;
 
    private int AX = 0;
    private int BX = 0;
    private int CX = 0;
    private int DX = 0;

    /**
     * Constructor de la clase CPU.
     * @param memory La memoria principal del sistema.
     */
    public CPU(MainMemory memory) {
        this.memory = memory;
    }

    /**
     * Asigna un PCB al CPU, actualizando los registros del CPU con los valores del PCB.
     * Esta información es enviada por el Dispatcher cuando se despacha un proceso al CPU.
     * @param pcb El PCB del proceso a ejecutar.
     */    
    public void setPCB(PCB pcb) {
        this.pcb = pcb;

        this.PC = pcb.getPC();
        this.AC = pcb.getAC();
        this.AX = pcb.getAX();
        this.BX = pcb.getBX();
        this.CX = pcb.getCX();
        this.DX = pcb.getDX();
    }

    /**
     * Obtiene la instrucción que se debe ejecutar del proceso actualmente en ejecución en el CPU, mediante el PC.
     * Si el PC está fuera del rango de memoria del proceso, se asume que el proceso ha terminado y se cambia su estado a EXIT.
     */
    public void fetch() {
        if (PC >= pcb.getStartPosition() && PC < pcb.getEndPosition()) {
            this.IR = memory.getInstruction(PC);
            this.PC++;
        } else {
            // de momento el profe no puso instrucción de END, entonces se asume
            // que si se sale del rango de memoria del proceso, es porque terminó
            pcb.setState(PCB.ProcessState.EXIT);
            System.out.println("Proceso " + pcb.getPID() + " ha terminado.");
        }
    }

    /**
     * Ejecuta la instrucción actual del proceso que está actualmente en ejecución en el CPU.
     * Realiza el decode de las instrucciones y ejecuta la operación correspondiente según el opcode y los registros involucrados.
     */
    public void execute() {
        // se obtienen los datos de la instrucción actual
        OpCode opcode = OpCode.getByCode(IR.getInstructionType());
        OpCode register = OpCode.getByCode(IR.getRegister());
        String value = IR.getValue();

        int intValue = 0;
        if (value != null && !value.isEmpty()) {
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                //despues ver
            }
        }


        if (opcode == null || register == null) {
            System.out.println("Error: Instrucción o registro no reconocido.");
            return;
        }

        switch (opcode) {
            case LOAD:
                executeLOAD(register);
                break;
            case STORE:
                executeSTORE(register);
                break;
            case ADD:
                executeADD(register);
                break;
            case SUB:
                executeSUB(register);
                break;
            case MOV:
                executeMOV(register, intValue);
                break;

            default:
                System.out.println("Error: Operación no reconocida.");
        }
    }

    // Ejecución de instrucciones específicas

    /**
     * Ejecuta la instrucción LOAD, cargando el valor del registro especificado en el acumulador (AC).
     * Ejemplo: LOAD AX cargará el valor del registro AX en el AC.
     * @param register El registro desde el cual se cargará el valor al AC.
     */
    public void executeLOAD(OpCode register) {
        switch (register) {
            case AX:
                AC = AX;
                break;
            case BX:
                AC = BX;
                break;
            case CX:
                AC = CX;
                break;
            case DX:
                AC = DX;
                break;
            default:
                System.out.println("Error: Registro no reconocido.");
        }
    }

    /**
     * Ejecuta la instrucción STORE, guardando el valor del acumulador (AC) en el registro especificado.
     * Ejemplo: STORE AX guardará el valor del AC en el registro AX.
     * @param register El registro en el cual se guardará el valor del AC.
     */
    public void executeSTORE(OpCode register) {
        switch (register) {
            case AX:
                AX = AC;
                break;
            case BX:
                BX = AC;
                break;
            case CX:
                CX = AC;
                break;
            case DX:
                DX = AC;
                break;
            default:
                System.out.println("Error: Registro no reconocido.");
        }
    }

    /**
     * Ejecuta la instrucción ADD, sumando el valor del registro especificado al acumulador (AC).
     * Ejemplo: ADD AX sumará el valor del registro AX al AC.
     * @param register El registro desde el cual se sumarán los valores al AC.
     */
    public void executeADD(OpCode register) {
        switch (register) {
            case AX:
                AC += AX;
                break;
            case BX:
                AC += BX;
                break;
            case CX:
                AC += CX;
                break;
            case DX:
                AC += DX;
                break;
            default:
                System.out.println("Error: Registro no reconocido.");
        }
    }

    /**
     * Ejecuta la instrucción SUB, restando el valor del registro especificado al acumulador (AC).
     * Ejemplo: SUB AX restará el valor del registro AX al AC.
     * @param register El registro desde el cual se restarán los valores al AC.
     */
    public void executeSUB(OpCode register) {
        switch (register) {
            case AX:
                AC -= AX;
                break;
            case BX:
                AC -= BX;
                break;
            case CX:
                AC -= CX;
                break;
            case DX:
                AC -= DX;
                break;
            default:
                System.out.println("Error: Registro no reconocido.");
        }
    }

    /**
     * Ejecuta la instrucción MOV, moviendo el valor especificado al registro indicado.
     * @param register El registro en el cual se moverá el valor.
     * @param value El valor a establecer en el registro (mover).
     */
    public void executeMOV(OpCode register, int value) {
        switch (register) {
            case AX:
                AX = value;
                break;
            case BX:
                BX = value;
                break;
            case CX:
                CX = value;
                break;
            case DX:
                DX = value;
                break;
            default:
                System.out.println("Error: Registro no reconocido.");
        }
    }


    /**
     * Método principal que ejecuta una instrucción completa mediante el fecth - execute.
     */
    public void executeInstruction() {
        fetch();
        execute();
    }


    // GETTERS
    public int getPC() {
        return PC;
    }

    public String getIR() {
        return IR.getOriginalInstructionText();
    }

    public int getAC() {
        return AC;
    }

    public int getAX() {
        return AX;
    }

    public int getBX() {
        return BX;
    }

    public int getCX() {
        return CX;
    }

    public int getDX() {
        return DX;
    }

}
