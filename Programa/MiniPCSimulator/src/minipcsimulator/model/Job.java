package minipcsimulator.model;

import java.util.ArrayList;

public class Job {
    private static int nextPID = 1;

    String pathFile; //ruta
    String nameFile;
    ArrayList<ArrayList<String>> asmArray = new ArrayList<>(); //array de instrucciones
    ArrayList<String> lines = new ArrayList<>(); // lineas originales del archivo .asm

    int assignedPID;
    int diskStartAddress;
    int size;
    boolean isLoadedInMemory = false;

    public Job(String pathFile, String nameFile, ArrayList<String> lines, ArrayList<ArrayList<String>> asmArray) {
        this.pathFile = pathFile;
        this.nameFile = nameFile;
        this.lines = lines;
        this.asmArray = asmArray;
        this.size = asmArray.size();
        this.assignedPID = nextPID++;
    }

    public void setDiskStartAddress(int diskStartAddress) {
        this.diskStartAddress = diskStartAddress;
    }

    public int getDiskStartAddress() {
        return diskStartAddress;
    }

    public int getSize() {
        return size;
    }

    public int getAssignedPID() {
        if (assignedPID == 0) {
            assignedPID = nextPID++;
        }
        return assignedPID;
    }
}
