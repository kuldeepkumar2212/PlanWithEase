package com.example.travelapp.Data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.travelapp.Database.RoomDB;
import com.example.travelapp.Models.Items;
import com.example.travelapp.constants.myconstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {

    RoomDB databse;
    Context context;
    String category;
    public static  final String LAST_VERSION = "LAST_VERSION";
    public static  final int NEW_VERSION = 3;

    public AppData(RoomDB databse) {
        this.databse = databse;
    }

    public AppData(RoomDB databse, Context context) {
        this.databse = databse;
        this.context = context;
    }

    public List<Items>getBasicData(){
        String []data = {"visa","passport","aadhar card","wallet","Tickets","license","keys"};
        return prepareItemsList(myconstants.BASIC_NEEDS_CAMEL_CASE,data);

    }

    public List<Items>getPersonalcare(){
        String []data = {"tooth brush","paste","soap","mouthwash","moisturizer","shampoo","comb","hair drier","trimmer","powder"};
        return prepareItemsList(myconstants.PERSONAL_CARE_CAMEL_CASE,data);
    }

    public List<Items>getClothing(){
        String []data = {"shirt","pant","socks","inner garments","vests","shorts","shoes","watch","rings","braclet"};
        return prepareItemsList(myconstants.CLOTHING_CAMEL_CASE,data);
    }
    public List<Items>getBabyNeeds(){
        String []data = {"diaper","diaper rash cream","baby wipes","pajama sets","night suits","baby vests","thermal wear","shirts","jeans"};
        return prepareItemsList(myconstants.BABY_NEEDS_CAMEL_CASE,data);
    }

    public List<Items>getHealthNeeds(){
        String []data = {"Tablets","syrup","inhalers","pain killers"};
        return prepareItemsList(myconstants.HEALTH_CAMEL_CASE,data);
    }

    public List<Items>getTechnologyNeeds(){
        String []data = {"Mobiles","charger","Camera","laptop","power bank","memory cards","drones"};
        return prepareItemsList(myconstants.TECHNOLOGY_CAMEL_CASE,data);
    }

    public List<Items>getFoodNeeds(){
        String []data = {"Biscuits","chocolates","Juices","maggie","wafers"};
        return prepareItemsList(myconstants.FOOD_CAMEL_CASE,data);
    }

    public List<Items>getBeachNeeds(){
        String []data = {"shades","shorts","sun screen","life jacket","Beer"};
        return prepareItemsList(myconstants.BEACH_SUPPLIES_CAMEL_CASE,data);
    }

    public List<Items>getCarNeeds(){
        String []data = {"spare wheel","emergency kit","Fuel","licence","keys"};
        return prepareItemsList(myconstants.CAR_SUPPLIES_CAMEL_CASE,data);
    }

    public List<Items>getNeeds(){
        String []data = {"Backpack","suitcase","laundry bag","lock","travel guide"};
        return prepareItemsList(myconstants.NEEDS_CAMEL_CASE,data);
    }




    public List<Items>prepareItemsList(String category,String []data){
        List<String>list = Arrays.asList(data);
        List<Items>dataList = new ArrayList<>();
        dataList.clear();
        for(int i=0;i<list.size();i++){
            dataList.add(new Items(list.get(i),category,false ));
        }
        return dataList;
    }

    public List<List<Items>> getAllData(){
        List<List<Items>> listofallitems = new ArrayList<>();
        listofallitems.clear();
        listofallitems.add(getBasicData());
        listofallitems.add(getPersonalcare());
        listofallitems.add(getBabyNeeds());
        listofallitems.add(getClothing());
        listofallitems.add(getBeachNeeds());
        listofallitems.add(getCarNeeds());
        listofallitems.add(getFoodNeeds());
        listofallitems.add(getHealthNeeds());
        listofallitems.add(getTechnologyNeeds());
        listofallitems.add(getNeeds());
        return  listofallitems;
     }

     public void persistAlldata(){
        List<List<Items>>listOfAllitems = getAllData();
        for(List<Items>list: listOfAllitems){
            for(Items items: list){
                databse.mainDao().saveItem(items);
            }
         }
        System.out.println("Data Added");
     }

     public void persistDataByCategory(String category,Boolean onlyData){
        try {
            List<Items>list = deleteAndGetListByCategory(category,onlyData);
            if(!onlyData){
                for(Items items: list){
                    databse.mainDao().saveItem(items);
                }
                Toast.makeText(context, "Reset Succesful", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Reset Succesful", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){

            ex.printStackTrace();
            Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show();
        }
     }

     private List<Items>deleteAndGetListByCategory(String category,Boolean onlyDelete){
        if(onlyDelete){
            databse.mainDao().deleteAllbycategoryandAddedBy(category,myconstants.SYSTEM_SMALL);
        }else{
            databse.mainDao().deleteAllbyCategory(category);
        }
        switch (category){
            case myconstants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();
            case myconstants.CLOTHING_CAMEL_CASE:
                return  getClothing();
            case myconstants.PERSONAL_CARE_CAMEL_CASE:
                return  getPersonalcare();
            case myconstants.BABY_NEEDS_CAMEL_CASE:
                return  getBabyNeeds();
            case myconstants.HEALTH_CAMEL_CASE:
                return  getHealthNeeds();
            case myconstants.TECHNOLOGY_CAMEL_CASE:
                return  getTechnologyNeeds();
            case myconstants.FOOD_CAMEL_CASE:
                return  getFoodNeeds();
            case myconstants.BEACH_SUPPLIES_CAMEL_CASE:
                return  getBeachNeeds();
            case myconstants.CAR_SUPPLIES_CAMEL_CASE:
                return  getCarNeeds();
            case myconstants.NEEDS_CAMEL_CASE:
                return  getNeeds();

            default: return new ArrayList<>();
        }
     }
}
