package ru.kiradev.weather.recycler_views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.kiradev.weather.R;
import ru.kiradev.weather.custom_views.FavoriteCityView;
import ru.kiradev.weather.fragments.AddCityFragment;
import ru.kiradev.weather.fragments.CitiesDetailsFragment;
import ru.kiradev.weather.fragments.Constants;
import ru.kiradev.weather.model.CityDetailsData;

public class RecyclerFavoriteCityAdapter extends RecyclerView.Adapter<RecyclerFavoriteCityAdapter.ViewHolder> implements Constants {
    private ArrayList<CityDetailsData> cityDetailsDataArrayList;
    private FragmentManager fragmentManager;

    public RecyclerFavoriteCityAdapter(ArrayList<CityDetailsData> cityDetailsDataArrayList, FragmentManager fragmentManager) {
        this.cityDetailsDataArrayList = cityDetailsDataArrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerFavoriteCityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_favorit_city, parent,
                false);
        return new RecyclerFavoriteCityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerFavoriteCityAdapter.ViewHolder holder, int position) {
        holder.setupViews(cityDetailsDataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityDetailsDataArrayList == null ? 0 : cityDetailsDataArrayList.size();
    }

    // класс для работы с одной view
    class ViewHolder extends RecyclerView.ViewHolder {
        private FavoriteCityView favoriteCityView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteCityView = itemView.findViewById(R.id.favoriteCityView);
        }

        void setupViews(CityDetailsData cityDetailsData) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_CITY_NAME, cityDetailsData.getCityName());
            favoriteCityView.setupViews(cityDetailsData);
            ImageButton favoriteMenuItem = favoriteCityView.findViewById(R.id.favoriteMenuItem);
            favoriteMenuItem.setOnClickListener(view -> new Thread(() -> {
                AddCityFragment fragment = new AddCityFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, true);
            }).start());
            favoriteCityView.setOnClickListener(view -> setCityFragment(cityDetailsData));
        }

        private void setCityFragment(CityDetailsData cityDetailsData) {
            CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData, new Bundle());
            replaceFragment(fragment, false);
        }

        private void replaceFragment(Fragment fragment, boolean isAddedToBackStack) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            if (isAddedToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        }

    }
}
