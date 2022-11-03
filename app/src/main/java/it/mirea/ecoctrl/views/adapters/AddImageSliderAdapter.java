package it.mirea.ecoctrl.views.adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.util.List;

import it.mirea.ecoctrl.databinding.ImageElementBinding;
//import it.mirea.ecoctrl.views.activities.AddActivity;
import it.mirea.ecoctrl.views.activities.MapActivity;

public class AddImageSliderAdapter extends RecyclerView.Adapter<AddImageSliderAdapter.ImageSliderViewHolder> {

    List<String> images;
    MapActivity mActivity;

    public AddImageSliderAdapter(List<String> images, boolean adding, MapActivity activity) {
        this.images = images;
        mActivity = activity;

        if (adding) {
            this.images.add(null);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(ImageElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageSliderViewHolder holder, int position) {
        if (images.get(position) == null) {
            holder.mBinding.imageContent.setVisibility(View.GONE);
            holder.mBinding.addButton.setVisibility(View.VISIBLE);
            holder.mBinding.addButton.setOnClickListener((View v) -> {
                if (mActivity != null) {
                    mActivity.getActivityResultRegistry().register("key", new ActivityResultContracts.OpenDocument(), result -> {
                        mActivity.getApplicationContext().getContentResolver().takePersistableUriPermission(
                                result,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        images.add(images.size() - 1, result.toString());
                        notifyDataSetChanged();
                    }).launch(new String[]{"image/*"});
                }
            });
        } else {
            holder.mBinding.addButton.setVisibility(View.GONE);
            holder.mBinding.imageContent.setVisibility(View.VISIBLE);

            if (mActivity != null) {
                try {
                    holder.mBinding.imageContent.setImageBitmap(
                            BitmapFactory.decodeFileDescriptor(
                                    mActivity.getApplicationContext().getContentResolver().openFileDescriptor(
                                            Uri.parse(images.get(position)), "r").getFileDescriptor()
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        ImageElementBinding mBinding;

        public ImageSliderViewHolder(ImageElementBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}
/*
public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    List<String> images;
    PlistActivity plistActivity;
    AddActivity addActivity;


    public ImageSliderAdapter(List<String> images, boolean adding, PlistActivity plistActivity,AddActivity addActivity) {
        this.images = images;
        this.plistActivity = plistActivity;
        this.addActivity = addActivity;
        Log.e("ImageSliderAdapter","Exc plist");
        if (adding) {
            this.images.add(null);
        }
    }


    @NonNull
    @Override
    public ImageSliderAdapter.ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(ImageElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.ImageSliderViewHolder holder, int position) {
        if(addActivity !=null){
            Log.e("addActivity","true");
        }
        else{
            Log.e("addActivity","false");
        }
        if (images.get(position) == null) {
            holder.mBinding.imageContent.setVisibility(View.GONE);
            holder.mBinding.addButton.setVisibility(View.VISIBLE);
            holder.mBinding.addButton.setOnClickListener((View v) -> {
                if (plistActivity != null) {
                    plistActivity.getActivityResultRegistry().register("key", new ActivityResultContracts.OpenDocument(), result -> {
                        plistActivity.getApplicationContext().getContentResolver().takePersistableUriPermission(
                                result,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        images.add(images.size() - 1, result.toString());
                        notifyDataSetChanged();
                    }).launch(new String[]{"image/*"});
                }
                if(addActivity !=null){
                    addActivity.getActivityResultRegistry().register("key", new ActivityResultContracts.OpenDocument(), result -> {
                        addActivity.getApplicationContext().getContentResolver().takePersistableUriPermission(
                                result,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        images.add(images.size() - 1, result.toString());
                        notifyDataSetChanged();
                    }).launch(new String[]{"image/*"});
                }
            });
        } else {
            holder.mBinding.addButton.setVisibility(View.GONE);
            holder.mBinding.imageContent.setVisibility(View.VISIBLE);

            if(addActivity !=null){
                Log.e("addActivity","true");
            }
            else{
                Log.e("addActivity","false");
            }

            if (plistActivity != null) {
                try {
                    holder.mBinding.imageContent.setImageBitmap(
                            BitmapFactory.decodeFileDescriptor(
                                    plistActivity.getApplicationContext().getContentResolver().openFileDescriptor(
                                            Uri.parse(images.get(position)), "r").getFileDescriptor()
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exc","Exc plist");
                }
            }
            if (addActivity != null) {
                try {
                    holder.mBinding.imageContent.setImageBitmap(
                            BitmapFactory.decodeFileDescriptor(
                                    addActivity.getApplicationContext().getContentResolver().openFileDescriptor(
                                            Uri.parse(images.get(position)), "r").getFileDescriptor()
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exc","Exc add");
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        ImageElementBinding mBinding;

        public ImageSliderViewHolder(ImageElementBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}*/
