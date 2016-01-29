/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.sdmxsource.webservice.main.finalPackage.writer;
import  demo.sdmxsource.webservice.main.finalPackage.builder.ExpAgencySchemeBuilder;
import  demo.sdmxsource.webservice.main.finalPackage.builder.ExpCodelistBuilder;
import  demo.sdmxsource.webservice.main.finalPackage.builder.ExpConceptSchemeBuilder;
import  demo.sdmxsource.webservice.main.finalPackage.builder.ExpDataStructureBuilder;
import  demo.sdmxsource.webservice.main.finalPackage.builder.ExpDataflowBuilder;
import demo.sdmxsource.webservice.main.finalPackage.builder.ExpHeaderSchemeBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.Code;
import org.fao.fenix.commons.msd.dto.full.DSDCodelist;
import org.fao.fenix.commons.msd.dto.full.DSDColumn;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.fao.fenix.commons.msd.dto.full.OjCodeList;
import org.sdmxsource.sdmx.api.constants.DATA_TYPE;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.sdmxsource.sdmx.api.model.header.HeaderBean;
import org.sdmxsource.sdmx.dataparser.manager.DataWriterManager;
import org.sdmxsource.sdmx.sdmxbeans.model.data.SdmxDataFormat;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.sdmxsource.sdmx.dataparser.manager.DataWriterManager;



/**
 *
 * @author joyeux
 */
@Service
public class MyStructureWriter {
    @Autowired
    private StructureWriterManager structureWritingManager;
    
    @Autowired
    private ExpAgencySchemeBuilder agencySchemeBuilder;
    
    @Autowired
    private ExpCodelistBuilder codelistBuilder;
    @Autowired
    private ExpConceptSchemeBuilder conceptSchemeBuilder;
    @Autowired
    private ExpDataStructureBuilder dataStructureBuilder;
    @Autowired
    private ExpDataflowBuilder dataflowBuilder;
   
    
   @Autowired
    private DataWriterManager dataWriterManager;
    
  /* @Autowired
    private SampleDataWriter sampleDataWriter;
   */
    
    
    public void writeStructureToFile(StructureFormat outputFormat, OutputStream out,OutputStream out2,
            Resource<DSDDataset,Object[]> res,
            Collection<Resource<DSDCodelist,Code>> codeList)  throws IOException {
                SdmxBeans beans = new SdmxBeansImpl();
                
                
                ExpHeaderSchemeBuilder headerbuilder=new ExpHeaderSchemeBuilder();
                HeaderBean hb=headerbuilder.buildheader(res.getMetadata(),"FENIX");
                beans.setHeader(hb);
                beans.addAgencyScheme(agencySchemeBuilder.buildAgencyScheme("FENIX", "Fenix"));
                if(res!=null && codeList!=null){
                    Iterator li=codeList.iterator();
                    while( li.hasNext() )
                    {beans.addCodelist(ExpCodelistBuilder.buildCodeList((Resource<DSDCodelist,Code>)li.next()));}
                }
          
	beans.addIdentifiable(conceptSchemeBuilder.myBuildConceptScheme(res.getMetadata().getDsd().getColumns().iterator()));
	
        
       // DataStructureBean dsd = dataStructureBuilder.buildDataStructure();
	DataStructureBean dsd = dataStructureBuilder.myBuildDataStructure(res);
	
        beans.addIdentifiable(dsd);
        DataflowBean dfb=dataflowBuilder.buildDataflow("DF_FENIX", "FENIX DataFlow", dsd);
	beans.addIdentifiable(dfb);
        
        // END OF STRUCTURE
        //begining of dataset 
        
        DataFormat dataFormat = new SdmxDataFormat(DATA_TYPE.COMPACT_2_1);
	//DataWriterEngine dataWriterEngine = dataWriterManager.getDataWriterEngine(dataFormat, getFileDataOutputStream());
	DataWriterEngine dataWriterEngine = dataWriterManager.getDataWriterEngine(dataFormat, out2);
	
        SampleDataWriter sampleDataWriter=new SampleDataWriter();
       
        sampleDataWriter.writeSampleFlatData(dsd, dfb, dataWriterEngine,res);
        
                //TEST
//                beans.setHeader(null);
                
        structureWritingManager.writeStructures(beans, outputFormat, out);
	}
    private OutputStream getFileDataOutputStream() throws IOException {
		File structureFile = new File("src/main/resources/sdmx/data/sample_data.xml");
		System.out.println("File Deleted : "+ structureFile.delete());
		System.out.println("File Created : "+structureFile.createNewFile());
		
                return new FileOutputStream(structureFile);
	}
}