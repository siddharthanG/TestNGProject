package com.rocket.automation.Utils;

/**
 * Data loader factory to load POJOS
 */
public class DataLoader {
    private RepoData repoData;

    public static DataLoader dataLoader;

    private DataLoader() {

    }

    public void createRepoData() {
        repoData = new RepoData();
    }

    public RepoData getRepoData() {
        return repoData;
    }

    /**
     * Method to return the singleton instance of the loader class
     * Always only one instance of Dataloader class is created
     * @return DataLoader object
     */
    public static DataLoader getDataLoaderInstance() {
        if (dataLoader == null) {
            dataLoader = new DataLoader();
        }
        return dataLoader;
    }

}
