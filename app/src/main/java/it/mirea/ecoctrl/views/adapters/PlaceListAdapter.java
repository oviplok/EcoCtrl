package it.mirea.ecoctrl.views.adapters;

import android.content.Context;
import android.util.Log;
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
import it.mirea.ecoctrl.databinding.PlaceListElementBinding;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
//import it.mirea.ecoctrl.views.activities.AddActivity;
import it.mirea.ecoctrl.views.activities.MapActivity;
import it.mirea.ecoctrl.views.activities.PlistActivity;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.AllViewHolder> {
    private Context context;
    private List<PlaceF> data;
    private PlistActivity mActivity;
   // private AddActivity addActivity;
   // private List<Party> data;
   // private MainActivity mActivity;
   //List<Place> data,Context context)
    public PlaceListAdapter(List<PlaceF> data, PlistActivity activity){
        mActivity = activity;
        this.data = data;
    }

  //  @NotNull
    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceListElementBinding binding = PlaceListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {

        holder.binding.placeName.setText(data.get(position).getPlace_name());
        holder.binding.placeMetan.setText("Methane: "+data.get(position).getMetanInfo());
        holder.binding.placeAzd.setText("Sulphur dioxide: "+data.get(position).getAzdInfo());
        holder.binding.placeSerd.setText("Nitrogen dioxide: "+data.get(position).getSerdInfo());
        holder.binding.LatLng.setText("Lat: "+data.get(position).getLat()+" Lng: "+data.get(position).getLng());

        if (data.get(position).getImagesF() != null && !data.get(position).getImagesF().isEmpty()) {
            Log.e("Image","Not Null");
            holder.binding.imageSlider.setVisibility(View.VISIBLE);
            holder.binding.imageSlider.setAdapter(new PlistImageSliderAdapter(data.get(position).getImagesF(), false, mActivity));
        } else {
            Log.e("Image","Null");
            holder.binding.imageSlider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setData(List<PlaceF> placeList) {
        data = placeList;
        notifyDataSetChanged();
    }

    public List<PlaceF> getData(){ return data; }

    public class AllViewHolder extends RecyclerView.ViewHolder {
        PlaceListElementBinding binding;
        TextView place_name, place_met, place_serd, place_azd, lat_lng;
        ImageView editImage;
        MapRoomDatabase DB;
        public AllViewHolder(PlaceListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            DB = MapRoomDatabase.getInstance(context);
            place_name = itemView.findViewById(R.id.place_name);
            place_met = itemView.findViewById(R.id.place_metan);
            place_serd = itemView.findViewById(R.id.place_serd);
            place_azd = itemView.findViewById(R.id.place_azd);
            lat_lng = itemView.findViewById(R.id.Lat_Lng);
        }
    }
}
