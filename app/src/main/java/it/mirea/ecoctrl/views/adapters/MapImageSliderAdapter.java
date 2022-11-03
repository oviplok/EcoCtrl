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

public class MapImageSliderAdapter extends RecyclerView.Adapter<MapImageSliderAdapter.ImageSliderViewHolder> {

    List<String> images;
    MapActivity mActivity;

    public MapImageSliderAdapter(List<String> images, boolean adding, MapActivity activity) {
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
