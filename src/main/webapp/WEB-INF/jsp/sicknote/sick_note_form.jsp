<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="title"/></title>
    <%@include file="../include/header.jsp" %>

    <script src="<spring:url value='/js/datepicker.js' />" type="text/javascript" ></script>

    <script type="text/javascript">
        $(document).ready(function() {

            var regional = "${pageContext.request.locale.language}";

            createDatepickerInstanceForSickNote(regional);
            
        });
    </script>
    
</head>
<body>

<spring:url var="formUrlPrefix" value="/web"/>

<%@include file="../include/menu_header.jsp" %>

<div id="content">

    <div class="container_12">

        <div class="grid_12">

            <div class="overview-header">
                <legend>
                    <p>
                        <c:choose>
                            <c:when test="${sickNote.id == null}">
                                <spring:message code="sicknotes.new" /> 
                            </c:when>
                            <c:otherwise>
                                <spring:message code="sicknotes.edit" /> 
                            </c:otherwise>
                        </c:choose>
                    </p>
                </legend>
            </div>

            <c:choose>
                <c:when test="${sickNote.id == null}">
                    <c:set var="METHOD" value="POST" />
                    <c:set var="ACTION" value="${formUrlPrefix}/sicknote" />
                </c:when>
                <c:otherwise>
                    <c:set var="METHOD" value="PUT" />
                    <c:set var="ACTION" value="${formUrlPrefix}/sicknote/${sickNote.id}/edit" />
                </c:otherwise>
            </c:choose>
            
            <form:form method="${METHOD}" action="${ACTION}" modelAttribute="sickNote" class="form-horizontal form-sicknote">

                <div class="control-group">
                    <label class="control-label" for="employee"><spring:message code='staff'/></label>

                    <div class="controls">
                        <c:choose>
                            <c:when test="${sickNote.id == null}">
                                <form:select path="person" id="employee" cssErrorClass="error">
                                    <c:forEach items="${persons}" var="person">
                                        <form:option value="${person.id}">${person.firstName}&nbsp;${person.lastName}</form:option>
                                    </c:forEach>
                                </form:select>
                                <span class="help-inline"><form:errors path="person" cssClass="error"/></span>
                            </c:when>
                            <c:otherwise>
                                <form:hidden path="id" />
                                <form:hidden path="person" value="${sickNote.person.id}" />
                                <c:out value="${sickNote.person.firstName}" />&nbsp;<c:out value="${sickNote.person.lastName}" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="from"><spring:message code='From' /></label>

                    <div class="controls">
                        <form:input id="from" path="startDate" cssClass="input-medium" cssErrorClass="error input-medium" />
                        <span class="help-inline"><form:errors path="startDate" cssClass="error"/></span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="to"><spring:message code='To' /></label>

                    <div class="controls">
                        <form:input id="to" path="endDate" cssClass="input-medium" cssErrorClass="error input-medium" />
                        <span class="help-inline"><form:errors path="endDate" cssClass="error"/></span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label"><spring:message code='sicknotes.aub'/></label>
                    <div class="controls">
                        <form:radiobutton path="aubPresent" value="true" />&nbsp;<spring:message code='yes' />&nbsp;&nbsp;
                        <form:radiobutton path="aubPresent" value="false" />&nbsp;<spring:message code='no' />
                        <span class="help-inline"><form:errors path="aubPresent" cssClass="error"/></span>
                    </div>
                </div>
                
                <hr/>
                
                <div class="control-group">
                    <button class="btn" type="submit"><i class='icon-ok'></i>&nbsp;<spring:message code="save" /></button>
                    <a class="btn" href="${formUrlPrefix}/sicknote"><i class='icon-remove'></i>&nbsp;<spring:message code='cancel'/></a>
                </div>
            
            </form:form>
            
        </div>
        
    </div>
    
</div>    

</body>
</html>