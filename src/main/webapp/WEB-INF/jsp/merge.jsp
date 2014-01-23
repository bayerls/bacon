<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Merge preview</title>

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
        <li class="active">3. Merge</li>
        <li>4. Store</li>
        <li>5. Done</li>
    </ol>

    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Resulting <strong>Dataset Structure Definition:</strong></h3>
                </div>
                <div class="panel-body">
                    <div class="well well-sm">These components will be part of the resulting cube. Add additional components by merging unmatched components.</div>
                    <span class="badge"><c:out value="${mergedDSD.matchingDimensions.size()}"/></span><strong> Dimensions:</strong>
                    <ul>
                        <c:forEach items="${mergedDSD.matchingDimensions}" var="matchDims">
                            <c:choose>
                                <c:when test="${empty matchDims.resource}">
                                    <li><span class="label label-info">${matchDims.component1.label}</span> ${matchDims.component1.resource} </li>
                                </c:when>
                                <c:otherwise>
                                    <li><span class="label label-info">${matchDims.label}</span> ${matchDims.resource} </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                    <span class="badge"><c:out value="${mergedDSD.matchingMeasures.size()}"/></span><strong> Measures:</strong>
                    <ul>
                        <c:forEach items="${mergedDSD.matchingMeasures}" var="matchMeas">
                            <c:choose>
                                <c:when test="${empty matchMeas.resource}">
                                    <li><span class="label label-warning">${matchMeas.component1.label}</span> ${matchMeas.component1.resource} </li>
                                </c:when>
                                <c:otherwise>
                                    <li><span class="label label-warning">${matchMeas.label}</span> ${matchMeas.resource}  </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Resulting <strong>Observations:</strong></h3>
                </div>
                <div class="panel-body">
                    <div class="well well-sm">An overlap indicates that there are observations with equal dimension values in both cubes. Merge dimensions, to resolve this potential key integrity violation.</div>
                    <div class="row">
                        <div class="col-lg-6">
                            Observations:
                            <ul>
                                <li>Cube 1: <span class="badge"><c:out value="${log.obsC1}"/></span></li>
                                <li>Cube 2: <span class="badge"><c:out value="${log.obsC2}"/></span></li>
                                <li>Overlapping total: <span class="badge"><c:out value="${log.obsMerged}"/></span></li>
                                <li>Overlapping %: <span class="badge"><c:out value="${log.percentageOverlap}%"/></span></li>
                                <li><strong>Resulting:</strong> <span class="badge"><c:out value="${log.obsC1 + log.obsC2 - log.obsMerged}"/></span></li>
                            </ul>
                        </div>
                        <div class="col-lg-6">
                            <c:out value="${svg}" escapeXml="false" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <hr/>


    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
        <li class="active"><a href="#addDimension" data-toggle="tab">3a) Add Dimension</a></li>
        <li><a href="#mergeComponents" data-toggle="tab">3b) Merge Components</a></li>
        <li><a href="#finalizeMergingProcess" data-toggle="tab">3c) Finalize merging process</a></li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">
        <div class="tab-pane active" id="addDimension">
        <div class="row">
            <div class="col-lg-6">
                <br/>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Unmatched components - Cube 1: ${g1Desc.label}</h3>
                    </div>
                    <div class="panel-body">
                        <ul>
                            <c:forEach items="${mergedDSD.dimensions1}" var="dims">
                                <li><span class="label label-info">${dims.label}</span> <span class="concept1">${dims.resource}</span>  <c:if test="${not empty dims.value}">(${dims.value})</c:if></li>
                            </c:forEach>
                            <c:forEach items="${mergedDSD.measures1}" var="meas">
                                <li><span class="label label-warning">${meas.label}</span> <span class="concept1">${meas.resource}</span></li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Unmatched components - Cube 2: ${g2Desc.label}</h3>
                    </div>
                    <div class="panel-body">
                        <ul>
                            <c:forEach items="${mergedDSD.dimensions2}" var="dims">
                                <li><span class="label label-info">${dims.label}</span> <span class="concept2">${dims.resource}</span>  <c:if test="${not empty dims.value}">(${dims.value})</c:if></li>
                            </c:forEach>
                            <c:forEach items="${mergedDSD.measures2}" var="meas">
                                <li><span class="label label-warning">${meas.label}</span> <span class="concept2">${meas.resource}</span></li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <h2>Add dimensions to the graphs:</h2>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Add an additional dimension:</h3>
                    </div>
                    <div class="panel-body">
                        <div class="well well-sm">A cube is missing a dimension? Add it together with a value for every observation.</div>
                        <form method="get" action="add-dimension" class="form-horizontal">
                            <div class="form-group popGraphAdd">
                                <label for="graphSelect" class="col-lg-2 control-label">Graph:</label>
                                <div class="col-lg-10">
                                    <select name="graph" class="form-control" id="graphSelect">
                                        <option value="${mergedDSD.g1}">Cube 1: ${g1Desc.label}</option>
                                        <option value="${mergedDSD.g2}">Cube 2: ${g2Desc.label}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group popLabelAdd">
                                <label for="addLabel" class="col-lg-2 control-label">Label:</label>
                                <div class="col-lg-6" id="addLabel">
                                    <input id="addLabelInput" name="label" type="text" class="form-control" placeholder="label" required>
                                </div>
                                <div class="col-lg-4">
                                    <a id="disambiguate" class="btn btn-default"><span class="glyphicon glyphicon-fire"></span> Disambiguate!</a>
                                </div>
                            </div>

                            <div class="form-group popUrlAdd">
                                <label for="addUrl" class="col-lg-2 control-label">Resource:</label>
                                <div class="col-lg-10" id="addUrl">
                                    <input id="addResourceInput" name="resource" type="text" class="form-control" placeholder="url" required>
                                </div>
                            </div>
                            <div class="form-group popValueAdd">
                                <label for="addValue" class="col-lg-2 control-label">Value:</label>
                                <div class="col-lg-10" id="addValue">
                                    <input name="value" type="text" class="form-control" placeholder="value" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-lg-12">
                                    <button type="submit" class="btn btn-default btn-small pull-right"><span class="glyphicon glyphicon-fire"></span> Add Dimension</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
        <div class="tab-pane" id="mergeComponents">
            <form method="get" action="merge-component" class="form-horizontal">
                <fieldset>
                    <div class="row">
                        <div class="col-lg-6">
                            <br/>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Unmatched components - Cube 1: ${g1Desc.label}</h3>
                                </div>
                                <div class="panel-body">
                                    <c:set var="firstItem1" value="true"/>
                                    <c:forEach items="${mergedDSD.dimensions1}" var="dims">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="compG1" value="${dims.resource}" <c:if test="${firstItem1 == true}">checked</c:if>>
                                                <c:set var="firstItem1" value="false"/>
                                                <span class="label label-info">${dims.label}</span> ${dims.resource} <c:if test="${not empty dims.value}">(${dims.value})</c:if>
                                            </label>
                                        </div>
                                    </c:forEach>
                                    <c:forEach items="${mergedDSD.measures1}" var="meas">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="compG1" value="${meas.resource}">
                                                <span class="label label-warning">${meas.label}</span> ${meas.resource}
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Unmatched components - Cube 2: ${g2Desc.label}</h3>
                                </div>
                                <div class="panel-body">
                                    <c:set var="firstItem2" value="true"/>
                                    <c:forEach items="${mergedDSD.dimensions2}" var="dims">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="compG2" value="${dims.resource}" <c:if test="${firstItem2 == true}">checked</c:if>>
                                                <c:set var="firstItem2" value="false"/>
                                                <span class="label label-info">${dims.label}</span> ${dims.resource}  <c:if test="${not empty dims.value}">(${dims.value})</c:if>
                                            </label>
                                        </div>
                                    </c:forEach>
                                    <c:forEach items="${mergedDSD.measures2}" var="meas">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="compG2" value="${meas.resource}">
                                                <span class="label label-warning">${meas.label}</span> ${meas.resource}
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <h2>Merge unmatched components: <a class="btn btn-primary" id="askBalloon">Ask Balloon</a></h2>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Merge components:</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="well well-sm">Merge two unmatched components to add them to the resulting cube. Maybe adding a dimension before is necessary?</div>
                                    <div class="form-group popLabelLeft">
                                        <label for="addLabelMerged" class="col-lg-2 control-label">Label:</label>
                                        <div class="col-lg-6" id="addLabelMerged">
                                            <input id="addLabelMergedInput" name="label" type="text" class="form-control" placeholder="label" required>
                                        </div>
                                        <div class="col-lg-4">
                                            <a id="disambiguateMerged" class="btn btn-default"><span class="glyphicon glyphicon-fire"></span> Disambiguate!</a>
                                        </div>
                                    </div>
                                    <div class="form-group popUrlLeft">
                                        <label for="mergedURL" class="col-lg-2 control-label">Resource:</label>
                                        <div class="col-lg-10">
                                            <input name="resource" type="text" class="form-control" id="mergedURL" placeholder="url" required>
                                        </div>
                                    </div>
                                    <div class="form-group popAddRange">
                                        <label for="r1" class="col-lg-2 control-label">Range:</label>
                                        <div class="col-lg-10">
                                            <select name="range" class="form-control" id="r1">
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeDimensionNominal">va:cubeDimensionNominal</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeDimensionOrdinal">va:cubeDimensionOrdinal</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeDimensionInterval">va:cubeDimensionInterval</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeDimensionRatio">va:cubeDimensionRatio</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationText">va:cubeObservationText</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationNumber">va:cubeObservationNumber</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationPercentage">va:cubeObservationPercentage</option>
                                                <option value="​http://code-research.eu/ontology/visual-analytics#cubeObservationCurrency">va:cubeObservationCurrency</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationTime">va:cubeObservationTime</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationDuration">va:cubeObservationDuration</option>
                                                <option value="http://code-research.eu/ontology/visual-analytics#cubeObservationBoolean">va:cubeObservationBoolean</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-lg-12">
                                            <button type="submit" class="btn btn-default btn-small pull-right"><span class="glyphicon glyphicon-fire"></span> Merge components</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <div class="tab-pane" id="finalizeMergingProcess">
            <br/>
            <div class="row">
                <div class="col-lg-4">
                </div>
                <div class="col-lg-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Finalize matching:</h3>
                        </div>
                        <div class="panel-body">
                            <div class="well well-sm">After adapting the components, proceed to the "store" step.</div>
                            <a class="btn btn-primary pull-right" href="prepare-store">Next step</a>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                </div>
            </div>
        </div>
    </div>
</div>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<div class="modal fade" id="modelDisambiguation" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Disambiguated concepts:</h4>
            </div>
            <div class="modal-body">
                <div class="well well-sm">Click on a suggested item to use it as the resource for the disambiguated label.</div>
                <img id="loaderDis" class="center-block" src="/cube-merging/resources/img/ajax-loader.gif"/>
                <ol id="disambiguatedSuggestions"></ol>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="modelSame" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Inferred sameAs relationships:</h4>
            </div>
            <div class="modal-body">
                <div class="well well-sm">A sameAs relationship between two components is a good indicator for merge candidates.</div>
                <img id="loaderSame" class="center-block" src="/cube-merging/resources/img/ajax-loader.gif"/>
                <ol id="sameSuggestions"></ol>
            </div>
        </div>
    </div>
</div>


<script src="/cube-merging/resources/js/jquery-2.0.3.js"></script>
<script src="/cube-merging/resources/js/bootstrap.js"></script>
<script src="/cube-merging/resources/js/scripts.js"></script>

</body>
</html>
