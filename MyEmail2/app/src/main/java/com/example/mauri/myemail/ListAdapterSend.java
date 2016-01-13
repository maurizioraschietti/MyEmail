package com.example.mauri.myemail;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mauri.myemail.model.emailSend;

import java.util.List;

/**
 * Created by Mauri on 06/12/2015.
 */
public class ListAdapterSend extends ArrayAdapter<emailSend> {

    public ListAdapterSend(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapterSend(Context context, int resource, List<emailSend> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        emailSend e = getItem(position);

        if (e != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.lb_from);
            TextView tt2 = (TextView) v.findViewById(R.id.lb_oggetto);
            TextView tt3 = (TextView) v.findViewById(R.id.lb_data);

            if (tt1 != null) {
                tt1.setText(e.getTo());
            }

            if (tt2 != null) {
                tt2.setText(e.getOggetto());
            }
            if (tt3 != null) {
                tt3.setText(e.getData());
            }

            tt1.setTypeface(Typeface.DEFAULT);
            tt1.setTextColor(Color.GRAY);
            tt2.setTypeface(Typeface.DEFAULT);
            tt2.setTextColor(Color.GRAY);
            tt3.setTypeface(Typeface.DEFAULT);
            tt3.setTextColor(Color.GRAY);
            v.setBackgroundColor(0x00000000);

        }

        return v;
    }

}
