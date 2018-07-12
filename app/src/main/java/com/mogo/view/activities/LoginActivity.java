package com.mogo.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.login.LoginPrp;
import com.mogo.view.customcontrols.ButtonRegular;
import com.mogo.view.customcontrols.EditTextRegular;
import com.mogo.view.customcontrols.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static LoginActivity instance;


    @BindView(R.id.emailAddressET)
    EditTextRegular emailAddressET;

    @BindView(R.id.passwordET)
    EditTextRegular passwordET;

    @BindView(R.id.signUpTV)
    TextViewRegular signUpTV;

    @BindView(R.id.forgotPasswordTV)
    TextViewRegular forgotPasswordTV;

    @BindView(R.id.googlePlusIB)
    ImageButton googlePlusIB;

    @BindView(R.id.facebookButtonIB)
    ImageButton facebookButtonIB;

    @BindView(R.id.loginBT)
    ButtonRegular loginBT;

    @BindView(R.id.aboutDescriptionTV)
    TextViewRegular aboutDescriptionTV;
    private String fbid;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private String namefacebook;
    private String emailfacebook;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private String emailgoogle;
    private String googlesocialid;
    private String personName;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Uri gmailimage;

    public static LoginActivity getInstances() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);


        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(
                "Mogo_Pref", MODE_PRIVATE);

        editor = sharedPreferences.edit();

        hashKey();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("tag", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }

    private void hashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mogo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }

    public static LoginActivity getInstance() {
        return instance;
    }


    public void ToSignUpClass(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void ForgotScreen(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.facebookButtonIB)
    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
        loginfacebook();
    }

    @OnClick(R.id.googlePlusIB)
    public void googleLogin() {
        signIn();
    }

    private void loginfacebook() {

        callbackManager = CallbackManager.Factory.create();

        loginManager = LoginManager.getInstance();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //progress.show();

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String profilePicUrl = null;
                                try {
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Log.d("imagepath", profilePicUrl);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (object.has("id")) {
                                    try {
                                        fbid = object.getString("id");
                                        Log.d("fbid============", fbid);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (object.has("name")) {
                                    try {
                                        namefacebook = object.getString("name");
                                        Log.d("lllllllll", namefacebook);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (object.has("email")) {
                                    try {
                                        emailfacebook = object.getString("email");
                                        Log.d("outttt", emailfacebook);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    emailfacebook = "";
                                }

                                if (!fbid.equalsIgnoreCase("")) {
                                    WebServiceResult.loginService(emailfacebook, "", namefacebook, profilePicUrl, "facebook", fbid, FirebaseInstanceId.getInstance().getToken());
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,birthday,picture,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        } catch (IllegalStateException e) {

        } catch (Exception e) {

        }

        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("tag", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("tag", "signInWithCredential:onComplete:" + task.isSuccessful());
                        Log.d("googleid", acct.getId());

                        emailgoogle = acct.getEmail();
                        googlesocialid = acct.getId();
                        personName = acct.getDisplayName();
                        gmailimage = acct.getPhotoUrl();
                        // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        WebServiceResult.loginService(emailgoogle, "", personName, gmailimage.toString(), "gmail", googlesocialid, FirebaseInstanceId.getInstance().getToken());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        if (!task.isSuccessful()) {
                            Log.w("tag", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @OnClick(R.id.loginBT)
    public void checkValidatrions() {

        String email = emailAddressET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if (email.isEmpty() && password.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.fields));
        } else if (email.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enteremail));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Dialogs.showToast(this, getString(R.string.emailformat));
        } else if (password.isEmpty()) {
            Dialogs.showToast(this, getString(R.string.enterpassword));
        } else {
            Dialogs.baseShowProgressDialog(this, getString(R.string.loading));
            WebServiceResult.loginService(email, password, "", "", "native", "", FirebaseInstanceId.getInstance().getToken());
        }
    }

    public void loginResponse(LoginPrp loginPrp) {
//        Dialogs.baseHideProgressDialog();
        if (loginPrp != null) {
            if (loginPrp.getResult().getStatus().equals("1")) {
                Dialogs.showToast(this, loginPrp.getResult().getMessage());
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                SharedPref.getInstance(LoginActivity.this).setString("userId", loginPrp.getResult().getData().getUserId());
                startActivity(intent);
                editor.putString("UserName", loginPrp.getResult().getData().getName());
                editor.putString("UserEmail", loginPrp.getResult().getData().getEmail());
                editor.putString("loggedin", "isLoggedin");
                editor.commit();
                finish();
            } else {
                Dialogs.baseHideProgressDialog();
                Dialogs.showToast(this, loginPrp.getResult().getMessage());
            }
        } else {
            Dialogs.baseHideProgressDialog();
            Dialogs.showToast(this, getString(R.string.somethingwrong));
        }

    }


}
