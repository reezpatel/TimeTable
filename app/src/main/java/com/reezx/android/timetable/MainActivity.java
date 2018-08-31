package com.reezx.android.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener{

    private final int[] NAV_ICONS_LOGIN = new int[]{R.drawable.ic_home,R.drawable.ic_time_table,R.drawable.ic_subjects,R.drawable.ic_user,R.drawable.ic_menu_share,R.drawable.ic_menu_send};
    private final int[] NAV_ICONS = new int[]{R.drawable.ic_user,R.drawable.ic_menu_share,R.drawable.ic_menu_send};
    private final String[] NAV_TEXT_LOGIN = new String[] {"Home","Time Table","Subjects","Logout","Share","Feedback"};
    private final String[] NAV_TEXT = new String[] {"Login","Share","Feedback"};

    private final String ICON = "icon";
    private final String TEXT = "text";

    private final String TAG = "MainActivity.";

    private ListView listView;

    private final int LOGIN_FRAGMENT = 1;
    private final int HOME_FRAGMENT = 2;
    private final int TIME_TABLE_FRAGMENT = 3;
    private final int SUBJECT_FRAGMENT = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        listView = (ListView) findViewById(R.id.ListViewNavigationView);
        renderDrawer(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(SharedPrefManager.isLoggedIn(MainActivity.this)) {
                    switch (position) {
                        case 0: changeFragment(HOME_FRAGMENT);
                            break;
                        case 1: changeFragment(TIME_TABLE_FRAGMENT);
                            break;
                        case 2: changeFragment(SUBJECT_FRAGMENT);
                            break;
                        case 3: doLogout();
                            renderUI();
                            break;
                    }
                }else {
                    switch (position) {
                        case 0:
                            changeFragment(LOGIN_FRAGMENT);
                            break;
                        case 1: onShareClicked();
                            break;
                        case 2: onFeedbackClicked();
                            break;
                    }
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if(SharedPrefManager.isLoggedIn(this)) {
            //Logged In Frame
            fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_LOGED_IN_FRAGMENT);
        }else {
            //Logged out Frame
            fragment = new LoginFragment();
            fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_LOGED_OUT_FRAGMENT);
        }
        fragmentTransaction.commit();

        renderUI();
    }

    public void renderDrawer(ListView listView) {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


        if(SharedPrefManager.isLoggedIn(this)) {
            for (int i=0;i<NAV_ICONS_LOGIN.length;i++) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(ICON,Integer.toString(NAV_ICONS_LOGIN[i]));
                hashMap.put(TEXT,NAV_TEXT_LOGIN[i]);
                arrayList.add(hashMap);
            }
        }else {
            for (int i=0;i<NAV_ICONS.length;i++) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(ICON,Integer.toString(NAV_ICONS[i]));
                hashMap.put(TEXT,NAV_TEXT[i]);
                arrayList.add(hashMap);
            }
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.navigation_view_list_view,new String[]{ICON,TEXT},new int[]{R.id.icon,R.id.name});
        listView.setAdapter(simpleAdapter);
    }

    public void onShareClicked() {
        Log.d("TAG","On Share Clicked");
    }

    public void onFeedbackClicked() {
        Log.d("TAG","On Feedback Clicked");
    }

    public void doLogout() {
        new AlertDialog.Builder(this).setTitle("Logout?").setMessage("Are you sure to Logout?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.removeLogin(MainActivity.this);
                        changeFragment(LOGIN_FRAGMENT);
                        renderDrawer(listView);
                        renderUI();
                    }
                }).setNegativeButton("NO",null).show();
    }

    public void changeFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (id) {
            case HOME_FRAGMENT:
                fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_LOGED_IN_FRAGMENT);
                break;
            case LOGIN_FRAGMENT:
                fragment = new LoginFragment();
                fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_LOGED_OUT_FRAGMENT);
                break;
            case TIME_TABLE_FRAGMENT:

                fragment = new TimeTableFragment();
                fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_TIME_TABLE_FRAGMENT);
                break;
            case SUBJECT_FRAGMENT:
                fragment = new SubjectsFragment();
                fragmentTransaction.replace(R.id.FrameLayoutMainActivity,fragment,Constants.TAG_SUBJECTS_FRAGMENT);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG",""+requestCode);
        if(requestCode == Constants.SIGNIN_REQUEST_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if(googleSignInAccount != null) {
                    firebaseAuthWithGoogle(googleSignInAccount);
                    String id = googleSignInAccount.getId();
                    String name = googleSignInAccount.getDisplayName();
                    String email = googleSignInAccount.getEmail();
                    Uri photoURL = googleSignInAccount.getPhotoUrl();
                    SharedPrefManager.setLogin(this,id,name,email,photoURL);
                    changeFragment(HOME_FRAGMENT);
                    renderDrawer(listView);
                    renderUI();
                }
            }else {
                Toast.makeText(this,"Login Failed. Try Again!!!",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    void renderUI() {
        TextView textView = (TextView) findViewById(R.id.textViewNameField);
        if(SharedPrefManager.isLoggedIn(this)) {
            Bundle bundle = SharedPrefManager.getUserData(this);
            String name = bundle.getString(Constants.TEXT_NAME);
            textView.setText(name);
        }else {
            textView.setText(R.string.author);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoginStatusChange(boolean status) {

    }

}
