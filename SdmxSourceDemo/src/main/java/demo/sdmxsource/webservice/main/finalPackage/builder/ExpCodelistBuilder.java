package demo.sdmxsource.webservice.main.finalPackage.builder;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.Code;
import org.fao.fenix.commons.msd.dto.full.DSDCodelist;
import org.fao.fenix.commons.msd.dto.full.DSDColumn;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.springframework.stereotype.Service;

/**
 * Create the codelists used for the demo project
 */
@Service
public class ExpCodelistBuilder {

	public CodelistBean buildCountryCodelist() {
		CodelistMutableBean codelistMutable = new CodelistMutableBeanImpl();
		codelistMutable.setAgencyId("SDMXSOURCE");
		codelistMutable.setId("CL_COUNTRY");
		codelistMutable.setVersion("1.0");
		codelistMutable.addName("en", "Country");
		
		codelistMutable.createItem("UK", "United Kingdom");
		codelistMutable.createItem("FR", "France");
		codelistMutable.createItem("DE", "Germany");
		
		return codelistMutable.getImmutableInstance();
	}

	
	public CodelistBean buildIndicatorCodelist() {
		CodelistMutableBean codelistMutable = new CodelistMutableBeanImpl();
		codelistMutable.setAgencyId("SDMXSOURCE");
		codelistMutable.setId("CL_INDICATOR");
		codelistMutable.setVersion("1.0");
		codelistMutable.addName("en", "World Development Indicators");
		
		codelistMutable.createItem("E", "Environment");
		codelistMutable.createItem("E_A", "Aggriculture land").setParentCode("E");
		codelistMutable.createItem("E_P", "Population").setParentCode("E");
		

		codelistMutable.createItem("H", "Health");
		codelistMutable.createItem("H_B", "Birth Rate").setParentCode("H");
		codelistMutable.createItem("H_C", "Children (0-14) living with HIV").setParentCode("H");
		
		return codelistMutable.getImmutableInstance();
	}
        public static CodelistBean buildCodeList(Resource<DSDCodelist,Code> codelist){
          // if(col.getKey())
            {
                CodelistMutableBean codelistMutable = new CodelistMutableBeanImpl();
                //System.out.println(col);          
                codelistMutable.setAgencyId("FENIX");
               
               // System.out.println(" ESSAI "+codelist.getMetadata().getUid().toUpperCase());
                codelistMutable.setId("CL_"+codelist.getMetadata().getUid().toUpperCase());
		codelistMutable.setVersion(/*"1.0"+*/codelist.getMetadata().getMetadataStandardVersion());
		codelistMutable.addName("en", codelist.getMetadata().getUid());
                //System.out.println(codelist);  
                Iterator li=  codelist.getData().iterator();
                int K=0;
              while(li.hasNext()) {       
                      
                          Code temp=(Code)li.next();
                   
                    String ccooddee=temp.getCode();
                     String ttiittllee=ccooddee+" : "+temp.getTitle().get("EN");
                    Pattern p1=Pattern.compile("[a-zA-Z0-9]+");
                    Matcher m1=p1.matcher(ccooddee);
                    if(!m1.matches())
                    {ccooddee="_"+K++;}
                      
                  codelistMutable.createItem(ccooddee,ttiittllee  );
                   
                }
		//codelistMutable.createItem("UK", "United Kingdom");

        return codelistMutable.getImmutableInstance();
            }
          //  else{return null;}
        }		

}
