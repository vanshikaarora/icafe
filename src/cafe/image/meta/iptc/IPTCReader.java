/**
 * Copyright (c) 2014 by Wen Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Any modifications to this file must keep this entire header intact.
 *
 * Change History - most recent changes go on top of previous changes
 *
 * IPTCReader.java
 *
 * Who   Date       Description
 * ====  =======    ============================================================
 * WY    12Jan2015  Initial creation to read IPTC information
 */

package cafe.image.meta.iptc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cafe.io.IOUtils;
import cafe.util.Reader;

/**
 * IPTC image metadata reader
 *  
 * @author Wen Yu, yuwen_66@yahoo.com
 * @version 1.0 01/12/2015
 */
public class IPTCReader implements Reader {
	private List<IPTCDataSet> datasetList = new ArrayList<IPTCDataSet>();
	private byte[] data;
	
	public IPTCReader(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void read() throws IOException {
		int i = 0;
		int tagMarker = data[i];
		
		while (tagMarker == 0x1c) {
			i++;
			int recordNumber = data[i++];
			int tag = data[i++];
			int recordSize = IOUtils.readUnsignedShortMM(data, i);
			i += 2;
			datasetList.add(new IPTCDataSet(recordNumber, tag, recordSize, data, i));
			i += recordSize;
			// Sanity check
			if(i >= data.length) break;	
			tagMarker = data[i];							
		}
				
		for(IPTCDataSet iptc : datasetList) {
			iptc.print();
		}
	}
	
	public List<IPTCDataSet> getDataSet() {
		return Collections.unmodifiableList(datasetList);
	}
}