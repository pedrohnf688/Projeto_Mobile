package com.pedrohnf688.appconfiguracao.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.pedrohnf688.appconfiguracao.R;
import com.pedrohnf688.appconfiguracao.fragments.ListarFragment;
import com.pedrohnf688.appconfiguracao.fragments.PerfilFragment;
import com.pedrohnf688.appconfiguracao.fragments.RankingFragment;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new ListarFragment()).addToBackStack(null).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){

                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new PerfilFragment()).addToBackStack(null).commit();
                    return true;

                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new ListarFragment()).addToBackStack(null).commit();
                    return true;

                case R.id.navigation_ranking:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new RankingFragment()).addToBackStack(null).commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {

        int x  = getSupportFragmentManager().getBackStackEntryCount();

        if(x == 1) {
            finish();
        }else if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

}
