package com.menlopark.rentsyuser.ui.activitys;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartRes;
import com.menlopark.rentsyuser.model.login.LoginModel;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.BaseActivity;
import com.menlopark.rentsyuser.ui.fragments.CalenderFragment;
import com.menlopark.rentsyuser.ui.fragments.CartFragment;
import com.menlopark.rentsyuser.ui.fragments.CategoryFragment;
import com.menlopark.rentsyuser.ui.fragments.FavouriteFragment;
import com.menlopark.rentsyuser.ui.fragments.NotificationFragment;
import com.menlopark.rentsyuser.ui.fragments.PendingBookingFragment;
import com.menlopark.rentsyuser.ui.fragments.PromotionFragment;
import com.menlopark.rentsyuser.ui.fragments.RatingFragment;
import com.menlopark.rentsyuser.ui.fragments.SettingFragment;
import com.menlopark.rentsyuser.util.FCViewPager;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.view.adapters.ViewPagerAdapter;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    TextView tabCategory, tabSubCategory, tabPrice;
    Config mConfig;
    String location_name, location_id;
    Activity act;
    ApiService apiService;
    FrameLayout main_fragment;
    FCViewPager viewpager;
    TabClick tabClick;
    CategoryFragment categoryFragment;
    //    TabLayout tablayout;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    Fragment page;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View appbar;
    private NavigationView navView;
    private MenuItem cartMenu;
    private MenuItem helpMenu;
    private LinearLayout tabLinear;
    public static String screenName="Category";


    private void assignViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        appbar = findViewById(R.id.appbar);
        navView = findViewById(R.id.nav_view);
        main_fragment = appbar.findViewById(R.id.main_fragment);
        viewpager = appbar.findViewById(R.id.viewpager);


        viewpager.setEnableSwipe(false);
        //tablayout = appbar.findViewById(R.id.tablayout);
        tabLinear = appbar.findViewById(R.id.tabLinear);
        tabCategory = appbar.findViewById(R.id.tabCategory);
        tabSubCategory = appbar.findViewById(R.id.tabSubCategory);
        tabPrice = appbar.findViewById(R.id.tabPrice);

    }

    public void setTabText(int tabIndex, String text) {
        if (tabIndex == 1)
            tabCategory.setText((text.equals("All") ? getString(R.string.category) : text));
        if (tabIndex == 2)
            tabSubCategory.setText((text.equals("All") ? getString(R.string.sub_category) : text));
        if (tabIndex == 3)
            tabPrice.setText((text.equals("All") ? getString(R.string.price) : text));
    }

    public int getViewPagerVisibility() {
        return viewpager.getVisibility();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        act = MainActivity.this;
        if (getIntent().getExtras() != null && getIntent().getExtras().getString(getString(R.string.INTENT_LOCATION)) != null) {
            Bundle b = getIntent().getExtras();
            location_name = b.getString(getString(R.string.INTENT_LOCATION));
            location_id = b.getString("location_id");
        }
        initialize();
        categoryFragment = new CategoryFragment();


        if (getIntent().getExtras() != null && getIntent().getExtras().getString("cart") != null) {
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);

            commanFragmentCall(new CartFragment());
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("pendingbooking") != null) {
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);

            commanFragmentCall(new PendingBookingFragment());
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        tabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewpager.getCurrentItem() != 0) {
                    viewpager.setCurrentItem(0);
                    tabCategory.setTextColor(Color.BLACK);
                    tabSubCategory.setTextColor(Color.GRAY);
                    tabPrice.setTextColor(Color.GRAY);
                }
                page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
                screenName="Category";
                CategoryFragment mFragment = (CategoryFragment) page;
                mFragment.openBottomBar(1);
               // CategoryFragment.newInstance(MainActivity.this, 1, Integer.parseInt(location_id), getString(R.string.category));
            }
        });
        tabSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewpager.getCurrentItem() != 1) {
                    viewpager.setCurrentItem(1);
                    tabSubCategory.setTextColor(Color.BLACK);
                    tabCategory.setTextColor(Color.GRAY);
                    tabPrice.setTextColor(Color.GRAY);
                }
                page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1);
                screenName="Sub Category";
                CategoryFragment mFragment = (CategoryFragment) page;
//                CategoryFragment.newInstance(MainActivity.this, 1,
//                        Integer.parseInt(location_id), getString(R.string.category));
//                CategoryFragment.newInstance(MainActivity.this, 1,
//                        Integer.parseInt(location_id), getString(R.string.sub_category));
                mFragment.openBottomBar(2);

               // CategoryFragment.newInstance(MainActivity.this, 1, Integer.parseInt(location_id), getString(R.string.sub_category));
            }
        });
        tabPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewpager.getCurrentItem() != 2) {
                    viewpager.setCurrentItem(2);
                    tabCategory.setTextColor(Color.GRAY);
                    tabPrice.setTextColor(Color.BLACK);
                    tabSubCategory.setTextColor(Color.GRAY);
                }
                page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 2);
                screenName="Price";
                CategoryFragment mFragment = (CategoryFragment) page;
//                CategoryFragment.newInstance(MainActivity.this, 1,
//                        Integer.parseInt(location_id), getString(R.string.price));
                mFragment.openBottomBar(3);

                //CategoryFragment.newInstance(MainActivity.this, 1, Integer.parseInt(location_id), getString(R.string.price));
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        addTabtoViewpager(viewpager);
        //tablayout.setupWithViewPager(viewpager);


    }

    private void addTabtoViewpager(ViewPager tabs) {
        if (location_id == null) {
            location_id = Config.getPreference(Constants.pref_Current_Store_id);
        }
        if (location_id != null) {
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFrag(CategoryFragment.newInstance(MainActivity.this, 1,
                    Integer.parseInt(location_id), getString(R.string.category)),
                    getString(R.string.category));

            pagerAdapter.addFrag(CategoryFragment.newInstance(MainActivity.this, 1,
                    Integer.parseInt(location_id), getString(R.string.sub_category)),
                    getString(R.string.sub_category));
            pagerAdapter.addFrag(CategoryFragment.newInstance(MainActivity.this, 1,
                    Integer.parseInt(location_id), getString(R.string.price)),
                    getString(R.string.price));

            tabs.addOnPageChangeListener(this);
            tabs.setAdapter(pagerAdapter);


        }
    }

    private void initialize() {
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "")).create(ApiService.class);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(location_name);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        LoginModel loginModel = new LoginModel();
        Gson gson = new Gson();
        String res = gson.toJson(Config.getPreference(Constants.pref_LoginUser));


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        cartMenu = menu.getItem(1);
        helpMenu = menu.getItem(0);
        helpMenu.setVisible(false);
        Menu menuNav = navigationView.getMenu();
        MenuItem menuItem = menuNav.findItem(R.id.nav_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        updateCartCount();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        View actionVieww = MenuItemCompat.getActionView(menu.findItem(R.id.action_cart));
        textCartItemCount = actionVieww.findViewById(R.id.cart_badge);
        updateCartCount();
        actionVieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act, CartActivity.class));
            }
        });
        return true;
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updateCartCount() {
        try {
            AddToCartRes addToCartRes = new Gson().fromJson(PreferenceUtil.getPref(act).getString(Constants.pref_Cart_Response, ""), AddToCartRes.class);
            mCartItemCount = addToCartRes.getData().getCartInfo().size();
            setupBadge();
        } catch (Exception e) {
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            // tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            Intent intent = new Intent(act, HelpActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void commanFragmentCall(Fragment fragment) {
        Fragment cFragment = fragment;
        if (cFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack("Rentsy", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, cFragment);
            fragmentTransaction.isAddToBackStackAllowed();
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            cartMenu.setVisible(true);
            helpMenu.setVisible(false);
            main_fragment.setVisibility(View.GONE);
            viewpager.setVisibility(View.VISIBLE);
            //tablayout.setVisibility(View.VISIBLE);
            tabLinear.setVisibility(View.VISIBLE);

            toolbar.setTitle(Config.getPreference(Constants.pref_Current_Store));
        } else if (id == R.id.nav_notification) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            // tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new NotificationFragment());
        } else if (id == R.id.nav_locaton) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            Intent mainIntent = new Intent(this, LocationActivity.class);
            startActivity(mainIntent);
        } else if (id == R.id.nav_bookings) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new CalenderFragment());
        } else if (id == R.id.nav_pending_bookings) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new PendingBookingFragment());
        } else if (id == R.id.nav_promotions) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new PromotionFragment());
        } else if (id == R.id.nav_favorite) {
            cartMenu.setVisible(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new FavouriteFragment());

        } else if (id == R.id.nav_cart) {
            cartMenu.setEnabled(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new CartFragment());
        } else if (id == R.id.nav_settings) {
            cartMenu.setEnabled(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new SettingFragment());
        } else if (id == R.id.nav_reating) {
            cartMenu.setEnabled(false);
            helpMenu.setVisible(true);
            main_fragment.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            //  tablayout.setVisibility(View.GONE);
            tabLinear.setVisibility(View.GONE);
            commanFragmentCall(new RatingFragment());
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Toast.makeText(act, position+"", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected Called");
        Log.d("eric", "position " + position + " ");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface TabClick {
        void onTabClick(Integer position);
    }
}
