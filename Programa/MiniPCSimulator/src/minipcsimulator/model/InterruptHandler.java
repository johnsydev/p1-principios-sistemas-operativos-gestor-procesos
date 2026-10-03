package minipcsimulator.model;

public class InterruptHandler {
    MainMemory memory;
    ProcessList processList;
    Scheduler scheduler;

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
}
