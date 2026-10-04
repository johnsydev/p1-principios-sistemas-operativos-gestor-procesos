package minipcsimulator.model;

public class InterruptHandler {
    MainMemory memory;
    ProcessList processList;
    Scheduler scheduler;

    String bufferOutput = "";
    boolean hasPendingOutput = false;

    public InterruptHandler(MainMemory memory, ProcessList processList, Scheduler scheduler) {
        this.memory = memory;
        this.processList = processList;
        this.scheduler = scheduler;
    }

    public void handleInterruptEXIT(CPU cpu) {
        // Obtener el proceso actual
        Process currentProcess = processList.getFirstProcess();
        if (currentProcess == null) {
            System.out.println("No hay proceso actual para manejar la interrupción EXIT.");
            return;
        }

        cpu.getPCB().setState(Process.ProcessState.EXIT);

        // Eliminar el proceso de la lista de procesos
        processList.removeProcess(currentProcess.getPCB().getPID());

        cpu.setPCB(null);

        // Verificar si hay trabajos pendientes para admitir
        scheduler.checkAdmitJob();
    }

    public void handleInterruptIO(CPU cpu, String ioType) {
        if (ioType.equals("INPUT")) {

            cpu.getPCB().setState(Process.ProcessState.BLOCKED);
            Dispatcher.saveContext(processList.getFirstProcess(), cpu, memory);

        } else if (ioType.equals("OUTPUT")) {

            this.bufferOutput = cpu.getPCB().getDX() + "\n";
            this.hasPendingOutput = true;
            
        } else {
            System.out.println("Tipo de E/S desconocido: " + ioType);
        }
    }

    public void handleInterruptIOInput(Process blockedProcess, int value) {
            if (blockedProcess != null && blockedProcess.getPCB() != null) {

            blockedProcess.getPCB().setDX(value);
            
            blockedProcess.getPCB().setState(Process.ProcessState.READY);
            
            System.out.println("InterruptHandler: Proceso PID " + blockedProcess.getPCB().getPID() + " desbloqueado con DX = " + value);
        }
    }

    public boolean hasPendingOutput() {
        return hasPendingOutput;
    }

    public String clearBufferOutput() {
        String output = bufferOutput;
        bufferOutput = "";
        hasPendingOutput = false;
        return output;
    }
}
