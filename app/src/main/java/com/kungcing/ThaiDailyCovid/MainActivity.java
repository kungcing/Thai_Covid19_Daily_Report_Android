package com.kungcing.ThaiDailyCovid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout;
    TextView tvTotalCase;
    TextView tvNewReCoverCase;
    TextView tvHospitalCase;
    TextView tvNewConfirm;
    TextView tvDeath;
    TextView tvUpdateDate;
    ImageView imageView;
    ImageView banner;

    @Override
    protected void onResume() {
        super.onResume();
        layout = findViewById(R.id.mainBackground);
        preferenceFromUser(layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.mainBackground);
        tvTotalCase = findViewById(R.id.totalCase);
        tvNewConfirm = findViewById(R.id.newCase);
        tvNewReCoverCase = findViewById(R.id.NewRecoverCase);
        tvHospitalCase = findViewById(R.id.hospitalCase);
        tvDeath = findViewById(R.id.deathCase);
        tvUpdateDate = findViewById(R.id.updateDate);
        imageView = findViewById(R.id.im1);
        banner = findViewById(R.id.im2);
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        String bb = netInfo.getTypeName();

        Toast.makeText(this, "" + bb, Toast.LENGTH_SHORT).show();
        writeFileToSDCard();
        // this below lines of code using for testing about JAVA I/O STREAMS
        //File fff = getFilesDir();
        String path = getFilesDir() + "/myData";
        File myDirectory = new File(path);
        if (!myDirectory.exists()) {
            boolean bool = myDirectory.mkdir();
            Toast.makeText(this, "complete " + bool, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "" + "contain this directory", Toast.LENGTH_SHORT).show();
        }
        try {
            createMyFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Ion.with(banner).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsm7wFGuQ-9obEILUST3J37xJhDW86O3dk0w&usqp=CAU");
        Ion.with(imageView).load("https://www.egov.go.th/upload/eservice-thumbnail/img_db3bfd36746902b2797c33a018e6124f.jpg");
        Ion.with(getApplicationContext())
                .load("https://covid19.ddc.moph.go.th/api/Cases/today-cases-all")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        int totalCase = result.get(0).getAsJsonObject().get("total_case").getAsInt();
                        int newConfirm = result.get(0).getAsJsonObject().get("new_case").getAsInt();
                        int newRecoverCase = result.get(0).getAsJsonObject().get("new_recovered").getAsInt();
                        //int hospitalCase = result.get(0).getAsJsonObject().get("new_case").getAsInt();
                        int death = result.get(0).getAsJsonObject().get("new_death").getAsInt();
                        String updateDate = result.get(0).getAsJsonObject().get("update_date").getAsString();
                        tvTotalCase.setText(String.valueOf(totalCase) + " คน");
                        tvNewConfirm.setText("(เพิ่มขึ้น " + String.valueOf(newConfirm) + " คน)");
                        tvNewReCoverCase.setText(String.valueOf(newRecoverCase));
                        //tvHospitalCase.setText(String.valueOf(hospitalCase));
                        tvDeath.setText(String.valueOf(death));
                        tvUpdateDate.setText("ข้อมูลเมื่อ " + updateDate);
                    }
                });
        preferenceFromUser(layout);
        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });
    }

    private void preferenceFromUser(LinearLayout layout) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.contains("theme")) {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("theme", false);
        }
        if (sharedPreferences.getBoolean("theme", false)) {
            layout.setBackgroundColor(getResources().getColor(R.color.purple_200));
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void createMyFile(String path) throws IOException {
        File myTextFile = new File(path + "/20210717_2.text");
        boolean isMyTextFileCreateSuccess;
        if (!myTextFile.exists()) {
            try {
                isMyTextFileCreateSuccess = myTextFile.createNewFile();
                Toast.makeText(this, "" + "create text file complete at path :" + myTextFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                writeToTextFile(myTextFile);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + "create text file don't complete :", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "" + "create text file don't complete: xxx", Toast.LENGTH_SHORT).show();
            writeToTextFile(myTextFile);
        }
    }

    private void writeToTextFile(File myTextFile) throws IOException {
        Writer writer = new FileWriter(myTextFile);
        String textForTest = "\nThis is Text to write into this text file";
        writer.write(textForTest);
        Toast.makeText(this, "" + "write data to file complete " + myTextFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        writer.close();
        readFromMyFile(myTextFile);
    }

    private void readFromMyFile(File myTextFile) throws IOException {
        Reader reader = new FileReader(myTextFile);
        int ch = 0;
        StringBuilder strr = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            strr.append((char) ch);
        }
        Toast.makeText(this, "the data from text file :" + strr, Toast.LENGTH_SHORT).show();
        reader.close();
    }

    private void writeFileToSDCard() {
        String externalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test.text";
        File externalTextFile = new File(externalPath);
        if (Environment.getExternalStorageState(Environment.getExternalStorageDirectory()).equals(Environment.MEDIA_MOUNTED)) {
            try {
                boolean isCompleteCreateExternalTextFile = externalTextFile.createNewFile();
                Snackbar.make(banner, "สร้างไฟล์ภายนอกสำเร็จ", Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Snackbar.make(banner, "ไม่พบ SD CARD", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void testPureJson() throws IOException, JSONException {
        URL url = new URL("https://covid19.th-stat.com/json/covid19v2/getTodayCases.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream in = conn.getInputStream();
        int index = 0;
        StringBuilder result = new StringBuilder();
        byte[] data = new byte[1024];
        while ((index = in.read(data)) != -1) {
            result.append(data);
        }
        JSONObject object = new JSONObject(String.valueOf(result));
        String sttt = object.getString("Confirmed");
        Toast.makeText(this, "sttt :" + sttt, Toast.LENGTH_SHORT).show();
    }
}