package development.parkenulm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ParkhausListAdapter extends BaseAdapter {
    private ArrayList<Parkhaus> ph_data;
    final AppCompatActivity activity;

    public ParkhausListAdapter(ArrayList<Parkhaus> ph_data, AppCompatActivity activity) {
        this.ph_data = ph_data;
        this.activity = activity;
    }

    public void updateData(ArrayList<Parkhaus> newData) {
        ph_data = newData;
        notifyDataSetChanged();
    }

    /**
     * This method returns the number of items in the list.
     *
     * @return The number of items in the list.
     */
    @Override
    public int getCount() {
        return ph_data.size();
    }

    /**
     * This method returns the Name at the given position.
     *
     * @param i The position of the item.
     * @return The Name at the given position.
     */
    @Override
    public Object getItem(int i) {
        return ph_data.get(i).getHaus();
    }

    /**
     * This method returns the item id at the given position.
     *
     * @param i The position of the item.
     * @return i.
     */
    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        Parkhaus db = ph_data.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ph_list_view_item, null, false);
        }
        TextView parkhaus_Name = view.findViewById(R.id.ParkhausListItemName);
        parkhaus_Name.setText(db.getHaus());
        //TextView parkhaus_Plaetze = view.findViewById(R.id.ParkhausListItemPlaetze);
        //parkhaus_Plaetze.setText(db.getPlatz());
        ImageButton button = view.findViewById(R.id.map_button);
        View finalView = view;
        button.setOnClickListener(v -> {
            String search = activity.getResources().getString(R.string.parking_garage) + " " + db.getHaus() + " " + activity.getResources().getString(R.string.ulm);
            String url = "geo:0,0?q=" + search;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(finalView.getContext().getPackageManager()) != null) {
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_maps_app), Toast.LENGTH_SHORT).show();
            }
        });
        finalView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("ParkhausName", db.getHaus());
            activity.startActivity(intent);
        });
        TextView parkhaus_Frei = view.findViewById(R.id.ParkhausListItemFreiePlaetze);
        parkhaus_Frei.setText(db.getFrei());
        //Colorize(parkhaus_Frei, parkhaus_Plaetze, parkhaus_Name, db.getFrei(), db.getPlatz());
        Colorize(parkhaus_Frei, parkhaus_Name, db.getFrei(), db.getPlatz());
        return view;
    }

    public void Colorize(TextView parkhaus_Frei, TextView parkhaus_Name, String frei, String platz) {
        Thread thread = new Thread(() -> {
            if (frei.matches("[0-9]*")) {
                int freiInt = Integer.parseInt(frei);
                int platzInt = Integer.parseInt(platz);
                boolean quater = freiInt < platzInt / 4;
                parkhaus_Frei.getTag(R.color.black);
                if (freiInt < 30) {
                    activity.runOnUiThread(() -> parkhaus_Frei.setTextColor(Color.RED));
                } else if (freiInt > 30 && quater) {
                    activity.runOnUiThread(() -> parkhaus_Frei.setTextColor(Color.rgb(255, 136, 0)));
                }
                else {
                    activity.runOnUiThread(() -> parkhaus_Frei.setTextColor(Color.GREEN));
                }
            } else {
                activity.runOnUiThread(() -> {
                    int nightModeFlags =
                            activity.getResources().getConfiguration().uiMode &
                                    Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags) {
                        case Configuration.UI_MODE_NIGHT_YES:
                            parkhaus_Frei.setTextColor(Color.DKGRAY);
                            //parkhaus_Plaetze.setTextColor(Color.DKGRAY);
                            parkhaus_Name.setTextColor(Color.DKGRAY);
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                            parkhaus_Frei.setTextColor(Color.LTGRAY);
                            //parkhaus_Plaetze.setTextColor(Color.LTGRAY);
                            parkhaus_Name.setTextColor(Color.LTGRAY);
                            break;
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            break;
                    }

                });
            }
        });
        thread.start();
    }
}