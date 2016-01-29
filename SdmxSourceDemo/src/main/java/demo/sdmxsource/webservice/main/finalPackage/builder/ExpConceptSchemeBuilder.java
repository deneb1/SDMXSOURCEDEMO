package  demo.sdmxsource.webservice.main.finalPackage.builder;

import java.util.Iterator;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.DSDColumn;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.MeIdentification;
import org.sdmxsource.sdmx.api.model.beans.conceptscheme.ConceptSchemeBean;
import org.sdmxsource.sdmx.api.model.mutable.conceptscheme.ConceptSchemeMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.conceptscheme.ConceptSchemeMutableBeanImpl;
import org.springframework.stereotype.Service;

@Service
public class ExpConceptSchemeBuilder {

	public ConceptSchemeBean buildConceptScheme() {
		ConceptSchemeMutableBean conceptSchemeMutable = new ConceptSchemeMutableBeanImpl();
		conceptSchemeMutable.setAgencyId("SDMXSOURCE");
		conceptSchemeMutable.setId("CONCEPTS");
		conceptSchemeMutable.setVersion("1.0");
		conceptSchemeMutable.addName("en", "Web Service Concepts");
		
		conceptSchemeMutable.createItem("COUNTRY", "Country");
		conceptSchemeMutable.createItem("INDICATOR", "World Development Indicators");
		conceptSchemeMutable.createItem("TIME", "Time");
		conceptSchemeMutable.createItem("OBS_VALUE", "Observation Value");
		
		return conceptSchemeMutable.getImmutableInstance();
	}
        
        public   ConceptSchemeBean     myBuildConceptScheme( Iterator metadata){
        ConceptSchemeMutableBean conceptSchemeMutable = new ConceptSchemeMutableBeanImpl();
        conceptSchemeMutable.setAgencyId("FENIX");
        conceptSchemeMutable.setId("CONCEPTS");
	conceptSchemeMutable.setVersion("1.0");
        conceptSchemeMutable.addName("en", "Fenix Concepts");
       while(metadata.hasNext())
       {
       DSDColumn temp=(DSDColumn)metadata.next();
       //System.out.println("test"+temp.getId().toUpperCase()+" __ " + temp.getTitle().get("EN"));
       conceptSchemeMutable.createItem(temp.getId().toUpperCase(), temp.getTitle().get("EN"));
       }
        
//	conceptSchemeMutable.createItem("COUNTRY", "Country");
        
        
        return conceptSchemeMutable.getImmutableInstance();
        }
	
}
