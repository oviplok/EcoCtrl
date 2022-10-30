package it.mirea.ecoctrl.views.adapters;

import android.content.Context;
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
import it.mirea.ecoctrl.views.activities.MapActivity;
import it.mirea.ecoctrl.views.activities.PlistActivity;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.AllViewHolder> {
    private Context context;
    private List<PlaceF> data;
    private PlistActivity mActivity;
    private MapActivity mapActivity;
   // private List<Party> data;
   // private MainActivity mActivity;
   //List<Place> data,Context context)
    public PlaceListAdapter(List<PlaceF> data, PlistActivity activity,MapActivity mapActivity){
        //plistActivity=activity;
        mActivity = activity;
        this.data = data;
        this.mapActivity=mapActivity;
       // this.context=context;
       // this.data=data;
    }

  //  @NotNull
    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceListElementBinding binding = PlaceListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllViewHolder(binding);
        //View view = LayoutInflater.from(context).inflate(R.layout.place_list_element, parent, false);
        //return new AllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {
       /* holder.binding.partyCard.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            String json = ServiceLocator.getInstance().getGson().toJson(data.get(position));
            bundle.putString("Party", json);

            Navigation.findNavController(mActivity.mBinding.navHostFragment)
                    .navigate(R.id.action_partyList_to_partyFragment, bundle);
        });*/

        holder.binding.placeName.setText(data.get(position).getPlace_name());
       // if (data.get(position).getCreator() != null) {
        //    holder.binding.partyCreator.setText(data.get(position).getCreator().getLastName() + " " + data.get(position).getCreator().getName());
        //}
      //  if (data.get(position).getPeopleList() != null) {
        //    holder.binding.partyPeopleCount.setText(data.get(position).getPeopleList().size() + " " +
          //          (data.get(position).getMaxPeopleCount() > 0 ? "/ " + data.get(position).getMaxPeopleCount() + " " : "")
            //        + "человек");
       // }
        holder.binding.placeMetan.setText("Methane: "+data.get(position).getMetanInfo());
        holder.binding.placeAzd.setText("Sulphur dioxide: "+data.get(position).getAzdInfo());
        holder.binding.placeSerd.setText("Nitrogen dioxide: "+data.get(position).getSerdInfo());
       // String latlng = data.get(position).getLat()+" "+data.get(position).getLng();
        holder.binding.LatLng.setText("Lat: "+data.get(position).getLat()+"Lng: "+data.get(position).getLng());

       /* if (data.get(position).getStartTime() != null && data.get(position).getStopTime() != null) {
            holder.binding.partyTime.setText(
                    data.get(position).getStartTime().toLocalDate().equals(data.get(position).getStopTime().toLocalDate())
                            ?
                            data.get(position).getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " +
                                    data.get(position).getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                                    data.get(position).getStopTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                            :
                            data.get(position).getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " +
                                    data.get(position).getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                                    data.get(position).getStopTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " +
                                    data.get(position).getStopTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        }*/

        if (data.get(position).getImagesF() != null && !data.get(position).getImagesF().isEmpty()) {
            holder.binding.imageSlider.setVisibility(View.VISIBLE);
            holder.binding.imageSlider.setAdapter(new ImageSliderAdapter(data.get(position).getImagesF(), false, mActivity,mapActivity));
        } else {
            holder.binding.imageSlider.setVisibility(View.GONE);
        }
       /* holder.place_name.setText(data.get(position).getPlace_name());
        holder.place_met.setText("Methane: "+data.get(position).getMetanInfo());
        holder.place_serd.setText("Sulphur dioxide: "+data.get(position).getSerdInfo());
        holder.place_azd.setText("Nitrogen dioxide: "+data.get(position).getAzdInfo());
        holder.lat_lng.setText("Lat: "+data.get(position).getLat()+" Lng: "+data.get(position).getLng());
        if (data.get(position).getImages() != null && !data.get(position).getImages().isEmpty()) {
            holder.binding.imageSlider.setVisibility(View.VISIBLE);
            holder.binding.imageSlider.setAdapter(new ImageSliderAdapter(data.get(position).getImages(), false, mActivity));
        } else {
            holder.binding.imageSlider.setVisibility(View.GONE);
        }*/
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

    //public void deleteData(){}

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
