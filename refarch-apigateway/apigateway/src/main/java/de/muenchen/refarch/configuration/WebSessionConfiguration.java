/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;

/**
 * This class configures Hazelcast as the ReactiveSessionRepository.
 *
 */
@Configuration
@EnableSpringWebSession
public class WebSessionConfiguration {

    @Value("${hazelcast.instance:hazl_instance}")
    public String hazelcastInstanceName;

    @Value("${hazelcast.group-name:session_replication_group}")
    public String groupConfigName;

    @Value("${app.spring-session-hazelcast.namespace:my_namespace}")
    public String openshiftNamespace;

    @Value("${hazelcast.openshift-service-name:apigateway}")
    public String openshiftServiceName;

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    public ReactiveSessionRepository<MapSession> reactiveSessionRepository(@Qualifier("hazelcastInstance") @Autowired HazelcastInstance hazelcastInstance) {
        final IMap<String, Session> map = hazelcastInstance.getMap(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME);
        return new ReactiveMapSessionRepository(map);
    }

    @Bean
    @Profile({ "local", "test" })
    public Config localConfig(@Value(
        "${spring.session.timeout}"
    ) int timeout) {
        final var hazelcastConfig = new Config();
        hazelcastConfig.setInstanceName(hazelcastInstanceName);
        hazelcastConfig.setClusterName(groupConfigName);

        addSessionTimeoutToHazelcastConfig(hazelcastConfig, timeout);

        final var networkConfig = hazelcastConfig.getNetworkConfig();

        final var joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig()
                .setEnabled(true)
                .addMember("127.0.0.1");

        return hazelcastConfig;
    }

    @Bean
    @Profile({ "dev", "kon", "prod" })
    public Config config(@Value("${spring.session.timeout}") int timeout) {
        final var hazelcastConfig = new Config();
        hazelcastConfig.setInstanceName(hazelcastInstanceName);
        hazelcastConfig.setClusterName(groupConfigName);

        addSessionTimeoutToHazelcastConfig(hazelcastConfig, timeout);

        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        hazelcastConfig.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                // explicitely configure namespace because default env lookup is not alway correct
                .setProperty("namespace", openshiftNamespace)
                //If we dont set a specific name, it would call -all- services within a namespace
                .setProperty("service-name", openshiftServiceName);

        return hazelcastConfig;
    }

    /**
     * Adds the session timeout in seconds to the hazelcast configuration.
     *
     * Since we are creating the map it's important to evict sessions
     * by setting a reasonable value for time to live.
     *
     * @param hazelcastConfig to add the timeout.
     * @param sessionTimeout for security session.
     */
    private void addSessionTimeoutToHazelcastConfig(final Config hazelcastConfig, final int sessionTimeout) {
        final var sessionConfig = new MapConfig();
        sessionConfig.setName(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME);
        sessionConfig.setTimeToLiveSeconds(sessionTimeout);
        sessionConfig.getEvictionConfig().setEvictionPolicy(EvictionPolicy.LRU);

        hazelcastConfig.addMapConfig(sessionConfig);
    }

}
