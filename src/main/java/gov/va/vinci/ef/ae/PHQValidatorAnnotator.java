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

/**
 * Validates PHQ numeric measurement range.
 *
 * Created by thomasginter on 9/1/15.
 */
public class PHQValidatorAnnotator extends NumericValidatorAnnotator {
	/**
	 * Check for a value between either 0 and 27.
	 *
	 * @param value
	 *            Double value to be validated
	 * @return true if the value is in the ranges
	 */
	@Override
	protected boolean isValid(double value) {
		if (value >= 0 && value <= 27)
			return true;
		return false;
	}

	protected boolean checkValid(String str) {
		Boolean valid = false;
		String firstString = null;
		String secondString = null;
		String[] parts = str.split("/");
		firstString = parts[0].trim();
		secondString = parts[1].trim();
		if (firstString != null) {
			int firstValue = Integer.parseInt(firstString);
			if (secondString != null) {
				int secondValue = Integer.parseInt(secondString);
				if ((firstValue >= 0 && firstValue <= 27) && (secondValue == 27)) {
					valid = true;
				}
			} else {
				if (firstValue >= 0 && firstValue <= 27) {
					valid = true;
				}
			}
		}
		return valid;
	}

}
