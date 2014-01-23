<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Merged</title>

    <link href="/cube-merging/resources/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="/cube-merging/resources/css/bootstrap-theme.css" rel="stylesheet" media="screen">
    <link href="/cube-merging/resources/css/custom.css" rel="stylesheet" media="screen">

</head>
<body>

<div class="container">

    <nav class="navbar navbar-default" role="navigation">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="select"><img src="resources/img/merge.png" style="height: 18px"/> Cube Merging</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="http://www.code-research.eu/">CODE Research</a></li>
            </ul>
        </div>
    </nav>

    <ol class="breadcrumb">
        <li>1. Select</li>
        <li>2. Suggest</li>
        <li>3. Merge</li>
        <li class="active">4. Store</li>
        <li>5. Done</li>
    </ol>


    <div class="col-lg-6">
        <h2>Store the merged graph:</h2>
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Finalize merge:</h3>
            </div>
            <div class="panel-body">
                <form action="finalize-merge" method="get" class="form-horizontal">
                    <div class="form-group">
                        <label for="label" class="col-lg-2 control-label">Label:</label>
                        <div class="col-lg-10">
                            <input name="label" type="text" class="form-control" id="label" placeholder="label" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-lg-2 control-label">Description:</label>
                        <div class="col-lg-10">
                            <input name="description" type="text" class="form-control" id="description" placeholder="description" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-12">
                            <p><button type="submit" class="btn btn-default btn-large pull-right"><span class="glyphicon glyphicon-fire"></span> Save!</button></p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

</div>

<script src="/cube-merging/resources/js/jquery-2.0.3.js"></script>
<script src="/cube-merging/resources/js/bootstrap.js"></script>
<script src="/cube-merging/resources/js/scripts.js"></script>

</body>
</html>
