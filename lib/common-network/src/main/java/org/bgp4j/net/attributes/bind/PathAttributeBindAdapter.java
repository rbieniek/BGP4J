/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;


/**
 * @author rainer
 *
 */
public class PathAttributeBindAdapter extends XmlAdapter<PathAttributeDTO, PathAttribute> {

	@Override
	public PathAttribute unmarshal(PathAttributeDTO dto) throws Exception {
		PathAttribute pa = null;
		
		switch(dto.getType()) {
		case LOCAL_PREF:
			pa = new LocalPrefPathAttribute(dto.getLocalPreference().getValue());
			break;
		}
		
		if(pa != null) {
			pa.setOptional(dto.isOptional());
			pa.setPartial(dto.isPartial());
			pa.setTransitive(dto.isTransitive());
		}
		
		return pa;
	}

	@Override
	public PathAttributeDTO marshal(PathAttribute v) throws Exception {
		PathAttributeDTO dto = new PathAttributeDTO(v);
		
		switch(v.getType()) {
		case LOCAL_PREF:
			dto.setLocalPreference(new LocalPreferenceDTO((LocalPrefPathAttribute)v));
			break;
		}
		
		return dto;
	}

}
