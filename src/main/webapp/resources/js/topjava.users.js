// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
            updateTable: function () {
                $.get("ajax/admin/users", updateTableByData);
            }
        }
    );
});

function enableOrDisableUser(id) {
    if ($("#checkbox").prop("checked")) {
        $.ajax({
            url: context.ajaxUrl + "enable/",
            type: "POST",
            data: "id=" + id
        }).done(function () {
            context.updateTable();
            successNoty("Enabled");
        })
    } else {
        $.ajax({
            url: context.ajaxUrl + "disable/",
            type: "POST",
            data: "id=" + id
        }).done(function () {
            context.updateTable();
            successNoty("Disabled");
        })
    }

}