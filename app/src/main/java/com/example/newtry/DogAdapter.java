package com.example.newtry;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DogAdapter extends ArrayAdapter<Dog> {

    private Context context;
    private List<Dog> dogList;

    public DogAdapter(Context context, List<Dog> dogList) {
        super(context, R.layout.item_dog, dogList);
        this.context = context;
        this.dogList = dogList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dog, parent, false);
        }

        ImageView imgDog = convertView.findViewById(R.id.imgDog);
        TextView txtName = convertView.findViewById(R.id.txtName);

        Dog dog = dogList.get(position);

        if (TextUtils.isEmpty(dog.getPicture())) {
            // Handle empty picture case (e.g., display a placeholder image)
            imgDog.setImageResource(R.drawable.ic_launcher_background);
        } else {
            // Load the dog's picture using Picasso
            Picasso.get().load(dog.getPicture()).into(imgDog);
        }

        txtName.setText(dog.getName());

        return convertView;
    }

}

