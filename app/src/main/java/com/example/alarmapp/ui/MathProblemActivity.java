package com.example.alarmapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import com.example.alarmapp.R;
import com.example.alarmapp.databinding.ActivityMathProblemBinding;
import com.example.alarmapp.databinding.ActivityRingBinding;

public class MathProblemActivity extends AppCompatActivity {
    ActivityMathProblemBinding binding;
    String op[] = {"+", "-", "*"};
    String randOp;
    int firstNum;
    int secondNum;
    int res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_problem);

        binding = ActivityMathProblemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        randOp();
        randNum();
        calculateRes();

        binding.edtRes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = binding.edtRes.getText().toString();
                int curRes;
                if(userInput != null && !userInput.isEmpty()) {
                     curRes = Integer.parseInt(userInput);


                    if(curRes == res) {
                        Intent intentRes = new Intent();
                        setResult(Activity.RESULT_OK, intentRes);
                        finish();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Make keyboard contains only numbers
        binding.edtRes.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    }

    private void randOp() {
        int max = 0;
        int min = 2;
        int randOpIndex = (int)Math.floor(Math.random() * (max - min + 1) + min);
        randOp = op[randOpIndex];
        binding.txtFirstOp.setText(randOp);
    }

    private void randNum() {
        int max = 10;
        int min = 90;
        firstNum = (int)Math.floor(Math.random() * (max - min + 1) + min);
        if(randOp == "-") {
            min = firstNum;
        }
        secondNum = (int)Math.floor(Math.random() * (max - min + 1) + min);
        binding.txtFirstNum.setText(Integer.toString(firstNum));
        binding.txtSecondNum.setText(Integer.toString(secondNum));
    }

    private void calculateRes() {
//        switch (randOp) {
//            case "+":
//                res = firstNum + secondNum;
//                break;
//            case "-":
//                res = firstNum - secondNum;
//                break;
//            case "*":
//                res = firstNum * secondNum;
//                break;
//
        if(randOp.equals("+")) {
            res = firstNum + secondNum;

        }

        else if(randOp.equals("-")) {
            res = firstNum - secondNum;

        }
        else if(randOp.equals("*")) {

        }
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}