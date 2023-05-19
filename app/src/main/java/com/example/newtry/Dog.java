package com.example.newtry;

import android.os.Parcel;
import android.os.Parcelable;

public class Dog implements Parcelable {
    private String id;
    private String picture;
    private String name;
    private String breed;
    private String age;


    public Dog() {
        // Default constructor required for Firestore deserialization
    }

    public Dog(String id, String picture, String name, String breed, String age) {
        this.id = id;
        this.picture = picture;
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picture);
        dest.writeString(name);
        dest.writeString(breed);
        dest.writeString(age);
    }

    protected Dog(Parcel in) {
        picture = in.readString();
        name = in.readString();
        breed = in.readString();
        age = in.readString();
    }

    public static final Creator<Dog> CREATOR = new Creator<Dog>() {
        @Override
        public Dog createFromParcel(Parcel in) {
            return new Dog(in);
        }

        @Override
        public Dog[] newArray(int size) {
            return new Dog[size];
        }
    };
}
