package it.mirea.ecoctrl.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.mock.MockBase;
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


}
