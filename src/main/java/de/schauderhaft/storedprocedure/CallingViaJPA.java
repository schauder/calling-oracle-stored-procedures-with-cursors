/*
 * Copyright 2017 the original author or authors.
 *
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
 */
package de.schauderhaft.storedprocedure;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Component;

/**
 * @author Jens Schauder
 */
@Component
class CallingViaJPA {

	private final EntityManager em;

	CallingViaJPA(EntityManager em) {
		this.em = em;
	}

	void execute() {
		//callString();
		callOne();
	}

	private void callOne() {

		StoredProcedureQuery callString = em.createNamedStoredProcedureQuery("namedAsCallOne");

		callString.execute();

		// incorrectly produces false
		System.out.println("has more results: " + callString.hasMoreResults());
		// works
//		System.out.println(callString.getResultList());
		// doesn't work
		System.out.println(callString.getOutputParameterValue(1));
	}

	private void callString() {
		
		StoredProcedureQuery callString = em.createNamedStoredProcedureQuery("namedAsCallString");
		callString.setParameter(1, "twentythree");

		callString.execute();

		System.out.println("has more results: " + callString.hasMoreResults());
		System.out.println(callString.getOutputParameterValue(2));
	}
}
