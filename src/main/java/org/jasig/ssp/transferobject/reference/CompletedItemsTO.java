/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.transferobject.reference;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.reference.CompletedItems;
import org.jasig.ssp.transferobject.TransferObject;

import com.google.common.collect.Lists;

public class CompletedItemsTO extends AbstractReferenceTO<CompletedItems>
		implements TransferObject<CompletedItems> {

	
	public CompletedItemsTO() {
		super();
	}

	public CompletedItemsTO(final UUID id, final String name,
			final String description) {
		super(id, name, description);
	}

	public CompletedItemsTO(final CompletedItems model) {
		super();
		from(model);
	}

	@Override
	public void from(CompletedItems model) {
		super.from(model);
	};
	
	public static List<CompletedItemsTO> toTOList(
			final Collection<CompletedItems> models) {
		final List<CompletedItemsTO> tObjects = Lists.newArrayList();
		for (CompletedItems model : models) {
			tObjects.add(new CompletedItemsTO(model));
		}
		return tObjects;
	}

}
