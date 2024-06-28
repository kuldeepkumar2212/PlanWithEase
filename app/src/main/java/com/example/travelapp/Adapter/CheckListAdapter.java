package com.example.travelapp.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Database.RoomDB;
import com.example.travelapp.Models.Items;
import com.example.travelapp.R;
import com.example.travelapp.constants.myconstants;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    Context context;
    List<Items> itemsList;
    RoomDB database;
    String show;
    public CheckListAdapter() {
    }


    public CheckListAdapter(Context context, List<Items> itemsList, RoomDB database, String show) {
        this.context = context;
        this.itemsList = itemsList;
        this.database = database;
        this.show = show;

        if(itemsList.size()==0){
            Toast.makeText(context.getApplicationContext(), "nothing to show", Toast.LENGTH_SHORT).show();
        }
    }



    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckListViewHolder(LayoutInflater.from(context).inflate(R.layout.check_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.checkBox.setText(itemsList.get(position).getItemname());
        holder.checkBox.setChecked(itemsList.get(position).getChecked());

        if(myconstants.FALSE_STRING.equals(show)){
            //  holder.btndelete.setVisibility(View.GONE);
            holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_1));
        }else{
            if(itemsList.get(position).getChecked()){
                holder.layout.setBackgroundColor(Color.parseColor("#8e546f"));
            }else{
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_1));
            }
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = holder.checkBox.isChecked();
                database.mainDao().checkUncheck(itemsList.get(position).getID(),check);
                if(myconstants.FALSE_STRING.equals(show)){
                    itemsList = database.mainDao().getALlSelected(true);
                    notifyDataSetChanged();
                }else{
                    itemsList.get(position).setChecked(check);
                    notifyDataSetChanged();
                    Toast toastMessage = null;
                    if(toastMessage!=null){
                        toastMessage.cancel();
                    }
                    if(itemsList.get(position).getChecked()){
                        toastMessage = Toast.makeText(context,holder.checkBox.getText()+" is packed",Toast.LENGTH_SHORT);
                    }
                    else{
                        toastMessage = Toast.makeText(context,holder.checkBox.getText()+" is unpacked",Toast.LENGTH_SHORT);
                    }

                    toastMessage.show();
                }
            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("DELETE " + itemsList.get(position).getItemname() + " ")
                        .setMessage("are you sure?")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.mainDao().delete(itemsList.get(position));
                                itemsList.remove(itemsList.get(position));
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).setIcon(R.drawable.baseline_delete).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}

class CheckListViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layout;
    CheckBox checkBox;
    Button btndelete;
    public CheckListViewHolder(@NonNull View itemView) {
        super(itemView);

        layout= itemView.findViewById(R.id.LinearLayout);
        checkBox = itemView.findViewById(R.id.checkbox);
        btndelete = itemView.findViewById(R.id.btndelete);
    }
}