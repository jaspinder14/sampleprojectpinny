package com.mogo.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Window;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.model.forgot.ForgotPrp;
import com.mogo.view.customcontrols.ButtonRegular;
import com.mogo.view.customcontrols.EditTextRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.emailAddressET)
    EditTextRegular emailAddressET;


    @BindView(R.id.submitBT)
    ButtonRegular submitBT;

    public static ForgotPasswordActivity instance;


    public static ForgotPasswordActivity getinstance(){
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        instance = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.submitBT)
    public void submitVlick(){

        String email = emailAddressET.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(instance, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(instance, getString(R.string.emailformat), Toast.LENGTH_SHORT).show();
        }
        else {

            Dialogs.baseShowProgressDialog(this, getString(R.string.loading));
            WebServiceResult.forgotPassword(email);
        }


    }

    public void forgotResponse(ForgotPrp forgotPrp){
        Dialogs.baseHideProgressDialog();
        if (forgotPrp!=null){
            if (forgotPrp.getResult().getStatus().equals("1")){
                Dialogs.showToast(this, forgotPrp.getResult().getMessage());
                Intent intent =new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Dialogs.showToast(this, forgotPrp.getResult().getMessage());
            }
        }
        else {
            Dialogs.showToast(this, getString(R.string.somethingwrong));
        }
    }
        }


