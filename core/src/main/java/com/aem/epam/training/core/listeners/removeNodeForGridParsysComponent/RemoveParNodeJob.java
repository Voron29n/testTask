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

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;


@Component(
        immediate = true,
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + GridParsysListenerForContent.JOB_TOPIC_WITHOUT_CONTENT
        }
)
public class RemoveParNodeJob implements JobConsumer {

    //    private final String PATH_TO_REMOVE_NODES = "/var/log/removedProperties";
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Reference
    private ResourceResolverFactory resolverFactory;

    private Node parNode;
    private Session session;

    @Override
    public JobResult process(Job job) {

        try {
            String pathToParNode = (String) job.getProperty("pathToParNode");

            if (isParNodeHasContent(pathToParNode)) {
                return JobResult.CANCEL;
            }
            return removeParNode(pathToParNode) ? JobResult.OK : JobResult.FAILED;

        } catch (LoginException | RepositoryException e) {
            e.printStackTrace();
            logger.error(e.toString());
            return JobResult.FAILED;
        }
    }

    private boolean removeParNode(String pathToParNode) {
        try {
            parNode.remove();
            session.save();
            session.logout();
            return true;
        } catch (RepositoryException e) {
            e.printStackTrace();
            logger.error("Remove Par node Failed");
            return false;
        }
    }

    private boolean isParNodeHasContent(String pathToParNode) throws LoginException, RepositoryException {

        ResourceResolver resourceResolver = getResourceResolver();
        session = resourceResolver.adaptTo(Session.class);

        parNode = resourceResolver.getResource(pathToParNode).adaptTo(Node.class);
        return parNode.getNodes().hasNext();
    }

    private ResourceResolver getResourceResolver() throws LoginException {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, GridParsysListenerForContent.SERVICE_USER_NAME);

        ResourceResolver resolver = null;
        resolver = resolverFactory.getServiceResourceResolver(param);

        return resolver;
    }

}
