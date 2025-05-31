package com.nforge.healthymornings.model.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.model.data.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private final Context context;
    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks, OnItemClickListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getName());
        holder.description.setText(task.getDescription());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(task));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
        }
    }
}