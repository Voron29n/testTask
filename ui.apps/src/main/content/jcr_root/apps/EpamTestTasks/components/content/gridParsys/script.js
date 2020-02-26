"use strict";
use(function () {
    var info = {};

    info.countRow = resource.properties["countRow"];
    info.countColumns = resource.properties["countColumns"];
    info.array = [];

    for(let i=0 ; i < info.countRow ; i++){
        let rowArray = [];
        for(let j=0 ; j < info.countColumns ; j++){
//            rowArray.push({ data-sly-resource : "${'par_" + i + "_" + j + "' @ resourceType='wcm/foundation/components/parsys'}"});
//            rowArray.push("${@path = 'par_" + i + "_" + j + "' @ resourceType='wcm/foundation/components/parsys'}");
            rowArray.push("'par_" + i + "_" + j + "'");
        }
        info.array.push(rowArray);
        log.info(rowArray);
    }

	log.info(info.array.length);

     return info;
});