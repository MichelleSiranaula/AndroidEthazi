package com.elorrieta.euskomet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private DatePickerDialog picker;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        picker = new DatePickerDialog(getActivity(), listener, year, month, day);

        Date today = new Date();
        c.setTime(today);
        long maxDate = c.getTime().getTime();

        LocalDate primero = LocalDate.parse("2021-01-01");
        c.setTime(Date.from(primero.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        long minDate = c.getTime().getTime();

        picker.getDatePicker().setMaxDate(maxDate);
        picker.getDatePicker().setMinDate(minDate);

        return picker;
    }

}
