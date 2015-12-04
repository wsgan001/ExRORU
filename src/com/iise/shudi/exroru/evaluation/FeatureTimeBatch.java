package com.iise.shudi.exroru.evaluation;

import com.iise.shudi.bp.BehavioralProfileSimilarity;
import com.iise.shudi.exroru.RefinedOrderingRelation;
import com.iise.shudi.exroru.RormSimilarity;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shudi on 2015/12/1.
 */
public class FeatureTimeBatch {
    public static final String ROOT_FOLDER = "C:\\Users\\Shudi\\Desktop\\rorm\\";
    public static int ITER_COUNT = 10;

    public static void main(String[] args) throws Exception {
        RefinedOrderingRelation.SDA_WEIGHT = 0.0;
        RefinedOrderingRelation.IMPORTANCE = false;
        FeatureTimeBatch tb = new FeatureTimeBatch();
        double dg = tb.batch("DG");
        double tc = tb.batch("TC");
        double sap = tb.batch("SAP");
//        double ibm = tb.batch("IBM");
        System.out.println("DG: " + dg);
        System.out.println("TC: " + tc);
        System.out.println("SAP: " + sap);
//        System.out.println("IBM: " + ibm);
    }

    private double batch(String modelFolderName) throws Exception {
        File[] modelFiles = new File(ROOT_FOLDER + modelFolderName).listFiles();
        List<NetSystem> nets = loadNets(modelFiles);
        return timeBatch(nets);
    }

    private List<NetSystem> loadNets(File[] files) throws Exception {
        List<NetSystem> nets = new ArrayList<>();
        PNMLSerializer pnmlSerializer = new PNMLSerializer();
        for(int i = 0; i < files.length; ++i) {
            NetSystem net = pnmlSerializer.parse(files[i].getAbsolutePath());
            net.setName(files[i].getName());
            nets.add(net);
        }
        return nets;
    }

    private double timeBatch(List<NetSystem> models) {
//        RormSimilarity measure = new RormSimilarity();
        BehavioralProfileSimilarity measure = new BehavioralProfileSimilarity();
        int totalCount = models.size(), finish = 0;
        long totalTime = 0L;
        for (int p = 0; p < models.size(); ++p) {
            System.out.println((++finish) + "/" + totalCount + " " + models.get(p).getName());
            long a = System.nanoTime();
            for (int i = 0; i < ITER_COUNT; ++i) {
                measure.feature(models.get(p));
            }
            long b = System.nanoTime();
            totalTime += (b - a) / ITER_COUNT;
        }
        double avgTime = ((double) totalTime) / totalCount;
        return avgTime;
    }
}