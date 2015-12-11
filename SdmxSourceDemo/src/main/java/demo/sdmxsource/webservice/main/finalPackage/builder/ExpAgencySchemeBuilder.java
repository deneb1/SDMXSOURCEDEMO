package demo.sdmxsource.webservice.main.finalPackage.builder;

import org.sdmxsource.sdmx.api.model.beans.base.AgencySchemeBean;
import org.sdmxsource.sdmx.api.model.mutable.base.AgencySchemeMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.beans.base.AgencySchemeBeanImpl;
import org.springframework.stereotype.Service;

@Service
public class ExpAgencySchemeBuilder {

	
	

	public AgencySchemeBean buildAgencyScheme(String AgencyID,String AgencyDescription) {
		AgencySchemeMutableBean mutableBean = AgencySchemeBeanImpl.createDefaultScheme().getMutableInstance();
		mutableBean.createItem(AgencyID, AgencyDescription);
		return mutableBean.getImmutableInstance();
	}
	
}
