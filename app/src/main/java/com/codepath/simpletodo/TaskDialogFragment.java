package com.codepath.simpletodo;

/**
 * Created by seetha on 6/27/16.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Shows a DialogFragment with Task edit feature
 */
public class TaskDialogFragment extends DialogFragment implements OnClickListener {
    Task mCurrentTask;
    EditText mEditTextTaskText;
    EditText mEditTextDueDate;
    RadioGroup mRadioGroupPriority;

    Button mButtonSave;
    Spinner mSpinnerClassification;

    String mPriority, mClassificationText, mDueDateText;
    int mPositionInArray;

    Calendar mCalendar;
    DatePickerDialog.OnDateSetListener mDateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.dialog_edittask, null);

        Long taskId = getArguments().getLong("taskId");

        mPositionInArray = getArguments().getInt("arrayPosition");

        //get task that user is currently editing
        mCurrentTask = Task.findById(Task.class, taskId);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_edittask, null);
        mEditTextTaskText = (EditText) layout
                .findViewById(R.id.etTaskText);
        mEditTextTaskText.setText("");
        mEditTextTaskText.append(mCurrentTask.taskText);


        mEditTextDueDate = (EditText) layout
                .findViewById(R.id.etDueDate);

        mEditTextDueDate.setText(mCurrentTask.dueDate);

        mEditTextDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCalendar = Calendar.getInstance();

                mDateListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, monthOfYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //update date in edittext
                        String myFormat = "EEE, MMMM dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        mDueDateText = sdf.format(mCalendar.getTime());
                        System.out.println("task printing date set: " + mDueDateText.toString());
                        mEditTextDueDate.setText(mDueDateText);

                    }

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                //disable all past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });


        mRadioGroupPriority = (RadioGroup) layout
                .findViewById(R.id.priorityGroup);

        //radiogroup change listener
        mRadioGroupPriority.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLow) {
                    mPriority = "Low";
                } else if (checkedId == R.id.rbMedium) {
                    mPriority = "Medium";
                } else {
                    mPriority = "High";
                }

                System.out.println("task curr priority is: " + mPriority);
            }
        });


        //debugging purposes
        mCurrentTask.printTask();

        //set priority in dialog
        String priority = mCurrentTask.priority;
        System.out.println("task priority: " + priority);
        if (priority.equals("Low")) {
            mRadioGroupPriority.check(R.id.rbLow);
        } else if (priority.equals("Medium")) {
            mRadioGroupPriority.check(R.id.rbMedium);
        } else {
            mRadioGroupPriority.check(R.id.rbHigh);

        }

        //get spinner
        mSpinnerClassification = (Spinner) layout
                .findViewById(R.id.spinnerClassification);

        //spinner change listener
        mSpinnerClassification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mClassificationText = mSpinnerClassification.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //set spinner selection
        String classification = mCurrentTask.classification;
        if (classification.equals("Personal")) {
            mSpinnerClassification.setSelection(0);
        } else if (classification.equals("Work")) {
            mSpinnerClassification.setSelection(1);
        } else if (classification.equals("Errands")) {
            mSpinnerClassification.setSelection(2);
        } else {//Other
            mSpinnerClassification.setSelection(3);

        }

        mButtonSave = (Button) layout.findViewById(R.id.btnSave);
        mButtonSave.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                saveTask();
                break;

        }
    }

    /**
     * Method to delete current task. Incomplete
     *
     * @param v
     */
    private void deleteTask(final View v) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes pressed
                        mCurrentTask.delete();
                        EditTaskDialogListener activity = (EditTaskDialogListener) getActivity();
                        activity.onFinishEditTaskDialog("sending back");
//                        getContext().dismiss();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No pressed
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    /**
     * Saves updated task to database and dismisses fragment to go back to MainActivity
     */
    public void saveTask() {


        String taskText = mEditTextTaskText.getText().toString();

        //modify current task's values
        mCurrentTask.taskText = taskText;
        mCurrentTask.priority = mPriority;
        mCurrentTask.dueDate = mDueDateText;
        mCurrentTask.completed = false;
        mCurrentTask.classification = mClassificationText;


        // Save the task object to the table
        mCurrentTask.save();

        //dismiss current fragment
        EditTaskDialogListener activity = (EditTaskDialogListener) getActivity();
        activity.onFinishEditTaskDialog("sending back");
        this.dismiss();


    }

    /**
     * Interface used as a listener for MainActivity when fragment is dismissed.
     */
    public interface EditTaskDialogListener {
        void onFinishEditTaskDialog(String inputText);
    }
}

