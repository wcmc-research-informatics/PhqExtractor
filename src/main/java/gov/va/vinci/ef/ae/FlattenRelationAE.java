package gov.va.vinci.ef.ae;

/*
 * #%L
 * Echo concept exctractor
 * %%
 * Copyright (C) 2010 - 2016 Department of Veterans Affairs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import gov.va.vinci.ef.types.*;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.impl.TypeDescription_impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Flatten relationships for easier output.
 * <p>
 * Created by vhaslcpatteo on 9/16/2015. Edited by Thomas Ginter on 09/18/2015.
 * Added the setValueStrings method.
 */
public class FlattenRelationAE extends LeoBaseAnnotator {
	protected Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
	protected Pattern qsPattern1 = Pattern.compile("(?<=score)(\\s*|=|:)\\d{1,2}(\\/\\d{1,2})?", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	protected Pattern qsPattern2 = Pattern.compile("(?<=score\\s{1,3}\\(range:?\\s\\d\\s-\\s\\d{1,2}\\))\\s*\\d{1,2}(\\/\\d{1,2})?", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	protected Pattern qsPattern3 = Pattern.compile("(?<=score\\s=?\\s)\\d{1,2}(\\/\\d{1,2})?\\s", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	protected Pattern qsPattern4 = Pattern.compile("(?<=scored?\\s{1,3}on\\s{1,3}\\d{1,2}(\\/|-|:)\\d{1,2}(\\/|-|:)\\d{1,4}\\s{1,3}(is|was|at|of)\\s{1,3})\\d{1,2}(\\/\\d{1,2})?\\s", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	// on 9/21/2015  - Score = 19
	
	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
		
//		Collection<NumericValue> values = AnnotationLibrarian.getAllAnnotationsOfType(aJCas, NumericValue.type, false);
//		for (gov.va.vinci.ef.types.NumericValue value : values) {
//	    	System.out.println("Value: " + value.getCoveredText());
//	    }
//		
//		Collection<Measurement> measures = AnnotationLibrarian.getAllAnnotationsOfType(aJCas, Measurement.type, false);
//		for (gov.va.vinci.ef.types.Measurement measure : measures) {
//	    	System.out.println("Measurement: " + measure.getCoveredText());
//	    }
		
		Collection<PhqRelation> relations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas, PhqRelation.type, false);	
		if (relations.size() > 0) {
			for (PhqRelation relation : relations) {
				// Create the output annotation
				Relation out = new Relation(aJCas, relation.getBegin(), relation.getEnd());
				out.addToIndexes();
				// Set the string value features
				setValueStrings(relation, out);
			}
		} 
		
		// Check for special cases of phq-9 with questions and scores. 
		Collection<gov.va.vinci.ef.types.QuestionScore> qscores = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
				gov.va.vinci.ef.types.QuestionScore.type, false);
		
//			for (gov.va.vinci.ef.types.QuestionScore term : qscores) {
//				System.out.println("Text: " + term.getCoveredText());
//			}

		if (qscores.size() > 0) {
			for (QuestionScore qs : qscores) {			
				//Pattern 1
				setCustomRelation(aJCas, qsPattern1,  qs);
				
				// Pattern 2
				setCustomRelation(aJCas, qsPattern2,  qs);
				
				// Pattern 3
				setCustomRelation(aJCas, qsPattern3,  qs);
				
				// Pattern 4
				setCustomRelation(aJCas, qsPattern4,  qs);			
			}
		}
		
	}

	protected void setCustomRelation(JCas aJCas, Pattern p, QuestionScore qs) {
		String covered = qs.getCoveredText();
		boolean foundValid = false;
		boolean hasString = false;
		
		Matcher m = p.matcher(covered);
		while (m.find()) {
			String score = covered.substring(m.start(), m.end());
			String val1 = "";
			String val2 = "";
			int start = 0;
			int end = 0;			
			// System.out.println("Score: " + score);
			
			if (!GenericValidator.isBlankOrNull(score)) {
				hasString = true;
				foundValid = true;
				String[] parts = score.trim().split("\\/");
				val1 = parts[0]; 
				if(parts.length > 1){
					val2 = parts[1]; 
				}
			}
			if ((hasString == true) && (foundValid == true)) {
				// Create annotation
				start = qs.getBegin() + m.start();
				end = qs.getBegin() + m.end();				
				Relation out = new Relation(aJCas, start, end);
				out.setValue(val1);
				out.setValue2(val2);
				out.setTerm("PHQ-9");
				out.setValueString(score);				
				out.addToIndexes();
			}
		}
	}
	

	protected void setValueStrings(PhqRelation in, Relation out) {

		// Get the NumericValue annotation from the merged set
		Annotation value = null;
		Annotation measure = null;
		FSArray merged = in.getLinked();
		for (int i = 0; i < merged.size(); i++) {
			Annotation a = (Annotation) merged.get(i);
			String typeName = a.getType().getName();
			if (typeName.equals(Measurement.class.getCanonicalName())) {
				measure = a;
				if (measure != null) {
					out.setTerm(measure.getCoveredText());
				}
			}

			else if (typeName.equals(NumericValue.class.getCanonicalName())) {
				value = a;
			}
		}
		// Exit if no value found
		if (value == null)
			return;
		// Get the values
		String valueText = value.getCoveredText();
		Matcher m = numberPattern.matcher(valueText);
		ArrayList<Double> values = new ArrayList<Double>(2);
		while (m.find())
			values.add(new Double(valueText.substring(m.start(), m.end())));
		Collections.sort(values);

		// Set the values
		if (values.size() > 0)
			out.setValue(values.get(0).toString());
		if (values.size() > 1)
			out.setValue2(values.get(values.size() - 1).toString());
		if (StringUtils.isNotBlank(valueText))
			out.setValueString(valueText);

	}

	@Override
	public LeoTypeSystemDescription getLeoTypeSystemDescription() {
		TypeDescription relationFtsd;
		String relationParent = "gov.va.vinci.ef.types.Relation";
		relationFtsd = new TypeDescription_impl(relationParent, "", "uima.tcas.Annotation");
		relationFtsd.addFeature("Term", "", "uima.cas.String"); // Extracted
																// term string
		relationFtsd.addFeature("Value", "", "uima.cas.String"); // Numeric
																	// value
		relationFtsd.addFeature("Value2", "", "uima.cas.String"); // Numeric
																	// value
		relationFtsd.addFeature("ValueString", "", "uima.cas.String"); // String
																		// value
																		// with
																		// units
																		// and
																		// extra
																		// modifiers

		LeoTypeSystemDescription types = new LeoTypeSystemDescription();
		try {
			types.addType(relationFtsd); // for target concepts with single
											// mapping

		} catch (Exception e) {
			e.printStackTrace();
		}
		return types;
	}

}
