package com.microsoft.cognitiveservices.speech.samples.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.samples.myapplication.R;

import java.util.concurrent.Future;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity {

    // Replace below with your own subscription key
    private static String speechSubscriptionKey = "9dbfa487c11744a0905058796c82b57c";
    // Replace below with your own service region (e.g., "westus").
    private static String serviceRegion = "westus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FOR DATABASE----------------------

        MyHelper helper = new MyHelper(this);
        //Create table and give reference.
        SQLiteDatabase database = helper.getWritableDatabase();

        //GETTING DATA FROM DATABASE. Create cursor object and Read row by row

        Cursor cursor = database.rawQuery("SELECT WORD, MEANING FROM DICTIONARY", new String[]{});
        //Create StringBuilder object to store fetched results.

        StringBuilder builder = new StringBuilder();

        if (cursor!=null){
            cursor.moveToFirst();
        }

        do {
            String word = cursor.getString(0);
            String meaning = cursor.getString(1);
            builder.append("WORD- "+word+" MEANING- "+ meaning);

        } while (cursor.moveToNext());



        //FOR VOICE--------------------------------
        // Note: we need to request the permissions
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
    }

    public void onSpeechButtonClicked(View v) {
        TextView txt = (TextView) this.findViewById(R.id.hello); // 'hello' is the ID of your text view

        try {
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            assert(config != null);

            SpeechRecognizer reco = new SpeechRecognizer(config);
            assert(reco != null);

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
            assert(task != null);


            SpeechRecognitionResult result = task.get();
            assert(result != null);

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                String whole_string = result.toString();
                //txt.setText(result.toString());

                int last_openbrac_pos = whole_string.lastIndexOf("<");

                System.out.println("The position of < is: "+last_openbrac_pos);
                int total_string_length = whole_string.length();
                //Stores the input word.
                String spokensentence = whole_string.substring(last_openbrac_pos+1, total_string_length-3);
                System.out.println("The spoken sentence is: "+spokensentence);
                txt.setText("The spoken sentence is: " +spokensentence);
                //Extracting the first word from the result.

                int firstspace = spokensentence.indexOf(' ');

                if (firstspace!=-1){

                    String first_spoken_word = spokensentence.substring(0,firstspace);
                    first_spoken_word=first_spoken_word.replace(",","");
                    String rest = spokensentence.substring(firstspace);

                    System.out.println("The first word spoken is: "+first_spoken_word);
                    txt.setText("The first spoken word is: "+first_spoken_word);


                }

            }
            else {
                txt.setText("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
            }

            reco.close();
        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            assert(false);
        }
    }
}