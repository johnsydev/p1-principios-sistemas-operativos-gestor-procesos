package minipcsimulator.model;

import java.util.ArrayList;

public class ProcessList {
    Process head;
    ArrayList<Process> processList = new ArrayList<>();

    public void addProcess(Process process) {
        if (head == null) {
            head = process;
        } else {
            PCB current = head.getPCB();
            while (current.getNextPCB() != null) { //recorro los PCBs
                current = current.getNextPCB();
            }
            current.setNextPCB(process.getPCB()); // anido el nuevo PCB al final de la lista
        }
        processList.add(process);
    }

    public Process getFirstProcess() {
        return head;
    }
}
