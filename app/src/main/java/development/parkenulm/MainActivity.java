package development.parkenulm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    ShareActionProvider shareActionProvider;
    ParkhausListAdapter adapter;
    //0 = don't sort, 1 = sort by name, 2 = sort by free places
    static int sortBy;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.
     *                           Note: Otherwise it is null.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        if (Paper.book().contains("ParkhausDB")) {
            adapter = new ParkhausListAdapter(Paper.book().read("ParkhausDB"), this);
        } else adapter = new ParkhausListAdapter(ParkhausDB.getParkhausDB(), this);
        if (Paper.book().contains("sortBy")) {
            sortBy = Paper.book().read("sortBy");
        } else if (sortBy > 2 || sortBy == 0) {
            sortBy = 0;
        }
        ListView listView = findViewById(R.id.ParkhausList);
        listView.setAdapter(adapter);
        listView.setClickable(true);
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

    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void share(Bitmap bitmap){
        String pathofBmp=
                MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        bitmap,"title", null);
        Uri uri = Uri.parse(pathofBmp);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)+": "+getString(R.string.share)));
    }

    /**
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    //Future: RestrictedApi solve
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        //MenuItem shareItem = menu.findItem(R.id.action_share);
        //shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return true;
    }

    /**
     * This method is called when the user clicks on a menu item.
     *
     * @param item The menu item that was clicked
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_share)
        {
            share(screenShot(findViewById(R.id.main_activity)));
        }
        if (item.getItemId() == R.id.refresh_menu) {
            getData();
            return true;
        }
        if (item.getItemId() == R.id.sort_sub1) {
            Log.d("Sort", "Sort by name");
            sortBy = 1;
            Paper.book().write("sortBy", sortBy);
            getData();
            return true;
        }
        if (item.getItemId() == R.id.sort_sub2) {
            Log.d("Sort", "Sort by free places");
            sortBy = 2;
            Paper.book().write("sortBy", sortBy);
            getData();
            return true;
        }
        if (item.getItemId() == R.id.sort_sub2) {
            Log.d("Sort", "Reset sort");
            sortBy = 0;
            Paper.book().write("sortBy", sortBy);
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
                    sort(parkhausList);
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

    private static void sort(ArrayList<Parkhaus> list) {
        //0 = don't sort, 1 = sort by name, 2 = sort by free places
        switch (sortBy) {
            case 0:
                break;
            case 1:
                list.sort(Comparator.comparing((Parkhaus::getFirstChar)));
                break;
            case 2:
                list.sort(Comparator.comparing((Parkhaus::getFreiAsInt)));
                Collections.reverse(list);
                Log.d("Sort", "Sort by free places");
                break;
        }
    }

    public String checkString(String s) {
        if (s.contains("/")) return s.substring(s.indexOf("/") + 2);
        else return s;
    }
}