//var elements = document.querySelectorAll("[data-json]");
//
//setStyleByCountColumn(elements);

console.log("info");

function setStyleByCountColumn(element){

    let countColumns = parseDialogValue(element);
    
    let cssStyleColumn = '';

    for (let i = 0 ; i < countColumns ; i++){
        cssStyleColumn += " 1fr ";
    }
    console.log(countColumns);



    $('.wrapper').css('grid-template-columns' , cssStyleColumn);
}


function parseDialogValue(element){

    for (var i = 0; i < element.length; i++) {
        var obj = JSON.parse(element[i].dataset.json);
        console.log(obj);
        return (obj.countColumns) ? obj.countColumns : 1 ; 
    } 
}




