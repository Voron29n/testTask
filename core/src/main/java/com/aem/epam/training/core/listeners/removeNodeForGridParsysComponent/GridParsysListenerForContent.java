package com.aem.epam.training.core.listeners.removeNodeForGridParsysComponent;


import org.apache.jackrabbit.oak.commons.PathUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;


@Component(
        service = EventListener.class,
        immediate = true
)
public class GridParsysListenerForContent implements EventListener {

    protected static final String SERVICE_USER_NAME = "testuser";
    protected static final String JOB_TOPIC_WITHOUT_CONTENT = "remove/parsys/node/without/content";
    protected static final String JOB_TOPIC_CHANGE_PROPERTY = "remove/parsys/node/exceeding/property";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Session session = null;

    @Reference
    private SlingRepository repository;
    @Reference
    private JobManager jobManager;


    @Activate
    @Modified
    public void activate(ComponentContext context) {

        log.debug("activating ExampleObservation");
        try {
            session = repository.loginService(SERVICE_USER_NAME, null);
            Workspace workspace = session.getWorkspace();
            session.getWorkspace().getObservationManager().addEventListener(
                    this, //handler
                    Event.NODE_REMOVED | Event.NODE_MOVED,  //binary combination of event types
                    "/content/EpamTestTasks/../..", //path
                    true, //is Deep?
                    null, //uuids filter
                    null, //nodetypes filter
                    false);
            log.debug("Custom listener is activate");
        } catch (RepositoryException | NullPointerException e) {
            log.error("unable to register session", e);
            e.printStackTrace();
        }
    }


    @Deactivate
    public void deactivate() {
        if (session != null) {
            session.logout();
        }
    }

    public void onEvent(EventIterator eventIterator) {

        ResourceResolver resourceResolver = null;
        try {
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent();
                String eventPath = event.getPath();

                if (checkEventPath(eventPath)) {
                    String pathToParNode = PathUtils.getAncestorPath(eventPath, 1);
                    final Map<String, Object> props = new HashMap<String, Object>();
                    props.put("pathToParNode", pathToParNode);

                    jobManager.addJob(JOB_TOPIC_WITHOUT_CONTENT, props);
                } else {
                    return;
                }


                log.info("something has been remove : {}", eventPath);
            }
        } catch (RepositoryException | NullPointerException e) {
            log.error("Error while treating events", e);
        }
    }

    private boolean checkEventPath(String eventPath) {
        String pathToParNode = PathUtils.getAncestorPath(eventPath, 1);
        return PathUtils.getName(pathToParNode).contains("par_");
    }
}
