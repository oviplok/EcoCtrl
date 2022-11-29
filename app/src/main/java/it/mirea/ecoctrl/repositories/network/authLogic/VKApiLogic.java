package it.mirea.ecoctrl.repositories.network.authLogic;

import androidx.navigation.Navigation;

import java.util.Map;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.network.authLogic.oAuth.VKAuth;
import it.mirea.ecoctrl.views.activities.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VKApiLogic {
    Map<String, String> api_info = Map.of("v", "5.131");

    public VKAuth auth;

    public VKApiLogic() {
        auth = new VKAuth();
    }

    public void getUserInfo(String token, MainActivity activity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VKApiService api = retrofit.create(VKApiService.class);

        api.getPersonInfo(api_info, token).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    if (ServiceLocator.getInstance().getUser() != null) {
                        ServiceLocator.getInstance().getUser().setFirst_name(response.body().response.first_name);
                        ServiceLocator.getInstance().getUser().setLast_name(response.body().response.last_name);
                        ServiceLocator.getInstance().getUser().getConnections().put("vk", "https://vk.com/" + response.body().response.screen_name);
                        ServiceLocator.getInstance().getUser().setRole(User.Role.User);

                        ServiceLocator.getInstance().getRepository().findUser(ServiceLocator.getInstance().getUser().getEmail(), activity).observe(activity, (user) -> {
                            if (user == null) {
                                ServiceLocator.getInstance().getRepository().addUser(ServiceLocator.getInstance().getUser());
                            } else {
                                if (!user.getFirst_name().equals(ServiceLocator.getInstance().getUser().getFirst_name()) ||
                                        !user.getLast_name().equals(ServiceLocator.getInstance().getUser().getFirst_name()) ||
                                        !user.getConnections().equals(ServiceLocator.getInstance().getUser().getConnections()) ||
                                        !user.getPhone().equals(ServiceLocator.getInstance().getUser().getPhone()) ||
                                        user.getRole() != ServiceLocator.getInstance().getUser().getRole()) {
                                    user.setFirst_name(ServiceLocator.getInstance().getUser().getFirst_name());
                                    user.setLast_name(ServiceLocator.getInstance().getUser().getLast_name());
                                    user.setPhone(ServiceLocator.getInstance().getUser().getPhone());
                                    user.setRole(User.Role.User);
                                    user.getConnections().put("vk", ServiceLocator.getInstance().getUser().getConnections().get("vk"));

                                    ServiceLocator.getInstance().getRepository().updateUser(user);
                                }
                            }

                           // Navigation.findNavController(activity.mBinding.navHostFragment).navigate(R.id.action_authFragment_to_partyList);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    public class APIResponse {
        public class APIPerson {
            public String first_name;
            public String last_name;
            public int id;
            public String screen_name;
        }

        public APIPerson response;
    }
}
