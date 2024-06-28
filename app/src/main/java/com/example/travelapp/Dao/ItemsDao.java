package com.example.travelapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

import com.example.travelapp.Models.Items;

import java.util.List;

@Dao //data access objects
public interface ItemsDao {
    @Insert(onConflict = REPLACE)
    void saveItem(Items items);
    @Query("select * from items where category = :category order by id asc ")
    List<Items> getAll(String category);

    @Delete
    void delete(Items items);

    @Query("update items set checked = :checked where ID = :id")
    void checkUncheck(int id,boolean checked);

    @Query("select * from items")
    int getItemsCount();

    @Query("delete from items where addedby = :addedby")
    Integer deleteAllsystemItems(String addedby);

    @Query("delete from items where category = :category")
    Integer deleteAllbyCategory(String category);

    @Query("delete from items where category = :category and addedby =:addedby")
    Integer deleteAllbycategoryandAddedBy(String category,String addedby);

    @Query("select * from items where checked=:checked order by id asc")
    List<Items>getALlSelected(Boolean checked);
}
