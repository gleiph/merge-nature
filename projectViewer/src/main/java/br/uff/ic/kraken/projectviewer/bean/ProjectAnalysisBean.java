/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.kraken.projectviewer.bean;

import br.uff.ic.gems.merge.vcs.Git;
import br.uff.ic.github.mergeviewer.analyzers.ProjectAnalyzer;
import br.uff.ic.github.mergeviewer.kinds.Project;
import java.io.File;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author gleiph
 */
@Named(value = "projectAnalysisBean")
@RequestScoped
public class ProjectAnalysisBean {

    private String repositoryUrl;
    private String repositoryPath;
    private String projectName;

    public String cloneRepository() {

        Git git = new Git(repositoryPath);
        git.clone(repositoryUrl);

        return null;
    }

    public String analyze() {
        
        File repositoriesDirectory = new File(repositoryPath);
        if(!repositoriesDirectory.isDirectory())
            repositoriesDirectory.mkdirs();
        
        Git git = new Git(repositoryPath);
        git.clone(repositoryUrl);

        String projectPath;
        if (!repositoryPath.endsWith(File.separator)) {
            projectPath = repositoryPath + File.separator + projectName;
        } else {
            projectPath = repositoryPath + projectName;
        }

        ProjectAnalyzer pa = new ProjectAnalyzer();
        Project analyze = pa.analyze(projectPath);

        return null;
    }

    /**
     * @return the repositoryUrl
     */
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
     * @param repositoryUrl the repositoryUrl to set
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    /**
     * @return the repositoryPath
     */
    public String getRepositoryPath() {
        return repositoryPath;
    }

    /**
     * @param repositoryPath the repositoryPath to set
     */
    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
