package com.deiselw.timeinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Deise on 19/01/2018.
 */

public class LifetimeRequirementsDialogFragment extends DialogFragment {
    LifetimeRequirementsDialogListener mListener;
    DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.set_birthday_message);
        builder.setMessage(R.string.birthday_req_message);

        View rootView = inflater.inflate(R.layout.fragment_lifetime_requirements, null);
        mDatePicker = rootView.findViewById(R.id.date_picker);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        mDatePicker.setMaxDate(calendar.getTimeInMillis());
        mDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.YEAR, currentYear - getResources().getInteger(R.integer.calendar_years_max));
        mDatePicker.setMinDate(calendar.getTimeInMillis());

        builder.setView(rootView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                        mListener.onDialogPositiveClick(calendar.getTimeInMillis());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LifetimeRequirementsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface LifetimeRequirementsDialogListener {
        public void onDialogPositiveClick(long timeInMillis);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (LifetimeRequirementsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LifetimeRequirementsDialogListener");
        }
    }
}
