package com.example.deardiary;

import java.util.ArrayList;

public class AllTasks {
    private ArrayList<Task> allTasks;

    public AllTasks(ArrayList<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public AllTasks() {
        this.allTasks = new ArrayList<>();
    }

    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(ArrayList<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public void add(Task task){
        allTasks.add(task);
    }

    public void remove(int position){
        allTasks.remove(position);
    }
}
