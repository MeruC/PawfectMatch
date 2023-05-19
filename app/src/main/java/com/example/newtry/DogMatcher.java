package com.example.newtry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DogMatcher extends AppCompatActivity {

    @Override
    public void onBackPressed() {

    }

    ImageView ImDog;

    TextView tvDogName, tvMinLife, tvMaxLife, tvMinWeight, tvMaxWeight, tvMinHeight, tvMaxHeight;
    Slider slChildren, slODogs, slPlayful, slProtective, slTrain, slBark;

    LinearLayout llButton;
    TextView tvDisplay;

    // Define an array of random dogs
    String[] randomDogs = {"Shih Tzu", "Dalmatian", "Airedale Terrier",
            "Alaskan Malamute", "American Foxhound", "American Staffordshire Terrier", "American Water Spaniel",
            "Anatolian Shepherd Dog", "Australian Cattle Dog", "Akita", "Affenpinscher", "Afghan Hound",
            "American English Coonhound", "American Eskimo Dog", "Australian Shepherd", "Australian Terrier",
            "Basenji", "Beagle", "Bulldog", "Basset Hound", "Bearded Collie", "Beauceron", "Bedlington Terrier",
            "Belgian Malinois", "Belgian Sheepdog", "Belgian Tervuren", "Bergamasco", "Bernese Mountain Dog",
            "Bichon Frise", "Black and Tan Coonhound", "Berger Picard", "Black Russian Terrier", "Bloodhound",
            "Boerboel", "Border Collie", "Bluetick Coonhound", "Border Terrier", "Borzoi", "Boston Terrier",
            "Boxer", "Boykin Spaniel", "Briard", "Brittany", "Brussels Griffon", "Bulldog", "Bull Terrier",
            "Cairn Terrier", "Canaan Dog", "Bullmastiff", "Cane Corso", "Cardigan Welsh Corgi", "Cavalier King Charles Spaniel",
            "Cesky Terrier", "Chesapeake Bay Retriever", "Chihuahua", "Chow Chow", "Chinese Crested",
            "Chinese Shar-Pei", "Chinook", "Cirneco dell’Etna", "Clumber Spaniel", "Cocker Spaniel", "Collie", "Corgi",
            "Coton de Tulear", "Curly-Coated Retriever", "Dachshund", "Dandie Dinmont Terrier",
            "Doberman Pinscher", "Dogue de Bordeaux", "English Cocker Spaniel", "English Foxhound", "English Setter",
            "English Toy Spaniel", "Entlebucher Mountain Dog", "Field Spaniel", "Finnish Lapphund", "German Pinscher",
            "French Bulldog", "Flat-Coated Retriever", "Finnish Spitz", "German Shepherd Dog", "German Wirehaired Pointer", "Giant Schnauzer",
            "Glen of Imaal Terrier", "Golden Retriever", "Gordon Setter", "Great Dane",
            "Great Pyrenees", "Greater Swiss Mountain Dog", "Greyhound", "Harrier", "Havanese", "Ibizan Hound", "Icelandic Sheepdog",
            "Irish Red and White Setter", "Irish Setter", "Irish Terrier", "Irish Water Spaniel", "Irish Wolfhound",
            "Italian Greyhound", "Japanese Chin", "Keeshond", "Kerry Blue Terrier", "Komondor", "Kuvasz",
            "Labrador Retriever", "Lagotto Romagnolo", "Lakeland Terrier", "Leonberger", "Lhasa Apso", "Löwchen",
            "Maltese", "Manchester Terrier", "Mastiff", "Miniature American Shepherd", "Miniature Bull Terrier", "Miniature Pinscher",
            "Miniature Schnauzer", "Neapolitan Mastiff", "Newfoundland", "Norfolk Terrier",
            "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier",
            "Nova Scotia Duck Tolling Retriever", "Old English Sheepdog", "Otterhound", "Papillon", "Parson Russell Terrier",
            "Pekingese", "Petit Basset Griffon Vendéen", "Pharaoh Houn", "Plott",
            "Pointer", "Polish Lowland Sheepdog", "Pomeranian", "Poodle", "Portuguese Podengo Pequeno",
            "Portuguese Water Dog", "Pug", "Puli", "Pyrenean Shepherd", "Rat Terrier", "Redbone Coonhound",
            "Rhodesian Ridgeback", "Rottweiler", "Russell Terrier", "Saint Bernard", "Saluki", "Samoyed",
            "Schipperke", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Shetland Sheepdog", "Shiba Inu",
            "Siberian Husky", "Silky Terrier", "Skye Terrier", "Soft Coated Wheaten Terrier", "Spanish Water Dog", "Spinone Italiano",
            "Staffordshire Bull Terrier", "Standard Schnauzer", "Sussex Spaniel", "Swedish Vallhund", "Tibetan Mastiff",
            "Tibetan Spaniel", "Tibetan Terrier", "Treeing Walker Coonhound", "Vizsla", "Weimaraner", "Welsh Springer Spaniel",
            "Welsh Terrier", "West Highland White Terrier", "Whippet", "Wire Fox Terrier", "Wirehaired Pointing Griffon",
            "Wirehaired Vizsla", "Xoloitzcuintli", "Yorkshire Terrier"
    };

    private static final String API_ENDPOINT = "https://api.api-ninjas.com/v1/dogs?name=";
    String APIKey = "DXO7BxMYypdkZ8GORO/eZQ==03RsVosiuTeKMPr6";
    String userAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_matcher);

        //get userAnswers from the dogmatcherr activity
        Intent intent = getIntent();
        userAnswer = intent.getStringExtra("userAnswer");

        initialize();
    }

    private void initialize() {

        llButton = findViewById(R.id.llButton);
        tvDisplay = findViewById(R.id.tvDisplay);

        //Displaying
        tvDogName = findViewById(R.id.tvDogName);
        tvMinLife = findViewById(R.id.tvMinLife);

        tvMinWeight = findViewById(R.id.tvMinWeight);

        tvMinHeight = findViewById(R.id.tvMinHeight);


        slChildren = findViewById(R.id.slChildren);
        slODogs = findViewById(R.id.slDog);
        slPlayful = findViewById(R.id.slPlayful);
        slProtective = findViewById(R.id.slProtective);
        slTrain = findViewById(R.id.slTrain);
        slBark = findViewById(R.id.slBark);
        ;

        ImDog = findViewById(R.id.ImDog);


        llButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPreference = userAnswer;
                String selectedDogBreed = getDogBestForYou(userPreference);

                String url = API_ENDPOINT + selectedDogBreed;
                //tvDisplay.setText(userPreference);


                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("X-Api-Key", APIKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String json = responseBody.string();
                                try {
                                    JSONArray jsonArray = new JSONArray(json);

                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String fetchedBreedName = jsonObject.getString("name");

                                            if (fetchedBreedName.equalsIgnoreCase(fetchedBreedName)) {
                                                String minLife = jsonObject.getString("min_life_expectancy");


                                                String minWeight = jsonObject.getString("min_weight_female");

                                                String minHeight = jsonObject.getString("min_height_female");

                                                String imageUrl = jsonObject.getString("image_link");

                                                String gChildren = jsonObject.getString("good_with_children");
                                                String gDogs = jsonObject.getString("good_with_other_dogs");
                                                String playful = jsonObject.getString("playfulness");
                                                String protective = jsonObject.getString("protectiveness");
                                                String train = jsonObject.getString("trainability");
                                                String barking = jsonObject.getString("barking");


                                                // Create an intent
                                                Intent intent1 = new Intent(DogMatcher.this, BestDog.class);

                                                // Put extra data in the intent
                                                intent1.putExtra("name", fetchedBreedName);
                                                intent1.putExtra("min_life", minLife);

                                                intent1.putExtra("min_weight", minWeight);

                                                intent1.putExtra("min_height", minHeight);

                                                intent1.putExtra("image_url", imageUrl);

                                                intent1.putExtra("gChildren", gChildren);
                                                intent1.putExtra("gDogs", gDogs);
                                                intent1.putExtra("playful", playful);
                                                intent1.putExtra("protective", protective);
                                                intent1.putExtra("train", train);
                                                intent1.putExtra("barking", barking);

                                                //start intent
                                                startActivity(intent1);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                    }
                                                });

                                                // Break the loop since we found the breed
                                                break;
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // Handle the case when the request is not successful
                        }
                    }
                });
            }
        });
    }

    private String getDogBestForYou(String userAnswer) {
        //Comparing user answers with the set of possible answers

        if (userAnswer.equals("NO HP LOW BASIC 4HOURS")) {
            return "Pomeranian";
        } else if (userAnswer.equals("NO PF LOW ADVANCE HOME")) {
            return "Labrador Retriever";
        } else if (userAnswer.equals("NO L HIGH BASIC 8HOURS")) {
            return "BEAGLE";
        } else if (userAnswer.equals("YES UNDER 12 HP HIGH ADVANCE 4HOURS")) {
            return "Doberman Pinscher";
        } else if (userAnswer.equals("YES UNDER 12 PF MEDIUM BASIC HOME")) {
            return "Vizsla";
        } else if (userAnswer.equals("YES UNDER 12 L MEDIUM ADVANCE HOME")) {
            return "Poodle";
        } else if (userAnswer.equals("YES ABOVE 12 HP LOW BASIC 4HOURS")) {
            return "Belgian Malinois";
        } else if (userAnswer.equals("YES ABOVE 12 PF HIGH ADVANCE HOME")) {
            return "Collie";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM BASIC 8HOURS")) {
            return "Cavalier King";
        } else if (userAnswer.equals("NO PF HIGH ADVANCE 4HOURS")) {
            return "Shetland Sheepdog";
        } else if (userAnswer.equals("NO L MEDIUM BASIC HOME")) {
            return "Boxer";
        } else if (userAnswer.equals("NO HP MEDIUM ADVANCE 8HOURS")) {
            return "Rottweiler";
        } else if (userAnswer.equals("YES UNDER 12 PF LOW BASIC 4HOURS")) {
            return "Pug";
        } else if (userAnswer.equals("YES UNDER 12 L HIGH ADVANCE HOME")) {
            return "Great Dane";
        } else if (userAnswer.equals("YES UNDER 12 HP MEDIUM BASIC 8HOURS")) {
            return "Rhodesian";
        } else if (userAnswer.equals("YES ABOVE 12 PF LOW ADVANCE 4HOURS")) {
            return "American Eskimo";
        } else if (userAnswer.equals("YES ABOVE 12 L HIGH BASIC HOME")) {
            return "Shih Tzu";
        } else if (userAnswer.equals("YES ABOVE 12 PF HIGH BASIC 4HOURS")) {
            return "Siberian Husky";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM ADVANCE 8HOURS")) {
            return "Portuguese Water Dog";
        } else if (userAnswer.equals("NO HP LOW BASIC HOME")) {
            return "Doberman Pinscher";
        } else if (userAnswer.equals("NO HP LOW BASIC 8HOURS")) {
            return "Greyhound";
        } else if (userAnswer.equals("NO HP LOW ADVANCE HOME")) {
            return "Bullmastiff";
        } else if (userAnswer.equals("NO HP LOW ADVANCE 4HOURS")) {
            return "Akita";
        } else if (userAnswer.equals("NO HP LOW ADVANCE 8HOURS")) {
            return "Basenji";
        } else if (userAnswer.equals("NO HP MEDIUM BASIC HOME")) {
            return "German Shepherd";
        } else if (userAnswer.equals("YES ABOVE 12 L HIGH ADVANCE 8HOURS")) {
            return "Labrador Retriever";
        } else if (userAnswer.equals("YES ABOVE 12 L HIGH ADVANCE 4HOURS")) {
            return "Golden Retriever";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM BASIC 4HOURS")) {
            return "Beagle";
        } else if (userAnswer.equals("YES ABOVE 12 L HIGH BASIC 8HOURS")) {
            return "Bichon Frise";
        } else if (userAnswer.equals("YES ABOVE 12 PF MEDIUM ADVANCE 4HOURS")) {
            return "Australian Shepherd";
        } else if (userAnswer.equals("YES ABOVE 12 HP HIGH ADVANCE HOME")) {
            return "Rottweiler";
        } else if (userAnswer.equals("YES ABOVE 12 HP HIGH BASIC 8HOURS")) {
            return "Newfoundland";
        } else if (userAnswer.equals("YES ABOVE 12 HP HIGH BASIC 4HOURS")) {
            return "Boxer";
        } else if (userAnswer.equals("YES ABOVE 12 HP HIGH BASIC HOME")) {
            return "German Shepherd";
        } else if (userAnswer.equals("YES ABOVE 12 HP MEDIUM ADVANCE 8HOURS")) {
            return "Giant Schnauzer";
        } else if (userAnswer.equals("YES ABOVE 12 HP MEDIUM ADVANCE 4HOURS")) {
            return "Belgian Tervuren";
        } else if (userAnswer.equals("YES ABOVE 12 HP MEDIUM ADVANCE HOME")) {
            return "Cane Corso";
        } else if (userAnswer.equals("YES ABOVE 12 HP MEDIUM BASIC 8HOURS")) {
            return "Chow Chow";
        } else if (userAnswer.equals("YES ABOVE 12 L HIGH BASIC 4HOURS")) {
            return "Cavalier King Charles Spaniel";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM ADVANCE 4HOURS")) {
            return "Australian Shepherd";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM ADVANCE HOME")) {
            return "Bernese Mountain Dog";
        } else if (userAnswer.equals("YES ABOVE 12 L MEDIUM BASIC HOME")) {
            return "Cocker Spaniel";
        } else if (userAnswer.equals("YES ABOVE 12 L LOW ADVANCE 8HOURS")) {
            return "Chinese Crested";
        } else if (userAnswer.equals("YES ABOVE 12 L LOW ADVANCE 4HOURS")) {
            return "Whippet";
        } else if (userAnswer.equals("YES ABOVE 12 L LOW ADVANCE HOME")) {
            return "Irish Wolfhound";
        } else if (userAnswer.equals("NO PF MEDIUM ADVANCE HOME")) {
            return "Boxer";
        } else if (userAnswer.equals("NO PF MEDIUM BASIC 8HOURS")) {
            return "Shiba Inu";
        } else if (userAnswer.equals("NO PF MEDIUM BASIC 4HOURS")) {
            return "Australian Shepherd";
        } else if (userAnswer.equals("NO PF MEDIUM BASIC HOME")) {
            return "Bernese Mountain Dog";
        } else if (userAnswer.equals("NO PF LOW BASIC 8HOURS")) {
            return "Bullmastiff";
        } else if (userAnswer.equals("YES ABOVE 12 PF MEDIUM ADVANCE 8HOURS")) {
            return "Giant Schnauzer";
        } else if (userAnswer.equals("YES ABOVE 12 HP MEDIUM BASIC 4HOURS")) {
            return "Portuguese Water Dog";
        } else if (userAnswer.equals("YES ABOVE 12 HP LOW BASIC 8HOURS")) {
            return "Rhodesian Ridgeback";
        } else if (userAnswer.equals("YES ABOVE 12 HP LOW BASIC HOME")) {
            return "Leonberger";
        } else if (userAnswer.equals("YES UNDER 12 L HIGH ADVANCE 8HOURS")) {
            return "English Springer Spaniel";
        } else if (userAnswer.equals("YES UNDER 12 L HIGH ADVANCE 4HOURS")) {
            return "Border Collie";
        } else if (userAnswer.equals("YES UNDER 12 L HIGH BASIC 8HOURS")) {
            return "Beagle";
        } else if (userAnswer.equals("YES UNDER 12 L HIGH BASIC 4HOURS")) {
            return "Shih Tzu";
        } else if (userAnswer.equals("NO HP HIGH BASIC HOME")) {
            return "Akita";
        } else if (userAnswer.equals("YES ABOVE 12 PF HIGH BASIC HOME")) {
            return "English Setter";
        } else if (userAnswer.equals("YES ABOVE 12 PF HIGH BASIC 8HOURS")) {
            return "Vizsla";
        } else {

// Create an instance of the Random class
            Random random = new Random();
// Get a random index within the range of the randomDogs array
            int randomIndex = random.nextInt(randomDogs.length);
// Return the randomly selected dog
            return randomDogs[randomIndex];
        }

    }

}
