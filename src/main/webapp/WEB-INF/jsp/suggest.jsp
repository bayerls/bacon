<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Select second dataset</title>

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
        <li class="active">2. Suggest</li>
        <li>3. Merge</li>
        <li>4. Store</li>
        <li>5. Done</li>
    </ol>

    <div class="row">
        <div class="col-lg-5">

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Selected Dataset:</h3>
                </div>
                <div class="panel-body">
                    <strong>Label:</strong> <c:out value="${cubeInfo.label}"/><br/>
                    <strong>Description:</strong> <c:out value=" ${cubeInfo.description}"/><br/>
                    <span class="badge"><c:out value="${cubeInfo.datasetStructureDefinition.dimensions.size()}"/></span><strong> Dimensions:</strong>
                    <ul>
                        <c:forEach items="${cubeInfo.datasetStructureDefinition.dimensions}" var="comp">
                            <li>${comp.label}</li>
                        </c:forEach>
                    </ul>
                    <span class="badge"><c:out value="${cubeInfo.datasetStructureDefinition.measures.size()}"/></span><strong> Measures:</strong>
                    <ul>
                        <c:forEach items="${cubeInfo.datasetStructureDefinition.measures}" var="comp">
                            <li>${comp.label}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Select ranking algorithm:</h3>
                </div>

                <div class="panel-body">
                    <div class="well well-sm">
                        A ranking algorithm ist used to sort the list of available cubes on the right side. Change the current ranking algorithm with this drowpdown.
                        These two algorithms compare the dataset strucutre definitions:
                        <ul>
                            <li>Full structure: All components (dimensions and measures) are taken into account.</li>
                            <li>Partial structure: The similarity is based on the ratio of common dimensions.</li>
                        </ul>
                    </div>
                    <form method="GET" action="suggest">
                        <fieldset>
                            <div class="form-group">
                                <label for="compare">Current ranking algorithm:</label>
                                <select id="compare" name="compare" class="form-control">
                                    <option value="fullStructure" <c:if test="${compareAlgorithm == 'FULL_STRUCTURE'}">selected</c:if>>Full structure: Dimensions and Measures</option>
                                    <option value="dimensionsOnly" <c:if test="${compareAlgorithm == 'DIMENSIONS_ONLY'}">selected</c:if>>Partial structure: Dimensions</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <input name="auth" type="hidden" class="form-control" id="authInput" value="<c:out value="${auth}" />" />
                            </div>
                            <div class="form-group">
                                <input name="graph" type="hidden" class="form-control" id="graph" value="<c:out value="${cubeInfo.graph}" />" />
                            </div>
                            <button type="submit" class="btn btn-primary pull-right">Sort again!</button>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-lg-7">

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Select a second dataset to start the merging process:</h3>
                </div>
                <div class="panel-body">
                    <div class="well well-sm">
                        The similarity is computed with the ranking algorithm. All available cubes are sorted by this similarity in the list below. <strong>Similarity:</strong> <span class="label label-success">Equal</span> <span class="label label-warning">Similar</span> <span class="label label-default">Related</span> <span class="label label-danger">Disjunct</span>
                    </div>

                    <br/>
                    <form method="GET" action="merge">
                        <fieldset>
                            <input name="g1" type="hidden" class="form-control" value="${cubeInfo.graph}" >
                            <input name="auth" type="hidden" class="form-control" value="<c:out value="${auth}" />"/>

                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary btn-large">Merge!</button>
                            </div>

                            <c:set var="firstItem" value="true"/>

                            <c:forEach items="${rankedItems}" var="ris">
                                <c:set var="score" value="${ris.score}"/>

                                <div class="radio">
                                    <label>
                                        <input type="radio" name="g2" value="${ris.datasetDescription.namedGraph}" <c:if test="${firstItem == true}">checked</c:if>>
                                        <c:set var="firstItem" value="false"/>
                                        <c:choose>
                                            <c:when test="${score == 1}">
                                                <span class="label label-success"><c:out value="${ris.score}"/></span>
                                            </c:when>
                                            <c:when test="${score >= 0.33}">
                                                <span class="label label-warning"><c:out value="${ris.score}"/></span>
                                            </c:when>
                                            <c:when test="${score > 0}">
                                                <span class="label label-default"><c:out value="${ris.score}"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="label label-danger"><c:out value="${ris.score}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                        </input>
                                        <c:out value="${ris.datasetDescription.label}:  ${ris.datasetDescription.description}"/>
                                    </label>
                                </div>
                            </c:forEach>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>

    </div>





</div>

<script src="/cube-merging/resources/js/jquery-2.0.3.js"></script>
<script src="/cube-merging/resources/js/bootstrap.js"></script>
<script src="/cube-merging/resources/js/scripts.js"></script>


</body>
</html>