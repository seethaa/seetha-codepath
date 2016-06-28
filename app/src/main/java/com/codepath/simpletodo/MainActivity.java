package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskDialogFragment.EditTaskDialogListener {
    public static final String TAG = "SimplifyTAG";
    static CustomTasksAdapter mCustomTasksAdapter;
    static ArrayList<Task> mTaskArrayList = new ArrayList<Task>();

    ListView mListView;
    EditText mEditTextAddTask;

    /**
     * Retrieves all items from database using SugarORM.
     *
     * @return ArrayList<Task> all task items
     */
    public static ArrayList<Task> getAllItems() {
        List<Task> taskList = Task.listAll(Task.class);
        return new ArrayList(taskList);

    }

    /**
     * Used for creating TaskDialogFragment and binding arguments
     *
     * @param taskId   ID of the Task object from database
     * @param position int position of task object in the arraylist
     * @return
     */
    static TaskDialogFragment newInstance(Long taskId, int position) {
        TaskDialogFragment f = new TaskDialogFragment();

        // task ID and array position as argument.
        Bundle args = new Bundle();
        args.putInt("arrayPosition", position);
        args.putLong("taskId", taskId);
        f.setArguments(args);

        return f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItemsIntoArrayList();

        mListView = (ListView) findViewById(R.id.lvItems);
        mEditTextAddTask = (EditText) findViewById(R.id.etEditText);

        mCustomTasksAdapter = new CustomTasksAdapter(this, mTaskArrayList);

        // attach customadapter to the ListView
        mListView.setAdapter(mCustomTasksAdapter);
        mCustomTasksAdapter.notifyDataSetChanged();

        mListView.setLongClickable(true);

        //add long click listener to items in listview for delete
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //get current task
                Task currTask = mTaskArrayList.get(position);

                mTaskArrayList.remove(position);
                currTask.delete();
                mCustomTasksAdapter.notifyDataSetChanged();
                return true;
            }
        });

        //add on click listener to items in list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                showEditDialog(mTaskArrayList.get(position).getId(), position);


            }
        });
    }

    /**
     * Used to refresh listview as necessary
     */
    public void refreshItems() {
        mTaskArrayList.clear();
        mTaskArrayList = getAllItems();
        mCustomTasksAdapter.clear();
        mCustomTasksAdapter = new CustomTasksAdapter(this, mTaskArrayList);

        // attach customadapter to the ListView
        mListView.setAdapter(mCustomTasksAdapter);
        mCustomTasksAdapter.notifyDataSetChanged();
    }

    /**
     * OnResume()
     */
    public void onResume() {
        super.onResume();
        refreshItems();
    }

    /**
     * Populates initial items into listview
     */
    private void loadItemsIntoArrayList() {
        mTaskArrayList = getAllItems();
        printTaskArrayList(mTaskArrayList);
    }

    /**
     * Prints entire task arraylist. Used for debugging.
     *
     * @param mTaskArrayList
     */
    private void printTaskArrayList(ArrayList<Task> mTaskArrayList) {

        for (Task t : mTaskArrayList) {
            t.printTask();
        }
    }

    public void onAddItem(View view) {
        String taskText = mEditTextAddTask.getText().toString();

        //set task text and default values for remaining fields
        Task newTask = new Task();
        newTask.taskText = taskText;
        newTask.dueDate = "Tue, June 28";
        newTask.priority = "Low";
        newTask.completed = false;
        newTask.classification = "Personal";
        newTask.save();

        mTaskArrayList.add(newTask);
        mCustomTasksAdapter.notifyDataSetChanged();
        mEditTextAddTask.setText("");

    }

    /**
     * Launch TaskDialogFragment with the given paramenters
     *
     * @param id
     * @param position
     */
    private void showEditDialog(Long id, int position) {
        FragmentManager fm = getSupportFragmentManager();
        TaskDialogFragment editTaskDialogFragment = newInstance(id, position);
        editTaskDialogFragment.show(fm, "dialog_edittask");
    }


    /**
     * Called when dialogfragment is finished
     *
     * @param inputText String that gets returned after dialogFragment is finished
     */
    @Override
    public void onFinishEditTaskDialog(String inputText) {
        refreshItems();
    }
}
