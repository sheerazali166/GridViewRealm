package com.example.gridviewrealm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// all female stubborn arrogant dogs must die other no value of this statement or close your court rooms
// and just use doggi mama's pussy

public class CityAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<City> cityList = null;

    public CityAdapter(Context context) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<City> _cityList) {

        this.cityList = _cityList;
    }

    @Override
    public int getCount() {

        if (cityList == null) {
            return 0;
        }

        return cityList.size();
    }

    @Override
    public Object getItem(int position) {

        if (cityList == null || cityList.get(position) == null) {

            return null;
        }


        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {

            view = layoutInflater.inflate(R.layout.city_listitem, viewGroup, false);
        }

        City city = cityList.get(position);

        if (city != null) {

            // yeh koi currupt kuti decide nahi karegi woh karegi toh uske pass jao
            ((TextView) view.findViewById(R.id.name)).setText(city.getName());
            ((TextView) view.findViewById(R.id.votes)).setText(String.format(Locale.UK, "%d", city.getVotes()));
        }

        return view;
    }
}
