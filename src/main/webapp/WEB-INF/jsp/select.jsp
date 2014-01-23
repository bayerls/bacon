<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Select first dataset</title>

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

    <div class="jumbotron">
        <h1><img src="resources/img/merge.png" style="height: 75px"/> Cube Merging</h1>
        <p>Use this prototype to merge <a href="http://www.w3.org/TR/vocab-data-cube/">RDF Data Cubes.</a></p>
    </div>

    <ol class="breadcrumb">
        <li class="active">1. Select</li>
        <li>2. Suggest</li>
        <li>3. Merge</li>
        <li>4. Store</li>
        <li>5. Done</li>
    </ol>

    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">Info:</h3>
        </div>
        <div class="panel-body">
            <ul>
                <li>If you directly access the merger, add the "auth" GET-parameter to access your cubes. (.../cube-merging/select?auth=123456)</li>
                <li>The available dataset are pulled from the bigdata repository on zaire</li>
                <li>Use this prototype to merge datasets previously imported with the <a href="http://zaire.dimis.fim.uni-passau.de:8181/code-server/demo/dataextraction">Data Extractor</a></li>
            </ul>
        </div>
    </div>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Select a dataset to start the merging process:</h3>
        </div>

        <div class="panel-body">
            <form method="GET" action="suggest">
                <fieldset>
                    <div class="form-group">
                        <select name="graph" class="form-control">
                            <c:forEach items="${datasetDescriptions}" var="dds">
                                <option value="${dds.namedGraph}"><c:out value="${dds.label}"/> - <c:out value="${dds.description}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="authInput">Your ID:</label>
                        <input name="auth" type="text" class="form-control" id="authInput" value="<c:out value="${auth}" />" />
                    </div>
                    <button type="submit" class="btn btn-primary pull-right">Submit</button>
                </fieldset>
            </form>
        </div>
    </div>
</div>

<br/>
<br/>
<br/>
<br/>
<br/>



<script src="/cube-merging/resources/js/jquery-2.0.3.js"></script>
<script src="/cube-merging/resources/js/bootstrap.js"></script>
<script src="/cube-merging/resources/js/scripts.js"></script>

</body>
</html>