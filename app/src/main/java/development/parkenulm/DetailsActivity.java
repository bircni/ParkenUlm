package development.parkenulm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

import io.paperdb.Paper;

public class DetailsActivity extends AppCompatActivity {

    String parkhausName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parkhausName = getIntent().getStringExtra("ParkhausName");
        TextView openTimes = findViewById(R.id.openTV);
        openTimes.setText(getOpenTimes());
        Toolbar toolbar_list = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(parkhausName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ImageView map = findViewById(R.id.map);
        map.setImageResource(getPath());
    }

    @SuppressLint("DiscouragedApi")
    private int getPath() {
        String drawableName = parkhausName.toLowerCase().replace(" ", "_").replace("ß", "ss");
        return getResources().getIdentifier(drawableName, "drawable", getPackageName());
    }

    private String getOpenTimes() {
        ArrayList<Parkhaus> parkhausDB = Paper.book().read("ParkhausDB");
        assert parkhausDB != null;
        for (Parkhaus parkhaus : parkhausDB) {
            if (parkhaus.getHaus().equals(parkhausName)) {
                return openTimesTranslator(parkhaus.getOpenTimes());
            }
        }
        return getString(R.string.no_open_times);
    }

    private String openTimesTranslator(String input) {
        if (!Locale.getDefault().getLanguage().equals("de") && input.contains("täglich durchgehend")) {
            return getString(R.string.open_24h);
        } else return input.replace(",", "\n");
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