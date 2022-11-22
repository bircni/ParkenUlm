package development.parkenulm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import io.paperdb.Paper;

public class DetailsActivity extends AppCompatActivity {

    String HTML;
    String parkhausName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        HTML = Paper.book().read("HTML");
        readHTML(HTML);
        parkhausName = getIntent().getStringExtra("ParkhausName");
        Toolbar toolbar_list = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(parkhausName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void readHTML(String content) {
        Log.d("LOOOOOG", "readHTML: " + content);
        ArrayList<ParkhausHTML> parkhausHTMLArrayList = ParkhausDB.getHTML_Strings();
        String HTML_String = "";
        for (ParkhausHTML parkhausHTML : parkhausHTMLArrayList) {
            if (parkhausHTML.getHaus().equals(parkhausName)) {
                HTML_String = parkhausHTML.getHTML_String();
            }
        }
    }

    public String checkString(String s) {
        if (s.contains("/")) return s.substring(s.indexOf("/") + 2);
        else return s;
    }

    /**
     * This method is called when the user presses the back button.
     *
     * @return True if the back button was pressed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}