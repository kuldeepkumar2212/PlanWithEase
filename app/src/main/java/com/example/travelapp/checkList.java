package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ImageDecoderKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.travelapp.Adapter.CheckListAdapter;
import com.example.travelapp.Data.AppData;
import com.example.travelapp.Database.RoomDB;
import com.example.travelapp.Models.Items;
import com.example.travelapp.constants.myconstants;

import java.util.ArrayList;
import java.util.List;


public class checkList extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database;
    List<Items>itemsList = new ArrayList<>();
    String header,show;
    EditText txtadd;
    Button btnadd;
    LinearLayout linearLayout;

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one,menu);

        if(myconstants.MY_SELECTIONS.equals(header)){
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }else if (myconstants.MY_LIST_CAMEL_CASE.equals(header)){
            menu.getItem(1).setVisible(false);
        }


        MenuItem menuItem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> FinalList = new ArrayList<>();
                for(Items items: itemsList){
                    if(items.getItemname().toLowerCase().contains(newText.toLowerCase())){
                        FinalList.add(items);
                    }
                }
                updateRecycler(FinalList);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this,checkList.class);
        AppData appData = new AppData(database,this);
        if (item.getItemId() == R.id.btnMyselections) {
            intent.putExtra(myconstants.HEADER_SMALL, myconstants.MY_SELECTIONS);
            intent.putExtra(myconstants.SHOW_SMALL, myconstants.FALSE_STRING);
            startActivityForResult(intent, 101);
            return true;
        }else if (item.getItemId() == R.id.btnCustomList){
            intent.putExtra(myconstants.HEADER_SMALL,myconstants.MY_LIST_CAMEL_CASE);
            intent.putExtra(myconstants.SHOW_SMALL,myconstants.TRUE_STRING);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.btnDeletedefault) {
            new AlertDialog.Builder(this)
                    .setTitle("delete default data")
                    .setMessage("do you want to delete the default data")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                appData.persistDataByCategory(header,true);
                                itemsList = database.mainDao().getAll(header);
                                updateRecycler(itemsList);
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(checkList.this, "deleting cancelled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(R.drawable.baseline_warning_amber_24).show();
            return true;

        }else if (item.getItemId() == R.id.btnReset){
            new AlertDialog.Builder(this)
                    .setTitle("Reset to default")
                    .setMessage("do you want to reset to default data")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appData.persistDataByCategory(header,false);
                            itemsList = database.mainDao().getAll(header);
                            updateRecycler(itemsList);
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(checkList.this, "reset cancelled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(R.drawable.baseline_warning_amber_24)
                    .show();

        } else if (item.getItemId() == R.id.btnExit) {
            this.finishAffinity();
            Toast.makeText(checkList.this,"exitted",Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.maps) {
            Intent it = new Intent(this,maps.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        header = intent.getStringExtra(myconstants.HEADER_SMALL);
        show = intent.getStringExtra(myconstants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

        txtadd = findViewById(R.id.txtadd);
        btnadd = findViewById(R.id.btnadd);
        recyclerView = findViewById(R.id.recyclerview);
        linearLayout = findViewById(R.id.LinearLayout);

        database = RoomDB.getInstance(this);
        if(myconstants.FALSE_STRING.equals(show)){
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getALlSelected(true);
        }else{
            itemsList =database.mainDao().getAll(header);
        }

        updateRecycler(itemsList);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemNAme = txtadd.getText().toString();
                if(itemNAme!=null && !itemNAme.isEmpty()){
                    addNewitem(itemNAme);
                    Toast.makeText(checkList.this, "item  added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(checkList.this, "emplty cannot be added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            itemsList = database.mainDao().getAll(header);
            updateRecycler(itemsList);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addNewitem(String itemname){
        Items items = new Items();
        items.setChecked(false);
        items.setCategory(header);
        items.setItemname(itemname);
        items.setAddedby(myconstants.USER_SMALL);
        database.mainDao().saveItem(items);
        itemsList = database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount()-1);
        txtadd.setText("");
    }

    private void updateRecycler(List<Items> itemsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter = new CheckListAdapter(checkList.this,itemsList,database,show);
        recyclerView.setAdapter(checkListAdapter);
    }
}