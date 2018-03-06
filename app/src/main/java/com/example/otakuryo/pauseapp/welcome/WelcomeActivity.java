package com.example.otakuryo.pauseapp.welcome;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.R;

public class WelcomeActivity extends AppCompatActivity {


    public static int page = 0;
    public static int paso = 0;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color15));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWelcome);
        //instanciamos el boton de + y lo ocultamos solo para la primera pantalla.
        final FloatingActionButton fab2 = findViewById(R.id.fab_playlist_welcome);
        final TextView textButtom3 = findViewById(R.id.info3);
        final TextView textButtom4 = findViewById(R.id.info4);
        fab2.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paso == 5){
                    mViewPager.setCurrentItem(6);
                    onBackPressed();
                    paso++;
                }
                if (paso == 4){
                    mViewPager.setCurrentItem(5);
                    paso++;
                }
                if (paso == 0){
                    mViewPager.setCurrentItem(1);
                    textButtom3.setText("Manten presionado este botton");
                    paso++;
                }
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (paso == 1){
                    Toast.makeText(WelcomeActivity.this, "Buen Trabajo...", Toast.LENGTH_LONG).show();
                    fab2.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    mViewPager.setCurrentItem(2);
                    textButtom3.setVisibility(View.GONE);
                    textButtom4.setVisibility(View.VISIBLE);
                    paso++;
                }
                return false;
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paso == 3){
                    Toast.makeText(WelcomeActivity.this, "Bien Echo", Toast.LENGTH_LONG).show();
                    fab2.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    mViewPager.setCurrentItem(4);
                    textButtom3.setVisibility(View.GONE);
                    textButtom4.setVisibility(View.GONE);
                    paso++;
                }
                if (paso == 2){
                    Toast.makeText(WelcomeActivity.this, "Excelente", Toast.LENGTH_LONG).show();
                    mViewPager.setCurrentItem(3);
                    paso++;
                }
            }
        });
        switch (paso){
            case 0:

                break;
            case 1:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            //Desde es punto mostraremos y ocultaremos las imagenes

            final ImageView imageView = rootView.findViewById(R.id.welcomeImg);
            final TextView textButtom2 = rootView.findViewById(R.id.info2);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0:
                    WelcomeActivity.page = 0;
                    break;
                case 1:
                    WelcomeActivity.page = 1;
                    textButtom2.setText("Bienvenido a PauseApp");
                    imageView.setImageResource(R.drawable.pause_logo);
                    break;
                case 2:
                    WelcomeActivity.page = 2;
                    break;
                case 3:
                    //imageView.setVisibility(View.GONE);
                    WelcomeActivity.page = 3;
                    textButtom2.setText("Para crear una PLAYLIST solo tendremos que presionar el boton.");
                    imageView.setImageResource(R.drawable.select_false);
                    break;
                case 4:
                    WelcomeActivity.page = 4;
                    textButtom2.setText("Para guardar la PLAYLIST volveremos a presionar el boton.");
                    imageView.setImageResource(R.drawable.select_true);
                    break;
                case 5:
                    WelcomeActivity.page = 5;
                    textButtom2.setText("No se olvide agregar una o mas carpetas de musica!");
                    imageView.setImageResource(R.drawable.folder_select);
                    break;
                case 6:
                    WelcomeActivity.page = 6;
                    textButtom2.setText("Ahora que conoce lo basico de este fabuloso reproductor, a escuchar musica sin parar!");
                    imageView.setImageResource(R.drawable.finish);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragmentNow = PlaceholderFragment.newInstance(position + 1);
            //ImageView imageView = findViewById(R.id.welcomeImg);
            return fragmentNow;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
