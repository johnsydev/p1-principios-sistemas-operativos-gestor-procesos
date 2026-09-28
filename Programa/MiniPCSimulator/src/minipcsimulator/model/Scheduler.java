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
            Job job = jobList.getNextJob();
            

            if (job != null) {
                int memoryStartAddress = SystemConfig.getUserMemoryStart();
                try {
                    memoryStartAddress = Loader.getFreeSpaceInMemory(job.getSize(), memory);
                } catch (Exception e) {
                    System.out.println("Error al admitir el trabajo: " + e.getMessage());
                    return;
                }

                // vamos a hacer el proceso
                Process process = new Process(job.getAssignedPID(), memoryStartAddress);

                process.getPCB().setState(PCB.ProcessState.READY);
                process.getPCB().setStartTime(systemClock.getTicks()); // tick de tiempo de inicio del proceso
                process.getPCB().setDiskStartPosition(job.getDiskStartAddress());
                process.getPCB().setDiskProgramSize(job.getSize());

                Loader.loadToMemory(process, memory, disk);

                processList.addProcess(process);
            }
        }
    }
}
