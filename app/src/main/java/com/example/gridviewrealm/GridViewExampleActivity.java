package com.example.gridviewrealm;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class GridViewExampleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private CityAdapter cityAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grid_view_example);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        // Clear the realm from last time
        Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        City modifiedCity = (City) cityAdapter.getItem(position);

        // Acquire the RealmObject matching the name of the clicked City.
        final City city = realm.where(City.class).equalTo("name", modifiedCity.getName()).findFirst();

        // Create a transaction to increment the vote count for the selected City in the realm
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                city.setVotes(city.getVotes() + 1);
            }
        });

        updateCities();

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Load from file "cities.json" first time
        if (cityAdapter == null) {

            List<City> cityList = loadCities();

            //This is the GridView adapter
            cityAdapter = new CityAdapter(this);
            cityAdapter.setData(cityList);

            //This is the GridView which will display the list of cities
            gridView = findViewById(R.id.cities_list);
            gridView.setAdapter(cityAdapter);
            gridView.setOnItemClickListener(GridViewExampleActivity.this);
            cityAdapter.notifyDataSetChanged();
            gridView.invalidate();
        }

    }

    public void updateCities() {

        // Pull all the cities from the realm
        RealmResults<City> cityRealmResults = realm.where(City.class).findAll();

        // Put these items in the Adapter
        cityAdapter.setData(cityRealmResults);
        cityAdapter.notifyDataSetChanged();
        gridView.invalidate();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private List<City> loadCities() {

        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream inputStream;

        try {

            inputStream = getAssets().open("assets/cities.json");

        } catch (IOException ioException) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(inputStream));
        List<City> cityList = gson.fromJson(jsonElement, new TypeToken<List<City>>() {}.getType());

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        realm.beginTransaction();

        Collection<City> cityCollection = realm.copyToRealm(cityList);
        realm.commitTransaction();

        return new ArrayList<City>(cityCollection);
    }
}
