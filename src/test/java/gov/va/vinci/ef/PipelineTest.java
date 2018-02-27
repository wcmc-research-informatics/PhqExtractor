package gov.va.vinci.ef;

import gov.va.vinci.ef.pipeline.*;
import gov.va.vinci.ef.types.Relation;
import junit.framework.Assert;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import java.io.File;
import java.util.Iterator;

public class PipelineTest {
  protected gov.va.vinci.leo.descriptors.LeoAEDescriptor aggregate = null;
  protected gov.va.vinci.leo.descriptors.LeoTypeSystemDescription types = null;
  protected String inputDir = "data/test/";
  protected String outputDir = "data/output/xmi/";
  protected boolean launchView = false;


  @org.junit.Before
  public void setup() throws Exception {
    ExtractorPipeline bs = new ExtractorPipeline();
    types = bs.getLeoTypeSystemDescription();
    aggregate = bs.getPipeline();

    File o = new File(outputDir);
    if (!o.exists()) {
      o.mkdirs();
    }//if
  }//setup


  @org.junit.Test
  public void testSimple() throws Exception {
    String docText = "Various unrelated text. PHQ-9 is 7. Various unrelated text. ";
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(aggregate.getAnalysisEngineDescription());
    JCas jcas = ae.newJCas();
    jcas.setDocumentText(docText);
    ae.process(jcas);
    Iterator<Annotation> aList = jcas.getAnnotationIndex(Relation.type).iterator();
    Assert.assertTrue(aList.hasNext());
    if (aList.hasNext()) {
      Relation r = (Relation) aList.next();
      Assert.assertEquals(r.getValue(), "7.0");
      Assert.assertNull(r.getValue2());
      Assert.assertEquals(r.getTerm(), "PHQ-9");
      Assert.assertFalse(aList.hasNext());
    }
  }

  @org.junit.Test
  public void testSimple2() throws Exception {
    String docText = "Various unrelated text. Pt scored 25 on Phq 9 . No cognitive issues. ";
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(aggregate.getAnalysisEngineDescription());
    JCas jcas = ae.newJCas();
    jcas.setDocumentText(docText);
    ae.process(jcas);
    Iterator<Annotation> aList = jcas.getAnnotationIndex(Relation.type).iterator();
    assert (aList.hasNext());
    if (aList.hasNext()) {
      Relation r = (Relation) aList.next();
      Assert.assertEquals(r.getValue(), "25.0");
      Assert.assertNull(r.getValue2());
      Assert.assertEquals(r.getTerm(), "Phq 9");
      Assert.assertFalse(aList.hasNext());
    }
  }

  @org.junit.Test
  public void testXmi() throws Exception {

    String docText = "";
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(
        aggregate.getAnalysisEngineDescription());

    File[] files = (new File(this.inputDir)).listFiles();
    if (files != null)
      for (File infile : files) {
        String filePath = infile.getAbsolutePath();
        if (infile.getName().endsWith(".txt")) {
          System.out.println("Processing " + filePath);
          try {

            docText = org.apache.uima.util.FileUtils.file2String(infile);
          } catch (Exception e) {
            System.out.println("Missing file!!");
          }
          if (org.apache.commons.lang3.StringUtils.isBlank(docText)) {
            System.out.println("Blank file!!");
          }

          JCas jcas = ae.newJCas();
          jcas.setDocumentText(docText);
          ae.process(jcas);
          try {
            File xmio = new File(outputDir, infile.getName() + ".xmi");
            XmiCasSerializer.serialize(jcas.getCas(), new java.io.FileOutputStream(xmio));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    if (launchView) launchViewer();
    assert (true);
    if (launchView) System.in.read();


  }//testXmi method

  protected void launchViewer() throws Exception {
    if (aggregate == null) {
      throw new RuntimeException("Aggregate is null, unable to generate descriptor for viewing xmi");
    }
    aggregate.toXML(outputDir);
    String aggLoc = aggregate.getDescriptorLocator().substring(5);
    java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node("org/apache/uima/tools/AnnotationViewer");
    if (aggLoc != null) {
      prefs.put("taeDescriptorFile", aggLoc);
    }//if mAggDesc != null
    if (outputDir != null) {
      prefs.put("inDir", outputDir);
    }//if mOutputDir != null
    org.apache.uima.tools.AnnotationViewerMain avm = new org.apache.uima.tools.AnnotationViewerMain();
    avm.setBounds(0, 0, 1000, 225);
    avm.setVisible(true);
  }//launchViewer method

  /**
   * @After
   */
  public void cleanup() throws Exception {
    File o = new File(outputDir);
    if (o.exists()) {
      org.apache.uima.util.FileUtils.deleteRecursive(o);
    }//if
  }//cleanup method
}//EchoTest class
