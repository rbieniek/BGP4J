/**
 * 
 */
package org.bgp4j.rib.processor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.rib.RouteAdded;
import org.bgp4j.rib.RouteWithdrawn;
import org.bgp4j.rib.RoutingEventListener;
import org.bgp4j.rib.RoutingInformationBase;
import org.bgp4j.rib.filter.DefaultPathAttributesInjector;
import org.bgp4j.rib.filter.PrefixRoutingFilter;
import org.bgp4j.rib.filter.RoutingFilter;

/**
 * @author rainer
 *
 */
public class RouteTransportListener implements RoutingEventListener {

	private RoutingInformationBase target;
	private RoutingInformationBase source;
	private @Inject DefaultPathAttributesInjector injector;
	private @Inject Instance<PrefixRoutingFilter> prefixFilterProvider;
	private List<RoutingFilter> filters = new LinkedList<RoutingFilter>();
	
	/* (non-Javadoc)
	 * @see org.bgp4j.rib.RoutingEventListener#routeAdded(org.bgp4j.rib.RouteAdded)
	 */
	@Override
	public void routeAdded(RouteAdded event) {
		boolean match = false;
		
		for(RoutingFilter filter : filters) {
			match |= filter.matchFilter(event.getRoute());
			
			if(match)
				break;
		}
		
		if(!match)
			target.addRoute(injector.injectMissingPathAttribute(event.getRoute()));
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.rib.RoutingEventListener#routeWithdrawn(org.bgp4j.rib.RouteWithdrawn)
	 */
	@Override
	public void routeWithdrawn(RouteWithdrawn event) {
		boolean match = false;
		
		for(RoutingFilter filter : filters) {
			match |= filter.matchFilter(event.getRoute());
			
			if(match)
				break;
		}
		
		if(!match)
			target.withdrawRoute(event.getRoute());
	}

	/**
	 * @param target the target to set
	 */
	void setTarget(RoutingInformationBase target) {
		this.target = target;
	}

	/**
	 * @return the source
	 */
	RoutingInformationBase getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	void setSource(RoutingInformationBase source) {
		this.source = source;
	}

	public void configure(Set<RoutingFilterConfiguration> localRoutingFilters,	PathAttributeConfiguration localDefaultPathAttributes) {
		injector.configure(localDefaultPathAttributes);
		
		for(RoutingFilterConfiguration filterConfig : localRoutingFilters) {
			if(filterConfig instanceof PrefixRoutingFilterConfiguration) {
				PrefixRoutingFilter filter = prefixFilterProvider.get();
				
				filter.configure((PrefixRoutingFilterConfiguration)filterConfig);
				filters.add(filter);
			}
		}
	}


}
