package development.parkenulm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    ParkhausListAdapter adapter;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        if (Paper.book().contains("ParkhausDB")) {
            adapter = new ParkhausListAdapter(Paper.book().read("ParkhausDB"), this);
        } else adapter = new ParkhausListAdapter(ParkhausDB.getParkhausDB(), this);
        ListView listView = findViewById(R.id.ParkhausList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("ParkhausName", adapter.getItem(position).toString());
            startActivity(intent);
        });
        getData();
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(getString(R.string.toolbar_title));
        setSupportActionBar(toolbar);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            getData();
        });
    }


    /**
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    //TODO: RestrictedApi solve
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    /**
     * This method is called when the user clicks on a menu item.
     *
     * @param item The menu item that was clicked
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            getData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean hasInternetConnection(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public void getData() {
        Thread thread = new Thread(() -> {
            try {
                String content = null;
                URLConnection connection;
                try {
                    connection = new URL("https://www.parken-in-ulm.de").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());
                    scanner.useDelimiter("\\Z");
                    content = scanner.next();
                    scanner.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (content != null) {
                    Document document = Jsoup.parse(content);
                    String[] parkhaus = ParkhausDB.getParkhausTB();
                    ArrayList<Parkhaus> parkhausList = new ArrayList<>();
                    for (String parkH : parkhaus) {
                        Element a = document.getElementById(parkH);
                        assert a != null;
                        String haus = checkString(Objects.requireNonNull(a.select("a").first()).text());
                        String platz = Objects.requireNonNull(a.select("td").next().first()).text();
                        String frei = Objects.requireNonNull(a.select("td").next().next().first()).text();
                        String open = Objects.requireNonNull(a.select("td").next().next().next().first()).text();
                        parkhausList.add(new Parkhaus(haus, platz, frei, open));
                    }
                    Paper.book().write("ParkhausDB", parkhausList);
                    runOnUiThread(() -> {
                        adapter.updateData(parkhausList);
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        if (hasInternetConnection(this)) {
            thread.start();
        } else {
            Toast.makeText(this, "no internet", Toast.LENGTH_SHORT).show();
        }
    }

    public String checkString(String s) {
        if (s.contains("/")) return s.substring(s.indexOf("/") + 2);
        else return s;
    }
}