function upload(fileId, path, isPublic) {

    var fd = new FormData();
    var files = $('#' + fileId)[0].files;

    if (files.length > 0) {
        $("#main").fadeTo('fast', 0.25);
        $("#loading").show();
        $("#progressBar").show();

        fd.append('file', files[0]);

        $.ajax({
            xhr: function() {
                var xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function(evt) {
                    if (evt.lengthComputable) {
                        var percentComplete = (evt.loaded / evt.total) * 100;
                        // Place upload progress bar visibility code here
                        var progress = Math.floor(percentComplete);
                        $("#progressBar").attr("value", progress);
                    }
                }, false);
                return xhr;
            },
            type: 'POST',
            url: "/" + path + "?name=" + $.cookie('name') + "&token=" + $.cookie('token'),
            data: fd,
            contentType: false,
            processData: false,
            success: function (response) {
                $("#loading").hide();
                $("#progressBar").hide();
                $("#progressBar").attr("value", 0);
                $("#main").fadeTo('fast', 1);
                if (response != 0) {
                    alert('file uploaded');
                } else {
                    alert('file not uploaded');
                }
                location.reload();
            }
        });
    } else {
        alert("Please select a file.");
    }
}

function download(fileName, isPublic) {

    //Set the File URL.
//    var url = "/download";
//
//    $.ajax({
//        type: 'GET',
//        url: url,
//        data: { param: fileName, isPublic: isPublic },
//        cache: false,
//        xhr: function () {
//            var xhr = new XMLHttpRequest();
//            xhr.onreadystatechange = function () {
//                if (xhr.readyState == 2) {
//                    if (xhr.status == 200) {
//                        xhr.responseType = "blob";
//                    } else {
//                        xhr.responseType = "text";
//                    }
//                }
//            };
//            return xhr;
//        },
//        success: function (data) {
//            //Convert the Byte Data to BLOB object.
//            var blob = new Blob([data], { type: "application/octetstream" });
//
//            //Check the Browser type and download the File.
//            var isIE = false || !!document.documentMode;
//            if (isIE) {
//                window.navigator.msSaveBlob(blob, fileName);
//            } else {
//                var url = window.URL || window.webkitURL;
//                link = url.createObjectURL(blob);
//                var a = $("<a />");
//                a.attr("download", fileName);
//                a.attr("href", link);
//                $("body").append(a);
//                a[0].click();
//                $("body").remove(a);
//            }
//        }
//    });
//    var postData = {
//        param:fileName,
//        isPublic:isPublic
//    };
    var fakeFormHtmlFragment = "<form style='display: none;' method='GET' action='"+"/download"+"'>";

//    var escapedKey = postData.param.replace("\\", "\\\\").replace("'", "\'");
//    var escapedValue = postValue.replace("\\", "\\\\").replace("'", "\'");
    fakeFormHtmlFragment += "<input type='hidden' name='"+"param"+"' value='"+fileName+"'>";
    fakeFormHtmlFragment += "<input type='hidden' name='"+"isPublic"+"' value='"+isPublic+"'>";

    fakeFormHtmlFragment += "</form>";
    $fakeFormDom = $(fakeFormHtmlFragment);
    $("body").append($fakeFormDom);
    $fakeFormDom.submit();

}

function downloadToServer(isPublic) {
    var targetName = $("#targetName").prop("value");
    var url = $("#downloadUrl").prop("value");
    $.get("/downloadProxy", { url: url, targetName: targetName, isPublic: isPublic, name: $.cookie('name') }, function (data, status) {
        if (status == "success") {
            console.log(data);
            alert("File Downloaded");
        } else {
            alert("File Not Downloaded");
        }
        location.reload();
    });
}

function deleteFile(isPublic, customFilename) {
    var deleteName = $("#deleteName").prop("value");
    if (customFilename){
        deleteName = customFilename;
    }

    $.post("/delete", { name: deleteName, isPublic: isPublic }, function (data, status) {
        if (status == "success") {
            console.log(data);
            alert("File Deleted");
        } else {
            alert("File Not Deleted");
        }
        location.reload();
    });
}

function makePublic() {
    var privateFileName = $("#privateFileName").prop("value");
    $.post("/makePublic", { privateFileName: privateFileName }, function (data, status) {
        if (status == "success") {
            console.log(data);
            alert("File Become Public");
            location.reload();
        } else {
            alert("Some Error");
            location.reload();
        }

    });
}

function moveToOnedrive(isPublic, fileName){
    $.post("/moveToOnedrive", { fileName: fileName, isPublic: isPublic, name: $.cookie('name') }, function (data, status) {
            if (status == "success") {
                console.log(data);
                alert("Move to Onedrive");
            } else {
                alert("Fail");
            }
            location.reload();
        });
}