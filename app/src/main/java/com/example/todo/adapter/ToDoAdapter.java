//package com.example.todo.adapter;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.todo.Add_Edit_Task;
//import com.example.todo.screens.MainActivity;
//import com.example.todo.R;
//import com.example.todo.database.DatabaseHandler;
//import com.example.todo.model.ToDoModel;
//
//import java.util.List;
//
//public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
//    private List<ToDoModel> toDoList;
//    private MainActivity activity;
//    private DatabaseHandler db;
//    private OnNoteListener onNoteListener;
//    public ToDoAdapter(MainActivity activity,OnNoteListener onNoteListener){
//
//        this.onNoteListener=onNoteListener;
//        this.activity=activity;
//    }
//    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
//        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view,parent,false);
//        return new ViewHolder(itemView,onNoteListener);
//    }
//
//    public void onBindViewHolder(ViewHolder holder,int position){
//        db.openDataBase();
//        ToDoModel item= toDoList.get(position);
//        holder.task.setText(item.getTask());
//    }
//
//    public void setTask(List<ToDoModel> toDoList){
//        this.toDoList =toDoList;
//        notifyDataSetChanged();
//    }
//    public void editItem(int position){
//        ToDoModel item=toDoList.get(position);
//        Bundle bundle=new Bundle();
//        bundle.putInt("id",item.getId());
//        bundle.putString("task",item.getTask());
//        Add_Edit_Task fragment=new Add_Edit_Task();
//        fragment.setArguments(bundle);
//        fragment.show(activity.getSupportFragmentManager(),Add_Edit_Task.TAG);
//    }
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        TextView task;
//        ImageView edit,delete;
//        OnNoteListener onNoteListener;
//        ViewHolder(View view,OnNoteListener onNoteListener) {
//            super(view);
//            this.onNoteListener=onNoteListener;
//            task = view.findViewById(R.id.toDoTextView);
//            edit = view.findViewById(R.id.edit);
//            delete = view.findViewById(R.id.delete);
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    onNoteListener.onDeleteClick(getAbsoluteAdapterPosition());
//                }
//            });
//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onNoteListener.onEditClick(getAbsoluteAdapterPosition());
//                }
//            });
//        }
//
//        @Override
//        public void onClick(View v) {
//            System.out.println("Click");
//
//        }
//    }
//    public void deleteItem(int position){
//        ToDoModel item=toDoList.get(position);
//        db.deleteTask(item.getId());
//        toDoList.remove(position);
//        notifyItemRemoved(position);
//    }
//    public int getItemCount() {
//        return toDoList.size();
//    }
////    public interface OnNoteListener{
////        void onEditClick(int position);
////        void onDeleteClick(int position);
////    }
//    public Context getContext() {
//        return activity;
//    }
//}
