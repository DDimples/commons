<@override name="include">

</@override>

<@override name="css">
<style type="text/css">
    body {
        padding-top: 70px;
    }

    .table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
        background-color: #87CEFA;
    }
</style>
</@override>

<@override name="main">
<div class="container bs-docs-container">

    <div class="row">
        <div class="col-md-9" role="main">
            <table id="example" class="table table-striped table-hover table-bordered col-md-9" cellspacing="0"
                   width="100%">
                <thead>
                <tr>
                    <th>s</th>
                    <th>schedName</th>
                    <th>triggerName</th>
                    <th>triggerGroup</th>
                    <th>jobName</th>
                    <th>startTime</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>s</th>
                    <th>schedName</th>
                    <th>triggerName</th>
                    <th>triggerGroup</th>
                    <th>jobName</th>
                    <th>startTime</th>
                </tr>
                </tfoot>
            </table>

        </div>
        <div class="col-md-3">
            <button id="btn" class="btn btn-success" onclick="btntest()">Success</button>
        </div>

    </div>
</div>
</@override>

<@override name="js">
<script src="/resources/static/common/js/webTable.js"></script>
<script type="application/javascript">

    $(document).ready(function () {

        editor = new $.fn.dataTable.Editor({
            ajax: {
                "url": "/quartz/updateTableData",
                "type": "POST"
            },
            idSrc: 'id',
            table: "#example",
            i18n: {
                create: {
                    button: "新增",
                    title: "新增",
                    submit: "提交"
                },
                edit: {
                    button: "修改",
                    title: "修改",
                    submit: "提交"
                },
                remove: {
                    button: "删除",
                    title: "删除",
                    submit: "确认",
                    confirm: {
                        _: "确认要删除 %d 行?",
                        1: "您确认要删除 1 行?"
                    }
                },
                error: {
                    system: "Une erreur s’est produite, contacter l’administrateur système"
                },
                multi: {
                    title: "确认",
                    info: "multi????",
                    restore: "Annuler les modifications"
                },
                datetime: {
                    previous: 'pre',
                    next: 'next',
                    months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                    weekdays: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
                }
            },
            fields: [
                {
                    label: "策略名称:",
                    name: "schedName"
                },
                {
                    label: "trigger名称:",
                    name: "triggerName"
                }, {
                    label: "triggerGroup:",
                    name: "triggerGroup"
                }, {
                    label: "任务名称:",
                    name: "jobName"
                }, {
                    label: "启动时间:",
                    name: "startTime",
                    type: 'datetime'
                }
            ]

        });


        var table = WebTable.initTable({
            tableId: "example",
            processing: true,
            searching: false,
            serverSide: true,
            rowId: "id",
            url: "/quartz/getTableData",
            order: [[3, 'asc'], [4, 'desc']],
            columnDefs: [
                {"orderable": false, "targets": 0}
            ],
            columns: [
                {
                    data: null,
                    defaultContent: '',
                    className: 'select-checkbox',
                    orderable: false
                },
                {"data": "schedName"},
                {"data": "triggerName"},
                {"data": "triggerGroup", "orderable": false},
                {"data": "jobName"},
                {"data": "startTime"}
            ]
        });
        // Display the buttons
        new $.fn.dataTable.Buttons(table, [
            {extend: "create", editor: editor},
            {extend: "edit", editor: editor},
            {extend: "remove", editor: editor}
        ]);

        table.buttons().container()
                .appendTo($('.col-sm-6:eq(0)', table.table().container()));
    });

    function btntest(){
        var temp = $("#example").DataTable();
        temp.ajax.url("/quartz/getTableData").load();
        window.console.log("~~~");
    }
</script>
</@override>

<@extends name="/common/_tableLayout.ftl"/>