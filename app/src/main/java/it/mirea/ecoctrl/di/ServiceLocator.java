package it.mirea.ecoctrl.di;

import android.app.Application;
import android.util.Log;

import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.mock.MockBase;
import it.mirea.ecoctrl.cutContent.IPtoLocation;
import it.mirea.ecoctrl.repositories.network.adressLogic.GeoApiService;
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
    private GeoApiService mApiService;
    public GeoApiService getApiService() {
        Log.e("IP","GET API SERVICE");
        if (mApiService== null) {
            mApiService = new GeoApiService();
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
    /*private Gson mGson;
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
    }*/


}
