"use strict";
use(function () {
    var info = {};
    let counter = 1 ;
    info.countRow = resource.properties["countRow"] ? resource.properties["countRow"] : 0;
    info.countColumns = resource.properties["countColumns"] ? resource.properties["countColumns"] : 0;
   
    if (info.countRow != 0 && info.countColumns != 0){
        info.title = "Grid Parsys Compunent 2 ";
    } else {
        info.title = "No columns or no row. Please set countRow of countColumns in dialog";
    }

    info.array = [];
    for(let i=0 ; i < info.countRow ; i++){
        let rowArray = [];
        for(let j=0 ; j < info.countColumns ; j++){
            rowArray.push("par_" + counter);
            counter++;
        }
        info.array.push(rowArray);
        log.info(rowArray);
    }

    info.style = 'grid-template-columns : 1fr';

    for(let i=1 ; i < info.countColumns ; i++){
        info.style += " 1fr";
    }
//    info.json2 = JSON.stringify(info.style);
    info.json = JSON.stringify(info);

	log.info(info.array);
     return info;
});