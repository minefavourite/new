<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>叩丁狼骡窝窝后台管理系统</title>
    <#include "../common/header.ftl" >
    <#--<link rel="stylesheet" href="/js/plugins/treeview/bootstrap-treeview.min.css" type="text/css" />
    <script type="text/javascript" src="/js/plugins/treeview/bootstrap-treeview.min.js"></script>-->
    <script src="/js/system/commonAll.js"></script>
    <script>
        $(function () {
           $('.changeStateBtn').click(function () {
               var id = $(this).data('id');
               var state = $(this).data('state');
               $.post("/travel/updateState",{id:id,state:state},function (data) {
                   window.location.reload();
               })
           })

            //查看文章
            $('.lookBtn').click(function () {
                var id = $(this).data('id');
                $.post("/travel/searchArticle",{id:id},function (data) {
                    console.log(data);
                    $('#showModal').html(data.object.content);
                })
                $('#contentModal').modal('show');
            })
        })
    </script>
    <style>
        .modal-body img{
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container " style="margin-top: 20px">
    <#include "../common/top.ftl">
    <div class="row">
        <div class="col-sm-3">
            <#assign currentMenu="travel"/>
            <#include "../common/menu.ftl">
        </div>
        <div class="col-sm-9">
            <div class="row">
                <div class="col-sm-12">
                    <h1 class="page-head-line">游记信息管理</h1>
                </div>
            </div>

            <form class="form-inline" id="searchForm" action="/travel/list" method="post">
                <input type="hidden" name="currentPage" id="currentPage" value="1">
                <div class="form-group">
                    <select id="auditState" class="form-control" autocomplete="off" name="state" >
                        <option value="1">待审核</option>
                        <option value="3">已拒绝</option>
                    </select>
                    <script>
                        $("#auditState").val(${(qo.state)!});
                    </script>
                    <button id="query" type="submit" class="btn btn-success"><i class="icon-search"></i> 查询</button>
                </div>
            </form>
           <br/>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-hover" id="travelTb">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>标题</th>
                            <th>封面</th>
                            <th>地点</th>
                            <th>作者</th>
                            <th>状态</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list (pageInfo.list)! as result>
                                <tr>
                                    <td>${result_index+1}</td>
                                    <td>${result.title}</td>
                                    <td><img src="${result.coverUrl}" width="60px"></td>
                                    <td>${(result.dest.name)!}</td>
                                    <td>${(result.author.nickname)!}</td>
                                    <td>${result.stateDisplay}</td>
                                    <td>
                                        <a href="javascript:;" data-state="2"  data-id="${result.id}" class="changeStateBtn">发布</a>
                                    </td>
                                    <td>
                                        <a href="javascript:;" data-state="-1" data-id="${result.id}" class="changeStateBtn">拒绝</a>
                                    </td>
                                    <td>
                                        <a href="javascript:;" class="lookBtn" data-id="${result.id}">查看文章</a>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
            <#include "../common/page.ftl"/>
        </div>
    </div>
</div>
<div class="modal fade" id="contentModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">文章内容</h4>
            </div>
            <div class="modal-body" id="showModal">
            </div>
        </div>
    </div>
</div>
</body>
</html>