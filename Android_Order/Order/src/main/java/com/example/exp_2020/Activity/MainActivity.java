package com.example.exp_2020.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exp_2020.Activity.MFood.MFoodActivity;
import com.example.exp_2020.Activity.MyOrder.MyOrderActivity;
import com.example.exp_2020.Activity.OrderManage.OrderManageActivity;
import com.example.exp_2020.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView img;
    private TextView currentId;
    private Button myorder;
    private Button orderM;
    private Button foodadd;
    private Button foodM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        img=findViewById(R.id.image_v);
        myorder=findViewById(R.id.my);
        orderM=findViewById(R.id.order_manager);
        foodadd=findViewById(R.id.food_add);
        foodM=findViewById(R.id.menu_manage);
        String User=LoginActivity.currentUser.getUserName();

        if(User.equals("管理员")){
        }else
        {
            Log.w("当前用户",""+LoginActivity.currentUser.getUserName());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {  //浮标
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,CartActivity.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.my, R.id.food_add, R.id.menu_manage,R.id.order_manager)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my:
                        Intent intent1=new Intent(MainActivity.this, MyOrderActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.food_add:
                        if(User.equals("管理员")){
                            Intent intent2=new Intent(MainActivity.this,FoodAddActivity.class);
                            startActivity(intent2);
                        }else
                        {
                            Log.w("当前用户",""+LoginActivity.currentUser.getUserName());
                            Toast.makeText(MainActivity.this,"无权限",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.menu_manage:
                        if(User.equals("管理员")){
                            Intent intent3=new Intent(MainActivity.this, MFoodActivity.class);
                            startActivity(intent3);
                        }else {
                            Log.w("当前用户", "" + LoginActivity.currentUser.getUserName());
                            Toast.makeText(MainActivity.this, "无权限", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.order_manager:
                        if(User.equals("管理员")){
                            Intent intent4=new Intent(MainActivity.this, OrderManageActivity.class);
                            startActivity(intent4);
                        }else {
                            Log.w("当前用户", "" + LoginActivity.currentUser.getUserName());
                            Toast.makeText(MainActivity.this, "无权限", Toast.LENGTH_LONG).show();
                        }

                        break;

                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
