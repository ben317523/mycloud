function upload(fileId, path) {
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
    $.post("/delete",{name : deleteName,,isPublic : false},function(data,status){
        if (status == "success"){
            console.log(data);
            alert("File Deleted");
        }else {
            alert("File Not Deleted");
        }
    });
}