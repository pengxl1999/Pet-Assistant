package com.pengxl.petassistant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class PetInfomationActivity extends AppCompatActivity {

    private EditText name, age, gender, height, weight, species;
    private String mName, mAge, mGender, mHeight, mWeight, mSpecies;
    private Button edit, save;
    private ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petinfomation);

        init();
    }

    private void init() {
        name = (EditText) findViewById(R.id.petinfo_name);
        age = (EditText) findViewById(R.id.petinfo_age);
        gender = (EditText) findViewById(R.id.petinfo_gender);
        height = (EditText) findViewById(R.id.petinfo_height);
        weight = (EditText) findViewById(R.id.petinfo_weight);
        species = (EditText) findViewById(R.id.petinfo_species);
        edit = (Button) findViewById(R.id.petinfo_edit);
        save = (Button) findViewById(R.id.petinfo_save);
        back = (ImageButton) findViewById(R.id.petinfo_back);

        name.setEnabled(false);
        age.setEnabled(false);
        gender.setEnabled(false);
        height.setEnabled(false);
        weight.setEnabled(false);
        species.setEnabled(false);

        mName = name.getText().toString();
        mAge = age.getText().toString();
        mGender = gender.getText().toString();
        mHeight = height.getText().toString();
        mWeight = weight.getText().toString();
        mSpecies = species.getText().toString();

        String s = Storage.readPetFile();
        String[] message = s.split(" ");
        if(message.length == 6) {
            mName = message[0];
            mAge = message[1];
            mGender = message[2];
            mHeight = message[3];
            mWeight = message[4];
            mSpecies = message[5];
            name.setText(mName);
            age.setText(mAge);
            gender.setText(mGender);
            height.setText(mHeight);
            weight.setText(mWeight);
            species.setText(mSpecies);
            name.setTextColor(Color.rgb(0, 0, 0));
            age.setTextColor(Color.rgb(0, 0, 0));
            gender.setTextColor(Color.rgb(0, 0, 0));
            height.setTextColor(Color.rgb(0, 0, 0));
            weight.setTextColor(Color.rgb(0, 0, 0));
            species.setTextColor(Color.rgb(0, 0, 0));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit.isActivated()) {
                    edit.setActivated(false);
                    name.setEnabled(false);
                    name.setTextColor(Color.rgb(0, 0, 0));
                    age.setEnabled(false);
                    age.setTextColor(Color.rgb(0, 0, 0));
                    gender.setEnabled(false);
                    gender.setTextColor(Color.rgb(0, 0, 0));
                    height.setEnabled(false);
                    height.setTextColor(Color.rgb(0, 0, 0));
                    weight.setEnabled(false);
                    weight.setTextColor(Color.rgb(0, 0, 0));
                    species.setEnabled(false);
                    species.setTextColor(Color.rgb(0, 0, 0));
                    name.setText(mName);
                    age.setText(mAge);
                    gender.setText(mGender);
                    height.setText(mHeight);
                    weight.setText(mWeight);
                    species.setText(mSpecies);
                    edit.setText("编辑");
                }
                else {
                    edit.setActivated(true);
                    name.setEnabled(true);
                    age.setEnabled(true);
                    gender.setEnabled(true);
                    height.setEnabled(true);
                    weight.setEnabled(true);
                    species.setEnabled(true);
                    edit.setText("取消");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setActivated(false);
                name.setEnabled(false);
                name.setTextColor(Color.rgb(0, 0, 0));
                age.setEnabled(false);
                age.setTextColor(Color.rgb(0, 0, 0));
                gender.setEnabled(false);
                gender.setTextColor(Color.rgb(0, 0, 0));
                height.setEnabled(false);
                height.setTextColor(Color.rgb(0, 0, 0));
                weight.setEnabled(false);
                weight.setTextColor(Color.rgb(0, 0, 0));
                species.setEnabled(false);
                species.setTextColor(Color.rgb(0, 0, 0));
                edit.setText("编辑");
                mName = name.getText().toString();
                mAge = age.getText().toString();
                mGender = gender.getText().toString();
                mHeight = height.getText().toString();
                mWeight = weight.getText().toString();
                mSpecies = species.getText().toString();
                String message = mName + " " + mAge + " " + mGender + " " + mHeight + " " + mWeight + " " + mSpecies;
                Storage.writePetFile(message);
            }
        });
    }
}
