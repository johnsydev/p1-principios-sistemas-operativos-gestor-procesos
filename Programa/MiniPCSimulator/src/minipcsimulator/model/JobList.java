package minipcsimulator.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class JobList {
    ArrayList<Job> allJobs = new ArrayList<>();
    Queue<Job> pendingJobs = new LinkedList<>();

    public void addJob(Job job) {
        allJobs.add(job);
        pendingJobs.add(job);
    }

    // obtiene sin eliminarlo
    public Job getNextJob() {
        return pendingJobs.peek();
    }

    public void removeNextJob() {
        pendingJobs.poll();
    }

    public boolean hasPendingJobs() {
        return !pendingJobs.isEmpty();
    }

    public ArrayList<Job> getAllJobs() {
        return allJobs;
    }
}
