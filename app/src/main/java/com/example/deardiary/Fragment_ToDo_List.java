package com.example.deardiary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

public class Fragment_ToDo_List extends Fragment_Base {
    public static final String TASKS="TASKS";
    public static final String NO_TASKS="NO_TASKS";
    private RecyclerView todo_list_LST_all_tasks;
    private TextView todo_list_BTN_add;
    private TextView todo_list_BTN_clear;
    private ImageView todo_list_IMG_background;
    private AllTasks allTasks;
    private Adapter_Task adapter_task;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list,container,false);
        findVies(view);
        initView();

        return view;
    }

    private void findVies(View view) {
        todo_list_LST_all_tasks=view.findViewById(R.id.todo_list_LST_all_tasks);
        todo_list_IMG_background=view.findViewById(R.id.todo_list_IMG_background);
        todo_list_BTN_clear=view.findViewById(R.id.todo_list_BTN_clear);
        todo_list_BTN_add=view.findViewById(R.id.todo_list_BTN_add);

    }

    private void initView() {
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getContext().getPackageName()), todo_list_IMG_background);
        gson = new Gson();
        readAllTasks_SP();

        todo_list_LST_all_tasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_task = new Adapter_Task(getContext());

        adapter_task.setClickListener(new Adapter_Task.MyItemClickListener() {
            @Override
            public void onItemClick(View view, Task task) {
            }

            @Override
            public void onSaveClick() {
                readAllTasks_SP();
            }

            @Override
            public void onCheckBoxClick() {
                readAllTasks_SP();
            }

            @Override
            public void onRemoveClick(int position) {
                allTasks.remove(position);
                todo_list_LST_all_tasks.removeViewAt(position);
                adapter_task.notifyItemRemoved(position);
                saveAllTasks_SP();
                adapter_task.readAllTasks_SP();
                adapter_task.notifyItemRangeChanged(position, allTasks.getAllTasks().size());
            }
        });
       todo_list_LST_all_tasks.setAdapter(adapter_task);
        todo_list_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                allTasks.add(task);
                saveAllTasks_SP();
                adapter_task.readAllTasks_SP();
                adapter_task.notifyDataSetChanged();
            }
        });
        todo_list_BTN_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = allTasks.getAllTasks().size();
                allTasks.getAllTasks().clear();
                saveAllTasks_SP();
                adapter_task.readAllTasks_SP();
                adapter_task.notifyDataSetChanged();

            }
        });

    }

    private void saveAllTasks_SP() {
        String allTasks_text=gson.toJson(allTasks);
        MySP.getInstance().putString(TASKS,allTasks_text);
    }

    private void readAllTasks_SP() {
        String allTasks_text=MySP.getInstance().getString(TASKS,NO_TASKS);
        if(allTasks_text.equals(NO_TASKS))
            allTasks=new AllTasks();
        else
            allTasks=gson.fromJson(allTasks_text,AllTasks.class);
    }
}
