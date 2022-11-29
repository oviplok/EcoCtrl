package it.mirea.ecoctrl.di;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.mock.MockBase;
import it.mirea.ecoctrl.cutContent.IPtoLocation;
import it.mirea.ecoctrl.repositories.network.adressLogic.LocationApiLogic;
import it.mirea.ecoctrl.repositories.network.authLogic.VKApiLogic;
import it.mirea.ecoctrl.repositories.room.MapRoomRepository;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ServiceLocator() {};

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
    private RepoTasks repoTasks;

    public void initBase(Application app) {
        if (repoTasks == null) {
            repoTasks = new MapRoomRepository(app);
        }
    }

    public RepoTasks getRepository() {
        if (repoTasks == null) {
            repoTasks = new MockBase();
        }
        return repoTasks;
    }
    private LocationApiLogic mApiService;
    public LocationApiLogic getApiService() {
        Log.e("IP","GET API SERVICE");
        if (mApiService== null) {
            mApiService = new LocationApiLogic();
        }
        return mApiService;
    }
    private IPtoLocation mAnalysis;
    public IPtoLocation getAnalysis() {
        if (mAnalysis == null) {
            mAnalysis = new IPtoLocation();
        }
        return mAnalysis;
    }
////////////////////////////////////////////////////////////////////////////////////////
    private Gson mGson;
    private User mUser;
    private VKApiLogic mVK_API;

    public Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(
                                    json.getAsString(),
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                            )
                    )
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(src))
                    )
                    .create();
        }
        return mGson;
    }

    public VKApiLogic getVK_API() {
        if (mVK_API == null) {
            mVK_API = new VKApiLogic();
        }
        return mVK_API;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
/////////////////////////////////////////////////////////////////////////////
}
