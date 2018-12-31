package com.example.fang.medicinereminderapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditAlarmActivity extends AppCompatActivity {

    EditText alarmTitleEditText;
    Switch activeASwitch;
    Button dataButton, timeButton, doneButton, cancelButton;
    Spinner occurenceSpinner;
    DateTime mDateTime;

    private Alarm newAlarm;

    private GregorianCalendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    static final int DAYS_DIALOG_ID = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        //Bind view ID
        bindID();

        getAlarmData();

        occurenceSpinner.setOnItemSelectedListener(mOccurenceSelectedListener);

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Alarm.ONCE == newAlarm.getOccurence()) {
                    Dialog dateDialog = createDialog(DATE_DIALOG_ID);
                    if(dateDialog!=null){
                        dateDialog.create();
                        dateDialog.show();
                    }
                }
                else if (Alarm.WEEKLY == newAlarm.getOccurence()) {
                    Dialog dateDialog = createDialog(DAYS_DIALOG_ID);
                    if(dateDialog!=null){
                        dateDialog.create();
                        dateDialog.show();
                    }
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog timeDialog = createDialog(TIME_DIALOG_ID);
                if(timeDialog!=null){
                    timeDialog.create();
                    timeDialog.show();
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlarm();
                Intent intent = new Intent();

                newAlarm.toIntent(intent);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();

            }
        });
    }

    private void updateAlarm() {
        String Title = alarmTitleEditText.getText().toString();
        newAlarm .setTitle(Title);
        boolean active = activeASwitch.isChecked();
        newAlarm.setEnabled(active);
        int occurence = (int) occurenceSpinner.getSelectedItemId();
        newAlarm.setOccurence(occurence);
    }

    private AdapterView.OnItemSelectedListener mOccurenceSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
        {
            newAlarm.setOccurence(position);
            updateButtons();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };



    private void bindID() {

        alarmTitleEditText = findViewById(R.id.et_alarm_title);
        activeASwitch = findViewById(R.id.switch_active);
        dataButton = findViewById(R.id.bt_data_picker);
        timeButton = findViewById(R.id.bt_time_picker);
        occurenceSpinner = findViewById(R.id.spinner);
        doneButton = findViewById(R.id.bt_edit_done);
        cancelButton = findViewById(R.id.bt_edit_cancel);

    }

    private void getAlarmData() {

        newAlarm = new Alarm(this);
        newAlarm.fromIntent(getIntent());

        mDateTime = new DateTime(this);

        alarmTitleEditText.setText(newAlarm.getTitle());
        activeASwitch.setChecked(newAlarm.getEnabled());
        occurenceSpinner.setSelection(newAlarm.getOccurence());

        mCalendar = new GregorianCalendar();
        mCalendar.setTimeInMillis(newAlarm.getDate());
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        updateButtons();


    }

    private Dialog createDialog(int id){

        if (DATE_DIALOG_ID == id)
            return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        else if (TIME_DIALOG_ID == id)
            return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, mDateTime.is24hClock());
        else if (DAYS_DIALOG_ID == id)
            return DaysPickerDialog();
        else
            return null;

    }


    private void updateButtons() {

        if (Alarm.ONCE == newAlarm.getOccurence())
            dataButton.setText(mDateTime.formatDate(newAlarm));
        else if (Alarm.WEEKLY == newAlarm.getOccurence())
            dataButton.setText(mDateTime.formatDays(newAlarm));
        timeButton.setText(mDateTime.formatTime(newAlarm));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            mCalendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
            newAlarm.setDate(mCalendar.getTimeInMillis());

            updateButtons();
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            mHour = hourOfDay;
            mMinute = minute;

            mCalendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
            newAlarm.setDate(mCalendar.getTimeInMillis());

            updateButtons();
        }
    };

    private Dialog DaysPickerDialog()
    {
        AlertDialog.Builder builder;
        final boolean[] days = mDateTime.getDays(newAlarm);
        final String[] names = mDateTime.getFullDayNames();

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Week days");

        builder.setMultiChoiceItems(names, days, new DialogInterface.OnMultiChoiceClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked)
            {
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                mDateTime.setDays(newAlarm, days);
                updateButtons();
            }
        });

        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

}

