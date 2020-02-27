package com.aem.epam.training.core.listeners.removeNodeForGridParsysComponent;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + GridParsysListenerForProperty.JOB_TOPIC_CHANGE_PROPERTY
        }
)
public class ClearGridParNodeJob implements JobConsumer {

    //    private final String PATH_TO_REMOVE_NODES = "/var/log/removedProperties";
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Reference
    private ResourceResolverFactory resolverFactory;

    private Node gridParNode;
    private Session session;

    private List<Node> listOfDeleteNode = new ArrayList<>();

    @Override
    public JobResult process(Job job) {

        try {
            String pathToGridParNode = (String) job.getProperty("pathToGridParNode");

            if (isGridParNodeHasCorrectNumbersChildNodes(pathToGridParNode)) {
                return JobResult.CANCEL;
            }
            return clearGridParNode(pathToGridParNode) ? JobResult.OK : JobResult.FAILED;

        } catch (LoginException | RepositoryException e) {
            e.printStackTrace();
            logger.error(e.toString());
            return JobResult.FAILED;
        }
    }

    private boolean clearGridParNode(String pathToParNode) {
        try {
            for (Node node : listOfDeleteNode) {
                node.remove();
            }
            session.save();
            session.logout();
            return true;
        } catch (RepositoryException e) {
            e.printStackTrace();
            logger.error("Clear GridPar node Failed");
            return false;
        }
    }

    private boolean isGridParNodeHasCorrectNumbersChildNodes(String pathToGridParNode) throws LoginException, RepositoryException {

        ResourceResolver resourceResolver = getResourceResolver();
        session = resourceResolver.adaptTo(Session.class);

        gridParNode = resourceResolver.getResource(pathToGridParNode).adaptTo(Node.class);

        if (!gridParNode.getNodes().hasNext()) {
            return true;
        }

        int countRow;
        int countColumns;

        try{
            countRow = Integer.parseInt(String.valueOf(gridParNode.getProperty("countRow").getValue()));
            countColumns = Integer.parseInt(String.valueOf(gridParNode.getProperty("countRow").getValue()));
        } catch (PathNotFoundException e){
            logger.error(String.format("Property for GridParsys Node in %s class not found" , this.getClass().getName() ));
            return true;
        }

        int gridSize = countRow * countColumns;

        NodeIterator nodeIterator = gridParNode.getNodes();

        while (nodeIterator.hasNext()){
            Node nodeItem = nodeIterator.nextNode();

            if (getParCounter(nodeItem.getPath()) > gridSize){
                listOfDeleteNode.add(nodeItem);
            }
        }

        return listOfDeleteNode.isEmpty();
    }

    private int getParCounter(String childNodePath) {
        String pathPar = childNodePath.substring(childNodePath.indexOf("par_"));
        if (pathPar.contains("/")) {
            pathPar = pathPar.substring(0, pathPar.indexOf("/"));
        }

        return Integer.parseInt(pathPar.substring(pathPar.indexOf("_") + 1));
    }

    private ResourceResolver getResourceResolver() throws LoginException {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, GridParsysListenerForContent.SERVICE_USER_NAME);

        ResourceResolver resolver = null;
        resolver = resolverFactory.getServiceResourceResolver(param);

        return resolver;
    }

}
