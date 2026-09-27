package minipcsimulator.model;

/**
 * Clase Dispatcher que se encarga de despachar un proceso al CPU, cambiando su estado a RUNNING y enviando su PCB al CPU.
 * @author johnsydev
 */
public class Dispatcher {

    /**
     * Despacha un proceso al CPU, cambiando su estado a RUNNING y enviando su PCB al CPU.
     * @param process El proceso a despachar.
     * @param cpu El CPU al que se enviará el proceso.
     */
    public static void dispatch(Process process, CPU cpu) {
        PCB pcb = process.getPCB();
        pcb.setState(PCB.ProcessState.RUNNING);
        cpu.setPCB(pcb);
        pcb.setCpuID(cpu.getCpuID());
    }
}
