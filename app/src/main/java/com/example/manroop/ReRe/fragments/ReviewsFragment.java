package com.example.manroop.ReRe.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.manroop.ReRe.R;

/**
 * Created by INSPIRON on 10-Jan-17.
 */

public class ReviewsFragment extends DialogFragment {

private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frag_reviews, null);

        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerView_reviews);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        recyclerView.setLayoutManager(layoutManager);



        TextView mTextView = (TextView) view.findViewById(R.id.textview);
        mTextView.setText("Hello");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Top 3 reviews")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}

