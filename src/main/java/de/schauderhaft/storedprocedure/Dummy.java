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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

/**
 * @author Jens Schauder
 */

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(
				name = "namedAsCallOne",
				procedureName = "callOne",
				resultClasses = String.class,
				parameters = {
						@StoredProcedureParameter(mode = ParameterMode.OUT, type = void.class)
				}
		),
		@NamedStoredProcedureQuery(
				name = "namedAsCallString",
				procedureName = "callString",
				parameters = {
						@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class)
				}
		)})
@Entity
public class Dummy {

	@Id Long id;
}
