package com.mogo.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.MultipartUtility;
import com.mogo.controller.SharedPref;
import com.mogo.model.registerprop.RegisterProp;
import com.mogo.view.customcontrols.EditTextRegular;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {


    public static SignUpActivity instance;

    @BindView(R.id.userNameET)
    EditTextRegular userNameET;
    @BindView(R.id.emailAddressET)
    EditTextRegular emailAddressET;
    @BindView(R.id.passwordET)
    EditTextRegular passwordET;
    @BindView(R.id.confirmPasswordET)
    EditTextRegular confirmPasswordET;


    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private static File file;

    private static File dir;

    String userName, emailAddress, password, confirmPassword;
    private MultipartUtility multipart;

    private String picturePath = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String userId;


    public static SignUpActivity getinstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        createDirIfNotExists("Mogo");
        instance = this;

        sharedPreferences = getApplicationContext().getSharedPreferences(
                "Mogo_Pref", MODE_PRIVATE);

        editor = sharedPreferences.edit();

    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        finish();
    }


    public void Signup(View view) {

        userName = userNameET.getText().toString().trim();
        emailAddress = emailAddressET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        confirmPassword = confirmPasswordET.getText().toString().trim();

        if (userName.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enterUserName));
        } else if (emailAddress.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enterEmailAddress));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Dialogs.showToast(this, getString(R.string.validEmailAddress));
        } else if (password.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enterPassword));
        } else if (confirmPassword.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enterPassword));
        } else if (!confirmPassword.matches(password)) {
            Dialogs.showToast(this, getString(R.string.passwordsNotMatch));
        } else {
            //   Dialogs.baseShowProgressDialog(this, "Loading...");
            //  new uploadProfile().execute();

            if (picturePath.equalsIgnoreCase("")) {
                Dialogs.baseShowProgressDialog(SignUpActivity.this, "Loading...");
                WebServiceResult.registerService(userName, emailAddress, password, FirebaseInstanceId.getInstance().getToken(), null);
            } else {
                Dialogs.baseShowProgressDialog(SignUpActivity.this, "Loading...");
                WebServiceResult.registerService(userName, emailAddress, password, FirebaseInstanceId.getInstance().getToken(), new File(picturePath));
            }


        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        dir = new File(Environment.getExternalStorageDirectory(), path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e("Failed :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }


    @OnClick(R.id.addImageIV)
    public void onAddImageClick() {
        Dialogs.profiledialog(SignUpActivity.this);
    }


    public void galleryImagePick() {

        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takePictureIntent, 2);

    }


    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(dir, "Profile_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
        Uri fileUri = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Dialogs.dismissdialog();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            picturePath = Uri.fromFile(file).getPath();
            profile_image.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), 100, 100));

        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            try (Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null)) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            profile_image.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(picturePath), 100, 100));
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


  /*  public class uploadProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialogs.progressdialog(SignUpActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            if (!picturePath.equals("")) {
                try {
                    multipart = new MultipartUtility("http://live.csdevhub.com/mogoapp/ws/register.php", "UTF-8");
                    multipart.addFormField("userName", userName);
                    multipart.addFormField("email", emailAddress);
                    multipart.addFormField("password", password);
                    multipart.addFormField("deviceToken", SharedPref.getInstance(getApplicationContext()).getString("token"));
                    multipart.addFilePart("image", new File(picturePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Dialogs.removedialog();
            Dialogs.showToast(SignUpActivity.this, "Profile Created Successfully");

            super.onPostExecute(s);
        }
    }*/


    public void register(RegisterProp registerProp) {
        if (registerProp != null) {
            Dialogs.baseHideProgressDialog();
            if (registerProp.getResult().getStatus().equalsIgnoreCase("1")) {
                SharedPref.getInstance(getApplicationContext()).setString("userId", registerProp.getResult().getData().getUserId());
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);
                editor.putString("UserName", registerProp.getResult().getData().getName());
                editor.putString("UserEmail", registerProp.getResult().getData().getEmail());
                editor.putString("loggedin", "isLoggedin");
                editor.commit();
                finish();
            }
        } else {
            Dialogs.showToast(SignUpActivity.this, "Something Went Wrong...");
        }

    }


}
