package com.example.buri_paoton.sleepanalysis.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.buri_paoton.sleepanalysis.R;

public class graphFragment extends Fragment {
    Button tag1, tag2, tag3, tag4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {

        tag1 = rootView.findViewById(R.id.tag1);
        tag2 = rootView.findViewById(R.id.tag2);
        tag3 = rootView.findViewById(R.id.tag3);
        tag4 = rootView.findViewById(R.id.tag4);

        tag1.setSelected(true);

        initOnclick();

    }

    private void initOnclick(){
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag1.isSelected()) {
                    tag1.setSelected(true);
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }
                    if (tag3.isSelected()){
                        tag3.setSelected(false);
                        tag3.setPressed(false);
                    }
                    if (tag4.isSelected()) {
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }

                }

            }
        });

        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag2.isSelected()) {
                    tag2.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }
                    if (tag3.isSelected()){
                        tag3.setSelected(false);
                        tag3.setPressed(false);
                    }
                    if (tag4.isSelected()) {
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }

                }

            }
        });

        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag3.isSelected()) {
                    tag3.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }
                    if (tag4.isSelected()) {
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }

                }

            }
        });

        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag4.isSelected()) {
                    tag4.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }
                    if (tag3.isSelected()) {
                        tag3.setSelected(false);
                        tag3.setPressed(false);
                    }

                }

            }
        });

    }
    }
