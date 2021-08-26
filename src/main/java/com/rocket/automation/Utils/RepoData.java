package com.rocket.automation.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO to store Repository data
 */
public class RepoData {

    ArrayList<File> deletedFiles;
    List<String> newfilename;
    Integer repoCount = null;

    public void setRepoFileCount(Integer repoCount) {
        this.repoCount = repoCount;
    }

    public Integer getRepoFileCount() {
        return repoCount;
    }


    public void setDeletedFiles(ArrayList<File> files) {
        this.deletedFiles = files;
    }

    public ArrayList<File> getDeletedFiles() {
        return this.deletedFiles;
    }

    public List<String> setFiles(List<String> newfilename) {
        this.newfilename = newfilename;
      return newfilename;
    }

    public List<String> getFiles() {
        return this.newfilename;
    }

}
