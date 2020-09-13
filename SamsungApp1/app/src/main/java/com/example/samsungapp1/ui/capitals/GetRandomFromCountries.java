package com.example.samsungapp1.ui.capitals;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GetRandomFromCountries {

    /*
    Класс для осуществление запроса в базу данных для получения названия случайного государства и его столицы.
    База данных создается с помощью вспомогательного класса MyDataBase. Метод getCountryAndCapital() возвращает массив,
    состоящий из названия государства и столицы используя запрос к базе данных по случайному индексу.
    */

    MyDataBase helper;
    SQLiteDatabase countries;
    public GetRandomFromCountries(MyDataBase helper) {
        this.helper = helper;
        countries = helper.getReadableDatabase();
    }

    public String[] getCountryAndCapital() {
        int id = (int) (Math.random() * 188 + 1);
        Cursor cursor = countries.query(
                MyDataBase.TABLE_NAME,
                new String[]{MyDataBase.KEY_COUNTRY, MyDataBase.KEY_CAPITAL},
                "_id = " + id,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        String country = cursor.getString(cursor.getColumnIndex(MyDataBase.KEY_COUNTRY)),
               capital =  cursor.getString(cursor.getColumnIndex(MyDataBase.KEY_CAPITAL));
        cursor.close();
        return new String[]{country, capital};
    }
}
