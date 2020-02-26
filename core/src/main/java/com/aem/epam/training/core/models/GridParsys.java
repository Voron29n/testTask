package com.aem.epam.training.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class)
public class GridParsys {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject @Named("countRow") @Default(intValues = 1)
    private int countRow;

    @Inject @Named("countColumns") @Default(intValues = 1)
    private int countColumns;

    @PostConstruct
    public void init(){
        logger.info(String.format("post construct of GridParsys model | row = %d | columns = %d ", countRow, countColumns));
    }

    public int getCountRow() {
        return countRow;
    }

    public int getCountColumns() {
        return countColumns;
    }
}
