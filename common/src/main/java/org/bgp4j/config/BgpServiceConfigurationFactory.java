package org.bgp4j.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.bgp4j.config.properties.BgpConfigurationProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class BgpServiceConfigurationFactory implements FactoryBean<BgpServiceConfiguration>, InitializingBean {

    private final BgpConfigurationProperties bgpConfigurationPrpoperties;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public BgpServiceConfiguration getObject() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return BgpServiceConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
