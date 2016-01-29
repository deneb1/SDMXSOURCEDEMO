/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.sdmxsource.webservice.main.finalPackage.writer;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.fao.fenix.commons.msd.dto.data.Resource;
import org.fao.fenix.commons.msd.dto.full.DSDColumn;
import org.fao.fenix.commons.msd.dto.full.DSDDataset;

import org.sdmxsource.sdmx.api.constants.DATASET_ACTION;
import org.sdmxsource.sdmx.api.constants.DIMENSION_AT_OBSERVATION;
import org.sdmxsource.sdmx.api.constants.TIME_FORMAT;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataStructureBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DimensionBean;
import org.sdmxsource.sdmx.api.model.header.DatasetHeaderBean;
import org.sdmxsource.sdmx.api.model.header.DatasetStructureReferenceBean;
import org.sdmxsource.sdmx.api.model.header.HeaderBean;
import org.sdmxsource.sdmx.sdmxbeans.model.header.DatasetHeaderBeanImpl;
import org.sdmxsource.sdmx.sdmxbeans.model.header.DatasetStructureReferenceBeanImpl;
import org.sdmxsource.sdmx.util.date.DateUtil;
import org.springframework.stereotype.Service;

/**
 *
 * @author joyeux Date 09/12/2015
 */
public class SampleDataWriter {

    /**
     * Writes data to the DataWriterEngine
     *
     * @param dsd the data structure the dataset is for
     * @param dataflow the dataflow the dataset is for (optional)
     * @param dwe the data writer to write the data to
     */
    void sampleDataWriter() {
    }

    public void writeSampleData(DataStructureBean dsd, DataflowBean dataflow, DataWriterEngine dwe) {
        /*try {
         dwe.startDataset(dataflow, dsd, null);
			
         dwe.startSeries();
         dwe.writeSeriesKeyValue("COUNTRY", "UK");
         dwe.writeSeriesKeyValue("INDICATOR", "E_P");
         dwe.writeObservation("2000", "18.22");
         dwe.writeObservation(new Date(), "17.63", TIME_FORMAT.WEEK);

         dwe.startSeries();
         dwe.writeSeriesKeyValue("COUNTRY", "FR");
         dwe.writeSeriesKeyValue("INDICATOR", "E_P");
         dwe.writeObservation("2000", "22.22");
         dwe.writeObservation(new Date(), "15.63", TIME_FORMAT.HALF_OF_YEAR);
         } finally {
         dwe.close();
         }*/
    }

    /**
     * Writes data to the DataWriterEngine
     *
     * @param dsd the data structure the dataset is for
     * @param dataflow the dataflow the dataset is for (optional)
     * @param dwe the data writer to write the data to
     */
    public void writeSampleCrossSectionalData(DataStructureBean dsd, DataflowBean dataflow, DataWriterEngine dwe) {
        /*try {
			
         DatasetStructureReferenceBean dsRef = 
         new DatasetStructureReferenceBeanImpl("Test", dsd.asReference(), null, null, "COUNTRY");
			
         DatasetHeaderBean header = 
         new DatasetHeaderBeanImpl("DS12345", DATASET_ACTION.INFORMATION, dsRef);
			
         dwe.startDataset(dataflow, dsd, header);

         dwe.startSeries();
         dwe.writeSeriesKeyValue("INDICATOR", "E_P");
         dwe.writeSeriesKeyValue(DimensionBean.TIME_DIMENSION_FIXED_ID, "2000");
         dwe.writeObservation("UK", "18.22");
         dwe.writeObservation("FR", "22.22");

         dwe.startSeries();
         dwe.writeSeriesKeyValue("INDICATOR", "E_P");
         String date = DateUtil.formatDate(new Date(), TIME_FORMAT.WEEK);
         dwe.writeSeriesKeyValue(DimensionBean.TIME_DIMENSION_FIXED_ID, date);
         dwe.writeObservation("UK", "17.63");
         dwe.writeObservation("FR", "15.63");
         } finally {
         dwe.close();
         }*/
    }

    /**
     * Writes data to the DataWriterEngine
     *
     * @param dsd the data structure the dataset is for
     * @param dataflow the dataflow the dataset is for (optional)
     * @param dwe the data writer to write the data to
     */
    public void writeSampleFlatData(DataStructureBean dsd, DataflowBean dataflow, DataWriterEngine dwe, Resource<DSDDataset, Object[]> res) {
        try {
            DatasetStructureReferenceBean dsRef =
                    new DatasetStructureReferenceBeanImpl("Test", dsd.asReference(), null, null, DIMENSION_AT_OBSERVATION.ALL.getVal());

            DatasetHeaderBean header =
                    new DatasetHeaderBeanImpl("DS12345", DATASET_ACTION.INFORMATION, dsRef);
                    //new DatasetHeaderBeanImpl("DS12345", DATASET_ACTION.INFORMATION, dsRef);
            
            dwe.startDataset(dataflow, dsd, header);


            /*
             dwe.writeSeriesKeyValue("INDICATOR", "E_P");
             dwe.writeSeriesKeyValue(DimensionBean.TIME_DIMENSION_FIXED_ID, "2000");
             dwe.writeObservation("COUNTRY", "UK", "18.22");
             dwe.writeObservation("COUNTRY", "FR", "19.22");

             dwe.startSeries();
             dwe.writeSeriesKeyValue("COUNTRY", "FR");
             dwe.writeSeriesKeyValue(DimensionBean.TIME_DIMENSION_FIXED_ID, "2000");
             dwe.writeObservation("INDICATOR", "E_P", "22.22");
             dwe.writeObservation("INDICATOR", "E_P", "23.22");
             */
            Iterator lidata = res.getData().iterator();
            int count = 0;
            while (lidata.hasNext() && count++ < 500) {
                dwe.startSeries();

                Object[] element = (Object[]) lidata.next();
                String myValue = "";
                String myTime = "";
                String myTimeName = "";
                Iterator lidsd = res.getMetadata().getDsd().getColumns().iterator();
                int i = 0;
                while (lidsd.hasNext()) {
                    DSDColumn columntemp = (DSDColumn) lidsd.next();
                    if (columntemp.getSubject() != null && columntemp.getSubject().equals("value")) {
                        //  System.out.println("Value "+columntemp.getId().toUpperCase());
                        //Value
                        if (element[i] != null) {
                            myValue = element[i].toString();
                        }

                    } else {
                        if (columntemp.getKey() != null && columntemp.getKey()) {
                            //Dimension
                            if (columntemp.getSubject().equals("time")) {
                                myTime = element[i].toString();
                                myTimeName = columntemp.getId().toUpperCase();
                            } else {
                                //  System.out.println("dimension"+columntemp.getId().toUpperCase());
                                dwe.writeSeriesKeyValue(columntemp.getId().toUpperCase(), element[i].toString());
                            }
                        } else {
                            //Attribute  
                            if (element[i] != null) {
                                dwe.writeAttributeValue(columntemp.getId().toUpperCase(), element[i].toString());
                            } else {
                                dwe.writeAttributeValue(columntemp.getId().toUpperCase(), "");
                            }
                        }

                    }


                    //  System.out.println("OK"+ element[1]);
                    i++;
                }
                ///// INSERT VALUE
                dwe.writeObservation(myTimeName, myTime, myValue);
            }
            System.out.println("TEST");

        } finally {
            dwe.close();
        }
    }
}
