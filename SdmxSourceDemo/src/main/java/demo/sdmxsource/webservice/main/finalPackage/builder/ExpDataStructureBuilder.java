package  demo.sdmxsource.webservice.main.finalPackage.builder;

import java.util.Iterator;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.DSDColumn;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.OjCodeList;
import org.fao.fenix.commons.msd.dto.type.DataType;
import org.sdmxsource.sdmx.api.constants.ATTRIBUTE_ATTACHMENT_LEVEL;
import org.sdmxsource.sdmx.api.constants.SDMX_STRUCTURE_TYPE;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.reference.StructureReferenceBean;
import org.sdmxsource.sdmx.api.model.mutable.datastructure.DataStructureMutableBean;
import org.sdmxsource.sdmx.api.model.mutable.datastructure.DimensionMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.datastructure.AttributeMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.datastructure.DataStructureMutableBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.datastructure.DimensionMutableBeanImpl;
import org.sdmxsource.sdmx.util.beans.reference.StructureReferenceBeanImpl;
import org.springframework.stereotype.Service;

@Service
public class ExpDataStructureBuilder {
	public DataStructureBean buildDataStructure() {
		DataStructureMutableBean dsd = new DataStructureMutableBeanImpl();
		dsd.setAgencyId("SDMXSOURCE");
		dsd.setId("WDI");
		dsd.addName("en", "World Development Indicators");
		dsd.addDimension(createConceptReference("COUNTRY"), createCodelistReference("CL_COUNTRY"));
		dsd.addDimension(createConceptReference("INDICATOR"), createCodelistReference("CL_INDICATOR"));
		dsd.addDimension(createConceptReference("TIME"), null).setTimeDimension(true);
		dsd.addPrimaryMeasure(createConceptReference("OBS_VALUE"));
		return dsd.getImmutableInstance();
	}
        
        public DataStructureBean myBuildDataStructure(Resource<DSDDataset,Object[]> res) {
		DataStructureMutableBean dsd = new DataStructureMutableBeanImpl();
		dsd.setAgencyId("FENIX");
		dsd.setId(res.getMetadata().getUid());
		dsd.addName("en", res.getMetadata().getUid());
		Iterator li=res.getMetadata().getDsd().getColumns().iterator();
                while(li.hasNext())
                {
                    DSDColumn temp=(DSDColumn)li.next();
                    
                    if(temp.getSubject()!=null && temp.getSubject().equals("value"))
                        {
                            System.out.println("Value "+temp.getId().toUpperCase());
                        //Value
                            dsd.addPrimaryMeasure(createConceptReference(temp.getId().toUpperCase()));
                        }
                    else {
                        if(temp.getKey()!=null && temp.getKey()){
                        //Dimension
                        System.out.println("dimension"+temp.getId().toUpperCase());
                      if(temp.getDataType()==DataType.year || temp.getDataType()==DataType.month || temp.getDataType()==DataType.date || temp.getDataType()==DataType.time  )
                      {dsd.addDimension(createConceptReference(temp.getId().toUpperCase()), null).setTimeDimension(true);}  
                      else{
                          if(temp.getDataType()==DataType.code ){
                          OjCodeList colTemp=(OjCodeList)temp.getDomain().getCodes().iterator().next();
                          System.out.println(colTemp.getIdCodeList().toUpperCase());
                          dsd.addDimension(createConceptReference(temp.getId().toUpperCase()), 
                          createCodelistReference("CL_"+colTemp.getIdCodeList().toUpperCase()));
                          }
                          else{
                              
                           dsd.addDimension(createConceptReference(temp.getId().toUpperCase()), null).setMeasureDimension(true);//free text
                           //dsd.addDimension();
                          }
                      }
                      //dsd.addDimension(createConceptReference("INDICATOR"), createCodelistReference("CL_INDICATOR"));
                    }
                    else
                    {
                        //if("flag".equals(temp.getId()))
                        
                    /*      OjCodeList colTemp=(OjCodeList)temp.getDomain().getCodes().iterator().next();
                            dsd.addAttribute(createConceptReference(temp.getId().toUpperCase()), 
                            createCodelistReference("CL_"+colTemp.getIdCodeList().toUpperCase()));
                     */
                       // if(temp.getDataType()=="text")
                        {
                            AttributeMutableBeanImpl amb=new AttributeMutableBeanImpl();
                            amb.setConceptRef(createConceptReference(temp.getId().toUpperCase()));
                            amb.setAssignmentStatus("Mandatory");
                            /*if(temp.getKey()!=null && temp.getKey()==false){amb.setAttachmentLevel(ATTRIBUTE_ATTACHMENT_LEVEL.DATA_SET);}
                            else*/
                            {
                                amb.setAttachmentLevel(ATTRIBUTE_ATTACHMENT_LEVEL.OBSERVATION);
                            }
                            dsd.addAttribute(amb);
                        }
                     /*     System.out.println("attribut"+temp.getId().toUpperCase());
                            dsd.addAttribute(createAttributetReference(temp.getId().toUpperCase()), 
                            createCodelistReference("CL_"+temp.getId().toUpperCase()));
                    */
                        //attribut
                    /*      AttributeMutableBean attributeTemp=new AttributeMutableBeanImpl();
                            attributeTemp.setConceptRef(temp.getId());
                            dsd.addAttribute(attributeTemp);
                    */
                    // dsd.addAttribute(createConceptReference(temp.getId().toUpperCase()));
                    }
                    
                    }
             
                }
		/*dsd.addDimension(createConceptReference("COUNTRY"), createCodelistReference("CL_COUNTRY"));
		dsd.addDimension(createConceptReference("INDICATOR"), createCodelistReference("CL_INDICATOR"));
		dsd.addDimension(createConceptReference("TIME"), null).setTimeDimension(true);
		dsd.addPrimaryMeasure(createConceptReference("OBS_VALUE"));
		*/
		return dsd.getImmutableInstance();
	}
	
	private StructureReferenceBean createCodelistReference(String id) 
        {return new StructureReferenceBeanImpl("FENIX", id, "1.0", SDMX_STRUCTURE_TYPE.CODE_LIST);}
	
	private StructureReferenceBean createConceptReference(String id) 
        {return new StructureReferenceBeanImpl("FENIX", "CONCEPTS", "1.0", SDMX_STRUCTURE_TYPE.CONCEPT, id);}
}