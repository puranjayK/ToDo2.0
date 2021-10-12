package com.example.todo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Add_Edit_Task;
//import com.example.todo.MyApplication;
import com.example.todo.R;
import com.example.todo.model.ToDoModel;
import com.example.todo.screens.MainActivity;


import java.util.ArrayList;
import java.util.List;

public class newToDoAdapter extends RecyclerView.Adapter<newToDoAdapter.ViewHolder> {
    private Context context;
    private MainActivity activity;
    private List<ToDoModel> toDoList;
    private OnNoteListener onNoteListener;

    public newToDoAdapter(MainActivity activity, List<ToDoModel> toDoList, OnNoteListener onNoteListener) {
        this.activity = activity;
        this.toDoList = toDoList;
        this.onNoteListener = onNoteListener;


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnNoteListener onNoteListener;
        TextView task;
        ImageView edit, delete;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.onNoteListener = onNoteListener;
            task = itemView.findViewById(R.id.toDoTextView);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoteListener.onEditClick(getAbsoluteAdapterPosition());

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoteListener.onDeleteClick(getAbsoluteAdapterPosition());
                }
            });
        }


        @Override
        public void onClick(View v) {

        }
    }


    @NonNull
    @Override
    public newToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(newToDoAdapter.ViewHolder holder, int position) {
        ToDoModel currentTask = toDoList.get(position);
        String task = currentTask.getTask();
        holder.task.setText(task);
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void setTask(List<ToDoModel> toDoList) {

        this.toDoList = toDoList;
        notifyDataSetChanged();

    }

    public void editTask(int position) {
        ToDoModel item = toDoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        System.out.println(item.getId());
        bundle.putString("task", item.getTask());
        Add_Edit_Task fragment = new Add_Edit_Task();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), Add_Edit_Task.TAG);


    }

    public Context getActivity() {
        return activity;
    }

    public interface OnNoteListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }

}
