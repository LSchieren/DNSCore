/*
  DA-NRW Software Suite | ContentBroker
  Copyright (C) 2013 Historisch-Kulturwissenschaftliche Informationsverarbeitung
  Universität zu Köln

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.uzk.hki.da.grid;

import de.uzk.hki.da.model.Node;

/**
 * On local developer machines, we won't do distributed conversion. So for acceptance
 * testing we use this implementation.
 * 
 * @author Daniel M. de Oliveira
 */
public class FakeDistributedConversionAdapter implements DistributedConversionAdapter {

	@Override
	public void create(String relativePath) {

	}

	@Override
	public void register(String relativePath, String physicalPath) {
		
	}

	@Override
	public void replicateToLocalNode(String relativePath, Node node) {
		
	}

	@Override
	public void remove(String relativePath) {
		
	}

}
