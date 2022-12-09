package it.mirea.ecoctrl.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.mirea.ecoctrl.databinding.WebFragmentBinding;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.views.activities.MainActivity;

public class WebFragment extends Fragment {
    private WebFragmentBinding webBinding;
    private String url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        webBinding = WebFragmentBinding.inflate(inflater, container, false);
        Log.e("WEB","onCreateView");
        if (url != null && !url.isEmpty()) {
            CookieManager.getInstance().removeAllCookies(null);
            webBinding.Web.clearCache(true);
            webBinding.Web.loadUrl(url);
            webBinding.Web.setWebViewClient(ServiceLocator.getInstance().getVK_API().auth.oath2VK((MainActivity) getActivity()));
        }

        return webBinding.getRoot();
    }
}
