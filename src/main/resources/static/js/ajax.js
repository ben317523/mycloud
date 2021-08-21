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