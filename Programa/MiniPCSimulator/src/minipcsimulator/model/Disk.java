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
     * Función para recuperar un índice de archivo del disco.
     * @param address La posición de memoria desde donde se recuperará el índice de archivo.
     * @return El objeto FileIndex almacenado en la posición del disco especificada.
     */
    public FileIndex getFileIndex(int address) {
        MemoryRegister memReg = disk[address];
        if (memReg != null) {
            return memReg.fileIndex;
        }
        return null;
    }

    /**
     * Función para almacenar un índice de archivo en el disco.
     * @param address La posición de memoria donde se almacenará el índice de archivo.
     * @param fileIndex El objeto FileIndex que se almacenará en el disco.
     */
    public void setFileIndex(int address, FileIndex fileIndex) {
        disk[address] = new MemoryRegister(fileIndex);
    }

    /**
     * Función para obtener todas las posiciones del disco y sus valores, que se utiliza para mostrar en la GUI (tabla de disco y memoria virtual).
     * @return Una lista con dos listas: 
     *          - La primera contiene las posiciones del disco para programas y archivos, cada una con su valor correspondiente.
     *          - La segunda las posiciones de la memoria virtual, cada una con su valor correspondiente.
     */
    public ArrayList<List<Object[]>> getAllDiskRows() {
        List<Object[]> diskList = new ArrayList<>();
        List<Object[]> mVirtualList = new ArrayList<>();
        int i = 0;
        for (MemoryRegister memr : disk) {
            if (i >= SystemConfig.getDiskMemoryVirtualStart()) { //para que la memoria virtual no se muestre en la tabla de disco
                if (memr == null) {
                    mVirtualList.add(new Object[] {i, null});
                    i++;
                    continue;
                }
                if (memr.instruction != null) {
                    mVirtualList.add(new Object[] {i, memr.instruction.getOriginalInstructionText()});
                } else if (memr.fileIndex != null) {
                    mVirtualList.add(new Object[] {i, memr.fileIndex.toString()});
                } else{
                    mVirtualList.add(new Object[] {i, memr.getName()});
                }
                i++;
            }
            else {
                if (memr == null) {
                    diskList.add(new Object[] {i, null});
                    i++;
                    continue;
                }
                if (memr.instruction != null) {
                    diskList.add(new Object[] {i, memr.instruction.getOriginalInstructionText()});
                } else if (memr.fileIndex != null) {
                    diskList.add(new Object[] {i, memr.fileIndex.toString()});
                } else {
                    diskList.add(new Object[] {i, memr.getName()});
                }
                i++;
            }
        }
        return new ArrayList<>(List.of(diskList, mVirtualList));
    }

    
}
