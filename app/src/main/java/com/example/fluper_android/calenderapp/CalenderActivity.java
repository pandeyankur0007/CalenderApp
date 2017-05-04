package com.example.fluper_android.calenderapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener, GridCellAdapter.OnItemClickListener{
    private static final String tag = "SimpleCalendarViewActivity";

    private FloatingActionButton fav;
    private ImageView calendarToJournalButton;
    public TextView selectedDayMonthYearButton;
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private List<EventModel> eventModels = new ArrayList<>();
    private DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        _calendar = Calendar.getInstance(Locale.getDefault());
        String day = String.valueOf(_calendar.get(Calendar.DAY_OF_MONTH));
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        /*Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);*/
        handler = DBHandler.getInstance(this);

        selectedDayMonthYearButton = (TextView) this.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        fav = (FloatingActionButton) findViewById(R.id.fav);
        fav.setOnClickListener(this);

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (Button) this.findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(this, R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    /**
     *
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year)
    {
        adapter = new GridCellAdapter(this, R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth)
        {
            if (month <= 1)
            {
                month = 12;
                year--;
            }
            else
            {
                month--;
            }
            /*Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);*/
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth)
        {
            if (month > 11)
            {
                month = 1;
                year++;
            }
            else
            {
                month++;
            }
            /*Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);*/
            setGridCellAdapterToDate(month, year);
        }

        if (v == fav) {
            dialog();
        }

    }

    private void dialog() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_dialog, null);
        final DatePicker datePicker = (DatePicker) customView.findViewById(R.id.dpStartDate);
        final EditText editEvent = (EditText) customView.findViewById(R.id.editEvent);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        builder.setTitle("Enter Event and Event Date");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String eventDay = String.valueOf(datePicker.getDayOfMonth());
                String event = editEvent.getText().toString();
                handler.addRecord(new EventModel(event, eventDay));
                dialog.dismiss();
                refresh();
            }
        });
        builder.create().show();
    }

    private void refresh() {
        eventModels = handler.getAllRecord();
    }

    @Override
    public void onItemClick(String dny) {
        selectedDayMonthYearButton.setText("Selected: " + dny);
    }
}
