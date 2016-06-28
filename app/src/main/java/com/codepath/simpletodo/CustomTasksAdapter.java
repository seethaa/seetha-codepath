package com.codepath.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by seetha on 6/26/16.
 * This custom task adapter takes an ArrayList of Task objects to populate the listview.
 * Also takes care of setting priority color
 */
public class CustomTasksAdapter extends ArrayAdapter<Task> {

    public CustomTasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Task task = getItem(position);
        // check for existing view or inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Look up view and add UI changes as necessary
        TextView tvName = (TextView) convertView.findViewById(R.id.tvTaskText);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        tvName.setText(task.taskText);
        tvDueDate.setText(task.dueDate);

        //get the priority view and set stroke color based on priority
        View priorityView = (View) convertView.findViewById(R.id.priorityView);
        String currTaskPriority = task.priority;
        if (currTaskPriority.equals("Low")) {
            priorityView.setBackgroundColor(Color.parseColor("#fff400"));
        } else if (currTaskPriority.equals("Medium")) {
            priorityView.setBackgroundColor(Color.parseColor("#fba118"));
        } else {
            priorityView.setBackgroundColor(Color.parseColor("#d83518"));

        }
        return convertView;
    }


}
