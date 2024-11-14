package com.example.my;  // Use your app's package name

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private TextView homeContent;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Ensure your layout file matches

        // Initialize Toolbar
        Toolbar homeToolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolbar);

        // Initialize TextView for displaying home content
        homeContent = findViewById(R.id.homeContent);
        homeContent.setText("Welcome to the Home Page");

        // Initialize SearchView (it will be managed in onOptionsItemSelected)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with the SearchView
        getMenuInflater().inflate(R.menu.menu_home, menu);  // Ensure this menu file exists

        // Get the SearchView from the menu and set it up
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission (for now, just display the query)
                homeContent.setText("Searching for: " + query);  // You can modify this to handle actual search results
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes while typing in the search bar
                // For example, you could filter content dynamically here
                homeContent.setText("Searching: " + newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selections (like back button)
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();  // Close the activity if using back navigation
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
