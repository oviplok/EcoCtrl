package it.mirea.ecoctrl.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import org.jetbrains.annotations.NotNull;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import it.mirea.ecoctrl.R;
//import it.mirea.ecoctrl.databinding.PlacceListElementBinding;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.views.activities.PlistActivity;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.AllViewHolder> {
    private Context context;
    private List<Place> data;
    private PlistActivity plistActivity;

    public PlaceListAdapter(List<Place> data,Context context){
        //plistActivity=activity;
        this.context=context;
        this.data=data;
    }

   // @NotNull
    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_list_element, parent, false);
        return new AllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {

        holder.place_name.setText(data.get(position).getPlace_name());
        holder.place_met.setText("Methane: "+data.get(position).getMetanInfo());
        holder.place_serd.setText("Sulphur dioxide: "+data.get(position).getSerdInfo());
        holder.place_azd.setText("Nitrogen dioxide: "+data.get(position).getAzdInfo());
        holder.lat_lng.setText("Lat: "+data.get(position).getLat()+" Lng: "+data.get(position).getLng());
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setData(List<Place> placeList) {
        data = placeList;
        notifyDataSetChanged();
    }

    public List<Place> getData(){ return data; }

    public class AllViewHolder extends RecyclerView.ViewHolder {

        TextView place_name, place_met, place_serd, place_azd, lat_lng;
        //ImageView editImage;
        MapRoomDatabase DB;
        public AllViewHolder(@NonNull final View itemView) {
            super(itemView);

            DB = MapRoomDatabase.getInstance(context);
            place_name = itemView.findViewById(R.id.place_name);
            place_met = itemView.findViewById(R.id.place_metan);
            place_serd = itemView.findViewById(R.id.place_serd);
            place_azd = itemView.findViewById(R.id.place_azd);
            lat_lng = itemView.findViewById(R.id.Lat_Lng);
        }
    }
}
