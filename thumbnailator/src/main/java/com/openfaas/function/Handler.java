package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.lang.Error;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Handler implements com.openfaas.model.IHandler {

    public IResponse Handle(IRequest req) {
        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        GarbageCollectorMXBean scavenge = gcs.get(0);
        GarbageCollectorMXBean markSweep = gcs.get(1);

        long countBeforeScavenge = scavenge.getCollectionCount();
        long timeBeforeScavenge = scavenge.getCollectionTime();
        long countBeforeMarkSweep = markSweep.getCollectionCount();
        long timeBeforeMarkSweep = markSweep.getCollectionTime();
        long before = System.nanoTime();

        String err = callFunction();

        long after = System.nanoTime();
        long countAfterScavenge = scavenge.getCollectionCount();
        long timeAfterScavenge = scavenge.getCollectionTime();
        long countAfterMarkSweep = markSweep.getCollectionCount();
        long timeAfterMarkSweep = markSweep.getCollectionTime();

        String output = err + System.lineSeparator();
        if (err.length() == 0) {
            output = Long.toString(after - before) + "," + // Business Logic Time in Milliseconds
                Long.toString(countAfterScavenge - countBeforeScavenge) + "," + // Scavenge Number of Collections
                Long.toString(timeAfterScavenge - timeBeforeScavenge) + "," + // Scavenge Collections Time Spent in Milliseconds
                Long.toString(countAfterMarkSweep - countBeforeMarkSweep) + "," + // MarkSweep Number of Collections
                Long.toString(timeAfterMarkSweep - timeBeforeMarkSweep); // MarkSweep Collections Time Spent in Milliseconds
        }

        Response res = new Response();
        res.setBody(output);
        return res;
    }

    static double scale;
    static BufferedImage image;
    static {
        try {
            URL imageUrl = new URL(System.getenv("image_url"));
            scale = Double.parseDouble(System.getenv("scale"));
            image = ImageIO.read(imageUrl);
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String callFunction() {
        String err = "";
        try {
            AffineTransform transform = AffineTransform.getScaleInstance(scale, scale); 
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR); 
            op.filter(image, null).flush();
        } catch (Exception e) {
            err = e.toString() + System.lineSeparator()
            		+ e.getCause() + System.lineSeparator()
            		+ e.getMessage();
            e.printStackTrace();
           
        } catch (Error e) {
            err = e.toString() + System.lineSeparator()
            		+ e.getCause() + System.lineSeparator()
            		+ e.getMessage();
            e.printStackTrace();
        }

        return err;
    }

}