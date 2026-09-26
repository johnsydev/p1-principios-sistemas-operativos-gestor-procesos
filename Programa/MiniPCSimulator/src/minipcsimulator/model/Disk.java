package minipcsimulator.model;

import java.util.ArrayList;
import java.util.List;
import minipcsimulator.utils.SystemConfig;

public class Disk {
        
    private MemoryRegister[] disk;
    
    /**
     * Constructor de la clase MainMemory.
     * Inicia la memoria principal con un arreglo de instrucciones vacías con el tamaño especificado.
     */
    public Disk() { 
        disk = new MemoryRegister[SystemConfig.getDiskSize()];
    }

    /**
     * Función para almacenar un registro de memoria en el disco.
     * @param address La posición de memoria donde se almacenará el registro.
     * @param register El objeto MemoryRegister que se almacenará en el disco.
     */
    public void setPosition(int address, MemoryRegister register) {
        disk[address] = register;
    }

    /**
     * Función para almacenar una instrucción en el disco.
     * @param address La posición de memoria donde se almacenará la instrucción.
     * @param instruction El objeto Instruction que se almacenará en la memoria.
     */
    public void setPositionInstruction(int address, Instruction instruction) {
        disk[address] = new MemoryRegister(instruction);
    }

    /**
     * Función para recuperar una instrucción deL disco.
     * @param address La posición de memoria desde donde se recuperará la instrucción.
     * @return El objeto MemoryRegister almacenado en la posición deL disco especificada.
     */
    public MemoryRegister getPosition(int address) {
        return disk[address];
    }

    /**
     * Función para recuperar una instrucción deL disco.
     * @param address La posición de memoria desde donde se recuperará la instrucción.
     * @return El objeto Instruction almacenado en la posición del disco especificada.
     */
    public Instruction getInstruction(int address) {
        MemoryRegister memReg = disk[address];
        if (memReg != null) {
            return memReg.instruction;
        }
        return null;
    }

    /**
     * Función para obtener todas las posiciones de memoria y sus valores, que se utiliza para mostrar en la GUI (tabla de memoria).
     * @return Una lista de arreglos de objetos que contiene la posición de memoria, el valor original y su representación binaria.
     */
    public List<Object[]> getAllMemoryRows() {
        List<Object[]> diskList = new ArrayList<>();
        int i = 0;
        int countPauseDiskStart = -1;
        for (MemoryRegister memr : disk) {
            if (memr == null) {
                
                if (i < SystemConfig.getDiskFilesStart()) {
                    if (countPauseDiskStart == -1) {
                        countPauseDiskStart = i;
                    }
                }
                else {
                    if (countPauseDiskStart != -1) {
                        diskList.add(new Object[] {countPauseDiskStart + " - " + (i - 1), "Memoria virtual reservada", null});
                        countPauseDiskStart = -1;
                    }
                    diskList.add(new Object[] {i, null, null});
                }
                i++;
                continue;
            }
            if (countPauseDiskStart != -1) {
                diskList.add(new Object[] {countPauseDiskStart + "..." + (i - 1), "Memoria virtual reservada", null});
                countPauseDiskStart = -1;
            }
            if (memr.instruction != null) {
                diskList.add(new Object[] {i, memr.instruction.getOriginalInstructionText(), memr.instruction.getBinaryInstruction()});
            } else {
                diskList.add(new Object[] {i, memr.getName(), memr.getBinaryValue()});
            }
            i++;
        }
        return diskList;
    }
}
