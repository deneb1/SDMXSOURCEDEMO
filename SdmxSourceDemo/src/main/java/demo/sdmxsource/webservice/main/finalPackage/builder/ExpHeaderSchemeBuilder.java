/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.sdmxsource.webservice.main.finalPackage.builder;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;
import org.sdmxsource.sdmx.api.model.header.HeaderBean;
import org.sdmxsource.sdmx.sdmxbeans.model.header.HeaderBeanImpl;

/**
 *
 * @author joyeux
 */
public class ExpHeaderSchemeBuilder {
    public ExpHeaderSchemeBuilder(){}
    public HeaderBean buildheader(String ID,String sender)
    {
        Object test;
        HeaderBean hb=new HeaderBeanImpl(ID,sender);
        
        return hb;
    /*
     * 
     * AgencySchemeMutableBean mutableBean = AgencySchemeBeanImpl.createDefaultScheme().getMutableInstance();
		mutableBean.createItem(AgencyID, AgencyDescription);
		return mutableBean.getImmutableInstance();
     */
    }

    public HeaderBean buildheader(MeIdentification<DSDDataset> metadata, String sender) {
         
        HeaderBean hb=new HeaderBeanImpl(metadata.getUid(),sender);
        return hb;
    }
}
