/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.sdmxsource.webservice.main.finalPackage;

import demo.sdmxsource.webservice.main.chapter3.writer.SampleDataWriter;
import demo.sdmxsource.webservice.main.finalPackage.writer.MyStructureWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.Code;
import org.fao.fenix.commons.msd.dto.full.DSDCodelist;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;
import org.sdmxsource.sdmx.api.constants.DATA_TYPE;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.manager.retrieval.SdmxBeanRetrievalManager;
import org.sdmxsource.sdmx.api.model.beans.base.MaintainableBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.beans.reference.MaintainableRefBean;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.api.model.format.StructureFormat;
import org.sdmxsource.sdmx.api.util.ReadableDataLocation;
import org.sdmxsource.sdmx.dataparser.manager.DataWriterManager;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.sdmxbeans.model.data.SdmxDataFormat;
import org.sdmxsource.sdmx.structureretrieval.manager.InMemoryRetrievalManager;
import org.sdmxsource.sdmx.util.beans.reference.MaintainableRefBeanImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExportSDMX {
    @Autowired
    private StructureParsingManager structureParsingManager;
    @Autowired
    private ReadableDataLocationFactory rdlFactory;
    @Autowired
    private DataWriterManager dataWriterManager;
    @Autowired
    private SampleDataWriter sampleDataWriter;
    @Autowired
    private MyStructureWriter fileWriter;
    

    private void writeData(File structureFile,String name) throws IOException {
        //Step 1 - Create Data Writer Engine
        DataFormat dataFormat = new SdmxDataFormat(DATA_TYPE.COMPACT_2_1);
        DataWriterEngine dataWriterEngine = dataWriterManager.getDataWriterEngine(dataFormat, getFileOutputStreamData(name));
        //Step 2 - Get Data Structure & Data Flow
        ReadableDataLocation rdl = rdlFactory.getReadableDataLocation(structureFile);
        SdmxBeanRetrievalManager retrievalManager = new InMemoryRetrievalManager(rdl);
        String agencyId = "SDMXSOURCE";
        String version = MaintainableBean.DEFAULT_VERSION;  //1.0
        MaintainableRefBean dsdRef = new MaintainableRefBeanImpl(agencyId, "WDI", version);
        MaintainableRefBean flowRef = new MaintainableRefBeanImpl(agencyId, "DF_WDI", version);
        DataStructureBean dsd = retrievalManager.getMaintainableBean(DataStructureBean.class, dsdRef);
        DataflowBean dataflow = retrievalManager.getMaintainableBean(DataflowBean.class, flowRef);
	//sampleDataWriter.writeSampleData(dsd, dataflow, dataWriterEngine);
	//sampleDataWriter.writeSampleCrossSectionalData(dsd, dataflow, dataWriterEngine);
	sampleDataWriter.writeSampleFlatData(dsd, dataflow, dataWriterEngine);
    }
    
    public  void main(String[] args) throws IOException {
	//Step 1 - Get the Application Context
	ClassPathXmlApplicationContext applicationContext = 
                new ClassPathXmlApplicationContext("spring/spring-beans-chapter1.xml");

        //Step 2 - Get the main class from the Spring beans container
        ExportSDMX main = applicationContext.getBean(ExportSDMX.class);
        
        //Step 3 - Create a Readable Data Location from the File
        File structureFile = new File("src/main/resources/structures/chapter2/structures_full.xml");

        main.writeData(structureFile,"src/main/resources/data/chapter3/sample_data.xml");
        execution(null ,null,null ,null);
        applicationContext.close();
	}
        
    /**
     *
     * @param res
     * @param codeList
     * @throws IOException
     */
    public  void execution(Resource<DSDDataset, Object[]> res,Collection<Resource<DSDCodelist,Code>> codeList,OutputStream structureFile,
            OutputStream dataFile) throws IOException {        
        //Step 1 - Get the Application Context
	ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("sdmx/spring/context.xml");
	//Step 2 - Get the main class from the Spring beans container
	ExportSDMX main = applicationContext.getBean(ExportSDMX.class);    
        //Step 3
	OutputStream outStructure = main.getFileOutputStreamStructure();            
         //Step 4 - Define the output format
	STRUCTURE_OUTPUT_FORMAT sdmxFormat = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
	StructureFormat outputFormat = new SdmxStructureFormat(sdmxFormat);        
       //Step 5 - Write the structures out to the fie
	//main.fileWriter.writeStructureToFile(outputFormat, outStructure,res,codeList);
       main.fileWriter.writeStructureToFile(outputFormat, structureFile,dataFile,res,codeList);
       
        //main.fileWriter.getDataFile();
        applicationContext.close();
    }
	
      //        System.out.println(res.getMetadata().getDsd().getColumns());
      //        Iterator li=  res.getMetadata().getDsd().getColumns().iterator();
      //        while(li.hasNext()){   DSDColumn temp=(DSDColumn)li.next();}
      //        System.out.println("REEEEEES");
     
    private OutputStream getFileOutputStreamData(String name) throws IOException {
        File structureFile = new File(name);//"src/main/resources/sdmx/data/sample_data.xml"
        System.out.println("File Deleted : "+ structureFile.delete());
        System.out.println("File Created : "+structureFile.createNewFile());
        return new FileOutputStream(structureFile);
    }
    private OutputStream getFileOutputStreamDataMain(String name) throws IOException {
        File structureFile = new File(name);//"src/main/resources/data/chapter3/sample_data.xml"
        System.out.println("File Deleted : "+ structureFile.delete());
        System.out.println("File Created : "+structureFile.createNewFile());
        
        return new FileOutputStream(structureFile);
    }
    private OutputStream getFileOutputStreamStructure() throws IOException {
        File structureFile = new File("src/main/resources/sdmx/structures/webservice_structures.xml");
        System.out.println("File Deleted : "+ structureFile.delete());
        System.out.println("File Created : "+structureFile.createNewFile());
        
        return new FileOutputStream(structureFile);
    }
}