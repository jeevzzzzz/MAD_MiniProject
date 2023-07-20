package com.example.alarmapp.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.alarmapp.R;
import com.example.alarmapp.myinterface.CountdownRecycleViewClickInterface;
import com.example.alarmapp.sharedPreference.CountdownDatabase;
import com.example.alarmapp.ui.PickTimeFragment;

import java.util.List;

public class CountdownAdapter extends RecyclerView.Adapter<CountdownAdapter.ViewHolder> {


    CountdownDatabase dataBase;

    private final String NAME_DATABASE = "preset_time_database";

    CountdownRecycleViewClickInterface recycleViewClickInterface;

    private List<String> localDataSet;


    private int selectedItem = RecyclerView.NO_POSITION; // vị trí của item được chọn


    public CountdownAdapter(PickTimeFragment pickTimeFragment) {
        dataBase = new CountdownDatabase(NAME_DATABASE, pickTimeFragment.getContext());
        localDataSet = dataBase.getDataBase();
        recycleViewClickInterface = pickTimeFragment;

    }

    public List<String> getLocalDataSet() {
        return this.localDataSet;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTime;
        private final TextView textViewDescription;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textViewTime = view.findViewById(R.id.view_preset_time);
            textViewDescription = view.findViewById((R.id.view_preset_time_name));
            imageView = view.findViewById(R.id.round_circle);

        }

        public TextView getTextViewTime() {
            return textViewTime;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextViewDescription(){ return textViewDescription;}

    }


    /**
     * Initialize the dataset of the Adapter
     *
     * by RecyclerView
     */


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.preset_timer, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,
                                 @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element



        TextView textViewTime = viewHolder.getTextViewTime();
        TextView textViewDescription = viewHolder.getTextViewDescription();

        //get data
        String data = localDataSet.get(position);
        String sub_data[] = data.split("\\|");
        textViewTime.setText(sub_data[0]);

        if(sub_data.length > 1) {
            textViewDescription.setText(sub_data[1]);
            textViewDescription.setVisibility(View.VISIBLE);
        } else {

            textViewDescription.setVisibility(View.GONE);
        }



        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                showOptionsDialog(view, position);
                return false;

            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewClickInterface.onItemClick(data);
                onItemClick(position);
            }
        });


        ImageView imageView = viewHolder.getImageView();

        if (selectedItem == position) {
            // Hiển thị vòng tròn nếu item được chọn
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Ẩn vòng tròn nếu item không được chọn
            imageView.setVisibility(View.GONE);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private void showOptionsDialog(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                remove_item(view, position);
                                break;
                        }
                    }
                });

        builder.create().show();
    }

    public void addItem(int position, String value) {

        this.localDataSet.add(position, value);
        dataBase.setDataBase(localDataSet);
        notifyDataSetChanged();
    }


    public void remove_item(View view, int position)  {

        if(selectedItem >= position) {
            selectedItem = selectedItem - 1;
        } else if (selectedItem == position) {
            selectedItem = RecyclerView.NO_POSITION;
        }
        localDataSet.remove(position);
        dataBase.setDataBase(localDataSet);
        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_LONG);
        notifyDataSetChanged();

    }


    // đổi màu khi click vào item
    public void onItemClick(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }



}
