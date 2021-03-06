/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.kraken.projectviewer.bean;

import br.uff.ic.gems.resources.ast.ASTTranslator;
import br.uff.ic.gems.resources.data.LanguageConstruct;
import br.uff.ic.kraken.projectviewer.utils.Navigation;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author gleiph
 */
@Named(value = "showConflictsBean")
@RequestScoped
public class ShowConflictsBean extends Navigation implements Serializable {

    private Long id;
    private String dataType;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String navigate() {
        try {
            return super.dataNavigation(id, dataType);
        } catch (SQLException ex) {
            Logger.getLogger(ShowConflictsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<String> getGeneralKindOfConflict(List<LanguageConstruct> leftFiltered, List<LanguageConstruct> rightFiltered) {

        if (leftFiltered == null || rightFiltered == null) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();

        for (LanguageConstruct lf : leftFiltered) {
            String translatedName = ASTTranslator.translate(lf.getName());
            if (!result.contains(translatedName)) {
                result.add(translatedName);
            }
        }

        for (LanguageConstruct rf : rightFiltered) {
            
            String translatedName = ASTTranslator.translate(rf.getName());

            if (!result.contains(translatedName)) {
                result.add(translatedName);
            }
        }

        Collections.sort(result);

        return result;
    }
}
