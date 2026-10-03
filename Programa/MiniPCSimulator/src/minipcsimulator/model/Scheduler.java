package minipcsimulator.model;

import minipcsimulator.utils.SystemConfig;

public class Scheduler {
    private JobList jobList;
    private ProcessList processList;
    private MainMemory memory;
    private Disk disk;
    private SystemClock systemClock;

    public Scheduler(JobList jobList, ProcessList processList, MainMemory memory, Disk disk, SystemClock systemClock) {
        this.jobList = jobList;
        this.processList = processList;
        this.memory = memory;
        this.disk = disk;
        this.systemClock = systemClock;
    }

    public boolean hasPendingJobs() {
        return jobList.hasPendingJobs();
    }

    public void checkAdmitJob() {
        while (jobList.hasPendingJobs()) {
            if (processList.getProcessCount() >= SystemConfig.MAX_PROCESSES_IN_MEMORY) {
                System.out.println("No se pueden admitir más procesos en memoria. Se alcanzó el límite máximo de procesos.");
                return;
            }

            Job job = jobList.getNextJob();

            if (job != null) {
                int memoryStartAddress = SystemConfig.getUserMemoryStart();
                int pcbStartAddress = SystemConfig.KERNEL_MEMORY_START;
                try {
                    memoryStartAddress = Loader.getFreeSpaceInMemory(job.getSize(), memory);
                    pcbStartAddress = Loader.getFreeSpaceInMemoryForPCB(memory);
                } catch (Exception e) {
                    System.out.println("Error al admitir el trabajo: " + e.getMessage());
                    return;
                }

                // vamos a hacer el proceso
                Process process = new Process(job.getAssignedPID(), memoryStartAddress, pcbStartAddress);
                process.setFilePathAndName(job.pathFile, job.nameFile);

                process.getPCB().setState(Process.ProcessState.READY);
                process.getPCB().setStartTime(systemClock.getTicks()); // tick de tiempo de inicio del proceso
                process.getPCB().setDiskStartPosition(job.getDiskStartAddress());
                process.getPCB().setDiskProgramSize(job.getSize());

                Loader.loadToMemory(process, memory, disk);

                processList.addProcess(process);
                jobList.removeNextJob();
                //Dispatcher.saveContext(process, cpu, memory); // PENDIENTE, NO DEBERIA PERO PREGUNTAR A PROFE

            }
        }
    }
}
