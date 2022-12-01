package it.mirea.ecoctrl.repositories.network.authLogic.oAuth;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.navigation.Navigation;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.views.activities.MainActivity;

public class VKAuth {
    public void auth(MainActivity activity) {
        Bundle bundle = new Bundle();
        //String url = "https://oauth.vk.com/authorize?client_id=7975999&scope=email&redirect_uri=https://oauth.vk.com/blank.html&display=mobile&response_type=token&scope=offline, email"
        bundle.putString("url", "https://oauth.vk.com/authorize?client_id=7975999&scope=email&redirect_uri=https://oauth.vk.com/blank.html&display=mobile&response_type=token&scope=offline, email");
        Navigation.findNavController(activity.mBinding.navHostFragment).navigate(R.id.action_authFragment_to_webFragment, bundle);
    }

    public WebViewClient oath2VK(MainActivity activity) {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains("https://oauth.vk.com/blank.html#")) {
                    String token = Uri.parse(request.getUrl().toString().replace("#", "?")).getQueryParameter("access_token");
                    String email = Uri.parse(request.getUrl().toString().replace("#", "?")).getQueryParameter("email");

                    ServiceLocator.getInstance().getRepository().findUser(email, activity).observe(activity, (user) -> {
                        if (user == null) {
                            User newUser = new User();
                            newUser.setEmail(email);

                            ServiceLocator.getInstance().setUser(newUser);
                        } else {
                            ServiceLocator.getInstance().setUser(user);
                        }
                        //засунуть intent и сервис_локатор
                        //не надо
                    });
                    ServiceLocator.getInstance().getVK_API().getUserInfo(token, activity);

                   // activity.getPreferences(Context.MODE_PRIVATE).edit().putString("token", token).putString("email", email).apply();

                    Navigation.findNavController(activity.mBinding.navHostFragment).navigate(R.id.action_webFragment_to_authFragment);

                    return false;
                }
                view.loadUrl(request.getUrl().toString());

                return true;
            }
        };
    }
}
