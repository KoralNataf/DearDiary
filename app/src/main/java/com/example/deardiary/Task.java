package com.example.deardiary;

public class Task {
    private String description;
    private boolean isChecked;
    private boolean isEdit;

    public Task() {
        description="";
        isChecked=false;
        isEdit=true;
    }

    public Task(String description) {
        this.description = description;
        this.isChecked=false;
        isEdit=true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }
}
