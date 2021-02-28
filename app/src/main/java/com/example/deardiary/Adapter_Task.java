package com.example.deardiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

public class Adapter_Task extends RecyclerView.Adapter<Adapter_Task.MyViewHolder> {
    private LayoutInflater mInflater;
    private Adapter_Task.MyItemClickListener mClickListener;
    private AllTasks allTasks;
    private Gson gson = new Gson();

    // data is passed into the constructor
    Adapter_Task(Context context) {
        this.mInflater = LayoutInflater.from(context);
        readAllTasks_SP();
    }

    // inflates the row layout from xml when needed
    @Override
    public Adapter_Task.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.todo_list, parent, false);
        return new MyViewHolder(view);
    }


    public void readAllTasks_SP() {
        String allTasks_text = MySP.getInstance().getString(Fragment_ToDo_List.TASKS, Fragment_ToDo_List.NO_TASKS);
        if (allTasks_text.equals(Fragment_ToDo_List.NO_TASKS))
            allTasks = new AllTasks();
        else
            allTasks = gson.fromJson(allTasks_text, AllTasks.class);
    }

    private void saveAllTasks_SP() {
        String allTasks_text = gson.toJson(allTasks);
        MySP.getInstance().putString(Fragment_ToDo_List.TASKS, allTasks_text);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(Adapter_Task.MyViewHolder holder, int position) {
        Task task = allTasks.getAllTasks().get(position);
        if (task.isEdit()) {
            holder.todo_list_BTN_save.setText(R.string.save_format);
            holder.todo_list_EDT_task.setEnabled(true);
        } else {
            holder.todo_list_BTN_save.setText(R.string.edit_format);
            holder.todo_list_EDT_task.setEnabled(false);
        }
        holder.todo_list_EDT_task.setText(task.getDescription());
        holder.todo_list_checkbox.setChecked(task.isChecked());
        holder.todo_list_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onRemoveClick(position);
            }
        });
        holder.todo_list_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mClickListener != null) {
                    if (holder.todo_list_BTN_save.getText().toString().equals(view.getContext().getString(R.string.save_format))){
                        allTasks.getAllTasks().get(position).setDescription(holder.todo_list_EDT_task.getText().toString());
                        allTasks.getAllTasks().get(position).setEdit(false);
                        holder.todo_list_BTN_save.setText(R.string.edit_format);
                        holder.todo_list_EDT_task.setEnabled(false);
                    } else {
                        holder.todo_list_BTN_save.setText(R.string.save_format);
                        holder.todo_list_EDT_task.setEnabled(true);
                        allTasks.getAllTasks().get(position).setEdit(true);
                    }
                    saveAllTasks_SP();
                    mClickListener.onSaveClick();
                }
            }
        });

        holder.todo_list_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.todo_list_checkbox.isChecked())
                    allTasks.getAllTasks().get(position).setChecked(true);
                else
                    allTasks.getAllTasks().get(position).setChecked(false);

                saveAllTasks_SP();
                mClickListener.onCheckBoxClick();
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return allTasks.getAllTasks().size();
    }


    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        public void onItemClick(View view, Task task);

        public void onSaveClick();

        public void onCheckBoxClick();

        public void onRemoveClick(int position);
    }

    // allows clicks events to be caught
    void setClickListener(Adapter_Task.MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView todo_list_BTN_save;
        EditText todo_list_EDT_task;
        CheckBox todo_list_checkbox;
        TextView todo_list_BTN_remove;

        MyViewHolder(View itemView) {
            super(itemView);
            todo_list_BTN_save = itemView.findViewById(R.id.todo_list_BTN_save);
            todo_list_EDT_task = itemView.findViewById(R.id.todo_list_EDT_task);
            todo_list_checkbox = itemView.findViewById(R.id.todo_list_checkbox);
            todo_list_BTN_remove=itemView.findViewById(R.id.todo_list_BTN_remove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, allTasks.getAllTasks().get(getAdapterPosition()));
                    }
                }
            });

        }

        // convenience method for getting data at click position
        Task getItem(int id) {

            return allTasks.getAllTasks().get(id);
        }

    }

    // convenience method for getting data at click position
    Task getItem(int id) {

        return allTasks.getAllTasks().get(id);
    }
}