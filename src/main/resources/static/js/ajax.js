function upload(fileId, path, isPublic) {
    $("#main").fadeTo('fast',0.25);
    $("#loading").show();
    var fd = new FormData();
    var files = $('#'+fileId)[0].files;

    if (files.length > 0) {
        fd.append('file', files[0]);


        $.ajax({
            type: 'POST',
            url: "/"+path+"?name=" + $.cookie('name') + "&token=" + $.cookie('token'),
            data: fd,
            contentType: false,
            processData: false,
            success: function (response) {
                $("#loading").hide();
                $("#main").fadeTo('fast',1);
                if (response != 0) {
                    alert('file uploaded');
                } else {
                    alert('file not uploaded');
                }
            },
        });
    } else {
        alert("Please select a file.");
    }
}

function download(fileName,isPublic) {
    //var fileName = $("#fileName").prop("value");
    $.get("/download",{param : fileName,isPublic : isPublic});
}

function downloadToServer(isPublic){
    var targetName = $("#targetName").prop("value");
    var url = $("#downloadUrl").prop("value");
    $.get("/downloadProxy",{url : url,targetName : targetName,isPublic : isPublic,name : $.cookie('name')},function(data,status){
        if (status == "success"){
            console.log(data);
            alert("File Downloaded");
        }else {
            alert("File Not Downloaded");
        }
    });
}

function deleteFile(isPublic) {
    var deleteName = $("#deleteName").prop("value");
    $.post("/delete",{name : deleteName,isPublic : isPublic},function(data,status){
        if (status == "success"){
            console.log(data);
            alert("File Deleted");
        }else {
            alert("File Not Deleted");
        }
    });
}

async function makePublic() {
    var privateFileName = $("#privateFileName").prop("value");
    $.post("/makePublic",{privateFileName : privateFileName},function(data,status){
        if (status == "success"){
            console.log(data);
            alert("File Become Public");
        }else {
            alert("Some Error");
        }
    });
}