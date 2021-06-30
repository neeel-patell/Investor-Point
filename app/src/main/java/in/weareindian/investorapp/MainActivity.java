package in.weareindian.investorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.weareindian.investorapp.ui.business.BusinessFragment;
import in.weareindian.investorapp.R;
import in.weareindian.investorapp.ui.defense.DefenseFragment;
import in.weareindian.investorapp.ui.home.HomeFragment;
import in.weareindian.investorapp.ui.india.IndiaFragment;
import in.weareindian.investorapp.ui.lists.ListsFragment;
import in.weareindian.investorapp.ui.prime.PrimeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawerLayout;
    LinearLayout customToolbar;
    TextView tvHome, tvBusiness, tvDefense, tvPrime, tvLists, tvBlog, tvIndia;
    LinearLayout llMain;
    ImageView ivMenu;
    TextView tvTitle, tvMainTitle;

    BottomNavigationView navView;

    //variables for notification activity
    RelativeLayout rlNotification;
    ImageView ivNotification;
    TextView tvNotificationCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Init();
        SetDrawer();
        getPermission();

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawers();
                else
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });



        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_home);
                replaceFragment(new HomeFragment());

            }
        });

        tvBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_mutual);
                replaceFragment(new BusinessFragment());
            }
        });

        tvDefense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_ipos);
                replaceFragment(new DefenseFragment());
            }
        });

        tvPrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_prime);
                replaceFragment(new PrimeFragment());
            }
        });

        tvLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_home);
                replaceFragment(new ListsFragment());
                // from here we will call others fragment
            }
        });

//        tvBlog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//                navView.setSelectedItemId(R.id.navigation_home);
//                replaceFragment(new BlogFragment());
//            }
//        });

        tvIndia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                navView.setSelectedItemId(R.id.navigation_stocks);
                replaceFragment(new IndiaFragment());
            }
        });


    }

    private void getPermission() {
    }

    private void SetDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //CommonUtils.hideSoftKeyboard(MainActivity.this);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void Init() {

        drawerLayout = findViewById(R.id.drawerLayout);
        customToolbar = findViewById(R.id.customToolbar);
        tvHome = findViewById(R.id.tvHome);
        tvBusiness = findViewById(R.id.tvBusiness);
        tvDefense = findViewById(R.id.tvDefense);
        tvPrime = findViewById(R.id.tvPrime);
        tvLists = findViewById(R.id.tvLists);
        tvIndia = findViewById(R.id.tvIndia);
//        tvBlog = findViewById(R.id.tvBlog);
        llMain = findViewById(R.id.llMain);
        ivMenu = findViewById(R.id.ivMenu);
        /*tvTitle = findViewById(R.id.tvTitle);
        tvMainTitle = findViewById(R.id.tvMainTitle);*/

        rlNotification = findViewById(R.id.rlNotification);
        ivNotification = findViewById(R.id.ivNotification);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_stocks, R.id.navigation_mutual, R.id.navigation_ipos, R.id.navigation_prime)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void replaceFragment(final Fragment fragment) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String backStateName = fragment.getClass().getName();
                FragmentManager manager = getSupportFragmentManager();

                //boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.nav_host_fragment, fragment, backStateName);
                    ft.addToBackStack(backStateName);
                    ft.commit();

            }
        }, 200);
    }

}