/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.kraken.projectviewer.utils;

import br.uff.ic.gems.resources.data.ConflictingChunk;
import br.uff.ic.gems.resources.data.ConflictingFile;
import br.uff.ic.gems.resources.data.Project;
import br.uff.ic.gems.resources.data.Revision;
import br.uff.ic.gems.resources.data.dao.sql.ConflictingChunkJDBCDAO;
import br.uff.ic.gems.resources.data.dao.sql.ConflictingFileJDBCDAO;
import br.uff.ic.gems.resources.data.dao.sql.JDBCConnection;
import br.uff.ic.gems.resources.data.dao.sql.RevisionJDBCDAO;
import br.uff.ic.kraken.projectviewer.pages.PagesName;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author gleiph
 */
public class Navigation {

    private Project project;
    private Revision revision;
    private ConflictingFile conflictingFile;
    private ConflictingChunk conflictingChunk;

    private String revisionVisibility = "hidden";
    private String conflictingFileVisibility = "hidden";
    private String conflictingChunkVisibility = "hidden";

    public String dataNavigation(Long id, String dataType) throws SQLException {

        setProject(null);
        setRevision(null);
        setConflictingFile(null);
        setConflictingChunk(null);

        revisionVisibility = "hidden";
        conflictingFileVisibility = "hidden";
        conflictingChunkVisibility = "hidden";

        try (Connection connection = (new JDBCConnection()).getConnection(DatabaseConfiguration.database)) {
        switch (dataType) {
            case DataTypes.PROJECT:
                System.out.println("Implement...");
                break;
            case DataTypes.REVISION:
                
                RevisionJDBCDAO revisionDAO = new RevisionJDBCDAO(connection);
                setRevision(revisionDAO.selectAllByRevisionId(id));
                revisionVisibility = "visible";
                System.out.println(revision.getSha());
                break;
            case DataTypes.CONFLICTING_FILE:
                ConflictingFileJDBCDAO conflictingFileDAO = new ConflictingFileJDBCDAO(connection);
                setConflictingFile(conflictingFileDAO.selectAllByConflictingFileId(id));
                conflictingFileVisibility = "visible";
                System.out.println(conflictingFile.getName());
                break;
            case DataTypes.CONFLICTING_CHUNK:
                ConflictingChunkJDBCDAO conflictingChunkDAO = new ConflictingChunkJDBCDAO(connection);
                setConflictingChunk(conflictingChunkDAO.selectAllByConflictingChunkId(id));
                conflictingChunkVisibility = "visible";
                System.out.println(conflictingChunk.getIdentifier());
                break;
        }

        return PagesName.showConflicts + "?faces-redirect=true";
        }
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the revision
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    /**
     * @return the conflictingFile
     */
    public ConflictingFile getConflictingFile() {
        return conflictingFile;
    }

    /**
     * @param conflictingFile the conflictingFile to set
     */
    public void setConflictingFile(ConflictingFile conflictingFile) {
        this.conflictingFile = conflictingFile;
    }

    /**
     * @return the conflictingChunk
     */
    public ConflictingChunk getConflictingChunk() {
        return conflictingChunk;
    }

    /**
     * @param conflictingChunk the conflictingChunk to set
     */
    public void setConflictingChunk(ConflictingChunk conflictingChunk) {
        this.conflictingChunk = conflictingChunk;
    }

    /**
     * @return the revisionVisibility
     */
    public String getRevisionVisibility() {
        return revisionVisibility;
    }

    /**
     * @param revisionVisibility the revisionVisibility to set
     */
    public void setRevisionVisibility(String revisionVisibility) {
        this.revisionVisibility = revisionVisibility;
    }

    /**
     * @return the conflictingFileVisibility
     */
    public String getConflictingFileVisibility() {
        return conflictingFileVisibility;
    }

    /**
     * @param conflictingFileVisibility the conflictingFileVisibility to set
     */
    public void setConflictingFileVisibility(String conflictingFileVisibility) {
        this.conflictingFileVisibility = conflictingFileVisibility;
    }

    /**
     * @return the conflictingChunkVisibility
     */
    public String getConflictingChunkVisibility() {
        return conflictingChunkVisibility;
    }

    /**
     * @param conflictingChunkVisibility the conflictingChunkVisibility to set
     */
    public void setConflictingChunkVisibility(String conflictingChunkVisibility) {
        this.conflictingChunkVisibility = conflictingChunkVisibility;
    }
}
