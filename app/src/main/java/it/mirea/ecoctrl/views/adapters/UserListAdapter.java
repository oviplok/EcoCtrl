package it.mirea.ecoctrl.views.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.PlaceListElementBinding;
import it.mirea.ecoctrl.databinding.UserListElementBinding;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.models.UserDTO;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.views.activities.PlistActivity;
import it.mirea.ecoctrl.views.activities.UsrActivity;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.AllViewHolder>{

    private UsrActivity mActivity;
    private List<User> data;

    public UserListAdapter(List<User> data, UsrActivity activity){
        Log.d("UserListAdapter","usrlistadptr");
        mActivity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public UserListAdapter.AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserListElementBinding binding = UserListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserListAdapter.AllViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.AllViewHolder holder, int position) {

        Log.e("onBindView",data.get(position).getEmail());
        holder.binding.userId.setText(data.get(position).getId());
        holder.binding.userEmail.setText(data.get(position).getEmail());
        holder.binding.userPassword.setText(data.get(position).getPassword());
        holder.binding.userRole.setText(data.get(position).getRole().toString());
      //  holder.binding.placeName.setText(data.get(position).getPlace_name());
      //  holder.binding.placeMetan.setText("Methane: "+data.get(position).getMetanInfo());
      //  holder.binding.placeAzd.setText("Sulphur dioxide: "+data.get(position).getAzdInfo());
      //  holder.binding.placeSerd.setText("Nitrogen dioxide: "+data.get(position).getSerdInfo());
      //  holder.binding.LatLng.setText("Lat: "+data.get(position).getLat()+" Lng: "+data.get(position).getLng());

      /*  if (data.get(position).getImagesF() != null && !data.get(position).getImagesF().isEmpty()) {
            Log.e("Image","Not Null");
            holder.binding.imageSlider.setVisibility(View.VISIBLE);
            holder.binding.imageSlider.setAdapter(new PlistImageSliderAdapter(data.get(position).getImagesF(), false, mActivity));
        } else {
            Log.e("Image","Null");
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

    public void setData(List<User> usrList) {
        Log.e("SetData",usrList.get(0).getEmail());
        Log.e("SetData",usrList.get(1).getEmail());
        Log.e("SetData",usrList.get(2).getEmail());
        Log.e("SetData",usrList.get(3).getEmail());
        data = usrList;
        notifyDataSetChanged();
    }

    public List<User> getData(){ return data; }

    public class AllViewHolder extends RecyclerView.ViewHolder {
        UserListElementBinding binding;
        TextView email, password, role, id; //, place_azd, lat_lng;
        public AllViewHolder(UserListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            id = itemView.findViewById(R.id.user_id);
            role = itemView.findViewById(R.id.user_role);
            email = itemView.findViewById(R.id.user_email);
            password = itemView.findViewById(R.id.user_password);
                                                                                                                                                                                                                                                                                                                //            lat_lng = itemView.findViewById(R.id.Lat_Lng);
        }
    }
}
