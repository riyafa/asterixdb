/*
 * Copyright 2009-2013 by The Regents of the University of California
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License from
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.uci.ics.asterix.common.config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.uci.ics.asterix.common.api.AsterixAppContextInfo;
import edu.uci.ics.asterix.event.schema.cluster.Cluster;
import edu.uci.ics.asterix.event.schema.cluster.Env;
import edu.uci.ics.asterix.event.schema.cluster.Node;
import edu.uci.ics.asterix.event.schema.cluster.Property;

/**
 * A holder class for properties related to the Asterix cluster.
 */
public class AsterixClusterProperties {

    private static final Logger LOGGER = Logger.getLogger(AsterixClusterProperties.class.getName());

    private static final String IO_DEVICES = "iodevices";

    public static final AsterixClusterProperties INSTANCE = new AsterixClusterProperties();

    private Map<String, Map<String, String>> ncConfiguration = new HashMap<String, Map<String, String>>();

    private static final String CLUSTER_CONFIGURATION_XML = "cluster.xml";

    private Cluster cluster;

    private AsterixClusterProperties() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(CLUSTER_CONFIGURATION_XML);
        try {
            JAXBContext ctx = JAXBContext.newInstance(Cluster.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            cluster = (Cluster) unmarshaller.unmarshal(is);
            populateClusterProperties(cluster);

        } catch (JAXBException e) {
            LOGGER.warning("Failed to read cluster configuration file " + CLUSTER_CONFIGURATION_XML);
        }
    }

    public enum State {
        ACTIVE,
        UNUSABLE
    }

    private State state = State.UNUSABLE;

    public void removeNCConfiguration(String nodeId) {
        state = State.UNUSABLE;
        ncConfiguration.remove(nodeId);
    }

    public void addNCConfiguration(String nodeId, Map<String, String> configuration) {
        ncConfiguration.put(nodeId, configuration);
        if (ncConfiguration.keySet().size() == AsterixAppContextInfo.getInstance().getMetadataProperties()
                .getNodeNames().size()) {
            state = State.ACTIVE;
        }
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(" Registering configuration parameters for node id" + nodeId);
        }
    }

    /**
     * Returns the number of IO devices configured for a Node Controller
     * 
     * @param nodeId
     *            unique identifier of the Node Controller
     * @return number of IO devices. -1 if the node id is not valid. A node id is not valid
     *         if it does not correspond to the set of registered Node Controllers.
     */
    public int getNumberOfIODevices(String nodeId) {
        Map<String, String> ncConfig = ncConfiguration.get(nodeId);
        if (ncConfig == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Configuration parameters for nodeId" + nodeId
                        + " not found. The node has not joined yet or has left.");
            }
            return -1;
        }
        return ncConfig.get(IO_DEVICES).split(",").length;
    }

    /**
     * @return
     */
    public synchronized Node getAvailableSubstitutionNode() {
        Node substitutionNode = null;
        List<Node> availableNodesForSubstitution = cluster.getSubstituteNodes().getNode();
        if (!availableNodesForSubstitution.isEmpty()) {
            substitutionNode = availableNodesForSubstitution.remove(0);
        }
        return substitutionNode;
    }

    public synchronized int getNumberOfAvailableSubstitutionNodes() {
        return cluster.getSubstituteNodes().getNode() == null ? 0 : cluster.getSubstituteNodes().getNode().size();
    }

    public static void populateClusterProperties(Cluster cluster) {
        List<Property> clusterProperties = null;
        if (cluster.getEnv() != null && cluster.getEnv().getProperty() != null) {
            clusterProperties = cluster.getEnv().getProperty();
            clusterProperties.clear();
        } else {
            clusterProperties = new ArrayList<Property>();
        }

        System.out.println("ASTERIX APP CTX INFO:" + AsterixAppContextInfo.getInstance());
        System.out.println("EXT PROPERTIES " + AsterixAppContextInfo.getInstance().getExternalProperties());
        System.out.println("NC JAVA PARAMS "
                + AsterixAppContextInfo.getInstance().getExternalProperties().getNCJavaParams());
        clusterProperties.add(new Property("nc.java.opts", AsterixAppContextInfo.getInstance().getExternalProperties()
                .getNCJavaParams()));
        clusterProperties.add(new Property("ASTERIX_HOME", cluster.getWorkingDir().getDir() + File.separator
                + "asterix"));
        clusterProperties.add(new Property("CLUSTER_NET_IP", cluster.getMasterNode().getClusterIp()));
        clusterProperties.add(new Property("CLIENT_NET_IP", cluster.getMasterNode().getClientIp()));
        clusterProperties.add(new Property("LOG_DIR", cluster.getLogDir()));
        clusterProperties.add(new Property("JAVA_HOME", cluster.getJavaHome()));
        clusterProperties.add(new Property("WORKING_DIR", cluster.getWorkingDir().getDir()));
        cluster.setEnv(new Env(clusterProperties));
    }

    public State getState() {
        return state;
    }

    public Cluster getCluster() {
        return cluster;
    }

}