package com.example.thomas.familyheroes;

/**
 * Created by Thomas on 09/10/2014.
 */
import static com.example.thomas.familyheroes.CommonUtilities.DISPLAY_MESSAGE_ACTION;


import static com.example.thomas.familyheroes.CommonUtilities.EXTRA_MESSAGE;
import static com.example.thomas.familyheroes.CommonUtilities.SENDER_ID;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gcm.GCMRegistrar;

import com.example.thomas.familyheroes.NavDrawerListAdapter;
import com.example.thomas.familyheroes.NavDrawerItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;


    public static String login;
    public static String mdp;

    public static String newMessage;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    //private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    TextView txtProfil;
    TextView txtRythme;
    TextView txtRythme1;
    TextView txtRythme2;
    TextView txtTension;
    TextView txtTension1;
    TextView txtTension2;
    TextView txtTemperature;
    TextView txtTemperature1;
    TextView txtTemperature2;

    ImageView imageProfil;
    ImageView imageRythme;
    ImageView imageTension;
    ImageView imageTemperature;
    ImageView backProfil;
    ImageView fleche;


    String id_personne_age = "1";

    JSONParser jsonParser = new JSONParser();

    private static final String url_sauvegarde_details = "http://thomaslanternier.fr/family_heroes/src/getSauvegarde.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "signe_vitaux";
    private static final String TAG_PID = "id_personne_age";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy);
        getActionBar().setTitle("");

        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflator.inflate(R.layout.view_logo, null);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER );

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(view, params);

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Getting name, email from intent
        Intent i = getIntent();

        login = i.getStringExtra("login");
        mdp = i.getStringExtra("mdp");

        // Make sure the device has the proper dependencies.
        //GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        // GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM ", Toast.LENGTH_LONG).show();



            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, login, mdp, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        //navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.nothing, // nav drawer open - description for accessibility
                R.string.nothing // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessages = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            newMessage = newMessages;
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                 new getSauvegardeDetails().execute();
                break;
            case 1:
                fragment = new FindPeopleFragment();
                break;
            case 2:
                fragment = new PhotosFragment();
                break;
            case 3:
                fragment = new CommunityFragment();
                break;
            case 4:
                fragment = new PagesFragment();
                break;
            case 5:
                fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();



            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    class getSauvegardeDetails extends AsyncTask<String, String, String> {


        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id_personne_age", id_personne_age));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_sauvegarde_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Sauvegarde Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray sauvegardeObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject sauvegarde = sauvegardeObj.getJSONObject(0);


                            txtProfil = (TextView) findViewById(R.id.profil);
                            txtRythme = (TextView) findViewById(R.id.rythme_cardiaque);
                            txtRythme1 = (TextView) findViewById(R.id.txtRythme);
                            txtRythme2 = (TextView) findViewById(R.id.txtRythme2);
                            txtTemperature = (TextView) findViewById(R.id.temperature);
                            txtTemperature1 = (TextView) findViewById(R.id.txtTemperature1);
                            txtTemperature2 = (TextView) findViewById(R.id.txtTemperature2);
                            txtTension = (TextView) findViewById(R.id.tension);
                            txtTension1 = (TextView) findViewById(R.id.txtTension);
                            txtTension2 = (TextView) findViewById(R.id.txtTension2);

                            imageProfil = (ImageView) findViewById(R.id.imageProfil);
                            imageRythme = (ImageView) findViewById(R.id.imageRythme);
                            imageTension = (ImageView) findViewById(R.id.imageTension);
                            imageTemperature = (ImageView) findViewById(R.id.imageTemp);
                            backProfil = (ImageView) findViewById(R.id.backProfil);
                            fleche = (ImageView) findViewById(R.id.flecheTension);

                            int rythme = Integer.parseInt(sauvegarde.getString("rythme_cardiaque"));
                            int temperature = Integer.parseInt(sauvegarde.getString("temperature"));
                            int tension = Integer.parseInt(sauvegarde.getString("tension"));

                            imageProfil.setImageResource(R.drawable.profil);
                            String url = "http://www.thomaslanternier.fr/family_heroes/avatar/profil-mamie.jpg";

                            try {
                                URL newurl = new URL(url);
                                backProfil.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            if(rythme < 70)
                            {
                                imageRythme.setImageResource(R.drawable.rc_bleu);
                                txtRythme1.setText("Rythme cardiaque : Faible");
                            }
                            else if(rythme >= 70 && rythme <= 110)
                            {
                                imageRythme.setImageResource(R.drawable.rc_vert);
                                txtRythme1.setText("Rythme cardiaque : Normal");
                            }
                            else if(rythme > 110)
                            {
                                imageRythme.setImageResource(R.drawable.rc_rouge);
                                txtRythme1.setText("Rythme cardiaque : Elevé");
                            }
                            txtRythme.setText(""+rythme);
                            txtRythme2.setText(""+rythme+" battements");

                            if(temperature <= 36)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_bas);
                                txtTemperature1.setText("Température : Basse");
                            }
                            else if(temperature>36 && temperature<=38)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_moyen);
                                txtTemperature1.setText("Température : Normale");
                            }
                            else if(temperature>38)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_haut);
                                txtTemperature1.setText("Température : Haute");
                            }
                            txtTemperature.setText(""+temperature);
                            txtTemperature2.setText(""+temperature+"° celsius");

                            imageTension.setImageResource(R.drawable.tension);
                            fleche.setImageResource(R.drawable.tension_fleche);

                            txtTension.setText(""+(tension/10));

                            if((tension/10)<9)
                            {
                                txtTension1.setText("Tension : Faible");
                                fleche.setRotation(-60);
                                fleche.setTranslationX(-100);
                                fleche.setTranslationY(60);
                            }
                            else if((tension/10) >=9 && (tension/10) <=15)
                            {
                                txtTension1.setText("Tension : Normale");
                                fleche.setRotation(0);

                            }
                            else if((tension/10) > 15)
                            {
                                txtTension1.setText("Tension : Elevée");

                                fleche.setRotation(60);
                                fleche.setTranslationX(100);
                                fleche.setTranslationY(60);
                            }

                            txtTension2.setText(""+tension+" mm de mercure");


                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


    }
}
